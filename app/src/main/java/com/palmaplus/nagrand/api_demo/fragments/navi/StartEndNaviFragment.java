package com.palmaplus.nagrand.api_demo.fragments.navi;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import com.palmaplus.nagrand.api_demo.MainActivity;
import com.palmaplus.nagrand.api_demo.NotifyInterface;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.api_demo.utils.JsonUtil;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.easyapi.MockPositionManager;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.navigate.StepInfo;
import com.palmaplus.nagrand.tools.ViewUtils;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jian.feng on 2017/6/6.
 */

public class StartEndNaviFragment extends BaseMapFragment {
    protected ImageOverlay startOverlay;
    protected ImageOverlay endOverlay;
    private int state = 0;
    private int voiceState = 0;

    private boolean isVoice;
    private double[] end;

    protected LBSManager lbsManager;

    protected CommonRecogParams apiParams;
    private ChainRecogListener listener;
    protected MyRecognizer myRecognizer;

    NotifyInterface notifyInterface;

    MySyntherizer syntherizer;


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

    public void setNotifyInterface(NotifyInterface notifyInterface) {
        this.notifyInterface = notifyInterface;
    }

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);

        addButton();

        initLisstener();

        init();

        // 获取MapView
        final Map map = mapView.getMap();
        // 构造一个Wi-Fi定位导航管理器
        lbsManager = new LBSManager(map, getContext(),
                "macAddress" // 本机的Mac地址
        );
        // 构造一个蓝牙定位导航管理器
        // lbsManager = new LBSManager(map, getContext(),
        //  66,  地图的MapId
        //  "AppKey" AppKey
        // );


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
                    steps.add(String.format("直行%.2f米，%s", allStepInfo[i].mLength, allStepInfo[i].mActionName) );
                }

                syntherizer.speak( steps.toString() );


            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
                state = 0;
            }
        });

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
//                        lbsManager.getNaviLayer().clearFeatures();
                        // 设置点击的区域为起点
//                        startOverlay.init(new double[] { point.x, point.y });
                        startOverlay.init(new double[]{1.3370133858461842E7, 3542084.446877452});
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
                        if (isVoice) {
                            endOverlay.init(end);
                            isVoice = false;
                        } else {
                            endOverlay.init(new double[]{point.x, point.y});

                        }

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


        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 3000);

    }

    private void addButton() {
        // 获取放置Widget的ViewGroup
        RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);
        // 增加开始定位的按钮
        Button button = new Button(getContext());
        button.setText("开始导航");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(end == null){
                    syntherizer.speak("请先选择目的地");
                    return;
                }
                if(notifyInterface !=null){
                    syntherizer.stop();
                    notifyInterface.startDynamic(end);
                }
            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), 0, ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        controlContainer.addView(button, layoutParams);
    }

    private void init() {
        listener = new ChainRecogListener();
        listener.addListener(new MyListener(handler) {
            @Override
            public void onAsrFinalResult(String[] results, RecogResult recogResult) {
                String voice = results[0];
                try {
                    double[] a = JsonUtil.jsonToMap().get(voice);
                    Log.i("FCS", "--------myRecognizer.stop();------------------" + Arrays.toString(a));
                    if (a == null) {
                        Toast.makeText(getContext(), "没有这个目的地:" + voice, Toast.LENGTH_LONG).show();
                        return;
                    }
                    end = a;
                    isVoice = true;
                    performclick();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myRecognizer = new MyRecognizer(getContext(), listener);
        apiParams = getApiParams();

        syntherizer = Init.synthesizer;
    }


    private void initLisstener() {
        start_reconized.setVisibility(View.VISIBLE);

        start_reconized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

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
        // 关闭定位导航
        lbsManager.close();
        myRecognizer.release();
        syntherizer.release();
        super.onDestroyView();
    }

    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(getActivity());
    }
}
