package com.shengrui.huilian.user_infor;

import android.content.Context;
import android.os.Handler;
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
import com.zhy.autolayout.utils.L;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by jh on 2016/5/29.
 */
public class UserInforPresenter extends BasePresenter<UserInforModel> {

    public static Context context;

    public UserInforModel mUserInforModel ;

    public static void setContext(Context context) {
        UserInforPresenter.context = context;
    }

    //单例
    private static UserInforPresenter instance;

    public static UserInforPresenter getInstance() {
        if (instance == null) {
            instance = new UserInforPresenter();
        }
        return instance;
    }

    private UserInforPresenter() {
    }

    @Override
    public void onInit() {
        model = UserInforModel.class;
    }

    public void loadingMessage(int userId, Object content,String message,int agency,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        params.put("agency",agency);
        params.put(message,content);

        this.client.getDataClient(Config.UpdateUserInfoURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                Log.d("---------------",stringData);
                if (status == Config.STATUS_SUCCESS) {
                    mUserInforModel = (UserInforModel) new Gson().fromJson(stringData, model);
                //    UserInforPresenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadingPhoto(int userId,String path,BaseCallBack callBack)  {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        Log.d("---------------------", path);
        try {
            params.put("userHead", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.client.getDataClient(Config.UpdateUserPhotoURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    mUserInforModel = (UserInforModel) new Gson().fromJson(stringData, model);
                    UserInforPresenter.this.sendEmptyMessage(status);
                }
            }
        });

    }

    public void refreshAllMessage(int userId,int myUserId, BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("userId", myUserId);
        params.put("colUserId", userId);
        this.client.getDataClient(Config.UserInfoURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Log.d("-----------------", stringData);
                    mUserInforModel = (UserInforModel) new Gson().fromJson(stringData, model);
                    Log.d("-----------------", mUserInforModel.getEmail());
                    UserInforPresenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void autoLogin(BaseCallBack callBack){
        RequestParams params = new RequestParams();
        this.client.getDataClient(Config.AutoLoginURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Log.d("-----------------", stringData);
                    mUserInforModel = (UserInforModel) new Gson().fromJson(stringData, model);
                    UserInforPresenter.this.sendEmptyMessage(status);
                }
            }
        });
    }

    public void collectUser(int userId, int colUserId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("collectUser.userId",userId);
        params.put("collectUser.colUserId",colUserId);

        this.client.getDataClient(Config.CollectUserURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == 100) {
                    UserInforPresenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void delCollectUser(int userId, int colUserId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        params.put("colUserId",colUserId);

        this.client.getDataClient(Config.CancelcollectUserURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == 100) {
                    UserInforPresenter.this.sendEmptyMessage(status);
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
