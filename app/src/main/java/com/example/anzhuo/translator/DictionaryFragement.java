package com.example.anzhuo.translator;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anzhuo on 2016/10/25.
 */
public class DictionaryFragement extends Fragment {
    //    View view;
    private ProgressBar progressBar;
    private ImageView img_Dailysentence;
    private TextView engtv_Dailysentence;
    private TextView chitv_Dailysentence;
    private EditText etInput;
    private Button btnTranslate;
    private TextView etResult;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private String from = "auto";
    String url = "http://open.iciba.com/dsapi";
    private String to = "auto";
    String[] language = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht"};
    String[] languages = {"en", "zh", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom"
            , "slo", "swe", "hu", "cht"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionaryfragement, container, false);
        etInput = (EditText) view.findViewById(R.id.et_input);
        btnTranslate = (Button) view.findViewById(R.id.translate_bt);
        etResult = (TextView) view.findViewById(R.id.et_result);
        spinnerFrom = (Spinner) view.findViewById(R.id.spinner_from);
        spinnerTo = (Spinner) view.findViewById(R.id.spinner_to);
        img_Dailysentence = (ImageView) view.findViewById(R.id.img_Dailysentence);
        engtv_Dailysentence = (TextView) view.findViewById(R.id.engtv_Dailysentence);
        chitv_Dailysentence = (TextView) view.findViewById(R.id.chitv_Dailysentence);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        changeLight(img_Dailysentence, -70);

        new Dailysentence().execute(url);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String request = etInput.getText().toString();
                RequestUtils requestUtils = new RequestUtils();
                if (!request.isEmpty()) {
                    try {
                        requestUtils.translate(request, from, to, new HttpCallBack() {
                            @Override
                            public void onSuccess(String result) {
                                etResult.setText(result);
                                etResult.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(String exception) {
                                etResult.setText(exception);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "请输入要翻译的内容!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = language[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = languages[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    class Dailysentence extends AsyncTask<String, String, String> {
        String engstr;
        String chistr;
        String imgurl;

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];
            try {
                //
                Thread.sleep(5000);
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                InputStream is = new BufferedInputStream(connection.getInputStream());
                StringBuffer stringBuffer = new StringBuffer();
                byte[] by = new byte[10 * 1024];
                int len;
                while ((len = is.read(by)) != -1) {
                    stringBuffer.append(new String(by, 0, len));
                }
                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                engstr = jsonObject.getString("content");
                chistr = jsonObject.getString("note");
                imgurl = jsonObject.getString("picture2");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            img_Dailysentence.setVisibility(View.VISIBLE);
            engtv_Dailysentence.setText(engstr);
            chitv_Dailysentence.setText(chistr);
            Picasso.with(getContext().getApplicationContext()).load(imgurl).into(img_Dailysentence);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    private void changeLight(ImageView imageView, int brightness) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
}
