package com.wang.mymusic.data;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.mymusic.R;
import com.wang.mymusic.model.SongLocalBean;
import com.wang.mymusic.util.Assist;

import java.util.List;

/**
 * MyMusic
 * Created by wang on 2017.5.27.
 */

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.MyHolder> {
    private Context mContext;
    private List<SongLocalBean> mSong;
    private Assist mtools;
    private onItemClick mClick;
    public LocalAdapter(Context context, List<SongLocalBean> mSong) {
        mContext=context;
        this.mSong=mSong;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.local_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        mtools=Assist.getAssist();
        holder.title.setText(mSong.get(position).getTitle());
        holder.artist.setText(mSong.get(position).getArtist());
        holder.time.setText(mtools.getPlayTime(mSong.get(position).getDuration()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSong.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private CardView card;
        private TextView title,artist,time;
        MyHolder(View itemView) {
            super(itemView);
            card= (CardView) itemView.findViewById(R.id.cardlocal);
            time= (TextView) itemView.findViewById(R.id.local_time);
            title= (TextView) itemView.findViewById(R.id.local_title);
            artist= (TextView) itemView.findViewById(R.id.local_artist);
        }
    }

    public void setmClick(onItemClick mClick) {
        this.mClick = mClick;
    }

    public interface onItemClick{
        void onClick(int position);
    }
}
