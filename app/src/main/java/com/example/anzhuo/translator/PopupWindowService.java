package com.example.anzhuo.translator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anzhuo on 2016/10/24.
 */
public class PopupWindowService extends Service{
    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    private boolean isAdded=true;//是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private View floatView;

    private float startX = 0;

    private float startY = 0;

    private float x;

    private float y;

    private String copyValue;
    String value;
    TextView titleText;
    @Nullable



    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatView=layoutInflater.inflate(R.layout.popupwindow,null);
        wm= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params=new WindowManager.LayoutParams();
        //设置 window type
        params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        //设置图片格式，效果为背景半透明
        params.format= PixelFormat.TRANSLUCENT;
        params.alpha=0.5f;
        //设置window flag；
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.flags=WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        params.width =getResources().getDimensionPixelSize(R.dimen.float_width);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        floatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY();
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int)( x - startX);
                        params.y = (int) (y - startY);
                        wm.updateViewLayout(floatView, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = startY = 0;
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
            switch (operation) {
                case OPERATION_SHOW:
                    if (isAdded) {
                        isAdded=false;
                        wm.addView(floatView,params);
                    }
                    break;
            }
            copyValue = intent.getStringExtra("copyValue");
            setupCellView(floatView);
            Log.i("LM", "=====copyValue :"+copyValue);
        }
        return super.onStartCommand(intent, flags, startId);
    }

//    /**
//    *创建悬浮窗
//     */
//    private void createFloatView(){
//
//        wm.addView(floatView, params);
//        isAdded = true;
//    }
    /**
    *设置悬浮窗子控件
     */
    private void setupCellView(View rootview){
        ImageView closedImg = (ImageView) rootview.findViewById(R.id.float_window_closed);
        titleText = (TextView) rootview.findViewById(R.id.float_window_title);
        closedImg.setAlpha(0.5f);
        Translate_Result result=new Translate_Result();
        try {
            result.Tests(copyValue, new SetResult() {
                @Override
                public void oint(String str) {
                    titleText.setText(str);
                    Log.i("LM",str+789);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        closedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    wm.removeView(floatView);
                isAdded=true;
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
