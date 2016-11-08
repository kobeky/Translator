package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

/**
 * Created by anzhuo on 2016/10/25.
 */
public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    SmartTabLayout smartTabLayout;
    FragmentPagerItemAdapter fragmentPagerItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        smartTabLayout = (SmartTabLayout) findViewById(R.id.viewpagertab);
        fragmentPagerItemAdapter= new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).add("翻译", TranslateFragement.class).add("词典",DictionaryFragement.class).add("我的", MeFragment.class).create());
        viewPager.setAdapter(fragmentPagerItemAdapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
