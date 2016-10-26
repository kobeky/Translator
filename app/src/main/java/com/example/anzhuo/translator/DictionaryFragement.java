package com.example.anzhuo.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anzhuo on 2016/10/25.
 */
public class DictionaryFragement extends Fragment {
    View view;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dictionaryfragement,container,false);
        textView= (TextView) view.findViewById(R.id.tv);
        textView.setText("zxcxzcz");
        return view;
    }
}
