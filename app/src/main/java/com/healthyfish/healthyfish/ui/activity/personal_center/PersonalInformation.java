package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.activity.Login;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;


import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;



/**
 * 描述：个人中心个人信息页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PersonalInformation extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_head_portrait)
    CircleImageView civHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_birthDate)
    TextView tvBirthDate;
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idCard)
    TextView tvIdCard;
    @BindView(R.id.lly_id_card)
    AutoLinearLayout llyIdCard;

    private String uid = MyApplication.uid;

    private BeanPersonalInformation beanPersonalInformation = new BeanPersonalInformation();


    private final int mRequestCode = 10036;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "个人信息");
        initData();
    }


    @OnClick(R.id.tv_change)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                Intent intent = new Intent(this, ChangePersonalInformation.class);
                intent.putExtra("BeanPersonalInformation", beanPersonalInformation);
                startActivityForResult(intent, mRequestCode);
                break;
        }
    }

    /**
     * 初始化个人信息数据
     */
    private void initData() {
        getDataFromDB();
    }

    /**
     * 从数据库获取个人信息
     */
    private void getDataFromDB() {
        String key = "info_" + uid;
        List<BeanPersonalInformation> personalInformationList = DataSupport.where("key = ?", key).find(BeanPersonalInformation.class);
        if (!personalInformationList.isEmpty()) {
            beanPersonalInformation = personalInformationList.get(0);
            initWidget();
        } else {
            getDataForNetwork();
        }
    }

    /**
     * 从网络获取个人信息
     */
    private void getDataForNetwork() {

        final String key = "info_" + uid;
        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(key);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {

            String resp = null;

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(resp)) {
                    BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                    if (beanBaseKeyGetResp.getCode() == 0) {
                        String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                        if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                            if (strJsonBeanPersonalInformation.substring(0, 1).equals("{")) {
                                beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                                initWidget();
                                beanPersonalInformation.saveOrUpdate("key = ?", key);
                            } else {
                                Toast.makeText(PersonalInformation.this, "个人信息有误,请更新您的个人信息",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            MyToast.showToast(PersonalInformation.this, "您还没有填写个人信息，请填写您的个人信息");
                        }
                    } else {
                        MyToast.showToast(PersonalInformation.this, "获取个人信息失败，请重试");
                    }
                } else {
                    MyToast.showToast(PersonalInformation.this, "获取个人信息失败，请重试");
                }
            }

            @Override
            public void onError(Throwable e) {

                MyToast.showToast(PersonalInformation.this, "获取个人信息失败,请更新您的个人信息");

                Log.i("LYQ", "获取个人信息失败，" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "个人信息：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示个人信息数据
     */
    private void initWidget() {
        if (!TextUtils.isEmpty(beanPersonalInformation.getImgUrl())) {
            Glide.with(PersonalInformation.this).load(HttpHealthyFishyUrl + beanPersonalInformation.getImgUrl()).error(R.mipmap.error).into(civHeadPortrait);
        }
        tvNickname.setText("昵称： " + beanPersonalInformation.getNickname());
        tvGender.setText("性别： " + beanPersonalInformation.getGender());
        tvBirthDate.setText("出生日期： " + beanPersonalInformation.getBirthDate());
        tvPhoneNumber.setText(beanPersonalInformation.getPhone());
        tvName.setText(beanPersonalInformation.getName());
        if (!TextUtils.isEmpty(beanPersonalInformation.getIdCard())) {
            tvIdCard.setText(beanPersonalInformation.getIdCard());
        } else {
            setTvColor();
        }
    }

    /**
     * 设置身份证未认证标识
     */
    private void setTvColor() {
        SpannableString spannableString = new SpannableString("未认证");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ec6941"));
        spannableString.setSpan(colorSpan, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvIdCard.setText(spannableString);
    }

    /**
     * 根据修改个人信息页面传回来的参数进行处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            getDataFromDB();
        }
    }

}
