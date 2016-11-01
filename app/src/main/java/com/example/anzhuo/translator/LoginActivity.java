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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        iv_button = (ImageButton) findViewById(R.id.ib_button);
        et_user = (EditText) findViewById(R.id.et_userName_login);
        et_psw = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_regeist = (TextView) findViewById(R.id.tv_regeist);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_regeist.setText(Html.fromHtml("没有账号？<font color='#ff0000'><middle><middle>快来注册一个吧！</big></big></font>"));
        bt_login.setOnClickListener(this);
        tv_regeist.setOnClickListener(this);
        iv_button.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                Intent intent = new Intent();
                intent.setAction("com.example.anzhuo.translator.userName");
                intent.putExtra("userName", et_user.getText().toString());
                this.sendBroadcast(intent);
                Log.i("LM","广播："+et_user.getText().toString()+".......");
                finish();
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
