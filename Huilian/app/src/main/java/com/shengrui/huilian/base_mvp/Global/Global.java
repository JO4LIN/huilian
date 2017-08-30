package com.shengrui.huilian.base_mvp.Global;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.shengrui.huilian.BuildConfig;

/**
 * Created by zhrx on 2016/4/8.
 */
public  class Global extends Application {
    public static boolean logined = false;
    public static int userId = 100000000;
    public static int mediaId = 100000003;

    public static String location = null;

    public static Context getContext() {
        return context;
    }

    public static Context context;



    public static OSS oss;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化OSS配置
        initOSSConfig();
    }
    private void initOSSConfig(){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.accessKey, Config.screctKey);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        if(BuildConfig.DEBUG){
            OSSLog.enableLog();
        }
        oss = new OSSClient(getApplicationContext(), Config.OSS_BUCKET_HOST_ID, credentialProvider, conf);
    }

}
