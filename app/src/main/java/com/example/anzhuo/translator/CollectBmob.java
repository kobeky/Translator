package com.example.anzhuo.translator;

import cn.bmob.v3.*;

/**
 * Created by anzhuo on 2016/11/7.
 */
public class CollectBmob extends BmobObject {
    private String content;
    private String TranslateContent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslateContent() {
        return TranslateContent;
    }

    public void setTranslateContent(String translateContent) {
        TranslateContent = translateContent;
    }


}
