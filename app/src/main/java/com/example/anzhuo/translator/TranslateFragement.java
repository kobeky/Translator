package com.example.anzhuo.translator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by anzhuo on 2016/11/2.
 */
public class TranslateFragement extends Fragment {
    View view;
    Button translate_bt;
    EditText translate_et;
    ImageButton translate_ib_cancel;
    ImageButton translate_ib_voice;
    ImageButton translate_ib_copy;
    ImageButton translate_rb_collection;
    TextView translate_result;
    TextView translate_resultContent;
    TextView translate_speak;
    TextView tv;
    LinearLayout translate_ll_result;
    ProgressBar translate_pb;

    HashMap<String, String> mIatResults = new HashMap<String, String>();
    Toast mToast;
    RecognizerDialog mIatDialog;
    SpeechRecognizer mIat;
    String mEngineType = SpeechConstant.TYPE_CLOUD;
    String PREFER_NAME = "com.iflytek.setting";

    int ret = 0; // 函数调用返回值
    SharedPreferences mSharedPreferences;
    SpeechSynthesizers speechSynthesizers;

    List<TranslateInfo> list = new ArrayList<TranslateInfo>();
    TransalteAdapter adapter;
    ListView listView;
    String stringResult;

    DbHelper dbHelper;
    ContentValues contentValues;

    BroadcastReceiver receiver;
    BroadcastReceiver receiver1;
    String userName = "";
    String user = "";
    int sure = 0;
    int collection = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.translatefragement, container, false);
        SpeechUtility.createUtility(getContext(), SpeechConstant.APPID + "=5805e329");
        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        speechSynthesizers = new SpeechSynthesizers();
        tv = (TextView) view.findViewById(R.id.tv);
        translate_ib_cancel = (ImageButton) view.findViewById(R.id.translate_ib_cancel);
        translate_ib_voice = (ImageButton) view.findViewById(R.id.translate_ib_voice);
        translate_ib_copy = (ImageButton) view.findViewById(R.id.translate_ib_copy);
        translate_ll_result = (LinearLayout) view.findViewById(R.id.translate_ll_result);
        translate_et = (EditText) view.findViewById(R.id.translate_et);
        translate_speak = (TextView) view.findViewById(R.id.translate_speak);
        translate_bt = (Button) view.findViewById(R.id.translate_bt);
        translate_result = (TextView) view.findViewById(R.id.translate_result);
        translate_resultContent = (TextView) view.findViewById(R.id.translate_result1);
        translate_rb_collection = (ImageButton) view.findViewById(R.id.translate_rb_collection);
        translate_pb = (ProgressBar) view.findViewById(R.id.translate_pb);

        listView = (ListView) view.findViewById(R.id.translate_lv);
        adapter = new TransalteAdapter(getContext().getApplicationContext(), list);
        listView.setAdapter(adapter);
        if (list.size() == 0) {
            dbHelper = new DbHelper(getContext().getApplicationContext());
            Cursor cursor = dbHelper.query(dbHelper.DB_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    TranslateInfo translateInfo = new TranslateInfo();
                    translateInfo.setTv1(cursor.getString(cursor.getColumnIndex("tv1")));
                    translateInfo.setTv2(cursor.getString(cursor.getColumnIndex("tv2")));
                    list.add(0, translateInfo);
                    adapter.notifyDataSetChanged();
                    listView.setVisibility(View.VISIBLE);
                }
                cursor.close();
            }
        }

        translate_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i >= i1 && !translate_et.getText().toString().equals("")) {
                    translate_bt.setTextColor(Color.WHITE);
                    translate_bt.setBackgroundColor(Color.parseColor("#1E90FF"));
                    translate_bt.setEnabled(true);
                    translate_ib_cancel.setVisibility(View.VISIBLE);

                } else {
                    translate_bt.setTextColor(Color.parseColor("#CDC1C5"));
                    translate_bt.setBackgroundColor(Color.parseColor("#ffffff"));
                    translate_bt.setEnabled(false);
                    translate_ib_cancel.setVisibility(View.GONE);
                    translate_ll_result.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        translate_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIat = SpeechRecognizer.createRecognizer(getContext(), mInitListener);
                mIatDialog = new RecognizerDialog(getContext(), mInitListener);
                mSharedPreferences = getActivity().getSharedPreferences(PREFER_NAME, Activity.MODE_PRIVATE);
                translate_et.setText(null);
                mIatResults.clear();
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean("iat_show", true);
                if (isShowDialog) {
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip("请开始说话...");
                } else {
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败，错误码：" + ret);
                    } else {
                        showTip("请开始说话");
                    }
                }
            }
        });

        translate_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
                translate_ll_result.setVisibility(View.VISIBLE);
                translate_pb.setVisibility(View.VISIBLE);
                String content = translate_et.getText().toString().trim();
                final Translate_Result translate_result1 = new Translate_Result();
                try {
                    translate_result1.Tests(content, new SetResult() {
                        @Override
                        public void oint(String str) {
                            String[] strings = str.split("\n");
                            stringResult = strings[1];
                            translate_resultContent.setText(translate_et.getText().toString());
                            translate_result.setText(str);

                            translate_pb.setVisibility(View.GONE);
                            TranslateInfo info = new TranslateInfo();
                            info.setTv1(translate_resultContent.getText().toString());
                            info.setTv2(stringResult);
                            list.add(0, info);
                            adapter.notifyDataSetChanged();
                            contentValues = new ContentValues();
                            contentValues.put("tv1", translate_resultContent.getText().toString());
                            contentValues.put("tv2", stringResult);
                            contentValues.put("collection", 1);
                            DbHelper dbHelper = new DbHelper(getContext().getApplicationContext());
                            dbHelper.insert(contentValues);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        translate_ib_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateInfo info = new TranslateInfo();
                info.setTv1(translate_et.getText().toString());
                translate_et.setText(null);
                translate_ll_result.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                sure = 0;
            }
        });

        translate_ib_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s = translate_result.getText().toString().split("\n");
                Copy.copy(s[1].toString(), getContext().getApplicationContext());
                showTip("已复制到剪贴板");
            }
        });

        translate_ib_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s = translate_result.getText().toString().split("\n");
                speechSynthesizers.SpeechSynthesizers(s[1].toString(), getContext().getApplicationContext());
            }
        });

        translate_rb_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.toString().equals("")) {
                    dbHelper = new DbHelper(getContext().getApplicationContext());
                    Cursor cursor = dbHelper.query(dbHelper.DB_NAME, null, null, null, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(cursor.getColumnIndex("collection")) == 2) {
                                if (cursor.getString(cursor.getColumnIndex("tv1")).equals(translate_resultContent.getText().toString())) {
                                    showTip("您已经收藏过了！");
                                    collection = 2;
                                } else {
                                    collection = 1;
                                }
                            }
                            if (collection != 2) {
                                TranslateInfo translateInfo = new TranslateInfo();
                                translateInfo.setTv1(translate_resultContent.getText().toString());
                                translateInfo.setCollection(2);
                                dbHelper.update(translateInfo);
                                showTip("收藏成功");
                            }
                        }
                    }
                } else {
                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    CollectExcel excel = new CollectExcel();
                    excel.setCloudWord(translate_et.getText().toString());
                    excel.setTranslateWord(stringResult);
                    excel.setAuthor(user);
                    excel.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                            } else {
//                           Toast.makeText(getContext(), e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("LM", e.getMessage() + e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                        {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                translate_ll_result.setVisibility(View.VISIBLE);
                                                translate_pb.setVisibility(View.VISIBLE);
                                                TextView textView = (TextView) view.findViewById(R.id.item_tv1);
                                                translate_resultContent.setText(textView.getText().toString());
                                                translate_et.setText(textView.getText().toString());
                                                String content = translate_resultContent.getText().toString().trim();
                                                Translate_Result translate_result1 = new Translate_Result();
                                                try {
                                                    translate_result1.Tests(content, new SetResult() {
                                                        @Override
                                                        public void oint(String str) {
                                                            translate_result.setText(str);
                                                            translate_pb.setVisibility(View.GONE);
                                                            listView.setVisibility(View.GONE);
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
        );
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("clear");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sure = intent.getExtras().getInt("sure");
            }
        };
        getActivity().registerReceiver(receiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("com.example.anzhuo.translator.userName");
        receiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                userName = intent.getStringExtra("userName").toString();
                if (!userName.toString().equals("")) {
                    Intent intent3 = new Intent();
                    intent3.setAction("user1");
                    intent3.putExtra("u1", userName.toString());
                    getContext().sendBroadcast(intent);
                }
            }
        };
        getActivity().registerReceiver(receiver1, filter1);


    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        String s = resultBuffer.toString().substring(1, resultBuffer.toString().length());
        translate_et.setText(s);
        if (!translate_et.getText().equals("")) {
            translate_bt.setTextColor(Color.BLUE);
            translate_bt.setEnabled(true);
        }
    }

    public void setParam() {
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((inputMethodManager != null) && this.getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sure == 1) {
            translate_et.setText("");
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(receiver1);
    }

}
