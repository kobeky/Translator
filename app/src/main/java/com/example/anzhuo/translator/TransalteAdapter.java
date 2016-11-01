package com.example.anzhuo.translator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anzhuo on 2016/10/28.
 */
public class TransalteAdapter extends BaseAdapter{
    List<TranslateInfo>list;
    Context context;
    ViewHolder viewHolder;
    public TransalteAdapter(Context context,List<TranslateInfo>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder=new ViewHolder();
        if(view==null||view.getTag()==null){
            view= LayoutInflater.from(context).inflate(R.layout.translate_item,null);
            viewHolder.tv1= (TextView) view.findViewById(R.id.item_tv1);
            viewHolder.tv2= (TextView) view.findViewById(R.id.item_tv2);
            view.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) view.getTag();
        viewHolder.tv1.setText(list.get(i).getTv1());
        viewHolder.tv2.setText(list.get(i).getTv2());
        return view;
    }
    public class ViewHolder{
        TextView tv1;
        TextView tv2;
    }
}
