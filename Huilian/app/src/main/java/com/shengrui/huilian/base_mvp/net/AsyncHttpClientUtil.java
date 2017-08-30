package com.shengrui.huilian.base_mvp.net;


import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by zhrx on 2016/4/7.
 */
//网络客户端单例类
public class AsyncHttpClientUtil extends AsyncHttpClient {

    private AsyncHttpClientUtil() {
    }

    //静态单例
    private static AsyncHttpClientUtil instance;

    public static AsyncHttpClientUtil getInstance() {
        System.out.println("---------->>>>"+"AsyncHttpClientUtil+getInstance");
        if (instance == null) {
            instance = new AsyncHttpClientUtil();
            //网络超时为30秒
            instance.setTimeout(30000);
        }
        return instance;
    }

}
