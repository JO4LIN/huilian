package com.shengrui.huilian.main_sideslip.setting_feedback;

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
 * Created by jh on 2016/7/26.
 */
public class Setting_feedback_presenter extends BasePresenter<Setting_feedback_model> {


   public Setting_feedback_model user_register_model;
    Setting_feedback_presenter() {
    }

    //单例
    private static Setting_feedback_presenter instance;

    public static Setting_feedback_presenter getInstance() {
        if (instance == null) {
            instance = new Setting_feedback_presenter();
        }
        return instance;
    }

    public void sendFeedBack(RequestParams params,BaseCallBack callBack){

        this.client.getDataClient(Config.SendFeedBackURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Setting_feedback_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onInit() {
        model = Setting_feedback_model.class;
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
