package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：红外皮温页面选择体质的GridView适配器
 * 作者：LYQ on 2017/7/11.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PhyGvAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> phyNames = new ArrayList<>();

    private int selectNum = 0;//已选择的体质数，初始值为0
    private int maxSelectNum = 3;//最大可选体质数

    private List<CheckBox> ckbList = new ArrayList<CheckBox>();
    private List<TextView> tvOrderList = new ArrayList<TextView>();

    public PhyGvAdapter(Context mContext, List<String> phyNames) {
        this.mContext = mContext;
        this.phyNames = phyNames;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return phyNames.size();
    }

    @Override
    public Object getItem(int position) {
        return phyNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_select_phy_gv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ckbPhy.setText(phyNames.get(position));

        Log.e("LYQ","phyNames.get("+position+")"+"+++"+phyNames.get(position));

        if (ckbList.size() < phyNames.size()){
            if (ckbList.size()>0 && ckbList.size()>position){
                ckbList.set(position,holder.ckbPhy);
                tvOrderList.set(position,holder.tvOrder);
            }else {
                ckbList.add(position,holder.ckbPhy);
                tvOrderList.add(position,holder.tvOrder);
            }
        }

        Log.e("LYQ","ckbList.size()+++"+String.valueOf(Integer.valueOf(ckbList.size())));
        Log.e("LYQ","tvOrderList.size()+++"+String.valueOf(Integer.valueOf(tvOrderList.size())));

        final ViewHolder finalHolder = holder;
        holder.ckbPhy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyToast.showToast(mContext, phyNames.get(position));
                if (isChecked){
                    selectNum++;
                    finalHolder.tvOrder.setText(String.valueOf(selectNum));
                    finalHolder.tvOrder.setVisibility(View.VISIBLE);

                    if (selectNum >= maxSelectNum){
                        for (int i=0; i<ckbList.size(); i++){
                            if (tvOrderList.get(i).getText().toString() == "0"){
                                ckbList.get(i).setEnabled(false);
                            }
                        }
                    }
                }else {
                    selectNum--;
                    int orderNumber = Integer.valueOf(finalHolder.tvOrder.getText().toString());
                    finalHolder.tvOrder.setText("0");
                    finalHolder.tvOrder.setVisibility(View.GONE);

                    for (int i=0; i<ckbList.size(); i++){

                        Log.e("LYQ","ckbList.size()---"+String.valueOf(Integer.valueOf(ckbList.size())));
                        Log.e("LYQ","tvOrderList.size()---"+String.valueOf(Integer.valueOf(tvOrderList.size())));

                        Log.e("LYQ","ckbList.get("+i+")"+"---"+ckbList.get(i).getText().toString());

                        if (Integer.valueOf(tvOrderList.get(i).getText().toString()) > orderNumber){
                            tvOrderList.get(i).setText(String.valueOf(Integer.valueOf(tvOrderList.get(i).getText().toString())-1));
                        }
                    }
                    if (selectNum < maxSelectNum){
                        for (int i=0; i<ckbList.size(); i++){
                            if (tvOrderList.get(i).getText().toString() == "0"){
                                ckbList.get(i).setEnabled(true);
                            }
                        }
                    }
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ckb_phy)
        CheckBox ckbPhy;
        @BindView(R.id.tv_order)
        TextView tvOrder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
