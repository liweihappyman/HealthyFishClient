package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：选择病历页面listview的适配器
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class SelectTimeAdapter extends BaseAdapter {
    private int mPosition;
    private SelListener mylistener;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<String> list;

    HashMap<String, Boolean> states = new HashMap<String, Boolean>();//用于记录每个checkbox的状态，并保证只可选一个


    public SelectTimeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
        //this.mylistener = myListener;
    }


    public interface SelListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void getSelId(List<String> ListID);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        SingleView singleView = new SingleView(context);
        singleView.setTitle(list.get(position));
        return singleView;

//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mLayoutInflater.inflate(R.layout.item_choose_time, viewGroup, false);//填充Listview的item视图
//            holder.time = (TextView) convertView.findViewById(R.id.time);
//            holder.checkBox = (CheckBox) convertView.findViewById(R.id.selected_state);
//            convertView.setTag(holder);
//            AutoUtils.auto(convertView);
//
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.time.setText(list.get(position));
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //重置，确保最多只有一项被选中
//                for (String key : states.keySet()) {
//                    states.put(key, false);
//
//                }
//                mPosition = position;//用接口将数据传出去
//                states.put(String.valueOf(position), holder.checkBox.isChecked());
//                SelectTimeAdapter.this.notifyDataSetChanged();
//            }
//        });
    }

    class ViewHolder {
        private TextView time;
        private CheckBox checkBox;
    }

    /**
     * 描述：具有单选功能的单选框
     * 作者：LYQ on 2017/7/6.
     * 邮箱：feifanman@qq.com
     * 编辑：LYQ
     */

    public class SingleView extends LinearLayout implements Checkable {


        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.selected_state)
        CheckBox selectedState;

        public SingleView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initView(context);
        }

        public SingleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }

        public SingleView(Context context) {
            super(context);
            initView(context);
        }

        public View initView(Context context) {
            // 填充布局
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_choose_time, this, true);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void setChecked(boolean checked) {
            selectedState.setChecked(checked);
        }

        @Override
        public boolean isChecked() {
            return selectedState.isChecked();
        }

        @Override
        public void toggle() {
            selectedState.toggle();
        }

        public void setTitle(String text) {
            time.setText(text);
        }
    }

}
