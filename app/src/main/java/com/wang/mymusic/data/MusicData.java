package com.wang.mymusic.data;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wang.mymusic.activity.MusicPlay;
import com.wang.mymusic.activity.PlayMusic;
import com.wang.mymusic.model.HttpApi;
import com.wang.mymusic.model.RecommentBean;
import com.wang.mymusic.model.SongBean;
import com.wang.mymusic.model.SongMusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static android.content.ContentValues.TAG;

/**
 * MyMusic
 * Created by wang on 2017.4.13.
 */

public class MusicData {
    private SongMusic mSongMusic;
    private Retrofit mRetrofit;
    private MusicPlay.PlayData mPlayData;
    private PlayMusic.addLrc mlrc;

    public void setMlrc(PlayMusic.addLrc mlrc) {
        this.mlrc = mlrc;
    }

    private static MusicData mData=new MusicData();
    private MusicData(){}
    public HttpApi getHttp(){
        if(mRetrofit==null){
            mRetrofit=new Retrofit.Builder()
                    .baseUrl("http://tingapi.ting.baidu.com/")
                    .client(getNewClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(HttpApi.class);
    }

    //获取音乐播放列表
    public void getListMusic(int type, String size, int off){
        Call<RecommentBean> mCall=getHttp().getrecomment(type,size,off);
        mCall.enqueue(new Callback<RecommentBean>() {
            @Override
            public void onResponse(Call<RecommentBean> call, Response<RecommentBean> response) {
                mSongMusic.setAdapter(response.body().getSong_list());
            }

            @Override
            public void onFailure(Call<RecommentBean> call, Throwable t) {

            }
        });
    }

  //获取播放数据源
    public void getListPlay(final int type, String size, int off){
        Call<RecommentBean> mCall=getHttp().getrecomment(type,size,off);
        mCall.enqueue(new Callback<RecommentBean>() {
            @Override
            public void onResponse(Call<RecommentBean> call, Response<RecommentBean> response) {
                mPlayData.setPlayData(response.body(),type);
            }

            @Override
            public void onFailure(Call<RecommentBean> call, Throwable t) {

            }
        });
    }
    //设置接口实现类的对象
    public void setSongServer(SongMusic song){
        mSongMusic=song;
    }

    public void setPlayData(MusicPlay.PlayData playData){
        mPlayData=playData;
    }

    public static MusicData getMusicData(){
        return mData;
    }

    //请求歌词文件
    public void getlrc(String url, final File file){
        if(file.exists()&&file.length()!=0){
            mlrc.setLrc(file);
        }else {
        Retrofit rLrc=new Retrofit.Builder()
                .baseUrl("http://musicdata.baidu.com/data2/lrc/13918580/13918580.lrc/")
                .client(getNewClient())
                .build();
        HttpApi hLrc=rLrc.create(HttpApi.class);
        Call<ResponseBody> cLrc=hLrc.downlrc(url);
        cLrc.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: 文件请求成功");
                    lrcFile(response.body(),file);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        }
    }
    //生成file文件路径
    public File getPath(Context context,String title,String author,String format){
        //create a file
        File filedirectory;
        File file;
        String name=title+"_"+author+format;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            filedirectory=new File(context.getExternalCacheDir()+File.separator+format.substring(1));
            file=new File(filedirectory+File.separator+name);
            Log.d(TAG, "getPath: 外部存储");
        }else {
            filedirectory=new File(context.getCacheDir()+File.separator+"lrc");
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
        return file;
    }
    //保存歌词文件到磁盘
   private void lrcFile(final ResponseBody respon, final File file) {
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in=respon.byteStream();
                try {
                    OutputStream out=new FileOutputStream(file);
                    byte[] b=new byte[4096];
                    int fileread;
                    while ((fileread=in.read(b))!=-1){
                        out.write(b,0,fileread);
                        out.flush();
                    }
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message ms=new Message();
                ms.obj=file;
                handler.sendMessage(ms);
            }
        });
       th.start();
    }
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            mlrc.setLrc((File) message.obj);
            Log.d(TAG, "handleMessage: "+message.obj);
            return true;
        }
    });

    //获取播放地址并播放
    public void getSong(String songid, final Boolean isdown){
        Call<SongBean> song=getHttp().getsong(songid);
        song.enqueue(new Callback<SongBean>() {
            @Override
            public void onResponse(Call<SongBean> call, Response<SongBean> response) {
                SongBean songBit=response.body();
                mPlayData.songId(songBit,isdown);
            }
            @Override
            public void onFailure(Call<SongBean> call, Throwable t) {

            }
        });
    }

    //获取本地歌曲


    private static OkHttpClient getNewClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(DEFAULT_KEYS_DIALER, TimeUnit.SECONDS)
                .build();
    }
}
