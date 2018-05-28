package com.palmaplus.nagrand.api_demo.fragments.event;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;

/**
 * Created by jian.feng on 2017/6/6.
 */

public class SingleEventFragment extends BaseMapFragment {

    private long clickId;

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Widget的ViewGroup
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.control_container);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.single_event_view, relativeLayout);
        final TextView screenView = (TextView) inflate.findViewById(R.id.screen_coord);
        final TextView nameView = (TextView) inflate.findViewById(R.id.name);
        final TextView poiView = (TextView) inflate.findViewById(R.id.poi);
        final TextView nameField = (TextView) inflate.findViewById(R.id.name_field);

        // 设置地图点击事件
        map.mapView().setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                // 查找当前点击的屏幕位置对应的Feature
                Feature feature = mapView.selectFeature(v, v1);
                if (feature == null) {
                    return;
                }
                screenView.setText(String.format("{ %s, %s}", v, v1));
                String s = LocationModel.display.get(feature);
                nameField.setVisibility(s == null || s.equals("") ? View.GONE : View.VISIBLE);
                nameView.setText(s);
                poiView.setText(LocationModel.id.get(feature).toString());

                mapView.resetOriginStyle("Area", clickId);
                clickId = LocationModel.id.get(feature);
                mapView.setRenderableColor("Area", clickId, Color.BLUE);
            }
        });

    }
}
