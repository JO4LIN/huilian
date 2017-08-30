package com.shengrui.huilian.medium_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jh on 2016/4/24.
 */
public class MedSkipActivity extends Activity implements View.OnClickListener{
    private EditText med_change = null;    //填写信息栏
    private TextView med_title = null;     //页面标题
    private Button med_save = null;        //保存按键
    private RelativeLayout med_back = null;     //返回图标
    Intent intent=null;                     //用于保存或获取数据（数据传递）
    Bundle bundle=null;                     //用于保存或获取数据（数据传递）
    ProgressDialog progressDialog;
    private int mediaId ;

    private MeduimInforPresanter mMeduimInforPresanter = MeduimInforPresanter.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_skip);
        mMeduimInforPresanter.setContext(MedSkipActivity.this);

        med_change = (EditText) findViewById(R.id.med_change);
        med_title = (TextView) findViewById(R.id.med_title);
        med_save = (Button) findViewById(R.id.med_save);
        med_back = (RelativeLayout) findViewById(R.id.search_back);
        med_back.setOnClickListener(this);

        intent = getIntent();
        bundle = intent.getExtras();                           //获取上一页面所传数据
        med_title.setText(bundle.getString("med_title"));
        mediaId = Global.mediaId;
        //原用户信息填入修改框
        //未填写时显示空
        if(bundle.getString("med_change").equals("未填写")){
            med_change.setText("");
        }else {
            med_change.setText(bundle.getString("med_change"));
            //页面标题修改为相应内容
        }
        inputType();  //确定键盘类型并自动弹出

        //保存按键
        med_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layout layout = med_change.getLayout();
                String message = layout.getText().toString();
                switch (bundle.getInt("med_logic")) {
                    //判断公众号正确性
                    case 2:
                        if (!TextUtils.isEmpty(message)) {
                            //判断是否为数字
                            if (message.matches("^[0-9]*$")) {
                                Toast.makeText(MedSkipActivity.this, "公众号不能为纯数字", Toast.LENGTH_SHORT).show();
                            } else {
                                skip(message, "mediaName",2);
                            }
                        } else {
                            Toast.makeText(MedSkipActivity.this, "请输入公众号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //判断微信号正确性
                    case 3:
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断微信号格式是否正确（只能数字、英文和下划线，且6-16位）
                            if (message.matches("^[a-zA-Z\\d_]{6,16}$")) {
                                skip(message,"wechatNum",3);
                            } else {
                                Toast.makeText(MedSkipActivity.this, "请输入正确的微信号", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MedSkipActivity.this, "请输入微信号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //判断粉丝数正确性
                    case 5:
                        //判空
                        if (!TextUtils.isEmpty(message)) {
                            //须为大于等于0的整数
                            if (message.matches("^\\d+$")) {
                                skip(message,"fansNum",5);
                            } else {
                                Toast.makeText(MedSkipActivity.this, "粉丝数为大于等于0的整数", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MedSkipActivity.this, "请输入粉丝数", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //返回按钮返回上一页面
/*        med_back.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }
    //存入修改内容并跳转页面
        public void skip(Object content,String message,int agency) {
            bundle.putString("med_message", content.toString());   //添加要返回给上一页面的数据
            intent.putExtras(bundle);
       //     progressDialog = ProgressDialog.show(mMeduimInforPresanter.context,"Loading","Please wait...",true,true);
           // SVProgressHUD.showErrorWithStatus(mMeduimInforPresanter.context, "正在保存，请稍后");
            this.mMeduimInforPresanter.loadingMessage(mediaId,content, message, agency, new FinishCallBack() {
                public void onSuccess() {
                    setResult(Activity.RESULT_OK, intent);      //返回页面
                    finish();
             //       progressDialog.dismiss();
                    //    SVProgressHUD.dismiss(mMeduimInforPresanter.context);
                }

                public void onFailure() {
                    setResult(Activity.RESULT_OK);      //返回页面
                    finish();
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
            //        progressDialog.dismiss();
                    //   SVProgressHUD.dismiss(mMeduimInforPresanter.context);
                }
            });

            bundle.clear();
        }

    //确定键盘类型并自动弹出
    private void inputType() {
        switch (bundle.getInt("med_logic")){
            case 2:
                keyboard();
                break;
            case 3:
                keyboard();
                break;
            case 5:
                med_change.setInputType(InputType.TYPE_CLASS_NUMBER);
                keyboard();
                break;
            default:
                break;
        }
    }

    //实现自动弹出软键盘
    private void keyboard() {
        med_change.requestFocus();   //设置焦点
        Timer timer = new Timer();
        //让软键盘延时弹出，以更好的加载Activity
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) med_change.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(med_change, 0);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                MedSkipActivity.this.finish();
        }
    }
}

