package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {

    @BindView(R.id.civ_head_portrait)
    CircleImageView civHeadPortrait;
    @BindView(R.id.tv_login_or_register)
    TextView tvLoginOrRegister;
    @BindView(R.id.rly_mail)
    AutoRelativeLayout rlyMail;
    @BindView(R.id.lly_personal_information)
    AutoLinearLayout llyPersonalInformation;
    @BindView(R.id.lly_my_news)
    AutoLinearLayout llyMyNews;
    @BindView(R.id.lly_my_concern)
    AutoLinearLayout llyMyConcern;
    @BindView(R.id.lly_feedback)
    AutoLinearLayout llyFeedback;
    @BindView(R.id.lly_set)
    AutoLinearLayout llySet;
    Unbinder unbinder;

    private Context mContext;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_personal_center, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initInfoPrompt("16");
        return rootView;
    }

    /**
     * 初始化显示消息数控件
     *
     * @param string:总消息数
     */
    private void initInfoPrompt(String string) {
        Badge badge = new QBadgeView(mContext).bindTarget(rlyMail);
        badge.setBadgeBackgroundColor(0xFFF70909);
        badge.setBadgeTextColor(0xffffffFF);
        badge.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
        badge.setBadgeTextSize(12, true);
        badge.setBadgePadding(3, true);
        badge.setBadgeText(string);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.civ_head_portrait, R.id.tv_login_or_register, R.id.rly_mail, R.id.lly_personal_information, R.id.lly_my_news, R.id.lly_my_concern, R.id.lly_feedback, R.id.lly_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_head_portrait:
                break;
            case R.id.tv_login_or_register:
                break;
            case R.id.rly_mail:
                break;
            case R.id.lly_personal_information:
                break;
            case R.id.lly_my_news:
                break;
            case R.id.lly_my_concern:
                break;
            case R.id.lly_feedback:
                break;
            case R.id.lly_set:
                break;
        }
    }
}
