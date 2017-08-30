package com.shengrui.huilian.main_sideslip.indent_details;

import android.graphics.Bitmap;
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
public class Accomplish_indent_details_adapter extends BaseDataAdapter<Accomplish_indent_details_model> {

    private LayoutInflater mInflater;
    public Accomplish_indent_details_adapter(List<Accomplish_indent_details_model> datas) {
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
            convertView = mInflater.inflate(R.layout.task_item,null);
            holder.mediaHead= (ImageView) convertView.findViewById(R.id.task_item_img);
            holder.mediaName= (TextView) convertView.findViewById(R.id.task_item_media);
            holder.price=((TextView) convertView.findViewById(R.id.task_item_money));
            holder.title=((TextView) convertView.findViewById(R.id.task_item_title));
            holder.date=((TextView) convertView.findViewById(R.id.task_item_time));
            holder.progress=((TextView) convertView.findViewById(R.id.task_item_evaluate));
            holder.indentId=((TextView) convertView.findViewById(R.id.indentId));

            convertView.setTag(holder);
         } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }
       // holder.media_id.setText(String.valueOf(datas.get(position).getMediaId()));
        holder.mediaName.setText(datas.get(position).getMediaName());
        holder.price.setText(String.valueOf(datas.get(position).getPrice()));
        holder.title.setText(String.valueOf(datas.get(position).getTitle()));
        holder.date.setText(String.valueOf(datas.get(position).getDate()));
        if(datas.get(position).getProgress().equals("0")){
            holder.progress.setText("进行中");
        }else if(datas.get(position).getProgress().equals("1")){
            holder.progress.setText("已完成");
        }else if(datas.get(position).getProgress().equals("2")){
            holder.progress.setText("失败");
        }else if(datas.get(position).getProgress().equals("3")){
            holder.progress.setText("待接单");
        }
        holder.indentId.setText(String.valueOf(datas.get(position).getIndentId()));

        //ImageLoader.getInstance().displayImage( ima[position], holder.media_pic);
        ImageLoader.getInstance().displayImage( datas.get(position).getWechatHead(), holder.mediaHead);
        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {
        private TextView indentId ;
        private ImageView mediaHead= null;
        private TextView mediaName = null;
        private TextView price = null;
        private TextView title = null;
        private TextView date = null;
        private TextView progress = null;


        public ImageView getMediaHead() {
            return mediaHead;
        }

        public void setMediaHead(ImageView mediaHead) {
            this.mediaHead = mediaHead;
        }

        public TextView getMediaName() {
            return mediaName;
        }

        public void setMediaName(TextView mediaName) {
            this.mediaName = mediaName;
        }

        public TextView getPrice() {
            return price;
        }

        public void setPrice(TextView price) {
            this.price = price;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public TextView getProgress() {
            return progress;
        }

        public void setProgress(TextView progress) {
            this.progress = progress;
        }


        @Override
        public void initView() {
        }


        public TextView getIndentId() {
            return indentId;
        }

        public void setIndentId(TextView indentId) {
            this.indentId = indentId;
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
