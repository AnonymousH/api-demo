package com.palmaplus.nagrand.api_demo.fragments.control;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.view.MapView;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class FloorControlFragment extends BaseMapFragment {
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
        map.getScale().setVisibility(View.INVISIBLE);
        // 隐藏2D/3D
        map.getSwitch().setVisibility(View.GONE);
        // 隐藏指南针
        map.getCompass().setVisibility(View.GONE);

        // 添加楼层切换的事件
        map.addOnChangePlanarGraph(new MapView.OnChangePlanarGraph() {
            @Override
            public void onChangePlanarGraph(PlanarGraph planarGraph, PlanarGraph planarGraph1, long l, long l1) {
                // 处理事件
                Toast.makeText(getContext(), String.format("from %s -- to %s", l, l1), Toast.LENGTH_LONG).show();
            }
        });
    }
}
