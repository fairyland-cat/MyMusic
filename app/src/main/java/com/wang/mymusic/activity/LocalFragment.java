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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.mymusic.R;
import com.wang.mymusic.data.LocalAdapter;
import com.wang.mymusic.model.SongLocalBean;
import com.wang.mymusic.util.Assist;

import java.util.ArrayList;
import java.util.List;

/**
 * MyMusic
 * Created by wang on 2017.5.26.
 */

public class LocalFragment extends Fragment {
    private XRecyclerView mlistview;
    private static final int type=0;
    private  int off=0;
    private List<SongLocalBean> mList=new ArrayList<>();
    private Assist mtools;
    private MusicPlay musicPlay;
    private Context mContext;
    private LocalAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_save,container,false);
        bindMusicPlay();
        mtools=Assist.getAssist();
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
                setAdapter();
                mlistview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                off+=10;
                setAdapter();
                mlistview.refreshComplete();
            }
        });

        setAdapter();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    //设置适配器
    public void setAdapter(){
        if (off == 0) {
            mList.clear();
        }
        mList.addAll(mtools.getLimitList(mContext,off,off+9));
        if (mAdapter==null){
            mAdapter=new LocalAdapter(mContext,mList);
            mlistview.setAdapter(mAdapter);
            mAdapter.setmClick(new LocalAdapter.onItemClick() {
                @Override
                public void onClick(int position) {
                    musicPlay.SongPlay(position,type);
                }
            });
        }
        mAdapter.notifyDataSetChanged();
    }

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

}
