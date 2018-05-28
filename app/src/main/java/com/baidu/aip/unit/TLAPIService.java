/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit;


import android.content.Context;

import com.baidu.aip.unit.listener.OnResultListener;
import com.baidu.aip.unit.model.CommunicateResponse;
import com.baidu.aip.unit.model.TLResponse;
import com.baidu.aip.unit.parser.CommunicateParser;
import com.baidu.aip.unit.parser.TLCommunicateParser;
import com.baidu.aip.unit.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/16.
 */

public class TLAPIService {
    private static String url = "http://openapi.tuling123.com/openapi/api/v2";
    private static String mApiKey = "9d7eb9bf0ab44208aeade3ad8475ecb1";
    private static TLAPIService instance;
    private Context context;
    private String accessToken;


    private TLAPIService() {

    }

    public static TLAPIService getInstance() {
        synchronized (APIService.class) {
            if (instance == null) {
                instance = new TLAPIService();
            }
        }

        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();

        HttpUtil.getInstance().init();
        // DeviceUuidFactory.init(context);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
    }

    public void communicate(final OnResultListener<TLResponse> listener,
                            int sceneId, String query, String sessionId) {

        String json = "{\n" +
                "\t\"reqType\":0,\n" +
                "    \"perception\": {\n" +
                "        \"inputText\": {\n" +
                "            \"text\": \"" + query + "\"\n" +
                "        },\n" +
                "        \"inputImage\": {\n" +
                "            \"url\": \"imageUrl\"\n" +
                "        },\n" +
                "        \"selfInfo\": {\n" +
                "            \"location\": {\n" +
                "                \"city\": \"上海\",\n" +
                "                \"province\": \"浦东新区\",\n" +
                "                \"street\": \"杨高路\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"userInfo\": {\n" +
                "        \"apiKey\": \"" + mApiKey + "\",\n" +
                "        \"userId\": \"" + "sola" + "\"\n" +
                "    }\n" +
                "}";

        TLCommunicateParser parser = new TLCommunicateParser();
        HttpUtil.getInstance().post(url,json, parser, listener);
    }
}
