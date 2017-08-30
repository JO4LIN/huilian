package com.shengrui.huilian.main_sideslip.setting_feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

import java.util.Date;

/**
 * Created by ChenXQ on 2016/7/19.
 */
public class Setting_feedback extends Activity implements View.OnClickListener{

    private RelativeLayout back;
    private TextView feedback_send;
    private EditText content;
    private EditText contact;
    private Setting_feedback_presenter feedback_presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_feedback);

        init();
    }

    private void init() {
        feedback_presenter = Setting_feedback_presenter.getInstance();
        content = (EditText) findViewById(R.id.content);
        contact = (EditText) findViewById(R.id.contact);
        back= (RelativeLayout) findViewById(R.id.setting_back);
        back.setOnClickListener(this);
        feedback_send = (TextView) findViewById(R.id.feedback_send);
        feedback_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:
                Setting_feedback.this.finish();
                break;
            case R.id.feedback_send:
                RequestParams params = new RequestParams();
                params.put("feedBack.content", content.getText().toString().trim());
                params.put("feedBack.contact",contact.getText().toString().trim());
                feedback_presenter.sendFeedBack(params, new FinishCallBack() {
                    public void onSuccess() {
                        Toast.makeText(Global.context, "发送成功", Toast.LENGTH_SHORT).show();
                        Setting_feedback.this.finish();
                    }

                    public void onFailure() {
                        Toast.makeText(Global.context, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}
