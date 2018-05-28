package com.palmaplus.nagrand.api_demo.fragments.dynamic;

import android.os.Bundle;

import com.palmaplus.nagrand.data.DataElement;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.geos.GeometryFactory;
import com.palmaplus.nagrand.geos.Point;
import com.palmaplus.nagrand.navigate.CoordinateInfo;
import com.palmaplus.nagrand.navigate.DynamicNavigateWrapper;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.position.Filter;
import com.palmaplus.nagrand.position.Location;

import java.util.Random;

/**
 * Created by jian.feng on 2017/6/7.
 */

public class DynamicNaviRecalcFragment extends DynamicnaviFragment {

    private volatile boolean once = true;

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);

        // 添加导航的事件回调
        lbsManager.addOnNavigationListener(new LBSManager.OnNavigationListener() {
            @Override
            public void onNavigateComplete(NavigateManager.NavigateState navigateState, FeatureCollection featureCollection) {
                once = true;
//                lbsManager.startUpdatingLocation();
            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
            }
        });

        // 增加动态导航和离开导航一段距离的回调
        lbsManager.addOnDynamicListener(new LBSManager.OnDynamicListener() {
            // 动态导航的回调
            @Override
            public void onDynamicChange(DynamicNavigateWrapper dynamicNavigateWrapper) {
            }

            // 定位点离开导航线一定距离回调
            @Override
            public void onLeaveNaviLine(Location location, float v) {
                if (once) {
//                    lbsManager.stopUpdatingLocation();
                    // 重新设置定位点
                    positionManager.reset();
                    once = false;
                    // 获取定位点的坐标
                    Point point = location.getPoint();
                    // 获取定位点所在的楼层
                    Long aLong = Location.floorId.get(location.getProperties());
                    // 获取终点的坐标
                    double[] geoCoordinate = endOverlay.getGeoCoordinate();
                    // 重新导航
                    lbsManager.navigateFromPoint(point.getCoordinate(), aLong, new Coordinate(geoCoordinate[0], geoCoordinate[1]), endOverlay.getFloorId(), aLong);
                }
            }
        });
        // 设置定位点离导航线5米外就触发离开导航线的回调
        lbsManager.setCorrectionRadius(5);
        // 可以忽略，用于设置在定位过程中插入一个远离导航线一定距离的定位点
        positionManager.setFilter(new Filter() {
            @Override
            public boolean filter(Location location, Location location1) {
                CoordinateInfo[] coordinateInfos = positionManager.getCoordinateInfos();
                int coordinateInfoIndex = positionManager.getCoordinateInfoIndex();
                if (coordinateInfos != null && ++coordinateInfoIndex == 4 && coordinateInfoIndex < coordinateInfos.length) {
                    coordinateInfos[coordinateInfoIndex].x += 15 + new Random().nextInt(15);
                    coordinateInfos[coordinateInfoIndex].y += 15 + new Random().nextInt(15);
                }
                return false;
            }
        });
    }
}
