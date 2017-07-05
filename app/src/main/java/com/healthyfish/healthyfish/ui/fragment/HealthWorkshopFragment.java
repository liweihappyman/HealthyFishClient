package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthyfish.healthyfish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthWorkshopFragment extends Fragment {
    private Context mContext;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_health_workshop, container,false);

        return rootView;
    }

}
