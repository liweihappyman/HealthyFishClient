package com.healthyfish.healthyfish;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanConcernList;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanMyAppointmentItem;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanServiceList;
import com.healthyfish.healthyfish.POJO.BeanSessionIdReq;
import com.healthyfish.healthyfish.POJO.BeanSessionIdResp;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.eventbus.InitAllMessage;
import com.healthyfish.healthyfish.ui.activity.Login;
import com.healthyfish.healthyfish.ui.fragment.HealthWorkshopFragment;
import com.healthyfish.healthyfish.ui.fragment.HealthyCircleFragment;
import com.healthyfish.healthyfish.ui.fragment.HomeFragment;
import com.healthyfish.healthyfish.ui.fragment.InterrogationFragment;
import com.healthyfish.healthyfish.ui.fragment.PersonalCenterFragment;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Sha256;
import com.healthyfish.healthyfish.utils.mqtt_utils.MqttUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;


/**
 * * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * <p>
 * 描述：首页
 * 作者：Wayne on 2017/6/26 14:51
 * 邮箱：liwei_happyman@qq.com
 * 编辑：wkj
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.fg_viewpage)
    ViewPager fgViewpage;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ly_home)
    AutoLinearLayout lyHome;
    @BindView(R.id.iv_interrogation)
    ImageView ivInterrogation;
    @BindView(R.id.tv_interrogation)
    TextView tvInterrogation;
    @BindView(R.id.ly_interrogation)
    AutoLinearLayout lyInterrogation;
    @BindView(R.id.iv_healthy_circle)
    ImageView ivHealthyCircle;
    @BindView(R.id.tv_healthy_circle)
    TextView tvHealthyCircle;
    @BindView(R.id.ly_healthy_circle)
    AutoLinearLayout lyHealthyCircle;
    @BindView(R.id.iv_health_workshop)
    ImageView ivHealthWorkshop;
    @BindView(R.id.tv_health_workshop)
    TextView tvHealthWorkshop;
    @BindView(R.id.ly_health_workshop)
    AutoLinearLayout lyHealthWorkshop;
    @BindView(R.id.iv_personal_center)
    ImageView ivPersonalCenter;
    @BindView(R.id.tv_personal_center)
    TextView tvPersonalCenter;
    @BindView(R.id.ly_personal_center)
    AutoLinearLayout lyPersonalCenter;
    private MainVpAdapter ViewpageAdapter;
    private List<Fragment> fragments;
    private HomeFragment homeFragment;
    private InterrogationFragment interrogationFragment;
    private HealthyCircleFragment healthyCircleFragment;
    private HealthWorkshopFragment healthWorkshopFragment;
    private PersonalCenterFragment personalCenterFragment;

    BeanSessionIdReq beanSessionIdReq = new BeanSessionIdReq();
    private final List<BeanMyAppointmentItem> myAppointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initPermision();
        init();

        // 初始化MQTT连接，首先获取sid，然后开启MQTT连接
        initMQTT();
    }

    //初始化接界面
    private void init() {
        initpgAdapter();//初始化viewpage
        setTab(0);//初始化界面设置，即指定刚进入是可见的界面



        //菜单监听
        lyHome.setOnClickListener(this);
        lyInterrogation.setOnClickListener(this);
        lyHealthyCircle.setOnClickListener(this);
        lyHealthWorkshop.setOnClickListener(this);
        lyPersonalCenter.setOnClickListener(this);
        fgViewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int pos = fgViewpage.getCurrentItem();
                setTab(pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //菜单点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_home:
                setTab(0);
                break;
            case R.id.ly_interrogation:
                setTab(1);
                break;
            case R.id.ly_healthy_circle:
                setTab(2);
                break;
            case R.id.ly_health_workshop:
                setTab(3);
                break;
            case R.id.ly_personal_center:
                setTab(4);
                break;
            default:
        }
    }

    //初始化ViewPage
    private void initpgAdapter() {
        fragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        interrogationFragment = new InterrogationFragment();
        healthyCircleFragment = new HealthyCircleFragment(getSupportFragmentManager());
        healthWorkshopFragment = new HealthWorkshopFragment();
        personalCenterFragment = new PersonalCenterFragment();
        fragments.add(homeFragment);
        fragments.add(interrogationFragment);
        fragments.add(healthyCircleFragment);
        fragments.add(healthWorkshopFragment);
        fragments.add(personalCenterFragment);
        ViewpageAdapter = new MainVpAdapter(getSupportFragmentManager()
                , fragments);
        //设置ViewPage的缓存页，数字表示预先加载的页面的偏移量，
        // 现在0的意思是不预先加载，另一个作用是也不销毁生的页面
        fgViewpage.setOffscreenPageLimit(0);
        fgViewpage.setAdapter(ViewpageAdapter);
    }

    //重置菜单状态
    private void reSet() {
        ivHome.setImageResource(R.mipmap.home_unselected);
        ivInterrogation.setImageResource(R.mipmap.interrogation_unselect);
        ivHealthyCircle.setImageResource(R.mipmap.healthy_circle_unselected);
        ivHealthWorkshop.setImageResource(R.mipmap.health_workshop_unselected);
        ivPersonalCenter.setImageResource(R.mipmap.personal_center_unselect);
        tvHome.setTextColor(getResources().getColor(R.color.color_general_and_title));
        tvInterrogation.setTextColor(getResources().getColor(R.color.color_general_and_title));
        tvHealthyCircle.setTextColor(getResources().getColor(R.color.color_general_and_title));
        tvHealthWorkshop.setTextColor(getResources().getColor(R.color.color_general_and_title));
        tvPersonalCenter.setTextColor(getResources().getColor(R.color.color_general_and_title));
    }

    //设置菜单的选中状态
    private void setTab(int i) {
        switch (i) {
            case 0:
                reSet();
                ivHome.setImageResource(R.mipmap.home);
                tvHome.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(0);
                break;
            case 1:
                reSet();
                ivInterrogation.setImageResource(R.mipmap.interrogation);
                tvInterrogation.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(1);
                break;
            case 2:
                reSet();
                ivHealthyCircle.setImageResource(R.mipmap.healthy_circle);
                tvHealthyCircle.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(2);
                break;
            case 3:
                reSet();
                ivHealthWorkshop.setImageResource(R.mipmap.health_workshop);
                tvHealthWorkshop.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(3);
                break;
            case 4:
                reSet();
                ivPersonalCenter.setImageResource(R.mipmap.personal_center);
                tvPersonalCenter.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(4);
                break;
            default:
        }
    }


    public void initPermision() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(RxPermissions.getInstance(this).ensureEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {

                    }
                });
    }

    /**
     * 初始化MQTT
     */
    private void initMQTT() {
        RetrofitManagerUtils.getInstance(MainActivity.this, HttpHealthyFishyUrl)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanSessionIdReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        String user = MySharedPrefUtil.getValue("user");
                        if (!TextUtils.isEmpty(user)) {
                            MqttUtil.connect();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            BeanSessionIdResp obj = new Gson().fromJson(responseBody.string(), BeanSessionIdResp.class);
                            Log.e("MainActivity从服务器获取sid", obj.getSid());
                            MySharedPrefUtil.saveKeyValue("sid", obj.getSid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initAllUserInformation(InitAllMessage initAllMessage) {
        Log.i("LYQ","initAllUserInformation");
        String uid = initAllMessage.getBeanUserLoginReq().getMobileNo();
        MyApplication.uid = uid;
        upDatePersonalInformation(uid);//更新个人信息
        appointmentListReq(uid);//更新我的挂号信息
        upDateServiceListReq(uid);//更新已购买服务列表
        upDateMyConcern(uid);//更新关注列表
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
                    BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                    if (beanBaseKeyGetResp.getCode() == 0) {
                        String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                        if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                            BeanPersonalInformation beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                            boolean isSave = beanPersonalInformation.saveOrUpdate("key = ?", key);
                            if (!isSave) {
                                MyToast.showToast(MainActivity.this, "保存个人信息失败");
                            }
                        } else {
                            //MyToast.showToast(MainActivity.this, "您还没有填写个人信息，请填写您的个人信息");//首页不用提醒，在个人中心页面再提醒
                        }
                    } else {
                        MyToast.showToast(MainActivity.this, "获取个人信息失败");
                    }
                } else {
                    MyToast.showToast(MainActivity.this, "获取个人信息失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(MainActivity.this, "获取个人信息失败,请更新您的个人信息");
            }

            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    resp = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 从网络获取已购买的服务列表
     */
    private void upDateServiceListReq(String uid) {

        BeanUserListValueReq beanUserListValueReq = new BeanUserListValueReq();
        beanUserListValueReq.setPrefix("service_" + uid);
        beanUserListValueReq.setFrom(0);
        beanUserListValueReq.setTo(-1);
        beanUserListValueReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListValueReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                List<String> strServiceList = JSONArray.parseObject(strJson, List.class);
                DataSupport.deleteAll(BeanServiceList.class);//清空数据库中旧的已购买服务列表
                for (String strService : strServiceList) {
                    BeanServiceList beanServiceList = JSON.parseObject(strService, BeanServiceList.class);
                    beanServiceList.save();//更新数据库中已购买服务列表
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "getServiceListReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", "获取已购买服务响应：" + strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 将用户的关注列表保存到数据库
     */
    private void upDateMyConcern(String uid) {
        final List<BeanConcernList> concernList = new ArrayList<>();

        BeanUserListReq beanUserListReq = new BeanUserListReq();
        beanUserListReq.setPrefix("care_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (DataSupport.findAll(BeanConcernList.class).isEmpty()) {
                            for (BeanConcernList beanConcernList : concernList) {
                                if (!beanConcernList.save()) {
                                    for (BeanConcernList beanConcernList1 : concernList) {
                                        beanConcernList1.save();
                                    }
                                }
                            }
                        } else {
                            DataSupport.deleteAll(BeanConcernList.class);
                            for (BeanConcernList beanConcernList : concernList) {
                                if (!beanConcernList.save()) {
                                    for (BeanConcernList beanConcernList1 : concernList) {
                                        beanConcernList1.save();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LYQ", "MainActivity_upDateMyConcern_onError:" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<String> concerns = JSONArray.parseObject(jsonStr, List.class);
                        for (String str : concerns) {
                            BeanConcernList beanConcernList = new BeanConcernList();
                            beanConcernList.setKey(str);
                            concernList.add(beanConcernList);

                        }
                    }
                });

    }

    /**
     * 获取预约列表请求
     */
    private void appointmentListReq(final String uid) {

        DataSupport.deleteAll(BeanMyAppointmentItem.class);//清除数据库中我的挂号信息

        BeanUserListReq beanUserListReq = new BeanUserListReq();
        beanUserListReq.setPrefix("reg_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        Log.i("LYQ", "挂号信息BeanUserListReq参数json:"+ JSON.toJSONString(beanUserListReq));

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {
            String appointmentListResp = "";

            @Override
            public void onCompleted() {
                List<String> list = JSONArray.parseObject(appointmentListResp, List.class);
                for (String str : list) {
                    BeanMyAppointmentItem myAppointment = new BeanMyAppointmentItem();
                    myAppointment.setRespKey(str);
                    myAppointmentList.add(myAppointment);
                }
                for (int i = 0; i < myAppointmentList.size(); i++) {
                    appointmentReq(uid,myAppointmentList.get(i));
                }
                myAppointmentList.clear();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "appointmentListReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    appointmentListResp = responseBody.string();
                    Log.i("LYQ", "appointmentListResp:" + appointmentListResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 获取预约信息请求
     *
     * @param beanMyAppointmentItem
     */
    private void appointmentReq(String uid,final BeanMyAppointmentItem beanMyAppointmentItem) {

        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(beanMyAppointmentItem.getRespKey());

        BeanUserListValueReq beanUserListReq = new BeanUserListValueReq();
        beanUserListReq.setPrefix("reg_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);


        Log.i("LYQ", "挂号信息BeanBaseKeyGetReq参数json:"+ JSON.toJSONString(beanBaseKeyGetReq));

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
            String appointmentResp = "";

            @Override
            public void onCompleted() {
                BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(appointmentResp, BeanBaseKeyGetResp.class);
                if (beanBaseKeyGetResp.getCode() == 0) {
                    BeanHospRegisterReq beanHospRegisterReq = JSON.parseObject(beanBaseKeyGetResp.getValue(), BeanHospRegisterReq.class);
                    beanMyAppointmentItem.setDoctorName(beanHospRegisterReq.getDoctTxt());
                    beanMyAppointmentItem.setHospital(beanHospRegisterReq.getHospTxt());
                    beanMyAppointmentItem.setVisitingPerson(beanHospRegisterReq.getName());
                    beanMyAppointmentItem.setAppointmentTime(beanHospRegisterReq.getDateTxt());
                    doctorInfoReq(beanMyAppointmentItem, beanHospRegisterReq.getHosp(), beanHospRegisterReq.getDept(), beanHospRegisterReq.getStaffNo());

                }

            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "appointmentResp_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    appointmentResp = responseBody.string();
                    Log.i("LYQ", "appointmentResp:" + appointmentResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 获取挂号的医生的详情
     * @param beanMyAppointmentItem
     * @param hosp
     * @param dept
     * @param staffNo
     */
    private void doctorInfoReq(final BeanMyAppointmentItem beanMyAppointmentItem, String hosp, String dept, String staffNo) {

        BeanHospDeptDoctInfoReq beanHospDeptDoctInfoReq = new BeanHospDeptDoctInfoReq();
        beanHospDeptDoctInfoReq.setHosp(hosp);
        beanHospDeptDoctInfoReq.setDept(dept);
        beanHospDeptDoctInfoReq.setStaffNo(staffNo);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptDoctInfoReq), new Subscriber<ResponseBody>() {
            String doctorInfoResp = "";

            @Override
            public void onCompleted() {
                BeanHospDeptDoctListRespItem beanHospDeptDoctListRespItem = JSON.parseObject(doctorInfoResp, BeanHospDeptDoctListRespItem.class);
                beanMyAppointmentItem.setImgUrl(beanHospDeptDoctListRespItem.getZHAOPIAN());
                beanMyAppointmentItem.setConsultationRoom(String.valueOf(beanHospDeptDoctListRespItem.getCLINIQUE_CODE()));
                beanMyAppointmentItem.setDutise(beanHospDeptDoctListRespItem.getREISTER_NAME());
                myAppointmentList.add(beanMyAppointmentItem);

                boolean isSave = beanMyAppointmentItem.save();//将挂号信息保存到数据库
                if (isSave) {
                    EventBus.getDefault().post(new BeanMyAppointmentItem());//通知问诊我的挂号页面刷新数据
                } else {
                    beanMyAppointmentItem.save();//若保存失败则再次保存
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "appointmentResp——onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    doctorInfoResp = responseBody.string();
                    Log.i("LYQ", "doctorInfoReq（）Resp:" + doctorInfoResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
