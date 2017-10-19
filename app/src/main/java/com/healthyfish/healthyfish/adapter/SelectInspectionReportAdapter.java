package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanInspectionReport;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：选择病历页面listview的适配器
 * 作者：WKJ on 2017/7/6.
 * 邮箱：
 * 编辑：WKJ
 */

public class SelectInspectionReportAdapter extends BaseAdapter {
    private SelReportListener myListener;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<BeanInspectionReport> list;
    private List<Map<String,Boolean>> listIsSelect;

    public SelectInspectionReportAdapter(Context context, List<BeanInspectionReport> list,List<Map<String,Boolean>> listIsSelect, SelReportListener myListener) {
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
        this.myListener = myListener;
        this.listIsSelect = listIsSelect;
    }


    public interface SelReportListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void getSelKeys(List<String> listKeys);
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
            convertView = mLayoutInflater.inflate(R.layout.item_inspection_report_share, viewGroup, false);//填充Listview的item视图
            holder.ireptItemName = (TextView) convertView.findViewById(R.id.irept_item_name);
            holder.selectedCheckBox = (CheckBox) convertView.findViewById(R.id.selected);
            holder.ireptPatientName = (TextView) convertView.findViewById(R.id.irept_patient_name);
            holder.ireptHospName = (TextView) convertView.findViewById(R.id.irept_hosp_name);
            holder.ireptRequestedDate = (TextView) convertView.findViewById(R.id.irept_requested_date);
            holder.ireptPatientId = (TextView) convertView.findViewById(R.id.irept_patient_id);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ireptItemName.setText(list.get(position).getITEM_NAME());
        holder.selectedCheckBox.setChecked(listIsSelect.get(position).get("isSelect"));
        holder.ireptPatientName.setText(list.get(position).getPATIENT_NAME());
        holder.ireptHospName.setText(list.get(position).getHOSPITAL_MARK());
        holder.ireptRequestedDate.setText(list.get(position).getREQUESTED_DATE());
        holder.ireptPatientId.setText(list.get(position).getPATIENT_ID());

        holder.selectedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listIsSelect.get(position).get("isSelect")) {
                    listIsSelect.get(position).put("isSelect",false);
                } else {
                    listIsSelect.get(position).put("isSelect",true);
                }
                traverseData();
            }
        });
        return convertView;
    }

    //遍历选出选中的数据的key，在接口的另一边通过实现接口的方法获得key，从数据库选出相应key的数据
    private void traverseData() {
        List<String> selectData = new ArrayList<>();
        for (int i = 0; i < listIsSelect.size(); i++) {
            if (listIsSelect.get(i).get("isSelect")) {
                //获取被选中的病历夹的唯一标识
                selectData.add(list.get(i).getKey());
            }
        }
        myListener.getSelKeys(selectData);
    }

    static class ViewHolder {
        private TextView ireptItemName;
        private CheckBox selectedCheckBox;
        private TextView ireptPatientName;
        private TextView ireptHospName;
        private TextView ireptRequestedDate;
        private TextView ireptPatientId;
    }
}
