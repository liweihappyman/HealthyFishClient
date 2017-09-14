package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择病历页面listview的适配器
 * 作者：WKJ on 2017/7/6.
 * 邮箱：
 * 编辑：WKJ
 */

public class SelectMedRecAdapter extends BaseAdapter {
    private SelMRBListener mylistener;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<BeanMedRec> list;

    public SelectMedRecAdapter(Context context, List<BeanMedRec> list, SelMRBListener myListener) {
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
        this.mylistener = myListener;
    }


    public interface SelMRBListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void getSelId(List<String> listKeys);
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
        final int index = position;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_select_med_rec, viewGroup,false);//填充Listview的item视图
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.imageCheckbox = (CheckBox) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getClinicalTime());
        holder.description.setText(list.get(position).getDiseaseInfo());
        holder.imageCheckbox.setChecked(list.get(position).isSelect());
        holder.imageCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isSelect()){
                    list.get(position).setSelect(false);
                }else {
                    list.get(position).setSelect(true);
                }
                traverseData();
            }
        });
        return convertView;
    }

    //遍历选出选中的数据的key，在接口的另一边通过实现接口的方法获得key，从数据库选出相应key的数据
    private void traverseData() {
        List<String> selectData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect()) {
                //获取被选中的病历夹的唯一标识
                selectData.add(list.get(i).getKey());
            }
        }
        mylistener.getSelId(selectData);
    }

    class ViewHolder {
        private TextView name;
        private TextView date;
        private TextView description;
        private CheckBox imageCheckbox;
    }
}
