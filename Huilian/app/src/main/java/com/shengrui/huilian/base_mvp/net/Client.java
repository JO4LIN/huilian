package com.shengrui.huilian.base_mvp.net;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.shengrui.huilian.base_mvp.Global.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhrx on 2016/4/9.
 */
public class Client<T> {
    public AsyncHttpClientUtil clientUtil = AsyncHttpClientUtil.getInstance();

    /**
     * 状态码、数据总数、返回的数据列表
     *
     * @param url      网络请求地址
     * @param params   网络返回数据
     * @param model    数据类
     * @param callBack 回调
     * @param handler  消息处理
     */
    public void getDataClient(final String url,
                              final RequestParams params,
                              final Class model,
                              final BaseCallBack callBack,
                              final DataHttpResponseHandler<T> handler) {
        //框架里面的post请求
        clientUtil.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("---------->>>>onFailure", "onFailure");
                //调用回调中失败
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {

                    Log.d("---------------",responseString);
                    JSONObject obj = new JSONObject(responseString);

                    //后台返回请求的状态码,如果没有该参数返回-100错误
                    int status = obj.optInt(Config.KEY_STATUS, Config.STATUS_NO_FAILURE);

                    //数据的总数,如果没有该参数设置成0
                    int totalCount = obj.optInt(Config.KEY_TOTAL_COUNT,0);
                    //返回数据的数组
                    JSONArray array = obj.optJSONArray(Config.KEY_DATA);

                    List<T> listData = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        listData.add((T) new Gson().fromJson(array.getJSONObject(i).toString(), model));
                    }
                    //调用接口，在presenter里面实例化
                    handler.onSuccess(status, totalCount, listData);
                    //调用回调中成功
                    if (status == 100 && callBack != null) {
                        callBack.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 状态码、返回的数据列表
     *
     * @param url      网络请求地址
     * @param params   网络返回数据
     * @param model    数据类
     * @param callBack 回调
     * @param handler  消息处理
     */
    public void getDataClient(final String url,
                              final RequestParams params,
                              final Class model,
                              final BaseCallBack callBack,
                              final SampleHttpResponseHandler<T> handler) {
        //框架里面的post请求
        clientUtil.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //调用回调中失败
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject obj = new JSONObject(responseString);
                    //后台返回请求的状态码
                    int status = obj.getInt(Config.KEY_STATUS);
                    //返回数据的数组
                    JSONArray array = obj.getJSONArray(Config.KEY_DATA);
                    List<T> data = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        data.add((T) new Gson().fromJson(array.getJSONArray(i).toString(), model));
                    }
                    //调用接口，在presenter里面实例化
                    handler.onSuccess(status, data);
                    //调用回调中成功
                    if (status == 100 && callBack != null) {
                        callBack.onSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 状态码、请求到并返回的string
     *
     * @param url      网络请求地址
     * @param params   网络返回数据
     * @param callBack 回调
     * @param handler  消息处理
     */
    public void getDataClient(final String url,
                              final RequestParams params,
                              final BaseCallBack callBack,
                              final StringDataResponseHandler handler) {
        //框架里面的post请求
        clientUtil.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //调用回调中失败
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.d("--------------+++",responseString);
                    JSONObject obj = new JSONObject(responseString);
                    //后台返回请求的状态码
                    int status = obj.getInt(Config.KEY_STATUS);
                    //调用接口，在presenter里面实例化
                    handler.onSuccess(status, responseString);
                    //调用回调中成功
                    if (status == 100 && callBack != null) {
                        callBack.onSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 状态码
     *
     * @param url      网络请求地址
     * @param params   网络返回数据
     * @param callBack 回调
     * @param handler  消息处理
     */
    public void getDataClient(final String url,
                              final RequestParams params,
                              final BaseCallBack callBack,
                              final StatusHttpResponseHandler handler) {
        //框架里面的post请求
        clientUtil.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //调用回调中失败
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject obj = new JSONObject(responseString);
                    //后台返回请求的状态码
                    int status = obj.getInt(Config.KEY_STATUS);
                    //调用接口，在presenter里面实例化
                    handler.onSuccess(status);
                    //调用回调中成功
                    if (status == 100 && callBack != null) {
                        callBack.onSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 状态码、请求到并返回的string
     *
     */
 /*   public void getDataClient(final String url,
                              final RequestParams params,
                              final BaseCallBack callBack,
                              final FileDataResponseHandler handler) {
        //框架里面的post请求
        clientUtil.post(url, params, new FileAsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                //调用回调中失败
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    //后台返回请求的状态码
                    int status = obj.getInt(Config.KEY_STATUS);
                    //调用接口，在presenter里面实例化
                    handler.onSuccess(status, response);
                    //调用回调中成功
                    if (status == 100 && callBack != null) {
                        callBack.onSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

*/
    //数据处理的接口
    public interface DataHttpResponseHandler<T> {
        void onSuccess(int status, int totalCount, List<T> modelData);
    }

    public interface SampleHttpResponseHandler<T> {
        void onSuccess(int status, List<T> modelData);
    }

    public interface StringDataResponseHandler {
        void onSuccess(int status, String stringData);
    }

    public interface StatusHttpResponseHandler {
        void onSuccess(int status);
    }

    public interface FileDataResponseHandler {
        void onSuccess(int status, File fileData);
    }

}
