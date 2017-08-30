package com.shengrui.huilian.main_region;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Adapter.BaseDataAdapter;
import com.shengrui.huilian.base_mvp.Global.Global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Main_region_adapter extends BaseDataAdapter<Main_region_model> {

    private LayoutInflater mInflater;
    public Main_region_adapter(List<Main_region_model> datas) {
        super(datas);
    }

    private ImageLoadingListener ill=new  AnimateFirstDisplayListener();
    DisplayImageOptions options; // 配置图片加载及显示选项
    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     * http://www.cnblogs.com/xiaowenji/archive/2010/12/08/1900579.html
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Global.getContext()));
       // 配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.jilin) // 在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.jilin) // image连接地址为空时
                .showImageOnFail(R.drawable.jilin) // image加载失败
                .cacheInMemory(false) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(false) // 加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20)) // 设置用户加载图片task(这里是圆角图片显示)
                .build();

        mInflater = LayoutInflater.from(Global.context);
        ActiveViewHolder holder = null;
        if (convertView == null) {
            holder = new ActiveViewHolder();
            // 初始化绑定控件
            convertView = mInflater.inflate(R.layout.region_item,null);
            holder.provinceName = (TextView) convertView.findViewById(R.id.region_province);
            holder.cityName= (TextView) convertView.findViewById(R.id.region_city);
            holder.cityMediaNum=((TextView) convertView.findViewById(R.id.mediaNum));
            holder.citySearchNum=((TextView) convertView.findViewById(R.id.searchNum));
            holder.cityId=((TextView) convertView.findViewById(R.id.cityId));

            convertView.setTag(holder);
         } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }

        holder.provinceName.setText(datas.get(position).getProvinceName());
        holder.cityName.setText(String.valueOf(datas.get(position).getCityName()));
        holder.cityMediaNum.setText(String.valueOf(datas.get(position).getCityMediaNum()));
        holder.citySearchNum.setText(String.valueOf(datas.get(position).getCitySearchNum()));
        holder.cityId.setText(String.valueOf(datas.get(position).getCityId()));

        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {

        private TextView cityName = null;
        private TextView provinceName = null;
        private TextView cityId;
        private TextView provinceId;
        private TextView cityMediaNum;
        private TextView citySearchNum;

        @Override
        public void initView() {
        }

        public TextView getCityName() {
            return cityName;
        }

        public void setCityName(TextView cityName) {
            this.cityName = cityName;
        }

        public TextView getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(TextView provinceName) {
            this.provinceName = provinceName;
        }

        public TextView getCityId() {
            return cityId;
        }

        public void setCityId(TextView cityId) {
            this.cityId = cityId;
        }

        public TextView getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(TextView provinceId) {
            this.provinceId = provinceId;
        }

        public TextView getCityMediaNum() {
            return cityMediaNum;
        }

        public void setCityMediaNum(TextView cityMediaNum) {
            this.cityMediaNum = cityMediaNum;
        }

        public TextView getCitySearchNum() {
            return citySearchNum;
        }

        public void setCitySearchNum(TextView citySearchNum) {
            this.citySearchNum = citySearchNum;
        }
    }



    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }
}
