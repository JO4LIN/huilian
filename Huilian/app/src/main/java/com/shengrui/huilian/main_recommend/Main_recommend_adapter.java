package com.shengrui.huilian.main_recommend;

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
public class Main_recommend_adapter extends BaseDataAdapter<Main_recommend_model> {

    private LayoutInflater mInflater;
    public Main_recommend_adapter(List<Main_recommend_model> datas) {
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
                .displayer(new RoundedBitmapDisplayer(20)) // 设置用户加载图片task(这里是圆角图片显示)
                .build();


        mInflater = LayoutInflater.from(Global.context);
        ActiveViewHolder holder = null;
//        if (convertView == null) {
            holder = new ActiveViewHolder();
            // 初始化绑定控件

            convertView = mInflater.inflate(R.layout.huilian_mian_listview_item,null);
            holder.mediaId = (TextView) convertView.findViewById(R.id.media_id);
            holder.mediaHead= (ImageView) convertView.findViewById(R.id.subscription_head_portrait);
            holder.mediaName= (TextView) convertView.findViewById(R.id.subscription_name);
            holder.price=((TextView) convertView.findViewById(R.id.subscription_price));
            holder.readNum=((TextView) convertView.findViewById(R.id.look_num));
            holder.fansNum=((TextView) convertView.findViewById(R.id.fans_num));
            holder.media_isverification=((ImageView) convertView.findViewById(R.id.subscription_verification));

            convertView.setTag(holder);
//         } else {
//            holder = (ActiveViewHolder) convertView.getTag();
//        }
       // holder.media_id.setText(String.valueOf(datas.get(position).getMediaId()));
        holder.mediaName.setText(datas.get(position).getMediaName());
        holder.price.setText(String.valueOf(datas.get(position).getHardSimplePrice()));
        if((Integer.valueOf(datas.get(position).getReadNum())/10000)!=0){
            holder.readNum.setText(String.valueOf(Integer.valueOf(datas.get(position).getReadNum())/10000)+"万");
        }else {
            holder.readNum.setText(String.valueOf(datas.get(position).getReadNum()));
        }
        if((Integer.valueOf(datas.get(position).getFansNum())/10000)!=0){
            holder.fansNum.setText(String.valueOf(Integer.valueOf(datas.get(position).getFansNum())/10000)+"万");
        }else {
            holder.fansNum.setText(String.valueOf(datas.get(position).getFansNum()));
        }
        holder.mediaId.setText(String.valueOf(datas.get(position).getMediaId()));
        Log.d("++++++++++++++++>", String.valueOf(datas.get(position).getIsVerification()));
        if(datas.get(position).getIsVerification()){
            holder.media_isverification.setImageResource(R.drawable.icon_verification_green);
        }

        //ImageLoader.getInstance().displayImage( ima[position], holder.media_pic);
        ImageLoader.getInstance().displayImage(datas.get(position).getWechatHead(), holder.mediaHead);


        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {

        private ImageView mediaHead= null;
        private TextView mediaName = null;
        private TextView price = null;
        private TextView readNum = null;
        private TextView fansNum = null;
        private TextView mediaId;
        private ImageView media_isverification = null;

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
        public ImageView getMedia_isverification() {
            return media_isverification;
        }

        public void setMedia_isverification(ImageView media_isverification) {
            this.media_isverification = media_isverification;
        }

        @Override
        public void initView() {
        }


        public TextView getReadNum() {
            return readNum;
        }

        public void setReadNum(TextView readNum) {
            this.readNum = readNum;
        }

        public TextView getFansNum() {
            return fansNum;
        }

        public void setFansNum(TextView fansNum) {
            this.fansNum = fansNum;
        }

        public TextView getMediaId() {
            return mediaId;
        }

        public void setMediaId(TextView mediaId) {
            this.mediaId = mediaId;
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
                    FadeInBitmapDisplayer.animate(imageView, 10); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }
}
