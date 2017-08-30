package com.shengrui.huilian.main_region.region_slide_choose;

import android.app.Activity;
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
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;

/**
 * Created by lintiehan on 2016/4/14.
 */
public class Region_slide_choose extends FragmentActivity implements View.OnClickListener,OnRefreshListener{
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
    private Region_slide_choose_presenter activePresenter;
    Bundle bundle;
    RequestParams rq;
    String searchKey = "%";
    private String cityName = null;

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
    private int screenPrice = 1000000000;
    private int screenFansNum = 0;
    private int screenReadNum = 0;
    private int isVerification1 = 0;
    private int isVerification2 = 1;
    private String screenPriceType = "hardSimplePrice";
    private String ordType = "fansNum";
    private boolean screenAgency = false;
    private boolean firSearch = true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidechoose_main);
        bundle=this.getIntent().getExtras();
        cityName = bundle.getString("cityName");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);


        if (activePresenter == null) {
            activePresenter = Region_slide_choose_presenter.getInstance();
        }

        //获取数据
        listView = (RefreshListView) findViewById(R.id.inner_industry_lv);
        activePresenter.bindData(listView);
        activePresenter.refresh();

        rq=new RequestParams();
        cityData(1);



        //点击进入查看自媒体信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
                TextView et = (TextView) ly.findViewById(R.id.media_id);
                String mediaId = et.getText().toString();
                startActivity(new Intent(Region_slide_choose.this, MedCheckActivity.class).putExtra("mediaId", mediaId));
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

                if (TextUtils.isEmpty(search.getText().toString())) {// 判断输入内容是否为空，为空则跳过
                    screenPrice = 1000000000;
                    screenFansNum = 0;
                    screenReadNum = 0;
                    isVerification1 = 0;
                    isVerification2 = 1;
                    screenPriceType = "hardSimplePrice";
                    ordType = "fansNum";
                    searchKey = "%";
                    listView.setVisibility(View.VISIBLE);
                    setTabState(fansImg, fansText, true);
                    setTabState(readingImg, readingText, false);
                    setTabState(priceImg, priceText, false);
                    searchAgency = false;
                    screenAgency = false;
                    cityData(1);
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
                cityData(1);
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
                cityData(2);

                // 控制脚布局隐藏
                listView.hideFooterView();
            }
        }.execute(new Void[]{});
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                Region_slide_choose.this.finish();
                break;

            case R.id.search_button:
                searchKey = search.getLayout().getText().toString();
                if(searchKey.equals("")){
                    Toast.makeText(Region_slide_choose.this,"请输入关键字",Toast.LENGTH_SHORT).show();
                }else {
              //      tipListView.setVisibility(View.GONE);
                    screenPrice = 1000000000;
                    screenFansNum = 0;
                    screenReadNum = 0;
                    isVerification1 = 0;
                    isVerification2 = 1;
                    screenPriceType = "hardSimplePrice";
                    ordType = "fansNum";
                    listView.setVisibility(View.VISIBLE);
                    searchAgency = true;
                    screenAgency = false;
                    setTabState(fansImg, fansText, true);
                    setTabState(readingImg, readingText, false);
                    setTabState(priceImg, priceText, false);
                    cityData(1);
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
                    Toast.makeText(Region_slide_choose.this,"请选择图文类型",Toast.LENGTH_SHORT).show();
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

                screenAgency = true;
                cityData(1);
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
                    ordType = "fansNum";
                    cityData(1);

                    break;
                case R.id.reading_layout:
//                    tabStateArr[1] = !tabStateArr[1];
//                    setTabState(readingImg, readingText, tabStateArr[1]);
                    setTabState(readingImg, readingText, true);
                    setTabState(fansImg, fansText, false);
                    setTabState(priceImg, priceText, false);
                    ordType = "readNum";
                    cityData(1);

                    break;
                case R.id.price_layout:
                    //tabStateArr[2] = !tabStateArr[2];
                    //setTabState(priceImg, priceText, tabStateArr[2]);
                    setTabState(priceImg, priceText, true);
                    setTabState(fansImg, fansText, false);
                    setTabState(readingImg, readingText, false);
                    if(screenAgency){
                        ordType = screenPriceType;
                    }else {
                        ordType = "hardSimplePrice";
                    }
                    cityData(1);
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
    public void cityData(int agency){
        rq.put("firSearch",firSearch);
        rq.put("searchKey",searchKey);
        rq.put("cityName", cityName);
        rq.put("price",screenPrice);
        rq.put("fansNum",screenFansNum);
        rq.put("readNum", screenReadNum);
        rq.put("isVerification1", isVerification1);
        rq.put("isVerification2", isVerification2);
        rq.put("priceType", screenPriceType);
        rq.put("orderType", ordType);

        activePresenter.getData(rq, new FinishCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                firSearch = false;
                mDrawerLayout.closeDrawer(right_drawer);
                Log.d("--->", "数据请求成功");
            }
            public void onFailure() {
                super.onFailure();
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
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

