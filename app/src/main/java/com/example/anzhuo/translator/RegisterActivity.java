package com.example.anzhuo.translator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_user;
    EditText et_pass;
    EditText et_passAgain;
    Button bt_register;
    ImageView iv_back;
    String name;
    private static int START=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_register);
        bt_register= (Button) findViewById(R.id.bt_register);
        et_user= (EditText) findViewById(R.id.et_userName_register);
        et_pass= (EditText) findViewById(R.id.et_pass_register);
        et_passAgain= (EditText) findViewById(R.id.et_passAgain);
        iv_back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.iv_back_register:
        finish();
        break;
    case R.id.bt_register:
        Bmob.initialize(this, "9f7cc626506e99fa3d0c7acb3e64ac5c");
        BmobQuery bmobQuery=new BmobQuery("News");
        bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            name = json.getString("name");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        if (et_user.getText().toString().equals(name)) {
                            Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                            START = 1;
                        }
                        else {
                            START=2;
                        }
                    }
                }
                if (START != 1) {
                    if (!et_passAgain.getText().toString().equals(et_pass.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    else if (et_user.getText().toString().equals("")||et_pass.getText().toString().equals("")){
                        Toast.makeText(RegisterActivity.this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        com.example.anzhuo.translator.Bmob bmob = new com.example.anzhuo.translator.Bmob(RegisterActivity.this);
                        bmob.regeist(et_user.getText().toString(), et_pass.getText().toString());
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        setResult(RESULT_OK,new Intent().putExtra("name",et_user.getText().toString()));
        finish();
        break;
}
    }
}
