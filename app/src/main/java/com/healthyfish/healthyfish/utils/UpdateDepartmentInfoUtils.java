package com.healthyfish.healthyfish.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanDepartmentInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListResp;
import com.healthyfish.healthyfish.POJO.BeanHospitalListRespItem;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 更新科室信息并保存到本地
 * Created by asus on 2017/10/19.
 */

public class UpdateDepartmentInfoUtils {


    /**
     * 更新科室信息
     *
     * @param context 上下文
     */
    public static void updateDepartmentInfoReq(Context context) {
        getHospitalListReq(context);

    }

    /**
     * 获取医院列表
     *
     * @param context 上下文
     */
    private static void getHospitalListReq(final Context context) {
        RetrofitManagerUtils.getInstance(context, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanHospitalListReq()), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String str = null;
                try {
                    str = responseBody.string();
                    BeanHospitalListResp beanHospitalListResp = JSON.parseObject(str, BeanHospitalListResp.class);
                    getDepartmentInfoReq(context, beanHospitalListResp.getHospitalList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.e("LYQ", "医院列表响应：" + str);
            }
        });
    }

    /**
     * 更新科室信息的网络请求
     *
     * @param context                      上下文
     * @param beanHospitalListRespItemList 医院信息
     */
    public static void getDepartmentInfoReq(final Context context, final List<BeanHospitalListRespItem> beanHospitalListRespItemList) {
        if (!beanHospitalListRespItemList.isEmpty()) {
            String hosp = "";
            String hospTxt = "";
            if (beanHospitalListRespItemList.get(0).getId().startsWith("lzzyy1")) {
                beanHospitalListRespItemList.remove(0);
            }
            if (beanHospitalListRespItemList.isEmpty()) {
                return;
            }
            if (beanHospitalListRespItemList.get(0).getId().startsWith("lzzyy")) {
                hosp = "lzzyy";
                if (beanHospitalListRespItemList.get(0).getName().endsWith("东院")) {
                    hospTxt = "柳州市中医院-东院";
                } else {
                    hospTxt = "柳州市中医院";
                }
            } else {
                hosp = beanHospitalListRespItemList.get(0).getId();
                hospTxt = beanHospitalListRespItemList.get(0).getName();
            }
            BeanHospDeptListReq beanHospDeptListReq = new BeanHospDeptListReq();
            beanHospDeptListReq.setHosp(hosp);

            final String finalHosp = hosp;
            final String finalHospTxt = hospTxt;
            RetrofitManagerUtils.getInstance(context, null)
                    .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptListReq), new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                            beanHospitalListRespItemList.remove(0);
                            getDepartmentInfoReq(context, beanHospitalListRespItemList);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            String jsonStr = null;
                            try {
                                jsonStr = responseBody.string();
                                //Log.e("LYQ", "所有科室信息：" + jsonStr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            List<BeanHospDeptListRespItem> beanHospDeptListRespItemList = JSONArray.parseArray(jsonStr, BeanHospDeptListRespItem.class);
                            for (BeanHospDeptListRespItem beanHospDeptListRespItem : beanHospDeptListRespItemList) {
                                UpdateDepartmentInfoUtils.saveDepartmentInfo(new BeanDepartmentInfo(finalHosp + "_" + beanHospDeptListRespItem.getDEPT_CODE(), finalHospTxt, beanHospDeptListRespItem.getDEPT_NAME()));
                            }
                        }
                    });
        }
    }

    /**
     * 保存科室信息
     *
     * @param beanDepartmentInfo
     * @return 是否已保存
     */
    public static boolean saveDepartmentInfo(BeanDepartmentInfo beanDepartmentInfo) {
        if (beanDepartmentInfo.getKey().startsWith("lzzyy")) {
            if (beanDepartmentInfo.getDepartmentName().startsWith("东院")) {
                beanDepartmentInfo.setHospital("柳州市中医院-东院");
            } else {
                beanDepartmentInfo.setHospital("柳州市中医院");
            }
        }
        return beanDepartmentInfo.saveOrUpdate("key = ?", beanDepartmentInfo.getKey());
    }

    /**
     * 查找科室信息
     *
     * @param hosp 医院代码
     * @param code 科室编号
     * @return
     */
    public static String findDepartmentInfo(String hosp, String code) {
        List<BeanDepartmentInfo> list = DataSupport.where("key = ?", hosp + "_" + code).find(BeanDepartmentInfo.class);
        if (!list.isEmpty()) {
            return list.get(0).getHospital() + "_" + list.get(0).getDepartmentName();
        } else {
            return null;
        }
    }

}
