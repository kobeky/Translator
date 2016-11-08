package com.example.anzhuo.translator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anzhuo on 2016/11/4.
 */
public class CloudAdapter extends BaseAdapter implements View.OnClickListener {
    List<CloudInfo>item;
    Context mcontext;
    int mScreentWidth;
    CloudInfo info;
    public CloudAdapter (Context context, List<CloudInfo> list, int ScreentWidth){
        mcontext=context;
        item=list;
        mScreentWidth=ScreentWidth;

    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //如果没有设置过,初始化convertView
        if (convertView == null)
        {
            //获得设置的view
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_of_collect, parent, false);

            //初始化holder
            holder = new ViewHolder();

            /**
             * 整个item的水平滚动层
             */
            holder.itemHorizontalScrollView = (HorizontalScrollView) convertView.findViewById(R.id.hsv);

            /**
             * 操作按钮层
             */
            holder.actionLayout = convertView.findViewById(R.id.ll_action);
            holder.delete= (Button) convertView.findViewById(R.id.bt_delete_cloud);

            //把位置放到view中,这样点击事件就可以知道点击的是哪一条item
            holder.delete.setTag(position);
            holder.CollectWord= (TextView) convertView.findViewById(R.id.tv_Cloud);
            holder.TranslateMean= (TextView) convertView.findViewById(R.id.tv_Translate);
            //设置内容view的大小为屏幕宽度,这样按钮就正好被挤出屏幕外
            holder.normalItemContentLayout = convertView.findViewById(R.id.ll_content);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.normalItemContentLayout.getLayoutParams();
            lp.width = mScreentWidth;

            convertView.setTag(holder);
        }
        else//有直接获得ViewHolder
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置监听事件
        convertView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:

                        //获得ViewHolder
                        ViewHolder viewHolder = (ViewHolder) v.getTag();

                        //获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = viewHolder.itemHorizontalScrollView.getScrollX();

                        //获得操作区域的长度
                        int actionW = viewHolder.actionLayout.getWidth();

                        //注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
                        //如果水平方向的移动值<操作区域的长度的一半,就复原
                        if (scrollX < actionW / 2)
                        {
                            viewHolder.itemHorizontalScrollView.smoothScrollTo(0, 0);
                        }
                        else//否则的话显示操作区域
                        {
                            viewHolder.itemHorizontalScrollView.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });

        //这里防止删除一条item后,ListView处于操作状态,直接还原
        if (holder.itemHorizontalScrollView.getScrollX() != 0)
        {
            holder.itemHorizontalScrollView.scrollTo(0, 0);
        }
        //设置填充内容
        holder= (ViewHolder) convertView.getTag();
        info=new CloudInfo();
        holder.CollectWord.setText(info.getTv_cloud());
        holder.TranslateMean.setText(info.getTv_translate());

        //设置监听事件
        holder.delete.setOnClickListener(this);
        return convertView;
    }
class ViewHolder{
    HorizontalScrollView itemHorizontalScrollView;
    /**
     * 正常布局下的item
     */
    View normalItemContentLayout;
    TextView CollectWord;
    TextView TranslateMean;
    /**
     * 侧滑出现的ImageView
     */
    View actionLayout;
    Button delete;
}
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()){
            case R.id.bt_delete_cloud:

                break;
        }
        notifyDataSetChanged();
    }
}
