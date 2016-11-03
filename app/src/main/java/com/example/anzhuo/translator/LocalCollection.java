package com.example.anzhuo.translator;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

import static com.example.anzhuo.translator.R.id.item_tv2;

/**
 * Created by anzhuo on 2016/11/1.
 */
public class LocalCollection extends AppCompatActivity {
    ImageButton localBack;
    com.baoyz.swipemenulistview.SwipeMenuListView listView;
    DbHelper dbHelper;
    List<TranslateInfo> list = new ArrayList<TranslateInfo>();
    TransalteAdapter adapter;
    TextView textView;
TextView tv_null;
    SpeechSynthesizers speechSynthesizers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);

     speechSynthesizers=new SpeechSynthesizers();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5805e329");

        tv_null= (TextView) findViewById(R.id.tv_null);
        localBack = (ImageButton) findViewById(R.id.local_back);
        listView = (com.baoyz.swipemenulistview.SwipeMenuListView) findViewById(R.id.local_lv);
        adapter = new TransalteAdapter(this, list);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem copyItem = new SwipeMenuItem(LocalCollection.this);
                copyItem.setTitle("复制");
                copyItem.setBackground(new ColorDrawable(Color.parseColor("#778899")));
                copyItem.setWidth(110);
                copyItem.setTitleSize(18);
                copyItem.setTitleColor(Color.parseColor("#ffffff"));
                menu.addMenuItem(copyItem);
                SwipeMenuItem voiceItem = new SwipeMenuItem(LocalCollection.this);
                voiceItem.setTitle("语音跟读");
                voiceItem.setBackground(new ColorDrawable(Color.parseColor("#7EC0EE")));
                voiceItem.setWidth(200);
                voiceItem.setTitleSize(18);
                voiceItem.setTitleColor(Color.parseColor("#ffffff"));
                menu.addMenuItem(voiceItem);
                SwipeMenuItem cancelItem = new SwipeMenuItem(LocalCollection.this);
                cancelItem.setTitle("取消收藏");
                cancelItem.setBackground(new ColorDrawable(Color.parseColor("#838B8B")));
                cancelItem.setWidth(200);
                cancelItem.setTitleSize(18);
                cancelItem.setTitleColor(Color.parseColor("#ffffff"));
                menu.addMenuItem(cancelItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.query(dbHelper.DB_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("collection")) == 2) {
                    tv_null.setVisibility(View.GONE);
                    TranslateInfo translateInfo = new TranslateInfo();
                    translateInfo.setTv1(cursor.getString(cursor.getColumnIndex("tv1")));
                    translateInfo.setTv2(cursor.getString(cursor.getColumnIndex("tv2")));
                    list.add(0, translateInfo);
                    adapter.notifyDataSetChanged();
                }
            }
            cursor.close();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textView = (TextView) view.findViewById(R.id.item_tv1);
                TextView textView1 = (TextView) view.findViewById(item_tv2);
//                Intent intent=new Intent(LocalCollection.this,MainActivity.class);
//                intent.putExtra("tv1",textView.getText().toString());
//                intent.putExtra("tv2",textView1.getText().toString());
//                startActivity(intent);

            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 2:
                        TranslateInfo translateInfo = new TranslateInfo();
                        translateInfo.setTv1(list.get(position).getTv1().toString());
                        translateInfo.setCollection(1);
                        dbHelper.update(translateInfo);
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(LocalCollection.this, "已取消！", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        speechSynthesizers.SpeechSynthesizers(list.get(position).getTv2().toString(), LocalCollection.this);
                        break;
                    case 0:
                        Copy.copy(list.get(position).getTv2().toString(), LocalCollection.this);
                        Toast.makeText(LocalCollection.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        localBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
