package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.AutoLinearLayout;

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

public class ChoiceService extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_doctor)
    CircleImageView imgDoctor;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_DepartmentAndTitle)
    TextView tvDepartmentAndTitle;
    @BindView(R.id.tv_doctorCompany)
    TextView tvDoctorCompany;
    @BindView(R.id.btn_attention)
    Button btnAttention;
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
    @BindView(R.id.btn_moreUserEvaluation)
    Button btnMoreUserEvaluation;
    @BindView(R.id.line_moreUserEvaluation)
    AutoLinearLayout lineMoreUserEvaluation;
    @BindView(R.id.listView_user_evaluate)
    ListView listViewUserEvaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_service);
        ButterKnife.bind(this);
        initToolBar();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        toolbar.setTitle("");//设置不显示应用名
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    /**
     * 返回按钮的监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.btn_attention, R.id.btn_sendTheMind, R.id.line_pictureConsulting,R.id.line_privateDoctor, R.id.line_appointment, R.id.line_moreDoctorInfo, R.id.btn_moreUserEvaluation, R.id.line_moreUserEvaluation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_attention:
                break;
            case R.id.btn_sendTheMind:
                Intent intent = new Intent(this, SendMind.class);
                startActivity(intent);
                break;
            case R.id.line_pictureConsulting:
                break;
            case R.id.line_privateDoctor:
                break;
            case R.id.line_appointment:
                break;
            case R.id.line_moreDoctorInfo:
                break;
            case R.id.btn_moreUserEvaluation:
                break;
            case R.id.line_moreUserEvaluation:
                break;
        }
    }
}
