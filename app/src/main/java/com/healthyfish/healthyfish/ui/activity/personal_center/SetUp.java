package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.BeanUserLogoutReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.EmptyMessage;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：个人中心设置页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SetUp extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bt_login_out)
    Button btLoginOut;
    @BindView(R.id.switch_message_notification)
    ToggleButton switchMessageNotification;
    @BindView(R.id.lly_message_notification)
    AutoLinearLayout llyMessageNotification;
    @BindView(R.id.iv_change_password)
    ImageView ivChangePassword;
    @BindView(R.id.lly_change_password)
    AutoLinearLayout llyChangePassword;
    @BindView(R.id.iv_update_version)
    ImageView ivUpdateVersion;
    @BindView(R.id.lly_update_version)
    AutoLinearLayout llyUpdateVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "设置");

    }


    /**
     * 返回按钮的监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EventBus.getDefault().post(new EmptyMessage());
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick(R.id.bt_login_out)
    public void onViewClicked() {
        loginOut();
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        String user = sp.getString("user", null);
        if (user == null) {
            Toast.makeText(this, "您还没有登录哟", Toast.LENGTH_LONG).show();
        } else {
            BeanUserLoginReq beanUserLoginReq = JSON.parseObject(user,BeanUserLoginReq.class);

            BeanUserLogoutReq beanUserLogoutReq = new BeanUserLogoutReq();
            beanUserLogoutReq.setMobileNo(beanUserLoginReq.getMobileNo());
            beanUserLogoutReq.setPwdSHA256(beanUserLoginReq.getPwdSHA256());

            RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserLogoutReq), new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                    sp.edit().clear().commit();//清除用户登录信息
                    EventBus.getDefault().post(new EmptyMessage());
                    Intent intent = new Intent(SetUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(SetUp.this, "网络异常"+e.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        String str = responseBody.string();
                        Toast.makeText(SetUp.this, "退出登录成功", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EmptyMessage());
    }
}
