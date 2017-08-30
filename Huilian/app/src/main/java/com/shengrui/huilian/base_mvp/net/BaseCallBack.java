package com.shengrui.huilian.base_mvp.net;

/**
 * Created by zhrx on 2016/4/9.
 */
public interface BaseCallBack {
    void onSuccess();
    void onFailure();
    void onStart();
    void onFinish();
    void onRetry();
    void onProgress();
}
