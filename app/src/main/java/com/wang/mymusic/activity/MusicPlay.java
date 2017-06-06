package com.wang.mymusic.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.wang.mymusic.R;
import com.wang.mymusic.data.MusicData;
import com.wang.mymusic.data.SqlUtil;
import com.wang.mymusic.model.RecommentBean;
import com.wang.mymusic.model.SongBean;
import com.wang.mymusic.model.SongData;
import com.wang.mymusic.model.SongLocalBean;
import com.wang.mymusic.util.Assist;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by wang on 2017/2/20.
 */

public class MusicPlay extends Service {

    private MediaPlayer mPlay;
    //当前播放的列表
    private RecommentBean.SongListBean mSongList;
    private int currentPosition;
    //ui更新接口对象
    private uiUpdate Change;
    private MusicData mdata;
    //播放循环模式
    private static int currentMode;
    //播放的歌曲类型
    private int mType;
    //状态值保存
    private SharedPreferences songConfig;
    private SongBean.SonginfoBean mSong;
    private SqlUtil sqlUtil;
    SparseArray<List<RecommentBean.SongListBean>> songlist=new SparseArray<>();
    private List<SongData> savelist;
    private List<SongLocalBean> localist;
    private RemoteViews mRemoteViews;
    private final int NOTIFICATION_ID=0X1;
    private NotificationCompat.Builder mBuilder;
    @Override
    public void onCreate() {
        super.onCreate();
        if(mPlay==null){
            mPlay=new MediaPlayer();
        }
        mPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            switch (currentMode){
                case 1: NextPlay();
                    break;
                case 2: SongPlay(playrandom(songlist.get(mType).size()),mType);
                    break;
                case 3: SongPlay(currentPosition,mType);
                    break;
            }
            }
        });
        songConfig=this.getSharedPreferences("songConfig",MODE_PRIVATE);
        currentPosition=songConfig.getInt("position",0);
        mType=songConfig.getInt("type",2);
        mdata=MusicData.getMusicData();
        mdata.getListPlay(2,"",0);
        mdata.getListPlay(22,"",0);
        mdata.getListPlay(21,"",0);
        mdata.setPlayData(new PlayData() {
            @Override
            public void setPlayData(RecommentBean songList,int type) {
                songlist.put(type,songList.getSong_list());
                getSongDate();
            }

            @Override
            public void songId(SongBean song, Boolean isdown) {
                if (isdown){
                    File file;
                    File filedirectory;
                    String name=song.getSonginfo().getTitle()+"_"+song.getSonginfo().getAuthor()+"."+song.getBitrate().getFile_extension();
                    if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                            || !Environment.isExternalStorageRemovable()){
                        filedirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                        file=new File(filedirectory,name);
                        Log.d(TAG, "getPath: 外部存储");
                    }else {
                        filedirectory=new File(MusicPlay.this.getCacheDir()+File.separator+"lrc");
                        file=new File(filedirectory+File.separator+name);
                    }
                    if (!filedirectory.exists()){
                        filedirectory.mkdir();
                    }
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mdata.getlrc(song.getBitrate().getFile_link(),file);
                }else {
                    mSong=song.getSonginfo();
                    String songUri=song.getBitrate().getFile_link();
                    initNotification(mSong.getTitle(),mSong.getAuthor(),mSong.getPic_small(),true);
                    if (songUri.length()!=0){
                    setSongPlay(songUri);
                }else {NextPlay();}
                }
            }
        });

        sqlUtil=new SqlUtil(MusicPlay.this);
        sqlUtil.getsqlData();
        savelist=sqlUtil.getAllData();

        localist=Assist.getAssist().getAllList(this);
        initRemoteView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction()!=null&&intent.getAction().equals("com.wang.mymusic.notification")){
            switch (intent.getIntExtra("oper",0)){
                case 0:
                    if (mPlay.isPlaying()){
                        SongPause();
                        initNotification(null,null,null,false);
                    }else {
                        StarPlay(false);
                        initNotification(null,null,null,true);
                    }
                    break;
                case 1:
                    LastPlay();
                    break;
                case 2:
                    NextPlay();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public class musicBind extends Binder{
        public MusicPlay getMusicPlay(){
            return MusicPlay.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new musicBind();
    }

    //加载资源并播放
    public void setSongPlay(String song){
        mPlay.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlay.reset();
        try {
            mPlay.setDataSource(song);
            mPlay.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlay.start();
        sendUIDate();
        sendProgress();
    }

    public void sendProgress(){
        Thread th=new Thread(new Runnable() {
         @Override
         public void run() {
             while (mPlay.isPlaying()){
                 if(Change!=null){
                     Change.upProgress(getPlayTime());
                 }
             }
         }
     });
        th.start();
    }
    //获取当前播放时间
    public int getPlayTime(){
        if(mPlay!=null){
            return mPlay.getCurrentPosition();
        }
        return 0;
    }
    //发送ui状态数据
    public void sendUIDate(){
        String mtitle=null,mauthor=null,mpic=null;
        Bundle bundle=new Bundle();
        switch (mType){
            case 1:
                SongData song=savelist.get(currentPosition);
                mtitle=song.getTitle();
                mauthor=song.getAuthor();
                mpic=song.getPic_small();
                if(mSong!=null){
                    bundle.putString("lrc",mSong.getLrclink());
                    bundle.putInt("maxTime",mSong.getFile_duration()*1000);
                    bundle.putString("time", Assist.getAssist().getSongTime(mSong.getFile_duration()));
                }
                break;
            case 0:
                mauthor=localist.get(currentPosition).getArtist();
                mtitle=localist.get(currentPosition).getTitle();
                bundle.putInt("maxTime",localist.get(currentPosition).getDuration());
                bundle.putString("time",Assist.getAssist().getPlayTime(localist.get(currentPosition).getDuration()));
                break;
            default:
                if(mSongList!=null){
                    mauthor=mSongList.getAuthor();
                    mpic=mSongList.getPic_small();
                    mtitle=mSongList.getTitle();
                }
                if(mSong!=null){
                    bundle.putString("lrc",mSong.getLrclink());
                    bundle.putInt("maxTime",mSong.getFile_duration()*1000);
                    bundle.putString("time", Assist.getAssist().getSongTime(mSong.getFile_duration()));
                }
                break;
        }
            bundle.putString("title",mtitle);
            bundle.putString("author",mauthor);
            bundle.putString("pic",mpic);
            bundle.putBoolean("isPlay",mPlay.isPlaying());
            Change.updatePlay(bundle);
    }

    //拖动进度条后，设置音乐播放位置
    public void setPlayPosition(int position){
        if(mPlay!=null){
            mPlay.seekTo(position);
        }
    }

    //获取歌曲信息
    public void getSongDate(){
        if(songlist.get(mType)!=null){
            mSongList=songlist.get(mType).get(currentPosition);
            sendUIDate();
        }
    }

    //播放音乐
    public void SongPlay(int position,int type){
        mType=type;
        currentPosition=position;
        switch (type){
            case 1:
                mdata.getSong(savelist.get(position).getSong_id(),false);
                break;
            case 0:
                setSongPlay(localist.get(position).getData());
                initNotification(localist.get(position).getTitle(),localist.get(position).getArtist(),null,true);
                break;
            default:
                getSongDate();
                mdata.getSong(mSongList.getSong_id(),false);
                break;
        }

        SharedPreferences.Editor ed=songConfig.edit();
        ed.putInt("position",currentPosition);
        ed.putInt("type",mType);
        ed.apply();
    }
    //暂停
    public void SongPause(){
        mPlay.pause();
    }
    //下一首
    public void NextPlay(){
        switch (mType){
            case 1:
                if(currentPosition==savelist.size()-1){
                    currentPosition=(-1);
                }
                break;
            case 0:
                if (currentPosition==localist.size()-1){
                    currentPosition=(-1);
                }
                break;
            default:
                if(currentPosition==songlist.get(mType).size()-1){
                    currentPosition=(-1);
                }
                break;
        }
        currentPosition++;
        SongPlay(currentPosition,mType);
    }
    //上一首
    public void LastPlay(){
        if(currentPosition==0){
            switch (mType){
                case 1:
                    currentPosition=savelist.size()-1;
                    break;
                case 0:
                    currentPosition=localist.size()-1;
                    break;
                default:
                    currentPosition=songlist.get(mType).size()-1;
                    break;
            }
        }
        currentPosition--;
        SongPlay(currentPosition,mType);
    }
    //播放暂停的音乐
    public void StarPlay(Boolean isfirst){
        if(isfirst){
            SongPlay(currentPosition,mType);
        }else if(mPlay!=null&& !mPlay.isPlaying()){
            mPlay.start();
        }
    }
    //判断是否正在播放音乐
    public Boolean isPlay(){
        return mPlay.isPlaying();
    }
    //下载歌曲
    public void downSong(int type,int position){
        RecommentBean.SongListBean musicSong=songlist.get(type).get(position);
        mdata.getSong(musicSong.getSong_id(),true);
    }
    //收藏歌曲
    public void saveSong(int type,int position){
        RecommentBean.SongListBean musicSong=songlist.get(type).get(position);
        sqlUtil.add(new SongData(musicSong.getSong_id(),musicSong.getTitle(),musicSong.getAuthor(),musicSong.getPic_small()));
    }
    //取消收藏
    public void cancelSong(String id){
        sqlUtil.deleter(id);
    }
    //获取随机数
    private int playrandom(int size){
        return (int)(Math.random()*size);
    }

    public interface PlayData{
        void setPlayData(RecommentBean songList,int type);
        void songId(SongBean song,Boolean isdown);
    }

    public static class PlayMode extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            currentMode=intent.getIntExtra("mode",1);
        }
    }

    public void setChange(uiUpdate change) {
        Change = change;
    }

    public interface uiUpdate{
        void updatePlay(Bundle bundle);
        void upProgress(int progress);
    }

    //创建一个RemoteView实例
    private void initRemoteView() {
        //创建一个RemoteView实例
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.music_play_notification);

        //实例化一个指向MusicService的intent
        Intent intent = new Intent(this, MusicPlay.class);
        intent.setAction("com.wang.mymusic.notification");

        //设置play按钮的点击事件
        intent.putExtra("oper", 0);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.play, pendingIntent);

        //设置next按钮的点击事件
        intent.putExtra("oper", 1);
        pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.next, pendingIntent);

        //设置prev按钮的点击事件
        intent.putExtra("oper", 2);
        pendingIntent = PendingIntent.getService(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.last, pendingIntent);

    }
    //使用RemoteView实例创建Nitification
    private void initNotification(@Nullable String title,@Nullable String author,@Nullable String img, Boolean isPlaying) {

        //实例化一个Builder
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.local);
        mBuilder.setContentTitle("My notification")
                .setContentText("Hello World!");
        //将remoteView设置进去
        mBuilder.setContent(mRemoteViews);
        //设置RemoteViews的属性值
        if (title!=null||author!=null||img!=null){
            mRemoteViews.setTextViewText(R.id.songtitle,title);
            mRemoteViews.setTextViewText(R.id.songauthor,author);
            NotificationTarget mTarget=new NotificationTarget(this,mRemoteViews,R.id.songicon,mBuilder.build(),NOTIFICATION_ID);
            Glide.with(this).load(img).asBitmap().into(mTarget);
        }
        if (isPlaying){
            mRemoteViews.setImageViewResource(R.id.play,R.drawable.playsong);
        }else {
            mRemoteViews.setImageViewResource(R.id.play,R.drawable.pause);
        }

        //获取NotificationManager实例
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sqlUtil.closeData();
    }
}
