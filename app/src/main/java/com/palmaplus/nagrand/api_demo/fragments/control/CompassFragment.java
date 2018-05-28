package com.palmaplus.nagrand.api_demo.fragments.control;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.easyapi.Map;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class CompassFragment extends BaseMapFragment {

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();
        // 获取放置Widget的ViewGroup
        RelativeLayout viewById = (RelativeLayout) view.findViewById(R.id.control_container);
        // 把控制手势的控件加入到ViewGroup中
        map.setDefaultWidgetContrainer(viewById);
        // 隐藏比例尺
        map.getScale().setVisibility(View.GONE);
        // 隐藏2D/3D
        map.getSwitch().setVisibility(View.GONE);
        // 隐藏楼层切换控件
        map.getFloorLayout().setVisibility(View.GONE);
    }
}
