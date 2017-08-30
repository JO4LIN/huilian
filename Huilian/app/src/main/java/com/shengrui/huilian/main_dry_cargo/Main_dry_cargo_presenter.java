package com.shengrui.huilian.main_dry_cargo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.Presenter.BasePresenter;
import com.shengrui.huilian.base_mvp.net.Client;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

import java.util.List;

/**
 * Created by lintiehan on 2016/5/25.
 */
public class Main_dry_cargo_presenter extends BasePresenter<Main_dry_cargo_model> {
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        adapter.setContext(context);
        this.context = context;
    }

    public Context context;
    //数据的总数
    private int totalCount;
    //不能被实例化
    Main_dry_cargo_presenter() {
    }

    //单例
    private static Main_dry_cargo_presenter instance;

    public static Main_dry_cargo_presenter getInstance() {
        if (instance == null) {
            instance = new Main_dry_cargo_presenter();
        }
        return instance;
    }


    @Override
    public void onInit() {
        //捆绑数据
        model = Main_dry_cargo_model.class;
        //捆绑adapter
        //data是父类的网络请求后的list数据
        adapter = new Main_dry_cargo_adapter(data);
    }

    @Override
    public void onRefresh(FinishCallBack callBack) {
        page = 1;
        //清除父类的数据
        data.clear();
        RequestParams params = new RequestParams();
        getData(Config.NewsListURL, params, callBack); //url 需改变
    }



    @Override
    public void onLoadMore(FinishCallBack callBack) {
            RequestParams params = new RequestParams();
            getData(Config.NewsListURL, params, callBack); //url 需改变
    }

    @Override
    protected void handleMessage(int status) {
        switch (status) {
            case Config.STATUS_SUCCESS:
                //调用父类的刷新函数
                adapter.notifyDataSetChanged();
                break;
//            case Config.STATUS_NO_STORE:
//                Toast.makeText(Global.context, "自媒体不存在", Toast.LENGTH_SHORT).show();
//                break;
            case -100:
                Toast.makeText(Global.context, "暂时无数据", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void refresh()
    {
        data.clear();
    }
    protected void getData(String url, RequestParams params, FinishCallBack callBack) {
        //清除父类的数据
        Log.d("-------------", "开始获取数据");
        //发给后台，我现在处于第几页
        params.put(Config.KEY_PAGE_NOW, page);
        //一页有多少数据
        params.put(Config.KEY_PAGE_SIZE, Config.PAGE_SAME_SIZE);


        client.getDataClient(url,
                params,
                model,
                callBack,
                new Client.DataHttpResponseHandler<Main_dry_cargo_model>() {
                    /**
                     * @param status  状态码
                     * @param totalCount  总数
                     * @param modelData    数据列表
                     */
                    @Override
                    public void onSuccess(int status, int totalCount, List<Main_dry_cargo_model> modelData) {
                        if (status == Config.STATUS_SUCCESS) {
                            //数据压进去list
                            data.addAll(modelData);
                            Main_dry_cargo_presenter.this.totalCount = totalCount;
                            page++;
                            //发送空消息，状态码赋值到message.what,并刷新
                            Main_dry_cargo_presenter.this.sendEmptyMessage(status);

                        }
                    }
                });
        super.notifyUpdate();
    }

}
