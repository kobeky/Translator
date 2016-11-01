package com.example.anzhuo.translator;

import android.content.Context;
import android.util.Log;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class Bmob extends cn.bmob.v3.Bmob {
    Context mcontext;

    public Bmob(Context context){
        this.mcontext=context;
    }
    public void regeist(String name,String password){
        cn.bmob.v3.Bmob.initialize(mcontext,"9f7cc626506e99fa3d0c7acb3e64ac5c");
        News news = new News();
        news.setName(name);
        news.setPass(password);
        news.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("LM", "添加数据成功，返回objectId为：" + s);
                } else {
                    Log.i("LM", "创建数据失败：" + e.getMessage());
                }
            }
        });
    }
    public class News extends BmobObject {
        private String name;
        private String password;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return password;
        }

        public void setPass(String pass) {
            this.password = pass;
        }
    }
}
