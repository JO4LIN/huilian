package com.shengrui.huilian.main_sideslip.my_collection;

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
import com.shengrui.huilian.base_mvp.net.CircleBitmap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */
public class My_collection_user_adapter extends BaseDataAdapter<My_collection_user_model> {

    private LayoutInflater mInflater;
    public My_collection_user_adapter(List<My_collection_user_model> datas) {
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
                .showStubImage(R.drawable.pic_bg) // 在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.pic_bg) // image连接地址为空时
                .showImageOnFail(R.drawable.pic_bg) // image加载失败
                .cacheInMemory(false) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(false) // 加载图片时会在磁盘中加载缓存
                .displayer(new CircleBitmap()) // 设置用户加载图片task(这里是圆角图片显示)
                .build();

        mInflater = LayoutInflater.from(Global.context);
        ActiveViewHolder holder = null;
        if (convertView == null) {
            holder = new ActiveViewHolder();
            // 初始化绑定控件
            convertView = mInflater.inflate(R.layout.my_collection_listview_item,null);
            holder.my_collection_head= (ImageView) convertView.findViewById(R.id.my_collection_head);
            holder.my_collection_name= (TextView) convertView.findViewById(R.id.my_collection_name);
            holder.user_id=((TextView) convertView.findViewById(R.id.my_collection_id));

            convertView.setTag(holder);
         } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }
       // holder.media_id.setText(String.valueOf(datas.get(position).getMediaId()));
        holder.my_collection_name.setText(datas.get(position).getUserName());
        holder.user_id.setText(String.valueOf(datas.get(position).getUserId()));

        //ImageLoader.getInstance().displayImage( ima[position], holder.media_pic);
        ImageLoader.getInstance().displayImage( datas.get(position).getUserHead(), holder.my_collection_head,options);
        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {

        private TextView user_id = null;
        private ImageView my_collection_head = null;
        private TextView my_collection_name = null;


        @Override
        public void initView() {
        }


        public ImageView getMy_collection_head() {
            return my_collection_head;
        }

        public void setMy_collection_head(ImageView my_collection_head) {
            this.my_collection_head = my_collection_head;
        }
        public TextView getMy_collection_name() {
            return my_collection_name;
        }

        public void setMy_collection_name(TextView my_collection_name) {
            this.my_collection_name = my_collection_name;
        }


        public TextView getUser_id() {
            return user_id;
        }

        public void setUser_id(TextView user_id) {
            this.user_id = user_id;
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
