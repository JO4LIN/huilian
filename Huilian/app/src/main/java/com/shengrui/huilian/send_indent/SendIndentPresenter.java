package com.shengrui.huilian.send_indent;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.Presenter.BasePresenter;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.Client;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by jh on 2016/5/26.
 */
public class SendIndentPresenter extends BasePresenter<SendIndentModel> {

    public SendIndentModel mSendIndentModel ;


    //单例
    private static SendIndentPresenter instance;

    public static SendIndentPresenter getInstance() {
        if (instance == null) {
            instance = new SendIndentPresenter();
        }
        return instance;
    }

    private SendIndentPresenter() {
    }

    @Override
    public void onInit() {
        model = SendIndentModel.class;
    }

    public void sendingIndent(int userId, int mediaId, String title, String intro, String price,String graphicsTypes, String proDate,String link,
                              String date,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("indentInfo.userId",userId);
        params.put("indentInfo.mediaId",mediaId);
        params.put("indentInfo.title",title);
        params.put("indentInfo.intro",intro);
        params.put("indentInfo.price",price);
        params.put("indentInfo.graphicsTypes",graphicsTypes);
        params.put("indentInfo.proDate",proDate);
        params.put("indentInfo.link",link);
        params.put("indentInfo.date",date);
        params.put("indentInfo.progress",3);

        this.client.getDataClient(Config.SendIndentURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    mSendIndentModel = (SendIndentModel) new Gson().fromJson(stringData, model);
                    SendIndentPresenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void refreshMedia(int mediaId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mediaId",mediaId);

        this.client.getDataClient(Config.RefreshIndentURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                Log.d("-----------------",stringData);
                if (status == Config.STATUS_SUCCESS) {
                    mSendIndentModel = (SendIndentModel) new Gson().fromJson(stringData, model);
                   // SendIndentPresenter.this.context.
                   Log.d("-----------------", mSendIndentModel.getMediaName());
                   Log.d("-----------------", mSendIndentModel.getIndentNum());
                    SendIndentPresenter.this.sendEmptyMessage(status);
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
