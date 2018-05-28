package com.palmaplus.nagrand.api_demo.fragments.dynamic;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.NotifyInterface;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.base.BaseFragment;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.easyapi.MockPositionManager;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.navigate.DynamicNavigateWrapper;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.tools.ViewUtils;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

import java.io.IOException;

/**
 * Created by Administrator on 2018-5-27.
 */

public class MyDynamicFragment extends BaseMapFragment {

    protected ImageOverlay startOverlay;
    protected ImageOverlay endOverlay;
    protected Map map;

    private double[] end;

    NotifyInterface notifyInterface;

    protected LBSManager lbsManager;
    protected MockPositionManager positionManager;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:

                    performclick();
                    break;
            }
        }
    };

    public void setNotifyInterface(NotifyInterface notifyInterface) {
        this.notifyInterface = notifyInterface;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);

        start_reconized.setVisibility(View.GONE);

        map = mapView.getMap();
        lbsManager = new LBSManager(map, getContext(),
                "macAddress" // 本机的Mac地址
        );

        addButton();

        initConfig();

        initOverLayout();

        initListener();

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 3000);
    }

    private void addButton() {
        // 获取放置Widget的ViewGroup
        RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);
        // 增加开始定位的按钮
        Button button = new Button(getContext());
        button.setText("停止导航");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbsManager.stopDynamic();
                lbsManager.stopUpdatingLocation();

                if(notifyInterface !=null){
                    notifyInterface.stopDynamic();
                }
            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), 0, ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        controlContainer.addView(button, layoutParams);

    }

    private void initOverLayout() {
        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) view.findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);
        // 添加起点的定位图标
        startOverlay = new ImageOverlay(this.getContext());
        startOverlay.setBackgroundResource(R.mipmap.ic_map_pin_start);
        startOverlay.init(new double[]{0, 0});
        map.addOverlay(startOverlay);
        // 添加终点的定位图标
        endOverlay = new ImageOverlay(this.getContext());
        endOverlay.setBackgroundResource(R.mipmap.ic_map_pin_end);
        endOverlay.init(new double[]{0, 0});
        map.addOverlay(endOverlay);
    }

    private void initListener() {
        lbsManager.addOnDynamicListener(new LBSManager.OnDynamicListener() {
            @Override
            public void onDynamicChange(DynamicNavigateWrapper dynamicNavigateWrapper) {
//                Log.i("FCS","-------onDynamicChange------ "+dynamicNavigateWrapper.mDynamicNavigateOutput.mClipCoordinate.getX()+
//                " --------------Y--------  "+dynamicNavigateWrapper.mDynamicNavigateOutput.mClipCoordinate.getY());

//                countLimit++;
//                Log.i("FCS", "-----onDynamicChange--countLimit------ "+countLimit);
            }

            @Override
            public void onLeaveNaviLine(Location location, float v) {
                Log.e("FCS", "-------onLeaveNaviLine------ ");
            }
        });


        // 设置地图点击事件
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {

                startOverlay.init(new double[]{1.3370133858461842E7, 3542084.446877452});
                // 设置点击所在的楼层为终点
                startOverlay.mFloorId = map.getFloorId();

                // 设置点击区域的终点
//                endOverlay.init(new double[]{1.3370423303766713E7, 3542046.7195758545});
                endOverlay.init(end);
                // 设置点击所在的楼层为终点
                endOverlay.mFloorId = map.getFloorId();
                // 开始导航
                lbsManager.navigateFromPoint(
                        new Coordinate(startOverlay.getGeoCoordinate()[0], startOverlay.getGeoCoordinate()[1]),
                        startOverlay.mFloorId,
                        new Coordinate(endOverlay.getGeoCoordinate()[0], endOverlay.getGeoCoordinate()[1]),
                        endOverlay.mFloorId,
                        endOverlay.mFloorId
                );


                mapView.getOverlayController().refresh();
            }
        });

        // 添加导航的事件回调
        lbsManager.addOnNavigationListener(new LBSManager.OnNavigationListener() {
            @Override
            public void onNavigateComplete(NavigateManager.NavigateState navigateState, FeatureCollection featureCollection) {
                // 如果导航成功，就重置导航状态
                // 开始动态导航
                lbsManager.startDynamic();
                // 开始跟新定位点
                lbsManager.startUpdatingLocation();
            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {

            }
        });


    }

    private void initConfig() {
        // 创建一个根据导航线模拟导航的管理器
        positionManager = new MockPositionManager();
        // 设置给导航定位管理器
        lbsManager.switchPostionType(positionManager);
        // 设置定位点的图标
        lbsManager.locationOverlay().setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_myposition));
        // 初始化一些必要的参数
        positionManager.attchLBSManager(lbsManager);
    }

    private void performclick() {
        final int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        final int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
//生成点击坐标
        int x = (int) (Math.random() * width * 0.6 + width * 0.2);
        int y = (int) (Math.random() * height * 0.6 + height * 0.2);
        //利用ProcessBuilder执行shell命令
        String[] order = {
                "input",
                "tap",
                "" + x,
                "" + y
        };

        try {
            new ProcessBuilder(order).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        // 关闭定位导航
//        lbsManager.close();
    }
}
