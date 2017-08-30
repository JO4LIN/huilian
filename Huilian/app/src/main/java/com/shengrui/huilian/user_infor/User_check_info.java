package com.shengrui.huilian.user_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.shengrui.huilian.main_sideslip.report.Report;

/**
 * Created by jh on 2016/7/23.
 */
public class User_check_info  extends AppCompatActivity implements View.OnClickListener {

    private ImageView per_headPortrait = null;    //头像
    private TextView per_userName = null;         //用户名
    private TextView per_phoneNumber = null;     //手机号码
    private TextView per_trueName = null;         //姓名
    private TextView per_sex = null;              //性别
    private TextView per_telephone = null;        //固定电话
    private TextView per_mailbox = null;          //邮箱地址
    private TextView per_qqNumber = null;         //QQ号
    private TextView per_wechatNumber = null;    //微信号
    private RelativeLayout back=null;            //返回键
    Intent intent = null;                           //用于保存或获取数据（数据传递）
    private TextView thisTextView = null;        //当前更改信息对应的TextView控件
    private UserInforPresenter mUserInforPresenter = UserInforPresenter.getInstance();
    Bundle bundle = null;
    private int userId;
    private int status = 0;
    private ImageView collect_user;
    private RelativeLayout report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_information_check);
        mUserInforPresenter.setContext(User_check_info.this);

        initialize();      //初始化控件
        control();         //设置触发事件
        refresh();
        //mUserInforPresenter.refreshAllMessage(1);
    }


    //初始化控件
    private void initialize() {
        bundle=this.getIntent().getExtras();
        userId = Integer.valueOf(bundle.getString("userId"));
        back = (RelativeLayout) findViewById(R.id.back);
        per_userName= (TextView) findViewById(R.id.per_userName);
        per_phoneNumber= (TextView) findViewById(R.id.per_phoneNumber);
        per_trueName= (TextView) findViewById(R.id.per_trueName);
        per_sex= (TextView) findViewById(R.id.per_sex);
        per_telephone= (TextView) findViewById(R.id.per_telephone);
        per_mailbox= (TextView) findViewById(R.id.per_mailbox);
        per_qqNumber= (TextView) findViewById(R.id.per_qqNumber);
        per_wechatNumber=(TextView)findViewById(R.id.per_wechatNumber);
        per_headPortrait=(ImageView)findViewById(R.id.per_headPortrait);
        report = (RelativeLayout) findViewById(R.id.report);
        report.setOnClickListener(this);
        collect_user= (ImageView) findViewById(R.id.collect_user);

        collect_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0){
                    mUserInforPresenter.collectUser(Global.userId,userId,new FinishCallBack(){
                        public void onSuccess() {
                            collect_user.setBackgroundResource(R.drawable.collect_on);
                            status=1;
                            Toast.makeText(Global.context,"收藏成功",Toast.LENGTH_SHORT).show();
                        }
                        public void onFailure() {
                            Toast.makeText(Global.context,"收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    mUserInforPresenter.delCollectUser(Global.userId, userId, new FinishCallBack() {
                        public void onSuccess() {
                            collect_user.setBackgroundResource(R.drawable.collect_off);
                            status = 0;
                            Toast.makeText(Global.context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        }

                        public void onFailure() {
                            Toast.makeText(Global.context, "取消收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //设置触发事件
    private void control() {
        per_userName.setClickable(false);
        per_phoneNumber.setClickable(false);
        per_trueName.setClickable(false);
        per_sex.setClickable(false);
        per_telephone.setClickable(false);
        per_mailbox.setClickable(false);
        per_qqNumber.setClickable(false);
        per_wechatNumber.setClickable(false);
        per_headPortrait.setClickable(false);
        back.setOnClickListener(this);
    }

    private void refresh() {
        mUserInforPresenter.refreshAllMessage(userId,Global.userId, new FinishCallBack() {
            public void onSuccess() {
                per_headPortrait.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(mUserInforPresenter.mUserInforModel.getUserHead())));
                per_userName.setText(mUserInforPresenter.mUserInforModel.getUserName());
                per_phoneNumber.setText(mUserInforPresenter.mUserInforModel.getMobile());
                per_trueName.setText(mUserInforPresenter.mUserInforModel.getRealName());
                per_sex.setText(mUserInforPresenter.mUserInforModel.getSex());
                per_telephone.setText(mUserInforPresenter.mUserInforModel.getTel());
                per_mailbox.setText(mUserInforPresenter.mUserInforModel.getEmail());
                per_qqNumber.setText(mUserInforPresenter.mUserInforModel.getQq());
                per_wechatNumber.setText(mUserInforPresenter.mUserInforModel.getWechat());
                userId = mUserInforPresenter.mUserInforModel.getUserId();
                status = mUserInforPresenter.mUserInforModel.getJudgeCol();
                if(status==0){
                    collect_user.setBackgroundResource(R.drawable.collect_off);
                }else {
                    collect_user.setBackgroundResource(R.drawable.collect_on);
                }
            }

            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back:
                setResult(Activity.RESULT_OK);
                User_check_info.this.finish();
                break;
            case R.id.report:
                startActivity(new Intent(User_check_info.this, Report.class));
                break;
        }
    }
}
