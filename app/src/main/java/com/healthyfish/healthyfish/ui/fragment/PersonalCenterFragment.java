package com.healthyfish.healthyfish.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhy;
import com.healthyfish.healthyfish.POJO.BeanUserPhyIdResp;
import com.healthyfish.healthyfish.POJO.BeanUserPhysical;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.healthy_management.MainIndexHealthyManagement;
import com.healthyfish.healthyfish.ui.activity.healthy_management.PhyIdeReport;
import com.healthyfish.healthyfish.ui.activity.personal_center.Feedback;
import com.healthyfish.healthyfish.ui.activity.Login;
import com.healthyfish.healthyfish.ui.activity.personal_center.MyConcern;
import com.healthyfish.healthyfish.ui.activity.personal_center.MyNews;
import com.healthyfish.healthyfish.ui.activity.personal_center.PersonalInformation;
import com.healthyfish.healthyfish.ui.activity.personal_center.SetUp;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.Subscriber;


import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;


/**
 * 描述：个人中心首页
 * <p>
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {
    @BindView(R.id.civ_head_portrait)
    CircleImageView civHeadPortrait;
    @BindView(R.id.tv_login_or_register)
    TextView tvLoginOrRegister;
    @BindView(R.id.lly_personal_information)
    AutoLinearLayout llyPersonalInformation;
    @BindView(R.id.lly_my_news)
    AutoLinearLayout llyMyNews;
    @BindView(R.id.lly_my_concern)
    AutoLinearLayout llyMyConcern;
    @BindView(R.id.lly_feedback)
    AutoLinearLayout llyFeedback;
    @BindView(R.id.lly_set)
    AutoLinearLayout llySet;
    @BindView(R.id.civ_head_portrait_login)
    CircleImageView civHeadPortraitLogin;
    @BindView(R.id.tv_name_login)
    TextView tvNameLogin;
    @BindView(R.id.tv_constitution_login)
    TextView tvConstitutionLogin;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.rly_mail_login)
    AutoRelativeLayout rlyMailLogin;
    @BindView(R.id.rly_not_login)
    AutoRelativeLayout rlyNotLogin;
    @BindView(R.id.rly_login)
    AutoRelativeLayout rlyLogin;
    Unbinder unbinder;

    private Context mContext;
    private View rootView;
    private BeanPersonalInformation beanPersonalInformation = new BeanPersonalInformation();
    private boolean isTestPhy = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_personal_center, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        String user = MySharedPrefUtil.getValue("user");
        String sid = MySharedPrefUtil.getValue("sid");
        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(sid)) {
            judgeLoginState(true);
        }
        return rootView;
    }

    @OnClick({R.id.civ_head_portrait, R.id.tv_login_or_register, R.id.lly_personal_information, R.id.lly_my_news, R.id.lly_my_concern, R.id.lly_feedback, R.id.lly_set, R.id.civ_head_portrait_login, R.id.tv_name_login, R.id.tv_constitution_login, R.id.iv_go, R.id.rly_mail_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_head_portrait:
                //点击头像
                break;
            case R.id.tv_login_or_register:
                //点击登录/注册
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
            case R.id.lly_personal_information:
                //点击个人信息
                if (!TextUtils.isEmpty(MyApplication.uid)) {
                    Intent intent02 = new Intent(getActivity(), PersonalInformation.class);
                    startActivity(intent02);
                } else {
                    MyToast.showToast(getActivity(), "您还没有登录呦！请先登录");
                }

                break;
            case R.id.lly_my_news:
                //点击我的消息
                Intent intent03 = new Intent(getActivity(), MyNews.class);
                startActivity(intent03);
                break;
            case R.id.lly_my_concern:
                //点击我的关注
                if (!TextUtils.isEmpty(MyApplication.uid)) {
                    Intent intent04 = new Intent(getActivity(), MyConcern.class);
                    startActivity(intent04);
                } else {
                    MyToast.showToast(getActivity(), "您还没有登录呦！请先登录");
                }
                break;
            case R.id.lly_feedback:
                //点击意见反馈
                Intent intent05 = new Intent(getActivity(), Feedback.class);
                startActivity(intent05);
                break;
            case R.id.lly_set:
                //点击设置
                Intent intent06 = new Intent(getActivity(), SetUp.class);
                startActivity(intent06);
                break;
            case R.id.civ_head_portrait_login:
                //登录后点击头像
                break;
            case R.id.tv_name_login:
                //登录后点击名字
                break;
            case R.id.tv_constitution_login:
                //登录后点击体质
                if (isTestPhy) {
                    Intent intent08 = new Intent(getActivity(), PhyIdeReport.class);
                    intent08.putExtra("IS_TESTED", isTestPhy);
                    startActivity(intent08);
                } else {
                    startActivity(new Intent(getActivity(), MainIndexHealthyManagement.class));
                }
                break;
            case R.id.iv_go:
                //登录后点击进入个人的健康信息
                if (isTestPhy) {
                    Intent intent08 = new Intent(getActivity(), PhyIdeReport.class);
                    intent08.putExtra("IS_TESTED", isTestPhy);
                    startActivity(intent08);
                } else {
                    startActivity(new Intent(getActivity(), MainIndexHealthyManagement.class));
                }
                break;
            case R.id.rly_mail_login:
                //登录后点击右上角消息邮箱
                Intent intent07 = new Intent(getActivity(), MyNews.class);
                startActivity(intent07);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLoginState(BeanPersonalInformation beanPersonalInformation) {
        this.beanPersonalInformation = beanPersonalInformation;
        Log.i("LYQ", "refreshLoginState");
        isTestPhy = false;
        judgeLoginState(beanPersonalInformation.isLogin());
    }


    //登录状态判断初始化相应的控件
    private void judgeLoginState(boolean isLogin) {
        if (isLogin) {
            String user = MySharedPrefUtil.getValue("user");
            BeanUserLoginReq beanUserLoginReq = JSON.parseObject(user, BeanUserLoginReq.class);
            String number = beanUserLoginReq.getMobileNo();
            isLogin(true, number);
        } else {
            isLogin(false, null);
        }
    }


    /**
     * 判断是否登录
     * number：目前用手机号码表示用户
     */
    private void isLogin(boolean isLogin, String number) {
        if (isLogin) {
            rlyNotLogin.setVisibility(View.GONE);
            rlyLogin.setVisibility(View.VISIBLE);

            //initInfoPrompt("16");//检查有多少未读消息，并显示

            //初始化个人信息
            initPersonalInformation(number);

            //初始化用户体质
            initUserPhy(number);

            rlyNotLogin.setBackgroundResource(R.color.color_primary_dark);
        } else {
            rlyLogin.setVisibility(View.GONE);
            rlyNotLogin.setVisibility(View.VISIBLE);
            rlyNotLogin.setBackgroundResource(R.color.color_divider);
        }
    }


    /**
     * 展示数据
     */
    private void initWidget() {
        if (beanPersonalInformation != null) {
            if (!TextUtils.isEmpty(beanPersonalInformation.getImgUrl())) {
                Glide.with(getActivity()).load(HttpHealthyFishyUrl + beanPersonalInformation.getImgUrl()).error(R.mipmap.error).into(civHeadPortraitLogin);
            } else {
                Glide.with(getActivity()).load(R.mipmap.ic_logo).into(civHeadPortraitLogin);
            }
            if (!TextUtils.isEmpty(beanPersonalInformation.getNickname())) {
                setTextBold(beanPersonalInformation.getNickname());
            } else {
                setTextBold("您");
            }
        } else {
            Glide.with(getActivity()).load(R.mipmap.ic_logo).into(civHeadPortraitLogin);
            setTextBold("您");
        }
    }

    /**
     * 设置字体为粗体
     */
    private void setTextBold(String name) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(name + " 的健康信息");
        //setSpan可多次使用
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpan, 0, name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvNameLogin.setText(spannableString);
    }

    /**
     * 初始化个人信息
     *
     * @param uid
     */
    private void initPersonalInformation(String uid) {
        String key = "info_" + uid;
        List<BeanPersonalInformation> personalInformationList = DataSupport.where("key = ?", key).find(BeanPersonalInformation.class);
        if (!personalInformationList.isEmpty()) {
            beanPersonalInformation = personalInformationList.get(0);
            initWidget();
        } else {
            upDatePersonalInformation(uid);
        }
    }

    /**
     * 初始化用户体质
     *
     * @param uid
     */
    private void initUserPhy(String uid) {

        if (MyApplication.isFirstUpdateUsrPhy) {
            upDateUserPhyFromNetwork(uid);
        } else {
            getUserPhyFromDB(uid);
        }
    }

    /**
     * 从数据库查找用户体质并初始化
     * @param uid
     */
    private void getUserPhyFromDB(String uid) {
        List<BeanUserPhy> beanUserPhyList = DataSupport.where("uid = ?", uid).find(BeanUserPhy.class);
        if (!beanUserPhyList.isEmpty()) {
            isTestPhy = true;
            BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(beanUserPhyList.get(0).getJsonStrPhysicalList(), BeanUserPhyIdResp.class);
            List<BeanUserPhysical> physicals = beanUserPhyIdResp.getPhyList();
            tvConstitutionLogin.setText(physicals.get(0).getTitle() + "质");
        } else {
            tvConstitutionLogin.setText("");
        }
    }


    /**
     * 初始化显示消息数控件
     *
     * @param string:总消息数
     */
    public void initInfoPrompt(String string) {
        Badge badge = new QBadgeView(mContext).bindTarget(rlyMailLogin);
        badge.setBadgeBackgroundColor(0xFFF70909);
        badge.setBadgeTextColor(0xffffffFF);
        badge.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
        badge.setBadgeTextSize(12, true);
        badge.setBadgePadding(3, true);
        badge.setBadgeText(string);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {

                }
            }
        });
    }

    /**
     * 从网络获取个人信息
     */
    private void upDatePersonalInformation(String uid) {

        final String key = "info_" + uid;
        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(key);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {

            String resp = null;

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(resp)) {
                    if (resp.toString().substring(0, 1).equals("{")) {
                        BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                        if (beanBaseKeyGetResp.getCode() == 0) {
                            String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                            if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                                beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                                boolean isSave = beanPersonalInformation.saveOrUpdate("key = ?", key);
                                if (!isSave) {
                                    MyToast.showToast(getActivity(), "保存个人信息失败");
                                }
                            } else {
                                MyToast.showToast(getActivity(), "您还没有填写个人信息，请填写您的个人信息");
                            }
                        } else {
                            MyToast.showToast(getActivity(), "获取个人信息失败");
                        }
                    } else {
                        MyToast.showToast(getActivity(), "加载个人信息出错啦");
                    }
                } else {
                    MyToast.showToast(getActivity(), "获取个人信息失败");
                }

                initWidget();
            }

            @Override
            public void onError(Throwable e) {

                MyToast.showToast(getActivity(), "获取个人信息失败,请更新您的个人信息");

                initWidget();

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "个人中心获取用户信息响应：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 从服务器更新本地数据库的用户体质
     *
     * @param uid
     */
    private void upDateUserPhyFromNetwork(final String uid) {
        BeanUserListValueReq beanUserListReq = new BeanUserListValueReq();
        beanUserListReq.setPrefix("phyad_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {

            String strResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(strResp)) {
                    if (strResp.toString().substring(0, 1).equals("[")) {
                        MyApplication.isFirstUpdateUsrPhy = false;
                        //DataSupport.deleteAll(BeanUserPhy.class);
                        List<String> strList = JSONArray.parseObject(strResp, List.class);
                        if (!strList.isEmpty()) {
                            for (String str : strList) {
                                BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(str, BeanUserPhyIdResp.class);
                                if (beanUserPhyIdResp.getCode() == 0) {
                                    MyApplication.isFirstUpdateUsrPhy = false;
                                    isTestPhy = true;
                                    tvConstitutionLogin.setText(beanUserPhyIdResp.getPhyList().get(0).getTitle() + "质");
                                    BeanUserPhy beanuserPhy = new BeanUserPhy();
                                    beanuserPhy.setUid(uid);
                                    beanuserPhy.setJsonStrPhysicalList(str);
                                    boolean isSave = beanuserPhy.saveOrUpdate("uid = ?", uid);
                                    if (!isSave) {
                                        beanuserPhy.saveOrUpdate("uid = ?", uid);
                                    }
                                }
                            }
                        }
                        getUserPhyFromDB(uid);
                    } else {
                        MyToast.showToast(getActivity(), "加载个人体质信息出错");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "个人中心体质报告onError：" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strResp = responseBody.string();
                    Log.i("LYQ", "个人中心体质报告：" + strResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

}
