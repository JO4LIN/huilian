package com.shengrui.huilian.medium_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.main_sideslip.report.Report;
import com.shengrui.huilian.main_sideslip.user_register.Register;
import com.shengrui.huilian.send_indent.SendIndentActivity;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

/**
 * Created by jh on 2016/5/26.
 */
public class MedCheckActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView med_codeTitle= null;          //二维码项

    private ImageView med_mainHead = null;         //公众号头像
    private TextView med_officialAccounts= null;  //公众号
    private TextView med_weixinNumber= null;       //微信号
    private TextView med_followers= null;          //粉丝数
    private TextView med_readNum = null;
    private TextView med_area= null;                //账号分类
    private TextView med_school= null;                //账号分类
    private TextView med_sort= null;                //账号分类
    private TextView med_multiOne= null;           //多图文第一条（软）
    private TextView med_multiOne2= null;          //多图文第一条（硬）
    private TextView med_multiTwo= null;           //多二硬
    private TextView med_multiTwo2= null;          //多二软
    private TextView med_multiOther= null;         //多其他软
    private TextView med_multiOther2= null;        //多其他硬
    private TextView med_single= null;              //单软
    private TextView med_single2= null;             //单硬
    private RelativeLayout med_chat= null;                   //联系
    private RelativeLayout med_indent= null;                   //发送
    private RelativeLayout back=null;
    ProgressDialog progressDialog = null;
    private Bundle bundle=null;
    private int mediaId = 1;
    private int status = 0;
    private RelativeLayout report = null;
    private ImageView collect_pic = null;


    CheckCodePopWin checkCodePopWin = null;
    private MeduimInforPresanter mMeduimInforPresanter = MeduimInforPresanter.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_check);
        mMeduimInforPresanter.setContext(MedCheckActivity.this);
        med_codeTitle = (TextView) findViewById(R.id.med_codeTitle);
        bundle=this.getIntent().getExtras();
        mediaId = Integer.valueOf(bundle.getString("mediaId"));
        Log.d("--------------------",String.valueOf(mediaId));
        initialize();
        refresh();
    }

    private void initialize() {

        report = (RelativeLayout) findViewById(R.id.report);
        report.setOnClickListener(this);

        //收藏控件
        collect_pic = (ImageView) findViewById(R.id.collect_pic);
        collect_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    if(status==0){
                        mMeduimInforPresanter.collectMedia(Global.userId, mediaId, new FinishCallBack() {
                            public void onSuccess() {
                                collect_pic.setBackgroundResource(R.drawable.collect_on);
                                status = 1;
                                Toast.makeText(Global.context, "收藏成功", Toast.LENGTH_SHORT).show();
                            }

                            public void onFailure() {
                                Toast.makeText(Global.context, "收藏失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        mMeduimInforPresanter.delCollectMedia(Global.userId, mediaId, new FinishCallBack() {
                            public void onSuccess() {
                                collect_pic.setBackgroundResource(R.drawable.collect_off);
                                status = 0;
                                Toast.makeText(Global.context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                            }

                            public void onFailure() {
                                Toast.makeText(Global.context, "取消收藏失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });


        checkCodePopWin = new CheckCodePopWin(this);
        med_readNum = (TextView) findViewById(R.id.med_readers);
        med_mainHead = (ImageView) findViewById(R.id.med_mainHead);
        med_officialAccounts = (TextView) findViewById(R.id.med_officialAccounts);
        med_weixinNumber = (TextView) findViewById(R.id.med_weixinNumber);
        med_codeTitle = (TextView) findViewById(R.id.med_codeTitle);
        med_followers = (TextView) findViewById(R.id.med_followers);
        med_area = (TextView) findViewById(R.id.med_area);
        med_school = (TextView) findViewById(R.id.med_school);
        med_sort = (TextView) findViewById(R.id.med_sort);
        med_multiOne = (TextView) findViewById(R.id.med_multiOne);
        med_multiOne2 = (TextView) findViewById(R.id.med_multiOne2);
        med_multiTwo = (TextView) findViewById(R.id.med_multiTwo);
        med_multiTwo2 = (TextView) findViewById(R.id.med_multiTwo2);
        med_multiOther = (TextView) findViewById(R.id.med_multiOther);
        med_multiOther2 = (TextView) findViewById(R.id.med_multiOther2);
        med_single = (TextView) findViewById(R.id.med_single);
        med_single2 = (TextView) findViewById(R.id.med_single2);
        med_indent = (RelativeLayout) findViewById(R.id.med_indent);
        back = (RelativeLayout) findViewById(R.id.back);

        med_codeTitle.setOnClickListener(this);
        med_indent.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void refresh() {
//        progressDialog = ProgressDialog.show(mMeduimInforPresanter.context, "Loading", "Please wait...", true, true);
        mMeduimInforPresanter.refreshAllMessage(Global.userId,mediaId,new FinishCallBack() {
            public void onSuccess() {
                checkCodePopWin.setCode(Utils.returnBitMap(mMeduimInforPresanter.mMeduimInforModel.getTwoCode()));
                med_mainHead.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(mMeduimInforPresanter.mMeduimInforModel.getWechatHead())));
                med_officialAccounts.setText(mMeduimInforPresanter.mMeduimInforModel.getMediaName());
                med_weixinNumber.setText(mMeduimInforPresanter.mMeduimInforModel.getWechatNum());
                med_followers.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getFansNum()));
                med_area.setText(mMeduimInforPresanter.mMeduimInforModel.getCity());
                med_school.setText(mMeduimInforPresanter.mMeduimInforModel.getSchool());
                med_sort.setText(mMeduimInforPresanter.mMeduimInforModel.getMedType());
                mediaId = mMeduimInforPresanter.mMeduimInforModel.getMediaId();
                status = mMeduimInforPresanter.mMeduimInforModel.getJudgeCol();
                if(status==0){
                    collect_pic.setBackgroundResource(R.drawable.collect_off);
                }else {
                    collect_pic.setBackgroundResource(R.drawable.collect_on);
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreFirPrice()).equals("-1")){
                    med_multiOne.setText("不接单");
                }else {
                    med_multiOne.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreFirPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreFirPrice()).equals("-1")){
                    med_multiOne2.setText("不接单");
                }else {
                    med_multiOne2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreFirPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreSecPrice()).equals("-1")){
                    med_multiTwo.setText("不接单");
                }else {
                    med_multiTwo.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreSecPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreSecPrice()).equals("-1")){
                    med_multiTwo2.setText("不接单");
                }else {
                    med_multiTwo2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreSecPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreOtherPrice()).equals("-1")){
                    med_multiOther.setText("不接单");
                }else {
                    med_multiOther.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreOtherPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreOtherPrice()).equals("-1")){
                    med_multiOther2.setText("不接单");
                }else {
                    med_multiOther2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreOtherPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftSimplePrice()).equals("-1")){
                    med_single.setText("不接单");
                }else{
                    med_single.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftSimplePrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardSimplePrice()).equals("-1")){
                    med_single2.setText("不接单");
                }else {
                    med_single2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardSimplePrice()));
                }
                med_readNum.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getReadNum()));

//                progressDialog.dismiss();
            }
            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                finish();
            }
        });
       // med_mainHead.setImageBitmap();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.med_codeTitle:
                checkCodePopWin.checkCode();
                checkCodePopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
                break;
//            case R.id.med_chat:
//                startActivity(new Intent(this,ChatWindow.class).putExtra(EaseConstant.EXTRA_USER_ID,"kefu"));
//                break;
            case R.id.med_indent:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(this, SendIndentActivity.class).putExtra("mediaId", String.valueOf(mediaId)));
                }
                break;
            case R.id.back:
                setResult(Activity.RESULT_OK);
                MedCheckActivity.this.finish();
                break;
            case R.id.report:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(MedCheckActivity.this, Report.class).putExtra("mediaId", mediaId)
                        .putExtra("reportType",1));
                }
                break;
        }
    }

}
