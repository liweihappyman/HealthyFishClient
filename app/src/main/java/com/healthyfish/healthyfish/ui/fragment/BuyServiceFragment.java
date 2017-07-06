package com.healthyfish.healthyfish.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.Pay;

/**
 * 描述：问诊模块选择服务页面点击不同服务弹出的相应购买服务窗口的处理
 * 作者：LYQ on 2017/7/4.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BuyServiceFragment extends DialogFragment {

    private ImageView serviceImg;
    private TextView serviceTypeAndDoctorName;
    private TextView serviceDscribe;
    private TextView servicePrice;
    private Button shopNow;
    private Bundle bundleServiceType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Dialog dialog = initDialog();
        initView(dialog);
        initData();
        btListener();
        return dialog;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        bundleServiceType = getArguments();
        if (bundleServiceType != null) {
            if (bundleServiceType.get("serviceType").equals("图文咨询")){
                serviceImg.setImageResource(R.mipmap.ic_picture_consulting_open);
                serviceTypeAndDoctorName.setText("图文咨询-"+ bundleServiceType.get("name"));
                servicePrice.setText(bundleServiceType.get("price")+"元/次");
            }else if (bundleServiceType.get("serviceType").equals("私人医生")){
                serviceImg.setImageResource(R.mipmap.ic_private_doctor_open);
                serviceTypeAndDoctorName.setText("私人医生-"+ bundleServiceType.get("name"));
                servicePrice.setText(bundleServiceType.get("price")+"元/周");
            }else if (bundleServiceType.get("serviceType").equals("预约挂号")){
                serviceImg.setImageResource(R.mipmap.ic_appointment_open);
                serviceTypeAndDoctorName.setText("预约挂号-"+ bundleServiceType.get("name"));
                servicePrice.setText(bundleServiceType.get("price")+"元/次");
            }
        }
    }

    /**
     * 初始化Dialog
     */
    private Dialog initDialog() {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);// 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.layout_buy_service);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);// 设置出入动画
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹
        window.setAttributes(lp);
        return dialog;
    }

    /**
     * 绑定控件
     * @param dialog
     */
    private void initView(Dialog dialog) {
        serviceImg = (ImageView) dialog.findViewById(R.id.iv_service_type);
        serviceTypeAndDoctorName = (TextView) dialog.findViewById(R.id.tv_serviceType_and_doctorName);
        serviceDscribe = (TextView) dialog.findViewById(R.id.tv_serviceType_describe);
        servicePrice = (TextView) dialog.findViewById(R.id.tv_price);
        shopNow = (Button) dialog.findViewById(R.id.btn_shopNow);
    }

    /**
     * 立即购买按钮的监听
     */
    private void btListener(){
        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDialog() != null){
                    Intent intent = new Intent(getActivity(), Pay.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("serviceType",bundleServiceType.get("serviceType").toString());
                    bundle.putString("name",bundleServiceType.get("name").toString());
                    bundle.putString("price",bundleServiceType.get("price").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getDialog().dismiss();
                }
            }
        });
    }

}
