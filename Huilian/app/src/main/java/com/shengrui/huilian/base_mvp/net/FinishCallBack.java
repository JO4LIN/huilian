package com.shengrui.huilian.base_mvp.net;

/**
 * Created by zhrx on 2016/4/9.
 */
public class FinishCallBack implements BaseCallBack {
    public void onSuccess(){
        System.out.println("---------->>>>"+"FinishCallBack+onSuccess");
    }
    public void onFailure(){
        System.out.println("---------->>>>"+"FinishCallBack+onFailure");
    }
    public void onStart(){

    }
    public void onFinish(){

    }
    public void onRetry(){

    }
    public void onProgress(){

    }
}
