package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by anzhuo on 2016/11/1.
 */
public class CloudActivity extends AppCompatActivity {
    ImageView iv_back;
    ListView listView;
    CloudAdapter adapter;
    List<CollectExcel>mlist=new ArrayList<>();
    DisplayMetrics dm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_cloud);
        listView= (ListView) findViewById(R.id.list_cloud);
        dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        adapter=new CloudAdapter(this,mlist,dm.widthPixels);
        listView.setAdapter(adapter);
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<CollectExcel>query=new BmobQuery<>();
        query.addWhereEqualTo("author",user);
        query.order("updatedAt");
        query.findObjects(new FindListener<CollectExcel>() {
            @Override
            public void done(List<CollectExcel> list, BmobException e) {
                if (e==null){
                        mlist.addAll(list);
                    adapter.notifyDataSetChanged();
                }else {

                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
