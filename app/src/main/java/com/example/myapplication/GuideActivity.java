package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.GuideFragment.CurrentFragment;
import com.example.myapplication.GuideFragment.FriendFragment;
import com.example.myapplication.GuideFragment.LoadFragment;
import com.example.myapplication.GuideFragment.LoveWallFragment;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vp;
    private CurrentFragment currentFragment;
    private FriendFragment friendFragment;
    private LoadFragment loadFragment;
    private LoveWallFragment loveWallFragment;
    private List<Fragment> fragmentList=new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private RadioButton guide_load,guide_friend,guide_lovewall,guide_current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        mFragmentAdapter=new FragmentAdapter(this.getSupportFragmentManager(), fragmentList) ;
        vp.setOffscreenPageLimit(4);//有几个界面就写几个
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
            }
            @Override
            public void onPageSelected(int position){
                changeRa(position);

            }
            @Override
            public void onPageScrollStateChanged(int state){
            }
        });


    }
    public void changeRa(int position){
        if (position==0){
            guide_load.setChecked(true);
            guide_current.setChecked(false);
            guide_friend.setChecked(false);
            guide_lovewall.setChecked(false);
        }else if (position==1){
            guide_load.setChecked(false);
            guide_current.setChecked(true);
            guide_friend.setChecked(false);
            guide_lovewall.setChecked(false);
        }
        else if (position==2){
            guide_load.setChecked(false);
            guide_current.setChecked(false);
            guide_friend.setChecked(true);
            guide_lovewall.setChecked(false);
        }else if(position==3){
            guide_load.setChecked(false);
            guide_current.setChecked(false);
            guide_friend.setChecked(false);
            guide_lovewall.setChecked(true);
        }

    }

    private void initView() {
        loadFragment=new LoadFragment();
        currentFragment=new CurrentFragment();
        friendFragment=new FriendFragment();
        loveWallFragment=new LoveWallFragment();
        fragmentList.add(loadFragment);
        fragmentList.add(currentFragment);
        fragmentList.add(friendFragment);
        fragmentList.add(loveWallFragment);
        vp=findViewById(R.id.guide_fragment);
        guide_load=findViewById(R.id.guide_load);
        guide_current=findViewById(R.id.guide_current);
        guide_friend=findViewById(R.id.guide_friend);
        guide_lovewall=findViewById(R.id.guide_lovewall);
        guide_load.setOnClickListener(this);
        guide_current.setOnClickListener(this);
        guide_friend.setOnClickListener(this);
        guide_lovewall.setOnClickListener(this);
        guide_load.setChecked(true);
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.guide_load:
                 vp.setCurrentItem(0,true);
                 guide_load.setChecked(true);
                 guide_current.setChecked(false);
                 guide_friend.setChecked(false);
                 guide_lovewall.setChecked(false);
                 break;
             case R.id.guide_current:
                 vp.setCurrentItem(1,true);
                 guide_load.setChecked(false);
                 guide_current.setChecked(true);
                 guide_friend.setChecked(false);
                 guide_lovewall.setChecked(false);
                 break;
             case R.id.guide_friend:
                 vp.setCurrentItem(2,true);
                 guide_load.setChecked(false);
                 guide_current.setChecked(false);
                 guide_friend.setChecked(true);
                 guide_lovewall.setChecked(false);
                 break;
             case R.id.guide_lovewall:
                 vp.setCurrentItem(3,true);
                 guide_load.setChecked(false);
                 guide_current.setChecked(false);
                 guide_friend.setChecked(false);
                 guide_lovewall.setChecked(true);
                 break;
         }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList=new ArrayList<Fragment>();
        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
            super(fm);
            this.fragmentList=fragmentList;
        }
        @Override
        public Fragment getItem(int position){
            return fragmentList.get(position);
        }
        @Override
        public int getCount(){
            return fragmentList.size();
        }
    }

}