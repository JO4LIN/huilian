package com.shengrui.huilian.main_sideslip.indent_details.Specific_indent;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.slide_choose.SlideChoose;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jh on 2016/7/23.
 */
public class Specific_indent_details extends AppCompatActivity implements View.OnClickListener {

    private ImageView meidaHead;
    private TextView mediaName;
    private TextView title;
    private TextView intro;
    private TextView price;
    private TextView graphicsTypes;
    private TextView proDate;
    private TextView link;
    private TextView finish;
    private TextView send_back;
    private RelativeLayout mediaInfo;
    private TextView activityTitle;
    private RelativeLayout back;
    ProgressDialog progressDialog = null;
    private Bundle bundle = null;
    private int indentId;
    private int progress;
    private int mediaId;
    private TextView userType;
    private int code;
    Date getCodeTime;
    Date regTime;

    private Specific_indent_details_presenter activePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_indent);

        if (activePresenter == null){
            activePresenter = Specific_indent_details_presenter.getInstance();
        }
        initialize();      //初始化控件
        activityType();
        refreshMedia();
    }

    private void activityType() {
        userType.setText("自媒体");
        activityTitle.setText("订单详情");
        if(progress==3){
            send_back.setVisibility(View.VISIBLE);
        }else if(progress==0){
            finish.setVisibility(View.VISIBLE);
        }
    }

    private void initialize() {
        bundle=this.getIntent().getExtras();
        indentId = Integer.valueOf(bundle.getString("indentId"));
        progress = Integer.valueOf(bundle.getString("progress"));


        back = (RelativeLayout) findViewById(R.id.back);
        meidaHead = (ImageView) findViewById(R.id.mainHead);
        mediaName = (TextView) findViewById(R.id.mainName);
        title = (TextView) findViewById(R.id.title);
        intro = (TextView) findViewById(R.id.intro);
        price = (TextView) findViewById(R.id.price);
        graphicsTypes = (TextView) findViewById(R.id.graphicsTypes);
        proDate = (TextView) findViewById(R.id.proDate);
        link = (TextView) findViewById(R.id.link);
        finish = (TextView) findViewById(R.id.finish);
        send_back = (TextView) findViewById(R.id.send_back);
        mediaInfo = (RelativeLayout) findViewById(R.id.mediaInfo);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        userType = (TextView) findViewById(R.id.userType);

        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        send_back.setOnClickListener(this);
        mediaInfo.setOnClickListener(this);
    }

    private void refreshMedia() {
        activePresenter.refreshIndent(indentId, new FinishCallBack() {
            public void onSuccess() {
                mediaName.setText(activePresenter.indentModel.getMediaName());
                meidaHead.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(activePresenter.indentModel.getWechatHead())));
                title.setText(activePresenter.indentModel.getTitle());
                intro.setText(activePresenter.indentModel.getIntro());
                proDate.setText(activePresenter.indentModel.getProDate());
                link.setText(activePresenter.indentModel.getLink());
                price.setText(String.valueOf(activePresenter.indentModel.getPrice()));
                mediaId =activePresenter.indentModel.getMediaId();
                graphicsTypes.setText(activePresenter.indentModel.getGraphicsTypes());
                Log.d("---------------",String.valueOf(mediaId));
            }

            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        //meidaHead.setImageBitmap();
        //       String[] s = mSendIndentPresenter.refreshReturn();
        //      mediaName.setText(s[0]);
        //      indentNum.setText(s[1]);
        //mediaName.setText(mSendIndentPresenter.mSendIndentModel.getMediaName());
        // indentNum.setText(model.getIndentNum());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish:
                RequestParams paramsfFinish = new RequestParams();
                paramsfFinish.put("progress",1);
                updateProgress(paramsfFinish);
                break;
            case R.id.send_back:
                RequestParams paramsbBack = new RequestParams();
                paramsbBack.put("progress",2);
                updateProgress(paramsbBack);
                break;
            case R.id.mediaInfo:
                startActivity(new Intent(Specific_indent_details.this, MedCheckActivity.class).putExtra("mediaId", String.valueOf(mediaId)));
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void updateProgress(RequestParams params){
        params.put("indentId",indentId);
//        progressDialog = ProgressDialog.show(Global.context,"Loading","Please wait...",true,true);
        activePresenter.progress(params, new FinishCallBack() {
            public void onSuccess() {
                //    progressDialog.dismiss();
                Toast.makeText(Global.context, "操作成功", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }

            public void onFailure() {
                Toast.makeText(Global.context, "更改失败", Toast.LENGTH_SHORT).show();
                //   progressDialog.dismiss();
            }
        });
    }


    //验证码测试
    public void getCode() throws ParseException {
        RequestParams params = new RequestParams();
        params.put("phone","13631254616");
//        progressDialog = ProgressDialog.show(Global.context,"Loading","Please wait...",true,true);
        activePresenter.code(params, new FinishCallBack() {
            public void onSuccess() {
                //    progressDialog.dismiss();
                code = activePresenter.indentModel.getCode();
                Toast.makeText(Global.context, "操作成功", Toast.LENGTH_SHORT).show();
                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                getCodeTime = new Date(System.currentTimeMillis());
            }

            public void onFailure() {
                Toast.makeText(Global.context, "操作失败", Toast.LENGTH_SHORT).show();
                //   progressDialog.dismiss();
            }
        });
    }
}
