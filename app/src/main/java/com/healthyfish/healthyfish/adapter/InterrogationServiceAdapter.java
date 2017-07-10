package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：问诊服务页面的适配器
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class InterrogationServiceAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private final int TYPE_PictureConsulting = 0;
    private final int TYPE_PrivateDoctor = 1;

    public InterrogationServiceAdapter(Context mContext, List<Map<String, Object>> list) {
        super();
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() > 0) {
            String type = list.get(position).get("type").toString();
            if (type.equals("pictureConsulting"))
                return TYPE_PictureConsulting;
            else
                return TYPE_PrivateDoctor;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderGraphicConsultation holderGraphicConsultation = null;
        ViewHolderPrivateDoctor holderPrivateDoctor = null;
        if (list.size() > 0) {
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case TYPE_PictureConsulting:
                        convertView = inflater.inflate(R.layout.item_graphic_consultation, null);
                        holderGraphicConsultation = new ViewHolderGraphicConsultation(convertView);
                        convertView.setTag(holderGraphicConsultation);
                        break;
                    case TYPE_PrivateDoctor:
                        convertView = inflater.inflate(R.layout.item_private_doctor, null);
                        holderPrivateDoctor = new ViewHolderPrivateDoctor(convertView);
                        convertView.setTag(holderPrivateDoctor);
                        break;
                    default:
                        break;
                }
            } else {
                switch (type) {
                    case TYPE_PictureConsulting:
                        holderGraphicConsultation = (ViewHolderGraphicConsultation) convertView.getTag();
                        break;
                    case TYPE_PrivateDoctor:
                        holderPrivateDoctor = (ViewHolderPrivateDoctor) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }
            switch (type) {
                case TYPE_PictureConsulting:
                    String flagPictureConsulting[] = new String[]{"title", "time", "department", "name", "reply"};
                    holderGraphicConsultation.tvLastMessage.setText((String) list.get(position).get(flagPictureConsulting[0]));
                    holderGraphicConsultation.tvDoctorDepartment.setText(list.get(position).get(flagPictureConsulting[1])+"  |  "+
                            list.get(position).get(flagPictureConsulting[2])+"  |  "+list.get(position).get(flagPictureConsulting[3]));
                    holderGraphicConsultation.tvReply.setText((String) list.get(position).get(flagPictureConsulting[4]));
                    break;
                case TYPE_PrivateDoctor:
                    String flagPrivateDoctor[] = new String[]{"image", "name","department", "duties", "hospital", "finishTime"};
                    Glide.with(mContext).load(list.get(position).get(flagPrivateDoctor[0])).into(holderPrivateDoctor.civDoctor);
                    holderPrivateDoctor.tvDoctorName.setText((String) list.get(position).get(flagPrivateDoctor[1]));
                    holderPrivateDoctor.tvDoctorDepartment.setText(list.get(position).get(flagPrivateDoctor[2])+"  "+list.get(position).get(flagPrivateDoctor[3]));
                    holderPrivateDoctor.tvHospital.setText((String) list.get(position).get(flagPrivateDoctor[4]));
                    holderPrivateDoctor.tvServiceTime.setText((String) list.get(position).get(flagPrivateDoctor[5]));
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }


    static class ViewHolderGraphicConsultation {
        @BindView(R.id.tv_last_message)
        TextView tvLastMessage;
        @BindView(R.id.tv_doctorDepartment)
        TextView tvDoctorDepartment;
        @BindView(R.id.tv_reply)
        TextView tvReply;

        ViewHolderGraphicConsultation(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderPrivateDoctor {
        @BindView(R.id.civ_doctor)
        CircleImageView civDoctor;
        @BindView(R.id.tv_doctorName)
        TextView tvDoctorName;
        @BindView(R.id.tv_doctorDepartment)
        TextView tvDoctorDepartment;
        @BindView(R.id.tv_hospital)
        TextView tvHospital;
        @BindView(R.id.tv_service_time)
        TextView tvServiceTime;

        ViewHolderPrivateDoctor(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
