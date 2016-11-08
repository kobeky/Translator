package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Created by anzhuo on 2016/10/25.
 */
public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    SmartTabLayout smartTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        smartTabLayout = (SmartTabLayout) findViewById(R.id.viewpagertab);

        FragmentPagerItemAdapter fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).add("翻译", DictionaryFragement.class).add("词典", TranslateFragement.class).add("我的", MeFragment.class).create());
//
//        ViewPagerItemAdapter adapter=new ViewPagerItemAdapter(ViewPagerItems.with(this).add(
//                "a",R.layout.tab1).add("b",R.layout.tab2).add("c",R.layout.tab2).create());
        viewPager.setAdapter(fragmentPagerItemAdapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
