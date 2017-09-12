package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanBaseKeySetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanServiceList;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.DateUtils;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.MyApplication.uid;

/**
 * 描述：问诊支付页面
 * 作者：LYQ on 2017/7/4.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class Pay extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_name_service)
    TextView tvNameService;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.cb_one_week)
    CheckBox cbOneWeek;
    @BindView(R.id.cb_three_month)
    CheckBox cbThreeMonth;
    @BindView(R.id.cb_one_year)
    CheckBox cbOneYear;
    @BindView(R.id.cb_one_month)
    CheckBox cbOneMonth;
    @BindView(R.id.cb_half_year)
    CheckBox cbHalfYear;
    @BindView(R.id.tv_expiration_date)
    TextView tvExpirationDate;
    @BindView(R.id.lly_choice_package)
    AutoLinearLayout llyChoicePackage;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.cb_balance_pay)
    CheckBox cbBalancePay;
    @BindView(R.id.lly_balance_pay)
    AutoLinearLayout llyBalancePay;
    @BindView(R.id.cb_alipay_pay)
    CheckBox cbAlipayPay;
    @BindView(R.id.lly_alipay_pay)
    AutoLinearLayout llyAlipayPay;
    @BindView(R.id.btn_pay_confirm)
    Button btnPayConfirm;
    @BindView(R.id.lly_pay)
    AutoLinearLayout llyPay;


    private String shopType;
    private String strPayPrice;
    private String doctorName;
    private Bundle bundleShopType;
    private int serviceData = 2;  //图文咨询的服务时间长度
    private String payPrice01, payPrice02, payPrice03, payPrice04, payPrice05;
    private BeanDoctorChatInfo beanDoctorChatInfo = new BeanDoctorChatInfo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        //初始化ToolBar
        initToolBar(toolbar, tvTitle, "购买服务");
        //获取上个页面传递的数据
        getFormerPagerData();
        //初始化基本数据
        initData();
        //根据服务类型设置标题
        setTitle();
        //根据服务类型设置选择套餐
        ChoicePackage();
    }

    /**
     * 所有点击事件的监听
     *
     * @param view
     */
    @OnClick({R.id.lly_balance_pay, R.id.lly_alipay_pay, R.id.btn_pay_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_balance_pay:
                //如果已经选择了余额支付，则取消选择，如果没有则选择余额支付，取消支付宝支付的选择
                if (cbBalancePay.isChecked()) {
                    cbBalancePay.setChecked(false);
                } else {
                    cbBalancePay.setChecked(true);
                    cbAlipayPay.setChecked(false);
                }
                break;
            case R.id.lly_alipay_pay:
                //如果已经选择了支付宝支付，则取消选择，如果没有则选择支付宝支付，取消余额支付的选择
                if (cbAlipayPay.isChecked()) {
                    cbAlipayPay.setChecked(false);
                } else {
                    cbAlipayPay.setChecked(true);
                    cbBalancePay.setChecked(false);
                }
                break;
            case R.id.btn_pay_confirm:
                if (shopType.equals("私人医生")) {
                    if (!cbOneWeek.isChecked() && !cbOneMonth.isChecked() && !cbThreeMonth.isChecked()
                            && !cbHalfYear.isChecked() && !cbOneYear.isChecked()) {
                        MyToast.showToast(this, "请选择服务套餐");
                    } else if (!cbBalancePay.isChecked() && !cbAlipayPay.isChecked()) {
                        MyToast.showToast(this, "请选择支付方式");
                    } else {
                        bundleShopType.putString("serviceFinishTime", "服务时间为" + DateUtils.addAndSubtractDate("D", 0) + "至" + tvExpirationDate.getText().toString());
                        jumpTo(PayServiceSuccess.class);
                    }
                } else if (!cbBalancePay.isChecked() && !cbAlipayPay.isChecked()) {
                    MyToast.showToast(this, "请选择支付方式");
                } else if (shopType.equals("送心意")) {
                    jumpTo(PaySuccess.class);
                } else {
                    bundleShopType.putString("serviceFinishTime", "服务时间为" + DateUtils.addAndSubtractDate("D", 0) + "至" + DateUtils.addAndSubtractDate("D", serviceData));
                    jumpTo(PayServiceSuccess.class);
                    addServiceListReq();//将购买服务记录上传服务器
                }
                break;
        }
    }

    /**
     * 获取上个页面传递的数据
     */
    public void getFormerPagerData() {
        Intent intent = this.getIntent();
        if (intent.getExtras() != null) {
            bundleShopType = intent.getExtras();
            shopType = bundleShopType.getString("serviceType");
            strPayPrice = bundleShopType.getString("price");
            doctorName = bundleShopType.getString("name");
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvNameService.setText(shopType + "-" + doctorName);
        tvPrice.setText(strPayPrice);
    }

    /**
     * 根据服务类型设置标题
     */
    private void setTitle() {
        if (shopType.equals("送心意")) {
            tvTitle.setText("送心意");
        } else {
            tvTitle.setText("购买服务");
            llyPay.setVisibility(View.GONE);
            cbBalancePay.setChecked(true);//设置选中其中一种支付方式，避免弹出请选择支付方式的提示而不能成功跳转下一页面
            btnPayConfirm.setText("确认购买");
        }
    }

    /**
     * 根据服务类型设置选择套餐
     */
    private void ChoicePackage() {
        if (shopType.equals("私人医生")) {
            //获取套餐服务价格
            getServicePrice();
            //服务套餐选择
            selectPackage();
            //选择套餐后还需设置服务到期时间
        }
    }

    /**
     * 模拟套餐服务价格
     */
    private void getServicePrice() {
        //模拟的数据，私人服务套餐的价格
        payPrice01 = "50.00";
        payPrice02 = "150.00";
        payPrice03 = "450.00";
        payPrice04 = "750.00";
        payPrice05 = "950.00";
    }

    /**
     * 套餐选择操作
     */
    public void selectPackage() {
        LinearLayout llyPrivate = (LinearLayout) findViewById(R.id.lly_choice_package);
        llyPrivate.setVisibility(View.VISIBLE);

        cbOneWeek.setText("一周" + "（" + payPrice01 + "元" + "）");
        cbOneMonth.setText("一月" + "（" + payPrice02 + "元" + "）");
        cbThreeMonth.setText("三月" + "（" + payPrice03 + "元" + "）");
        cbHalfYear.setText("半年" + "（" + payPrice04 + "元" + "）");
        cbOneYear.setText("一年" + "（" + payPrice05 + "元" + "）");

        cbOneWeek.setChecked(true);
        tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 7));

        cbOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbOneWeek.isChecked()) {
                    tvPrice.setText(payPrice01 + "元");
                    cbOneMonth.setChecked(false);
                    cbThreeMonth.setChecked(false);
                    cbHalfYear.setChecked(false);
                    cbOneYear.setChecked(false);
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 7));
                } else {
                    tvPrice.setText("0元");
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 0));
                }
            }
        });

        cbOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbOneMonth.isChecked()) {
                    tvPrice.setText(payPrice02 + "元");
                    cbOneWeek.setChecked(false);
                    cbThreeMonth.setChecked(false);
                    cbHalfYear.setChecked(false);
                    cbOneYear.setChecked(false);
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("M", 1));
                } else {
                    tvPrice.setText("0元");
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 0));
                }
            }
        });

        cbThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbThreeMonth.isChecked()) {
                    tvPrice.setText(payPrice03 + "元");
                    cbOneMonth.setChecked(false);
                    cbOneWeek.setChecked(false);
                    cbHalfYear.setChecked(false);
                    cbOneYear.setChecked(false);
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("m", 3));
                } else {
                    tvPrice.setText("0元");
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 0));
                }
            }
        });

        cbHalfYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHalfYear.isChecked()) {
                    tvPrice.setText(payPrice04 + "元");
                    cbOneMonth.setChecked(false);
                    cbOneMonth.setChecked(false);
                    cbOneWeek.setChecked(false);
                    cbOneYear.setChecked(false);
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("M", 6));
                } else {
                    tvPrice.setText("0元");
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 0));
                }
            }
        });

        cbOneYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbOneYear.isChecked()) {
                    tvPrice.setText(payPrice05 + "元");
                    cbOneMonth.setChecked(false);
                    cbThreeMonth.setChecked(false);
                    cbHalfYear.setChecked(false);
                    cbOneWeek.setChecked(false);
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("Y", 1));
                } else {
                    tvPrice.setText("0元");
                    tvExpirationDate.setText(DateUtils.addAndSubtractDate("D", 0));
                }
            }
        });
    }

    /**
     * 页面跳转
     *
     * @param cla
     */
    public void jumpTo(Class<? extends Activity> cla) {
        Intent intent = new Intent(this, cla);
        intent.putExtras(bundleShopType);
        startActivity(intent);
    }

    private void addServiceListReq() {

        String dateFormat = "yyyy-MM-dd HH-mm";

        String startTime = DateUtils.getCurrentDate(dateFormat);
        Log.i("LYQ", "startTime:" + startTime);

        String endTime = DateUtils.addOrSubtractDate(dateFormat, "D", serviceData);
        Log.i("LYQ", "endTime:" + endTime);

        final BeanServiceList beanServiceList = (BeanServiceList) bundleShopType.getSerializable("BeanServiceList");
        beanServiceList.setStartTime(startTime);
        //// TODO: 2017/9/12 测试过期时间
        beanServiceList.setEndTime(endTime);
        String serviceListJson = JSON.toJSONString(beanServiceList);

        Log.i("LYQ", "serviceListJson:" + serviceListJson);
        final String serviceKey = beanServiceList.getKey();
        final String phoneNumber = beanServiceList.getPhoneNumber();
        BeanBaseKeySetReq beanBaseKeySetReq = new BeanBaseKeySetReq();
        beanBaseKeySetReq.setKey(serviceKey);
        beanBaseKeySetReq.setValue(serviceListJson);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeySetReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                BeanBaseResp beanBaseResp = JSON.parseObject(strJson, BeanBaseResp.class);
                if (beanBaseResp.getCode() == 0) {
                    MyToast.showToast(Pay.this, "购买服务成功");
                    beanServiceList.saveOrUpdate("phoneNumber = ?", phoneNumber);//保存已购买的服务
                    jumpTo(PayServiceSuccess.class);
                } else {
                    MyToast.showToast(Pay.this, "购买服务失败，请重试");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "addServiceListReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", "上传已购买服务响应：" + strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
