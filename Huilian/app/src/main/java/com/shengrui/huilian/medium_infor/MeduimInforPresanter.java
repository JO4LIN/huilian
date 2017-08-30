package com.shengrui.huilian.medium_infor;

import android.content.Context;
import android.graphics.Bitmap;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 2016/4/24.
 */
public class MeduimInforPresanter extends BasePresenter<MeduimInforModel> {

    public MeduimInforModel mMeduimInforModel = null;

    public String[] school ;

    public static Context context;

    public static void setContext(Context context) {
        MeduimInforPresanter.context = context;
    }

    private MeduimInforPresanter() {
    }

    //单例
    private static MeduimInforPresanter instance;

    public static MeduimInforPresanter getInstance() {
        if (instance == null) {
            instance = new MeduimInforPresanter();
        }
        return instance;
    }

    public void loadingMessage(int mediaId, Object content, String message, int agency, BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("mediaId",mediaId);
        params.put("agency",agency);
        params.put(message,content);

        this.client.getDataClient(Config.UpdateMediaInfoURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    mMeduimInforModel = (MeduimInforModel) new Gson().fromJson(stringData, model);
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadingPhoto(int mediaId, String path,BaseCallBack callBack)  {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.put("mediaId", mediaId);
        try {
            params.put("mediaHead", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.client.getDataClient(Config.UpdateMediaPhotoURL, params, callBack, new Client.StringDataResponseHandler() {
                @Override
                public void onSuccess(int status, String stringData) {
                    if (status == Config.STATUS_SUCCESS) {
                        mMeduimInforModel = (MeduimInforModel) new Gson().fromJson(stringData, model);
                        MeduimInforPresanter.this.sendEmptyMessage(status);
                    }
                }
        });

    }

    public void loadingCode(int mediaId, String path,BaseCallBack callBack)  {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.put("mediaId", mediaId);
        try {
            params.put("twocode", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.client.getDataClient(Config.UpdateMediaCodeURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    mMeduimInforModel = (MeduimInforModel) new Gson().fromJson(stringData, model);
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
            }
        });

    }

    public void loadingPrice(int mediaId, String contentSoft,String messageSoft, String contentHard,String messageHard,int agency,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put(messageSoft,contentSoft);
        params.put(messageHard, contentHard);
        params.put("agency",agency);
        params.put("mediaId",mediaId);

        this.client.getDataClient(Config.UpdateMediaInfoURL, params,callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    mMeduimInforModel = (MeduimInforModel) new Gson().fromJson(stringData, model);
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void refreshAllMessage(int userId,int mediaId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("colMediaId",mediaId);
        params.put("userId", userId);
        this.client.getDataClient(Config.MediaInfo, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Log.d("-----------------", stringData);
                    mMeduimInforModel = (MeduimInforModel) new Gson().fromJson(stringData, model);
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
            }

        });
    }

    public void findSchool(String cityName,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("CITY_NAME",cityName);

        this.client.getDataClient(Config.GetSchoolURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    try {
                        JSONObject obj = new JSONObject(stringData);
                        //返回数据的数组
                        JSONArray array = obj.getJSONArray(Config.KEY_DATA);
                        JSONArray jsonArray = new JSONArray(array.toString());
                        school = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++){
                           school[i] = jsonArray.getJSONObject(i).get("schoolName").toString();
                        }
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void collectMedia(int userId, int colMediaId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("collectMedia.userId",userId);
        params.put("collectMedia.colMediaId",colMediaId);

        this.client.getDataClient(Config.CollectMediaURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == 100) {
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void delCollectMedia(int userId, int colMediaId,BaseCallBack callBack){
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        params.put("colMediaId",colMediaId);

        this.client.getDataClient(Config.CancelcollectMediaURL, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == 100) {
                    MeduimInforPresanter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void saveMyBitmap(String bitName,Bitmap mBitmap){
        File f = new File("/sdcard/" + bitName + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(f.getPath());
    }

    @Override
    public void onInit() {
        //捆绑数据
        model = MeduimInforModel.class;
        //捆绑adapter
        //data是父类的网络请求后的list数据
        adapter = new MeduimInforAdapter(data);
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
