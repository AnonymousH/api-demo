/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.model;

/**
 * Created by Administrator on 2018/5/16.
 */

public class TLResponse {
    private int code;
    private String intentName;
    private String actionName;
    private String parameters;
    private String resulteType;
    private String values;
    private String text = "";
    private String url = "";
    private String image = "";


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return text + url;
    }



}
