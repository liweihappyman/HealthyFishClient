package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanVisitingPerson;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChangeVisitingPersonAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.AllMedRec;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.NewMedRec;
import com.healthyfish.healthyfish.utils.MyToast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：挂号更换就诊人页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangeVisitingPerson extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_change_visiting_person)
    ListView lvChangeVisitingPerson;
    @BindView(R.id.bt_complete)
    Button btComplete;

    private ChangeVisitingPersonAdapter adapter;
    private List<BeanVisitingPerson> list = new ArrayList<>();

    private final static int mRequestCode = 10053;
    public final static int mResultCode = 10056;
    private BeanVisitingPerson visitingPerson;

    private String id = MyApplication.uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_visiting_person);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "更换就诊人");
        getData();
        initListView();
    }


    @OnClick({R.id.tv_new, R.id.bt_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_new:
                //新建就诊人
                Intent intent = new Intent(this, NewVisitingPerson.class);
                startActivityForResult(intent, mRequestCode);
                break;
            case R.id.bt_complete:
                //完成就诊人的选择
                int position = lvChangeVisitingPerson.getCheckedItemPosition();
                if (lvChangeVisitingPerson.INVALID_POSITION != position) {
                    BeanVisitingPerson visitingPerson = list.get(position);
                    Intent intent1 = new Intent(this,ConfirmReservationInformation.class);
                    intent1.putExtra("BeanVisitingPerson", visitingPerson);
                    ChangeVisitingPerson.this.setResult(mResultCode, intent1);
                    finish();
                } else {
                    MyToast.showToast(this, "请选择就诊人");
                }
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        adapter = new ChangeVisitingPersonAdapter(this, list);
        lvChangeVisitingPerson.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvChangeVisitingPerson.setAdapter(adapter);
        lvChangeVisitingPerson.setVerticalScrollBarEnabled(false);
        lvChangeVisitingPerson.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDelDialog(position);
                return true;
            }
        });
    }

    /**
     * 数据库查找就诊人
     */
    private void getData() {
        List<BeanVisitingPerson> visitingPersonList = DataSupport.where("phoneId = ? ", id).find(BeanVisitingPerson.class);
        for (BeanVisitingPerson visitingPerson : visitingPersonList) {
            list.add(visitingPerson);
        }
    }

    /**
     * 删除就诊人
     */
    private void deleteData(int position) {
        int deleteCount = DataSupport.delete(BeanVisitingPerson.class, list.get(position).getId());
        if (deleteCount == 1) {
            MyToast.showToast(this, "成功删除该就诊人");
            list.remove(position);
            adapter.notifyDataSetChanged();
        } else {
            MyToast.showToast(this, "删除就诊人失败");
        }
    }

    /**
     * 更新就诊人
     */
    private void updateData(int id) {
        BeanVisitingPerson visitingPerson = new BeanVisitingPerson();
        int deleteCount = visitingPerson.update(id);
        if (deleteCount == id) {
            MyToast.showToast(this, "成功修改该就诊人");
        } else {
            MyToast.showToast(this, "修改就诊人失败");
        }
    }

    /**
     * 删除提示对话框
     */
    private void showDelDialog(final int position) {
        new AlertDialog.Builder(ChangeVisitingPerson.this).setMessage("是否要删除此就诊人？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            if (resultCode == NewVisitingPerson.resultCode) {
                visitingPerson = (BeanVisitingPerson) data.getSerializableExtra("BeanVisitingPerson");
                list.add(visitingPerson);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
