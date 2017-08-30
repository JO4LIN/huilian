package com.shengrui.huilian.user_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.medium_infor.MyOptionsPickerView;
import com.shengrui.huilian.medium_infor.TakePhotoPopWin;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jh on 2016/5/14.
 */
public class InformationActivity  extends AppCompatActivity implements View.OnClickListener {
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
    String title = null;                           //更改信息页面标题
    private TextView thisTextView = null;        //当前更改信息对应的TextView控件
    Bitmap photoFinal = null;
    ProgressDialog progressDialog;
    private boolean upPhoto = false;

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    TakePhotoPopWin takePhotoPopWin = null;
    private int userId;
    protected Handler mHandler = new Handler();

    //输出的类型可以是imgae的任何类型，包括jpg/bmp/gif等等
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private MyOptionsPickerView<String> mOpv;
    private String[] listSex;
    private String sexText;
    private ArrayList<String> mListSex = new ArrayList<String>();
    private UserInforPresenter mUserInforPresenter = UserInforPresenter.getInstance();
    private String photoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_information);
        mUserInforPresenter.setContext(InformationActivity.this);

        initialize();      //初始化控件
        control();         //设置触发事件
        text();            //初始化TextView控件内容
        refresh();
    }

    private void refresh() {
        progressDialog = ProgressDialog.show(mUserInforPresenter.context,"Loading","Please wait...",true,true);
        mUserInforPresenter.refreshAllMessage(userId,userId, new FinishCallBack() {
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
                progressDialog.dismiss();
            }

            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        });

    }

    //初始化TextView控件内容
    private void text() {
        per_userName.setText("  ");
        per_phoneNumber.setText("   ");
        per_trueName.setText("   ");
        per_sex.setText("  ");
        per_telephone.setText("  ");
        per_mailbox.setText("  ");
        per_qqNumber.setText("  ");
        per_wechatNumber.setText("  ");
    }

    //页面跳转并携带数据
    private void data(String title,TextView textview,int logicNumber) {
        Layout layout=textview.getLayout();
        intent = new Intent();
        intent.setClass(InformationActivity.this, SkipActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("per_change", layout.getText().toString());
        bundle.putString("per_title", title);
        bundle.putInt("per_logic", logicNumber);
        intent.putExtras(bundle);//将Bundle添加到Intent
        startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值
    }

    //数据回传处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();     //获取数据
                thisTextView.setText(bundle.getString("per_message"));  //控件内容修改为最新内容
                return;
            }
        }

        //数据回传失败
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            startPhotoZoom(Uri.fromFile(picture));
        }
        //返回数据为空
        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            Uri url = data.getData();
            Log.d("-----------", url.toString());
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();  //获取所压数据
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");  //接收返回的图像数据
             //   photoFinal = Utils.toRoundBitmap(photo);
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                try {
                    FileOutputStream bos = new FileOutputStream(picture);
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    bos.flush();
                    bos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                takePhotoPopWin.dismiss();     //退出提示框
                Log.d("------------------path",picture.getPath());
                uploadData(picture.getPath());
            }
        }
    }

    //设置触发事件
    private void control() {
        per_userName.setOnClickListener(this);
        per_phoneNumber.setOnClickListener(this);
        per_trueName.setOnClickListener(this);
        per_sex.setOnClickListener(this);
        per_telephone.setOnClickListener(this);
        per_mailbox.setOnClickListener(this);
        per_qqNumber.setOnClickListener(this);
        per_wechatNumber.setOnClickListener(this);
        per_headPortrait.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    //初始化控件
    private void initialize() {
        Bundle bundle=this.getIntent().getExtras();
        userId = Global.userId;
        takePhotoPopWin = new TakePhotoPopWin(this);
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
        pickView();
    }

    //点击触发事件
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                InformationActivity.this.finish();
                break;
            case R.id.per_headPortrait:
                takePhotoPopWin.TakePhoto(onClickListener);
                takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
                break;
            case R.id.per_userName:
                title = "更改用户名";
                thisTextView = per_userName;  //更改当前TextView
                data(title, per_userName, 2);   //第一个参数为更改页面标题，第二个为当前点击控件，第三个为当前点击控件代号
                break;
            case R.id.per_phoneNumber:
                title = "更改手机号码";
                thisTextView = per_phoneNumber;  //更改当前TextView
                data(title, per_phoneNumber,3);   //第一个参数为更改页面标题，第二个为当前点击控件，第三个为当前点击控件代号
                break;                               //以下同理
            case R.id.per_trueName:
                title = "更改姓名";
                thisTextView = per_trueName;
                data(title,per_trueName,4);
                break;
            case R.id.per_telephone:
                title = "更改固定电话";
                thisTextView = per_telephone;
                data(title,per_telephone,6);
                break;
            case R.id.per_mailbox:
                title = "更改邮箱";
                thisTextView = per_mailbox;
                data(title,per_mailbox,7);
                break;
            case R.id.per_qqNumber:
                title = "更改QQ号";
                thisTextView = per_qqNumber;
                data(title,per_qqNumber,8);
                break;
            case R.id.per_wechatNumber:
                title = "更改微信号";
                thisTextView = per_wechatNumber;
                data(title,per_wechatNumber,9);
                break;
            case R.id.per_sex:
                mOpv.show();
                break;
            default:
                break;
        }
    }

    private void  pickView(){
        listSex = new String[]{"男","女"};
        for(int i=0;i<listSex.length;i++){
            mListSex.add(listSex[i]);
        }
        mOpv = new MyOptionsPickerView<String>(this);
        mOpv.setTitle("请选择");
        mOpv.setPicker(mListSex);
        mOpv.setCyclic(false, false, false);
        mOpv.setSelectOptions(0, 0, 0);
        mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                //      progressDialog = ProgressDialog.show(this,"Loading","Please wait...",true,true);
                sexText = mListSex.get(options1);
                mUserInforPresenter.loadingMessage(userId, sexText, "sex", 5, new FinishCallBack() {
                    public void onSuccess() {
                        per_sex.setText(sexText);
                        //        progressDialog.dismiss();
                    }

                    public void onFailure() {
                        Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                        //      progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:

                    //跳转到相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //将所拍照片存入
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, PHOTOHRAPH);  //带数据回传跳转
                    break;
                case R.id.btn_pick_photo:
                    //跳转到选择图片（相册或文件管理等，由用户选择）
                    Intent intentp = new Intent(Intent.ACTION_PICK, null);
                    //设置uri以及文件类型
                    //MediaStore这个类是android系统提供的一个多媒体数据库，Images.Media代表图片信息，
                    //这个Uri代表要查询的数据库名称加上表的名称
                    //跳转数据为图片数据库，类型为任何图片类型
                    intentp.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intentp, PHOTOZOOM);  //带数据回传跳转
                    break;
                case R.id.btn_cancel:
                    takePhotoPopWin.dismiss();
            }
        }
    };

    public void startPhotoZoom(Uri uri) {
        //跳转到图片裁剪界面
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED); //跳转数据所选图片图片，类型为任意类型
        //裁剪图片
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);  //返回数据为true
        startActivityForResult(intent, PHOTORESOULT);  //带数据回传跳转
    }


    /**
     * OSS上传图片
     * @param
     */
    private void uploadData(String uploadFilePath){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InformationActivity.this, "等待上传", Toast.LENGTH_SHORT).show();
            }
        });

        PutObjectRequest put = new PutObjectRequest(Config.OSS_BUCKET,getImageObjectKey("123456789"), uploadFilePath);

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            }
        });

        OSSAsyncTask task = Global.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                upPhoto = true;
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //如果上传失败了，通过mHandler ，发出失败的消息到主线程中。处理异常。
//							showNetErrorInfo();
                        upPhoto = false;
                    }
                });
            }
        });

        task.waitUntilFinished();

        if(upPhoto){
            mUserInforPresenter.loadingMessage(userId, photoUrl, "userHead", 1, new FinishCallBack() {
                public void onSuccess() {
                    Bitmap photoF = Utils.toRoundBitmap(Utils.returnBitMap(photoUrl));
                    per_headPortrait.setImageBitmap(photoF); //显示最终照片
                    Toast.makeText(Global.context, "更改成功", Toast.LENGTH_SHORT).show();
                    Log.d("----++++++++", "suc6");
                }

                public void onFailure() {
                    Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                    Log.d("----++++++++", "suc7");
                }
            });
        }


    }

    //通过UserCode 加上日期组装 OSS路径
    private String getImageObjectKey (String strUserCode){

        Date date = new Date();
        Log.d("+++++++++++++++>","http://"+Config.OSS_BUCKET+"."+Config.OSS_BUCKET_HOST_ID+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg");
//		return new SimpleDateFormat("yyyy/M/d").format(date)+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        photoUrl = "http://"+Config.OSS_BUCKET+"."+Config.OSS_BUCKET_HOST_ID+"/"+strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
        return strUserCode+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg";
    }


}
