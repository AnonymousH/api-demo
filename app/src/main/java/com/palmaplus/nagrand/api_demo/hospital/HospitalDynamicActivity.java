package com.palmaplus.nagrand.api_demo.hospital;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.aip.unit.Init;
import com.baidu.aip.unit.listener.MyListener;
import com.baidu.android.voicedemo.control.MyRecognizer;
import com.baidu.android.voicedemo.recognization.ChainRecogListener;
import com.baidu.android.voicedemo.recognization.CommonRecogParams;
import com.baidu.android.voicedemo.recognization.RecogResult;
import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
import com.baidu.tts.sample.control.MySyntherizer;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.utils.Constants;
import com.palmaplus.nagrand.api_demo.utils.JsonUtil;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.navigate.StepInfo;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HospitalDynamicActivity extends AppCompatActivity {

    private Button voice_button;
    private int voiceState = 0;

    private MapView mapView;
    private Map map;
    protected LBSManager lbsManager;
    protected ImageOverlay startOverlay;
    protected ImageOverlay endOverlay;
    private int state = 0;
    private boolean isVoice;
    private boolean isSpeaking;
    private double[] end;
    private long floorID;

    protected CommonRecogParams apiParams;
    private ChainRecogListener listener;
    protected MyRecognizer myRecognizer;

    MySyntherizer syntherizer; // 语音播报

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    performclick();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_dynamic);

        initView();

        initConfig();

        initListener();

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 3000);

        init();
    }

    private void init() {
        listener = new ChainRecogListener();
        listener.addListener(new MyListener(handler) {
            @Override
            public void onAsrFinalResult(String[] results, RecogResult recogResult) {
                String voice = results[0];
                try {
                    double[] a = null;
//                    double[] a = JsonUtil.jsonToMap().get(voice);
//                    Log.i("FCS", "--------onAsrFinalResult------------------" + Arrays.toString(a));
                    Log.i("FCS", "--------onAsrFinalResult------------------" + voice);
//                    if (a == null) {
//                        syntherizer.speak("没有这个目的地:" + voice);
////                        Toast.makeText(HospitalDynamicActivity.this, "没有这个目的地:" + voice, Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    if (voice.contains("一楼")) {
                        a = new double[]{1.3530982926961768E7, 3657410.412079565};
                        floorID = 305092L;
                    } else if (voice.contains("二楼")) {
                        a = new double[]{1.353097812613774E7, 3657372.8435779065};
                        floorID = 305342L;
                    } else if (voice.contains("三楼")) {
                        a = new double[]{1.3530969732158262E7, 3657347.6163096186};
                        floorID = 305571L;
                    }

                    end = a;
                    isVoice = true;
                    performclick();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = getApiParams();

        syntherizer = Init.synthesizer;
    }

    private void initConfig() {
        lbsManager = new LBSManager(map, this,
                "macAddress" // 本机的Mac地址
        );

        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);
        // 添加起点的定位图标
        startOverlay = new ImageOverlay(this);
        startOverlay.setBackgroundResource(R.mipmap.ic_map_pin_start);
        startOverlay.init(new double[]{0, 0});
        map.addOverlay(startOverlay);
        // 添加终点的定位图标
        endOverlay = new ImageOverlay(this);
        endOverlay.setBackgroundResource(R.mipmap.ic_map_pin_end);
        endOverlay.init(new double[]{0, 0});
        map.addOverlay(endOverlay);
    }

    private void initView() {

        voice_button = (Button) findViewById(R.id.voice_button);
        voice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HospitalDynamicActivity.this);

                java.util.Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入
                if (voiceState == 0) {
                    myRecognizer.start(params);
                    Log.i("FCS", "--------myRecognizer.start(params);------------------");
                    voiceState = 1;
                } else {
                    myRecognizer.stop();
                    Log.i("FCS", "--------myRecognizer.stop();------------------");
                    voiceState = 0;
                }
            }
        });

        // 获取MapView
        mapView = (MapView) findViewById(R.id.mapView);
        // 通过MapView获取Map对象，并且根据MapID渲染地图
        mapView.getMap().startWithMapID(Constants.mapId);

        map = mapView.getMap();

        // 获取放置Widget的ViewGroup
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.control_container);
        // 将Widgets放入ViewGroup中
        map.setDefaultWidgetContrainer(relativeLayout);
        // 隐藏指南针
        map.getCompass().setVisibility(View.GONE);
        // 隐藏比例尺
        map.getScale().setVisibility(View.INVISIBLE);
        // 隐藏2D/3D控件
        map.getSwitch().setVisibility(View.GONE);
    }

    private void initListener() {
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
//                        startOverlay.init(new double[] { point.x, point.y });
//                        startOverlay.mFloorId = map.getFloorId();
                        Log.e("FCS", "--------onSingleTap------------------  " + point.x + "  --y--  " + point.y + " --floor--  " + map.getFloorId());
                        startOverlay.init(new double[]{1.353094773320948E7, 3657367.3479511663});
                        // 设置点击所在的楼层为终点
                        startOverlay.mFloorId = 305092L;

                        // 重置终点图标
                        endOverlay.mFloorId = 0;
                        endOverlay.setVisibility(View.GONE);
                        state++;

                        break;
                    case 1:
                        // 设置终点图标的可见性
                        endOverlay.setVisibility(View.VISIBLE);
                        // 设置点击区域的终点
                        if (isVoice) {
                            endOverlay.init(end);
                            isVoice = false;
                            // 设置点击所在的楼层为终点
                            endOverlay.mFloorId = floorID;
                        } else {
                            endOverlay.init(new double[]{point.x, point.y});
                            // 设置点击所在的楼层为终点
                            endOverlay.mFloorId = map.getFloorId();
                        }

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


        // 添加导航的事件回调
        lbsManager.addOnNavigationListener(new LBSManager.OnNavigationListener() {
            @Override
            public void onNavigateComplete(NavigateManager.NavigateState navigateState, FeatureCollection featureCollection) {
                // 如果导航成功，就重置导航状态
                state = 0;
                // 如果导航成功，则获取所有的分段导航的信息
                final StepInfo[] allStepInfo = lbsManager.navigateManager().getAllStepInfo();
                List<String> steps = new ArrayList<>();
                for (int i = 0; i < allStepInfo.length; i++) {
                    steps.add(String.format("直行%.2f米，%s", allStepInfo[i].mLength, allStepInfo[i].mActionName));
                }
                if (!isSpeaking) {
                    isSpeaking = true;
                    syntherizer.speak(steps.toString());
                }

                Log.e("FCS", "--------onNavigateComplete------------------" + steps.toString());
            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
                state = 0;
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图
        mapView.drop();

        syntherizer.release();
        myRecognizer.release();
    }

    @Override
    public void onBackPressed() {
        syntherizer.stop();
        super.onBackPressed();
    }

    private void performclick() {
        final int width = getWindowManager().getDefaultDisplay().getWidth();
        final int height = getWindowManager().getDefaultDisplay().getHeight();
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

    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }

}
