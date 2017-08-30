package com.shengrui.huilian.main_sideslip.media_enter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shengrui.huilian.MainActivity;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;
import com.shengrui.huilian.medium_infor.MeduimInforPresanter;
import com.shengrui.huilian.medium_infor.MyOptionsPickerView;
import com.shengrui.huilian.medium_infor.TakePhotoPopWin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ChenXQ on 2016/8/5.
 */
public class Media_enter_info extends Activity implements View.OnClickListener{

    private RelativeLayout back;
    private ImageView media_enter_head;
    private TextView media_enter_name;
    private TextView media_enter_num;
    private EditText media_enter_introduce;
    private EditText fans_num;
    private Button fans_upload;
    private TextView fans_address;
    private TextView read_num;
    private RelativeLayout media_enter_province;
    private TextView media_province;
    private RelativeLayout media_enter_type;
    private TextView media_type;
    private RelativeLayout media_enter_school;
    private TextView media_school;
    private Button next;

    private Bundle bundle = null;
    private String mediaName = null;
    private String wechatNum = null;
    private String intro = null;
    private String twoCode = null;
    private String wechatHead = null;
    private String a=null;

    private String[] listSchool;
    private ArrayList<String> mListSchool = new ArrayList<String>();
    private ArrayList<String> mListProvince = new ArrayList<String>();
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    private String[][] province_city;
    private String[] listSort;
    private ArrayList<String>m ;
    private MyOptionsPickerView<String> mOpv;
    private ArrayList<String> mListSort = new ArrayList<String>();
    private String province_city_Text;
    private String sortText;
    private String cityName;
    private String provinceName;

    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    private Bitmap photo;
    TakePhotoPopWin takePhotoPopWin = null;
    private MeduimInforPresanter mMeduimInforPresanter = MeduimInforPresanter.getInstance();
    private TextView textNum;
    DisplayImageOptions options; // 配置图片加载及显示选项
    Uri selectedImage = null;

    private File picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_enter_info);
        init();
        setInfo();
        vp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //数据回传失败
        if (resultCode == NONE) {
            return;
        }
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            try{
                selectedImage = data.getData(); //获取系统返回的照片的Uri
                ImageSize mImageSize = new ImageSize(220, 215);
                ImageLoader.getInstance().loadImage(selectedImage.toString(), mImageSize,options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        File picture = new File(Utils.getRealFilePath(selectedImage));
                        try {
                            FileOutputStream bos = new FileOutputStream(picture);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                            bos.flush();
                            bos.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fans_address.setText("已上传");
                        a=picture.getPath();
                        takePhotoPopWin.dismiss();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }

        //返回数据为空
        if (data == null)
            return;

        // 处理结果
        if (requestCode == PHOTORESOULT) {
            try{
                selectedImage = data.getData(); //获取系统返回的照片的Uri
                ImageSize mImageSize = new ImageSize(220, 215);
                ImageLoader.getInstance().loadImage(selectedImage.toString(), mImageSize,options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        Date date = new Date();
                        picture = new File(Environment.getExternalStorageDirectory()+"/"+new SimpleDateFormat("yyyyMMddssSSS").format(date)+".jpg");
                        try {
                            FileOutputStream bos = new FileOutputStream(picture);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                            bos.flush();
                            bos.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fans_address.setText("已上传");
                        a=picture.getPath();
                        takePhotoPopWin.dismiss();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
//                uploadData(selectedImage.getPath().toString());
//                Log.d("456+++++++++++++++++++++", selectedImage.getPath().toString());

            }catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }

    }


    private void vp() {
        province_city = new String[][]{
                {"北京", "朝阳区", "海淀区", "通州区", "房山区", "丰台区", "昌平区", "大兴区", "顺义区", "西城区", "延庆县", "石景山区", "宣武区", "怀柔区", "崇文区", "密云县", "东城区", "门头沟区", "平谷区"},
                {"天津", "和平区", "北辰区", "河北区", "河西区", "西青区", "津南区", "东丽区", "武清区", "宝坻区", "红桥区", "大港区", "汉沽区", ",海县", "宁河县", "塘沽区", "蓟县", "南开区", "河东区"},
                {"上海", "松江区", "宝山区", "金山区", "嘉定区", "南汇区", "青浦区", "浦东新区", "奉贤区", "闵行区", "徐汇区", "静安区", "黄浦区", "普陀区", "杨浦区", "虹口区", "闸北区", "长宁区", "崇明县", "卢湾区"},
                {"重庆", "江北区", "渝北区", "沙坪坝区", "九龙坡区", "万州区", "永川市", "南岸区", "酉阳县", "北碚区", "涪陵区", "秀山县", "巴南区", "渝中区", "石柱县", "忠县", "合川市", "大渡口区", "开县", "长寿区", "荣昌县", "云阳县", "梁平县", "潼南县", "江津市", "彭水县", "璧山县", "綦江县", "大足县", "黔江区", "巫溪县", "巫山县", "垫江县", "丰都县", "武隆县", "万盛区", "铜梁县", "南川市", "奉节县", "双桥区", "城口县"},
                {"河北", "河北区", "石家庄市", "保定市", "秦皇岛", "唐山市", "邯郸市", "邢台市", "沧州市", "承德市", "廊坊市", "衡水市", "张家口"},
                {"河南", "郑州市", "洛阳市", "焦作市", "商丘市", "信阳市", "新乡市", "安阳市", "开封市", "漯河市", "南阳市", "鹤壁市", "平顶山", "濮阳市", "许昌市", "周口市", "三门峡", "驻马店", "徐州市"},
                {"云南", "昆明市", "红河州", "大理州", "文山州", "德宏州", "曲靖市", "昭通市", "楚雄州", "保山市", "玉溪市", "丽江地区", "临沧地区", "思茅地区", "西双版纳州", "怒江州", "迪庆州"},
                {"辽宁", "大连市", "沈阳市", "丹东市", "辽阳市", "葫芦岛市", "锦州市", "朝阳市", "营口市", "鞍山市", "抚顺市", "阜新市", "本溪市", "盘锦市", "铁岭市"},
                {"黑龙江", "齐齐哈尔市", "哈尔滨市", "大庆市", "佳木斯市", "双鸭山市", "牡丹江市", "鸡西市", "黑河市", "绥化市", "鹤岗市", "伊春市", "大兴安岭地区", "七台河市"},
                {"湖南", "长沙市", "邵阳市", "常德市", "衡阳市", "株洲市", "湘潭市", "永州市", "岳阳市", "怀化市", "郴州市", "娄底市", "益阳市", "张家界市", "湘西州"},
                {"安徽", "芜湖市", "合肥市", "六安市", "宿州市", "阜阳市", "安庆市", "马鞍山市", "蚌埠市", "淮北市", "淮南市", "宣城市", "黄山市", "铜陵市", "亳州市", "池州市", "巢湖市", "滁州市"},
                {"山东", "济南市", "青岛市", "临沂市", "济宁市", "菏泽市", "烟台市", "泰安市", "淄博市", "潍坊市", "日照市", "威海市", "滨州市", "东营市", "聊城市", "德州市", "莱芜市", "枣庄市"},
                {"新疆", "乌鲁木齐市", "伊犁州", "昌吉州", "石河子市", "哈密地区", "阿克苏地区", "巴音郭楞州", "喀什地区", "塔城地区", "克拉玛依市", "和田地区", "阿勒泰州", "吐鲁番地区", "阿拉尔市", "博尔塔拉州", "五家渠市", "克孜勒苏州", "图木舒克市"},
                {"江苏", "苏州市", "徐州市", "盐城市", "无锡市", "南京市", "南通市", "连云港市", "常州市", "扬州市", "镇江市", "淮安市", "泰州市", "宿迁市"},
                {"浙江", "温州市", "宁波市", "杭州市", "台州市", "嘉兴市", "金华市", "湖州市", "绍兴市", "舟山市", "丽水市", "衢州市"},
                {"江西", "南昌市", "赣州市", "上饶市", "吉安市", "九江市", "新余市", "抚州市", "宜春市", "景德镇市", "萍乡市", "鹰潭市"},
                {"湖北", "武汉市", "宜昌市", "襄樊市", "荆州市", "恩施州", "孝感市", "黄冈市", "十堰市", "咸宁市", "黄石市", "仙桃市", "随州市", "天门市", "荆门市", "潜江市", "鄂州市", "神农架林区"},
                {"广西", "贵港市", "玉林市", "北海市", "南宁市", "柳州市", "桂林市", "梧州市", "钦州市", "来宾市", "河池市", "百色市", "贺州市", "崇左市", "防城港市"},
                {"甘肃", "兰州市", "天水市", "庆阳市", "武威市", "酒泉市", "张掖市", "陇南地区", "白银市", "定西地区", "平凉市", "嘉峪关市", "临夏回族自治州", "金昌市", "甘南州"},
                {"山西", "太原市", "大同市", "运城市", "长治市", "晋城市", "忻州市", "临汾市", "吕梁市", "晋中市", "阳泉市", "朔州市"},
                {"内蒙古", "赤峰市", "包头市", "通辽市", "呼和浩特市", "乌海市", "鄂尔多斯市", "呼伦贝尔市", "兴安盟", "巴彦淖尔盟", "乌兰察布盟", "锡林郭勒盟", "阿拉善盟"},
                {"陕西", "西安市", "咸阳市", "宝鸡市", "汉中市", "渭南市", "安康市", "榆林市", "商洛市", "延安市", "铜川市"},
                {"吉林", "吉林市", "长春市", "白山市", "白城市", "延边州", "松原市", "辽源市", "通化市", "四平市"},
                {"福建", "漳州市", "泉州市", "厦门市", "福州市", "莆田市", "宁德市", "三明市", "南平市", "龙岩市"},
                {"贵州省", "贵阳市", "黔东南州", "黔南州", "遵义市", "黔西南州", "毕节地区", "铜仁地区", "安顺市", "六盘水市"},
                {"广东", "广州市", "东莞市", "中山市", "深圳市", "惠州市", "江门市", "珠海市", "汕头市", "佛山市", "湛江市", "河源市", "肇庆市", "潮州市", "清远市", "韶关市", "揭阳市", "阳江市", "云浮市", "茂名市", "梅州市", "汕尾市"},
                {"青海", "西宁市", "海西州", "海东地区海北州", "果洛州", "玉树州", "黄南藏族自治州"},
                {"西藏", "拉萨市", "山南地区", "林芝地区", "日喀则地区", "阿里地区", "昌都地区", "那曲地区"},
                {"四川", "成都市", "绵阳市", "广元市", "达州市", "南充市", "德阳市", "广安市", "阿坝州", "巴中市", "遂宁市", "内江市", "凉山州", "攀枝花市", "乐山市", "自贡市", "泸州市", "雅安市", "宜宾市", "资阳市", "眉山市", "甘孜州"},
                {"宁夏", "银川市", "吴忠市", "中卫市", "石嘴山市", "固原市"},
                {"海南", "三亚市", "海口市", "琼海市", "文昌市", "东方市", "昌江县", "陵水县", "乐东县", "五指山市", "保亭县", "澄迈县", "万宁市", "儋州市", "临高县", "白沙县", "定安县", "琼中县", "屯昌县"},
                {"台湾", "台北市", "高雄市", "台中市", "新竹市", "基隆市", "台南市", "嘉义市"},
                {"香港", ""}, {"澳门", ""}
        };

        for (int i = 0; i < province_city.length; i++) {
            mListProvince.add(province_city[i][0]);
            m = new ArrayList<String>();
            for (int j = 1; j < province_city[i].length; j++) {
                m.add(province_city[i][j]);
            }
            mListCiry.add(m);
        }

        listSort = new String[]{"财经", "宠物", "电商", "动漫", "购物", "旅游", "美食", "明星", "家居", "养生", "商业", "励志", "搞笑",
                "汽车", "情感", "生活", "时尚", "体育", "音乐", "营销", "游戏", "职场", "母婴", "影视", "科技", "娱乐", "高校", "互联网", "自媒体",
                "地区号", "私密话", "白领个人", "报刊杂志", "创业投资",
                "文学艺术", "新闻资讯", "星座语录", "运动健身", "知识百科", "其他"};
        for (int i = 0; i < listSort.length; i++) {
            mListSort.add(listSort[i]);
        }
    }

    private void pickViewListener(int agency){
        mOpv = new MyOptionsPickerView<String>(this);
        mOpv.setTitle("请选择");
        switch(agency) {
            case 1:
                mOpv.setPicker(mListProvince, mListCiry, true);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        province_city_Text = mListProvince.get(options1)+" "+mListCiry.get(options1).get(option2);
                        provinceName = mListProvince.get(options1);
                        cityName = mListCiry.get(options1).get(option2);
                        media_province.setText(province_city_Text);
                        mMeduimInforPresanter.findSchool(cityName,new FinishCallBack() {
                            public void onSuccess() {
                                mListSchool.clear();
                                listSchool = mMeduimInforPresanter.school;
                            }

                            public void onFailure() {
                                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //                progressDialog.dismiss();
                    }
                });
                break;
            case 2:
                mOpv.setPicker(mListSort);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        sortText = mListSort.get(options1);
                        media_type.setText(sortText);
                    }
                });
                break;
            case 3:
                mListSchool.clear();
                for(int i=0;i<listSchool.length;i++){
                    mListSchool.add(listSchool[i]);
                }
                mListSchool.add("无");
                mOpv.setPicker(mListSchool);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        media_school.setText(mListSchool.get(options1));
                    }
                });
                break;
            default:
                break;
        }

    }

    private void setInfo() {
        bundle=this.getIntent().getExtras();
        mediaName = bundle.getString("mediaName");
        wechatNum = bundle.getString("wechatNum");
        intro = bundle.getString("intro");
        wechatHead = bundle.getString("wechatHead");
        twoCode = bundle.getString("twoCode");

        media_enter_name.setText(mediaName);
        media_enter_head.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(wechatHead)));
        media_enter_num.setText(wechatNum);
        media_enter_introduce.setText(intro);
    }

    private void init() {

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Global.getContext()));

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(false) // 加载图片时会在磁盘中加载缓存
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        textNum = (TextView) findViewById(R.id.textNum);
        next = (Button) findViewById(R.id.next);
        back = (RelativeLayout) findViewById(R.id.back);
        media_enter_head = (ImageView) findViewById(R.id.media_enter_head);
        media_enter_name = (TextView) findViewById(R.id.media_enter_name);
        media_enter_num = (TextView) findViewById(R.id.media_enter_num);
        media_enter_introduce = (EditText) findViewById(R.id.media_enter_introduce);
        fans_num = (EditText) findViewById(R.id.fans_num);
        fans_upload = (Button) findViewById(R.id.fans_upload);
        fans_address = (TextView) findViewById(R.id.fans_address);
        read_num = (TextView) findViewById(R.id.read_num);
        media_enter_province = (RelativeLayout) findViewById(R.id.media_enter_province);
        media_province = (TextView) findViewById(R.id.media_province);
        media_enter_type = (RelativeLayout) findViewById(R.id.media_enter_type);
        media_type = (TextView) findViewById(R.id.media_type);
        media_enter_school = (RelativeLayout) findViewById(R.id.media_enter_school);
        media_school = (TextView) findViewById(R.id.media_school);

        takePhotoPopWin = new TakePhotoPopWin(this);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        fans_upload.setOnClickListener(this);
        media_enter_province.setOnClickListener(this);
        media_enter_type.setOnClickListener(this);
        media_enter_school.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                if(media_enter_introduce.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有填写介绍", Toast.LENGTH_SHORT).show();
                    return;
                } else if(fans_num.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有填写粉丝数", Toast.LENGTH_SHORT).show();
                    return;
                } else if(a==null){
                    Toast.makeText(Media_enter_info.this, "没有上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }else if(read_num.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有填写周平均阅读量", Toast.LENGTH_SHORT).show();
                    return;
                } else if(media_type.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有选择账号分类", Toast.LENGTH_SHORT).show();
                    return;
                } else if(media_province.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有选择省市", Toast.LENGTH_SHORT).show();
                    return;
                } else if(media_school.length()<1){
                    Toast.makeText(Media_enter_info.this, "没有选择学院", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(this, Media_enter_price.class).putExtra("mediaName", mediaName)
                        .putExtra("wechatNum", wechatNum).putExtra("intro", intro).putExtra("twoCode", twoCode)
                        .putExtra("wechatHead", wechatHead).putExtra("fansNum", fans_num.getText().toString().trim()).putExtra("sort", media_type.getText().toString().trim())
                        .putExtra("readNum", read_num.getText().toString().trim()).putExtra("cityName", cityName).putExtra("provinceName", provinceName)
                        .putExtra("fansPicture",a).putExtra("schoolName",media_school.getText().toString().trim()));
                break;
            case R.id.back:
                Media_enter_info.this.finish();
                break;
            case R.id.media_enter_province:
                pickViewListener(1);
                mOpv.show();
                break;
            case R.id.media_enter_type:
                pickViewListener(2);
                mOpv.show();
                break;
            case R.id.media_enter_school:
                pickViewListener(3);
                mOpv.show();
                break;
            case R.id.fans_upload:
                takePhotoPopWin.TakePhoto(onClickListener);
                takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
            default:
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory(), "temp.jpg")));
                        startActivityForResult(intent, PHOTOHRAPH);  //带数据回传跳转
                    } else {
                        Toast.makeText(Media_enter_info.this, "没有SD卡", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.btn_pick_photo:
                    //跳转到选择图片（相册或文件管理等，由用户选择）
                    Intent intentp = new Intent(Intent.ACTION_PICK, null);
                    //设置uri以及文件类型
                    //MediaStore这个类是android系统提供的一个多媒体数据库，Images.Media代表图片信息，
                    //这个Uri代表要查询的数据库名称加上表的名称
                    //跳转数据为图片数据库，类型为任何图片类型
                    intentp.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intentp, PHOTORESOULT);  //带数据回传跳转
                    break;
                case R.id.btn_cancel:
                    takePhotoPopWin.dismiss();
            }
        }
    };
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            textNum.setText(String.valueOf(s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = media_enter_introduce.getSelectionStart();
            editEnd = media_enter_introduce.getSelectionEnd();
            if (temp.length() > 150) {
                Toast.makeText(Media_enter_info.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                media_enter_introduce.setText(s);
                media_enter_introduce.setSelection(tempSelection);
            }
        }
    };
}
