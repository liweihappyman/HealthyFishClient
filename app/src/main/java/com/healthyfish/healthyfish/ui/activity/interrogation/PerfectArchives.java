package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.InputUtils;
import com.healthyfish.healthyfish.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：问诊模块购买私人医生服务成功后跳转到的完善档案页面
 * 作者：LYQ on 2017/7/6.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PerfectArchives extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_gender)
    EditText etGender;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.btn_done)
    Button btnDone;

    private final int resultSuccessfulCode = 1;
    private final int resultFailedCode = 0;

    private String name;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String phoneNumber;

    private String shopType = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_document);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"个人基本信息");
        getFormerData();
        setFailResult();
        setTextHintSize(etName,"姓名  *必填");
        setTextHintSize(etGender,"性别  *必填");
        setTextHintSize(etAge,"年龄  *必填");
    }


    @OnClick(R.id.btn_done)
    public void onViewClicked() {
        if (shopType != null){
            if (shopType.equals("图文咨询")){
                getInputInfo();  //获取输入的信息
                if (!name.isEmpty() && !gender.isEmpty() && !age.isEmpty()){
                    if (InputUtils.isGender(gender)){
                        if (InputUtils.isAge(age)){
                            Intent intent = new Intent();
                            intent.putExtra("privateInfo",name+"（"+gender+"，"+age+"岁）");
                            PerfectArchives.this.setResult(resultSuccessfulCode, intent);
                            finish();
                        }else {
                            MyToast.showToast(this,"请认真填写年龄选项噢！");
                        }
                   }else {
                        MyToast.showToast(this,"请认真填写性别选项噢！");
                    }
                }else {
                    MyToast.showToast(this,"您还有必填项没有填写噢！");
                }
            }
        } else{
            //跳转到聊天界面
            //Intent intent = new Intent(this,InquiryChatActivity.class);
            //startActivity(intent);
        }
    }

    /**
     * 获取上一页面的参数
     */
    private void getFormerData() {
        //接收上一页面传递过来的购买服务类型
        Intent intent = this.getIntent();
        if (intent.getExtras() != null) {
            shopType = intent.getStringExtra("shopType");
        }
    }

    /**
     * 设置完善档案失败的返回数据
     */
    private void setFailResult() {
        Intent intent = new Intent();
        intent.putExtra("privateInfo","档案未提交");
        PerfectArchives.this.setResult(resultFailedCode, intent);
    }

    /**
     * 获取输入的信息
     */
    private  void getInputInfo(){
        name = etName.getText().toString().trim();//获取输入的姓名
        gender = etGender.getText().toString().trim();
        age = etAge.getText().toString().trim();
        height = etHeight.getText().toString().trim();
        weight = etWeight.getText().toString().trim();
        phoneNumber = etPhoneNumber.getText().toString().trim();
    }

    /**
     * 设置字体大小
     */
    private void setTextHintSize(EditText et,String str) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(str);
        //设置字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(50);
        spannableString.setSpan(absoluteSizeSpan, 2, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        et.setHint(spannableString);
    }

}
