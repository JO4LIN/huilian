package com.shengrui.huilian.main_sideslip.user_register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.text.ParseException;
import java.util.Date;

/**
 * 用户注册
 */
public class User_register extends AutoLayoutActivity implements View.OnClickListener {

    private EditText reg_phoneNumber= null;    //手机号码
    private EditText reg_idCode= null;          //验证码
    private LinearLayout reg_getIdCode= null;   //获取验证码
    private EditText reg_passWord= null;       //密码
    private EditText reg_rePassWord= null;     //再次输入密码
    private Button reg_btn_agree= null;        //注册
    private CheckBox reg_checkBox= null;       //服务协议
    private RelativeLayout register_back;  //返回键
    private String code;
    Date getCodeTime;
    Date regTime;

    private User_presenter activePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        if (activePresenter == null){
            activePresenter = User_presenter.getInstance();
        }

        register_back= (RelativeLayout) findViewById(R.id.register_back);
        reg_passWord=(EditText)findViewById(R.id.reg_passWord);
        reg_rePassWord=(EditText)findViewById(R.id.reg_rePassWord);
        reg_phoneNumber = (EditText) findViewById(R.id.reg_phoneNumber);
        reg_idCode = (EditText) findViewById(R.id.reg_idCode);
        reg_getIdCode = (LinearLayout) findViewById(R.id.reg_getIdCode);
        reg_btn_agree = (Button) findViewById(R.id.reg_btn_agree);
        reg_checkBox = (CheckBox)findViewById(R.id.reg_checkBox);

        register_back.setOnClickListener(this);


        //获取验证码按键监听
        reg_getIdCode.setOnClickListener(new LinearLayout.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //判断手机号码正确性
                if (!TextUtils.isEmpty(reg_phoneNumber.getText().toString().trim()))   //判断是否填写手机号
                {
                    if (reg_phoneNumber.getText().toString().trim().length() == 11)    //判断手机号位数
                    {
                        //判断手机号码是否正确
                        if(!isMobileNO(reg_phoneNumber.getText().toString())){
                            Toast.makeText(User_register.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        }else {
                            activePresenter.checkMobileRegister(reg_phoneNumber.getText().toString().trim(),
                                    new FinishCallBack(){
                                        @Override
                                        public void onSuccess() {
                                            try {
                                                getCode();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(User_register.this, "请求失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else
                    {
                        Toast.makeText(User_register.this, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                        reg_phoneNumber.requestFocus();
                    }
                } else
                {
                    Toast.makeText(User_register.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                    reg_phoneNumber.requestFocus();
                }
            }
        });

        //注册按钮监听
        reg_btn_agree.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //判断手机号码正确性
                if (!TextUtils.isEmpty(reg_phoneNumber.getText().toString().trim()))   //判断是否填写手机号
                {
                    if (reg_phoneNumber.getText().toString().trim().length() != 11)    //判断手机号位数
                    {
                        Toast.makeText(User_register.this, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                        reg_phoneNumber.requestFocus();
                        return;
                    }
                } else {
                    Toast.makeText(User_register.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                    reg_phoneNumber.requestFocus();
                    return;
                }

                //判断手机号码是否正确
                if(!isMobileNO(reg_phoneNumber.getText().toString())){
                    Toast.makeText(User_register.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断验证码长度是否正确
                if (!TextUtils.isEmpty(reg_idCode.getText().toString().trim())) {
                    if (reg_idCode.getText().toString().trim().length() != 6) {
                        Toast.makeText(User_register.this, "请输入完整验证码", Toast.LENGTH_SHORT).show();
                        reg_idCode.requestFocus();
                        return;
                    }
                } else    //验证码长度为0
                {
                    Toast.makeText(User_register.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    reg_idCode.requestFocus();
                    return;
                }

                //判断是否输入密码
                if (reg_passWord.getText().toString().equals("")) {
                    Toast.makeText(User_register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码位数
                if ((reg_passWord.getText().toString().length() < 6) ||
                        (reg_passWord.getText().toString().length() > 16)) {
                    Toast.makeText(User_register.this, "密码位数为6-16位", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码格式
                char[] chars2 = reg_passWord.getText().toString().toCharArray();
                for (int i = 0; i < chars2.length; i++) {
                    if (chars2[i] == ' ') {
                        Toast.makeText(User_register.this, "密码不能含有空格", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (chars2[i] >= 127) {
                        Toast.makeText(User_register.this, "密码不能含有汉字", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //判断再次输入密码是否为空
                if (reg_rePassWord.getText().toString().equals("")) {
                    Toast.makeText(User_register.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码是否一致
                if (!reg_rePassWord.getText().toString().equals(reg_passWord.getText().toString().trim())) {
                    Toast.makeText(User_register.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断是否勾选《服务协议》
                if (!reg_checkBox.isChecked()) {
                    Toast.makeText(User_register.this, "未勾选《服务协议》", Toast.LENGTH_SHORT).show();
                    return;
                }

                regTime = new Date(System.currentTimeMillis());
                if(getCodeTime==null){
                    Toast.makeText(User_register.this, "请获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                long l=regTime.getTime()-getCodeTime.getTime();
                long day=l/(24*60*60*1000);
                long hour=(l/(60*60*1000)-day*24);
                long min=((l/(60*1000))-day*24*60-hour*60);
                //  long sec=(l/1000-day*24*60*60-hour*60*60-min*60);

                if (day==0&&hour==0&&min<=2) {
                    if (Utils.toMD5(reg_idCode.getLayout().getText().toString().trim()).equals(String.valueOf(code))) {
                        activePresenter.register(reg_phoneNumber.getText().toString().trim(), reg_passWord.getText().toString().trim(),
                                new FinishCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(User_register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(User_register.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                    Toast.makeText(Global.context, "验证码错误", Toast.LENGTH_SHORT).show();
                }
                }else {
                    Toast.makeText(Global.context, "验证码超时", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    //判断手机号码是否正确
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][358]\\d{9}";
        return mobileNums.matches(telRegex);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_back:
                User_register.this.finish();
                break;
        }
    }

    //验证码测试
    public void getCode() throws ParseException {
        RequestParams params = new RequestParams();
        params.put("mobile",reg_phoneNumber.getText().toString().trim());
//        progressDialog = ProgressDialog.show(Global.context,"Loading","Please wait...",true,true);
        activePresenter.code(params, new FinishCallBack() {
            public void onSuccess() {
                //    progressDialog.dismiss();
                code = activePresenter.user_model.getCode();
                Toast.makeText(Global.context, "操作成功", Toast.LENGTH_SHORT).show();
                getCodeTime = new Date(System.currentTimeMillis());
            }

            public void onFailure() {
                Toast.makeText(Global.context, "操作失败", Toast.LENGTH_SHORT).show();
                //   progressDialog.dismiss();
            }
        });
    }

}
