package com.shengrui.huilian.main_sideslip.report;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shengrui.huilian.MainActivity;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.BaseCallBack;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenXQ on 2016/8/8.
 */
public class Report extends Activity implements View.OnClickListener{

    private TextView rep_eroticism;
    private TextView rep_ad;
    private TextView rep_violence;
    private TextView rep_reaction;
    private TextView rep_copyright;
    private TextView rep_other;
    private Button report;

    private ImageView report_picOne;
    private ImageView report_picTwo;
    private ImageView report_picThree;
    private ImageView report_picAdd;

    private RelativeLayout report_picOneLayout;
    private RelativeLayout report_picTwoLayout;
    private RelativeLayout report_picThreeLayout;

    private int rep_eroticism_status=0;
    private int rep_ad_status=0;
    private int rep_violence_status=0;
    private int rep_reaction_status=0;
    private int rep_copyright_status=0;
    private int rep_other_status=0;

    private String repic_one_path = null;
    private String repic_two_path = null;
    private String repic_three_path = null;
    private int repic_Num=1;
    private ImageView report_picOne_delete;
    private ImageView report_picTwo_delete;
    private ImageView report_picThree_delete;
    private boolean report_picOne_add = false;
    private boolean report_picTwo_add = false;
    private boolean report_picThree_add = false;
    private int reportTypeId;


    private RelativeLayout back;
    private Report_presenter report_presenter;

    private EditText rep_intro;
    private TextView textNum;
    private Uri selectedImage = null;
    private DisplayImageOptions options; // 配置图片加载及显示选项

    protected Handler mHandler = new Handler();

    private String photoUrl = null;
    private String[] pic_URL = new String[3];
    private String saveURL = null;
    private int reportMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        init();
        rep_click();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            textNum.setText(String.valueOf(s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = rep_intro.getSelectionStart();
            editEnd = rep_intro.getSelectionEnd();
            if (temp.length() > 150) {
                Toast.makeText(Report.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                rep_intro.setText(s);
                rep_intro.setSelection(tempSelection);
            }
        }
    };

    private void init() {

        if (report_presenter == null)
        {
            report_presenter= Report_presenter.getInstance();
        }

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Global.getContext()));

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(false) // 加载图片时会在磁盘中加载缓存
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        Bundle bundle = this.getIntent().getExtras();
        reportMan = bundle.getInt("reportType");
        if(reportMan==1){
            reportTypeId = bundle.getInt("mediaId");
            saveURL = Config.ReportMediaURL;
        }else {
            reportTypeId = bundle.getInt("userId");
            saveURL = Config.ReportUserURL;
        }


        rep_eroticism = (TextView) findViewById(R.id.rep_eroticism);
        rep_ad = (TextView) findViewById(R.id.rep_ad);
        rep_violence = (TextView) findViewById(R.id.rep_violence);
        rep_reaction = (TextView) findViewById(R.id.rep_reaction);
        rep_copyright = (TextView) findViewById(R.id.rep_copyright);
        rep_other = (TextView) findViewById(R.id.rep_other);
        rep_intro = (EditText) findViewById(R.id.report_introduce);
        textNum = (TextView) findViewById(R.id.textNum);
        back = (RelativeLayout) findViewById(R.id.back);
        rep_intro.addTextChangedListener(mTextWatcher);
        report = (Button) findViewById(R.id.report);

        report_picOne = (ImageView) findViewById(R.id.report_picOne);
        report_picTwo = (ImageView) findViewById(R.id.report_picTwo);
        report_picThree = (ImageView) findViewById(R.id.report_picThree);
        report_picAdd = (ImageView) findViewById(R.id.report_picAdd);

        report_picOneLayout = (RelativeLayout) findViewById(R.id.report_picOneLayout);
        report_picTwoLayout = (RelativeLayout) findViewById(R.id.report_picTwoLayout);
        report_picThreeLayout = (RelativeLayout) findViewById(R.id.report_picThreeLayout);

        report_picOne_delete = (ImageView) findViewById(R.id.report_picOne_delete);
        report_picTwo_delete = (ImageView) findViewById(R.id.report_picTwo_delete);
        report_picThree_delete = (ImageView) findViewById(R.id.report_picThree_delete);

        report_picOne_delete.setOnClickListener(this);
        report_picTwo_delete.setOnClickListener(this);
        report_picThree_delete.setOnClickListener(this);

        report_picOne.setOnClickListener(this);
        report_picTwo.setOnClickListener(this);
        report_picThree.setOnClickListener(this);
        report_picAdd.setOnClickListener(this);

        report.setOnClickListener(this);

    }

    private void rep_click() {

        back.setOnClickListener(this);

        rep_eroticism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_eroticism_status){
                    case 0:
                        rep_eroticism.setBackgroundResource(R.drawable.report_button_on);
                        rep_eroticism.setTextColor(Color.parseColor("#ffffff"));
                        rep_eroticism_status=1;
                        break;
                    case 1:
                        rep_eroticism.setBackgroundResource(R.drawable.report_button_off);
                        rep_eroticism.setTextColor(Color.parseColor("#949494"));
                        rep_eroticism_status=0;
                        break;
                }
            }
        });
        rep_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_ad_status){
                    case 0:
                        rep_ad.setBackgroundResource(R.drawable.report_button_on);
                        rep_ad.setTextColor(Color.parseColor("#ffffff"));
                        rep_ad_status=1;
                        break;
                    case 1:
                        rep_ad.setBackgroundResource(R.drawable.report_button_off);
                        rep_ad.setTextColor(Color.parseColor("#949494"));
                        rep_ad_status=0;
                        break;
                }
            }
        });
        rep_violence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_violence_status){
                    case 0:
                        rep_violence.setBackgroundResource(R.drawable.report_button_on);
                        rep_violence.setTextColor(Color.parseColor("#ffffff"));
                        rep_violence_status=1;
                        break;
                    case 1:
                        rep_violence.setBackgroundResource(R.drawable.report_button_off);
                        rep_violence.setTextColor(Color.parseColor("#949494"));
                        rep_violence_status=0;
                        break;
                }
            }
        });
        rep_reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_reaction_status){
                    case 0:
                        rep_reaction.setBackgroundResource(R.drawable.report_button_on);
                        rep_reaction.setTextColor(Color.parseColor("#ffffff"));
                        rep_reaction_status=1;
                        break;
                    case 1:
                        rep_reaction.setBackgroundResource(R.drawable.report_button_off);
                        rep_reaction.setTextColor(Color.parseColor("#949494"));
                        rep_reaction_status=0;
                        break;
                }
            }
        });
        rep_copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_copyright_status){
                    case 0:
                        rep_copyright.setBackgroundResource(R.drawable.report_button_on);
                        rep_copyright.setTextColor(Color.parseColor("#ffffff"));
                        rep_copyright_status=1;
                        break;
                    case 1:
                        rep_copyright.setBackgroundResource(R.drawable.report_button_off);
                        rep_copyright.setTextColor(Color.parseColor("#949494"));
                        rep_copyright_status=0;
                        break;
                }
            }
        });
        rep_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rep_other_status){
                    case 0:
                        rep_other.setBackgroundResource(R.drawable.report_button_on);
                        rep_other.setTextColor(Color.parseColor("#ffffff"));
                        rep_other_status=1;
                        break;
                    case 1:
                        rep_other.setBackgroundResource(R.drawable.report_button_off);
                        rep_other.setTextColor(Color.parseColor("#949494"));
                        rep_other_status=0;
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //数据回传失败
        if (resultCode == 0)
            return;

        //返回数据为空
        if (data == null)
            return;

        // 处理结果
        if (requestCode == 3) {
            try{
                selectedImage = data.getData(); //获取系统返回的照片的Uri
                ImageSize mImageSize = new ImageSize(220, 215);
                ImageLoader.getInstance().loadImage(selectedImage.toString(), mImageSize,options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        Date date = new Date();
                        File picture = new File(Environment.getExternalStorageDirectory()+"/"+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg");
                        try {
                            FileOutputStream bos = new FileOutputStream(picture);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                            bos.flush();
                            bos.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(!report_picOne_add){
                            report_picOneLayout.setVisibility(View.VISIBLE);
                            report_picOne.setImageBitmap(bitmap);
                            repic_one_path = picture.getPath();
                            report_picOne_add = true;
                        }else if(!report_picTwo_add){
                            report_picTwoLayout.setVisibility(View.VISIBLE);
                            report_picTwo.setImageBitmap(bitmap);
                            repic_two_path = picture.getPath();
                            report_picTwo_add = true;
                        }else if(!report_picThree_add){
                            report_picThreeLayout.setVisibility(View.VISIBLE);
                            report_picThree.setImageBitmap(bitmap);
                            repic_three_path = picture.getPath();
                            report_picThree_add = true;
                        }
                    }
                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

            }catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.report_picOne:
                break;
            case R.id.report_picTwo:
                break;
            case R.id.report_picThree:
                break;
            case R.id.report_picAdd:
                if(report_picOne_add&&report_picTwo_add&&report_picThree_add){
                    Toast.makeText(Report.this, "最多上传3张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectPic();
                break;
            case R.id.report_picOne_delete:
                report_picOneLayout.setVisibility(View.GONE);
                report_picOne_add = false;
                repic_one_path = null;
                break;
            case R.id.report_picTwo_delete:
                report_picTwoLayout.setVisibility(View.GONE);
                report_picTwo_add = false;
                repic_two_path = null;
                break;
            case R.id.report_picThree_delete:
                report_picThreeLayout.setVisibility(View.GONE);
                report_picThree_add = false;
                repic_three_path = null;
                break;

            case R.id.report:
                mutiUpload();
        }
    }

    private void mutiUpload(){
        String uploadFilePath = null;
        if(repic_Num==1){
            uploadFilePath = repic_one_path;
        }else if(repic_Num==2){
            uploadFilePath = repic_two_path;
        }else if(repic_Num==3){
            uploadFilePath = repic_three_path;
        }

        if(repic_Num>3){
            String reportType = "";
            if(rep_eroticism_status==1) {
                reportType+="色情 ";
            }
            if(rep_ad_status==1) {
                reportType+="广告 ";
            }
            if(rep_violence_status==1) {
                reportType+="暴力 ";
            }
            if(rep_reaction_status==1) {
                reportType+="反动 ";
            }
            if(rep_copyright_status==1) {
                reportType+="版权 ";
            }
            if(rep_other_status==1) {
                reportType+="其他 ";
            }
            if(reportType==""){
                Toast.makeText(Report.this, "没有选择举报类型", Toast.LENGTH_SHORT).show();
                return;
            }
            if(rep_intro.length()<1){
                Toast.makeText(Report.this, "没有填写举报信息", Toast.LENGTH_SHORT).show();
                return;
            }
            report_presenter.reportBack(saveURL, reportTypeId, reportType, rep_intro.getText().toString().trim(),
                    pic_URL, reportMan, new FinishCallBack() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(Report.this, "举报成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Report.this, MainActivity.class));
                            return;
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(Global.context, "举报失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if(uploadFilePath==null){
            repic_Num++;
            if (repic_Num<5){
                mutiUpload();
            }
        }else {
            repic_Num++;
            if (repic_Num<5){
                uploadData(uploadFilePath);
            }
        }
    }

    private void selectPic(){
        //跳转到选择图片（相册或文件管理等，由用户选择）
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        //设置uri以及文件类型
        //MediaStore这个类是android系统提供的一个多媒体数据库，Images.Media代表图片信息，
        //这个Uri代表要查询的数据库名称加上表的名称
        //跳转数据为图片数据库，类型为任何图片类型
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 3);  //带数据回传跳转
    }


    /**
     * OSS上传图片
     * @param
     */
    private void uploadData(String uploadFilePath) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
            }
        });

        PutObjectRequest put = new PutObjectRequest(Config.OSS_BUCKET, getImageObjectKey("123456789"), uploadFilePath);

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            }
        });

        OSSAsyncTask task = Global.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {


            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //如果上传失败了，通过mHandler ，发出失败的消息到主线程中。处理异常。
//							showNetErrorInfo();

                    }
                });
            }
        });

        task.waitUntilFinished();

        pic_URL[repic_Num-2] = photoUrl;
        mutiUpload();
    }

        //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey (String strUserCode){

        Date date = new Date();
        Log.d("+++++++++++++++>", "http://" + Config.OSS_BUCKET + "." + Config.OSS_BUCKET_HOST_ID + "/" + strUserCode + new SimpleDateFormat("yyyyMMddssSSS").format(date) + ".jpg");
//		return new SimpleDateFormat("yyyy/M/d").format(date)+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        photoUrl = "http://"+Config.OSS_BUCKET+"."+Config.OSS_BUCKET_HOST_ID+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        return strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
    }

}
