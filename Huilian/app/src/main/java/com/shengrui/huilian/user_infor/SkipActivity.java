package com.shengrui.huilian.user_infor;

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
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jh on 2016/3/8.
 */
public class SkipActivity extends Activity implements View.OnClickListener{

    private RelativeLayout back = null;
    private EditText per_change = null;    //填写信息栏
    private TextView per_title = null;     //页面标题
    private Button per_save = null;        //保存按键
    private ImageView per_back = null;     //返回图标
    Intent intent=null;                     //用于保存或获取数据（数据传递）
    Bundle bundle=null;                     //用于保存或获取数据（数据传递）
    ProgressDialog progressDialog;
    private int userId ;
    private UserInforPresenter mUserInforPresenter = UserInforPresenter.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        mUserInforPresenter.setContext(SkipActivity.this);

        per_change = (EditText) findViewById(R.id.per_change);
        per_title = (TextView) findViewById(R.id.per_title);
        per_save = (Button) findViewById(R.id.per_save);
        back = (RelativeLayout) findViewById(R.id.search_back);
        back.setOnClickListener(this);

        intent = getIntent();
        bundle = intent.getExtras();                           //获取上一页面所传数据
        per_change.setText(bundle.getString("per_change"));  //原用户信息填入修改框
        per_title.setText(bundle.getString("per_title"));    //页面标题修改为相应内容
        userId = Global.userId;
        inputType();                 //实现设定输入框填写类型以及自动弹出软键盘


        per_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          Intent intentBack = getIntent();
                //         Bundle bundleBack = intentBack.getExtras();
                Layout layout = per_change.getLayout();
                String message = layout.getText().toString();
                //http://www.cnblogs.com/jason-liu-blogs/archive/2012/09/03/2668624.html 正则表达式
                switch (bundle.getInt("per_logic")) {
                    case 2:
                        if (!TextUtils.isEmpty(message.trim())) {
                            skip(message, "userName", 2);     //存入修改内容并跳转页面
                        }else {
                            Toast.makeText(SkipActivity.this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        //判断手机号码正确性
                        if (!TextUtils.isEmpty(message))   //判断是否填写手机号
                        {
                            if (message.length() >= 11)    //判断手机号位数
                            {
                                //判断手机号码是否正确
                                if (!isMobileNO(message) || message.length() > 11) {
                                    Toast.makeText(SkipActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                                } else {
                                    skip(message,"mobile",3);     //存入修改内容并跳转页面
                                }
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入完整电话号码", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        //判断姓名正确性
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断姓名格式是否正确（2-5个汉字）
                            if (message.matches("[\\u4e00-\\u9fa5]{2,5}")) {
                                skip(message,"realName",4);     //存入修改内容并跳转页面，以下同理
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6:
                        //判断固定电话正确性
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断固定电话格式是否正确（匹配形式如 0511-4405222 或 021-87888822）
                            if (message.matches("\\d{3}-\\d{8}|\\d{4}-\\d{7}")) {
                                skip(message,"tel",6);
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入正确的固定电话", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入固定电话", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 7:
                        //判断邮箱正确性
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断邮箱格式是否正确
                            if (message.matches("\\w+([-+.]\\w+)*@[\\w-]{2,35}(?:(?:\\.(?:com|net|org|gov))|(?:\\.[a-z]{2}(?=\\.cn)))?\\.(?:com|cn|mobi|tel|asia|net|org|name|me|tv|cc|hk|biz|info)$")) {
                                skip(message,"email",7);
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 8:
                        //判断QQ号正确性
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断QQ号格式是否正确（腾讯QQ号从10000开始）
                            if (message.matches("[1-9][0-9]{4,}")) {
                                skip(message,"qq",8);
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入正确的QQ号", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入QQ号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 9:
                        //判断微信号正确性
                        //判断是否为空
                        if (!TextUtils.isEmpty(message)) {
                            //判断微信号格式是否正确（只能数字、英文和下划线，且5位以上）
                            if (message.matches("^[a-zA-Z\\d_]{6,}$")) {
                                skip(message,"wechat",9);
                            } else {
                                Toast.makeText(SkipActivity.this, "请输入正确的微信号", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SkipActivity.this, "请输入微信号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;

                }
            }

        });

        //返回按钮返回上一页面
     /*   per_back.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    //实现自动弹出软键盘
    private void keyboard() {
        per_change.requestFocus();   //设置焦点
        Timer timer = new Timer();
        //让软键盘延时弹出，以更好的加载Activity
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) per_change.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(per_change, 0);
            }
        }, 300);
    }

    //  http://m.blog.csdn.net/article/details?id=50094503    设定输入类型
    private void inputType() {
        switch (bundle.getInt("per_logic")){
            case 1:
                //修改手机号码设置为PHONE
                per_change.setInputType(InputType.TYPE_CLASS_PHONE);
                keyboard();
                break;
            case 2:
                //修改姓名设置为PERSON_NAME
                per_change.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                keyboard();
                break;
            case 3:
                //修改固定电话设置为PHONE
                per_change.setInputType(InputType.TYPE_CLASS_PHONE);
                keyboard();
                break;
            case 4:
                //修改邮箱设置为EMAIL_ADDRESS
                per_change.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                keyboard();
                break;
            case 5:
                //修改QQ号设置为NUMBER
                per_change.setInputType(InputType.TYPE_CLASS_NUMBER);
                keyboard();
                break;
            case 6:
                //修改微信号设置为普通文本
                per_change.setInputType(InputType.TYPE_CLASS_TEXT);
                keyboard();
                break;
        }
    }

    //存入修改内容并跳转页面
    public void skip(String message,String messageName,int agency) {
        bundle.putString("per_message", message);   //添加要返回给上一页面的数据
        intent.putExtras(bundle);
   //     progressDialog = ProgressDialog.show(mUserInforPresenter.context,"Loading","Please wait...",true,true);
        mUserInforPresenter.loadingMessage(userId, message, messageName, agency,new FinishCallBack() {
            public void onSuccess() {
                setResult(Activity.RESULT_OK, intent);      //返回页面
                finish();
                //          progressDialog.dismiss();
                //    SVProgressHUD.dismiss(mMeduimInforPresanter.context);
            }

        public void onFailure() {
            setResult(Activity.RESULT_OK);      //返回页面
            finish();
            Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
      //      progressDialog.dismiss();
            //   SVProgressHUD.dismiss(mMeduimInforPresanter.context);
        }
        });
        bundle.clear();
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
            case R.id.search_back:
                SkipActivity.this.finish();
        }
    }
}
