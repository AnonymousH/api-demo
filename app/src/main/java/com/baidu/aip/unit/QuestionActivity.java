///*
// * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
// */
//package com.baidu.aip.unit;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Handler;
//import android.os.Message;
//import android.preference.PreferenceManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.baidu.android.voicedemo.control.MyRecognizer;
//import com.baidu.android.voicedemo.recognization.ChainRecogListener;
//import com.baidu.android.voicedemo.recognization.CommonRecogParams;
//import com.baidu.android.voicedemo.recognization.IRecogListener;
//import com.baidu.android.voicedemo.recognization.MessageStatusRecogListener;
//import com.baidu.android.voicedemo.recognization.RecogResult;
//import com.baidu.android.voicedemo.recognization.StatusRecogListener;
//import com.baidu.android.voicedemo.recognization.offline.OfflineRecogParams;
//import com.baidu.android.voicedemo.recognization.online.InFileStream;
//import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
//import com.baidu.speech.asr.SpeechConstant;
//import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
//import com.baidu.tts.client.SpeechSynthesizer;
//import com.baidu.tts.client.SpeechSynthesizerListener;
//import com.baidu.tts.client.TtsMode;
//import com.baidu.tts.sample.control.InitConfig;
//import com.baidu.tts.sample.control.MySyntherizer;
//import com.baidu.tts.sample.control.NonBlockSyntherizer;
//import com.baidu.tts.sample.listener.MessageListener;
//import com.baidu.voicerecognition.android.ui.DigitalDialogInput;
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.baidu.android.voicedemo.recognization.IStatus.STATUS_NONE;
//
//public class QuestionActivity extends AppCompatActivity {
//
//    private ImageView image;
//    private boolean ok;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        InFileStream.setContext(this);
//        setContentView(R.layout.activity_question);
//        image = findViewById(R.id.activity_question_img);
//        init();
////        start();
//
////        image.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (ok) {
////                    start();
////                } else {
////                    stop();
////                }
////                ok = !ok;
////            }
////        });
//
////        image.setOnLongClickListener(new View.OnLongClickListener() {
////            @Override
////            public boolean onLongClick(View v) {
////                goAnswer("这是问题");
////                return false;
////            }
////        });
//    }
//
//
//
//    MySyntherizer syntherizer;
//
//    private void init(){
//        initPermission();
//        initRecog();
//        initialTts();
//        syntherizer = Init.synthesizer;
//    }
//    protected void initialTts() {
//        LoggerProxy.printable(true); // 日志打印在logcat中
//        // 设置初始化参数
//        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
//        final MessageListener listener = new MessageListener();
//        listener.setCallback(new MessageListener.FinishCallback() {
//            @Override
//            public void callback() {
//                Log.e("test","callback");
//                listen();
//            }
//        });
//        Map<String, String> params = getParams();
//
//
//        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
//        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
//
//        Init.synthesizer = NonBlockSyntherizer.getInstance(this,initConfig,mainHandler);
//    }
//
//    private Handler mainHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0){
//                if(i == 0 ) syntherizer.speak("还有什么需要帮助的");
//                Log.w("speak", ""+(++i));
//            }
//
//
//        }
//    };
//
//    int i = 0;
//    protected String appId = "11222818";
//
//    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";
//
//    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";
//
//    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
//    protected TtsMode ttsMode = TtsMode.ONLINE;
//
//    protected Map<String, String> getParams() {
//        Map<String, String> params = new HashMap<String, String>();
//        // 以下参数均为选填
//        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
//        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
//        // 设置合成的音量，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
//        // 设置合成的语速，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
//        // 设置合成的语调，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
//
////        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
////        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
////        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
////        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
////        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
////        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
////
////        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
////        OfflineResource offlineResource = createOfflineResource(offlineVoice);
////        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
////        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
////        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
////                offlineResource.getModelFilename());
//        return params;
//    }
//
//    private void initPermission() {
//        String[] permissions = {
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.ACCESS_NETWORK_STATE,
//                Manifest.permission.INTERNET,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//
//        ArrayList<String> toApplyList = new ArrayList<String>();
//
//        for (String perm : permissions) {
//            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
//                toApplyList.add(perm);
//                // 进入到这里代表没有权限.
//
//            }
//        }
//        String[] tmpList = new String[toApplyList.size()];
//        if (!toApplyList.isEmpty()) {
//            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
//        }
//
//    }
//
//    protected Handler handler = new Handler();
//    protected MyRecognizer myRecognizer;
//
//    /*
//     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
//     */
//    protected CommonRecogParams apiParams;
//    protected int status;
//    protected boolean enableOffline = false;
//    private ChainRecogListener listener;
//    protected void initRecog() {
////        StatusRecogListener listener = new MessageStatusRecogListener(handler);
//        listener = new ChainRecogListener();
//        listener.addListener(new MyListener(handler));
//        myRecognizer = new MyRecognizer(this, listener);
//        apiParams = getApiParams();
////        status = STATUS_NONE;
////        if (enableOffline) {
////            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
////        }
//    }
//    protected CommonRecogParams getApiParams() {
//        return new OnlineRecogParams(this);
//    }
//    //正在识别
//    boolean mIsRunning = false;
//    /**
//     * 开始讲话
//     */
//    private void start(){
//        Glide.with(this).load(R.drawable.questioning).into(image);
//
//    }
//    DigitalDialogInput input;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        if(syntherizer == null)
////            syntherizer = Init.synthesizer;
////        syntherizer.speak("请问还需要什么");
//        stop();
//    }
//
//    private void listen(){
//        mIsRunning = true;
////        onRecognitionStart();
//
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入
//
//        // BaiduASRDigitalDialog的输入参数
//        input = new DigitalDialogInput(myRecognizer, listener, params);
//
//        Map<String, Object> params0 = input.getStartParams();
//        params0.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
//        myRecognizer.start(input.getStartParams());
//    }
//    /**
//     * 结束讲话
//     */
//    private void stop() {
//        Glide.with(this).load(R.drawable.questioning).into(image);
//
////        listen();
//    }
//
//    /**
//     * 跳转到回复页面
//     * @param question 讲话内容，下个界面获得回复内容并播放
//     */
//    private void goAnswer(String question) {
//        Intent intent = new Intent(this,AnswerActivity.class);
//        intent.putExtra("question",question);
//        startActivity(intent);
//        finish();
//    }
//
//    class MyListener extends StatusRecogListener implements IRecogListener{
//        private Handler handler;
//
//        private long speechEndTime;
//
//        private boolean needTime = true;
//
//        public MyListener(Handler handler) {
//            this.handler = handler;
//        }
//
//
//        @Override
//        public void onAsrReady() {
//            super.onAsrReady();
//            sendStatusMessage("引擎就绪，可以开始说话。");
//        }
//
//        @Override
//        public void onAsrBegin() {
//            super.onAsrBegin();
//            sendStatusMessage("检测到用户说话");
//            start();
//        }
//
//        @Override
//        public void onAsrEnd() {
//            super.onAsrEnd();
//            speechEndTime = System.currentTimeMillis();
//            sendMessage("检测到用户说话结束");
//        }
//
//        @Override
//        public void onAsrPartialResult(String[] results, RecogResult recogResult) {
//            sendStatusMessage("临时识别结果，结果是“" + results[0] + "”；原始json：" + recogResult.getOrigalJson());
//            super.onAsrPartialResult(results, recogResult);
//        }
//
//        @Override
//        public void onAsrFinalResult(String[] results, RecogResult recogResult) {
//            super.onAsrFinalResult(results, recogResult);
//            String message = "识别结束，结果是”" + results[0] + "”";
//            sendStatusMessage(message + "“；原始json：" + recogResult.getOrigalJson());
//            if (speechEndTime > 0) {
//                long diffTime = System.currentTimeMillis() - speechEndTime;
//                message += "；说话结束到识别结束耗时【" + diffTime + "ms】";
//
//            }
//            speechEndTime = 0;
//            sendMessage(message, status, true);
//            Log.w("speak",message);
//            myRecognizer.release();
//            syntherizer.release();
//            Init.synthesizer = null;
//            goAnswer(results[0]);
//        }
//
//        @Override
//        public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage) {
//            super.onAsrFinishError(errorCode, subErrorCode, errorMessage, descMessage);
//            String message = "识别错误, 错误码：" + errorCode + "," + subErrorCode;
//            sendStatusMessage(message + "；错误消息:" + errorMessage + "；描述信息：" + descMessage);
//            if (speechEndTime > 0) {
//                long diffTime = System.currentTimeMillis() - speechEndTime;
//                message += "。说话结束到识别结束耗时【" + diffTime + "ms】";
//            }
//            speechEndTime = 0;
//            sendMessage(message, status, true);
//            speechEndTime = 0;
//            myRecognizer.start(input.getStartParams());
//        }
//
//        @Override
//        public void onAsrOnlineNluResult(String nluResult) {
//            super.onAsrOnlineNluResult(nluResult);
//            if (!nluResult.isEmpty()) {
//                sendStatusMessage("原始语义识别结果json：" + nluResult);
//            }
//        }
//
//        @Override
//        public void onAsrFinish(RecogResult recogResult) {
//            super.onAsrFinish(recogResult);
//            sendStatusMessage("识别一段话结束。如果是长语音的情况会继续识别下段话。");
//
//        }
//
//        /**
//         * 长语音识别结束
//         */
//        @Override
//        public void onAsrLongFinish() {
//            super.onAsrLongFinish();
//            sendStatusMessage("长语音识别结束。");
//        }
//
//
//        /**
//         * 使用离线命令词时，有该回调说明离线语法资源加载成功
//         */
//        @Override
//        public void onOfflineLoaded() {
//            sendStatusMessage("【重要】离线资源加载成功。没有此回调可能离线语法功能不能使用。");
//        }
//
//        /**
//         * 使用离线命令词时，有该回调说明离线语法资源加载成功
//         */
//        @Override
//        public void onOfflineUnLoaded() {
//            sendStatusMessage(" 离线资源卸载成功。");
//        }
//
//        @Override
//        public void onAsrExit() {
//            super.onAsrExit();
//            sendStatusMessage("识别引擎结束并空闲中");
//        }
//
//        private void sendMessage(String message) {
//            sendMessage(message, WHAT_MESSAGE_STATUS);
//        }
//
//        private void sendStatusMessage(String message) {
//            sendMessage(message, status);
//        }
//
//        private void sendMessage(String message, int what) {
//            sendMessage(message, what, false);
//        }
//
//
//        private void sendMessage(String message, int what, boolean highlight) {
//            if (needTime && what != STATUS_FINISHED) {
//                message += "  ;time=" + System.currentTimeMillis();
//            }
//            Message msg = Message.obtain();
//            msg.what = what;
//            msg.arg1 = status;
//            if (highlight) {
//                msg.arg2 = 1;
//            }
//            msg.obj = message + "\n";
//            handler.sendMessage(msg);
//            Log.w("asr",message);
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        syntherizer.release();
////        Init.synthesizer = null;
////        myRecognizer.release();
//    }
//}
