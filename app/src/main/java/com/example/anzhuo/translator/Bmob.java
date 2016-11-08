package com.example.anzhuo.translator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class Bmob extends cn.bmob.v3.Bmob {
    Context mcontext;
String objectId;
    public Bmob(Context context){
        this.mcontext=context;
    }
    public void regeist(String name,String password){
        cn.bmob.v3.Bmob.initialize(mcontext,"9f7cc626506e99fa3d0c7acb3e64ac5c");
        final News news = new News();

        news.setName(name);
        news.setPass(password);
        news.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("LM", "添加数据成功，返回objectId为：" + s);
                    objectId=news.getObjectId().toString();
                    Toast.makeText(mcontext, "aaa"+s+"bbb"+objectId, Toast.LENGTH_LONG).show();

                } else {
                    Log.i("LM", "创建数据失败：" + e.getMessage());
                }
            }
        });
//        return objectId;
    }

    public void updata(String objectId,List<TranslateInfo>list){
        cn.bmob.v3.Bmob.initialize(mcontext,"9f7cc626506e99fa3d0c7acb3e64ac5c");
        News n =new News();
        n.setList(list);
        n.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(mcontext,"收藏成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mcontext,"更新失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class News extends BmobObject {
        private String name;
        private String password;
        private List<TranslateInfo>list;
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

        public List<TranslateInfo> getList() {
            return list;
        }

        public void setList(List<TranslateInfo> list) {
            this.list = list;
        }
    }
}
