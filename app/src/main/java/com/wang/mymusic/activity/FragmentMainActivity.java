package com.wang.mymusic.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wang.mymusic.R;

import java.util.ArrayList;

public class FragmentMainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
ViewPager.OnPageChangeListener{

    private ViewPager mPager;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private RadioButton mlove,mlocal;
    private PagerAdapter mAdapter;
    private RadioGroup mGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        mlocal= (RadioButton) findViewById(R.id.local);
        mlove= (RadioButton) findViewById(R.id.love);
        mPager= (ViewPager) findViewById(R.id.pager);
        mGroup= (RadioGroup) findViewById(R.id.group);
        SaveFragment fm_love=new SaveFragment();
        LocalFragment fm_local=new LocalFragment();
        fragments.add(fm_love);
        fragments.add(fm_local);
        mAdapter=new com.wang.mymusic.data.PagerAdapter(getSupportFragmentManager(),fragments);
        mPager.setAdapter(mAdapter);
        if (getIntent().getIntExtra("type",1)==1){
            mPager.setCurrentItem(0);
            mlove.setChecked(true);
        }else {
            mPager.setCurrentItem(1);
            mlocal.setChecked(true);
        }
        mPager.addOnPageChangeListener(this);
        mGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.d("tag", "onCheckedChanged: "+i);
        switch (i){
            case R.id.love:
                mPager.setCurrentItem(0);
                break;
            case R.id.local:
                mPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state==2){
            switch (mPager.getCurrentItem()){
                case 0:
                    mlove.setChecked(true);
                    break;
                case 1:
                    mlocal.setChecked(true);
                    break;
            }
            Log.d("tag", "onPageScrollStateChanged: "+mPager.getCurrentItem());
        }
    }
}
