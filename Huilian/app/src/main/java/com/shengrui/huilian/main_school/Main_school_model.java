package com.shengrui.huilian.main_school;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Main_school_model {

    private int provinceId;
    private String schoolName;
    private int cityId;
    private String schoolHead;
    private String schoolId;

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getSchoolHead() {
        return schoolHead;
    }

    public void setSchoolHead(String schoolHead) {
        this.schoolHead = schoolHead;
    }
}
