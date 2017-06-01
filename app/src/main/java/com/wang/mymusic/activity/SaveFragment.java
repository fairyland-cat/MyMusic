package com.wang.mymusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.mymusic.R;
import com.wang.mymusic.data.RecommendAdapter;
import com.wang.mymusic.data.SaveSong;
import com.wang.mymusic.data.SqlUtil;
import com.wang.mymusic.model.SongData;

import java.util.ArrayList;
import java.util.List;

public class SaveFragment extends Fragment {

    private XRecyclerView mlistview;
    private SaveSong mSaveSong;
    private static final int type=1;
    private  int off=0;
    private List<SongData> mList=new ArrayList<>();
    private SqlUtil sql;
    private Boolean isSaveSong=false;
    private MusicPlay musicPlay;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_save,container,false);
        bindMusicPlay();
        mlistview= (XRecyclerView) view.findViewById(R.id.songlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mlistview.setLayoutManager(layoutManager);
        mlistview.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mlistview.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        //实现刷新与加载
        mlistview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                off=0;
                getSonglist();
                mlistview.refresh();
            }

            @Override
            public void onLoadMore() {
                off+=10;
                getSonglist();
                mlistview.refresh();
            }
        });
        sql=new SqlUtil(mContext);
        sql.getsqlData();

        getSonglist();
        return view;
    }

    //获取喜欢的歌曲列表
    private void getSonglist(){
        if (off == 0) {
            mList.clear();
        }
        mList.addAll(sql.getOnePage(off,off+9));
        if (mSaveSong==null){
            mSaveSong=new SaveSong(mContext,mList);
            mlistview.setAdapter(mSaveSong);
            mSaveSong.setOnItemClickListener(onClick);
        }
        mSaveSong.notifyDataSetChanged();
    }
    //实现item点击事件的接口
    RecommendAdapter.OnItemClickListener onClick=new RecommendAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, final int position, final String song_id) {
            switch (view.getId()) {
                case R.id.musicplay:
                    musicPlay.SongPlay(position,type);
                    break;
                case R.id.more:
                    PopupMenu pop = new PopupMenu(mContext, view);
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
                                    Toast.makeText(mContext, "下载开始了", Toast.LENGTH_SHORT).show();
                                    musicPlay.downSong(type,position);
                                    break;
                                case R.id.love:
                                    if (isSaveSong){
                                        musicPlay.cancelSong(song_id);
                                        getSonglist();
                                        Toast.makeText(mContext, "取消", Toast.LENGTH_LONG).show();
                                    }else {
                                        musicPlay.saveSong(type,position);
                                        Toast.makeText(mContext, "收藏", Toast.LENGTH_LONG).show();
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

    public void bindMusicPlay(){
            Intent intent=new Intent(mContext,MusicPlay.class);
            getActivity().bindService(intent,serConn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlay.musicBind bind = (MusicPlay.musicBind) iBinder;
            musicPlay = bind.getMusicPlay();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

        @Override
    public void onDestroy() {
        super.onDestroy();
        sql.closeData();
    }
}
