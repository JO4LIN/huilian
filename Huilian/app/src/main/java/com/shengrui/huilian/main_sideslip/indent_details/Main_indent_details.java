package com.shengrui.huilian.main_sideslip.indent_details;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengrui.huilian.R;

import java.util.ArrayList;
import java.util.List;

public class Main_indent_details extends FragmentActivity implements View.OnClickListener{

    private ViewPager mPageVp;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Indent_details_adapter mIndentdetailsadapter;
    private RelativeLayout my_task_back;          //返回键

    /**
     * Tab显示内容TextView
     */
    private TextView tvone, tvtwo, tvthree, tvfour;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private Wait_indent_details allindentdetails;
    private Accomplish_indent_details accomplishindentdetails;
    private Processing_indent_details processingindentdetails;
    private Fail_indent_details failindentdetails;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    int i;
    TextView[]tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indent_details);

        findById();
        init();
        initTabLineWidth();//设置引导线的宽带
//        MeFragment s=  MeFragment.getInstance();
//        switch (s.state)
//        {
//            case "underway":
//                i=1;
//                break;
//            case "finished":
//                i=2;
//                break;
//            case "failure":
//                i=3;
//                tvfour.setTextColor(Color.RED);
//                break;
//        }
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
//                .getLayoutParams();
//        resetTextView();
//        tv[i].setTextColor(Color.RED);
//        lp.leftMargin=(screenWidth*i)/4;
//        mPageVp.setCurrentItem(i);
//        mTabLineIv.setLayoutParams(lp);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_task_back:
                Main_indent_details.this.finish();
                break;
        }
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
        my_task_back= (RelativeLayout) findViewById(R.id.my_task_back);
        tvone = (TextView) this.findViewById(R.id.tvone);
        tvtwo = (TextView) this.findViewById(R.id.tvtwo);
        tvthree = (TextView) this.findViewById(R.id.tvthree);
        tvfour = (TextView) findViewById(R.id.tvfour);
        tv=new TextView[]{tvone,tvtwo,tvthree,tvfour};
        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
        tvone.setOnClickListener(new MyOnClickListener(0));
        tvtwo.setOnClickListener(new MyOnClickListener(1));
        tvthree.setOnClickListener(new MyOnClickListener(2));
        tvfour.setOnClickListener(new MyOnClickListener(3));

        my_task_back.setOnClickListener(this);
    }
    private void init() {
        accomplishindentdetails = new Accomplish_indent_details();
        processingindentdetails = new Processing_indent_details();
        allindentdetails = new Wait_indent_details();
        failindentdetails = new Fail_indent_details();

        mFragmentList.add(allindentdetails);
        mFragmentList.add(processingindentdetails);
        mFragmentList.add(accomplishindentdetails);
        mFragmentList.add(failindentdetails);

        mIndentdetailsadapter = new Indent_details_adapter(getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mIndentdetailsadapter);
        mPageVp.setCurrentItem(0);

        //mPageVp.setCurrentItem(3);
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
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();
                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记5个页面,
                 * 从左到右分别为0,1,2,3,4
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                } else if (currentIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
                    Log.d("sss", "" + lp.leftMargin);
                } else if (currentIndex == 3 && position == 2) // 3->2
                {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex * (screenWidth / 4));
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
                }
                currentIndex = position;
            }
        });

    }

    /**
     * 设置滑动条的宽度为屏幕的1/4(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 4;
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
    }

}
