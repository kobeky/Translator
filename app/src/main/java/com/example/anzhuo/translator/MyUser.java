package com.example.anzhuo.translator;
import cn.bmob.v3.BmobUser;

/**
 * Created by anzhuo on 2016/10/31.
 */
public class MyUser extends BmobUser {
    private String nickname;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
