package com.shengrui.huilian;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.shengrui.huilian.main_dry_cargo.Main_dry_cargo;
import com.shengrui.huilian.main_industry.Main_industry;
import com.shengrui.huilian.main_recommend.Main_recommend;
import com.shengrui.huilian.main_region.Main_region;
import com.shengrui.huilian.main_region.Main_region_presenter;
import com.shengrui.huilian.main_region.select_city.CityListActivity;
import com.shengrui.huilian.main_school.Main_school;
import com.shengrui.huilian.main_school.Main_school_presenter;
import com.shengrui.huilian.main_search.Main_search;
import com.shengrui.huilian.main_sideslip.Main_Setting;
import com.shengrui.huilian.main_sideslip.indent_details.Main_indent_details;
import com.shengrui.huilian.main_sideslip.media_enter.Media_enter_add;
import com.shengrui.huilian.main_sideslip.my_collection.My_collection;
import com.shengrui.huilian.main_sideslip.report.Report;
import com.shengrui.huilian.main_sideslip.user_register.Register;
import com.shengrui.huilian.user_infor.InformationActivity;
import com.shengrui.huilian.user_infor.UserInforPresenter;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private ViewPager mPageVp;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Main_activity_adapter mTaskdetailsadapter;
    private Main_region_presenter areaPresenter = Main_region_presenter.getInstance();

    /**
     * Tab显示内容TextView
     */
    private TextView tvone, tvtwo, tvthree, tvfour,tvfive;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private Main_dry_cargo dry_cargo;
    private Main_region region;
    private Main_industry industry;
    private Main_recommend recommend;
    private Main_school school;

    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private double screenWidth;
    TextView[]tv;

    //侧滑
    private DrawerLayout mDrawerLayout = null;
//    private TextView myadTsask = null;
    private ImageView userHead = null;
    private TextView indent = null;
    private TextView myCollect = null;
    private TextView severOnline = null;
    private TextView setting = null;
    private RelativeLayout join = null;
    private TextView userName = null;
    private UserInforPresenter mUserInforPresenter = UserInforPresenter.getInstance();
    private Main_school_presenter activePresenter = Main_school_presenter.getInstance();

    //搜索
    private RelativeLayout main_search;
    private RelativeLayout message;

    //声明AMapLocationClient类对象(高德地图)
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int PERMISSON_REQUESTCODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huilian_main_activity);

        initLocation();
       // XGPushManager.setTag(this,"man");   //设置信鸽标签

        //侧滑
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);
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

        RelativeLayout button = (RelativeLayout) findViewById(R.id.drawer_img);
        button.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 按钮按下，将抽屉打开
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        //message
        message = (RelativeLayout) findViewById(R.id.message);
        message.setOnClickListener(this);

        //主页面fragment切换
        findById();
        init();
        initTabLineWidth();         //设置引导线的宽带
        refresh();



    }

    private void initLocation() {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);


            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);

            if(mLocationOption.isOnceLocationLatest()){
                mLocationOption.setOnceLocationLatest(true);
                //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
                //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
            }

            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
//            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(1000*60*60*24);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //        mLocationClient.onDestroy();//销毁定位客户端。



    }

    //刷新
    private void refresh() {
        Utils.setCookies();
        mUserInforPresenter.autoLogin(new FinishCallBack() {
            public void onSuccess() {
                userHead.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(mUserInforPresenter.mUserInforModel.getUserHead())));
                userName.setText(mUserInforPresenter.mUserInforModel.getUserName());
                Global.userId = mUserInforPresenter.mUserInforModel.getUserId();
                //绑定信鸽，账号注册
                XGPushManager.registerPush(Global.context, String.valueOf(Global.userId),
                        new XGIOperateCallback() {
                            @Override
                            public void onSuccess(Object data, int flag) {
                                Log.w(Constants.LogTag,
                                        "+++ register push sucess. token:" + data);
                            }

                            @Override
                            public void onFail(Object data, int errCode, String msg) {
                                Log.w(Constants.LogTag,
                                        "+++ register push fail. token:" + data
                                                + ", errCode:" + errCode + ",msg:"
                                                + msg);
                            }
                        });
            }

            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
                refresh();
        }else if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            userHead.setImageResource(R.drawable.jienigui);
            userName.setText("未登录");
        }
    }

    //信鸽
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }



    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            mPageVp.setCurrentItem(index);
        }
    }
    private void findById() {
        tvone = (TextView) this.findViewById(R.id.tv_main_recommend);
        tvtwo = (TextView) this.findViewById(R.id.tv_main_industry);
        tvthree = (TextView) this.findViewById(R.id.tv_main_region);
        tvfour = (TextView) findViewById(R.id.tv_main_school);
        tvfive=(TextView) findViewById(R.id.tv_main_dry_cargo);
        tv=new TextView[]{tvone,tvtwo,tvthree,tvfour};
        mTabLineIv = (ImageView) this.findViewById(R.id.main_tab);
        mPageVp = (ViewPager) this.findViewById(R.id.main_viewPager);
        tvone.setOnClickListener(new MyOnClickListener(0));
        tvtwo.setOnClickListener(new MyOnClickListener(1));
        tvthree.setOnClickListener(new MyOnClickListener(2));
        tvfour.setOnClickListener(new MyOnClickListener(3));
        tvfive.setOnClickListener(new MyOnClickListener(4));

    }
    private void init() {
//侧滑
        userHead = (ImageView) findViewById(R.id.userhead);
//        myadTsask = (TextView) findViewById(R.id.my_adtask);
        indent = (TextView) findViewById(R.id.indent);
        myCollect = (TextView) findViewById(R.id.mycollect);
        severOnline = (TextView) findViewById(R.id.serve);
        setting = (TextView) findViewById(R.id.setting);
        join = (RelativeLayout) findViewById(R.id.join);
        userName = (TextView) findViewById(R.id.userName);

        userHead.setOnClickListener(this);
//        myadTsask.setOnClickListener(this);
        indent.setOnClickListener(this);
        myCollect.setOnClickListener(this);
        severOnline.setOnClickListener(this);
        setting.setOnClickListener(this);
        join.setOnClickListener(this);

        main_search= (RelativeLayout) findViewById(R.id.main_search);
        main_search.setOnClickListener(this);

        //fragment切换
        recommend = new Main_recommend();
        industry = new Main_industry();
        region = new Main_region();
        dry_cargo = new Main_dry_cargo();
        school = new Main_school();

        mFragmentList.add(recommend);
        mFragmentList.add(industry);
        mFragmentList.add(region);
        mFragmentList.add(school);
        mFragmentList.add(dry_cargo);

        mTaskdetailsadapter = new Main_activity_adapter(getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mTaskdetailsadapter);
        mPageVp.setCurrentItem(0);

        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv.getLayoutParams();
                lp.width=180;
                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记5个页面,
                 * 从左到右分别为0,1,2,3,4
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                } else if (currentIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                    Log.d("sss", "" + lp.leftMargin);
                } else if (currentIndex == 3 && position == 2) // 3->2
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                }
                else if (currentIndex == 3 && position == 3) // 3->4
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                } else if (currentIndex == 4 && position == 3) // 4->3
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 5) + currentIndex * (screenWidth / 5));
                }

                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        tvone.setTextColor(Color.rgb(231, 31, 25));
                        break;
                    case 1:
                        tvtwo.setTextColor(Color.rgb(231, 31, 25));
                        break;
                    case 2:
                        tvthree.setTextColor(Color.rgb(231, 31, 25));
                        break;
                    case 3:
                        tvfour.setTextColor(Color.rgb(231, 31, 25));
                        break;
                    case 4:
                        tvfive.setTextColor(Color.rgb(231, 31, 25));
                        break;
                }
                currentIndex = position;
            }
        });

    }

    /**
     * 设置滑动条的宽度为屏幕的1/5(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = (dpMetrics.widthPixels)*0.86;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = (int) (screenWidth / 5);
        mTabLineIv.setLayoutParams(lp);

    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tvone.setTextColor(Color.rgb(133, 132, 132));
        tvtwo.setTextColor(Color.rgb(133, 132, 132));
        tvthree.setTextColor(Color.rgb(133, 132, 132));
        tvfour.setTextColor(Color.rgb(133, 132, 132));
        tvfive.setTextColor(Color.rgb(133, 132, 132));
    }


    //设置滑动边距
    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userhead:
                if (Global.userId==100000000){
                    startActivityForResult(new Intent(this, Register.class), 0);
                }else {
                    startActivity(new Intent(this, InformationActivity.class));
                }
                break;
//            case R.id.my_adtask:
//                if (Global.userId==100000000){
//                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
//                }else {
//                    startActivity(new Intent(this,Media_enter_add.class).putExtra("task","0"));
//                }
//
//                break;
            case R.id.indent:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(this, Main_indent_details.class));
                }
                break;
            case R.id.mycollect:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(this, My_collection.class));
                }

                break;
            case R.id.serve:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
//                    startActivityForResult(new Intent(this, Report.class), 0);
                }

                break;
            case R.id.setting:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivityForResult(new Intent(this, Main_Setting.class), 1);
                }

                break;
            case R.id.join:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(this, Media_enter_add.class).putExtra("task","0"));
                }

                break;
            case R.id.main_search:
                startActivity(new Intent(this, Main_search.class));
                break;
            case R.id.message:
                if (Global.userId==100000000){
                    Toast.makeText(Global.context, "请登陆", Toast.LENGTH_SHORT).show();
                }else {
//                    startActivity(new Intent(this, CityListActivity.class));
                }

                break;
            default:
                break;
        }
    }

    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断是否需要检测，防止不停的弹框(高德地图)
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }
    /**
     *
     * @param Request PermissonList(高德地图)
     * @since 2.5.0
     *
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表(高德地图)
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权(高德地图)
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息(高德地图)
     *
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("title");
        builder.setMessage("message");
        // 拒绝, 退出应用
        builder.setNegativeButton("退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("允许",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     *  启动应用的设置(高德地图)
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //开启定位
        if (!mLocationClient.isStarted())
            mLocationClient.startLocation();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //停止定位
        mLocationClient.stopLocation();
    }
    //声明定位回调监听器

    public AMapLocationListener mLocationListener = new AMapLocationListener() {


        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            areaPresenter.hotCity(new FinishCallBack());
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //返回当前地址
                    Global.location= amapLocation.getCity();
                    if(Global.location=="北京市"||Global.location=="天津市"||Global.location=="重庆市"||Global.location=="上海市"){
                        Global.location = amapLocation.getDistrict();
                    }

                    RequestParams rq=new RequestParams();
                    rq.put("CITY_NAME", "\"" +Global.location+"\"");
                    //Log.d("---->", "'珠海市'");
                    activePresenter.getData1(Config.areaSchool, rq, new FinishCallBack(){
                        @Override
                        public void onFailure() {
                            Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                            super.onFailure();
                        }
                    }, 1);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
}

