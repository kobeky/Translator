package com.example.anzhuo.translator;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by anzhuo on 2016/10/26.
 */
class SpeechSynthesizers {

    public void SpeechSynthesizers(String s, final Context context) {
        InitListener mInitListener = new InitListener() {

            @Override
            public void onInit(int code) {

                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(context, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
                }
            }
        };

        SynthesizerListener synthesizerListener = new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                Toast.makeText(context, "语音开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                Toast.makeText(context, speechError.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
            }
        };

        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.createSynthesizer(context, mInitListener);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "vixy");
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
        speechSynthesizer.startSpeaking(s, synthesizerListener);
    }
    /**
     * 初始化监听器。
     */


}
