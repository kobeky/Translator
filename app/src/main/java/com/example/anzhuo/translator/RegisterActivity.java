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
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_user;
    EditText et_pass;
    EditText et_passAgain;
    EditText et_NikName;
    EditText et_email;
    Button bt_register;
    ImageView iv_back;
    MyUser information;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9f7cc626506e99fa3d0c7acb3e64ac5c");
        setContentView(R.layout.register_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_register);
        bt_register= (Button) findViewById(R.id.bt_register);
        et_user= (EditText) findViewById(R.id.et_userName_register);
        et_pass= (EditText) findViewById(R.id.et_pass_register);
        et_passAgain= (EditText) findViewById(R.id.et_passAgain);
        et_NikName= (EditText) findViewById(R.id.et_NikName_register);
        et_email= (EditText) findViewById(R.id.et_email_register);
        iv_back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        information=new MyUser();
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.iv_back_register:
        finish();
        break;
    case R.id.bt_register:

        if (et_user.getText().toString().equals("") || et_NikName.getText().toString().equals("")) {
            Toast.makeText(this, "请输入账号或昵称！", Toast.LENGTH_SHORT).show();
        } else {
            if (et_pass != null && et_pass.getText().toString().equals(et_passAgain.getText().toString())) {
                information.setUsername(et_user.getText().toString());
                information.setPassword(et_pass.getText().toString());
                information.setNickname(et_NikName.getText().toString());
                information.setEmail(et_email.getText().toString());
                information.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            MyUser userInformations = BmobUser.getCurrentUser(MyUser.class);
                            setResult(RESULT_OK,new Intent().putExtra("name",userInformations.getUsername().toString()));
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (e.getMessage().toString().equals("username '" + et_user.getText().toString() + "' already taken.")) {
                                Toast.makeText(RegisterActivity.this, "注册失败：该用户名已存在", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(this, "请输入相同密码！", Toast.LENGTH_SHORT).show();
            }
        }

        break;
}
    }
}
