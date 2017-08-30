package com.shengrui.huilian.main_dry_cargo;

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
public class Main_dry_cargo_adapter extends BaseDataAdapter<Main_dry_cargo_model> {

    private LayoutInflater mInflater;
    public Main_dry_cargo_adapter(List<Main_dry_cargo_model> datas) {
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
            convertView = mInflater.inflate(R.layout.main_dry_cargo_item,null);

            holder.newsUrl = (TextView) convertView.findViewById(R.id.news_url);
            holder.newsId = (TextView) convertView.findViewById(R.id.news_id);
            holder.newsHead= (ImageView) convertView.findViewById(R.id.news_head);
            holder.newsDate= (TextView) convertView.findViewById(R.id.news_date);
            holder.newsCategory=((TextView) convertView.findViewById(R.id.news_category));
            holder.newsName=((TextView) convertView.findViewById(R.id.news_name));

            convertView.setTag(holder);
         } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }
        holder.newsName.setText(datas.get(position).getNewsName());
        holder.newsDate.setText(String.valueOf(datas.get(position).getNewsDate()));
        holder.newsCategory.setText(String.valueOf(datas.get(position).getNewsCategory()));
        holder.newsId.setText(String.valueOf(datas.get(position).getNewsId()));
        holder.newsUrl.setText(String.valueOf(datas.get(position).getNewsUrl()));
        ImageLoader.getInstance().displayImage( datas.get(position).getNewsHead(), holder.newsHead);

        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {


        private ImageView newsHead= null;
        private TextView newsName = null;
        private TextView newsDate = null;
        private TextView newsCategory = null;
        private TextView newsId = null;
        private TextView newsUrl = null;

        @Override
        public void initView() {
        }


        public ImageView getNewsHead() {
            return newsHead;
        }

        public void setNewsHead(ImageView newsHead) {
            this.newsHead = newsHead;
        }

        public TextView getNewsName() {
            return newsName;
        }

        public void setNewsName(TextView newsName) {
            this.newsName = newsName;
        }

        public TextView getNewsDate() {
            return newsDate;
        }

        public void setNewsDate(TextView newsDate) {
            this.newsDate = newsDate;
        }

        public TextView getNewsCategory() {
            return newsCategory;
        }

        public void setNewsCategory(TextView newsCategory) {
            this.newsCategory = newsCategory;
        }

        public TextView getNewsId() {
            return newsId;
        }

        public void setNewsId(TextView newsId) {
            this.newsId = newsId;
        }

        public TextView getNewsUrl() {
            return newsUrl;
        }

        public void setNewsUrl(TextView newsUrl) {
            this.newsUrl = newsUrl;
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
