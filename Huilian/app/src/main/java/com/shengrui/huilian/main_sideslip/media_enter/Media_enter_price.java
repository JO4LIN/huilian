package com.shengrui.huilian.main_sideslip.media_enter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.shengrui.huilian.MainActivity;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenXQ on 2016/8/5.
 */
public class Media_enter_price extends Activity implements View.OnClickListener{

    String a="-1";
    private RelativeLayout back;
    private EditText soft_fir_price;
    private EditText soft_sec_price;
    private EditText soft_other_price;
    private EditText soft_simple_price;
    private EditText hard_fir_price;
    private EditText hard_sec_price;
    private EditText hard_other_price;
    private EditText hard_simple_price;
    private TextView media_enter_send;

    private Bundle bundle = null;
    private String mediaName = null;
    private String wechatNum = null;
    private String intro = null;
    private String twoCode = null;
    private String wechatHead = null;
    private String fansNum = null;
    private String sort = null;
    private String readNum = null;
    private String cityName = null;
    private String provinceName = null;
    private String fansPicture = null;
    private String schoolName = null;
    private Media_enter_presenter activePresenter;
    protected Handler mHandler = new Handler();
    private String photoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_enter_price);
        if (activePresenter == null)
            activePresenter = Media_enter_presenter.getInstance();
        init();
        setInfo();
    }

    private void setInfo() {
        bundle=this.getIntent().getExtras();
        mediaName = bundle.getString("mediaName");
        wechatNum = bundle.getString("wechatNum");
        intro = bundle.getString("intro");
        wechatHead = bundle.getString("wechatHead");
        twoCode = bundle.getString("twoCode");
        fansNum = bundle.getString("fansNum");
        sort = bundle.getString("sort");
        readNum = bundle.getString("readNum");
        cityName = bundle.getString("cityName");
        provinceName = bundle.getString("provinceName");
        fansPicture = bundle.getString("fansPicture");
        schoolName = bundle.getString("schoolName");
    }

    private void init() {
        back = (RelativeLayout) findViewById(R.id.back);
        soft_fir_price = (EditText) findViewById(R.id.soft_fir_price);
        soft_sec_price = (EditText) findViewById(R.id.soft_sec_price);
        soft_other_price = (EditText) findViewById(R.id.soft_other_price);
        soft_simple_price = (EditText) findViewById(R.id.soft_simple_price);
        hard_fir_price = (EditText) findViewById(R.id.hard_fir_price);
        hard_sec_price = (EditText) findViewById(R.id.hard_sec_price);
        hard_other_price = (EditText) findViewById(R.id.hard_other_price);
        hard_simple_price = (EditText) findViewById(R.id.hard_simple_price);
        media_enter_send = (TextView) findViewById(R.id.media_enter_send);

        back.setOnClickListener(this);
        media_enter_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Media_enter_price.this.finish();
                break;
            case R.id.media_enter_send:
                if((Integer.parseInt(soft_fir_price.getText().toString())<0)||
                        (Integer.parseInt(soft_sec_price.getText().toString())<0)||
                        (Integer.parseInt(soft_other_price.getText().toString())<0)||
                        (Integer.parseInt(soft_simple_price.getText().toString())<0)||
                        (Integer.parseInt(hard_fir_price.getText().toString())<0)||
                        (Integer.parseInt(hard_sec_price.getText().toString())<0)||
                        (Integer.parseInt(hard_other_price.getText().toString())<0)||
                        (Integer.parseInt(hard_simple_price.getText().toString())<0)){

                    Toast.makeText(Media_enter_price.this, "价格填写不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadData(fansPicture);
                break;
        }
    }

    /**
     * OSS上传图片
     * @param
     */
    private void uploadData(String uploadFilePath){
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        PutObjectRequest put = new PutObjectRequest(Config.OSS_BUCKET,getImageObjectKey("123456789"), uploadFilePath);

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            }
        });

        OSSAsyncTask task = Global.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //如果上传失败了，通过mHandler ，发出失败的消息到主线程中。处理异常。
//							showNetErrorInfo();

                    }
                });
            }
        });

        task.waitUntilFinished();

        String soft_fir_priceQ=soft_fir_price.getText().toString().trim();
        String soft_sec_priceQ=soft_sec_price.getText().toString().trim();
        String soft_other_priceQ=soft_other_price.getText().toString().trim();
        String soft_simple_priceQ=soft_simple_price.getText().toString().trim();
        String hard_fir_priceQ=hard_fir_price.getText().toString().trim();
        String hard_sec_priceQ=hard_sec_price.getText().toString().trim();
        String hard_other_priceQ=hard_other_price.getText().toString().trim();
        String hard_simple_priceQ=hard_simple_price.getText().toString().trim();

        if("".equals(soft_fir_priceQ)){
            soft_fir_priceQ="-1";
        } else if("".equals(soft_sec_priceQ)){
            soft_sec_priceQ="-1";
        } else if("".equals(soft_other_priceQ)){
            soft_other_priceQ="-1";
        } else if("".equals(soft_simple_priceQ)){
            soft_simple_priceQ="-1";
        } else if("".equals(hard_fir_priceQ)){
            hard_fir_priceQ="-1";
        } else if("".equals(hard_sec_priceQ)){
            hard_sec_priceQ="-1";
        } else if("".equals(hard_other_priceQ)){
            hard_other_priceQ="-1";
        } else if("".equals(hard_simple_priceQ)){
            hard_simple_priceQ="-1";
        }
        activePresenter.enterMedia(Global.userId, mediaName, wechatNum, wechatHead, twoCode, Integer.valueOf(fansNum), Integer.valueOf(readNum), intro, cityName, provinceName,
                soft_fir_priceQ, soft_sec_priceQ,
                soft_other_priceQ, soft_simple_priceQ,
                hard_fir_priceQ, hard_sec_priceQ,
                hard_other_priceQ, hard_simple_priceQ, sort,schoolName,photoUrl, new FinishCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(Media_enter_price.this, Media_enter_add.class).putExtra("task","1"));
                        Toast.makeText(Global.context, "入驻成功", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey (String strUserCode){
        Date date = new Date();
        Log.d("+++++++++++++++>", "http://" + Config.OSS_BUCKET + "." + Config.OSS_BUCKET_HOST_ID + "/" + strUserCode + new SimpleDateFormat("yyyyMMddssSSS").format(date) + ".jpg");
//		return new SimpleDateFormat("yyyy/M/d").format(date)+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        photoUrl = "http://"+Config.OSS_BUCKET+"."+Config.OSS_BUCKET_HOST_ID+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        return strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
    }
}
