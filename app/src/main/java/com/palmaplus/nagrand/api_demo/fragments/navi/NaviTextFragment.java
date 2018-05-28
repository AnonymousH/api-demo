package com.palmaplus.nagrand.api_demo.fragments.navi;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.easyapi.LBSManager;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.easyapi.MockPositionManager;
import com.palmaplus.nagrand.navigate.DynamicNavigateWrapper;
import com.palmaplus.nagrand.navigate.DynamicNavigationMode;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.navigate.StepInfo;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.tools.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jian.feng on 2017/6/6.
 */

public class NaviTextFragment extends StartEndNaviFragment {

    protected MockPositionManager positionManager;

    private int countLimit ;
//    Timer timer = new Timer();


    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);

        inttView();

        initConfig();

        initListener();


    }

    private void initListener() {
        // 添加导航的事件回调
        lbsManager.addOnNavigationListener(new LBSManager.OnNavigationListener() {
            @Override
            public void onNavigateComplete(NavigateManager.NavigateState navigateState, FeatureCollection featureCollection) {

                // 如果导航成功，则获取所有的分段导航的信息
//                final StepInfo[] allStepInfo = lbsManager.navigateManager().getAllStepInfo();
//                List<String> steps = new ArrayList<>();
//                for (int i = 0; i < allStepInfo.length; i++) {
//                    steps.add(String.format("直行%.2f米，%s", allStepInfo[i].mLength, allStepInfo[i].mActionName) );
//                }
//                Log.e("FCS","-------endOverlay------ "+endOverlay.getGeoCoordinate()[0] +"  --------Y ----  "+endOverlay.getGeoCoordinate()[1] );

            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
            }
        });

        lbsManager.addOnDynamicListener(new LBSManager.OnDynamicListener() {
            @Override
            public void onDynamicChange(DynamicNavigateWrapper dynamicNavigateWrapper) {
//                Log.i("FCS","-------onDynamicChange------ "+dynamicNavigateWrapper.mDynamicNavigateOutput.mClipCoordinate.getX()+
//                " --------------Y--------  "+dynamicNavigateWrapper.mDynamicNavigateOutput.mClipCoordinate.getY());

                countLimit++;
                Log.i("FCS", "-----onDynamicChange--countLimit------ "+countLimit);
            }

            @Override
            public void onLeaveNaviLine(Location location, float v) {
                Log.e("FCS", "-------onLeaveNaviLine------ ");
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

    private void inttView() {
        RelativeLayout controlContainer = (RelativeLayout) view.findViewById(R.id.control_container);
        // 增加开始定位的按钮
        Button button = new Button(getContext());
        button.setText("重新定位");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDynamic();

            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), 0, ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        controlContainer.addView(button, layoutParams);

        // 朝北模式的按钮
        button = new Button(getContext());
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10), 0, 0);
        button.setText("开始导航");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FCS", "-----lbsManager.locationOverlay()------ "+lbsManager.locationOverlay().getVisibility()+"");
//                initConfig();
                // 开始动态导航
                lbsManager.startDynamic();
                // 开始跟新定位点
                lbsManager.startUpdatingLocation();
                lbsManager.locationOverlay().setVisibility(View.VISIBLE);
//
//                countLimit = 2;
//                Message message = handler.obtainMessage(1);     // Message
//                handler.sendMessageDelayed(message, 1000);

            }
        });
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        controlContainer.addView(button, layoutParams);
    }

//    TimerTask task = new TimerTask() {
//
//        @Override
//        public void run() {
//            Log.e("FCS", "-----TimerTask--countLimit------ "+countLimit);
//            countLimit--;
//            if (countLimit < 0) {
//                timer.cancel();
//                Log.e("FCS", "-----TimerTask--导航结束------ ");
//
//                stopDynamic();
//            }
//        }
//    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    countLimit--;

                    Log.e("FCS", "-----handleMessage--countLimit------ "+countLimit);
                    if(countLimit < 0){
                        Log.e("FCS", "-----TimerTask--导航结束------ ");
                        stopDynamic();
                    }else {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }
            }
        }
    };

//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//            countLimit--;
//            Log.e("FCS", "-----TimerTask--countLimit------ "+countLimit);
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessage(message);
//        }
//    };


    private void stopDynamic() {
        // 清除地图上的导航线
//        lbsManager.getNaviLayer().clearFeatures();
////        // 重置终点图标
//        endOverlay.mFloorId = 0;
//
//        startOverlay.setVisibility(View.GONE);
//        endOverlay.setVisibility(View.GONE);

        lbsManager.stopDynamic();
        lbsManager.stopUpdatingLocation();
//        lbsManager.clearNavigate();
//        positionManager.reset();
//        lbsManager.locationOverlay().setVisibility(View.GONE);


    }

}
