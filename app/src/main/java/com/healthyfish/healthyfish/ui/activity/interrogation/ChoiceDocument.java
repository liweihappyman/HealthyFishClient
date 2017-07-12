package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChoiceDocumentLvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：问诊选择档案页面
 * 作者：LYQ on 2017/7/6.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChoiceDocument extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_choice_document)
    ListView lvChoiceDocument;
    @BindView(R.id.bt_add_new_document)
    Button btAddNewDocument;
    @BindView(R.id.btn_done)
    Button btnDone;

    private int mRequestCode = 000;
    private ChoiceDocumentLvAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_document);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"选择档案");
        List<String> list = getDocument();
        initListView(list);
    }

    /**
     * 获取已经存在的档案
     */
    private List<String> getDocument() {
        List<String> list = new ArrayList<>();
        list.add("老李（男，35岁）");
        list.add("老王（男，35岁）");
        return list;
    }


    @OnClick({R.id.bt_add_new_document, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_add_new_document:
                Bundle bundle = new Bundle();
                bundle.putString("shopType","图文咨询");
                Intent intent = new Intent(this,PerfectArchives.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, mRequestCode);
                break;
            case R.id.btn_done:
                int position = lvChoiceDocument.getCheckedItemPosition();
                if (lvChoiceDocument.INVALID_POSITION != position) {
                    Intent intent02 = new Intent(this,InterrogationService.class);
                    startActivity(intent02);
                }else {
                    MyToast.showToast(this,"未选择档案");
                }
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView(List<String> list) {
        lvChoiceDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//设置单选功能
        mAdapter = new ChoiceDocumentLvAdapter(this,list);
        lvChoiceDocument.setAdapter(mAdapter);
    }

    /**
     * 根据从完善档案页面传回来的数据进行判断处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode){
            String info = data.getStringExtra("privateInfo");
            // 根据发送过去的请求来区别
            switch (resultCode){
                case 0:
                    MyToast.showToast(this,"档案未提交");
                    break;
                case 1:
                    mAdapter.addDataToAdapter(info);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
