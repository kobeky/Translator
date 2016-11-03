package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by anzhuo on 2016/11/1.
 */
public class CollectActivity extends AppCompatActivity {
    ImageView iv_back;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);
        iv_back= (ImageView) findViewById(R.id.local_back);
        listView= (ListView) findViewById(R.id.local_lv);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
