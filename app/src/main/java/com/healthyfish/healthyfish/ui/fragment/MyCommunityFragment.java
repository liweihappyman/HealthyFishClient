package com.healthyfish.healthyfish.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.healthyfish.healthyfish.POJO.BeanHealthyCircleItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MyCommunityAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：健康圈我关注的社区Fragment
 * 作者：LYQ on 2017/7/9.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyCommunityFragment extends Fragment {

    @BindView(R.id.lv_my_community)
    ListView lvMyCommunity;
    Unbinder unbinder;

    private Context mContext;
    private View rootView;
    public static MyCommunityAdapter adapter ;
    public List<BeanHealthyCircleItem> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_community, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        initListView();
        return rootView;
    }

    /**
     * 初始化模拟数据
     */
    private void initData() {
        for (int i = 0; i < 5; i++) {
            BeanHealthyCircleItem bean = new BeanHealthyCircleItem(null, "http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg", "愉悦社区", "这是一个非常有乐趣的社区");
            list.add(bean);
        }
    }

    /**
     * 初始化listView适配器
     */
    private void initListView() {
        adapter = new MyCommunityAdapter(mContext,list);
        lvMyCommunity.setAdapter(adapter);
        lvMyCommunity.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
