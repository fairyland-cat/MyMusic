package com.wang.mymusic.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wang.mymusic.model.SongData;

import java.util.ArrayList;
import java.util.List;

/**
 * MyMusic
 * Created by wang on 2017.5.22.
 */

public class SqlUtil{
    private SQLiteDatabase sqlbase;
    private Context mContext;
    private SqliteHelp help;
    public SqlUtil(Context context){mContext=context;}

    private class SqliteHelp extends SQLiteOpenHelper{

        public SqliteHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql="CREATE TABLE save (songid VARCHAR(10) NOT NULL PRIMARY KEY,title VARCHAR(30)," +
                    "author VARCHAR(20),pic VARCHAR(300))";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
    //获取数据库操作实例
    public void getsqlData(){
        help=new SqliteHelp(mContext,"save.db",null,1);
        sqlbase=help.getWritableDatabase();
    }
    //关闭数据库
    public void closeData(){
        if (sqlbase!=null){
            sqlbase.close();
        }
    }
    //添加一条记录
    public void add(SongData song){
        sqlbase.execSQL("INSERT INTO save (songid,title,author,pic) VALUES (?,?,?,?)",new String[]{
                song.getSong_id(),song.getTitle(),song.getAuthor(),song.getPic_small()
        });
    }
    //删除一条记录
    public void deleter(String id){
        sqlbase.execSQL("DELETE FROM save WHERE songid=?",new String[]{id});
    }
    //查询所有的数据
    public List<SongData> getAllData(){
        Cursor cursor=sqlbase.rawQuery("SELECT * FROM save ORDER BY songid ASC",null);
        List<SongData> song=new ArrayList<>();
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("songid"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String author=cursor.getString(cursor.getColumnIndex("author"));
            String image=cursor.getString(cursor.getColumnIndex("pic"));
            song.add(new SongData(id,title,author,image));
        }
        cursor.close();
        return song;
    }
    //数据分页
    public List<SongData> getOnePage(int star,int end){
        Cursor cursor=sqlbase.rawQuery("SELECT * FROM save ORDER BY songid ASC LIMIT ?,?",new String[]{String.valueOf(star), String.valueOf(end)});
        List<SongData> song=new ArrayList<>();
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("songid"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String author=cursor.getString(cursor.getColumnIndex("author"));
            String image=cursor.getString(cursor.getColumnIndex("pic"));
            song.add(new SongData(id,title,author,image));
        }
        cursor.close();
        return song;
    }
    //查询指定title的记录
    public List<SongData> getTitle(String mtitle){
        Cursor cursor=sqlbase.rawQuery("SELECT * FROM save WHERE title=?",new String[]{mtitle});
        List<SongData> song=new ArrayList<>();
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("songid"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String author=cursor.getString(cursor.getColumnIndex("author"));
            String image=cursor.getString(cursor.getColumnIndex("pic"));
            song.add(new SongData(id,title,author,image));
        }
        cursor.close();
        return song;
    }
    //是否已经收藏
    public Boolean isSave(String id){
        Cursor cursor=sqlbase.rawQuery("SELECT * FROM save WHERE songid=?",new String[]{id});
        Boolean is=cursor.moveToNext();
        cursor.close();
        return is;
    }

}
