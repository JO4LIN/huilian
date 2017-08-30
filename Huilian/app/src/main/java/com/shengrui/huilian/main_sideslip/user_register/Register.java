package com.shengrui.huilian.main_sideslip.user_register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.MainActivity;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Date;

public class Register extends AutoLayoutActivity implements View.OnClickListener {
    private TextView btn_enterRegister;  //进入注册界面
    private Button btn_enter;          //登陆
    private EditText enter_account;    //登陆用户名
    private EditText enter_passWord;   //登陆密码
    private TextView btn_forgetPassword; //忘记密码
    private RelativeLayout enter_back; //返回键
    public static ProgressDialog pd;
    private String mobile;
    private String password;
    private User_presenter activePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btn_enterRegister = (TextView) findViewById(R.id.btn_enterRegister);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        enter_account = (EditText) findViewById(R.id.enter_account);
        enter_passWord = (EditText) findViewById(R.id.enter_passWord);
        btn_forgetPassword = (TextView) findViewById(R.id.btn_forgetPassword);
        enter_back= (RelativeLayout) findViewById(R.id.enter_back);
        if (activePresenter == null){
            activePresenter = User_presenter.getInstance();
        }

        //监听事件
        enter_back.setOnClickListener(this);
        btn_forgetPassword.setOnClickListener(this);   //忘记密码按钮监听
        btn_enter.setOnClickListener(this);            //登陆按钮监听
        btn_enterRegister.setOnClickListener(this);    //注册按钮监听

        //登陆按钮监听
        btn_enter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断手机号码正确性
                if (!TextUtils.isEmpty(enter_account.getText().toString().trim()))   //判断是否填写手机号
                {
                    if (enter_account.getText().toString().trim().length() == 11)    //判断手机号位数
                    {
                        //判断手机号码是否正确
                        if(!User_register.isMobileNO(enter_account.getText().toString())) {
                            Toast.makeText(Global.context, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else
                    {
                        Toast.makeText(Global.context, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                        enter_account.requestFocus();
                        return;
                    }
                } else
                {
                    Toast.makeText(Global.context, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                    enter_account.requestFocus();
                    return;
                }

                if (enter_passWord.getText().toString().equals(""))   //判断密码是否为空
                {
                    Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mobile = enter_account.getText().toString().trim();
                    password = enter_passWord.getText().toString().trim();
                    Utils.clear();
                    Utils.setCookies();
                    RequestParams params = new RequestParams();
                    params.put("mobile",mobile);
                    params.put("userPassword",password);
                    activePresenter.login(params, new FinishCallBack() {
                        public void onSuccess() {
                            //    progressDialog.dismiss();
                            Global.userId = activePresenter.user_model.getUserId();
                            Toast.makeText(Global.context, "操作成功", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }

                        public void onFailure() {
                            Toast.makeText(Global.context, "操作失败", Toast.LENGTH_SHORT).show();
                            //   progressDialog.dismiss();
                        }
                    });
                }

            }
        });

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        try{
            pd.dismiss();
        }catch (Exception e) {
            System.out.println("myDialog取消，失败！");
            // TODO: handle exception
        }
        super.onDestroy();
    }

    //按钮触发事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_back:
                Register.this.finish();
                break;
            case R.id.btn_enterRegister:    //跳转到注册界面
                startActivity(new Intent(this, User_register.class));
                break;
            case R.id.btn_forgetPassword:   //跳转到忘记密码界面
                startActivity(new Intent(this, Forget_password.class));
            default:
                break;
        }
    }

}
