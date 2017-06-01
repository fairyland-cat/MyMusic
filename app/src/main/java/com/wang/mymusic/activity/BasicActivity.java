package com.wang.mymusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.wang.mymusic.R;

public abstract class BasicActivity extends AppCompatActivity {

    protected MusicPlay musicPlay;
    private Boolean isBinder=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        musicPlay.setChange(mChange);
        musicPlay.sendUIDate();
    }

    ServiceConnection serConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlay.musicBind bind= (MusicPlay.musicBind) iBinder;
            musicPlay=bind.getMusicPlay();
            musicPlay.setChange(mChange);
            musicPlay.sendUIDate();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicPlay=null;
        }
    };

    public void bindMusicPlay(){
        if(!isBinder){
            Intent intent=new Intent(this,MusicPlay.class);
            bindService(intent,serConn, Context.BIND_AUTO_CREATE);
            isBinder=true;
        }
    }
    //ui更新接口实现
    private MusicPlay.uiUpdate mChange=new MusicPlay.uiUpdate() {
        @Override
        public void updatePlay(Bundle bundle) {
            changePlay(bundle);
        }

        @Override
        public void upProgress(int progress) {
            getProgress(progress);
        }
    };

    public abstract void changePlay(Bundle bundle);
    public abstract void getProgress(int progress);

    public void unBindMusicPlay(){
        if(isBinder){
            unbindService(serConn);
            isBinder=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindMusicPlay();
    }
}
