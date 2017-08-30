package com.shengrui.huilian.main_sideslip.media_enter;

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
public class Media_enter_add_presenter extends BasePresenter<Media_enter_add_model> {
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
    Media_enter_add_presenter() {
    }

    //单例
    private static Media_enter_add_presenter instance;

    public static Media_enter_add_presenter getInstance() {
        if (instance == null) {
            instance = new Media_enter_add_presenter();
        }
        return instance;
    }


    @Override
    public void onInit() {
        //捆绑数据
        model = Media_enter_add_model.class;
        //捆绑adapter
        //data是父类的网络请求后的list数据
        adapter = new Media_enter_add_adapter(data);
    }

    @Override
    public void onRefresh(FinishCallBack callBack) {
        page = 1;
        //清除父类的数据
        data.clear();
        RequestParams params = new RequestParams();
        params.put("userId",Global.userId);
        getData(Config.EnterMediaListURL, params, callBack); //url 需改变
    }


    @Override
    public void onLoadMore(FinishCallBack callBack) {
            RequestParams params = new RequestParams();
            getData(Config.EnterMediaListURL, params, callBack); //url 需改变
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
    protected void getData(String url, RequestParams params, FinishCallBack callBack,int agency) {
        if (agency==1){
            page = 1;
            data.clear();
        }
        //清除父类的数据
        Log.d("-------------", "开始获取数据");
        //发给后台，我现在处于第几页
        params.put(Config.KEY_PAGE_NOW, page);
        //一页有多少数据
        params.put("KEY_PAGE_SIZE", Config.PAGE_SAME_SIZE);


        client.getDataClient(url,
                params,
                model,
                callBack,
                new Client.DataHttpResponseHandler<Media_enter_add_model>() {
                    /**
                     * @param status  状态码
                     * @param totalCount  总数
                     * @param modelData    数据列表
                     */
                    @Override
                    public void onSuccess(int status, int totalCount, List<Media_enter_add_model> modelData) {
                        if (status == Config.STATUS_SUCCESS) {
                            //数据压进去list
                            data.addAll(modelData);
                            Media_enter_add_presenter.this.totalCount = totalCount;
                            page++;
                            //发送空消息，状态码赋值到message.what,并刷新
                            Media_enter_add_presenter.this.sendEmptyMessage(status);

                        }
                    }
                });
        super.notifyUpdate();
    }

}
