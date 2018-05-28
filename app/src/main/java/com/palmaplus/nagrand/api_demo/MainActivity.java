package com.palmaplus.nagrand.api_demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.unit.Init;
import com.baidu.aip.unit.listener.MyListener;
import com.baidu.android.voicedemo.control.MyRecognizer;
import com.baidu.android.voicedemo.recognization.ChainRecogListener;
import com.baidu.android.voicedemo.recognization.CommonRecogParams;
import com.baidu.android.voicedemo.recognization.IRecogListener;
import com.baidu.android.voicedemo.recognization.RecogResult;
import com.baidu.android.voicedemo.recognization.StatusRecogListener;
import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.MessageListener;
import com.baidu.voicerecognition.android.ui.DigitalDialogInput;
import com.palmaplus.nagrand.api_demo.data.FunctionListDataProvider;
import com.palmaplus.nagrand.api_demo.fragments.dynamic.MyDynamicFragment;
import com.palmaplus.nagrand.api_demo.fragments.navi.NaviTextFragment;
import com.palmaplus.nagrand.api_demo.fragments.navi.StartEndNaviFragment;
import com.palmaplus.nagrand.api_demo.model.FunctionItem;
import com.palmaplus.nagrand.api_demo.utils.FragmentUtils;
import com.palmaplus.nagrand.api_demo.utils.JsonUtil;
import com.palmaplus.nagrand.api_demo.widgets.adapter.GroupAdapter;
import com.palmaplus.nagrand.tools.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NotifyInterface {


    protected TextView subTitle;
    private TextView title;
    //    private Button start_dynamic;
    //    private Button start_reconized;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        subTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        title = (TextView) findViewById(R.id.toolbar_title);
//        start_dynamic = (Button) findViewById(R.id.start_dynamic);
//        start_reconized = (Button) findViewById(R.id.start_reconized);
//        start_reconized.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入
//                if (state == 0) {
//                    myRecognizer.start(params);
//                    Log.i("FCS", "--------myRecognizer.start(params);------------------");
//                    state = 1;
//                } else {
//                    myRecognizer.stop();
//                    Log.i("FCS", "--------myRecognizer.stop();------------------");
//                    state = 0;
//                }
//            }
//        });

        try {
            Log.i("FCS", "--------myRecognizer.stop();------------------" + Arrays.toString(JsonUtil.jsonToMap().get("三星体验店")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ListView
        ListView listView = new ListView(this);
        FunctionListDataProvider provider = new FunctionListDataProvider();
        final GroupAdapter<FunctionItem> groupAdapter = new GroupAdapter<FunctionItem>(this, provider.provideFunctionList(), R.layout.group_list_view, R.layout.item_list_view) {
            @Override
            public void bindView(FunctionItem s, View view) {
                ((TextView) view.findViewById(R.id.addexam_list_item_text)).setText(s.getName());
            }

            @Override
            public void bindGroup(FunctionItem s, View view) {
                ((TextView) view.findViewById(R.id.addexam_list_item_text)).setText(s.getName());
            }
        };
        listView.setAdapter(groupAdapter);
        final PopupWindow popupWindow = new PopupWindow(listView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewUtils.dip2px(this, 150));
        popupWindow.setHeight(ViewUtils.dip2px(this, 300));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectFunctionItem(groupAdapter.getItem(position));
                popupWindow.dismiss();
            }
        });

        // add Event Listener
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(subTitle);
            }
        });

//        start_dynamic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (page == 1) {
//                    return;
//                }
//                title.setText("动态导航");
//                page = 1;
//                try {
//                    MyDynamicFragment fragment = new MyDynamicFragment();
//                    fragment.setNotifyInterface(MainActivity.this);
//                    FragmentUtils.replaceFragment(getSupportFragmentManager(),fragment , R.id.fragment_container, false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

        selectFunctionItem(groupAdapter.getItem(1));

        init();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void selectFunctionItem(FunctionItem functionItem) {
        if (functionItem.getClazz() == null) {
            return;
        }
        title.setText("起终点规划");

        try {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), new StartEndNaviFragment(), R.id.fragment_container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


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

    protected CommonRecogParams apiParams;
    private ChainRecogListener listener;
    protected MyRecognizer myRecognizer;
    private int sayState;

    protected void initRecog() {
//        StatusRecogListener listener = new MessageStatusRecogListener(handler);
        listener = new ChainRecogListener();
        listener.addListener(new MyListener(handler));
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = getApiParams();

    }

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

    protected String appId = "11222818";

    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";

    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.ONLINE;

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

    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }


    @Override
    public void stopDynamic() {
        if (page == 1) {
            title.setText("起终点规划");
            page = 0;
            try {
                FragmentUtils.replaceFragment(getSupportFragmentManager(), new StartEndNaviFragment(), R.id.fragment_container, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
    }

    @Override
    public void startDynamic(double[] endPosition) {
        if (page == 1) {
            return;
        }
        title.setText("动态导航");
        page = 1;
        try {
            MyDynamicFragment fragment = new MyDynamicFragment();
            fragment.setEnd(endPosition);
            fragment.setNotifyInterface(MainActivity.this);
            FragmentUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.fragment_container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
