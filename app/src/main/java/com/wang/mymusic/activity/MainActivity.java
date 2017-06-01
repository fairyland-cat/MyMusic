package com.wang.mymusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.mymusic.R;
import com.wang.mymusic.data.MusicData;
import com.wang.mymusic.data.RecommendAdapter;
import com.wang.mymusic.data.SqlUtil;
import com.wang.mymusic.model.RecommentBean;
import com.wang.mymusic.model.SongMusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BasicActivity implements SongMusic{

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private XRecyclerView mXRecycler;
    private RecommendAdapter mdata;
    private List<RecommentBean.SongListBean> mlist;
    private int off=0;
    private int type=2;
    private RelativeLayout mPlayitem;
    private ImageView mIcon;
    private TextView mTitle,mAuthor;
    private ImageButton mLast;
    private ImageButton mPlay;
    private ImageButton mNext;
    private ImageButton mMode;
    private Boolean isPlaying=false;
    private static int playmode=1;
    private MusicData mTools;
    private SqlUtil sql;
    private Boolean isSaveSong=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();//实例化组件
        mTools=MusicData.getMusicData();
        mTools.setSongServer(this);
        //加载数据
        mTools.getListMusic(type,"10",off);

        //启动服务
        Intent intent=new Intent(this,MusicPlay.class);
        startService(intent);
        bindMusicPlay();

        //设置刷新，加载更多的监听事件
        mXRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                off=0;
                mTools.getListMusic(type,"10",off);
                mdata.notifyDataSetChanged();
                mXRecycler.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                off+=10;
                mTools.getListMusic(type,"10",off);
                mXRecycler.loadMoreComplete();
            }
        });

        /**
         * 将toolbar与drawerlayout关联上
         * */
        final ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mToggle.syncState();
        mDrawer.addDrawerListener(mToggle);
        //设置NavigationView的点击事件
        NavigationView mNavigation = (NavigationView) findViewById(R.id.nav);
        mNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.recom:
                        type=2;
                        off=0;
                        mTools.getListMusic(type,"10",off);
                        break;
                    case R.id.oumei:
                        type=21;
                        off=0;
                        mTools.getListMusic(type,"10",off);
                        break;
                    case R.id.jingdian:
                        type=22;
                        off=0;
                        mTools.getListMusic(type,"10",off);
                        break;
                    case R.id.gedan:
                        Intent intent=new Intent(MainActivity.this,FragmentMainActivity.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                        break;
                    case R.id.bendi:
                        Intent intent1=new Intent(MainActivity.this,FragmentMainActivity.class);
                        intent1.putExtra("type",0);
                        startActivity(intent1);
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }
        });

        //实现列表中下载，收藏等菜单监听事件


        //实现底部音乐控制台的按钮事件
        mControl mCt=new mControl();
        mPlayitem.setOnClickListener(mCt);
        mLast.setOnClickListener(mCt);
        mPlay.setOnClickListener(mCt);
        mNext.setOnClickListener(mCt);
        mMode.setOnClickListener(mCt);

        //获取sql帮助类
        sql=new SqlUtil(this);
        sql.getsqlData();
    }

    @Override
    public void changePlay(Bundle bundle) {
        if (bundle.getString("pic")!=null){
            Glide.with(this).load(bundle.getString("pic")).into(mIcon);
        }
        mTitle.setText(bundle.getString("title"));
        mAuthor.setText(bundle.getString("author"));
        if(bundle.getBoolean("isPlay")){
            mPlay.setImageResource(R.drawable.playsong);
            isPlaying=true;
        }else {
            mPlay.setImageResource(R.drawable.pause);
            isPlaying=false;
        }
    }

    @Override
    public void getProgress(int progress) {
        //更新进度条
    }

    private void findview(){
        //获得组件实例
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mlist=new ArrayList<>();
        mDrawer= (DrawerLayout) findViewById(R.id.drawer);
        mXRecycler= (XRecyclerView) findViewById(R.id.xrecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecycler.setLayoutManager(layoutManager);
        mXRecycler.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mXRecycler.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mPlayitem= (RelativeLayout) findViewById(R.id.control);
        mPlay= (ImageButton) findViewById(R.id.play);
        mIcon= (ImageView) findViewById(R.id.songicon);
        mTitle= (TextView) findViewById(R.id.songtitle);
        mAuthor= (TextView) findViewById(R.id.songauthor);
        mLast= (ImageButton) findViewById(R.id.last);
        mNext= (ImageButton) findViewById(R.id.next);
        mMode= (ImageButton) findViewById(R.id.moder);
    }

    //item点击事件接口的实现
    RecommendAdapter.OnItemClickListener onClick=new RecommendAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, final int position, final String song_id) {
            switch (view.getId()) {
                case R.id.musicplay:
                    mPlay(position,type);
                    break;
                case R.id.more:
                    PopupMenu pop = new PopupMenu(MainActivity.this, view);
                    if (sql.isSave(song_id)){
                        pop.getMenuInflater().inflate(R.menu.pop_cencel, pop.getMenu());
                        isSaveSong=true;
                    }else {
                        pop.getMenuInflater().inflate(R.menu.pop_menu, pop.getMenu());
                        isSaveSong=false;
                    }
                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.down:
                                    Toast.makeText(MainActivity.this, "下载开始了", Toast.LENGTH_SHORT).show();
                                    musicPlay.downSong(type,position);
                                    break;
                                case R.id.love:
                                    if (isSaveSong){
                                        musicPlay.cancelSong(song_id);
                                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_LONG).show();
                                    }else {
                                        musicPlay.saveSong(type,position);
                                        Toast.makeText(MainActivity.this, "收藏", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                    pop.show();
                    break;
            }

        }
    };

    //获取适配器对象
    public void songData(List<RecommentBean.SongListBean> song){
        if (off == 0) {
            mlist.clear();
            mlist.addAll(song);
        } else if (song != null) {
            mlist.addAll(song);
        }
        if (mdata == null) {
            mdata = new RecommendAdapter(MainActivity.this, mlist);
            mdata.setOnItemClickListener(onClick);
            mXRecycler.setAdapter(mdata);
        }
        mdata.notifyDataSetChanged();
    }
    @Override
    public void setAdapter(List<RecommentBean.SongListBean> song) {
        songData(song);
    }

    public class mControl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.control:
                    Intent intent=new Intent(MainActivity.this,PlayMusic.class);
                    startActivity(intent);
                    break;
                case R.id.last:
                    musicPlay.LastPlay();
                    break;
                case R.id.play:
                    if(musicPlay.isPlay()){
                        mPlay.setImageResource(R.drawable.pause);
                        musicPlay.SongPause();
                        isPlaying=true;
                    }else if(isPlaying){
                        mPlay.setImageResource(R.drawable.playsong);
                        musicPlay.StarPlay(false);
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
                    setmPlayMode();
                    break;
            }
        }
    }
    public void setmPlayMode(){
        Intent modeintent=new Intent("com.wang.mymusic.PLAYMODE");
        modeintent.putExtra("mode",playmode);
        sendBroadcast(modeintent);
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
        setmPlayMode();
        mTools.setMlrc(new PlayMusic.addLrc() {
            @Override
            public void setLrc(File file) {
                Toast.makeText(MainActivity.this, "下载成功，存放地址\n"+file, Toast.LENGTH_LONG).show();
            }
        });
    }

    //播放
    public void mPlay(int position,int type){
        mPlay.setImageResource(R.drawable.playsong);
        musicPlay.SongPlay(position,type);
    }
    //循环模式
    public static class PlayMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            playmode=intent.getIntExtra("mode",1);
        }
    }
    //下载
    //收藏
}
