package com.example.anzhuo.translator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.Bmob;

/**
 * Created by anzhuo on 2016/10/21.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton iv_button;
    EditText et_user;
    EditText et_psw;
    Button bt_login;
    TextView tv_regeist;
    TextView tv_forget;
    MyUser information;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9f7cc626506e99fa3d0c7acb3e64ac5c");
        setContentView(R.layout.login_layout);
        information=new MyUser();
        iv_button = (ImageButton) findViewById(R.id.ib_button);
        et_user = (EditText) findViewById(R.id.et_userName_login);
        et_psw = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_regeist = (TextView) findViewById(R.id.tv_regeist);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_regeist.setText(Html.fromHtml("没有账号？<font color='#4861b4'><middle><middle>快来注册一个吧！</big></big></font>"));
        bt_login.setOnClickListener(this);
        tv_regeist.setOnClickListener(this);
        iv_button.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if(et_user.getText().toString().equals("")){
                    Toast.makeText(this, "请输入账号！", Toast.LENGTH_SHORT).show();
                }else {
                    information.setUsername(et_user.getText().toString());
                    information.setPassword(et_psw.getText().toString());
                    information.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                MyUser user= BmobUser.getCurrentUser(MyUser.class);
                                Intent intent = new Intent();
                                intent.setAction("com.example.anzhuo.translator.userName");
                                intent.putExtra("userName", user.getNickname().toString());
                                LoginActivity.this.sendBroadcast(intent);
                                Log.i("LM","广播："+et_user.getText().toString()+".......");
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                                Log.i("LM",e.getMessage()+e.getErrorCode());
                            }
                        }
                    });
                  }
                break;
            case R.id.ib_button:
                finish();
                break;
            case R.id.tv_regeist:
                startActivityForResult(new Intent(this, RegisterActivity.class), 10);
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPassActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10&&resultCode==RESULT_OK){
            et_user.setText(data.getStringExtra("name"));
        }
    }

}
