package com.palmaplus.nagrand.api_demo.fragments.overlay;

import android.os.Bundle;
import android.view.ViewGroup;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.TextOverlay;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class TextOverlayFragment extends BaseMapFragment {
    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) view.findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);
        // 设置地图点击事件
        map.mapView().setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                // 将屏幕坐标转换成WGS84墨卡托投影坐标
                Types.Point point = mapView.converToWorldCoordinate(v, v1);
                // 创建一个文字标签的Overlay
                TextOverlay textOverlay = new TextOverlay(getContext());
                textOverlay.setText("Palmap+");
                // 设置Overlay附属的楼层
                textOverlay.mFloorId = map.getFloorId();
                // 初始化Overlay的位置
                textOverlay.init(new double[] { point.x, point.y });
                // 添加Overlay
                map.addOverlay(textOverlay);
            }
        });
    }
}
