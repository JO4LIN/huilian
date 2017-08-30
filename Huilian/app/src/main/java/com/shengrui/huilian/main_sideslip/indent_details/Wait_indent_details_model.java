package com.shengrui.huilian.main_sideslip.indent_details;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Wait_indent_details_model {
    private int indentId;
    private String wechatHead;
    private String mediaName;
    private String price;
    private String title;
    private String date;
    private String progress;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public int getIndentId() {
        return indentId;
    }

    public void setIndentId(int indentId) {
        this.indentId = indentId;
    }


    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getWechatHead() {
        return wechatHead;
    }

    public void setWechatHead(String wechatHead) {
        this.wechatHead = wechatHead;
    }
}
