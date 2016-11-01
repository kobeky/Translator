package com.example.anzhuo.translator;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.*;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class ForgetPassActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    Button bt_find;
    EditText userName;
    String name;
    String pass;
    private int START;
    private int MSD_ID = 1;
    NotificationManager manager;
    NotificationCompat.Builder builder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_forget);
        bt_find= (Button) findViewById(R.id.bt_forget);
        userName= (EditText) findViewById(R.id.et_userName_forget);
        iv_back.setOnClickListener(this);
        bt_find.setOnClickListener(this);
        manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back_forget:
                finish();
                break;
            case R.id.bt_forget:
                cn.bmob.v3.Bmob.initialize(this, "9f7cc626506e99fa3d0c7acb3e64ac5c");
                BmobQuery b=new BmobQuery("News");
                b.findObjectsByTable(new QueryListener<JSONArray>() {
                    @Override
                    public void done(JSONArray jsonArray, BmobException e) {
                        if (e==null){
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                                name=jsonObject.getString("name");
                                    pass=jsonObject.getString("password");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                if (userName.getText().toString().equals(name)){
                                    builder=new NotificationCompat.Builder(ForgetPassActivity.this);
                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setContentTitle("JT翻译");
                                    builder.setContentText("    "+name+"账户的密码为"+pass);
                                    manager.notify(MSD_ID,builder.build());
                                    START=1;
                                    finish();
                                }
                                else {
                                    START=2;
                                }
                            }
                        }
                        if (START!=1){
                            Toast.makeText(ForgetPassActivity.this, "该用户名不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}
