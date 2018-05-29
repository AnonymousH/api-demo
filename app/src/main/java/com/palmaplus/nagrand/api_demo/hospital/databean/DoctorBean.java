package com.palmaplus.nagrand.api_demo.hospital.databean;

/**
 * Created by Administrator on 2018-5-28.
 */

public class DoctorBean {

    private boolean isSelect;
    private int headImg;
    private String name;
    private String title;
    private String good;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
