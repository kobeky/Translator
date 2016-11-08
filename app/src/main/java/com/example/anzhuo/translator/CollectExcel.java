package com.example.anzhuo.translator;

import cn.bmob.v3.BmobObject;

/**
 * Created by anzhuo on 2016/11/8.
 */
public class CollectExcel extends BmobObject {
private String CloudWord;
    private String TranslateWord;

    public String getCloudWord() {
        return CloudWord;
    }

    public void setCloudWord(String cloudWord) {
        CloudWord = cloudWord;
    }

    public String getTranslateWord() {
        return TranslateWord;
    }

    public void setTranslateWord(String translateWord) {
        TranslateWord = translateWord;
    }
}
