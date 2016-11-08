package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class ForgetPassActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    Button bt_find;
    EditText userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"9f7cc626506e99fa3d0c7acb3e64ac5c");
        setContentView(R.layout.forgetpass_layout);
        iv_back= (ImageView) findViewById(R.id.iv_back_forget);
        bt_find= (Button) findViewById(R.id.bt_forget);
        userName= (EditText) findViewById(R.id.et_userName_forget);
        iv_back.setOnClickListener(this);
        bt_find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_forget:
                finish();
                break;
            case R.id.bt_forget:
                BmobUser.resetPasswordByEmail(userName.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                   if (e==null){
                       Toast.makeText(ForgetPassActivity.this, "重置密码请求成功"+"请注意查收邮件，并根据提示重置您的密码！", Toast.LENGTH_LONG).show();
                       finish();
                   }   else {
                       Toast.makeText(ForgetPassActivity.this, e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                    }
                });
                break;
        }
    }

}
