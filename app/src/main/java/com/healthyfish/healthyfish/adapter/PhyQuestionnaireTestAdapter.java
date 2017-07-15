package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanPhyQuestionnaireTest;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：健康管理体质测试问卷ListView适配器
 * 作者：LYQ on 2017/7/11.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PhyQuestionnaireTestAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanPhyQuestionnaireTest> mList;

    public PhyQuestionnaireTestAdapter(Context mContext, List<BeanPhyQuestionnaireTest> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public List<BeanPhyQuestionnaireTest> getData() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        BeanPhyQuestionnaireTest beanPhyTest = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_phy_ide_questionnaire_test, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(position + 1 + "." + beanPhyTest.getContent());//设置测试问卷题目标题
        holder.rgpPhyTest.setTag(position);//设置RadioGroup标记，标记每一个item的RadioGroup，用来区分不同item的RadioGroup
        holder.rgpPhyTest.setOnCheckedChangeListener(null);//取消RadioGroup的监听机制，要在item的选择情况重新赋值之前清除RadioButton的选择监听，否则会复制上面的item的选择情况
        //获取item的选择情况，重新赋值（如果之前已经选择了某一个答案，则在相应的答案上显示，如果之前没有选择答案则清除item的选择）
        if (BeanPhyQuestionnaireTest.answers[0].equals(beanPhyTest.getAnswer())) {
            holder.rgpPhyTest.check(holder.rbtNot.getId());
        } else if (BeanPhyQuestionnaireTest.answers[1].equals(beanPhyTest.getAnswer())) {
            holder.rgpPhyTest.check(holder.rbtLittle.getId());
        } else if (BeanPhyQuestionnaireTest.answers[2].equals(beanPhyTest.getAnswer())) {
            holder.rgpPhyTest.check(holder.rbtSometimes.getId());
        } else if (BeanPhyQuestionnaireTest.answers[3].equals(beanPhyTest.getAnswer())) {
            holder.rgpPhyTest.check(holder.rbtOften.getId());
        } else if (BeanPhyQuestionnaireTest.answers[4].equals(beanPhyTest.getAnswer())) {
            holder.rgpPhyTest.check(holder.rbtAlways.getId());
        } else {
            holder.rgpPhyTest.clearCheck();
        }

        //设置选项是否可以收起和展开
        final ViewHolder finalHolder = holder;
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.rgpPhyTest.getVisibility() == View.VISIBLE) {
                    finalHolder.rgpPhyTest.setVisibility(View.GONE);
                } else {
                    finalHolder.rgpPhyTest.setVisibility(View.VISIBLE);
                }
            }
        });

        //设置RadioGroup的监听，将item的选择情况保存到bean里面
        holder.rgpPhyTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BeanPhyQuestionnaireTest beanPhyTest = mList.get(((Integer) group.getTag()));
                if (group == finalHolder.rgpPhyTest) {
                    switch (checkedId) {
                        case R.id.rbt_not:
                            beanPhyTest.setAnswer(BeanPhyQuestionnaireTest.answers[0]);
                            break;
                        case R.id.rbt_little:
                            beanPhyTest.setAnswer(BeanPhyQuestionnaireTest.answers[1]);
                            break;
                        case R.id.rbt_sometimes:
                            beanPhyTest.setAnswer(BeanPhyQuestionnaireTest.answers[2]);
                            break;
                        case R.id.rbt_often:
                            beanPhyTest.setAnswer(BeanPhyQuestionnaireTest.answers[3]);
                            break;
                        case R.id.rbt_always:
                            beanPhyTest.setAnswer(BeanPhyQuestionnaireTest.answers[4]);
                            break;
                    }
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rbt_not)
        RadioButton rbtNot;
        @BindView(R.id.rbt_little)
        RadioButton rbtLittle;
        @BindView(R.id.rbt_sometimes)
        RadioButton rbtSometimes;
        @BindView(R.id.rbt_often)
        RadioButton rbtOften;
        @BindView(R.id.rbt_always)
        RadioButton rbtAlways;
        @BindView(R.id.rgp_phy_test)
        RadioGroup rgpPhyTest;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
