package com.example.anzhuo.translator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * Created by anzhuo on 2016/11/1.
 */
public class CloudActivity extends AppCompatActivity {
    ImageView iv_back;
    ListView listView;
    CloudAdapter adapter;
    CloudInfo cloudInfo;
    List<CloudInfo>list=new ArrayList<>();
    BroadcastReceiver receiver;
    DisplayMetrics dm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_cloud);
        listView= (ListView) findViewById(R.id.list_cloud);
         dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.anzhuo.translator.userName");
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


            }
        };
        this.registerReceiver(receiver,filter);
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
        this.unregisterReceiver(receiver);
    }
}
