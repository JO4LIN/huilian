package com.shengrui.huilian.base_mvp.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */
public abstract class BaseDataAdapter<T> extends BaseAdapter {

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected Context context;

    protected List<T> datas;

    public BaseDataAdapter(List<T> datas) {
        this.datas = datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ViewHolder {
        View itemView = null;

        void initView();
    }
}