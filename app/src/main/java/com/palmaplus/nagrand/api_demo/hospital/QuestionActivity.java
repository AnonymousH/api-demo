package com.palmaplus.nagrand.api_demo.hospital;

import android.Manifest;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.unit.Init;
import com.baidu.aip.unit.TLAPIService;
import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.listener.OnResultListener;
import com.baidu.aip.unit.model.TLResponse;
import com.baidu.android.voicedemo.control.MyRecognizer;
import com.baidu.android.voicedemo.recognization.ChainRecogListener;
import com.baidu.android.voicedemo.recognization.CommonRecogParams;
import com.baidu.android.voicedemo.recognization.IRecogListener;
import com.baidu.android.voicedemo.recognization.RecogResult;
import com.baidu.android.voicedemo.recognization.StatusRecogListener;
import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.MessageListener;
import com.bumptech.glide.Glide;
import com.palmaplus.nagrand.api_demo.R;
import com.ping.chatdemo.adapter.ChatAdapter;
import com.ping.chatdemo.dao.MsgDaoUtil;
import com.ping.chatdemo.entity.Msg;
import com.ping.chatdemo.listener.OnDbUpdateListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private List<Msg> mMsgs;
    private MsgDaoUtil mMsgDaoUtil;
    private ChatAdapter mAdapter;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    RecyclerView mRvChatList;
    ImageView display;
    ImageView logo;
    TextView hint;
    TextView dymic_back;

    ImageView saybutton;

    ImageView returnview;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            addMsg(new Msg(null, "请问有什么问题需要咨询?聊聊天也行啊!", Msg.TYPE_BLE, df.format(new Date())));
//            handler.postDelayed(this, 5000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
//        ButterKnife.bind(this);



        TLAPIService.getInstance().init(this);

        initView();

        mMsgDaoUtil = new MsgDaoUtil(this);
        mMsgs = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvChatList.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter(this, mMsgs);
        mRvChatList.setAdapter(mAdapter);
        mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);

        mMsgDaoUtil.setUpdateListener(new OnDbUpdateListener() {
            @Override
            public void onUpdate(Msg msg) {
                Log.w("textinsert","insert");
                mAdapter.addItem(msg);
//                mAdapter.
                mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mRvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -10) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mEtContent.getWindowToken(), 0);
                }
            }
        });

        //语音引擎初始化
        init();
        handler.postDelayed(runnable, 300);

    }

    private boolean addMsg(Msg msg) {
        boolean flag = mMsgDaoUtil.insertMsg(msg);
        return flag;
    }


    private void initView()
    {
        mRvChatList = (RecyclerView)findViewById(R.id.rv_chatList);

        display = (ImageView)findViewById(R.id.activity_question_img);
        Glide.with(this).load(R.mipmap.questioning).into(display);

        logo = (ImageView)findViewById(R.id.logo);

        hint = (TextView) findViewById(R.id.lab);
        dymic_back = (TextView) findViewById(R.id.dymic_back);
        dymic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saybutton = (ImageView) findViewById(R.id.speak);

        saybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                say_stop_click();
            }
        });
    }


    int click_status = 0;
    int bot_speaking = 0;
    private void say_stop_click(){
        if(click_status == 0){
            if(bot_speaking == 0){
                click_status = 1;
                stopBot();
                startListen();
            }else if(bot_speaking == 1){
                stopBot();
            }
        }else if(click_status == 1){
            click_status = 0;
            stopListen();
        }
    }
    void startListen(){
        saybutton.setImageResource(R.mipmap.dymic3);
        hint.setText("正在说话");
        myRecognizer.start(params);

    }
    void stopListen(){
        saybutton.setImageResource(R.mipmap.dymic1);
        hint.setText("点击开始说话");
        myRecognizer.stop();
    }

    void speak(String text){
        bot_speaking = 1;
        for(;;){
            if(ready == 1){
                syntherizer.speak(text);
                break;
            }
        }
    }
    void stopBot(){
        bot_speaking = 0;
        syntherizer.stop();
    }
    private void answer(String question){
        TLAPIService.getInstance().communicate(new OnResultListener<TLResponse>() {
            @Override
            public void onResult(TLResponse result) {
                String text = result.getText();
                String url = result.getUrl();
                String image = result.getImage();
                addMsg(new Msg(null, text + url + image, Msg.TYPE_BLE, df.format(new Date())));
                speak(text);
            }

            @Override
            public void onError(UnitError error) {
                addMsg(new Msg(null, "系统错误", Msg.TYPE_BLE, df.format(new Date())));
            }
        },0,question,"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRecognizer.release();
        syntherizer.release();
    }

    //语音部分
    private void init(){
        initPermission();
        initRecog();
        initialTts();
        syntherizer = Init.synthesizer;
    }

    //语音识别参数
    protected CommonRecogParams apiParams;
    protected int status;
    protected boolean enableOffline = false;
    protected ChainRecogListener listener;
    protected MyRecognizer myRecognizer;
    protected Map<String,Object> params;



    //识别引擎初始化
    protected void initRecog() {
        listener = new ChainRecogListener();
        listener.addListener(new MyListener(handler));
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = getApiParams();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        params = apiParams.fetch(sp);
    }
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
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


    class MyListener extends StatusRecogListener implements IRecogListener {
        private Handler handler;

        private long speechEndTime;

        private boolean needTime = true;

        public MyListener(Handler handler) {
            this.handler = handler;
        }


        @Override
        public void onAsrReady() {
            super.onAsrReady();
            sendStatusMessage("引擎就绪，可以开始说话。");
        }

        @Override
        public void onAsrBegin() {
            super.onAsrBegin();
            sendStatusMessage("检测到用户说话");

        }

        @Override
        public void onAsrEnd() {
            super.onAsrEnd();
            speechEndTime = System.currentTimeMillis();
            sendMessage("检测到用户说话结束");
        }

        @Override
        public void onAsrPartialResult(String[] results, RecogResult recogResult) {
            sendStatusMessage("临时识别结果，结果是“" + results[0] + "”；原始json：" + recogResult.getOrigalJson());
            super.onAsrPartialResult(results, recogResult);
        }

        @Override
        public void onAsrFinalResult(String[] results, RecogResult recogResult) {
            super.onAsrFinalResult(results, recogResult);
            String message = "识别结束，结果是”" + results[0] + "”";
            sendStatusMessage(message + "“；原始json：" + recogResult.getOrigalJson());
            if (speechEndTime > 0) {
                long diffTime = System.currentTimeMillis() - speechEndTime;
                message += "；说话结束到识别结束耗时【" + diffTime + "ms】";

            }
            speechEndTime = 0;
            sendMessage(message, status, true);
            Log.w("yuyin",message);

            addMsg(new Msg(null, results[0], Msg.TYPE_PHONE, df.format(new Date())));
            stopListen();
            answer(results[0]);

        }

        @Override
        public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage) {
            super.onAsrFinishError(errorCode, subErrorCode, errorMessage, descMessage);
            String message = "识别错误, 错误码：" + errorCode + "," + subErrorCode;
            sendStatusMessage(message + "；错误消息:" + errorMessage + "；描述信息：" + descMessage);
            if (speechEndTime > 0) {
                long diffTime = System.currentTimeMillis() - speechEndTime;
                message += "。说话结束到识别结束耗时【" + diffTime + "ms】";
            }
            speechEndTime = 0;
            sendMessage(message, status, true);
            speechEndTime = 0;
            Log.w("yuyin","in error kaishi luyin");
            if(click_status == 1){
                startListen();
            }else if(click_status == 0){

            }
        }

        @Override
        public void onAsrOnlineNluResult(String nluResult) {
            super.onAsrOnlineNluResult(nluResult);
            if (!nluResult.isEmpty()) {
                sendStatusMessage("原始语义识别结果json：" + nluResult);
            }
        }

        @Override
        public void onAsrFinish(RecogResult recogResult) {
            super.onAsrFinish(recogResult);
            sendStatusMessage("识别一段话结束。如果是长语音的情况会继续识别下段话。");

        }

        /**
         * 长语音识别结束
         */
        @Override
        public void onAsrLongFinish() {
            super.onAsrLongFinish();
            sendStatusMessage("长语音识别结束。");
        }


        /**
         * 使用离线命令词时，有该回调说明离线语法资源加载成功
         */
        @Override
        public void onOfflineLoaded() {
            sendStatusMessage("【重要】离线资源加载成功。没有此回调可能离线语法功能不能使用。");
        }

        /**
         * 使用离线命令词时，有该回调说明离线语法资源加载成功
         */
        @Override
        public void onOfflineUnLoaded() {
            sendStatusMessage(" 离线资源卸载成功。");
        }

        @Override
        public void onAsrExit() {
            super.onAsrExit();
            sendStatusMessage("识别引擎结束并空闲中");
        }

        private void sendMessage(String message) {
            sendMessage(message, WHAT_MESSAGE_STATUS);
        }

        private void sendStatusMessage(String message) {
            sendMessage(message, status);
        }

        private void sendMessage(String message, int what) {
            sendMessage(message, what, false);
        }


        private void sendMessage(String message, int what, boolean highlight) {
            if (needTime && what != STATUS_FINISHED) {
                message += "  ;time=" + System.currentTimeMillis();
            }
            Message msg = Message.obtain();
            msg.what = what;
            msg.arg1 = status;
            if (highlight) {
                msg.arg2 = 1;
            }
            msg.obj = message + "\n";
            handler.sendMessage(msg);
            Log.w("asr",message);
        }
    }


    MySyntherizer syntherizer;
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        final MessageListener listener = new MessageListener();
        listener.setCallback(new MessageListener.FinishCallback() {
            @Override
            public void callback() {
                bot_speaking = 0;
            }

        });
        Map<String, String> params = getParams();



        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        Init.synthesizer = NonBlockSyntherizer.getInstance(this,initConfig,mainHandler);
    }

    int ready = 0;
    Handler mainHandler = new Handler()
    {
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2){
                syntherizer.speak("请问有什么问题需要咨询");
                ready = 1;
            }


        }
    };
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

//        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
//        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
//        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//
//        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
//        OfflineResource offlineResource = createOfflineResource(offlineVoice);
//        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
//        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
//                offlineResource.getModelFilename());
        return params;
    }
    protected String appId = "11222818";

    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";

    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.ONLINE;

}
