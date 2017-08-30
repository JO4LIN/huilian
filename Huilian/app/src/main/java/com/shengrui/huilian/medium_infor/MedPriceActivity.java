package com.shengrui.huilian.medium_infor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

/**
 * Created by jh on 2016/4/24.
 */
public class MedPriceActivity extends Activity implements View.OnClickListener {

    private RelativeLayout setting_back;
    private TextView med_priceTitle = null;     //页面标题
    private TextView med_priceSave = null;        //保存按键
    private TextView med_acceptTitleSoft = null;    //软广是否接单
//    private TextView med_acceptSoft = null;    //软广是否接单内容
    private EditText med_priceSoft = null;    //软广价格
    private TextView med_acceptTitleHard = null;    //硬广是否接单
//    private TextView med_acceptHard = null;    //硬广是否接单内容
    private EditText med_priceHard = null;    //硬广价格
    Intent intent=null;                     //用于保存或获取数据（数据传递）
    Bundle bundle=null;                     //用于保存或获取数据（数据传递）
    String uploadTitle=null;
    private String stringDataSoft=null;
    private String stringDataHard=null;
    private String softTitle;
    private String hardTitle;
    private int agency;
    ProgressDialog progressDialog;
    private int mediaId ;

    private ImageView med_acceptSoft;    //软广是否接单内容
    private ImageView med_acceptHard;    //硬广是否接单内容
    private int status_med_acceptSoft = 1;
    private int status_med_acceptHard = 1;
    private MeduimInforPresanter mMeduimInforPresanter = MeduimInforPresanter.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_price);
        mMeduimInforPresanter.setContext(MedPriceActivity.this);
        initialize();      //初始化控件
        control();         //设置触发事件

        intent = getIntent();
        bundle = intent.getExtras();
        uploadTitle = bundle.getString("uploadTitle");
        mediaId = bundle.getInt("mediaId");
        title(uploadTitle);
        med_priceTitle.setText(bundle.getString("med_priceTitle"));  //标题显示广告类型
        //不接单时，是否接单显示否，未填写时，价格显示空，其余显示原价格
        if (!bundle.getString("med_priceSoft").equals("不接单")) {
            if(bundle.getString("med_priceSoft").equals("未填写")){
                med_priceSoft.setText("");
            }else {
                med_priceSoft.setText(bundle.getString("med_priceSoft"));
            }
        } else {
//            med_acceptSoft.setChecked(false);
            status_med_acceptSoft=0;
            med_acceptSoft.setBackgroundResource(R.drawable.price_turn_off);
            med_priceSoft.setEnabled(false);
        }
        if (!bundle.getString("med_priceHard").equals("不接单")) {
            if(bundle.getString("med_priceHard").equals("未填写")){
                med_priceHard.setText("");
            }else {
                med_priceHard.setText(bundle.getString("med_priceHard"));
            }
        } else {
            status_med_acceptHard = 0;
            med_acceptHard.setBackgroundResource(R.drawable.price_turn_off);
            med_priceHard.setEnabled(false);
        }
    }

    private void title(String uploadTitle) {
        if(uploadTitle.equals("多图文第一条")){
            softTitle = "softMoreFirPrice";
            hardTitle = "hardMoreFirPrice";
            agency = 11;
        }else if (uploadTitle.equals("多图文第二条")){
            softTitle = "softMoreSecPrice";
            hardTitle = "hardMoreSecPrice";
            agency = 12;
        }else if (uploadTitle.equals("多图文其他位置")){
            softTitle = "softMoreOtherPrice";
            hardTitle = "hardMoreOtherPrice";
            agency = 13;
        }else {
            softTitle = "softSimplePrice";
            hardTitle = "hardSimplePrice";
            agency = 14;
        }
    }

    //设置触发事件
    private void control() {
        med_priceSave.setOnClickListener(this);
//        med_acceptTitleSoft.setOnClickListener(this);
//        med_acceptTitleHard.setOnClickListener(this);
        setting_back.setOnClickListener(this);
    }

    //初始化控件
    private void initialize() {
        setting_back=(RelativeLayout)findViewById(R.id.setting_back);
        med_priceSave= (TextView) findViewById(R.id.med_priceSave);
        med_priceTitle= (TextView) findViewById(R.id.med_priceTitle);
        med_priceSoft= (EditText) findViewById(R.id.med_priceSoft);
        med_priceHard= (EditText) findViewById(R.id.med_priceHard);
        med_acceptSoft= (ImageView) findViewById(R.id.med_acceptSoft);
        med_acceptHard= (ImageView) findViewById(R.id.med_acceptHard);


        med_acceptSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status_med_acceptSoft){
                    case 0:
                        med_acceptSoft.setBackgroundResource(R.drawable.price_turn_on);
                        med_priceSoft.setEnabled(true);
                        status_med_acceptSoft = 1;
                        break;
                    case 1:
                        med_acceptSoft.setBackgroundResource(R.drawable.price_turn_off);
                        med_priceSoft.setText("");
                        med_priceSoft.setEnabled(false);
                        status_med_acceptSoft = 0;
                        break;
                }
            }
        });
        med_acceptHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status_med_acceptHard) {
                    case 0:
                        med_acceptHard.setBackgroundResource(R.drawable.price_turn_on);
                        med_priceHard.setEnabled(true);
                        status_med_acceptHard = 1;
                        break;
                    case 1:
                        med_acceptHard.setBackgroundResource(R.drawable.price_turn_off);
                        med_priceHard.setText("");
                        med_priceHard.setEnabled(false);
                        status_med_acceptHard = 0;
                        break;
                }
            }
        });
    }

    private void skip(String stringDataSoft,String stringDataHard) {
        intent.putExtras(bundle);
     //   progressDialog = ProgressDialog.show(mMeduimInforPresanter.context,"Loading","Please wait...",true,true);
        this.mMeduimInforPresanter.loadingPrice(mediaId,stringDataSoft, softTitle,
                stringDataHard, hardTitle,agency, new FinishCallBack() {
                    public void onSuccess() {
                        setResult(Activity.RESULT_OK, intent);      //返回页面
                        finish();
               //         progressDialog.dismiss();
                    }
                    public void onFailure() {
                        setResult(Activity.RESULT_OK);      //返回页面
                        finish();
                        Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                //        progressDialog.dismiss();
                    }
                });
        bundle.clear();
    }
   /* private void skip(String message,String messageName) {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(false);  // 设置是否可以通过点击Back键取消
        progress.setCanceledOnTouchOutside(false); // 设置在点击Dialog外是否取消Dialog进度条
        progress.setMessage("保存中...");
        progress.show()
        //     progress.show(this, null, "保存中...");
        //  progress.setMessage("保存中...");
        uploadMessage mText = new uploadMessage();
        int tip = mText.uploadText(message, messageName);
        if (tip==0){
            progress.dismiss();
           // Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "没有获取到网络的响应", Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }

    }*/





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:
                MedPriceActivity.this.finish();
                break;
            //保存按键
            case R.id.med_priceSave:
                Layout layoutS = med_priceSoft.getLayout();
                String stringS = layoutS.getText().toString();
                Layout layoutH = med_priceHard.getLayout();
                String stringH = layoutH.getText().toString();
                //软广
                //如果可接单，判断价格是否为空，不为空是回传数据，不可接单时回传回不接单

                if(status_med_acceptSoft==1){
                    if(!TextUtils.isEmpty(stringS)){
                        bundle.putString("med_priceSoft", stringS);
                        stringDataSoft = stringS;
                        //  skip(stringS, "priceSoft"+uploadTitle);
                    }else {
                        Toast.makeText(MedPriceActivity.this, "报价不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }else {
                    bundle.putString("med_priceSoft", "不接单");
                    stringDataSoft = "不接单";
                }
                //硬广
                //如果可接单，判断价格是否为空，不为空是回传数据，不可接单时回传回不接单
                if(status_med_acceptHard==1){
                    if(!TextUtils.isEmpty(stringH)){
                        bundle.putString("med_priceHard", stringH);
                        stringDataHard = stringH;
                        skip(stringDataSoft, stringDataHard);
                        break;
                    } else {
                        Toast.makeText(MedPriceActivity.this, "报价不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }else {
                    bundle.putString("med_priceHard", "不接单");
                    stringDataHard = "不接单";
                    skip(stringDataSoft, stringDataHard);
                    break;
                }
                //返回键
        /*    case R.id.med_priceBack:
                finish();
                break;*/
            default:
                break;

        }
    }

}

