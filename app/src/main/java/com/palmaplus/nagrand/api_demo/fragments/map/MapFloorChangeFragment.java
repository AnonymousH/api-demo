package com.palmaplus.nagrand.api_demo.fragments.map;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.easyapi.Map;

/**
 * Created by jian.feng on 2017/6/2.
 */

public class MapFloorChangeFragment extends BaseMapFragment {
    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        Map map = mapView.getMap();
        // 获取放置Widget的ViewGroup
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.control_container);
        // 将Widgets放入ViewGroup中
        map.setDefaultWidgetContrainer(relativeLayout);
        // 隐藏指南针
        map.getCompass().setVisibility(View.GONE);
        // 隐藏比例尺
        map.getScale().setVisibility(View.INVISIBLE);
        // 隐藏2D/3D控件
        map.getSwitch().setVisibility(View.GONE);
    }
}
