package com.palmaplus.nagrand.api_demo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.palmaplus.nagrand.api_demo.utils.Constants;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

public class MapActivity extends AppCompatActivity {

    protected LBSManager lbsManager;

    private MapView mapView;
    private Button startButton, resetButton;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mapView.getOverlayController().refresh();
                    break;
            }
        }
    };

    //我的位置 星巴克
    double[] myposition = {1.3370133858461842E7, 3542084.446877452};
    protected ImageOverlay startOverlay;
    protected ImageOverlay endOverlay;

    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();

        initConfig();
    }

    private void initConfig() {
        Map map = mapView.getMap();

        lbsManager = new LBSManager(map, this,
                "macAddress" // 本机的Mac地址
        );

        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);

        startOverlay = new ImageOverlay(this);
        startOverlay.setBackgroundResource(R.mipmap.ic_map_pin_start);
//        startOverlay.init(new double[]{0, 0});
        startOverlay.init(myposition);
        map.addOverlay(startOverlay);


        // 添加终点的定位图标
        endOverlay = new ImageOverlay(this);
        endOverlay.setBackgroundResource(R.mipmap.ic_map_pin_end);
        endOverlay.init(new double[]{0, 0});
        map.addOverlay(endOverlay);


        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 3000);

    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        // 通过MapView获取Map对象，并且根据MapID渲染地图
        mapView.getMap().startWithMapID(Constants.mapId);
        startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this , DynamicActivity.class);
                startActivity(intent);
                mapView.stop();

//                try {
//                    SurfaceHolder surfaceHolder = mapView.getHolder();
//                    if(surfaceHolder != null){
//                        Canvas canvas = surfaceHolder.lockCanvas();
//                        surfaceHolder.unlockCanvasAndPost(canvas);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        resetButton = (Button) findViewById(R.id.button_reset);

        final Map map = mapView.getMap();
        // 设置地图点击事件
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                // 将屏幕坐标转换成WGS84墨卡托投影坐标
                Types.Point point = mapView.converToWorldCoordinate(v, v1);
                // 判断状态，如果已经导航成功，则重新允许选择起点终点
                switch (state) {
                    case 0:
                        // 清除地图上的导航线
                        lbsManager.getNaviLayer().clearFeatures();
                        // 设置点击的区域为起点
                        startOverlay.init(new double[] { point.x, point.y });
//                        startOverlay.init(new double[] { 1.3370133858461842E7, 3542084.446877452 });
                        // 设置点击所在的楼层为终点
                        startOverlay.mFloorId = map.getFloorId();
                        // 重置终点图标
                        endOverlay.mFloorId = 0;
                        endOverlay.setVisibility(View.GONE);
                        state++;


//                        lbsManager.locationOverlay().setVisibility(View.GONE);

                        break;
                    case 1:
//                        lbsManager.locationOverlay().setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_myposition));
//                        lbsManager.locationOverlay().setVisibility(View.VISIBLE);
                        // 设置终点图标的可见性
                        endOverlay.setVisibility(View.VISIBLE);
                        // 设置点击区域的终点
                        endOverlay.init(new double[] { point.x, point.y });
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
                        state++;
                        break;
                }
                // 刷新Overlay
                mapView.getOverlayController().refresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图
        mapView.drop();
    }
}
