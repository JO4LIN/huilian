package com.shengrui.huilian.main_sideslip.user_register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.text.ParseException;
import java.util.Date;


/**
 * Created by chenXQ on 2016/1/25.
 */
public class Forget_password extends AutoLayoutActivity implements View.OnClickListener{
    private EditText fgt_phoneNumber;    //手机号码
    private EditText fgt_idCode;         //验证码
    private LinearLayout fgt_getIdCode;        //发送验证码
    private Button btn_forgetEnter;      //确定修改
    private TextView m_fgt_now;          //验证码倒计时
    private EditText fgt_newPassWord;    //输入新密码
    private EditText fgt_reNewPassWord;  //重新输入新密码
    private RelativeLayout forgetpassword_back; //返回键

    private String m_phoneNumber;
    private String m_idCord;
    private int m_time = 60;                //倒计时
    private boolean m_flag = true;         //判断验证码
    private String code;
    Date getCodeTime;
    Date regTime;
    private User_presenter activePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        if (activePresenter == null){
            activePresenter = User_presenter.getInstance();
        }

        forgetpassword_back = (RelativeLayout) findViewById(R.id.forgetpassword_back);
        fgt_phoneNumber = (EditText) findViewById(R.id.fgt_phoneNumber);
        btn_forgetEnter = (Button) findViewById(R.id.btn_forgetEnter);
        fgt_newPassWord = (EditText) findViewById(R.id.fgt_newPassWord);
        fgt_reNewPassWord = (EditText) findViewById(R.id.fgt_reNewPassWord);
        fgt_getIdCode = (LinearLayout) findViewById(R.id.getCode);
        fgt_idCode = (EditText) findViewById(R.id.code);

        forgetpassword_back.setOnClickListener(this);

        //修改密码按钮监听
        btn_forgetEnter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断手机号
                if (!TextUtils.isEmpty(fgt_phoneNumber.getText().toString().trim()))   //判断是否填写手机号
                {
                    if (fgt_phoneNumber.getText().toString().trim().length() != 11)    //判断手机号位数
                    {
                        Toast.makeText(Forget_password.this, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                        fgt_phoneNumber.requestFocus();
                        return;
                    }
                } else {
                    Toast.makeText(Forget_password.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                    fgt_phoneNumber.requestFocus();
                    return;
                }

                //判断手机号码是否正确
                if (!isMobileNO(fgt_phoneNumber.getText().toString())) {
                    Toast.makeText(Forget_password.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }


                //判断验证码长度是否正确
                if (!TextUtils.isEmpty(fgt_idCode.getText().toString().trim())) {
                    if (fgt_idCode.getText().toString().trim().length() != 6) {
                        Toast.makeText(Forget_password.this, "请输入完整验证码", Toast.LENGTH_SHORT).show();
                        fgt_idCode.requestFocus();
                        return;
                    }
                } else    //验证码长度为0
                {
                    Toast.makeText(Forget_password.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    fgt_idCode.requestFocus();
                    return;
                }

                //判断是否输入密码
                if (fgt_newPassWord.getText().toString().equals("")) {
                    Toast.makeText(Forget_password.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码位数
                if ((fgt_newPassWord.getText().toString().length() < 6) ||
                        (fgt_newPassWord.getText().toString().length() > 16)) {
                    Toast.makeText(Forget_password.this, "密码位数为6-16位", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码格式
                char[] chars2 = fgt_newPassWord.getText().toString().toCharArray();
                for (int i = 0; i < chars2.length; i++) {
                    if (chars2[i] == ' ') {
                        Toast.makeText(Forget_password.this, "密码不能含有空格", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (chars2[i] >= 127) {
                        Toast.makeText(Forget_password.this, "密码不能含有汉字", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //判断再次输入密码是否为空
                if (fgt_reNewPassWord.getText().toString().equals("")) {
                    Toast.makeText(Forget_password.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码是否一致
                if (!fgt_reNewPassWord.getText().toString().equals(fgt_newPassWord.getText().toString().trim())) {
                    Toast.makeText(Forget_password.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                regTime = new Date(System.currentTimeMillis());
                if(getCodeTime==null){
                    Toast.makeText(Forget_password.this, "请获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                long l=regTime.getTime()-getCodeTime.getTime();
                long day=l/(24*60*60*1000);
                long hour=(l/(60*60*1000)-day*24);
                long min=((l/(60*1000))-day*24*60-hour*60);
                //  long sec=(l/1000-day*24*60*60-hour*60*60-min*60);

                if (day==0&&hour==0&&min<=2) {
                    if (Utils.toMD5(fgt_idCode.getLayout().getText().toString().trim()).equals(String.valueOf(code))) {
                        activePresenter.forgetPassword(fgt_phoneNumber.getText().toString().trim(), fgt_newPassWord.getText().toString().trim(),
                                new FinishCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(Global.context, "更改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(Global.context, "更改失败", Toast.LENGTH_SHORT).show();
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

        //获取验证码按键监听
        fgt_getIdCode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断手机号码正确性
                if (!TextUtils.isEmpty(fgt_phoneNumber.getText().toString().trim()))   //判断是否填写手机号
                {
                    if (fgt_phoneNumber.getText().toString().trim().length() == 11)    //判断手机号位数
                    {
                        //判断手机号码是否正确
                        if (!isMobileNO(fgt_phoneNumber.getText().toString())) {
                            Toast.makeText(Forget_password.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        } else {
                            m_phoneNumber = fgt_phoneNumber.getText().toString().trim();

                            activePresenter.checkMobileForget(fgt_phoneNumber.getText().toString().trim(),
                                    new FinishCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            try {
                                                getCode();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            fgt_idCode.requestFocus();
                                            fgt_getIdCode.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(Forget_password.this, "请求失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    } else {
                        Toast.makeText(Forget_password.this, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                        fgt_phoneNumber.requestFocus();
                    }
                } else {
                    Toast.makeText(Forget_password.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                    fgt_phoneNumber.requestFocus();
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

    //验证码测试
    public void getCode() throws ParseException {
        RequestParams params = new RequestParams();
        params.put("mobile",fgt_phoneNumber.getText().toString().trim());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetpassword_back:
                Forget_password.this.finish();
                break;
        }
    }
}
