package com.palmaplus.nagrand.api_demo.fragments.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmaplus.nagrand.api_demo.R;

/**
 * Created by jian.feng on 2017/6/2.
 */

public class MapPositionFragment extends BaseMapFragment {

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 设置地图的初始旋转角度
        mapView.initRotate(30);
        // 设置地图的初始显示比例
        mapView.initRatio(0.6f);
        // 设置地图初始最大比例
        mapView.setMaxScale(500);
        // 设置地图初始最小比例
        mapView.setMinScale(3000);

        RelativeLayout controlLayout = (RelativeLayout) view.findViewById(R.id.control_container);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.map_position_message, controlLayout);
        ((TextView) inflate.findViewById(R.id.init_rotate)).setText("30度");
        ((TextView) inflate.findViewById(R.id.init_ratio)).setText("60%");
        ((TextView) inflate.findViewById(R.id.max_scale)).setText("1:500");
        ((TextView) inflate.findViewById(R.id.min_scale)).setText("1:3000");
    }
}
