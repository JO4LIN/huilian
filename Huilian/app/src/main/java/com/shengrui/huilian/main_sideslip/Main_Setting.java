package com.shengrui.huilian.main_sideslip;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.shengrui.huilian.main_sideslip.setting_feedback.Setting_feedback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by ChenXQ on 2016/7/19.
 */
public class Main_Setting extends Activity implements View.OnClickListener{

    private RelativeLayout back;
    private RelativeLayout feedback;
    private TextView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);

        init();
    }

    private void init() {
        back= (RelativeLayout) findViewById(R.id.setting_back);
        back.setOnClickListener(this);

        feedback= (RelativeLayout) findViewById(R.id.feedback);
        feedback.setOnClickListener(this);

        exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:
                Main_Setting.this.finish();
                break;
            case R.id.feedback:
                startActivity(new Intent(this,Setting_feedback.class));
                break;
            case R.id.exit:
                CustomDialog.Builder builder = new CustomDialog.Builder(Main_Setting.this);
                builder.setMessage("确定退出登录？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Global.userId = 100000000;
                        Utils.clear();
                        XGPushManager.registerPush(Global.context, "*");
                        XGPushManager.unregisterPush(Global.context);
                        dialog.dismiss();
                        setResult(Activity.RESULT_OK);
                        finish();
                        //设置你的操作事项
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();

                break;
        }
    }

}
