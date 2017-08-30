package com.shengrui.huilian.main_industry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengrui.huilian.R;


public class GrideAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    String[] name;
    int[] iconarray;

    public GrideAdapter(Context context, String[] name, int[] iconarray) {
        this.inflater = LayoutInflater.from(context);
        this.name = name;
        this.iconarray = iconarray;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView=this.inflater.inflate(R.layout.grid_item, null);
            holder.iv=(ImageView) convertView.findViewById(R.id.iv_item);
            holder.tv=(TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(iconarray[position]);
        holder.tv.setText(name[position]);
        return convertView;
    }
    private class ViewHolder{
        ImageView iv;
        TextView tv;
    }

}
