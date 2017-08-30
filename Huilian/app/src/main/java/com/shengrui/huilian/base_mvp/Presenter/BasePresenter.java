package com.shengrui.huilian.base_mvp.Presenter;

import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Adapter.BaseDataAdapter;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.Client;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */

public abstract class BasePresenter<T> extends Handler {

    protected BasePresenter() {
        onInit();
    }

    //请求回来的数据
    protected List<T> data = new ArrayList<>();
    protected BaseDataAdapter<T> adapter = null;
    protected Client<T> client = new Client<>();
    protected Class model;

    //当前数据在哪一页
    protected int page = 1;
    //数据的总数
    private int totalCount;

    protected void getData(String url, RequestParams params, FinishCallBack callBack) {
        //发给后台，我现在处于第几页
        params.put(Config.KEY_PAGE_NOW, page);
        //一页有多少数据
        params.put(Config.KEY_PAGE_SIZE, Config.PAGE_SAME_SIZE);
        client.getDataClient(url,
                params,
                model,
                callBack,
                new Client.DataHttpResponseHandler<T>() {
                    /**
                     * @param status  状态码
                     * @param totalCount  总数
                     * @param modelData    数据列表
                     */
                    @Override
                    public void onSuccess(int status, int totalCount, List<T> modelData) {
                        if (status == Config.STATUS_SUCCESS) {
                            //数据压进去list
                            data.addAll(modelData);
                            BasePresenter.this.totalCount = totalCount;
                            page++;
                            //发送空消息，状态码赋值到message.what,并刷新ListView
                            BasePresenter.this.sendEmptyMessage(status);
                        }
                    }
                });
    }

    //在初始化方法中初始化 Model 的Class
    //同时初始化如adapter之类
    public abstract void onInit();

    public abstract void onRefresh(FinishCallBack callBack);

    public abstract void onLoadMore(FinishCallBack callBack);

    //判定数据
    public void bindData(AbsListView v) {
        v.setAdapter(adapter);
    }

    //判断数据是否还能加载
    public boolean canLoadMore() {
        return data != null && data.size() < totalCount;
    }

    //返回数据数量
    public int getDataSize() {
        return data.size();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Config.STATUS_NO_LOGIN:
                Toast.makeText(Global.context, "登陆超时，请重新登陆", Toast.LENGTH_SHORT).show();
                //清楚登陆状态
                Utils.clear();
                break;
            default:
                handleMessage(msg.what);
                break;
        }
    }

    //重载函数
    protected abstract void handleMessage(int status);

    //adapter的动态动态更新ListView
    public void notifyUpdate() {
        adapter.notifyDataSetChanged();
    }
}