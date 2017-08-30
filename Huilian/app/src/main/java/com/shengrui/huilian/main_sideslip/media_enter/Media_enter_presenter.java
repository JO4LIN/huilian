package com.shengrui.huilian.main_sideslip.media_enter;

import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.Presenter.BasePresenter;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.Client;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

/**
 * Created by jh on 2016/8/5.
 */
public class Media_enter_presenter extends BasePresenter<Media_enter_model> {

    private static Media_enter_presenter instance;
    public Media_enter_model media_enter_model;

    public static Media_enter_presenter getInstance() {
        if (instance == null) {
            instance = new Media_enter_presenter();
        }
        return instance;
    }

    private Media_enter_presenter() {
    }

    @Override
    public void onInit() {
        model = Media_enter_model.class;
    }

    public void enterCode(String code,String url, BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("url",url);
        params.put("code",code);
        this.client.getDataClient(Config.CheckEnterMediaURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    media_enter_model = (Media_enter_model) new Gson().fromJson(stringData, model);
                    Media_enter_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "验证码错误", Toast.LENGTH_SHORT).show();
                    Media_enter_presenter.this.sendEmptyMessage(status);
                }
                if (status == 300) {
                    Toast.makeText(Global.context, "该公众号已存在", Toast.LENGTH_SHORT).show();
                    Media_enter_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void enterMedia(int userId,String mediaName, String wechatNum, String wechatHead,String twoCode, int fansNum,int readNum,
                              String intro, String cityName,String provinceName, String softMoreFirPrice, String softMoreSecPrice, String softMoreOtherPrice, String softSimplePrice,
                              String hardMoreFirPrice, String hardMoreSecPrice, String hardMoreOtherPrice, String hardSimplePrice,String sort, String schoolName,String fansNumPicture, BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mediaInfo.userId",userId);
        params.put("mediaInfo.mediaName",mediaName);
        params.put("mediaInfo.wechatNum",wechatNum);
        params.put("mediaInfo.wechatHead",wechatHead);
        params.put("mediaInfo.twoCode",twoCode);
        params.put("mediaInfo.fansNum",fansNum);
        params.put("mediaInfo.readNum",readNum);
        params.put("mediaInfo.intro",intro);
        params.put("cityName",cityName);
        params.put("provinceName",provinceName);
        params.put("mediaInfo.softMoreFirPrice",softMoreFirPrice);
        params.put("mediaInfo.softMoreSecPrice",softMoreSecPrice);
        params.put("mediaInfo.softMoreOtherPrice",softMoreOtherPrice);
        params.put("mediaInfo.softSimplePrice",softSimplePrice);
        params.put("mediaInfo.hardMoreFirPrice",hardMoreFirPrice);
        params.put("mediaInfo.hardMoreSecPrice",hardMoreSecPrice);
        params.put("mediaInfo.hardMoreOtherPrice",hardMoreOtherPrice);
        params.put("mediaInfo.hardSimplePrice",hardSimplePrice);
        params.put("mediaInfo.isVerification",0);
        params.put("sort",sort);
        params.put("schoolName",schoolName);
        params.put("fansNumPicture",fansNumPicture);

        this.client.getDataClient(Config.EnterMediaURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Media_enter_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onRefresh(FinishCallBack callBack) {

    }

    @Override
    public void onLoadMore(FinishCallBack callBack) {

    }

    @Override
    protected void handleMessage(int status) {

    }
}
