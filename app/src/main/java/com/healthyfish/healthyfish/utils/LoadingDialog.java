package com.healthyfish.healthyfish.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

/**
 * 描述：
 * 作者：LYQ on 2017/7/24.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class LoadingDialog extends Dialog {

    private TextView tvLoading;//标题

    public LoadingDialog(Context context, String str) {
        super(context,R.style.Theme_AppCompat_Light_Dialog);
        setContentView(R.layout.layout_loading);//设置对话框的布局
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        tvLoading.setText(str);
        show();//显示对话框
    }

}
