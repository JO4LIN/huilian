package com.shengrui.huilian.slide_choose;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Adapter.BaseDataAdapter;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */
public class SlideChoose_adapter extends BaseDataAdapter<SlideChoose_model> {
    String ss="https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1047640764,1894277665&fm=116&gp=0.jpg";
    private LayoutInflater mInflater;
    public SlideChoose_adapter(List<SlideChoose_model> datas) {
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
            convertView = mInflater.inflate(R.layout.allmedia_isselected_item, null);
            holder.media_id= (TextView) convertView.findViewById(R.id.media_id);
            holder.media_pic= (ImageView) convertView.findViewById(R.id.media_pic);
            holder.media_name=((TextView) convertView.findViewById(R.id.media_name));
            holder.media_fansnum=((TextView) convertView.findViewById(R.id.media_fansnum));
            holder.media_readingnum=((TextView) convertView.findViewById(R.id.media_readingnum));
            holder.hardSimplePrice= (TextView) convertView.findViewById(R.id.subscription_price);
            holder.media_isverification=((ImageView) convertView.findViewById(R.id.subscription_verification));
            convertView.setTag(holder);
         } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }
        if(datas.get(position).getIsVerification()){
            holder.media_isverification.setImageResource(R.drawable.icon_verification_green);
        }else {
            holder.media_isverification.setImageResource(R.drawable.icon_verification);
        }
        if((Integer.valueOf(datas.get(position).getReadNum())/10000)!=0){
            holder.media_readingnum.setText(String.valueOf(Integer.valueOf(datas.get(position).getReadNum()) / 10000) + "万");
        }else {
            holder.media_readingnum.setText(String.valueOf(datas.get(position).getReadNum()));
        }
        if((Integer.valueOf(datas.get(position).getFansNum())/10000)!=0){
            holder.media_fansnum.setText(String.valueOf(Integer.valueOf(datas.get(position).getFansNum())/10000)+"万");
        }else {
            holder.media_fansnum.setText(String.valueOf(datas.get(position).getFansNum()));
        }
        holder.media_id.setText(String.valueOf(datas.get(position).getMediaId()));
        holder.media_name.setText(datas.get(position).getMediaName());
        holder.hardSimplePrice.setText(String.valueOf(datas.get(position).getHardSimplePrice()));

     //   holder.media_pic.setImageBitmap(Utils.toRoundBitmap(ImageLoader.getInstance().loadImageSync(datas.get(position).getWechatHead())));
        ImageLoader.getInstance().displayImage(datas.get(position).getWechatHead(), holder.media_pic);

        return convertView;
    }

    public String getItemContent(int position) {
        // TODO Auto-generated method stub
        return String.valueOf(datas.get(position).getMediaId());
    }

    public final class ActiveViewHolder implements ViewHolder {
        private ImageView media_pic = null;
        private TextView media_id=null;
        private TextView media_name = null;
        private TextView media_fansnum = null;
        private TextView media_readingnum = null;
        private ImageView media_isverification = null;
        private TextView hardSimplePrice = null;
        @Override
        public void initView() {
        }

        public ImageView getMedia_pic() {
            return media_pic;
        }

        public void setMedia_pic(ImageView media_pic) {
            this.media_pic = media_pic;
        }

        public TextView getMedia_name() {
            return media_name;
        }

        public void setMedia_name(TextView media_name) {
            this.media_name = media_name;
        }

        public TextView getMedia_fansnum() {
            return media_fansnum;
        }

        public void setMedia_fansnum(TextView media_fansnum) {
            this.media_fansnum = media_fansnum;
        }

        public TextView getMedia_readingnum() {
            return media_readingnum;
        }

        public void setMedia_readingnum(TextView media_readingnum) {
            this.media_readingnum = media_readingnum;
        }


        public TextView getMedia_id() {
            return media_id;
        }

        public void setMedia_id(TextView media_id) {
            this.media_id = media_id;
        }

        public TextView getHardSimplePrice() {
            return hardSimplePrice;
        }

        public void setHardSimplePrice(TextView hardSimplePrice) {
            this.hardSimplePrice = hardSimplePrice;
        }

        public ImageView getMedia_isverification() {
            return media_isverification;
        }

        public void setMedia_isverification(ImageView media_isverification) {
            this.media_isverification = media_isverification;
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
