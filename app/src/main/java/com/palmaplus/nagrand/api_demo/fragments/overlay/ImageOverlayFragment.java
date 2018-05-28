package com.palmaplus.nagrand.api_demo.fragments.overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class ImageOverlayFragment extends BaseMapFragment {
    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) view.findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);

        // 创建一个Bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_map_pin_end);
        // 添加地图的单击事件
        map.mapView().setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                // 将屏幕坐标转换成WGS84墨卡托投影坐标
                Types.Point point = mapView.converToWorldCoordinate(v, v1);
                // 创建ImageOverlay
                ImageOverlay imageOverlay = new ImageOverlay(getContext());
                imageOverlay.setImageBitmap(bitmap);
                // 设置Overlay附属的楼层
                imageOverlay.mFloorId = map.getFloorId();
                // 初始化Overlay的位置
                imageOverlay.init(new double[] { point.x, point.y });
                // 添加Overlay
                map.addOverlay(imageOverlay);
            }
        });
    }
}
