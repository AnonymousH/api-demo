package com.palmaplus.nagrand.api_demo.hospital;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.unit.Init;
import com.baidu.aip.unit.listener.MyListener;
import com.baidu.android.voicedemo.control.MyRecognizer;
import com.baidu.android.voicedemo.recognization.ChainRecogListener;
import com.baidu.android.voicedemo.recognization.CommonRecogParams;
import com.baidu.android.voicedemo.recognization.RecogResult;
import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.MessageListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.adapter.FloorAdapter;
import com.palmaplus.nagrand.api_demo.hospital.databean.FloorBean;
import com.palmaplus.nagrand.api_demo.utils.Constants;
import com.palmaplus.nagrand.api_demo.utils.DataUtil;
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
import java.util.HashMap;
import java.util.List;

public class HospitalDynamicActivity extends AppCompatActivity {

    private FloorAdapter floorAdapter;
    private RecyclerView floor_recycle;
    private TextView floor_txt;
    //    private Button voice_button;
    private int voiceState = 0;

    private TextView dymicTxt;
    private ImageView dymicImg;
    private TextView dymic_phone;
    private TextView dymic_back;


    private TextView ennd_position,ennd_lenth,ennd_time;
    private LinearLayout start_layout;
    private LinearLayout end_layout;

    private MapView mapView;
    private Map map;
    protected LBSManager lbsManager;
    private int lbsLength;
    protected ImageOverlay startOverlay;
    protected ImageOverlay endOverlay;
    private int state = 0;
    private boolean isVoice;
    private boolean isSpeaking;
    private double[] end;
    private String endVoice;
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
                case 2:
                    break;
                case 3:
                    dymicImg.setImageResource(R.mipmap.dymic2);
                    dymicTxt.setText("点击停止导航");
                    dymic_phone.setVisibility(View.VISIBLE);
                    ennd_lenth.setText("距离："+lbsLength+"米");
                    ennd_time.setText("耗时："+DataUtil.getTime(lbsLength));

                    start_layout.setVisibility(View.GONE);
                    end_layout.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    dymicImg.setImageResource(R.mipmap.dymic1);
                    dymicTxt.setText("点击开始说话");
                    dymic_phone.setVisibility(View.GONE);
                    start_layout.setVisibility(View.VISIBLE);
                    end_layout.setVisibility(View.GONE);
                    break;
            }
        }
    };Handler mainHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

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

    protected java.util.Map<String, String> getParams() {
        java.util.Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        return params;
    }

    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    protected void initialTts() {
//        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        final MessageListener listener = new MessageListener();
        listener.setCallback(new MessageListener.FinishCallback() {
            @Override
            public void callback() {
                Log.e("test", "callback");
//                listen();
            }
        });
        java.util.Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        Init.synthesizer = NonBlockSyntherizer.getInstance(this, initConfig, mainHandle);
    }

    protected String appId = "11222818";

    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";

    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.ONLINE;

    private void init() {
        initPermission();

        initialTts();

        listener = new ChainRecogListener();
        listener.addListener(new MyListener(mainHandle) {
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
                    } else if (voice.contains("四楼")) {
                        a = new double[]{1.3530978711370476E7, 3657365.7866325825};
                        floorID = 305797L;
                    } else if (voice.contains("五楼")) {
                        a = new double[]{1.3530954505519342E7, 3657405.488416152};
                        floorID = 306021L;
                    }

                    if(a !=null){
                        end = a;
                        isVoice = true;
                        endVoice = voice;
                        performclick();
                    }else {
                        syntherizer.speak("未识别到目的地");
                        Message message = handler.obtainMessage(4);
                        handler.sendMessage(message);

                        voiceState = 0;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage) {
                super.onAsrFinishError(errorCode, subErrorCode, errorMessage, descMessage);

                syntherizer.speak("未识别到目的地");
                Message message = handler.obtainMessage(4);
                handler.sendMessage(message);

                voiceState = 0;
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

        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        end_layout = (LinearLayout) findViewById(R.id.end_layout);
        ennd_position = (TextView) findViewById(R.id.ennd_position);
        ennd_lenth = (TextView) findViewById(R.id.ennd_lenth);
        ennd_time = (TextView) findViewById(R.id.ennd_time);

        dymicTxt = (TextView) findViewById(R.id.dymic_txt);
        dymicImg = (ImageView) findViewById(R.id.dymic_img);
        dymicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgClick();
            }
        });
        dymic_phone = (TextView) findViewById(R.id.dymic_phone);
        dymic_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDia();
            }
        });
        dymic_back = (TextView) findViewById(R.id.dymic_back);
        dymic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        floor_txt = (TextView) findViewById(R.id.floor_txt);
        floor_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floor_recycle.getVisibility() == View.GONE) {
                    floor_recycle.setVisibility(View.VISIBLE);
                } else {
                    floor_recycle.setVisibility(View.GONE);
                }
            }
        });
        floor_recycle = (RecyclerView) findViewById(R.id.floor_recycle);
        final List<FloorBean> floorBeans = DataUtil.getFloorData();
        floorAdapter = new FloorAdapter();
        floor_recycle.setLayoutManager(new LinearLayoutManager(this));
        floor_recycle.setAdapter(floorAdapter);
        floorAdapter.setNewData(floorBeans);
        floorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.equals(floor_txt.getText().toString(), floorBeans.get(position).getFloorName())) {
                    return;
                }

                floor_recycle.setVisibility(View.GONE);
                floor_txt.setText(floorBeans.get(position).getFloorName());
                map.switchMapWithID(floorBeans.get(position).getFloorID());
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
//        map.setDefaultWidgetContrainer(relativeLayout);
        // 隐藏指南针
        map.getCompass().setVisibility(View.GONE);
        // 隐藏比例尺
        map.getScale().setVisibility(View.INVISIBLE);
        // 隐藏2D/3D控件
        map.getSwitch().setVisibility(View.GONE);
    }

    private void imgClick() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HospitalDynamicActivity.this);
        java.util.Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入
        if (voiceState == 0) {
            Log.i("FCS", "--------myRecognizer.start(params);------------------");
            voiceState = 1;
            dymicImg.setImageResource(R.mipmap.dymic3);
            dymicTxt.setText("停止说话，开始识别");
            dymic_phone.setVisibility(View.GONE);
            myRecognizer.start(params);
        } else if (voiceState == 1) {
            Log.i("FCS", "--------myRecognizer.stop();------------------");
            voiceState = 0;
            myRecognizer.stop();
        } else if (voiceState == 2) {
            if (isSpeaking) {
                syntherizer.stop();
                isSpeaking = false;
            }

            if (lbsManager != null && lbsManager.getNaviLayer() != null) {
                lbsManager.getNaviLayer().clearFeatures();
            }
            map.switchMapWithID(305092L);
            floor_txt.setText("F1");
            performclick();
            Log.i("FCS", "--------myRecognizer.stop();------------------");
            voiceState = 0;
            dymicTxt.setText("点击开始说话");
            dymic_phone.setVisibility(View.GONE);
            dymicImg.setImageResource(R.mipmap.dymic1);
            start_layout.setVisibility(View.VISIBLE);
            end_layout.setVisibility(View.GONE);
        }
        // 刷新Overlay
        mapView.getOverlayController().refresh();
    }

    private void showDia() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
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
                        if (lbsManager != null && lbsManager.getNaviLayer() != null) {
                            lbsManager.getNaviLayer().clearFeatures();
                        }
//                        // 设置点击的区域为起点
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
                            ennd_position.setText("终点："+endVoice);
                        } else {
                            endOverlay.init(new double[]{point.x, point.y});
                            // 设置点击所在的楼层为终点
                            endOverlay.mFloorId = map.getFloorId();
                            ennd_position.setText("终点：手动选择");
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
                if (state == 0) {
                    return;
                }


                voiceState = 2;

                // 如果导航成功，就重置导航状态
                state = 0;
                // 如果导航成功，则获取所有的分段导航的信息
                lbsLength = 0 ;
                final StepInfo[] allStepInfo = lbsManager.navigateManager().getAllStepInfo();
                List<String> steps = new ArrayList<>();
                for (int i = 0; i < allStepInfo.length; i++) {
                    steps.add(DataUtil.getFloorName(allStepInfo[i].mFloorId) + String.format("直行%.2f米，%s", allStepInfo[i].mLength, allStepInfo[i].mActionName));

                    lbsLength += allStepInfo[i].mLength;
                }

                Message message = handler.obtainMessage(3);
                handler.sendMessage(message);

                if (!isSpeaking) {
                    isSpeaking = true;
                    syntherizer.speak(steps.toString());
                }

                Log.e("FCS", "--------onNavigateComplete------------------" + steps.toString());
            }

            @Override
            public void onNavigateError(NavigateManager.NavigateState navigateState) {
                state = 0;
                Message message = handler.obtainMessage(4);
                handler.sendMessage(message);

                voiceState = 0;
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
