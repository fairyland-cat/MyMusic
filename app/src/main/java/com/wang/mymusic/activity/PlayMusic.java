package com.wang.mymusic.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.mymusic.R;
import com.wang.mymusic.data.MusicData;
import com.wang.mymusic.util.Assist;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhengken.lyricview.LyricView;

public class PlayMusic extends BasicActivity {
    private ImageView mImage;
    private LyricView lyrSong;
    private ImageButton mLast;
    private ImageButton mPlay;
    private ImageButton mNext;
    private ImageButton mMode;
    private Boolean isPlaying=false;
    private static int playmode=1;
    private AnimatorSet aset;
    private SeekBar mSeekBar;
    private TextView minTime,maxTime,mTitle,mAuthor;
    private MusicData musicDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        findView();
        Glide.with(PlayMusic.this).load(R.drawable.head).bitmapTransform(new CropCircleTransformation(this))
                .into(mImage);
        aset= (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.revolve);
        aset.setTarget(mImage);
        aset.setInterpolator(new LinearInterpolator());
        //绑定音乐播放服务
        bindMusicPlay();
        //音乐播放点击事件
        mControl mC=new mControl();
        mMode.setOnClickListener(mC);
        mPlay.setOnClickListener(mC);
        mLast.setOnClickListener(mC);
        mNext.setOnClickListener(mC);
        //进度条拖动事件监听
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    musicPlay.setPlayPosition(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //歌词滚动监听
        lyrSong.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
            @Override
            public void onPlayerClicked(long l, String s) {
                musicPlay.setPlayPosition((int) l);
                lyrSong.setCurrentTimeMillis(l);
            }
        });

        musicDate=MusicData.getMusicData();
    }

    //更新播放状态与ui
    public void upDatePlay(Bundle bundle){
        if (bundle.getString("pic")!=null){
            Glide.with(PlayMusic.this).load(bundle.getString("pic")).bitmapTransform(new CropCircleTransformation(this))
                    .into(mImage);
        }
        if(bundle.getBoolean("isPlay")){
            mPlay.setImageResource(R.drawable.playsong);
            aset.start();
            if (bundle.getString("lrc")!=null){
                File file=musicDate.getPath(this,bundle.getString("title"),bundle.getString("author"),".lrc");
                musicDate.getlrc(bundle.getString("lrc"),file);
            }else {
                lyrSong.setLyricFile(null);
            }
            isPlaying=true;
        }
        maxTime.setText(bundle.getString("time"));
        mSeekBar.setMax(bundle.getInt("maxTime"));
        mAuthor.setText(bundle.getString("author"));
        mTitle.setText(bundle.getString("title")+"_");
    }
    @Override
    public void changePlay(Bundle bundle) {
        upDatePlay(bundle);
    }
    //更新进度条
    @Override
    public void getProgress(int progress) {
        mSeekBar.setProgress(progress);
        handler.sendEmptyMessage(progress);
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            minTime.setText(Assist.getAssist().getPlayTime(message.what));
            lyrSong.setCurrentTimeMillis(message.what);
            return true;
        }
    });

    public void findView(){
        mImage= (ImageView) findViewById(R.id.cirimg);
        lyrSong= (LyricView) findViewById(R.id.lyrsong);
        mPlay= (ImageButton) findViewById(R.id.play);
        mLast= (ImageButton) findViewById(R.id.last);
        mNext= (ImageButton) findViewById(R.id.next);
        mMode= (ImageButton) findViewById(R.id.moder);
        mSeekBar= (SeekBar) findViewById(R.id.songseek);
        minTime= (TextView) findViewById(R.id.minTime);
        maxTime= (TextView) findViewById(R.id.maxTime);
        mTitle= (TextView) findViewById(R.id.title);
        mAuthor= (TextView) findViewById(R.id.author);
    }


    public class mControl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.last:
                    musicPlay.LastPlay();
                    break;
                case R.id.play:
                    if(musicPlay.isPlay()){
                        mPlay.setImageResource(R.drawable.pause);
                        musicPlay.SongPause();
                        isPlaying=true;
                        aset.pause();
                    }else if(isPlaying){
                        mPlay.setImageResource(R.drawable.playsong);
                        musicPlay.StarPlay(false);
                        aset.resume();
                    }else {
                        musicPlay.StarPlay(true);
                    }
                    break;
                case R.id.next:
                    musicPlay.NextPlay();
                    break;
                case R.id.moder:
                    switch (playmode){
                        case 1:
                            mMode.setImageResource(R.drawable.random);
                            playmode=2;
                            break;
                        case 2:
                            mMode.setImageResource(R.drawable.oneround);
                            playmode=3;
                            break;
                        case 3:
                            mMode.setImageResource(R.drawable.round);
                            playmode=1;
                            break;
                    }
                    Intent modeintent=new Intent("com.wang.mymusic.PLAYMODE");
                    modeintent.putExtra("mode",playmode);
                    sendBroadcast(modeintent);
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (playmode){
            case 1:
                mMode.setImageResource(R.drawable.round);
                break;
            case 2:
                mMode.setImageResource(R.drawable.random);
                break;
            case 3:
                mMode.setImageResource(R.drawable.oneround);
                break;
        }
        musicDate.setMlrc(new addLrc() {
            @Override
            public void setLrc(File file) {
                lyrSong.setLyricFile(file);
            }
        });
    }

    public static class PlayMode extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            playmode=intent.getIntExtra("mode",1);
        }
    }

    //加载歌词文件的接口
    public interface addLrc{
        void setLrc(File file);
    }
}
