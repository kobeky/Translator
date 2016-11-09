package com.example.anzhuo.translator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by anzhuo on 2016/10/25.
 */
public class MeFragment extends Fragment implements View.OnClickListener{
    View view;
    TextView userName;
    RelativeLayout rl_user;
    RelativeLayout rl_collect;
    RelativeLayout rl_cloud;
    RelativeLayout rl_update;
    RelativeLayout rl_delete;
    Switch switch_me;
    Intent intent=new Intent();
    int on;
    Context context;
    BroadcastReceiver receiver;
    DbHelper dbHelper;
    MyDialog myDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.mefragement,container,false);
        context=this.getContext();
        userName= (TextView) view.findViewById(R.id.et_userName);
        rl_user= (RelativeLayout) view.findViewById(R.id.rl_user);
        rl_collect= (RelativeLayout) view.findViewById(R.id.rl_collect);
        rl_cloud= (RelativeLayout) view.findViewById(R.id.rl_CloudBackup);
        rl_delete= (RelativeLayout) view.findViewById(R.id.rl_delete);
        rl_update= (RelativeLayout) view.findViewById(R.id.rl_update);
        switch_me= (Switch) view.findViewById(R.id.switch_me);
        rl_user.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_cloud.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_delete.setOnClickListener(this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("L",Context.MODE_PRIVATE);
        on = sharedPreferences.getInt("K", on);
        if (on == 1) {
            switch_me.setChecked(true);
        } else {
            switch_me.setChecked(false);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        switch_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_me.isChecked()) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("L", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("K", 1);
                    editor.commit();
                } else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("L", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("K", 2);
                    editor.commit();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.anzhuo.translator.userName");
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("LM",intent.getStringExtra("userName").toString());
                userName.setText(intent.getStringExtra("userName").toString());
            }
        };
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user:
                intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_collect:
                startActivity(new Intent(getContext(),LocalCollection.class));
                break;
            case R.id.rl_CloudBackup:
                if (!userName.getText().toString().equals("")){
                    startActivity(new Intent(getContext(),CloudActivity.class));
                }else {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_update:
                Toast.makeText(getContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_delete:
                myDialog=new MyDialog(getContext(),R.style.MyDialog);
                myDialog.setTitle("删除提示");
                myDialog.setMessage("是否删除");
                myDialog.setYesOnclickListener("是", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        dbHelper=new DbHelper(context);
                                dbHelper.delete("Result");
                        Intent intent=new Intent();
                        intent.setAction("clear");
                        intent.putExtra("sure",1);
                        getContext().sendBroadcast(intent);
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("否", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
        }
    }
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("L", Context.MODE_PRIVATE);
        on = sharedPreferences.getInt("K", on);
        if (on == 1) {
            sharedPreferences = getContext().getSharedPreferences("L", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("K", 1);
            editor.commit();
            intent.setClass(getContext(), ClipBoardService.class);
            context.startService(intent);
        }else{
            sharedPreferences=getContext().getSharedPreferences("L",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("K",2);
            editor.commit();
            intent.setClass(getContext(), ClipBoardService.class);
            context.stopService(intent);
        }
    }
}

