package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseKeySetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanConcernList;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.R;

import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.activity.appointment.DoctorDetail;
import com.healthyfish.healthyfish.ui.fragment.BuyServiceFragment;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.NestingUtils;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

/**
 * 描述：选择服务页面
 * 作者：LYQ on 2017/7/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChoiceService extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_doctor)
    CircleImageView civDoctor;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_DepartmentAndTitle)
    TextView tvDepartmentAndTitle;
    @BindView(R.id.tv_doctorCompany)
    TextView tvDoctorCompany;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.btn_sendTheMind)
    Button btnSendTheMind;
    @BindView(R.id.img_pictureConsulting)
    ImageView imgPictureConsulting;
    @BindView(R.id.tv_pictureConsultingPrice)
    TextView tvPictureConsultingPrice;
    @BindView(R.id.line_pictureConsulting)
    AutoLinearLayout linePictureConsulting;
    @BindView(R.id.img_privateDoctor)
    ImageView imgPrivateDoctor;
    @BindView(R.id.tv_privateDoctorPrice)
    TextView tvPrivateDoctorPrice;
    @BindView(R.id.line_privateDoctor)
    AutoLinearLayout linePrivateDoctor;
    @BindView(R.id.img_appointment)
    ImageView imgAppointment;
    @BindView(R.id.tv_appointmentPrice)
    TextView tvAppointmentPrice;
    @BindView(R.id.line_appointment)
    AutoLinearLayout lineAppointment;
    @BindView(R.id.cb_moreDoctorInfo)
    CheckBox cbMoreDoctorInfo;
    @BindView(R.id.line_moreDoctorInfo)
    AutoLinearLayout lineMoreDoctorInfo;
    @BindView(R.id.tv_doctorInfo)
    TextView tvDoctorInfo;
    @BindView(R.id.tv_doctorInfoMore)
    TextView tvDoctorInfoMore;
    @BindView(R.id.line_moreUserEvaluation)
    AutoLinearLayout lineMoreUserEvaluation;
    @BindView(R.id.listView_user_evaluate)
    ListView listViewUserEvaluate;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.btn_moreUserEvaluation)
    TextView btnMoreUserEvaluation;
    @BindView(R.id.layout_choiceService)
    AutoLinearLayout layoutChoiceService;

    private Context mContext;//全局上下文
//    private BeanHospDeptDoctListRespItem DeptDoctInfo;
//    private BeanHospRegisterReq beanHospRegisterReq;
    private BeanDoctorInfo beanDoctorInfo;

    private String imgDoctorUrl;  //医生头像资源
    private String doctorName;  //医生名字
    private String doctorDepartment;  //医生科室
    private String doctorTitle;  //医生职称
    private String doctorCompany;  //医生工作医院
    private String pictureConsultingPrice;  //图文咨询服务价格
    private String privateDoctorPrice;  //私人医生服务价格
    private String appointmentPrice;  //预约挂号服务价格
    private String doctorInfo;  //医生简介
    private boolean isOpenPictureConsulting;  //是否开通图文咨询
    private boolean isOpenPrivateDoctor;  //是否开通私人医生
    private boolean isOpenAppointment;  //是否开通预约挂号

    private boolean isAttention = false;//是否已经关注该医生

    private String uid = MyApplication.uid;

    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_service);
        ButterKnife.bind(this);
        mContext = this;
        initToolBar(toolbar, tvTitle, "选择服务");
        getData();
        isOpenPictureConsultingReq();
        tvAttentionListener();
        initData();
        initListView();
    }


    /**
     * 模拟医生数据
     */
    private void getData() {

        if (getIntent().getSerializableExtra("BeanDoctorInfo") != null) {
            beanDoctorInfo = (BeanDoctorInfo) getIntent().getSerializableExtra("BeanDoctorInfo");

//            DeptDoctInfo = new BeanHospDeptDoctListRespItem();
//            DeptDoctInfo.setDOCTOR(beanDoctorInfo.getDOCTOR());
//            DeptDoctInfo.setDOCTOR_NAME(beanDoctorInfo.getName());
//            DeptDoctInfo.setSchdList(beanDoctorInfo.getSchdList());
//            DeptDoctInfo.setCLINIQUE_CODE(beanDoctorInfo.getCLINIQUE_CODE());
//            DeptDoctInfo.setPRE_ALLOW(beanDoctorInfo.getPRE_ALLOW());
//            DeptDoctInfo.setPRICE(Integer.parseInt(beanDoctorInfo.getPrice()));
//            DeptDoctInfo.setREISTER_NAME(beanDoctorInfo.getDuties());
//            DeptDoctInfo.setSTAFF_NO(beanDoctorInfo.getSTAFF_NO());
//            DeptDoctInfo.setWEB_INTRODUCE(beanDoctorInfo.getIntroduce());
//            DeptDoctInfo.setWORK_TYPE(beanDoctorInfo.getWORK_TYPE());
//            DeptDoctInfo.setZHAOPIAN(beanDoctorInfo.getImgUrl());
//
//            beanHospRegisterReq = new BeanHospRegisterReq();
//            beanHospRegisterReq.setHosp(beanDoctorInfo.getHosp());
//            beanHospRegisterReq.setHospTxt(beanDoctorInfo.getHospital());
//            beanHospRegisterReq.setDept(beanDoctorInfo.getDept());
//            beanHospRegisterReq.setDeptTxt(beanDoctorInfo.getDepartment());
//            beanHospRegisterReq.setStaffNo(String.valueOf(beanDoctorInfo.getSTAFF_NO()));

        }

        imgDoctorUrl = HttpHealthyFishyUrl + beanDoctorInfo.getImgUrl();
        doctorName = beanDoctorInfo.getName();
        doctorTitle = beanDoctorInfo.getDuties();
        doctorInfo = beanDoctorInfo.getIntroduce();
        appointmentPrice = beanDoctorInfo.getPrice();
        doctorCompany = beanDoctorInfo.getHospital();
        doctorDepartment = beanDoctorInfo.getDepartment();

        pictureConsultingPrice = "免费";
        privateDoctorPrice = "免费";
        isOpenPictureConsulting = true;
        isOpenPrivateDoctor = false;
        isOpenAppointment = true;

    }

    /**
     * 模拟用户评价数据
     */
    private List<Map<String, Object>> getLisViewData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        return list;
    }

    /**
     * 设置是否关注按钮的监听
     */
    private void tvAttentionListener() {
        if (!TextUtils.isEmpty(uid)) {
            key = "care_" + uid + "_" + beanDoctorInfo.getHosp() + "_" + beanDoctorInfo.getDept() + "_" + String.valueOf(beanDoctorInfo.getSTAFF_NO());
            if (!DataSupport.where("key = ?", key).find(BeanConcernList.class).isEmpty()) {
                isAttention = true;
                tvAttention.setClickable(false);
            } else {
                isAttention = false;
            }
        }

        tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(uid)) {
                    if (tvAttention.getText().toString().equals("+关注")) {
                        addConcern();//加关注
                    }
                } else {
                    MyToast.showToast(ChoiceService.this, "您还没有登录呦！请先登录再关注");
                }
            }
        });

    }

    /**
     * 加关注操作
     */
    private void addConcern() {
        Log.i("LYQ", key);

        final String strJson = JSON.toJSONString(beanDoctorInfo);
        Log.i("LYQ", "strJsonBeanDoctorInfo:" + strJson);

        BeanBaseKeySetReq beanBaseKeySetReq = new BeanBaseKeySetReq();
        beanBaseKeySetReq.setKey(key);
        beanBaseKeySetReq.setValue(strJson);

        RetrofitManagerUtils.getInstance(mContext, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeySetReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                BeanBaseResp beanBaseResp = JSON.parseObject(strJson, BeanBaseResp.class);
                if (beanBaseResp.getCode() >= 0) {
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundResource(R.drawable.concern);
                    tvAttention.setTextColor(getResources().getColor(R.color.color_white));
                    tvAttention.setClickable(false);
                    isAttention = true;
                    BeanConcernList beanConcernList = new BeanConcernList();
                    beanConcernList.setKey(key);
                    if (beanConcernList.saveOrUpdate("key = ?", key)) {
                        beanConcernList.saveOrUpdate("key = ?", key);
                    }
                } else {
                    MyToast.showToast(ChoiceService.this, "关注失败，请重试");
                    tvAttention.setText("+关注");
                    tvAttention.setBackgroundResource(R.drawable.concern_not);
                    tvAttention.setTextColor(getResources().getColor(R.color.color_primary));
                    tvAttention.setClickable(true);
                    isAttention = false;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 所有点击事件的监听
     *
     * @param view
     */
    @OnClick({R.id.btn_sendTheMind, R.id.line_pictureConsulting, R.id.line_privateDoctor, R.id.line_appointment, R.id.line_moreDoctorInfo, R.id.line_moreUserEvaluation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sendTheMind:
                //跳转到送心意页面，需要传递医生标识
                if (!TextUtils.isEmpty(uid)) {
                    Intent intent = new Intent(this, SendMind.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", doctorName);
                    bundle.putString("imgUrl", imgDoctorUrl);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    MyToast.showToast(ChoiceService.this, "您还没有登录呦！请先登录");
                }

                break;
            case R.id.line_pictureConsulting:
                //点击购买图文咨询服务
                if (!TextUtils.isEmpty(uid)) {
                    buyPictureConsultingService();
                } else {
                    MyToast.showToast(ChoiceService.this, "您还没有登录呦！请先登录");
                }

                break;
            case R.id.line_privateDoctor:
                //点击购买私人医生服务
                if (!TextUtils.isEmpty(uid)) {
                    buyPrivateDoctorService();
                } else {
                    MyToast.showToast(ChoiceService.this, "您还没有登录呦！请先登录");
                }

                break;
            case R.id.line_appointment:
                //点击进行预约挂号
                if (!TextUtils.isEmpty(uid)) {
                    Intent intent1 = new Intent(ChoiceService.this, DoctorDetail.class);
//                    intent1.putExtra("BeanHospRegisterReq", beanHospRegisterReq);
//                    intent1.putExtra("BeanHospDeptDoctListRespItem", DeptDoctInfo);
                    intent1.putExtra("BeanDoctorInfo", beanDoctorInfo);
                    startActivity(intent1);
                } else {
                    MyToast.showToast(ChoiceService.this, "您还没有登录呦！请先登录");
                }

                break;
            case R.id.line_moreDoctorInfo:
                //点击显示更多医生介绍
                openAndStopDoctorInfo();
                break;
            case R.id.line_moreUserEvaluation:
                //点击展示更多用户评价
                MyToast.showToast(this, "没有更多用户评价");
                break;
        }
    }

    /**
     * 初始化显示数据
     */
    private void initData() {
        Glide.with(mContext).load(imgDoctorUrl).error(R.mipmap.error).into(civDoctor);//设置医生头像
        tvName.setText(doctorName);  //设置医生名字
        tvDepartmentAndTitle.setText(doctorDepartment + "   " + beanDoctorInfo.getDuties());  //设置医生的科室和职称
        tvDoctorCompany.setText(doctorCompany);  //设置医生工作的医院
        //判断是否已经关注该医生
        if (isAttention) {
            tvAttention.setText("已关注");
            tvAttention.setBackgroundResource(R.drawable.concern);
            tvAttention.setTextColor(getResources().getColor(R.color.color_white));
            tvAttention.setClickable(false);
        } else {
            tvAttention.setText("+关注");
            tvAttention.setBackgroundResource(R.drawable.concern_not);
            tvAttention.setTextColor(getResources().getColor(R.color.color_primary));
            tvAttention.setClickable(true);
        }
        //判断是否已开通私人医生服务
        if (isOpenPrivateDoctor) {
            imgPrivateDoctor.setImageResource(R.mipmap.ic_private_doctor_open);
            //tvPrivateDoctorPrice.setText(privateDoctorPrice + "元/周");
            tvPrivateDoctorPrice.setText(privateDoctorPrice);
            tvPrivateDoctorPrice.setTextColor(getResources().getColor(R.color.color_primary));
        } else {
            imgPrivateDoctor.setImageResource(R.mipmap.ic_private_doctor_not_open);
            tvPrivateDoctorPrice.setText("暂未开通");
        }
        //判断是否已开通预约挂号服务
        if (isOpenAppointment) {
            imgAppointment.setImageResource(R.mipmap.ic_appointment_open);
            tvAppointmentPrice.setText(appointmentPrice + "元/次");
            tvAppointmentPrice.setTextColor(getResources().getColor(R.color.color_primary));
        } else {
            imgAppointment.setImageResource(R.mipmap.ic_appointment_not_open);
            tvAppointmentPrice.setText("暂未开通");
        }
        //设置医生简介
        tvDoctorInfo.setText(doctorInfo);
        tvDoctorInfoMore.setText(doctorInfo);
    }

    /**
     * 初始化用户评价ListView
     */
    public void initListView() {
        //评价列表适配器
        SimpleAdapter adapter = new SimpleAdapter(this, getLisViewData(), R.layout.item_more_evalutae_listview,
                new String[]{"username", "satisfaction", "content"},
                new int[]{R.id.tv_user, R.id.tv_satisfaction_degree, R.id.tv_evaluate});
        listViewUserEvaluate.setAdapter(adapter);
        NestingUtils.setListViewHeightBasedOnChildren(listViewUserEvaluate);
    }

    /**
     * 展开和收起更多医生详情操作
     */
    public void openAndStopDoctorInfo() {
        if (tvDoctorInfo.getVisibility() == View.VISIBLE) {
            tvDoctorInfo.setVisibility(View.GONE);
            tvDoctorInfoMore.setVisibility(View.VISIBLE);
            cbMoreDoctorInfo.setChecked(true);
        } else {
            tvDoctorInfoMore.setVisibility(View.GONE);
            tvDoctorInfo.setVisibility(View.VISIBLE);
            cbMoreDoctorInfo.setChecked(false);
        }
    }

    /**
     * 选择预约挂号操作
     */
    private void buyAppointmentService() {
        if (isOpenAppointment) {
            BuyServiceFragment buyServiceFragment = new BuyServiceFragment();
            Bundle bundle = new Bundle();
            bundle.putString("serviceType", "预约挂号");
            bundle.putString("name", doctorName);
            bundle.putString("price", appointmentPrice);
            buyServiceFragment.setArguments(bundle);
            buyServiceFragment.show(getSupportFragmentManager(), "buyServiceFragment");
        } else {
            MyToast.showToast(mContext, "该医生暂未开通此服务");
        }
    }

    /**
     * 选择私人医生操作
     */
    private void buyPrivateDoctorService() {
        if (isOpenPrivateDoctor) {
            BuyServiceFragment buyServiceFragment = new BuyServiceFragment();
            Bundle bundle = new Bundle();
            bundle.putString("serviceType", "私人医生");
            bundle.putString("name", doctorName);
            bundle.putString("price", privateDoctorPrice);
            buyServiceFragment.setArguments(bundle);
            buyServiceFragment.show(getSupportFragmentManager(), "buyServiceFragment");
        } else {
            MyToast.showToast(mContext, "该医生暂未开通此服务");
        }
    }

    /**
     * 选择图文咨询操作
     */
    private void buyPictureConsultingService() {
        if (isOpenPictureConsulting) {
            BuyServiceFragment buyServiceFragment = new BuyServiceFragment();
            Bundle bundle = new Bundle();
            bundle.putString("serviceType", "图文咨询");
            bundle.putString("name", doctorName);
            bundle.putString("price", pictureConsultingPrice);
            buyServiceFragment.setArguments(bundle);
            buyServiceFragment.show(getSupportFragmentManager(), "buyServiceFragment");
        } else {
            MyToast.showToast(mContext, "该医生暂未开通此服务");
        }
    }

    private void isOpenPictureConsultingReq() {
        String reqKey = "ol_" + beanDoctorInfo.getHosp() + "_" + beanDoctorInfo.getDept() + "_" + beanDoctorInfo.getSTAFF_NO();
        final BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(reqKey);

        RetrofitManagerUtils.getInstance(this,null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                //判断是否已开通图文咨询服务
                if (isOpenPictureConsulting) {
                    imgPictureConsulting.setImageResource(R.mipmap.ic_picture_consulting_open);
                    //tvPictureConsultingPrice.setText(pictureConsultingPrice + "元/次");
                    tvPictureConsultingPrice.setText(pictureConsultingPrice);
                    tvPictureConsultingPrice.setTextColor(getResources().getColor(R.color.color_primary));
                } else {
                    imgPictureConsulting.setImageResource(R.mipmap.ic_picture_consulting_not_open);
                    tvPictureConsultingPrice.setText("暂未开通");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(ChoiceService.this, "获取开通服务情况失败,请检查网络状态");
                imgPictureConsulting.setImageResource(R.mipmap.ic_picture_consulting_not_open);
                tvPictureConsultingPrice.setText("暂未开通");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String resp = null;
                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "isOpenPictureConsultingReq:" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(resp)) {
                    BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                    if (beanBaseKeyGetResp.getCode() >= 0) {
                        if (!TextUtils.isEmpty(beanBaseKeyGetResp.getValue())) {
                            isOpenPictureConsulting = true;
                        } else {
                            isOpenPictureConsulting = false;
                        }
                    } else {
                        MyToast.showToast(ChoiceService.this, "获取开通服务情况失败");
                    }
                } else {
                    MyToast.showToast(ChoiceService.this, "获取开通服务情况失败");
                }
            }
        });
    }

}
