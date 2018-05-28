///*
// * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
// */
////package com.baidu.aip.unit;
////
////import java.text.SimpleDateFormat;
////import java.util.ArrayList;
////import java.util.Date;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Locale;
////import java.util.Map;
////
////import com.baidu.aip.chatkit.ImageLoader;
////import com.baidu.aip.chatkit.message.MessageInput;
////import com.baidu.aip.chatkit.message.MessagesList;
////import com.baidu.aip.chatkit.message.MessagesListAdapter;
////
////import com.baidu.aip.chatkit.model.Hint;
////import com.baidu.aip.chatkit.model.Message;
////import com.baidu.aip.chatkit.model.User;
////import com.baidu.aip.unit.exception.UnitError;
////import com.baidu.aip.unit.listener.OnResultListener;
////import com.baidu.aip.unit.model.AccessToken;
////import com.baidu.aip.unit.model.CommunicateResponse;
////import com.baidu.aip.unit.model.Scene;
////
////import com.baidu.aip.unit.model.TLResponse;
////import com.baidu.aip.unit.widget.BasePopupWindow;
////import com.baidu.android.voicedemo.util.Logger;
////import com.baidu.tts.client.SpeechError;
////import com.baidu.tts.client.SpeechSynthesizer;
////import com.baidu.tts.client.SpeechSynthesizerListener;
////import com.baidu.tts.client.TtsMode;
////import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
////
////import android.Manifest;
////import android.content.Intent;
////import android.content.pm.PackageManager;
////import android.support.v4.app.ActivityCompat;
////import android.support.v7.app.AppCompatActivity;
////import android.os.Bundle;
////import android.text.TextUtils;
////import android.util.DisplayMetrics;
////import android.util.Log;
////import android.util.TypedValue;
////import android.view.Gravity;
////import android.view.KeyEvent;
////import android.view.View;
////import android.view.ViewGroup;
////import android.view.WindowManager;
////import android.view.inputmethod.EditorInfo;
////import android.widget.AdapterView;
////import android.widget.ArrayAdapter;
////import android.widget.ListView;
////import android.widget.PopupWindow;
////import android.widget.RelativeLayout;
////import android.widget.TextView;
////import android.widget.Toast;
////
////public class MainActivity extends AppCompatActivity
////        implements MessagesListAdapter.OnLoadMoreListener, MessageInput.InputListener,
////        MessageInput.VoiceInputListener {
////
////    private static final int SCENE_SWEEPER_ROBOT = 3087;
////    private static final int SCENE_NAVIGATE = 3109;
////    private static final int SCENE_CONSTELLATION = 10538;
////
////    protected ImageLoader imageLoader;
////    protected MessagesListAdapter<Message> messagesAdapter;
////    protected HintAdapter hintAdapter;
////
////    private TextView titleTv;
////    private RelativeLayout titleRl;
////    private RelativeLayout rootRl;
////    private BasePopupWindow popupWindow;
////    private MessagesList messagesList;
////    private MessageInput messageInput;
////
////    private User sender;
////    private User cs;
////    private String sessionId = "";
////    private Scene curScene;
////    private int id = 0;
////    private String accessToken;
////    private List<Message> waitList = new ArrayList<>();
////    private Map<Integer, String> sceneMap = new HashMap<Integer, String>();
////
////    private ListView dataListview;
////    private List<Scene> dataList;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
//////        SpeechSynthesizer mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//////        mSpeechSynthesizer.setContext(this);
//////        SpeechSynthesizerListener listener = new SpeechSynthesizerListener() {
//////            @Override
//////            public void onSynthesizeStart(String s) {
//////
//////            }
//////
//////            @Override
//////            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
//////
//////            }
//////
//////            @Override
//////            public void onSynthesizeFinish(String s) {
//////
//////            }
//////
//////            @Override
//////            public void onSpeechStart(String s) {
//////
//////            }
//////
//////            @Override
//////            public void onSpeechProgressChanged(String s, int i) {
//////
//////            }
//////
//////            @Override
//////            public void onSpeechFinish(String s) {
//////
//////            }
//////
//////            @Override
//////            public void onError(String s, SpeechError speechError) {
//////
//////            }
//////        };
//////        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
//////
//////        String AppId = "11222818";
//////        String AppKey = "QID1SOTBb9e70yWoIVWBnmTe";
//////        String AppSecret = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";
//////
//////        mSpeechSynthesizer.setAppId(AppId);
//////        mSpeechSynthesizer.setApiKey(AppKey,AppSecret);
//////
//////        mSpeechSynthesizer.auth(TtsMode.ONLINE);
//////        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
//////
//////        mSpeechSynthesizer.initTts(TtsMode.ONLINE);
//////
//////        mSpeechSynthesizer.speak("百度一下");
//////
//////        mSpeechSynthesizer.release();
////
////
////
//////
////        initData();
////
////        findView();
////        initAdapter();
////
////        messageInput.getInputEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
////            @Override
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if (actionId == EditorInfo.IME_ACTION_SEND){
////                    onSubmit(v.getEditableText());
////                    v.setText("");
////                }
////                return true;
////            }
////        });
////
////
////        hintAdapter = new HintAdapter(this);
////
////        changeScene(dataList.get(0));
////        initPopupWindow();
////
////        initAccessToken();
////    }
////
////    // 演示数据，开发者可到ai.baidu.com 官网理解与交互技术(UNIT)板块申请并训练自己的场景机器人
////    private void initData() {
////        sender = new User("0", "kf", "", true);
////        cs = new User("1", "客服", "", true);
////
//////        sceneMap.put(SCENE_SWEEPER_ROBOT, "扫地机器人");
//////        sceneMap.put(SCENE_NAVIGATE, "车载导航");
//////        sceneMap.put(SCENE_CONSTELLATION, "星座运势查询");
////        sceneMap.put(21387, "asdfdaadf");
////
////        dataList = new ArrayList<Scene>();
//////        dataList.add(new Scene(SCENE_SWEEPER_ROBOT , "扫地机器人"));
//////        dataList.add(new Scene(SCENE_NAVIGATE , "车载导航"));
//////        dataList.add(new Scene(SCENE_CONSTELLATION , "星座运势查询"));
////        dataList.add(new Scene(21387 , "asdfdf"));
////
////    }
////
////    private void findView() {
////        this.rootRl = (RelativeLayout) findViewById(R.id.root);
////        this.titleRl = (RelativeLayout) findViewById(R.id.title_rl);
////        this.titleTv = (TextView) findViewById(R.id.title_tv);
////        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
////        messageInput = (MessageInput) findViewById(R.id.input);
////
////        messageInput.setInputListener(this);
////        messageInput.setAudioInputListener(this);
//////        hintRV = (RecyclerView) findViewById(R.id.hint_rv);
////        titleRl.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                popupWindow.getPopupWindow().setAnimationStyle(R.style.animTranslate);
////                popupWindow.showAtLocation(rootRl, Gravity.BOTTOM, 0, 0);
////                WindowManager.LayoutParams lp=getWindow().getAttributes();
////                lp.alpha = 0.3f;
////                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
////                getWindow().setAttributes(lp);
////            }
////        });
////
////    }
////
////    /**
////     * 为了防止破解app获取ak，sk，建议您把ak，sk放在服务器端。
////     */
////    private void initAccessToken() {
////        APIService.getInstance().init(getApplicationContext());
////        APIService.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
////            @Override
////            public void onResult(AccessToken result) {
////                accessToken = result.getAccessToken();
////                Log.i("MainActivity", "AccessToken->" + result.getAccessToken());
////                if (!TextUtils.isEmpty(accessToken)) {
////                    resendWaitList();
////                }
////
////            }
////
////            @Override
////            public void onError(UnitError error) {
////                Log.i("wtf", "AccessToken->" + error.getErrorMessage());
////            }
////        }, "gPuG7cqMqEeCOrQ5ySpDjKrT", "y0za97jGweSWsB5qQK87s7oRb6k9MCZq");
//////        }, "ruKQt3mup9uvn0NACO1Qm3V7", "ZzOlRNdKYBE7hBwaVO7qRd3euqoAveN5");
////    }
////
////    /**
////     * 重发未发送成功的消息
////     */
////    private void resendWaitList() {
////        for (Message message : waitList) {
////            sendMessage(message);
////        }
////    }
////
////    @Override
////    protected void onStart() {
////        super.onStart();
////        Message message = new Message(String.valueOf(id++), cs, "主人，很高兴为你服务！", new Date());
////        // messagesAdapter.addToStart(message, true);
////    }
////
////
////    @Override
////    public boolean onSubmit(CharSequence input) {
////
////        Message message = new Message(String.valueOf(id++), sender, input.toString(), new Date());
////        messagesAdapter.addToStart(message, true);
////
////        sendMessage(message);
////        return true;
////    }
////
////    @Override
////    public void onVoiceInputClick() {
////
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
////                .PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 100);
////            return;
////        }
////
////        start();
////    }
////
////    private void initAdapter() {
////        messagesAdapter = new MessagesListAdapter<>(sender.getId(), imageLoader);
////        // messagesAdapter.enableSelectionMode(this);
////        messagesAdapter.setLoadMoreListener(this);
////        messagesAdapter.setOnHintClickListener(new MessagesListAdapter.OnHintClickListener<Message>() {
////            @Override
////            public void onHintClick(String hint) {
////                Message message = new Message(String.valueOf(id++), sender, hint, new Date());
////                sendMessage(message);
////                messagesAdapter.addToStart(message, true);
////            }
////        });
////        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
////                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
////                    @Override
////                    public void onMessageViewClick(View view, Message message) {
////                        //                        AppUtils.showToast(MainActivity.this,
////                        //                                message.getUser().getName() + " avatar click",
////                        //                                false);
////                    }
////                });
////        this.messagesList.setAdapter(messagesAdapter);
////
////    }
////
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////    }
////
////    @Override
////    public void onLoadMore(int page, int totalItemsCount) {
////
////    }
////
////    /**
////     * 切换场景
////     *
////     * @param scene
////     */
////    private void changeScene(Scene scene) {
////
////        curScene = scene;
////        titleTv.setText(scene.getName());
////        messagesAdapter.clear();
////
////        sessionId = "";
////        Message message = new Message(String.valueOf(id++), sender, "你好", new Date());
////        sendMessage(message);
////    }
////
////    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
////        return new MessagesListAdapter.Formatter<Message>() {
////            @Override
////            public String format(Message message) {
////                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
////                        .format(message.getCreatedAt());
////
////                String text = message.getText();
////                if (text == null) {
////                    text = "[attachment]";
////                }
////
////                return String.format(Locale.getDefault(), "%s: %s (%s)",
////                        message.getUser().getName(), text, createdAt);
////            }
////        };
////    }
////
////    private void sendMessageTL(Message message){
////        TLAPIService.getInstance().communicate(new OnResultListener<TLResponse>() {
////            @Override
////            public void onResult(TLResponse result) {
////                handleResponse0(result);
////            }
////
////            @Override
////            public void onError(UnitError error) {
////
////            }
////        },0,message.getText(),"");
////    }
////    private void handleResponse0(TLResponse result) {
////        String resp = result.getText();
////    }
////    private void sendMessage(Message message) {
////
////        if (TextUtils.isEmpty(accessToken)) {
////            waitList.add(message);
////            return;
////        }
////
////        APIService.getInstance().communicate(new OnResultListener<CommunicateResponse>() {
////            @Override
////            public void onResult(CommunicateResponse result) {
////
////                handleResponse(result);
////            }
////
////            @Override
////            public void onError(UnitError error) {
////
////            }
////        }, curScene.getId(), message.getText(), sessionId);
////
////    }
////
////    private void handleResponse(CommunicateResponse result) {
////        if (result != null) {
////            sessionId = result.sessionId;
////
////            //  如果有对于的动作action，请执行相应的逻辑
////            List<CommunicateResponse.Action> actionList = result.actionList;
////            if (actionList.size() > 1) {
////                Message message = new Message(String.valueOf(id++), cs, "", new Date());
////                for (CommunicateResponse.Action action : actionList) {
////
////                    if (!TextUtils.isEmpty(action.say)) {
////                        StringBuilder sb = new StringBuilder();
////                        sb.append(action.say);
////
////                        Message actionMessage = new Message("", cs, sb.toString(), new Date());
////
////                        for (String hintText : action.hintList) {
////
////                            actionMessage.getHintList().add(new Hint(hintText));
////                        }
////                        message.getComplexMessage().add(actionMessage);
////                    }
////                }
////                messagesAdapter.addToStart(message, true);
////            } else if (actionList.size() == 1){
////                CommunicateResponse.Action action = actionList.get(0);
////                if (!TextUtils.isEmpty(action.say)) {
////                    StringBuilder sb = new StringBuilder();
////                    sb.append(action.say);
////
////                    Message message = new Message(String.valueOf(id++), cs, sb.toString(), new Date());
////                    messagesAdapter.addToStart(message, true);
////                    for (String hintText : action.hintList) {
////
////                        message.getHintList().add(new Hint(hintText));
////                    }
////
////                }
////
////                // 执行自己的业务逻辑
////                if ("start_work_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "开始扫地");
////                } else if ("stop_work_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "停止工作");
////                } else if ("move_action_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "移动");
////                } else if ("timed_charge_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "定时充电");
////                } else if ("timed_task_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "定时扫地");
////                } else if ("sing_song_satisfy".equals(action.actionId)) {
////                    Log.i("wtf", "唱歌");
////                }
////
////                if (!TextUtils.isEmpty(action.mainExe)) {
////                    Toast.makeText(MainActivity.this, "请执行函数：" + action.mainExe, Toast.LENGTH_SHORT).show();
////                }
////            }
////        }
////    }
////
////    private void initPopupWindow() {
////        // get the height of screen
////        DisplayMetrics metrics = new DisplayMetrics();
////        getWindowManager().getDefaultDisplay().getMetrics(metrics);
////        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48 * dataList.size() + 3, metrics);
////        // create popup window
////        popupWindow = new BasePopupWindow(this, R.layout.scene_list_view, ViewGroup.LayoutParams.MATCH_PARENT,
////                height) {
////            @Override
////            protected void initView() {
////                View view=getContentView();
////                dataListview = (ListView) view.findViewById(R.id.data_list);
////                dataListview.setAdapter(new ArrayAdapter<Scene>(MainActivity.this, R.layout.item_popup_view, dataList));
////            }
////
////            @Override
////            protected void initEvent() {
////                dataListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        popupWindow.getPopupWindow().dismiss();
////                        Scene scene = dataList.get(position);
////                        changeScene(scene);
////                    }
////                });
////            }
////
////            @Override
////            protected void initWindow() {
////                super.initWindow();
////                PopupWindow instance = getPopupWindow();
////                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
////                    @Override
////                    public void onDismiss() {
////                        WindowManager.LayoutParams lp = getWindow().getAttributes();
////                        lp.alpha = 1.0f;
////                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
////                        getWindow().setAttributes(lp);
////                    }
////                });
////            }
////        };
////    }
////
////
////
////    /**
////     * 开始录音，点击“开始”按钮后调用。
////     */
////    protected void start() {
////        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);
////        // intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG); //修改对话框样式
////
////        startActivityForResult(intent, 2);
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        String text = "";
////        if (resultCode == RESULT_OK) {
////            ArrayList results = data.getStringArrayListExtra("results");
////            if (results != null && results.size() > 0) {
////                text += results.get(0);
////            }
////            Message message = new Message(String.valueOf(id++), sender, text, new Date());
////            sendMessage(message);
////            messagesAdapter.addToStart(message, true);
////        } else {
////            Logger.info("出现错误");
////        }
////    }
////
////}
//
//
////////////---------------------------------------
///*
// * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
// */
//package com.baidu.aip.unit;
//
//        import java.io.IOException;
//        import java.text.SimpleDateFormat;
//        import java.util.ArrayList;
//        import java.util.Date;
//        import java.util.HashMap;
//        import java.util.List;
//        import java.util.Locale;
//        import java.util.Map;
//
//        import com.baidu.aip.chatkit.ImageLoader;
//        import com.baidu.aip.chatkit.message.MessageInput;
//        import com.baidu.aip.chatkit.message.MessagesList;
//        import com.baidu.aip.chatkit.message.MessagesListAdapter;
//
//        import com.baidu.aip.chatkit.model.Hint;
//        import com.baidu.aip.chatkit.model.Message;
//        import com.baidu.aip.chatkit.model.User;
//        import com.baidu.aip.unit.exception.UnitError;
//        import com.baidu.aip.unit.listener.OnResultListener;
//        import com.baidu.aip.unit.model.AccessToken;
//        import com.baidu.aip.unit.model.CommunicateResponse;
//        import com.baidu.aip.unit.model.Scene;
//
//        import com.baidu.aip.unit.model.TLResponse;
//        import com.baidu.aip.unit.widget.BasePopupWindow;
//        import com.baidu.android.voicedemo.util.Logger;
//        import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
//        import com.baidu.tts.client.SpeechError;
//        import com.baidu.tts.client.SpeechSynthesizer;
//        import com.baidu.tts.client.SpeechSynthesizerListener;
//        import com.baidu.tts.client.TtsMode;
//        import com.baidu.tts.sample.control.InitConfig;
//        import com.baidu.tts.sample.control.MySyntherizer;
//        import com.baidu.tts.sample.control.NonBlockSyntherizer;
//        import com.baidu.tts.sample.listener.MessageListener;
//        import com.baidu.tts.sample.listener.UiMessageListener;
//        import com.baidu.tts.sample.util.AutoCheck;
//        import com.baidu.tts.sample.util.OfflineResource;
//        import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
//
//        import android.Manifest;
//        import android.content.Intent;
//        import android.content.pm.PackageManager;
//        import android.media.AudioManager;
//        import android.os.Handler;
//        import android.support.v4.app.ActivityCompat;
//        import android.support.v7.app.AppCompatActivity;
//        import android.os.Bundle;
//        import android.text.TextUtils;
//        import android.util.DisplayMetrics;
//        import android.util.Log;
//        import android.util.TypedValue;
//        import android.view.Gravity;
//        import android.view.KeyEvent;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.view.WindowManager;
//        import android.view.inputmethod.EditorInfo;
//        import android.widget.AdapterView;
//        import android.widget.ArrayAdapter;
//        import android.widget.ListView;
//        import android.widget.PopupWindow;
//        import android.widget.RelativeLayout;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity
//        implements MessagesListAdapter.OnLoadMoreListener, MessageInput.InputListener,
//        MessageInput.VoiceInputListener {
//
//    private static final int SCENE_SWEEPER_ROBOT = 3087;
//    private static final int SCENE_NAVIGATE = 3109;
//    private static final int SCENE_CONSTELLATION = 10538;
//
//    protected ImageLoader imageLoader;
//    protected MessagesListAdapter<Message> messagesAdapter;
//    protected HintAdapter hintAdapter;
//
//    private TextView titleTv;
//    private RelativeLayout titleRl;
//    private RelativeLayout rootRl;
//    private BasePopupWindow popupWindow;
//    private MessagesList messagesList;
//    private MessageInput messageInput;
//
//    private User sender;
//    private User cs;
//    private String sessionId = "";
//    private Scene curScene;
//    private int id = 0;
//    private String accessToken;
//    private List<Message> waitList = new ArrayList<>();
//    private Map<Integer, String> sceneMap = new HashMap<Integer, String>();
//
//    private ListView dataListview;
//    private List<Scene> dataList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initData();
//
//        findView();
//        initAdapter();
//
//        messageInput.getInputEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND){
//                    onSubmit(v.getEditableText());
//                    v.setText("");
//                }
//                return true;
//            }
//        });
//
//
//        hintAdapter = new HintAdapter(this);
//
//        changeScene(dataList.get(0));
//        initPopupWindow();
//
//        TLAPIService.getInstance().init(getApplicationContext());
//        if(!Init.isInit){
//            Log.w("init","final init");
//            initialTts();
//            //initTTs();
//            Init.isInit = true;
//        }
//        synthesizer = Init.synthesizer;
////        initAccessToken();
//    }
//
//    // 演示数据，开发者可到ai.baidu.com 官网理解与交互技术(UNIT)板块申请并训练自己的场景机器人
//    private void initData() {
//        sender = new User("0", "kf", "", true);
//        cs = new User("1", "客服", "", true);
//
////        sceneMap.put(SCENE_SWEEPER_ROBOT, "扫地机器人");
////        sceneMap.put(SCENE_NAVIGATE, "车载导航");
////        sceneMap.put(SCENE_CONSTELLATION, "星座运势查询");
//        sceneMap.put(21387, "富聪小聪");
//
//        dataList = new ArrayList<Scene>();
////        dataList.add(new Scene(SCENE_SWEEPER_ROBOT , "扫地机器人"));
////        dataList.add(new Scene(SCENE_NAVIGATE , "车载导航"));
////        dataList.add(new Scene(SCENE_CONSTELLATION , "星座运势查询"));
//        dataList.add(new Scene(21387 , "富聪小聪"));
//
//    }
//
//    private void findView() {
//        this.rootRl = (RelativeLayout) findViewById(R.id.root);
//        this.titleRl = (RelativeLayout) findViewById(R.id.title_rl);
//        this.titleTv = (TextView) findViewById(R.id.title_tv);
//        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
//        messageInput = (MessageInput) findViewById(R.id.input);
//
//        messageInput.setInputListener(this);
//        messageInput.setAudioInputListener(this);
////        hintRV = (RecyclerView) findViewById(R.id.hint_rv);
//        titleRl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.getPopupWindow().setAnimationStyle(R.style.animTranslate);
//                popupWindow.showAtLocation(rootRl, Gravity.BOTTOM, 0, 0);
//                WindowManager.LayoutParams lp=getWindow().getAttributes();
//                lp.alpha = 0.3f;
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                getWindow().setAttributes(lp);
//            }
//        });
//
//    }
//
//    /**
//     * 为了防止破解app获取ak，sk，建议您把ak，sk放在服务器端。
//     */
//    private void initAccessToken() {
//        APIService.getInstance().init(getApplicationContext());
//        APIService.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken result) {
//                accessToken = result.getAccessToken();
//                Log.i("MainActivity", "AccessToken->" + result.getAccessToken());
//                if (!TextUtils.isEmpty(accessToken)) {
//                    resendWaitList();
//                }
//
//            }
//
//            @Override
//            public void onError(UnitError error) {
//                Log.i("wtf", "AccessToken->" + error.getErrorMessage());
//            }
//        }, "gPuG7cqMqEeCOrQ5ySpDjKrT", "y0za97jGweSWsB5qQK87s7oRb6k9MCZq");
////        }, "ruKQt3mup9uvn0NACO1Qm3V7", "ZzOlRNdKYBE7hBwaVO7qRd3euqoAveN5");
//    }
//
//    protected SpeechSynthesizer mSpeechSynthesizer;
//    /**
//     * 注意此处为了说明流程，故意在UI线程中调用。
//     * 实际集成中，该方法一定在新线程中调用，并且该线程不能结束。具体可以参考NonBlockSyntherizer的写法
//     */
//    private void initTTs() {
//        LoggerProxy.printable(true); // 日志打印在logcat中
//        boolean isMix = ttsMode.equals(TtsMode.MIX);
//        boolean isSuccess;
//        if (isMix) {
//            Log.e("offline","nooffline");
////            // 检查2个离线资源是否可读
////            isSuccess = checkOfflineResources();
////            if (!isSuccess) {
////                return;
////            } else {
////                print("离线资源存在并且可读, 目录：" + TEMP_DIR);
////            }
//        }
//        // 日志更新在UI中，可以换成MessageListener，在logcat中查看日志
//        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
//
//        // 1. 获取实例
//        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//        mSpeechSynthesizer.setContext(this);
//
//        // 2. 设置listener
//        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
//
//        // 3. 设置appId，appKey.secretKey
//        int result = mSpeechSynthesizer.setAppId(appId);
//        checkResult(result, "setAppId");
//        result = mSpeechSynthesizer.setApiKey(appKey, secretKey);
//        checkResult(result, "setApiKey");
//
//        // 4. 支持离线的话，需要设置离线模型
//        if (isMix) {
////            // 检查离线授权文件是否下载成功，离线授权文件联网时SDK自动下载管理，有效期3年，3年后的最后一个月自动更新。
////            isSuccess = checkAuth();
////            if (!isSuccess) {
////                return;
////            }
////            // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
////            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_FILENAME);
////            // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
////            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, MODEL_FILENAME);
//        }
//
//        // 5. 以下setParam 参数选填。不填写则默认值生效
//        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
//        // 设置合成的音量，0-9 ，默认 5
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
//        // 设置合成的语速，0-9 ，默认 5
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
//        // 设置合成的语调，0-9 ，默认 5
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
//
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
//        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
//        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//
//        mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);
//
//        // x. 额外 ： 自动so文件是否复制正确及上面设置的参数
//        Map<String, String> params = new HashMap<>();
//        // 复制下上面的 mSpeechSynthesizer.setParam参数
//        // 上线时请删除AutoCheck的调用
//        if (isMix) {
////            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_FILENAME);
////            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, MODEL_FILENAME);
//        }
//        InitConfig initConfig =  new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
//        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
//            @Override
//            /**
//             * 开新线程检查，成功后回调
//             */
//            public void handleMessage(android.os.Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainDebugMessage();
////                        print(message); // 可以用下面一行替代，在logcat中查看代码
//                         Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//
//        });
//
//        // 6. 初始化
//        result = mSpeechSynthesizer.initTts(ttsMode);
//        checkResult(result, "initTts");
//        mSpeechSynthesizer.speak("呵呵");
//    }
//    /**
//     * 重发未发送成功的消息
//     */
//    private void resendWaitList() {
//        for (Message message : waitList) {
////            sendMessage(message);
//            sendMessageTL(message);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Message message = new Message(String.valueOf(id++), cs, "主人，很高兴为你服务！", new Date());
//        // messagesAdapter.addToStart(message, true);
//    }
//
//
//    protected String appId = "11222818";
//
//    protected String appKey = "QID1SOTBb9e70yWoIVWBnmTe";
//
//    protected String secretKey = "2PalQ9mk9ViSGAcfaTMM0bvV17g7CI5R";
//
//    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
//    protected TtsMode ttsMode = TtsMode.ONLINE;
//
//    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
//    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
//    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
//    protected String offlineVoice = OfflineResource.VOICE_MALE;
//
//    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================
//
//    // 主控制类，所有合成控制方法从这个类开始
//    protected MySyntherizer synthesizer;
//
//    protected Handler mainHandler;
//
//    protected void initialTts() {
//        LoggerProxy.printable(true); // 日志打印在logcat中
//        // 设置初始化参数
//        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
//        SpeechSynthesizerListener listener = new MessageListener();
//
//        Map<String, String> params = getParams();
//
//
//        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
//        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
//
//        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
//        // 上线时请删除AutoCheck的调用
////        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
////            @Override
////            public void handleMessage(android.os.Message msg) {
////                if (msg.what == 100) {
////                    AutoCheck autoCheck = (AutoCheck) msg.obj;
////                    synchronized (autoCheck) {
////                        String message = autoCheck.obtainDebugMessage();
//////                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
////                         Log.w("AutoCheckMessage", message);
////                    }
////                }
////            }
////
////        });
////        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
////        Init.synthesizer = NonBlockSyntherizer.getInstance(this,initConfig,mainHandler);
//        Init.synthesizer = new NonBlockSyntherizer(this,initConfig,mainHandler);
//    }
//    /**
//     * 合成的参数，可以初始化时填写，也可以在合成前设置。
//     *
//     * @return
//     */
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
//    /**
//     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
//     * 获取音频流的方式见SaveFileActivity及FileSaveListener
//     * 需要合成的文本text的长度不能超过1024个GBK字节。
//     */
//    private void speak(String text) {
//
////        // 需要合成的文本text的长度不能超过1024个GBK字节。
//        // 合成前可以修改参数：
//        // Map<String, String> params = getParams();
//        // synthesizer.setParams(params);
//        int result = synthesizer.speak(text);
////        Log.w("final","before check");
////        int result = mSpeechSynthesizer.speak(text);
//        Log.w("final","before check");
////        checkResult(result, "speak");
//    }
//    private void checkResult(int result, String method) {
//        Log.e("final","1111111111111");
//
//        if (result != 0) {
//            Log.e("final","error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
//        }
//    }
//    protected OfflineResource createOfflineResource(String voiceType) {
//        OfflineResource offlineResource = null;
//        try {
//            offlineResource = new OfflineResource(this, voiceType);
//        } catch (IOException e) {
//            // IO 错误自行处理
//            e.printStackTrace();
//            Log.w("final","【error】:copy files from assets failed." + e.getMessage());
//        }
//        return offlineResource;
//    }
//    @Override
//    public boolean onSubmit(CharSequence input) {
//
//        Message message = new Message(String.valueOf(id++), sender, input.toString(), new Date());
//        messagesAdapter.addToStart(message, true);
//
//        sendMessageTL(message);
////        sendMessage(message);
//        return true;
//    }
//
//    @Override
//    public void onVoiceInputClick() {
//
//        Log.w("voice","click");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
//                .PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 100);
//            return;
//        }
//
//        start();
//    }
//
//    private void initAdapter() {
//        messagesAdapter = new MessagesListAdapter<>(sender.getId(), imageLoader);
//        // messagesAdapter.enableSelectionMode(this);
//        messagesAdapter.setLoadMoreListener(this);
//        messagesAdapter.setOnHintClickListener(new MessagesListAdapter.OnHintClickListener<Message>() {
//            @Override
//            public void onHintClick(String hint) {
//                Message message = new Message(String.valueOf(id++), sender, hint, new Date());
////                sendMessage(message);
//                sendMessageTL(message);
//                messagesAdapter.addToStart(message, true);
//            }
//        });
//        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
//                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
//                    @Override
//                    public void onMessageViewClick(View view, Message message) {
//                        //                        AppUtils.showToast(MainActivity.this,
//                        //                                message.getUser().getName() + " avatar click",
//                        //                                false);
//                    }
//                });
//        this.messagesList.setAdapter(messagesAdapter);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLoadMore(int page, int totalItemsCount) {
//
//    }
//
//    /**
//     * 切换场景
//     *
//     * @param scene
//     */
//    private void changeScene(Scene scene) {
//
//        curScene = scene;
//        titleTv.setText(scene.getName());
//        messagesAdapter.clear();
//
//        sessionId = "";
//        Message message = new Message(String.valueOf(id++), sender, "你好", new Date());
////        sendMessage(message);
//    }
//
//    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
//        return new MessagesListAdapter.Formatter<Message>() {
//            @Override
//            public String format(Message message) {
//                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
//                        .format(message.getCreatedAt());
//
//                String text = message.getText();
//                if (text == null) {
//                    text = "[attachment]";
//                }
//
//                return String.format(Locale.getDefault(), "%s: %s (%s)",
//                        message.getUser().getName(), text, createdAt);
//            }
//        };
//    }
//
//    private void sendMessageTL(Message message){
//        TLAPIService.getInstance().communicate(new OnResultListener<TLResponse>() {
//            @Override
//            public void onResult(TLResponse result) {
//                handleResponse0(result);
//            }
//
//            @Override
//            public void onError(UnitError error) {
//
//            }
//        },0,message.getText(),"");
//    }
//    private void handleResponse0(TLResponse result) {
//        String resp = result.getText();
//        String url = result.getUrl();
//
//        messagesAdapter.addToStart(new Message(String.valueOf(id++), cs, resp + url, new Date()), true);
//        Log.w("final","start speak" + resp);
//        speak(resp);
//        Log.w("final","end speak");
//    }
//    private void sendMessage(Message message) {
//
//        if (TextUtils.isEmpty(accessToken)) {
//            waitList.add(message);
//            return;
//        }
//
//        APIService.getInstance().communicate(new OnResultListener<CommunicateResponse>() {
//            @Override
//            public void onResult(CommunicateResponse result) {
//
//                handleResponse(result);
//            }
//
//            @Override
//            public void onError(UnitError error) {
//
//            }
//        }, curScene.getId(), message.getText(), sessionId);
//
//    }
//
//    private void handleResponse(CommunicateResponse result) {
//        if (result != null) {
//            sessionId = result.sessionId;
//
//            //  如果有对于的动作action，请执行相应的逻辑
//            List<CommunicateResponse.Action> actionList = result.actionList;
//            if (actionList.size() > 1) {
//                Message message = new Message(String.valueOf(id++), cs, "", new Date());
//                for (CommunicateResponse.Action action : actionList) {
//
//                    if (!TextUtils.isEmpty(action.say)) {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(action.say);
//
//                        Message actionMessage = new Message("", cs, sb.toString(), new Date());
//
//                        for (String hintText : action.hintList) {
//
//                            actionMessage.getHintList().add(new Hint(hintText));
//                        }
//                        message.getComplexMessage().add(actionMessage);
//                    }
//                }
//                messagesAdapter.addToStart(message, true);
//            } else if (actionList.size() == 1){
//                CommunicateResponse.Action action = actionList.get(0);
//                if (!TextUtils.isEmpty(action.say)) {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(action.say);
//
//                    Message message = new Message(String.valueOf(id++), cs, sb.toString(), new Date());
//                    messagesAdapter.addToStart(message, true);
//                    for (String hintText : action.hintList) {
//
//                        message.getHintList().add(new Hint(hintText));
//                    }
//
//                }
//
//                // 执行自己的业务逻辑
//                if ("start_work_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "开始扫地");
//                } else if ("stop_work_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "停止工作");
//                } else if ("move_action_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "移动");
//                } else if ("timed_charge_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "定时充电");
//                } else if ("timed_task_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "定时扫地");
//                } else if ("sing_song_satisfy".equals(action.actionId)) {
//                    Log.i("wtf", "唱歌");
//                }
//
//                if (!TextUtils.isEmpty(action.mainExe)) {
//                    Toast.makeText(MainActivity.this, "请执行函数：" + action.mainExe, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private void initPopupWindow() {
//        // get the height of screen
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48 * dataList.size() + 3, metrics);
//        // create popup window
//        popupWindow = new BasePopupWindow(this, R.layout.scene_list_view, ViewGroup.LayoutParams.MATCH_PARENT,
//                height) {
//            @Override
//            protected void initView() {
//                View view=getContentView();
//                dataListview = (ListView) view.findViewById(R.id.data_list);
//                dataListview.setAdapter(new ArrayAdapter<Scene>(MainActivity.this, R.layout.item_popup_view, dataList));
//            }
//
//            @Override
//            protected void initEvent() {
//                dataListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        popupWindow.getPopupWindow().dismiss();
//                        Scene scene = dataList.get(position);
//                        changeScene(scene);
//                    }
//                });
//            }
//
//            @Override
//            protected void initWindow() {
//                super.initWindow();
//                PopupWindow instance = getPopupWindow();
//                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        WindowManager.LayoutParams lp = getWindow().getAttributes();
//                        lp.alpha = 1.0f;
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                        getWindow().setAttributes(lp);
//                    }
//                });
//            }
//        };
//    }
//
//
//
//    /**
//     * 开始录音，点击“开始”按钮后调用。
//     */
//    protected void start() {
//        Log.w("voice","position 1");
//        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);
//        // intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG); //修改对话框样式
//
//        Log.w("voice","position 2");
//        startActivityForResult(intent, 2);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.w("voice","callback");
//        String text = "";
//        if (resultCode == RESULT_OK && requestCode ==2) {
//            ArrayList results = data.getStringArrayListExtra("results");
//            if (results != null && results.size() > 0) {
//                text += results.get(0);
//            }
//            Log.e("test",text);
//
//            ///
//
//
//
//
//
//
//            Message message = new Message(String.valueOf(id++), sender, text, new Date());
//////            sendMessage(message);
//            sendMessageTL(message);
//            messagesAdapter.addToStart(message, true);
//        } else {
//            Logger.info("出现错误");
//        }
//    }
//
//}
