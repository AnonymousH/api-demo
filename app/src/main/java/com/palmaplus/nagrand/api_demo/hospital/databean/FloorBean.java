package com.palmaplus.nagrand.api_demo.hospital.databean;

/**
 * Created by Administrator on 2018-5-29.
 */

public class FloorBean {
    private String floorName;
    private long floorID;

    public FloorBean(String floorName, long floorID) {
        this.floorName = floorName;
        this.floorID = floorID;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public long getFloorID() {
        return floorID;
    }

    public void setFloorID(long floorID) {
        this.floorID = floorID;
    }
}
