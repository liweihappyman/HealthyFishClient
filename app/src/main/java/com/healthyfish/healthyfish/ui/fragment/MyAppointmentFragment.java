package com.healthyfish.healthyfish.ui.fragment;

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
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanMyAppointmentItem;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MyAppointmentLvAdapter;
import com.healthyfish.healthyfish.utils.AutoLogin;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：问诊服务中我的医生选项页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyAppointmentFragment extends Fragment {


    @BindView(R.id.lv_my_appointment)
    ListView lvMyAppointment;
    Unbinder unbinder;

    private View rootView;
    private final List<BeanMyAppointmentItem> mList = new ArrayList<>();
    private MyAppointmentLvAdapter adapter;
    private final List<BeanMyAppointmentItem> myAppointmentList = new ArrayList<>();
    private String uid = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fagment_my_aappointment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        String user = MySharedPrefUtil.getValue("user");
        if (!TextUtils.isEmpty(user)) {
            //mList.clear();
            if (MyApplication.isFirstUpdateMyAppointment) {
                BeanUserLoginReq beanUserLoginReq = JSON.parseObject(user, BeanUserLoginReq.class);
                uid = beanUserLoginReq.getMobileNo();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appointmentListReq(uid);
                            }
                        });
                    }
                }).start();
            } else {
                getDataFromDB();
            }

        }

        initListView(mList);

        return rootView;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMyAppointment(BeanMyAppointmentItem beanMyAppointmentItem) {
        Log.i("LYQ", "MyAppointmentFragment_refreshMyAppointment");
        myAppointmentList.clear();
        myAppointmentList.add(beanMyAppointmentItem);
        appointmentReq(myAppointmentList);

    }

    /**
     * 从数据库获取挂号信息
     */
    private void getDataFromDB() {
        List<BeanMyAppointmentItem> list = DataSupport.findAll(BeanMyAppointmentItem.class);
        if (!list.isEmpty()) {
            for (BeanMyAppointmentItem beanMyAppointmentItem : list) {
                mList.add(beanMyAppointmentItem);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * ListView的点击监听
     */
    private void lvListener(final List<BeanMyAppointmentItem> list) {
        lvMyAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * 初始化ListView
     *
     * @param list
     */
    private void initListView(List<BeanMyAppointmentItem> list) {
        adapter = new MyAppointmentLvAdapter(getActivity(), list);
        lvMyAppointment.setAdapter(adapter);
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

        Log.i("LYQ", "挂号信息BeanUserListReq参数json:" + JSON.toJSONString(beanUserListReq));

        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {
            String appointmentListResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(appointmentListResp)) {
                    if (appointmentListResp.substring(0, 1).equals("[")) {
                        List<String> list = JSONArray.parseObject(appointmentListResp, List.class);
                        if (!list.isEmpty()) {
                            for (String str : list) {
                                BeanMyAppointmentItem myAppointment = new BeanMyAppointmentItem();
                                myAppointment.setRespKey(str);
                                myAppointmentList.add(myAppointment);
                            }

                            appointmentReq(myAppointmentList);

                        }
                    } else {
                        MyToast.showToast(getActivity(), "获取挂号列表出错啦");
                    }
                } else {
                    MyToast.showToast(getActivity(), "获取挂号列表出错");
                }
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
     */
    private void appointmentReq(List<BeanMyAppointmentItem> beanMyAppointmentItemList) {

        final BeanMyAppointmentItem beanMyAppointmentItem = beanMyAppointmentItemList.get(0);

        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(beanMyAppointmentItem.getRespKey());

        BeanUserListValueReq beanUserListReq = new BeanUserListValueReq();
        beanUserListReq.setPrefix("reg_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);


        Log.i("LYQ", "挂号信息BeanBaseKeyGetReq参数json:" + JSON.toJSONString(beanBaseKeyGetReq));

        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
            String appointmentResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(appointmentResp)) {
                    if (appointmentResp.substring(0, 1).equals("{")) {
                        BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(appointmentResp, BeanBaseKeyGetResp.class);
                        if (beanBaseKeyGetResp.getCode() == 0) {
                            BeanHospRegisterReq beanHospRegisterReq = JSON.parseObject(beanBaseKeyGetResp.getValue(), BeanHospRegisterReq.class);
                            beanMyAppointmentItem.setDoctorName(beanHospRegisterReq.getDoctTxt());
                            beanMyAppointmentItem.setHospital(beanHospRegisterReq.getHospTxt());
                            beanMyAppointmentItem.setVisitingPerson(beanHospRegisterReq.getName());
                            beanMyAppointmentItem.setAppointmentTime(beanHospRegisterReq.getDateTxt());
                            doctorInfoReq(beanMyAppointmentItem, beanHospRegisterReq.getHosp(), beanHospRegisterReq.getDept(), beanHospRegisterReq.getStaffNo());
                        } else {
                            MyToast.showToast(getActivity(), "获取挂号信息失败");
                        }
                    } else {
                        MyToast.showToast(getActivity(), "获取挂号信息出错啦");
                    }
                } else {
                    MyToast.showToast(getActivity(), "获取挂号信息出错");
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
     *
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

        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptDoctInfoReq), new Subscriber<ResponseBody>() {
            String doctorInfoResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(doctorInfoResp)) {
                    if (doctorInfoResp.substring(0, 1).equals("{")) {

                        myAppointmentList.remove(0);
                        if (myAppointmentList.size() > 0) {
                            appointmentReq(myAppointmentList);
                        }

                        MyApplication.isFirstUpdateMyAppointment = false;
                        BeanHospDeptDoctListRespItem beanHospDeptDoctListRespItem = JSON.parseObject(doctorInfoResp, BeanHospDeptDoctListRespItem.class);
                        beanMyAppointmentItem.setImgUrl(beanHospDeptDoctListRespItem.getZHAOPIAN());
                        beanMyAppointmentItem.setConsultationRoom(String.valueOf(beanHospDeptDoctListRespItem.getCLINIQUE_CODE()));
                        beanMyAppointmentItem.setDuties(beanHospDeptDoctListRespItem.getREISTER_NAME());
                        //myAppointmentList.add(beanMyAppointmentItem);
                        Log.i("LYQ", "beanMyAppointmentItem.getRespKey():" + beanMyAppointmentItem.getRespKey());
                        boolean isSave = beanMyAppointmentItem.save();//将挂号信息保存到数据库
                        if (!isSave) {
                            beanMyAppointmentItem.save();//若保存失败则再次保存
                        }

                        mList.add(beanMyAppointmentItem);
                        adapter.notifyDataSetChanged();
                    } else {
                        MyToast.showToast(getActivity(), "获取挂号医生信息出错啦");
                    }
                } else {
                    MyToast.showToast(getActivity(), "获取挂号医生信息出错");
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
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        myAppointmentList.clear();
        mList.clear();
        unbinder.unbind();
    }
}
