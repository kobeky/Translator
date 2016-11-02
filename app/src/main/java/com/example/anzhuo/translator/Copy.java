package com.example.anzhuo.translator;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by anzhuo on 2016/11/1.
 */
public class Copy {
        //注意
        public static void copy(String s, Context context) {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(s.trim());
        }
}
