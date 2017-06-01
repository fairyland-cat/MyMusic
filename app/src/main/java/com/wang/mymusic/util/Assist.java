package com.wang.mymusic.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.wang.mymusic.model.SongLocalBean;

import java.util.ArrayList;
import java.util.List;

/**
 * MyMusic
 * Created by wang on 2017.5.16.
 */

public class Assist {
    private Assist() {
    }
    private static Assist mTools=new Assist();

    public static Assist getAssist(){
        return mTools;
    }

    public String getSongTime(int milli){
        String min=milli/60+"";
        String sec=milli%60+"";
        if(min.length()<2){
            min="0"+min;
        }
        if(sec.length()<2){
            sec="0"+sec;
        }
        return min+":"+sec;
    }
    public String getPlayTime(int milli){
        String min=milli/(1000*60)+"";
        String sec=milli%(1000*60)/1000+"";
        if(min.length()<2){
            min="0"+min;
        }
        if(sec.length()<2){
            sec="0"+sec;
        }
        return min+":"+sec;
    }
    //获取指定数目的本地音乐
    public List<SongLocalBean> getLimitList(Context context,int star,int end){
        ContentResolver mLocal=context.getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor song=mLocal.query(uri,new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.DATA},null,null,"_data LIMIT "+star+","+end);
        List<SongLocalBean> mlist=new ArrayList<>();
        assert song != null;
        while (song.moveToNext()){
            String title=song.getString(song.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=song.getString(song.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            int duration=song.getInt(song.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int ismusic=song.getInt(song.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            String data=song.getString(song.getColumnIndex(MediaStore.Audio.Media.DATA));
            mlist.add(new SongLocalBean(ismusic,duration,title,data,artist));
        }
        song.close();
        return mlist;
    }
    //获取全部的本地音乐
    public List<SongLocalBean> getAllList(Context context){
        ContentResolver mLocal=context.getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor song=mLocal.query(uri,new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.DATA},null,null,"_data");
        List<SongLocalBean> mlist=new ArrayList<>();
        assert song != null;
        while (song.moveToNext()){
            String title=song.getString(song.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=song.getString(song.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            int duration=song.getInt(song.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int ismusic=song.getInt(song.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            String data=song.getString(song.getColumnIndex(MediaStore.Audio.Media.DATA));
            mlist.add(new SongLocalBean(ismusic,duration,title,data,artist));
        }
        song.close();
        return mlist;
    }
}
