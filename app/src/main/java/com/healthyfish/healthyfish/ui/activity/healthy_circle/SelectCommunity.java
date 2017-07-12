package com.healthyfish.healthyfish.ui.activity.healthy_circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：健康圈选择社区页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SelectCommunity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_select_community)
    ListView lvSelectCommunity;

    private Bundle bundle = null;
    private String selectedFlag = null;

    private final int resultSuccessfulCode = 0;
    private final int resultFailedCode = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_community);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"选择社区");
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        final String[] str = new String[]{"Tile"};
        final List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0; i<10; i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put(str[0],"健康社区"+i);
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(SelectCommunity.this,list,R.layout.item_select_community_lv,str,new int[]{R.id.tv_community_title});
        lvSelectCommunity.setAdapter(simpleAdapter);
        lvSelectCommunity.setVerticalScrollBarEnabled(false);
        //ListView的item点击监听
        lvSelectCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("Title",list.get(position).get(str[0]).toString());
                SelectCommunity.this.setResult(resultSuccessfulCode, intent);
                finish();
            }
        });
    }

    /**
     * 重写头部返回按钮的监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                SelectCommunity.this.setResult(resultFailedCode, intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 重写物理返回键的监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            SelectCommunity.this.setResult(resultFailedCode, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
