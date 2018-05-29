//package com.ping.chatdemo.activity;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.preference.PreferenceManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.baidu.aip.unit.TLAPIService;
//import com.baidu.aip.unit.exception.UnitError;
//import com.baidu.aip.unit.listener.OnResultListener;
//import com.baidu.aip.unit.model.TLResponse;
//
//import com.baidu.android.voicedemo.control.MyRecognizer;
//import com.baidu.android.voicedemo.recognization.ChainRecogListener;
//import com.baidu.android.voicedemo.recognization.CommonRecogParams;
//import com.baidu.android.voicedemo.recognization.IRecogListener;
//import com.baidu.android.voicedemo.recognization.RecogResult;
//import com.baidu.android.voicedemo.recognization.StatusRecogListener;
//import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
//import com.ping.chatdemo.R;
//import com.ping.chatdemo.adapter.ChatAdapter;
//import com.ping.chatdemo.dao.MsgDaoUtil;
//import com.ping.chatdemo.entity.Msg;
//import com.ping.chatdemo.listener.OnDbUpdateListener;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class MainActivity extends AppCompatActivity {
//
//    private List<Msg> mMsgs;
//    private MsgDaoUtil mMsgDaoUtil;
//    private ChatAdapter mAdapter;
//    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//
//    @BindView(R.id.rv_chatList)
//    RecyclerView mRvChatList;
//    @BindView(R.id.et_content)
//    EditText mEtContent;
//    @BindView(R.id.bt_send)
//    Button mBtSend;
//    @BindView(R.id.bt_voice_send)
//    Button bt_voice_send;
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            addMsg(new Msg(null, "请问有什么问题需要咨询?聊聊天也行啊!", Msg.TYPE_BLE, df.format(new Date())));
////            handler.postDelayed(this, 5000);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        //http初始化
//        TLAPIService.getInstance().init(this);
//
//        //语音引擎初始化
//        init();
//
//        mMsgDaoUtil = new MsgDaoUtil(this);
//
////        mMsgs = mMsgDaoUtil.queryAllMsg();
//        mMsgs = new ArrayList<>();
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRvChatList.setLayoutManager(linearLayoutManager);
//        mAdapter = new ChatAdapter(this, mMsgs);
//        mRvChatList.setAdapter(mAdapter);
//        mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//
//        mMsgDaoUtil.setUpdateListener(new OnDbUpdateListener() {
//            @Override
//            public void onUpdate(Msg msg) {
//                mAdapter.addItem(msg);
//                mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//            }
//        });
//
//        mRvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy < -10) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mEtContent.getWindowToken(), 0);
//                }
//            }
//        });
//
//        handler.postDelayed(runnable, 300);
////        handler.sendMessage()
//    }
//
//    private boolean addMsg(Msg msg) {
//        boolean flag = mMsgDaoUtil.insertMsg(msg);
//        return flag;
//    }
//
//
//    @OnClick(R.id.bt_send)
//    public void onViewClicked() {
//        String content = mEtContent.getText().toString();
//        addMsg(new Msg(null, content, Msg.TYPE_PHONE, df.format(new Date())));
//        answer(content);
//        mEtContent.setText("");
//    }
//
//    static final int status_saying = 1;
//    static final int status_no_saying = 0;
//    static final int status_bot_saying = 2;
//    int say_status = status_no_saying;
//    @OnClick(R.id.bt_voice_send)
//    public void onVoiceClicked() {
//        Log.i("YY","--------------onVoiceClicked()-----------");
//        if(say_status == status_no_saying){
//            myRecognizer.start(params);
//            bt_voice_send.setText("正在录音");
//            say_status = status_saying;
//        }else if(say_status == status_saying){
//            myRecognizer.stop();
//            bt_voice_send.setText("开始录音");
//            say_status = status_no_saying;
//        }else if(say_status == 2){
//
//        }
//    }
//
//        private void answer(String question){
//        TLAPIService.getInstance().communicate(new OnResultListener<TLResponse>() {
//            @Override
//            public void onResult(TLResponse result) {
//                String text = result.getText();
//                String url = result.getUrl();
//                String image = result.getImage();
//                addMsg(new Msg(null, text, Msg.TYPE_BLE, df.format(new Date())));
//            }
//
//            @Override
//            public void onError(UnitError error) {
//                addMsg(new Msg(null, "系统错误", Msg.TYPE_BLE, df.format(new Date())));
//            }
//        },0,question,"");
//    }
//
//
//    //语音部分
//
//    private void init(){
//        initPermission();
//        initRecog();
////        initialTts();
////        syntherizer = Init.synthesizer;
//    }
//
//
//
//    //语音识别参数
//    protected CommonRecogParams apiParams;
//    protected int status;
//    protected boolean enableOffline = false;
//    protected ChainRecogListener listener;
//    protected MyRecognizer myRecognizer;
//    protected Map<String,Object> params;
//
//
//    //识别引擎初始化
//    protected void initRecog() {
////        StatusRecogListener listener = new MessageStatusRecogListener(handler);
//        listener = new ChainRecogListener();
//        listener.addListener(new MyListener(handler));
//        myRecognizer = new MyRecognizer(this, listener);
//        apiParams = getApiParams();
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        params = apiParams.fetch(sp);
////        status = STATUS_NONE;
////        if (enableOffline) {
////            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
////        }
//    }
//    protected CommonRecogParams getApiParams() {
//        return new OnlineRecogParams(this);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
//
//
//    class MyListener extends StatusRecogListener implements IRecogListener {
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
//
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
//
//
//            say_status = status_no_saying;
//            bt_voice_send.setText("开始录音");
//            addMsg(new Msg(null, message, Msg.TYPE_PHONE, df.format(new Date())));
//            answer(message);
//
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
//
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
//}
