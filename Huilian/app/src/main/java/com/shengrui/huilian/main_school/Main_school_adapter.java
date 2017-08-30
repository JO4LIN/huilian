package com.shengrui.huilian.main_school;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Adapter.BaseDataAdapter;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.CircleBitmap;

import java.util.List;

/**
 * Created by zhrx on 2016/4/19.
 */
public class Main_school_adapter extends BaseDataAdapter<Main_school_model> {
    private LayoutInflater mInflater;
    public Main_school_adapter(List<Main_school_model> datas) {
        super(datas);
    }
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
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Global.getContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

    //    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Global.getContext()));
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
            convertView = mInflater.inflate(R.layout.typeschool_item, null);
           // holder.getTitle().setImageBitmap(returnBitMap(datas.get(position).getTitle()));
//            holder.school_pic= (ImageView) convertView.findViewById(R.id.school_pic);
            holder.school_id= (TextView) convertView.findViewById(R.id.school_id);
            holder.school_name= (TextView) convertView.findViewById(R.id.school_name);
            convertView.setTag(holder);
        } else {
            holder = (ActiveViewHolder) convertView.getTag();
        }
        holder.getSchool_id().setText(datas.get(position).getSchoolId());
        holder.getSchool_name().setText(datas.get(position).getSchoolName());
//        ImageLoader.getInstance().displayImage( datas.get(position).getSchoolHead(), holder.school_pic,options);
        //ImageLoader.getInstance().displayImage(testurl, holder.school_pic);
        return convertView;
    }

    public final class ActiveViewHolder implements ViewHolder {
//        private ImageView school_pic = null;
        private TextView school_name = null;
        private TextView school_id = null;
        @Override
        public void initView() {
        }
//        public ImageView getSchool_pic() {
//            return school_pic;
//        }
//        public void setSchool_pic(ImageView school_pic) {
//            this.school_pic = school_pic;
//        }
        public TextView getSchool_name() {
            return school_name;
        }
        public void setSchool_name(TextView school_name) {
            this.school_name = school_name;
        }

        public TextView getSchool_id() {
            return school_id;
        }

        public void setSchool_id(TextView school_id) {
            this.school_id = school_id;
        }
    }
}
