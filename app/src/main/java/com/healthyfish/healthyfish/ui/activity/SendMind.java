package com.healthyfish.healthyfish.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：送心意页面
 * 作者：LYQ on 2017/7/3.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SendMind extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_doctor_img)
    CircleImageView civDoctorImg;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.rbt_five_yuan)
    RadioButton rbtFiveYuan;
    @BindView(R.id.rbt_ten_yuan)
    RadioButton rbtTenYuan;
    @BindView(R.id.rbt_fifteen_yuan)
    RadioButton rbtFifteenYuan;
    @BindView(R.id.rbt_twenty_yuan)
    RadioButton rbtTwentyYuan;
    @BindView(R.id.rbt_more_price)
    RadioButton rbtMorePrice;
    @BindView(R.id.rgp_choice_figure)
    RadioGroup rgpChoiceFigure;
    @BindView(R.id.et_thinks)
    EditText etThinks;
    @BindView(R.id.bt_commit)
    Button btCommit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mind);
        ButterKnife.bind(this);
        mContext = this;
        initToolBar(toolbar,tvTitle,"送心意");
        initData();
    }

    @OnClick({R.id.rbt_more_price, R.id.bt_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbt_more_price:
                //选择更多答谢金额，弹出输入框
                break;
            case R.id.bt_commit:
                //提交感谢语和支付答谢金额
                getFigureAndThinks();
                break;
        }
    }

    /**
     * 获取答谢金额数
     */
    private void getFigureAndThinks() {
        if (findViewById(rgpChoiceFigure.getCheckedRadioButtonId()) != null) {
            RadioButton radioButton = (RadioButton) findViewById(rgpChoiceFigure.getCheckedRadioButtonId());
            if (etThinks.getText().toString() != null) {
                Intent intent = new Intent(this, Pay.class);
                Bundle bundle = new Bundle();
                bundle.putString("serviceType", "送心意");
                bundle.putString("name", tvDoctorName.getText().toString());
                bundle.putString("price", radioButton.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                MyToast.showToast(mContext, "请输入感谢语");
            }
        } else {
            MyToast.showToast(mContext, "请选择答谢金额");
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(mContext).load(bundle.get("imgUrl")).into(civDoctorImg);
            tvDoctorName.setText(bundle.get("name") + "医生");
        }
    }
}
