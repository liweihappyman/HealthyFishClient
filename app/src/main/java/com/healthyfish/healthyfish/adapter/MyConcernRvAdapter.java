package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.healthyfish.healthyfish.POJO.BeanMyConcernDoctorItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：个人中心我的关注页面的适配器
 * 作者：LYQ on 2017/7/8.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyConcernRvAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanMyConcernDoctorItem> mList = new ArrayList<>();
    private HolderDoctor holderDoctor = null;

    public MyConcernRvAdapter(Context mContext, List<BeanMyConcernDoctorItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sly_message_view;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_my_concern_doctor,null);
        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        holderDoctor = new HolderDoctor(convertView);
        Glide.with(mContext).load(mList.get(position).getImgUrl()).into(holderDoctor.civHeadPortrait);
        holderDoctor.tvDoctorName.setText(mList.get(position).getName());
        holderDoctor.tvDoctorDepartment.setText(mList.get(position).getDepartment());
        holderDoctor.tvDoctorTitle.setText(mList.get(position).getDuties());
        holderDoctor.tvHospital.setText(mList.get(position).getHospital());
        holderDoctor.tvDoctorInfo.setText(mList.get(position).getDescribe());

        holderDoctor.slyMessageView.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        holderDoctor.slyMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyToast.showToast(mContext, mList.get(position).getName());

            }
        });

        holderDoctor.tvCancelAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showToast(mContext,"删除了第"+position+"条："+mList.get(position).getName());
                mList.remove(position);
                MyConcernRvAdapter.this.notifyDataSetChanged();
                holderDoctor.slyMessageView.close();
            }
        });
    }

    public class HolderDoctor extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_cancel_attention)
        TextView tvCancelAttention;
        @BindView(R.id.civ_head_portrait)
        CircleImageView civHeadPortrait;
        @BindView(R.id.tv_doctorName)
        TextView tvDoctorName;
        @BindView(R.id.tv_doctorDepartment)
        TextView tvDoctorDepartment;
        @BindView(R.id.tv_doctorTitle)
        TextView tvDoctorTitle;
        @BindView(R.id.tv_hospital)
        TextView tvHospital;
        @BindView(R.id.tv_doctorInfo)
        TextView tvDoctorInfo;
        @BindView(R.id.sly_message_view)
        SwipeLayout slyMessageView;


        public HolderDoctor(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
