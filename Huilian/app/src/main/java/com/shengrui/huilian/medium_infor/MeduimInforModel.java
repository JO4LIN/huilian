package com.shengrui.huilian.medium_infor;

/**
 * Created by jh on 2016/4/24.
 */
public class MeduimInforModel {
    private int userId;
    private int mediaId;
    private String wechatHead = null;         //公众号头像
    private String mediaName= null;  //公众号
    private String wechatNum= null;       //微信号
    private String twoCode= null;          //二维码
    private int fansNum ;    //粉丝数
    private String medType= null;          //账号分类
    private String province= null;
    private String city= null;
    private String school= null;
    private int softMoreFirPrice;
    private int softMoreSecPrice;
    private int softMoreOtherPrice;
    private int softSimplePrice;
    private int hardMoreFirPrice;
    private int hardMoreSecPrice;
    private int hardMoreOtherPrice;
    private int hardSimplePrice;
    private int readNum ;
    private int judgeCol;




    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public String getTwoCode() {
        return twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }



    public int getSoftMoreFirPrice() {
        return softMoreFirPrice;
    }

    public void setSoftMoreFirPrice(int softMoreFirPrice) {
        this.softMoreFirPrice = softMoreFirPrice;
    }

    public int getSoftMoreSecPrice() {
        return softMoreSecPrice;
    }

    public void setSoftMoreSecPrice(int softMoreSecPrice) {
        this.softMoreSecPrice = softMoreSecPrice;
    }

    public int getSoftMoreOtherPrice() {
        return softMoreOtherPrice;
    }

    public void setSoftMoreOtherPrice(int softMoreOtherPrice) {
        this.softMoreOtherPrice = softMoreOtherPrice;
    }

    public int getSoftSimplePrice() {
        return softSimplePrice;
    }

    public void setSoftSimplePrice(int softSimplePrice) {
        this.softSimplePrice = softSimplePrice;
    }

    public int getHardMoreFirPrice() {
        return hardMoreFirPrice;
    }

    public void setHardMoreFirPrice(int hardMoreFirPrice) {
        this.hardMoreFirPrice = hardMoreFirPrice;
    }


    public int getHardMoreOtherPrice() {
        return hardMoreOtherPrice;
    }

    public void setHardMoreOtherPrice(int hardMoreOtherPrice) {
        this.hardMoreOtherPrice = hardMoreOtherPrice;
    }

    public int getHardMoreSecPrice() {
        return hardMoreSecPrice;
    }

    public void setHardMoreSecPrice(int hardMoreSecPrice) {
        this.hardMoreSecPrice = hardMoreSecPrice;
    }

    public int getHardSimplePrice() {
        return hardSimplePrice;
    }

    public void setHardSimplePrice(int hardSimplePrice) {
        this.hardSimplePrice = hardSimplePrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getWechatHead() {
        return wechatHead;
    }

    public void setWechatHead(String wechatHead) {
        this.wechatHead = wechatHead;
    }

    public int getJudgeCol() {
        return judgeCol;
    }

    public void setJudgeCol(int judgeCol) {
        this.judgeCol = judgeCol;
    }
}
