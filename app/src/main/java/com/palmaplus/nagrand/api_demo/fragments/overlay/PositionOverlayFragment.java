package com.palmaplus.nagrand.api_demo.fragments.overlay;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.tools.ViewUtils;
import com.palmaplus.nagrand.view.overlay.LocationOverlay;

import java.util.List;
import java.util.Random;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class PositionOverlayFragment extends BaseMapFragment implements SensorEventListener {

    private LocationOverlay locationOverlay;

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Overlay的ViewGroup
        final ViewGroup container = (ViewGroup) view.findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);
        // 创建一个定位的Overlay
        locationOverlay = new LocationOverlay(getContext(), map.mapView(), 1000);
        // 初始化Overlay的位置
        locationOverlay.init(new double[] { 0, 0 });
        locationOverlay.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_myposition));
        // 添加Overlay
        map.addOverlay(locationOverlay);

        // 获取放置Widget的ViewGroup
        RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);
        // 添加切换定位的按钮
        Button positionButton = new Button(getContext());
        positionButton.setBackgroundResource(R.drawable.btn_map_position);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewUtils.dip2px(getContext(), 40), ViewUtils.dip2px(getContext(), 40));
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), 0, 0, ViewUtils.dip2px(getContext(), 20));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        controlContainer.addView(positionButton, layoutParams);
        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 根据category在Facility查找符合条件的Feature
                List<Feature> features = map.mapView().searchFeature("Facility", "category", new Value(24091000l));
                if (features.size() > 0) {
                    // 随机选择一个公共设施作为模拟定位点的位置参考
                    Feature feature = features.get(new Random().nextInt(features.size()));
                    // 获取公共设施的点坐标
                    Coordinate centroid = feature.getCentroid();
                    // 将屏幕坐标转换成WGS84墨卡托投影坐标
                    Types.Point point = map.mapView().converToScreenCoordinate(centroid.getX(), centroid.getY());
                    // 重新设置定位点的坐标
                    locationOverlay.init(point.x, point.y);
                    // 设置Overlay附属的楼层
                    locationOverlay.setFloorId(map.getFloorId());
                    // 移动地图到指指定位置
                    map.mapView().moveToPoint(centroid);
                    // 需要重新刷新Overlay
                    map.mapView().getOverlayController().refresh();
                }
            }
        });

        SensorManager sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this,
            sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float value = event.values[0];
            locationOverlay.setRotation(value - (float) mapView.getRotate());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SensorManager sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
    }
}
