package com.shengrui.huilian.medium_infor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.shengrui.huilian.R;

/**
 * Created by jh on 2016/5/26.
 */
public class CheckCodePopWin extends PopupWindow {
    private ImageView btn_code = null;
    private View view = null;

    public CheckCodePopWin(Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.check_code_pop, null);
        btn_code = (ImageView) view.findViewById(R.id.btn_code);
    }

    public void setCode(Bitmap phone){
        btn_code.setImageBitmap(phone);
        checkCode();
    }

    public void checkCode(){
        //btn_code.setImageBitmap(phone);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int top = view.findViewById(R.id.code_pop_layout).getTop();
                int left = view.findViewById(R.id.code_pop_layout).getLeft();
                int right = view.findViewById(R.id.code_pop_layout).getRight();
                int bottom = view.findViewById(R.id.code_pop_layout).getBottom();

                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y<top || y>bottom || x<right || x>left) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_code_anim);

    }
}
