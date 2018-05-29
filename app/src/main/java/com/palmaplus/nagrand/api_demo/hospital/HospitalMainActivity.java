package com.palmaplus.nagrand.api_demo.hospital;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.aip.unit.Init;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.MessageListener;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.utils.DateUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HospitalMainActivity extends AutoLayoutActivity implements View.OnClickListener {

    LinearLayout questinLayout;
    LinearLayout dynamicLayout;
        LinearLayout register_layout;
    LinearLayout appointLayout;

    private TextView dateText;


    protected String appId = "11222818";

    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";

    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.ONLINE;

    private int sayState;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sayState = 1;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_main);

        initView();

//        init();
    }

    private void init() {
        initPermission();
//        initRecog();
        initialTts();
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
        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        Init.synthesizer = NonBlockSyntherizer.getInstance(this, initConfig, handler);
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
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

    private void initView() {
        dateText = (TextView) findViewById(R.id.date_text);
        setDate();

        questinLayout = (LinearLayout) findViewById(R.id.questin_layout);
        questinLayout.setOnClickListener(this);
        dynamicLayout = (LinearLayout) findViewById(R.id.dynamic_layout);
        dynamicLayout.setOnClickListener(this);
        register_layout = (LinearLayout) findViewById(R.id.register_layout);
        register_layout.setOnClickListener(this);
        appointLayout = (LinearLayout) findViewById(R.id.appoint_layout);
        appointLayout.setOnClickListener(this);

    }

    private void setDate() {
        //new日期对象
        Date date = new Date(System.currentTimeMillis());
        dateText.setText(DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY年MM月DD日) + " " + DateUtil.getDayWeekOfDate1(date));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.questin_layout:
                Intent chatIntent = new Intent(this, QuestionActivity.class);
                startActivity(chatIntent);
                break;
            case R.id.dynamic_layout:
                Intent intent = new Intent(this, HospitalDynamicActivity.class);
                startActivity(intent);
                break;
            case R.id.register_layout:
                Intent register = new Intent(this, DepartmentActivity.class);
                register.putExtra("type",1);
                startActivity(register);
                break;
            case R.id.appoint_layout:
                Intent appoint = new Intent(this, DepartmentActivity.class);
                appoint.putExtra("type",2);
                startActivity(appoint);
                break;
            default:
                break;
        }
    }
}
