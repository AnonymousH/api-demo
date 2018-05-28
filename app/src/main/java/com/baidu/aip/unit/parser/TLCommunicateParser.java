/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.parser;

import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.model.CommunicateResponse;
import com.baidu.aip.unit.model.TLResponse;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.model.CommunicateResponse;

import android.util.Log;

/**
 * Created by Administrator on 2018/5/16.
 */

public class TLCommunicateParser implements Parser<TLResponse>{


    @Override
    public TLResponse parse(String json) throws UnitError {
        Log.e("return", "TLCommunicateParser:" + json);
        TLResponse response = new TLResponse();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = (JSONArray) object.get("results");
            int length = results.length();
            Log.w("return","result长度"+length);
//            int group;
            String url = "";
            if(length != 0) {
                for (int i = 0; i < length; ++i) {
                    JSONObject result = (JSONObject) results.get(i);
                    Log.w("return",result.toString());
                    String type = (String) result.get("resultType");
                    Log.w("return","result"+":"+i+":"+type);
                    if (type.equals("url")) {
                        JSONObject values = (JSONObject)result.get("values");
                        url = url + ":" + values.get("url");
                    } else if (type.equals("text")) {
                        JSONObject values = (JSONObject)result.get("values");
                        response.setText((String)values.get("text"));
                    }

                }
            }else{
                response.setText("你到底在说什么");
            }
            response.setUrl(url);
        }catch (Exception e){
            Log.w("return",e.getMessage());
            response.setText("你到底在说什么");
        }
        Log.w("return",response.toString());
        return response;
    }
}
