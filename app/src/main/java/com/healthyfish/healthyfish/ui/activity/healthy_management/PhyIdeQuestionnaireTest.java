package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanPhyQuestionnaireTest;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.PhyQuestionnaireTestAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 描述：体质测试问卷
 * 作者：Wayne on 2017/7/9 17:13
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
// FIXME: 2017/7/9 完成问卷测试
public class PhyIdeQuestionnaireTest extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_phy_ide_title)
    TextView tvPhyIdeTitle;
    @BindView(R.id.iv_divider)
    ImageView ivDivider;
    @BindView(R.id.lv_phy_test)
    ListView lvPhyTest;
    @BindView(R.id.bt_commit_test)
    Button btCommitTest;

    private PhyQuestionnaireTestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_ide_questionnaire_test);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "体质测试问卷");
        initListView();
    }

    /**
     * 初始化问卷列表
     */
    private void initListView() {
        String[] testTitle = new String[]{"您容易疲劳吗？","您说话时感觉声音低弱无力吗？","您怕冷吗？","您手脚发凉吗？",
        "您感到手脚心发热吗？","您感觉眼睛干涩吗？","您感觉身体沉重不轻松吗？","您觉得嘴里有黏黏的感觉吗？","您感到口苦或嘴里有异味吗？",
        "您感觉大便黏滞不爽，有解不干净的情况吗？","您容易忘事吗？","您的嘴唇颜色偏暗吗？","您会不由自主的叹气吗？",
        "您感到闷闷不乐或情绪低落吗？","您容易过敏吗？"};

        List<BeanPhyQuestionnaireTest> list = new ArrayList<>();
        BeanPhyQuestionnaireTest beanPhyTeat;
        for (int i=0; i<testTitle.length; i++){
            beanPhyTeat = new BeanPhyQuestionnaireTest();
            beanPhyTeat.setContent(testTitle[i]);
            list.add(beanPhyTeat);
        }
        adapter = new PhyQuestionnaireTestAdapter(this, list);
        lvPhyTest.setAdapter(adapter);
        lvPhyTest.setVerticalScrollBarEnabled(false);
    }

    @OnClick(R.id.bt_commit_test)
    public void onViewClicked() {
        //点击提交测试按钮跳转
        List<BeanPhyQuestionnaireTest> list = adapter.getData();
        for (int i=0; i<list.size(); i++){
            if (list.get(i).getAnswer() == null){
                MyToast.showToast(this,"您还有选项没有选择噢！");
                return;
            }
        }
        //将答案列表上传到服务器
        Intent intent = new Intent(this, PhyIdeReport.class);
        startActivity(intent);
    }
}
