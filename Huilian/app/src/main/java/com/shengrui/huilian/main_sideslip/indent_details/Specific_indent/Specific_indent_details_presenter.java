package com.shengrui.huilian.main_sideslip.indent_details.Specific_indent;

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

/**
 * Created by jh on 2016/7/23.
 */
public class Specific_indent_details_presenter  extends BasePresenter<Specific_indent_details_model> {

    public Specific_indent_details_model indentModel ;

    //单例
    private static Specific_indent_details_presenter instance;

    public static Specific_indent_details_presenter getInstance() {
        if (instance == null) {
            instance = new Specific_indent_details_presenter();
        }
        return instance;
    }

    private Specific_indent_details_presenter() {
    }

    @Override
    public void onInit() {
        model = Specific_indent_details_model.class;
    }

    public void refreshIndent(int indentId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("indentId",indentId);

        this.client.getDataClient(Config.FindIndentInfoURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Log.d("--------------",stringData);
                    indentModel = (Specific_indent_details_model) new Gson().fromJson(stringData, model);
                    Specific_indent_details_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }


    public void progress(RequestParams params,BaseCallBack callBack){

        this.client.getDataClient(Config.UpdateProgressURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Specific_indent_details_presenter.this.sendEmptyMessage(status);
                }if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void code(RequestParams params,BaseCallBack callBack){

        this.client.getDataClient(Config.UpdateProgressURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    indentModel = (Specific_indent_details_model) new Gson().fromJson(stringData, model);
                    Specific_indent_details_presenter.this.sendEmptyMessage(status);
                }if (status == 200) {
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
