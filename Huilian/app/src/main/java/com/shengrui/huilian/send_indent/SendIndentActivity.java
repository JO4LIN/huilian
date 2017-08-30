package com.shengrui.huilian.send_indent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.medium_infor.MyOptionsPickerView;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.OnClickListener;

public class SendIndentActivity extends AppCompatActivity implements OnClickListener {
    private ImageView meidaHead;
    private TextView mediaName;
    private TextView indentNum;
    private EditText title;
    private TextView introClick;
    private TextView introTitle;
    private EditText intro;
    private EditText price;
    private TextView graphicsTypesClick;
    private TextView graphicsTypes;
    private TextView proDateClick;
    private TextView proDate;
    private TextView linkClick;
    private TextView linkTitle;
    private EditText link;
    private TextView send;
    private CheckBox agreement;
    private final int FILE_SELECT_CODE = 101;
    private TextView textNum;
    private TextView reset;
    private RelativeLayout back;
    ProgressDialog progressDialog = null;
    private Bundle bundle = null;


    private ArrayList<String> mListFollowers = new ArrayList<String>();
    private ArrayList<String> mListDate = new ArrayList<String>();
    private String[] listFollowers;
    private String[] listDate;
    private MyOptionsPickerView<String> mOpv;
    private int mediaId = 1;
    private int userId = 1;

    private SendIndentPresenter mSendIndentPresenter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_indent);
        if (mSendIndentPresenter == null)
            mSendIndentPresenter = SendIndentPresenter.getInstance();
        bundle=this.getIntent().getExtras();
        mediaId = Integer.valueOf(bundle.getString("mediaId"));
        inti();
        initialize();      //初始化控件
        refreshMedia();

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
            editStart = intro.getSelectionStart();
            editEnd = intro.getSelectionEnd();
            if (temp.length() > 150) {
                Toast.makeText(SendIndentActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                intro.setText(s);
                intro.setSelection(tempSelection);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshMedia() {
     //   progressDialog = ProgressDialog.show(Global.context,"Loading","Please wait...",true,true);
        mSendIndentPresenter.refreshMedia(mediaId,new FinishCallBack() {
            public void onSuccess() {
                mediaName.setText(mSendIndentPresenter.mSendIndentModel.getMediaName());
                indentNum.setText(mSendIndentPresenter.mSendIndentModel.getIndentNum());
                meidaHead.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(mSendIndentPresenter.mSendIndentModel.getWechatHead())));
     //           progressDialog.dismiss();
            }
            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
       //         progressDialog.dismiss();
            }
        });
        //meidaHead.setImageBitmap();
        //       String[] s = mSendIndentPresenter.refreshReturn();
        //      mediaName.setText(s[0]);
        //      indentNum.setText(s[1]);
        //mediaName.setText(mSendIndentPresenter.mSendIndentModel.getMediaName());
        // indentNum.setText(model.getIndentNum());
    }


    private void initialize() {
        back = (RelativeLayout) findViewById(R.id.back);
        meidaHead = (ImageView) findViewById(R.id.mainHead);
        mediaName = (TextView) findViewById(R.id.mainName);
        indentNum = (TextView) findViewById(R.id.indentNum);
        title = (EditText) findViewById(R.id.title);
        introClick = (TextView) findViewById(R.id.introClick);
        introTitle = (TextView) findViewById(R.id.introTitle);
        intro = (EditText) findViewById(R.id.intro);
        price = (EditText) findViewById(R.id.price);
        graphicsTypesClick = (TextView) findViewById(R.id.graphicsTypesClick);
        graphicsTypes = (TextView) findViewById(R.id.graphicsTypes);
        proDateClick = (TextView) findViewById(R.id.proDateClick);
        proDate = (TextView) findViewById(R.id.proDate);
        linkClick = (TextView) findViewById(R.id.linkClick);
        link = (EditText) findViewById(R.id.link);
        agreement = (CheckBox) findViewById(R.id.agreement);
        send = (TextView) findViewById(R.id.send);
        reset = (TextView) findViewById(R.id.reset);
        textNum = (TextView) findViewById(R.id.textNum);

        back.setOnClickListener(this);
        graphicsTypesClick.setOnClickListener(this);
        proDateClick.setOnClickListener(this);
        send.setOnClickListener(this);
        reset.setOnClickListener(this);
        intro.addTextChangedListener(mTextWatcher);
    }

    private void inti() {
        listDate = datime();
        listFollowers = new String[]{"多图文第一条（软广）","多图文第一条（硬广）","多图文第二条（软广）","多图文第二条（硬广）",
                "多图文其他位置（软广）","多图文其他位置（硬广）","单图文各位置（软广）","单图文各位置（硬广）",};
        for(int i=0;i<listDate.length;i++){
            mListDate.add(listDate[i]);
        }
        for(int i=0;i<listFollowers.length;i++){
            mListFollowers.add(listFollowers[i]);
        }
        //phone.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                SendIndentActivity.this.finish();
            case R.id.reset:
                title.setText("");
                price.setText("");
                intro.setText("");
                link.setText("");
                graphicsTypes.setText("");
                proDate.setText("");
                break;
            case R.id.graphicsTypesClick:
                mOpv = new MyOptionsPickerView<String>(this);
                mOpv.setTitle("请选择");
                mOpv.setPicker(mListFollowers);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        String tx = mListFollowers.get(options1);
                        graphicsTypes.setText(tx);
                    }
                });
                mOpv.show();
                break;
            case R.id.proDateClick:
                mOpv = new MyOptionsPickerView<String>(this);
                mOpv.setTitle("请选择");
                mOpv.setPicker(mListDate);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        String tx = mListDate.get(options1);
                        proDate.setText(tx);
                    }
                });
                mOpv.show();
                break;
            case R.id.send:
                String titleSend = title.getLayout().getText().toString();
                String introSend = intro.getLayout().getText().toString();
                String priceSend = price.getLayout().getText().toString();
                String graphicsTypesSend = graphicsTypes.getLayout().getText().toString();
                String proDateSend = proDate.getLayout().getText().toString();
                String linkSend = link.getLayout().getText().toString();


                //  startActivity(new Intent(this, TypeFragment.class));;
                if (TextUtils.isEmpty(titleSend)) {
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(introSend)) {
                    Toast.makeText(this, "请输入简介", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(priceSend)) {
                    Toast.makeText(this, "请输入价格", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(graphicsTypesSend)) {
                    Toast.makeText(this, "请输入图文类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(proDateSend)) {
                    Toast.makeText(this, "请输入推广日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!agreement.isChecked()) {
                    Toast.makeText(this, "请查看服务协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String date = format.format(curDate);
                Log.d("-----------------------",date);
           //     progressDialog = ProgressDialog.show(Global.context,"Loading","Please wait...",true,true);
                mSendIndentPresenter.sendingIndent(Global.userId,mediaId,titleSend,introSend,priceSend,graphicsTypesSend,proDateSend,
                        linkSend,date,new FinishCallBack() {
                            public void onSuccess() {
                                Toast.makeText(Global.context, "发布成功", Toast.LENGTH_SHORT).show();
                                SendIndentActivity.this.finish();
            //                    progressDialog.dismiss();
                            }

                            public void onFailure() {
                                Toast.makeText(Global.context, "发布失败", Toast.LENGTH_SHORT).show();
             //                   progressDialog.dismiss();
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "选择附件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

 /*   private String[] datetime(){
        String[]datetime = null;
        SimpleDateFormat m_format = new SimpleDateFormat("MM");
        Date m_curDate = new Date(System.currentTimeMillis());
        int month = Integer.parseInt(m_format.format(m_curDate));
        SimpleDateFormat d_format = new SimpleDateFormat("dd");
        Date d_curDate = new Date(System.currentTimeMillis());
        int day = Integer.parseInt(d_format.format(d_curDate))+1;
        SimpleDateFormat e_format = new SimpleDateFormat("EEE");
        Date e_curDate = new Date(System.currentTimeMillis());
        Log.d("________________________",m_format.format(m_curDate));
        Log.d("________________________",d_format.format(d_curDate));
        Log.d("________________________",e_format.format(e_curDate));
        for(int i=0;i<7;i++){
            datetime[i] = month++
        }
        return datetime;
    }*/

    private String[] datime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String date = format.format(curDate);
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 6));
        int day = Integer.parseInt(date.substring(7,8));
        String[] datetime = new String[8];
        datetime[0] = "不限";
        System.out.print(year+month+day);
        int monthAll[] = {31,28,31,30,31,30,31,31,30,31,30,31};
        if(year%4==0){
            monthAll[1] = 29;
        }
        if((day+6) > monthAll[month-1]){
            int i = 0,j=1 ;
            while (day<=monthAll[month-1]){
                datetime[j] = Integer.toString(year)+"年"+Integer.toString(month)+"月"+Integer.toString(day)+"日";
                day++; i++; j++;
            }
            if (month==12){
                month = 1;
                year = ++year;
            }else {
                month = month+1;
            }
            for(int k=1;k<=7-i;j++){
                datetime[j] = Integer.toString(year)+"年"+Integer.toString(month)+"月"+Integer.toString(k)+"日";
                k++;
            }
        }else if (day+6 <= monthAll[month-1]){
            for(int i=1,j=1;i<=7;i++){
                datetime[j] = Integer.toString(year)+"年"+Integer.toString(month)+"月"+Integer.toString(day)+"日";
                day++; j++;
            }
        }
        return datetime;

    }


}
