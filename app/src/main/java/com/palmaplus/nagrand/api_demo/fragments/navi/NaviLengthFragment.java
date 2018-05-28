package com.palmaplus.nagrand.api_demo.fragments.navi;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.navigate.NavigateManager;

/**
 * Created by jian.feng on 2017/6/6.
 */

public class NaviLengthFragment extends StartEndNaviFragment {
    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        Map map = mapView.getMap();

        // 获取放置Widget的ViewGroup
        final RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);
        map.setDefaultWidgetContrainer(controlContainer);
        // 隐藏楼层控制器
        map.getFloorLayout().setVisibility(View.GONE);
        // 隐藏指南针
        map.getCompass().setVisibility(View.GONE);
        // 隐藏2D/3D切换器
        map.getSwitch().setVisibility(View.GONE);

        // 添加显示导航线长度的TextView
        final TextView lengthView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        controlContainer.addView(lengthView, layoutParams);

        // 添加导航的事件回调
        lbsManager.addOnNavigationListener(new LBSManager.OnNavigationListener() {
            @Override
            public void onNavigateComplete(NavigateManager.NavigateState navigateState, FeatureCollection featureCollection) {
                lengthView.post(new Runnable() {
                    @Override
                    public void run() {
                        lengthView.setText(String.format("导航线总长度: %.2f", lbsManager.navigateManager().getTotalLineLength()));
                    }
                });
            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
            }
        });
    }
}
