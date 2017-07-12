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

    public PhyGvAdapter(Context mContext, List<String> phyNames, int maxSelectNum) {
        this.mContext = mContext;
        this.phyNames = phyNames;
        this.maxSelectNum = maxSelectNum;
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ckbPhy.setText(phyNames.get(position));//设置标题
        holder.tvOrder.setText("0");//这一步很关键，刚开始必须全部初始化排序标号为0，用来标识item的选中状态。（在item布局里设置为0是无效的）
        //拿到item里的控件列表。将所有的CheckBox和TextView保存到集合，用来遍历比较和设置，要保证集合里的CheckBox和TextView和item的顺序一致
        if (ckbList.size() < phyNames.size()) {  //控制集合数与item数一致（因为getView方法会反复执行，不做控制集合数会无限增加）
            if (ckbList.size() > 0 && ckbList.size() > position) {   //当集合数大于position时表明集合已存在当前CheckBox和TextView，则按位置更改集合里面的元素（因为getView方法有时是不限次数遍历item的）
                ckbList.set(position, holder.ckbPhy);
                tvOrderList.set(position, holder.tvOrder);
            } else { //当集合数小于position时表明集合里面没有当前CheckBox和TextView，则将当前CheckBox和TextView添加到集合里
                ckbList.add(position, holder.ckbPhy);
                tvOrderList.add(position, holder.tvOrder);
            }
        }
        //设置CheckBox的选中状态改变监听，并依此控制item的选中状态和可选状态以及选中的排序
        final ViewHolder finalHolder = holder;
        holder.ckbPhy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //点击后选中的时候执行
                    selectNum++;//选中总数加一
                    finalHolder.tvOrder.setText(String.valueOf(selectNum));//设置排序号
                    finalHolder.tvOrder.setVisibility(View.VISIBLE);//显示排序
                    //判断是否到达可选数量上限
                    if (selectNum >= maxSelectNum) {
                        for (int i = 0; i < ckbList.size(); i++) {   //遍历所有的排序号
                            if (tvOrderList.get(i).getText().toString() == "0") {    //排序为0则表明该item未选中
                                ckbList.get(i).setEnabled(false);   //将该item设置为不可选状态
                            }
                        }
                    }
                } else {//点击后取消选中的时候执行
                    selectNum--;    //选中总数减一
                    int orderNumber = Integer.valueOf(finalHolder.tvOrder.getText().toString());    //获取当前item的排序
                    finalHolder.tvOrder.setText("0");   //将该item排序置0
                    finalHolder.tvOrder.setVisibility(View.GONE);   //隐藏排序
                    //遍历所有的item重新排序当前排序号之后的排序
                    for (int i = 0; i < ckbList.size(); i++) {
                        if (Integer.valueOf(tvOrderList.get(i).getText().toString()) > orderNumber) {    //找到大于当前排序号的item
                            tvOrderList.get(i).setText(String.valueOf(Integer.valueOf(tvOrderList.get(i).getText().toString()) - 1)); //将该排序号减一
                        }
                    }
                    //判断目前选中数量是否小于设置的选中上限
                    if (selectNum < maxSelectNum) {
                        for (int i = 0; i < ckbList.size(); i++) {   //遍历所有的item
                            if (tvOrderList.get(i).getText().toString() == "0") {    //找到未选中的item
                                ckbList.get(i).setEnabled(true);    //将未选中的item全部设置为可选状态
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
