package com.healthyfish.healthyfish.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

import static java.lang.System.load;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {


    @BindView(R.id.topbar_scan)
    ImageView topbarScan;
    @BindView(R.id.topbar_search_iv)
    ImageView topbarSearchIv;
    @BindView(R.id.topbar_search_et)
    EditText topbarSearchEt;
    @BindView(R.id.topbar_info)
    ImageView topbarInfo;
    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;


    }

    private void init() {
        bannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.error)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });

        bannerGuideContent.setData(Arrays.asList("https://github.com/panxw/android-image-indicator/blob/master/screenshot/guider_00.jpg",
                "https://github.com/panxw/android-image-indicator/blob/master/screenshot/guider_01.jpg",
                "https://github.com/panxw/android-image-indicator/blob/master/screenshot/guider_01.jpg"),
                Arrays.asList("提示文字1", "提示文字2", "提示文字3"));



//        List<View> views = new ArrayList<>();
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.placeholder));
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.healthy_circle));
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.health_workshop));
//        bannerGuideContent.setData(views);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

