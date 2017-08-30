package com.shengrui.huilian.main_sideslip.media_enter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.main_sideslip.indent_details.Specific_indent.Specific_indent_details;

import java.util.Date;
import java.util.Random;

/**
 * Created by jh on 2016/8/5.
 */
public class Media_enter_code extends Activity implements View.OnClickListener {


    private RelativeLayout back;
    private Button reget_code;
    private EditText enter_code;
    private EditText enter_url;
    private Button next;
    private Media_enter_presenter activePresenter;
    private Date getCodeTime;
    private Date sendTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_enter);
        if (activePresenter == null)
            activePresenter = Media_enter_presenter.getInstance();
        init();
    }

    private void init() {
        back = (RelativeLayout) findViewById(R.id.back);
        reget_code = (Button) findViewById(R.id.reget_code);
        enter_code = (EditText) findViewById(R.id.enter_code);
        enter_url = (EditText) findViewById(R.id.enter_url);
        next = (Button) findViewById(R.id.next);

        enter_code.setText(getCode(20));
        getCodeTime = new Date(System.currentTimeMillis());
        back.setOnClickListener(this);
        reget_code.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.reget_code:
                enter_code.setText(getCode(20));
                getCodeTime = new Date(System.currentTimeMillis());
                break;
            case R.id.next:
                sendTime = new Date(System.currentTimeMillis());
                long l=sendTime.getTime()-getCodeTime.getTime();
                long day=l/(24*60*60*1000);
                long hour=(l/(60*60*1000)-day*24);
                long min=((l/(60*1000))-day*24*60-hour*60);
                if (day==0&&hour==0&&min<=15) {
                    activePresenter.enterCode(enter_code.getText().toString().trim(), enter_url.getText().toString().trim(), new FinishCallBack() {
                        @Override
                        public void onSuccess() {
                            String mediaName = activePresenter.media_enter_model.getMediaName();
                            String wechatNum = activePresenter.media_enter_model.getWechatNum();
                            String intro = activePresenter.media_enter_model.getIntro();
                            String twoCode = activePresenter.media_enter_model.getTwoCode();
                            String wechatHead = activePresenter.media_enter_model.getWechatHead();
                            startActivity(new Intent(Media_enter_code.this, Media_enter_info.class).putExtra("mediaName", mediaName)
                                    .putExtra("wechatNum", wechatNum).putExtra("intro", intro)
                                    .putExtra("twoCode", twoCode).putExtra("wechatHead", wechatHead));
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(Global.context, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(Global.context, "验证码超时", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public String getCode(int length){

        String val = "";
        Random random = new Random();
        for(int i = 0; i < length; i++)
        {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                val += (char) (97 + random.nextInt(26));
            }
            else if("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
