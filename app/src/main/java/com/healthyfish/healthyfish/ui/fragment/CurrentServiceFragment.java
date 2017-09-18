package com.healthyfish.healthyfish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyRemReq;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanInterrogationServiceDoctorList;
import com.healthyfish.healthyfish.POJO.BeanServiceList;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.ImMsgBean;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationServiceAdapter;
import com.healthyfish.healthyfish.eventbus.WeChatReceiveMsg;
import com.healthyfish.healthyfish.ui.activity.interrogation.ChoiceService;
import com.healthyfish.healthyfish.ui.activity.interrogation.HealthyChat;
import com.healthyfish.healthyfish.utils.DateTimeUtil;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：问诊服务中当前服务选项页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class CurrentServiceFragment extends Fragment {

    @BindView(R.id.lv_current_service)
    ListView lvCurrentService;
    Unbinder unbinder;

    private BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
    private BeanDoctorInfo beanDoctorInfo = new BeanDoctorInfo();
    private String doctorPhone;
    private String sender;
    private String uid = "";

    //private String sender = "u18077207818";

    private View rootView;
    private InterrogationServiceAdapter mAdapter;
    private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_current_service, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        lvListener();
        EventBus.getDefault().register(this);

        String user = MySharedPrefUtil.getValue("user");
        if (!TextUtils.isEmpty(user)) {
            //更新用户的个人信息
            upDatePersonalInformation();
            // 获取登录用户信息
            beanUserLoginReq = JSON.parseObject(MySharedPrefUtil.getValue("user"), BeanUserLoginReq.class);
            sender = "u" + beanUserLoginReq.getMobileNo();

            mList.clear();
            initListView();
        }
        uid = MyApplication.uid;

        return rootView;
    }

    /**
     * ListView的点击监听
     */
    private void lvListener() {
        lvCurrentService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 获取医生peer信息，
                BeanDoctorChatInfo beanDoctorChatInfo = new BeanDoctorChatInfo();
                beanDoctorChatInfo.setPhone((String) mList.get(position).get("peerNumber"));
                beanDoctorChatInfo.setName((String) mList.get(position).get("name"));
                beanDoctorChatInfo.setImgUrl((String) mList.get(position).get("portrait"));
                beanDoctorChatInfo.setServiceType((String) mList.get(position).get("type"));
                // 根据手机号查询购买服务与否时用到的手机号
                doctorPhone = (String) mList.get(position).get("peerNumber");

                if (!TextUtils.isEmpty(uid)) {
                    buyPictureConsultingService();
                } else {
                    MyToast.showToast(MyApplication.getContetxt(), "您还没有登录呦！请先登录");
                }
            }
        });
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        refreshDataList();
        mAdapter = new InterrogationServiceAdapter(getActivity(), mList);
        lvCurrentService.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     *
     * @return
     */
    private void refreshDataList() {
        Map<String, Object> map;
        // 获取购买过问诊服务的医生列表
        List<BeanInterrogationServiceDoctorList> purchaseList = DataSupport.findAll(BeanInterrogationServiceDoctorList.class);

        for (BeanInterrogationServiceDoctorList bean : purchaseList) {
            map = new HashMap<String, Object>();
            String topic = "d" + bean.getDoctorNumber();
            String msgType;
            try {
                ImMsgBean lastMsg = DataSupport.where("topic = ? and name = ? or topic = ? and name = ?", topic, sender, sender, topic).findLast(ImMsgBean.class);

                if (lastMsg != null) {
                    switch (lastMsg.getType()) {
                        case "t":
                            msgType = "「文本消息」";
                            break;
                        case "i":
                            msgType = "「图片消息」";
                            break;
                        case "m":
                            msgType = "「病历消息」";
                            break;
                        default:
                            msgType = "「消息」";
                            break;
                    }
                    map.put("type", "pictureConsulting");
                    map.put("message", msgType);
                    map.put("time", DateTimeUtil.getTime(lastMsg.getTime()));
                    map.put("time1", lastMsg.getTime());// 用于排序的比较器
                    map.put("hospital", "柳州市中医院");
                    map.put("name", bean.getDoctorName());
                    map.put("portrait", bean.getDoctorPortrait());
                    map.put("peerNumber", bean.getDoctorNumber());
                    map.put("isNew", lastMsg.isNewMsg());
                    map.put("isSender", lastMsg.isSender());
                    mList.add(map);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 按发送时间给列表排序time1
            Collections.sort(mList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if (((long) o1.get("time1")) > (long) o2.get("time1")) {
                        return -1;
                    }
                    if (((long) o1.get("time1")) > (long) o2.get("time1")) {
                        return 0;
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * 更新发送信息状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSendingStatus(ImMsgBean msg) {
        // 刷新列表状态
        mList.clear();
        initListView();
    }

    /**
     * 接收到信息状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSendingStatus(WeChatReceiveMsg msg) {

        mList.clear();
        initListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        initListView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新用户的个人信息
     */

    private void upDatePersonalInformation() {
        String user = MySharedPrefUtil.getValue("user");
        if (!TextUtils.isEmpty(user)) {

            BeanUserLoginReq beanUserLoginReq = JSON.parseObject(user, BeanUserLoginReq.class);
            final String uid = beanUserLoginReq.getMobileNo();
            MyApplication.uid = uid;
            if (MyApplication.isIsFirstUpdatePersonalInfo) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        upDatePersonalInformationReq(uid);
                    }
                }).start();
            }
        }

    }

    /**
     * 更新个人信息请求
     */
    private void upDatePersonalInformationReq(String uid) {

        final String key = "info_" + uid;
        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(key);

        RetrofitManagerUtils.getInstance(getContext(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {

            String resp = null;

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(resp)) {
                    if (resp.toString().substring(0, 1).equals("{")) {
                        BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                        if (beanBaseKeyGetResp.getCode() == 0) {
                            String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                            if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                                if (strJsonBeanPersonalInformation.substring(0, 1).equals("{")) {
                                    BeanPersonalInformation beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                                    boolean isSave = beanPersonalInformation.saveOrUpdate("key = ?", key);
                                    if (!isSave) {
                                        if (!beanPersonalInformation.saveOrUpdate("key = ?", key)) {
                                            MyToast.showToast(getContext(), "更新个人信息失败");
                                        }
                                    }
                                } else {
                                    MyToast.showToast(getContext(), "个人信息有误,请更新您的个人信息");
                                }
                            } else {
                                //MyToast.showToast(MainActivity.this, "您还没有填写个人信息，请填写您的个人信息");//首页不用提醒，在个人中心页面再提醒
                            }
                            MyApplication.isIsFirstUpdatePersonalInfo = false;
                        } else {
                            MyToast.showToast(getContext(), "更新个人信息失败");
                        }
                    } else {
                        MyToast.showToast(getContext(), "加载个人信息出错啦");
                    }
                } else {
                    MyToast.showToast(getContext(), "更新个人信息失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(getContext(), "更新个人信息失败,请更新您的个人信息");
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
     * 选择图文咨询操作
     */
    private void buyPictureConsultingService() {
        if (MyApplication.isFirstUpdateMyService) {
            upDateServiceListReq(uid);
        } else {
            getMyServiceFromDB(uid);
        }
    }

    /**
     * 从数据库检查我已购买的服务
     *
     * @param uid
     */
    private void getMyServiceFromDB(String uid) {
        BeanDoctorChatInfo beanDoctorChatInfo = new BeanDoctorChatInfo();
        beanDoctorChatInfo.setName(beanDoctorInfo.getName());
        beanDoctorChatInfo.setPhone(doctorPhone);
        beanDoctorChatInfo.setImgUrl(HttpHealthyFishyUrl + beanDoctorInfo.getImgUrl());
        beanDoctorChatInfo.setServiceType("pictureConsulting");

        String serviceKey = "service_" + uid + "_" + "PTC_" + doctorPhone;
//        Log.i("LYQ", "serviceKey:" + serviceKey);

        List<BeanServiceList> serviceLists = DataSupport.where("phoneNumber = ?", doctorPhone).find(BeanServiceList.class);//查找数据库
        if (!serviceLists.isEmpty()) {//不为空则购买过该医生的图文咨询服务
            BeanServiceList beanServiceList = serviceLists.get(0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
            Date endDate = dateFormat.parse(beanServiceList.getEndTime(), new ParsePosition(0));
            Date currentDate = new Date(System.currentTimeMillis());

            if (currentDate.getTime() <= endDate.getTime()) {//服务未过期
                Intent intent = new Intent(MyApplication.getContetxt(), HealthyChat.class);
                intent.putExtra("BeanDoctorChatInfo", beanDoctorChatInfo);
                startActivity(intent);
                // 添加用户已购买服务的医生列表
                //addPictureConsultServiceDoctorList();
            } else {//服务已过期
                DataSupport.delete(BeanServiceList.class, beanServiceList.getId());//删除本地数据库该购买服务记录
                deleteServiceReq(serviceKey);//删除服务器端该购买服务记录

                //goToBuyService(serviceKey, true, beanDoctorChatInfo);
                /*Intent intent = new Intent(MyApplication.getContetxt(), ChoiceService.class);
                intent.putExtra("BeanDoctorChatInfo", beanDoctorChatInfo);
                startActivity(intent);*/
                Toast.makeText(MyApplication.getContetxt(), "服务已过期，请重新购买服务", Toast.LENGTH_SHORT).show();
            }
        } else {//空则没有购买过该医生的图文咨询服务或者已过期
            //goToBuyService(serviceKey, false, beanDoctorChatInfo);
            /*Intent intent = new Intent(MyApplication.getContetxt(), ChoiceService.class);
            intent.putExtra("BeanDoctorChatInfo", beanDoctorChatInfo);
            startActivity(intent);*/
            Toast.makeText(MyApplication.getContetxt(), "服务已过期，请重新购买服务", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 从网络更新已购买的服务列表
     */
    private void upDateServiceListReq(final String uid) {

        BeanUserListValueReq beanUserListValueReq = new BeanUserListValueReq();
        beanUserListValueReq.setPrefix("service_" + uid);
        beanUserListValueReq.setFrom(0);
        beanUserListValueReq.setTo(-1);
        beanUserListValueReq.setNum(-1);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListValueReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(strJson)) {
                    if (strJson.substring(0, 1).equals("[")) {
                        MyApplication.isFirstUpdateMyService = false;
                        DataSupport.deleteAll(BeanServiceList.class);//清空数据库中旧的已购买服务列表
                        List<String> strServiceList = JSONArray.parseObject(strJson, List.class);
                        if (!strServiceList.isEmpty()) {
                            for (String strService : strServiceList) {
                                BeanServiceList beanServiceList = JSON.parseObject(strService, BeanServiceList.class);
                                beanServiceList.save();//更新数据库中已购买服务列表
                            }
                        }
                        getMyServiceFromDB(uid);
                    } else {
                        MyToast.showToast(MyApplication.getContetxt(), "更新已购买服务出错啦");
                    }
                } else {
                    MyToast.showToast(MyApplication.getContetxt(), "更新已购买服务出错");
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
     * 删除已过期服务记录
     */
    private void deleteServiceReq(final String serviceKey) {
        BeanBaseKeyRemReq beanBaseKeyRemReq = new BeanBaseKeyRemReq();
        beanBaseKeyRemReq.setKey(serviceKey);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyRemReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                BeanBaseResp beanBaseResp = JSON.parseObject(strJson, BeanBaseResp.class);
                if (beanBaseResp.getCode() < 0) {
                    MyToast.showToast(MyApplication.getContetxt(), "删除该服务购买记录失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "deleteServiceReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", "删除已购买服务响应：" + strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
