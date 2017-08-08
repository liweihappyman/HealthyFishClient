package com.healthyfish.healthyfish.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
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

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanPictureConsultServiceDoctorList;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.ImMsgBean;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationServiceAdapter;
import com.healthyfish.healthyfish.adapter.healthy_chat.ChattingListAdapter;
import com.healthyfish.healthyfish.eventbus.WeChatReceiveMsg;
import com.healthyfish.healthyfish.ui.activity.Login;
import com.healthyfish.healthyfish.ui.activity.interrogation.HealthyChat;
import com.healthyfish.healthyfish.utils.ComparatorDate;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.Subscriber;

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

    // TODO: 2017/8/8 获取sender// 获取全局登录信息
    private BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
    // private String sender = "u" + beanUserLoginReq.getMobileNo();
    private String sender = "u18077207818";

    private View rootView;
    private InterrogationServiceAdapter mAdapter;
    private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_current_service, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mList.clear();
        initListView();
        lvListener();
        EventBus.getDefault().register(this);

        String user = MySharedPrefUtil.getValue("user");
        if (!TextUtils.isEmpty(user)) {
            //更新用户的个人信息
            upDatePersonalInformation();
        }


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
                Intent intent = new Intent(getActivity(), HealthyChat.class);
                intent.putExtra("BeanDoctorChatInfo", beanDoctorChatInfo);
                startActivity(intent);
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
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  // 声明列表容器
        Map<String, Object> map;
        // 获取购买过问诊服务的医生列表
        List<BeanPictureConsultServiceDoctorList> purchaseList = DataSupport.findAll(BeanPictureConsultServiceDoctorList.class);

        Log.e("list size", purchaseList.size() + "");

        for (BeanPictureConsultServiceDoctorList bean : purchaseList) {
            map = new HashMap<String, Object>();
            String topic = "d" + bean.getDoctorNumber();
            String msgType;
            ImMsgBean lastMsg = DataSupport.where("topic = ? and name = ? or topic = ? and name = ?", topic, sender, sender, topic).findLast(ImMsgBean.class);
            if (lastMsg != null) {
                switch (lastMsg.getType()) {
                    case "t":
                        msgType = "「文本消息」";
                        break;
                    case "i":
                        msgType = "「图片消息」";
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
//            Log.e("refreshDataList: ", lastMsg.isNewMsg() + "    " + lastMsg.getContent() + "     " + lastMsg.getTime());
                map.put("isSender", lastMsg.isSender());
                mList.add(map);
            }
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
        // 刷新列表状态
//        String time = msg.getTime() + "";
//        ImMsgBean bean = DataSupport.where("time = ?", time).find(ImMsgBean.class).get(0);
//        mList.clear();
//        initData(true);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        refreshDataList();

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
                                BeanPersonalInformation beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                                boolean isSave = beanPersonalInformation.saveOrUpdate("key = ?", key);
                                if (!isSave) {
                                    if (!beanPersonalInformation.saveOrUpdate("key = ?", key)) {
                                        MyToast.showToast(getContext(), "更新个人信息失败");
                                    }
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


}
