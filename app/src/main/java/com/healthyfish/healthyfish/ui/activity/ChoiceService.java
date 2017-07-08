package com.healthyfish.healthyfish.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.fragment.BuyServiceFragment;
import com.healthyfish.healthyfish.utils.MyToast;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.ckb_attention)
    CheckBox ckbAttention;
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

    private boolean isAttention;//是否已经关注该医生

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_service);
        ButterKnife.bind(this);
        mContext = this;
        initToolBar(toolbar,tvTitle,"选择服务");
        getData();
        initData();
        initListView();
        ckbAttentionListener();
    }


    /**
     * 模拟医生数据
     */
    private void getData() {
        //模拟数据
        String info = "擅长：消化系统疾病、口腔溃疡、阴虚、肾阳虚、阳虚、虚火。（测试数据）擅长：消化系统疾病、口腔溃疡、阴虚、肾阳虚、阳虚、虚火。（测试数据）" +
                "擅长：消化系统疾病、口腔溃疡、阴虚、肾阳虚、阳虚、虚火。（测试数据）擅长：消化系统疾病、口腔溃疡、阴虚、肾阳虚、阳虚、虚火。（测试数据）" +
                "擅长：消化系统疾病、口腔溃疡、阴虚、肾阳虚、阳虚、虚火。（测试数据）";

        doctorName = "赵婧";
        imgDoctorUrl = "http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg";
        doctorDepartment = "五官科";
        doctorTitle = "医师";
        doctorCompany = "柳州市中医院";
        pictureConsultingPrice = "30";
        privateDoctorPrice = "100";
        appointmentPrice = "10";
        doctorInfo = info;
        isOpenPictureConsulting = false;
        isOpenPrivateDoctor = true;
        isOpenAppointment = false;
        isAttention = false;
    }

    /**
     * 模拟用户评价数据
     */
    private List<Map<String, Object>> getLisViewData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "非凡boy");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生在看诊过程中非常细心，而且对于我的问题解答得很详细！赞！");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("username", "非凡girl");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生在看诊过程中非常细心，而且对于我的问题解答得很详细！赞！张宇医生在看诊过程中非常细心，而且对于我的问题解答得很详细！赞！");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("username", "非凡girl");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生在看诊过程中非常细心，而且对于我的问题解答得很详细！赞！");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("username", "非凡girl");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生在看诊过程中非常细心，赞！");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("username", "非凡girl");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生，赞！");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("username", "非凡girl");
        map.put("satisfaction", "很满意");
        map.put("content", "张宇医生，一般般！");
        list.add(map);

        return list;
    }

    /**
     * 设置是否关注按钮的监听
     */
    private void ckbAttentionListener() {
        ckbAttention.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //需要访问服务器改变关注的状态
                    ckbAttention.setText("已关注");
                    isAttention = true;
                } else {
                    //需要访问服务器改变关注的状态
                    ckbAttention.setText("+关注");
                    isAttention = false;
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
                Intent intent = new Intent(this, SendMind.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", doctorName);
                bundle.putString("imgUrl", imgDoctorUrl);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.line_pictureConsulting:
                //点击购买图文咨询服务
                buyPictureConsultingService();
                break;
            case R.id.line_privateDoctor:
                //点击购买私人医生服务
                buyPrivateDoctorService();
                break;
            case R.id.line_appointment:
                //点击进行预约挂号
                buyAppointmentService();
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
        tvDepartmentAndTitle.setText(doctorDepartment + "   " + doctorTitle);  //设置医生的科室和职称
        tvDoctorCompany.setText(doctorCompany);  //设置医生工作的医院
        //判断是否已经关注该医生
        if (isAttention) {
            ckbAttention.setChecked(true);
            ckbAttention.setText("已关注");
        } else {
            ckbAttention.setChecked(false);
            ckbAttention.setText("+关注");
        }
        //判断是否已开通图文咨询服务
        if (isOpenPictureConsulting) {
            imgPictureConsulting.setImageResource(R.mipmap.ic_picture_consulting_open);
            tvPictureConsultingPrice.setText(pictureConsultingPrice + "元/次");
            tvPictureConsultingPrice.setTextColor(getResources().getColor(R.color.color_primary));
        } else {
            imgPictureConsulting.setImageResource(R.mipmap.ic_picture_consulting_not_open);
            tvPictureConsultingPrice.setText("暂未开通");
        }
        //判断是否已开通私人医生服务
        if (isOpenPrivateDoctor) {
            imgPrivateDoctor.setImageResource(R.mipmap.ic_private_doctor_open);
            tvPrivateDoctorPrice.setText(privateDoctorPrice + "元/周");
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

}
