package com.shengrui.huilian.main_sideslip.user_register;

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
public class User_presenter extends BasePresenter<User_model> {


   public User_model user_model;
    User_presenter() {
    }

    //单例
    private static User_presenter instance;

    public static User_presenter getInstance() {
        if (instance == null) {
            instance = new User_presenter();
        }
        return instance;
    }

    public void code(RequestParams params,BaseCallBack callBack){

        this.client.getDataClient(Config.GetCodeURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    user_model = (User_model) new Gson().fromJson(stringData, model);
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void login(RequestParams params,BaseCallBack callBack){
        this.client.getDataClient(Config.LoginURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    user_model = (User_model) new Gson().fromJson(stringData, model);
                    User_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void register(String mobile,String userPassword,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("userInfo.mobile",mobile);
        params.put("userInfo.userPassword",userPassword);
        this.client.getDataClient(Config.RegisterURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void forgetPassword(String mobile,String userPassword,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mobile",mobile);
        params.put("userPassword",userPassword);
        this.client.getDataClient(Config.ForgetPasswordURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void checkMobileRegister(String mobile,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mobile",mobile);
        this.client.getDataClient(Config.CheckMobileRegURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    User_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "该手机号码已注册", Toast.LENGTH_SHORT).show();
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void checkMobileForget(String mobile,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mobile",mobile);
        this.client.getDataClient(Config.CheckMobileForURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    User_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "该手机号码未注册", Toast.LENGTH_SHORT).show();
                    User_presenter.this.sendEmptyMessage(status);
                }
            }
        });
    }


    @Override
    public void onInit() {
        model = User_model.class;
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
