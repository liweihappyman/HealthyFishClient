package com.healthyfish.healthyfish.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationServiceAdapter;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：问诊服务中当前服务选项页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class CurrentServiceFragment extends Fragment {

    @BindView(R.id.lv_current_service)
    ListView lvCurrentService;
    Unbinder unbinder;

    private View rootView;
    private InterrogationServiceAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_current_service, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        List<Map<String, Object>> list = initData();
        initListView(list);
        lvListener(list);
        return rootView;
    }

    /**
     * ListView的点击监听
     */
    private void lvListener(final List<Map<String, Object>> list) {
        lvCurrentService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) list.get(position).get("type");
                String name = list.get(position).get("name").toString();
                MyToast.showToast(getActivity(),type+":"+name);
            }
        });
    }

    /**
     * 初始化ListView
     * @param list
     */
    private void initListView(List<Map<String, Object>> list) {
        mAdapter = new InterrogationServiceAdapter(getActivity(), list);
        //mAdapter.notifyDataSetChanged();
        lvCurrentService.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     * @return
     */
    private List<Map<String, Object>> initData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  // 声明列表容器
        Map<String, Object> map;
        for (int i = 0; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("type", "pictureConsulting");
            map.put("title", "您跟图文咨询的医生进行通话的最后一句留言...");
            map.put("time", "2016年12月7日");
            map.put("department", "中医科");
            map.put("name", "赵婧");
            map.put("reply", "已回复");
            list.add(map);
        }

        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, Object>();
            map.put("type", "privateDoctor");
            map.put("image", "http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
            map.put("name", "赵婧");
            map.put("department", "中医科");
            map.put("duties", "主任医师");
            map.put("hospital", "柳州市中医院");
            map.put("finishTime", "距离私人医生服务结束时间还有5天");
            list.add(map);
        }

        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
