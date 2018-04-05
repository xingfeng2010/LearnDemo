package com.administrator.learndemo.map;

/**
 * Created by Administrator on 2018/1/5.
 */

import java.io.Serializable;

public class SearchPoi implements Serializable {
    public static String SEARCH="0";
    public static String NAVI="1";

    private String longitude;
    private String latitude;
    private String addrname;
    private String district;
    private double distance;
    private String type=SEARCH; //0代表搜索 ，1代表导航

    public SearchPoi(String latitude, String longitude, String addrname) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.addrname = addrname;
    }

    public SearchPoi() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddrname() {
        return addrname;
    }

    public void setAddrname(String addrname) {
        this.addrname = addrname;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }
}