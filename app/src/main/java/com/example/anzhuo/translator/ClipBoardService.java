package com.example.anzhuo.translator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by anzhuo on 2016/10/21.
 */
public class ClipBoardService extends Service {
    private ClipBoardReceiver mBoardReceiver;
    private MyBinder binder = new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public void onCreate(){
        super.onCreate();
        final ClipboardManager cm= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = cm.getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                Intent mIntent = new Intent();
                mIntent.setAction("com.example.anzhuo.translator.ClipBoardReceiver");
                mIntent.putExtra("clipboardvalue", item.getText().toString());
                sendBroadcast(mIntent);
                Log.i("LM",item.getText().toString()+"111111");
            }
        });
        mBoardReceiver=new ClipBoardReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.anzhuo.translator.ClipBoardReceiver");
        registerReceiver(mBoardReceiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    public class MyBinder extends Binder{
        ClipBoardService getService(){
            return ClipBoardService.this;
        }
    }
    class ClipBoardReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String value= intent.getStringExtra("clipboardvalue");
            Log.i("clipboardvalue",value);
            Intent show = new Intent(ClipBoardService.this, PopupWindowService.class);
            show.putExtra(PopupWindowService.OPERATION,PopupWindowService.OPERATION_SHOW);
            show.putExtra("copyValue", value);
            startService(show);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LM","service destroy");
        this.unregisterReceiver(mBoardReceiver);
    }
}
