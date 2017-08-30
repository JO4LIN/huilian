package com.shengrui.huilian.main_sideslip.report;

import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.Presenter.BasePresenter;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.Client;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

/**
 * Created by ChenXQ on 2016/8/9.
 */
public class Report_presenter extends BasePresenter<Report_model> {

    Report_presenter(){

    }
    //单例
    private static Report_presenter instance;
    public static Report_presenter getInstance() {
        if (instance == null) {
            instance = new Report_presenter();
        }
        return instance;
    }
    public void reportBack(String reportUrl, int reportTypeId, String reportType, String reportIntro,
                           String[] reportEvidence,int reportMan, FinishCallBack callBack){
        RequestParams params = new RequestParams();
        if(reportMan==1){
            params.put("report.repMediaId",reportTypeId);
        }else {
            params.put("report.repUserId",reportTypeId);
        }
        params.put("report.userId", Global.userId);
        params.put("report.reportType",reportType);
        params.put("report.reportIntro",reportIntro);
        params.put("report.reportEvidenceFir",reportEvidence[0]);
        params.put("report.reportEvidenceSec",reportEvidence[1]);
        params.put("report.reportEvidenceThi",reportEvidence[2]);


        this.client.getDataClient(reportUrl, params, callBack, new Client.StringDataResponseHandler() {
            @Override
            public void onSuccess(int status, String stringData) {
                if (status == Config.STATUS_SUCCESS) {
                    Report_presenter.this.sendEmptyMessage(status);
                }
                if (status == 200) {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onInit() {

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
