package com.palmaplus.nagrand.api_demo.fragments.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.tools.ViewUtils;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class ZoomControlFragment extends BaseMapFragment {
    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Widget的ViewGroup
        RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);

        // 添加放大缩小控件
        LinearLayout zoomLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.zoom_widget, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 20));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        controlContainer.addView(zoomLayout, layoutParams);

        zoomLayout.findViewById(R.id.zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 放大
                map.mapView().zoomIn();
            }
        });
        zoomLayout.findViewById(R.id.zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 缩小
                map.mapView().zoomOut();
            }
        });
    }
}
