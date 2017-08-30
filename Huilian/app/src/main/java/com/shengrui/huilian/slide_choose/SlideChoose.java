package com.shengrui.huilian.slide_choose;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lintiehan on 2016/4/14.
 */
public class SlideChoose extends FragmentActivity implements View.OnClickListener,OnRefreshListener{
    private ImageView back;
    private DrawerLayout mDrawerLayout;
    //右边栏
    private RelativeLayout right_drawer;
    //右边栏打开/关闭状态
    private boolean isDirection_right = false;
    TranslateAnimation animation;// 出现的动画效果
    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;
    private LinearLayout fansLayout, readingLayout, priceLayout, searchLayout;
    private boolean[] tabStateArr = new boolean[4];// 标记tab的选中状态，方便设置
    private ImageView fansImg, readingImg, priceImg;
    private TextView fansText, readingText, priceText;

    boolean orderbyfans=false;
    boolean orderbyreading=false;
    boolean orderbyprice=false;

    private RefreshListView listView=null;
    private SlideChoose_presenter activePresenter;
    Bundle bundle;
    String target_type;
    String target_id;
    String target_url;
    RequestParams rq;
    int rqAgency = 10;
    String searchKey = null;

    //搜索
    private TextView search_button;
    private EditText search;
    boolean searchAgency = false;

    //侧拉
    private Button verification_all;
    private Button verification_yes;
    private Button verification_no;

    private Button hard;
    private Button soft;

    private Spinner spinner;

    private EditText price;
    private EditText fans;
    private EditText read;

    private Button confirm;
    private Button reset;

    private int verification_all_status=0;
    private int verification_yes_status=0;
    private int verification_no_status=0;

    private int type_hard_status=0;
    private int type_soft_status=0;

    private static final String[] m_arr = {"多图文第一条","多图文第二条","多图文其他位置","单图文各位置"};
    private ArrayAdapter<String> spinner_adapter;

    //筛选参数
    private int screenPrice;
    private int screenFansNum;
    private int screenReadNum;
    private int isVerification1;
    private int isVerification2;
    private String screenType;
    private String screenPriceType;
    boolean screenAgency = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidechoose_main);
        bundle=this.getIntent().getExtras();
        target_type=bundle.getString("TYPE");
        target_id=bundle.getString(target_type);
        target_url=bundle.getString("URL");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);


        if (activePresenter == null) {
            activePresenter = SlideChoose_presenter.getInstance();
        }

        //获取数据
        listView = (RefreshListView) findViewById(R.id.inner_industry_lv);
        activePresenter.bindData(listView);
        activePresenter.refresh();

        rq=new RequestParams();
        rq.put(target_type, target_id);

        activePresenter.getData(target_url, rq, new FinishCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                Log.d("--->", "数据请求成功");
            }
        }, 1);



        //点击进入查看自媒体信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
                TextView et = (TextView) ly.findViewById(R.id.media_id);
                String mediaId = et.getText().toString();
                startActivity(new Intent(SlideChoose.this, MedCheckActivity.class).putExtra("mediaId", mediaId));
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置没法点击覆盖的页面
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;

        initView();

        confirm.setOnClickListener(this);
        reset.setOnClickListener(this);
        //spinner
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,m_arr);
        //设置下拉列表的风格
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setVisibility(View.VISIBLE);

        slide_button();

        mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑

        setTabState(fansImg, fansText, true);
        setTabState(readingImg, readingText, false);
        setTabState(priceImg, priceText, false);

        //搜索
        search_button = (TextView) findViewById(R.id.search_button);
        search = (EditText) findViewById(R.id.query);
        search_button.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {// EditText变化监听

            /**
             * 正在输入
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (!TextUtils.isEmpty(search.getText().toString())) {// 判断输入内容是否为空，为空则跳过
                }else {
                    rqAgency = 10;
                    slideData(1);
                    listView.setVisibility(View.VISIBLE);
                    setTabState(fansImg, fansText, true);
                    setTabState(readingImg, readingText, false);
                    setTabState(priceImg, priceText, false);
                    searchAgency = false;
                    screenAgency = false;
                }


            }

            /**
             * 输入之前
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            /**
             * 输入之后
             */
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        listView.setOnRefreshListener(this);
    }

    private void slide_button() {
        verification_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (verification_all_status){
                    case 0:
                        verification_all.setBackgroundResource(R.drawable.slidechoose_button_on);
                        verification_all.setTextColor(Color.rgb(218, 59, 65));
                        verification_all_status = 1;
                        verification_no_status = 0;
                        verification_yes_status = 0;
                        verification_yes.setTextColor(Color.rgb(37, 37, 37));
                        verification_no.setTextColor(Color.rgb(37, 37, 37));
                        verification_yes.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_no.setBackgroundResource(R.drawable.slidechoose_button);
                        isVerification1 = 1;
                        isVerification2 = 0;
                        break;
                    case 1:
                        verification_all.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_all.setTextColor(Color.rgb(37, 37, 37));
                        verification_all_status = 0;
                        break;
                }
            }
        });
        verification_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (verification_yes_status){
                    case 0:
                        verification_yes.setBackgroundResource(R.drawable.slidechoose_button_on);
                        verification_yes.setTextColor(Color.rgb(218, 59, 65));
                        verification_yes_status = 1;
                        verification_all_status = 0;
                        verification_no_status = 0;
                        verification_all.setTextColor(Color.rgb(37, 37, 37));
                        verification_no.setTextColor(Color.rgb(37, 37, 37));
                        verification_all.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_no.setBackgroundResource(R.drawable.slidechoose_button);
                        isVerification1 = 1;
                        isVerification2 = 1;
                        break;
                    case 1:
                        verification_yes.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_yes.setTextColor(Color.rgb(37, 37, 37));
                        verification_yes_status = 0;
                        break;
                }
            }
        });
        verification_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (verification_no_status){
                    case 0:
                        verification_no.setBackgroundResource(R.drawable.slidechoose_button_on);
                        verification_no.setTextColor(Color.rgb(218, 59, 65));
                        verification_no_status = 1;
                        verification_yes_status = 0;
                        verification_all_status = 0;
                        verification_yes.setTextColor(Color.rgb(37, 37, 37));
                        verification_all.setTextColor(Color.rgb(37, 37, 37));
                        verification_all.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_yes.setBackgroundResource(R.drawable.slidechoose_button);
                        isVerification1 = 0;
                        isVerification2 = 0;
                        break;
                    case 1:
                        verification_no.setBackgroundResource(R.drawable.slidechoose_button);
                        verification_no.setTextColor(Color.rgb(37, 37, 37));
                        verification_no_status = 0;
                        break;
                }
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type_hard_status){
                    case 0:
                        hard.setBackgroundResource(R.drawable.slidechoose_button_on);
                        hard.setTextColor(Color.rgb(218, 59, 65));
                        type_hard_status = 1;
                        soft.setBackgroundResource(R.drawable.slidechoose_button);
                        soft.setTextColor(Color.rgb(37, 37, 37));
                        type_soft_status = 0;
                        break;
                    case 1:
                        hard.setBackgroundResource(R.drawable.slidechoose_button);
                        hard.setTextColor(Color.rgb(37, 37, 37));
                        type_hard_status = 0;
                        break;
                }
            }
        });
        soft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type_soft_status){
                    case 0:
                        soft.setBackgroundResource(R.drawable.slidechoose_button_on);
                        soft.setTextColor(Color.rgb(218, 59, 65));
                        type_soft_status = 1;
                        hard.setBackgroundResource(R.drawable.slidechoose_button);
                        hard.setTextColor(Color.rgb(37, 37, 37));
                        type_hard_status = 0;
                        break;
                    case 1:
                        soft.setBackgroundResource(R.drawable.slidechoose_button);
                        soft.setTextColor(Color.rgb(37, 37, 37));
                        type_soft_status = 0;
                        break;
                }
            }
        });
    }


    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }
            @Override
            protected void onPostExecute(Void result){
                Log.d("---", "iii");
                slideData(1);
                listView.hideHeaderView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            //用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(500);

                return null;
            }

            //当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
            @Override
            protected void onPostExecute(Void result) {

                Log.d("--->", "chenchenchen");
                slideData(2);

                // 控制脚布局隐藏
                listView.hideFooterView();
            }
        }.execute(new Void[]{});
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:SlideChoose.this.finish();
                break;

            case R.id.search_button:
                searchKey = search.getLayout().getText().toString();
                if(searchKey.equals("")){
                    Toast.makeText(SlideChoose.this,"请输入关键字",Toast.LENGTH_SHORT).show();
                }else {
              //      tipListView.setVisibility(View.GONE);
                    if(target_type.equals("sortId")){
                        rqAgency = 40;
                    }
                    if(target_type.equals("schoolId")){
                        rqAgency = 30;
                    }
                    slideData(1);
                    listView.setVisibility(View.VISIBLE);
                    searchAgency = true;
                    screenAgency = false;
                    setTabState(fansImg, fansText, true);
                    setTabState(readingImg, readingText, false);
                    setTabState(priceImg, priceText, false);
                }
                break;
            case R.id.reset:
                price.setText("");
                fans.setText("");
                read.setText("");
                verification_all_status=0;
                verification_yes_status=0;
                verification_no_status=0;
                type_hard_status=0;
                type_soft_status=0;
                verification_yes.setTextColor(Color.rgb(37, 37, 37));
                verification_no.setTextColor(Color.rgb(37, 37, 37));
                verification_all.setTextColor(Color.rgb(37, 37, 37));
                verification_all.setBackgroundResource(R.drawable.slidechoose_button);
                verification_yes.setBackgroundResource(R.drawable.slidechoose_button);
                verification_no.setBackgroundResource(R.drawable.slidechoose_button);
                soft.setBackgroundResource(R.drawable.slidechoose_button);
                soft.setTextColor(Color.rgb(37, 37, 37));
                hard.setBackgroundResource(R.drawable.slidechoose_button);
                hard.setTextColor(Color.rgb(37, 37, 37));
                break;
            case R.id.confirm:

                if(type_soft_status==0&&type_hard_status==0){
                    Toast.makeText(SlideChoose.this,"请选择图文类型",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(verification_all_status==0&&verification_no_status==0&&verification_yes_status==0){
                    isVerification1 = 0;
                    isVerification2 = 1;
                }

                if(spinner.getSelectedItem().equals("多图文第一条")){
                    if(type_soft_status==1){
                        screenPriceType = "softMoreFirPrice";
                    }else {
                        screenPriceType = "hardMoreFirPrice";
                    }
                }

                if(spinner.getSelectedItem().equals("多图文第二条")){
                    if(type_soft_status==1){
                        screenPriceType = "softMoreSecPrice";
                    }else {
                        screenPriceType = "hardMoreSecPrice";
                    }
                }

                if(spinner.getSelectedItem().equals("多图文其他位置")){
                    if(type_soft_status==1){
                        screenPriceType = "softMoreOtherPrice";
                    }else {
                        screenPriceType = "hardMoreOtherPrice";
                    }
                }

                if(spinner.getSelectedItem().equals("单图文各位置")){
                    if(type_soft_status==1){
                        screenPriceType = "softSimplePrice";
                    }else {
                        screenPriceType = "hardSimplePrice";
                    }
                }

                if(price.getText().toString().trim().equals("")){
                    screenPrice = 1000000000;
                }else {
                    screenPrice = Integer.valueOf(price.getText().toString().trim());
                }
                if(fans.getText().toString().trim().equals("")){
                    screenFansNum = 0;
                }else {
                    screenFansNum = Integer.valueOf(fans.getText().toString().trim());
                }
                if(read.getText().toString().trim().equals("")){
                    screenReadNum = 0;
                }else {
                    screenReadNum = Integer.valueOf(read.getText().toString().trim());
                }

                if(target_type.equals("sortId")){
                    screenType = "medTypesFir";
                }
                if(target_type.equals("schoolId")){
                    screenType = "schoolId";
                }
                screenAgency = true;
                rqAgency = 110;
                slideData(1);
                setTabState(priceImg, priceText, false);
                setTabState(fansImg, fansText, true);
                setTabState(readingImg, readingText, false);

                break;

            default:
                break;
        }
    }





    /**
     * DrawerLayout监听
     */
    private class DrawerLayoutStateListener extends
            DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerOpened(View drawerView) {
            if (drawerView == right_drawer) {
                isDirection_right = true;
            }
        }
        @Override
        public void onDrawerClosed(View drawerView) {
            if (drawerView == right_drawer) {
                isDirection_right = false;
            }
        }
    }


    private void initView() {
        fansLayout = (LinearLayout) findViewById(R.id.fans_layout);
        readingLayout = (LinearLayout) findViewById(R.id.reading_layout);
        priceLayout = (LinearLayout) findViewById(R.id.price_layout);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        fansImg = (ImageView) findViewById(R.id.fans_img);
        readingImg = (ImageView) findViewById(R.id.reading_img);
        priceImg = (ImageView) findViewById(R.id.price_img);
        fansText = (TextView) findViewById(R.id.fans_textView);
        readingText = (TextView) findViewById(R.id.reading_textView);
        priceText = (TextView) findViewById(R.id.price_textView);
        MyClicktop myClicktop=new MyClicktop();
        fansLayout.setOnClickListener(myClicktop);
        readingLayout.setOnClickListener(myClicktop);
        priceLayout.setOnClickListener(myClicktop);
        searchLayout.setOnClickListener(myClicktop);
        int[] location = new int[2];
        animation = new TranslateAnimation(0, 0, -700, location[1]);
        animation.setDuration(500);

        //侧拉
        verification_all = (Button) findViewById(R.id.verification_all);
        verification_yes = (Button) findViewById(R.id.verification_yes);
        verification_no = (Button) findViewById(R.id.verification_no);
        hard = (Button) findViewById(R.id.hard);
        soft = (Button) findViewById(R.id.soft);
        spinner = (Spinner) findViewById(R.id.spinner);
        price = (EditText) findViewById(R.id.price);
        fans = (EditText) findViewById(R.id.fans);
        read = (EditText) findViewById(R.id.read);
        confirm = (Button) findViewById(R.id.confirm);
        reset = (Button) findViewById(R.id.reset);


    }


    class MyClicktop implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fans_layout:
                    //tabStateArr[0] = !tabStateArr[0];
                   // setTabState(fansImg, fansText, tabStateArr[0]);
                    setTabState(fansImg, fansText, true);
                    setTabState(readingImg, readingText, false);
                    setTabState(priceImg, priceText,false);
                    if(screenAgency){
                        rqAgency = 110;
                    }else {
                        if(target_type.equals("sortId")){
                            rqAgency = 50;
                        }
                        if(target_type.equals("schoolId")){
                            rqAgency = 80;
                        }
                    }

                    //在此加触发事件
                   // rq.put("ss","http://120.25.153.22/schoolMedia/fansNumSchoolListJson");
                    if(orderbyfans)
                    {
                        rq.put("ORDER_STATE",1);
                        slideData(1);
                    }else
                    {
                        rq.put("ORDER_STATE",0);
                        slideData(1);
                    }

                    break;
                case R.id.reading_layout:
//                    tabStateArr[1] = !tabStateArr[1];
//                    setTabState(readingImg, readingText, tabStateArr[1]);
                    setTabState(readingImg, readingText, true);
                    setTabState(fansImg, fansText, false);
                    setTabState(priceImg, priceText, false);
                    if(screenAgency){
                        rqAgency = 120;
                    }else {
                        if(target_type.equals("sortId")){
                            rqAgency = 60;
                        }
                        if(target_type.equals("schoolId")){
                            rqAgency = 90;
                        }
                    }
                    if(orderbyreading)
                    {
                        rq.put("ORDER_STATE", 1);
                        slideData(1);
                    }else
                    {
                        rq.put("ORDER_STATE", 0);
                        slideData(1);
                    }

                    break;
                case R.id.price_layout:
                    //tabStateArr[2] = !tabStateArr[2];
                    //setTabState(priceImg, priceText, tabStateArr[2]);
                    setTabState(priceImg, priceText, true);
                    setTabState(fansImg, fansText, false);
                    setTabState(readingImg, readingText, false);
                    if(screenAgency){
                        rqAgency = 130;
                    }else {
                        if(target_type.equals("sortId")){
                            rqAgency = 70;
                        }
                        if(target_type.equals("schoolId")){
                            rqAgency = 100;
                        }
                    }
                    if(orderbyprice)
                    {
                        rq.put("ORDER_STATE", 1);
                        slideData(1);
                    }else
                    {
                        rq.put("ORDER_STATE", 0);
                        slideData(1);
                    }

                    break;
                case R.id.search_layout:
                    tabStateArr[3] = !tabStateArr[3];

                    if (!isDirection_right) {//判断是否已经打开右抽屉
                        mDrawerLayout.openDrawer(right_drawer);
                    } else {// 打开右抽屉
                        mDrawerLayout.closeDrawer(right_drawer);
                    }
                    break;
            }
        }
    }
    /**
     * 设置tab的状态
     *
     * @param img      // ImageView对象
     * @param textView // TextView对象
     * @param state    // 状态
     */
    private void setTabState(ImageView img, TextView textView, boolean state) {
        if (state) {// 选中状态
            img.setBackgroundResource(R.drawable.icon_to_up);
            textView.setTextColor(getResources().getColor(
                    R.color.tab_text_pressed_color));
        } else {
            img.setBackgroundResource(R.drawable.icon_to_down);
            textView.setTextColor(getResources().getColor(
                    R.color.tab_text_color));
        }
    }


    //加载方法
    public void slideData(int agency){
        rq=new RequestParams();
        //rqAgency 10:行业自媒体、学校自媒体  30：搜索学校   40：搜索行业
        //         50:行业(粉丝数)  60：行业(阅读量)  70：行业(价格)
        //         80:学校(粉丝数)  90：学校(阅读量)  100：学校(价格)
        //         110:筛选(粉丝数)  120：筛选(阅读量)  130：筛选(价格)
        //dataAgency  1：刷新   2：加载
        switch (rqAgency){
            case 10:
                rq.put(target_type, target_id);
                if(target_type.equals("sortId")){
                    target_url = Config.mediaType;
                }
                if(target_type.equals("schoolId")){
                    target_url = Config.mediaSchool;
                }
                break;
            case 30:
                rq.put("searchType",2);
                rq.put(target_type,target_id);
                rq.put("searchKey",searchKey);
                rq.put("rankBy","fansNum");
                target_url = Config.search;
                break;
            case 40:
                rq.put("searchType",3);
                rq.put(target_type,target_id);
                rq.put("searchKey",searchKey);
                rq.put("rankBy","fansNum");
                target_url = Config.search;
                break;
            case 50:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",3);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","fansNum");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaTypeFans;
                }
                break;
            case 60:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",3);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","readNum");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaTyoeRead;
                }
                break;
            case 70:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",3);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","hardSimplePrice");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaTypePrice;
                }
                break;
            case 80:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",2);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","fansNum");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaSchoolFans;
                }
                break;
            case 90:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",2);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","readNum");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaSchoolRead;
                }
                break;
            case 100:
                rq.put(target_type, target_id);
                if (searchAgency){
                    rq.put("searchType",2);
                    rq.put("searchKey",searchKey);
                    rq.put("rankBy","hardSimplePrice");
                    target_url = Config.search;
                }else {
                    target_url = Config.mediaSchoolPrice;
                }
                break;
            case 110:
                if (searchAgency){
                    rq.put(target_type, target_id);
                    if(target_type.equals("sortId")){
                        rq.put("searchType",3);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy","hardSimplePrice");
                        target_url = Config.search;
                    }
                    if(target_type.equals("schoolId")){
                        rq.put("searchType",2);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy","fansNum");
                        target_url = Config.search;
                    }
                }else {
                    target_url = Config.ScreenMediaURL;
                }
                rq.put("screenId", target_id);
                rq.put("screenType",screenType);
                rq.put("price",screenPrice);
                rq.put("fansNum",screenFansNum);
                rq.put("readNum", screenReadNum);
                rq.put("isVerification1", isVerification1);
                rq.put("isVerification2", isVerification2);
                rq.put("priceType", screenPriceType);
                rq.put("orderType", "fansNum");
                break;
            case 120:
                if (searchAgency){
                    rq.put(target_type, target_id);
                    if(target_type.equals("sortId")){
                        rq.put("searchType",3);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy","hardSimplePrice");
                        target_url = Config.search;
                    }
                    if(target_type.equals("schoolId")){
                        rq.put("searchType",2);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy","readNum");
                        target_url = Config.search;
                    }
                }else {
                    target_url = Config.ScreenMediaURL;
                }
                rq.put("screenId", target_id);
                rq.put("screenType",screenType);
                rq.put("price",screenPrice);
                rq.put("fansNum",screenFansNum);
                rq.put("readNum", screenReadNum);
                rq.put("isVerification1", isVerification1);
                rq.put("isVerification2", isVerification2);
                rq.put("priceType", screenPriceType);
                rq.put("orderType", "readNum");
                break;
            case 130:
                if (searchAgency){
                    rq.put(target_type, target_id);
                    if(target_type.equals("sortId")){
                        rq.put("searchType",3);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy","hardSimplePrice");
                        target_url = Config.search;
                    }
                    if(target_type.equals("schoolId")){
                        rq.put("searchType",2);
                        rq.put("searchKey",searchKey);
                        rq.put("rankBy",screenPriceType);
                        target_url = Config.search;
                    }
                }else {
                    target_url = Config.ScreenMediaURL;
                }
                rq.put("screenId", target_id);
                rq.put("screenType",screenType);
                rq.put("price",screenPrice);
                rq.put("fansNum",screenFansNum);
                rq.put("readNum", screenReadNum);
                rq.put("isVerification1", isVerification1);
                rq.put("isVerification2", isVerification2);
                rq.put("priceType", screenPriceType);
                rq.put("orderType", screenPriceType);
                target_url = Config.ScreenMediaURL;
                break;
            default:
                break;
        }

        activePresenter.getData(target_url, rq, new FinishCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                mDrawerLayout.closeDrawer(right_drawer);
                Log.d("--->", "数据请求成功");
            }
            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                super.onFailure();
                Log.d("--->", "数据请求失败");
            }
        },agency);
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //spinner_text.setText("图文第"+spinner_value[i]+"条");
            TextView tv=(TextView)view;
            tv.setTextSize(14.0f);

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}

