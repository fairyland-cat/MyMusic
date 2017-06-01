package com.wang.mymusic.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.mymusic.R;
import com.wang.mymusic.model.SongData;

import java.util.List;

/**
 * MyMusic
 * Created by wang on 2017.5.24.
 */

public class SaveSong extends RecyclerView.Adapter<SaveSong.MyHolder>{

    private Context mContext;
    private RecommendAdapter.OnItemClickListener mClick;
    private List<SongData> mdata;

    public SaveSong(Context mContext, List<SongData> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Glide.with(mContext).load(mdata.get(position).getPic_small()).into(holder.mpic);
        holder.mtitle.setText(mdata.get(position).getTitle());
        holder.mauthor.setText(mdata.get(position).getAuthor());
        MyOnClick mOnClick=new MyOnClick(position,mdata.get(position).getSong_id());
        if(mClick!=null){
            holder.mplay.setOnClickListener(mOnClick);
            holder.mmore.setOnClickListener(mOnClick);
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView mtitle,mauthor;
        private ImageView mpic;
        private ImageButton mplay,mmore;
        public MyHolder(View itemView) {
            super(itemView);
            mtitle= (TextView) itemView.findViewById(R.id.songtitle);
            mauthor= (TextView) itemView.findViewById(R.id.songauthor);
            mpic= (ImageView) itemView.findViewById(R.id.songicon);
            mplay= (ImageButton) itemView.findViewById(R.id.musicplay);
            mmore= (ImageButton) itemView.findViewById(R.id.more);
        }
    }
    public void setOnItemClickListener(RecommendAdapter.OnItemClickListener onClick){
        mClick=onClick;
    }
    public class MyOnClick implements View.OnClickListener{
        private int mPosition;
        private String song_id;
        public MyOnClick(int position,String id) {
            mPosition=position;
            song_id=id;
        }

        @Override
        public void onClick(View view) {
            mClick.onItemClick(view,mPosition,song_id);
        }
    }
}
