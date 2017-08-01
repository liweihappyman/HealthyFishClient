package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.healthyfish.healthyfish.POJO.BeanSelectDate;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：自定义计划选择养生日GridView适配器
 * 作者：LYQ on 2017/7/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class CustomSchemeSelectDateGvAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanSelectDate> mDate;
    private int selectedNumber = 0;
    private Handler mHandler;
    public final int mesFlag = 102;

    public CustomSchemeSelectDateGvAdapter(Context mContext, List<BeanSelectDate> mDate, Handler handler) {
        this.mContext = mContext;
        this.mDate = mDate;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mHandler = handler;
    }

    public List<BeanSelectDate> getList() {
        return mDate;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_custom_scheme_select_date_gv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mDate.get(position).getDate())) {
            holder.ckbSelectDate.setText(mDate.get(position).getDate().substring(8,10));
        } else {
            holder.ckbSelectDate.setText("");
            holder.ckbSelectDate.setEnabled(false);
        }
        holder.ckbSelectDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mDate.get(position).setCheck(true);
                    selectedNumber++;
                    updateTv();
                }else {
                    mDate.get(position).setCheck(false);
                    selectedNumber--;
                    updateTv();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ckb_select_date)
        CheckBox ckbSelectDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 发送已选日期数，更新页面显示
     */
    private void updateTv(){
        Message message = new Message();
        message.what = mesFlag;
        message.arg1 =selectedNumber;
        mHandler.sendMessage(message);
    }
}
