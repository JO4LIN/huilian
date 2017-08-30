package com.shengrui.huilian.main_search;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Main_search_model {
    private String wechatHead;
    private String mediaName;
    private String hardSimplePrice;

    private int mediaId;
    private String readNum;
    private String fansNum;
    boolean isVerification;

    public boolean getIsVerification() {
        return isVerification;
    }

    public void setIsVerification(boolean isVerification) {
        this.isVerification = isVerification;
    }
    public String getWechatHead() {
        return wechatHead;
    }

    public void setWechatHead(String wechatHead) {
        this.wechatHead = wechatHead;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getHardSimplePrice() {
        return hardSimplePrice;
    }

    public void setHardSimplePrice(String hardSimplePrice) {
        this.hardSimplePrice = hardSimplePrice;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }
}
