package com.example.anzhuo.translator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by anzhuo on 2016/10/27.
 */
public class Translate_Result {
    private static final String UTF8 = "utf-8";
    private static final int SUCCEE_RESULT = 10;
    private static final int ERROR_TEXT_TOO_LONG = 20;
    private static final int ERROR_CANNOT_TRANSLATE = 30;
    private static final int ERROR_UNSUPPORT_LANGUAGE = 40;
    private static final int ERROR_WRONG_KEY = 50;
    private static final int ERROR_WRONG_RESULT = 60;
    private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
    private String YouDaoKeyFrom = "satisfaction";
    private String YouDaoKey = "908062580";
    private String YouDaoType = "data";
    private String YouDaoDoctype = "json";
    private String YouDaoVersion = "1.1";
    TranslateHandler handler;

    public void Tests(final String str, final SetResult setResult) throws Exception {
        final URL urll = new URL(YouDaoBaseUrl + "?keyfrom="
                + YouDaoKeyFrom + "&key=" + YouDaoKey + "&type="
                + YouDaoType + "&doctype=" + YouDaoDoctype + "&version="
                + YouDaoVersion + "&q=" + URLEncoder.encode(str, "utf-8"));
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection httpURLConnection = null;
                String message = null;
                try {
                    httpURLConnection = (HttpURLConnection) urll.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        builder.append(str).append("\n");
                    }
                    JSONObject resultJson = new JSONObject(builder.toString());
                    String errorCode = resultJson.getString("errorCode");
                    if (errorCode.equals("20")) {
                        handler.sendEmptyMessage(ERROR_TEXT_TOO_LONG);
                    } else if (errorCode.equals("30")) {
                        handler.sendEmptyMessage(ERROR_CANNOT_TRANSLATE);
                    } else if (errorCode.equals("40")) {
                        handler.sendEmptyMessage(ERROR_UNSUPPORT_LANGUAGE);
                    } else if (errorCode.equals("50")) {
                        handler.sendEmptyMessage(ERROR_WRONG_KEY);
                    } else {
                        Message msg = new Message();
                        msg.what = SUCCEE_RESULT;
                        String query = resultJson.getString("query");
                        message = "翻译结果";
                        //翻译结果
                        Gson gson = new Gson();
                        Type lt = new TypeToken<String[]>() {
                        }.getType();
                        String[] translations = gson.fromJson(resultJson.getString("translation"), lt);
                        for (String translation : translations) {
                            message += "\n" + translation;
                        }
                        if (resultJson.has("basic")) {
                            JSONObject basic = resultJson.getJSONObject("basic");
                            if (basic.has("phonetic")) {
                                String phonetic = basic.getString("phonetic");
                                message += "\n" + phonetic;
                            }
                            if (basic.has("explains")) {
                                String explains = basic.getString("explains");
                                message += "\t" + explains;
                            }
                        }
                        if (resultJson.has("web")) {
                            String web = resultJson.getString("web");
                            JSONArray webString = new JSONArray("[" + web + "]");
                            message += "\n网络释义";
                            JSONArray webArray = webString.getJSONArray(0);
                            int count = 0;
                            while (!webArray.isNull(count)) {
                                if (webArray.getJSONObject(count).has("key")) {
                                    String key = webArray.getJSONObject(count).getString("key");
                                    message += "\n(" + (count + 1) + ")" + key + "\n";
                                }
                                if (webArray.getJSONObject(count).has("value")) {
                                    String[] values = gson.fromJson(webArray.getJSONObject(count).getString("value"), lt);
                                    for (int j = 0; j < values.length; j++) {
                                        String value = values[j];
                                        message += value;
                                        if (j < values.length - 1) {
                                            message += ",";
                                        }
                                    }
                                }
                                count++;
                            }
                            message = URLDecoder.decode(message, UTF8);
                        }
                        msg.obj = message;

                        //      handler.sendMessage(msg);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return message;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                setResult.oint(s);
            }
        }.execute();

    }


    private class TranslateHandler extends Handler {
        private Context mContext;
        private TextView mTextView;

        public TranslateHandler(Context context, TextView textView) {
            this.mContext = context;
            this.mTextView = textView;


        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCEE_RESULT:
                    mTextView.setText((String) msg.obj);
                    TranslateFragement Translate = new TranslateFragement();
                    Translate.closeInput();
                    break;
                case ERROR_TEXT_TOO_LONG:
                    Toast.makeText(mContext, "要翻译的文本过长", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CANNOT_TRANSLATE:
                    Toast.makeText(mContext, "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_UNSUPPORT_LANGUAGE:
                    Toast.makeText(mContext, "不支持的语言类型", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_WRONG_KEY:
                    Toast.makeText(mContext, "无效的Key", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_WRONG_RESULT:
                    Toast.makeText(mContext, "提取异常", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
