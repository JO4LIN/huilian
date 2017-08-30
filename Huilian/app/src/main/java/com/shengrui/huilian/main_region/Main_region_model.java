package com.shengrui.huilian.main_region;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Main_region_model {
    private int cityId;
    private int provinceId;
    private String cityName;
    private int cityMediaNum;
    private int citySearchNum;
    private String provinceName;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityMediaNum() {
        return cityMediaNum;
    }

    public void setCityMediaNum(int cityMediaNum) {
        this.cityMediaNum = cityMediaNum;
    }

    public int getCitySearchNum() {
        return citySearchNum;
    }

    public void setCitySearchNum(int citySearchNum) {
        this.citySearchNum = citySearchNum;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
