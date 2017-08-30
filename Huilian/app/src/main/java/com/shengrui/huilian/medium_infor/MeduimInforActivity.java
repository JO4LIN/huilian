package com.shengrui.huilian.medium_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.base_mvp.net.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jh on 2016/4/24.
 */
public class MeduimInforActivity  extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> mListProvince = new ArrayList<String>();
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    private String[][] province_city;
    private String[] listSchool;
    private String[] listOpen;
    private String[] listSort;
    private MyOptionsPickerView<String> mOpv;
    private ArrayList<String>m ;
    private ArrayList<String> mListSchool = new ArrayList<String>();
    private ArrayList<String> mListOpen = new ArrayList<String>();
    private ArrayList<String> mListSort = new ArrayList<String>();
    private String openText;
    private String schoolText;
    private String province_city_Text;
    private String sortText;

    private View view = null;
    //项用于触发点击
    private ImageView med_mainHead = null;         //公众号头像
    private TextView med_officialAccounts= null;  //公众号
    private TextView med_weixinNumberTitle= null; //微信号项
    private TextView med_weixinNumber= null;       //微信号
    private TextView med_codeTitle= null;          //二维码项
    private TextView med_followers= null;          //粉丝数
    private TextView med_area= null;                //地区
    private TextView med_schoolTitle= null;          //学校项
    private TextView med_school= null;                //学校
    private TextView med_sortTitle= null;          //账号分类项
    private TextView med_sort= null;                //账号分类
    private TextView med_multiOneTitle= null;      //多图文第一条项
    private TextView med_multiOne= null;           //多图文第一条（软）
    private TextView med_multiOne2= null;          //多图文第一条（硬）
    private TextView med_multiTwoTitle= null;     //多二项
    private TextView med_multiTwo= null;           //多二硬
    private TextView med_multiTwo2= null;          //多二软
    private TextView med_multiOtherTitle= null;   //多其他项
    private TextView med_multiOther= null;         //多其他软
    private TextView med_multiOther2= null;        //多其他硬
    private TextView med_singleTitle= null;        //单图文项
    private TextView med_single= null;              //单软
    private TextView med_single2= null;             //单硬
    private TextView med_readers = null;            //阅读量
    private RelativeLayout back= null;                   //联系
    private Uri cropPhoto;
    private int mediaId ;

    Intent intent = null;                           //用于保存或获取数据（数据传递）
    String title = null;                           //更改信息页面标题
    private String photoSort = null;
    private TextView thisTextView = null;        //当前更改信息对应的TextView控件
    private TextView thisPriceSoft = null;       //当前广告类型的软广价
    private TextView thisPriceHard = null;       //当前广告类型的硬广价

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    TakePhotoPopWin takePhotoPopWin = null;
   CheckCodePopWin checkCodePopWin = null;

    //输出的类型可以是imgae的任何类型，包括jpg/bmp/gif等等
    public static final String IMAGE_UNSPECIFIED = "image/*";
    Bitmap photoFinal = null;
    Bitmap photo = null;
    ProgressDialog progressDialog = null;

    private MeduimInforPresanter mMeduimInforPresanter = MeduimInforPresanter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium);
        mMeduimInforPresanter.setContext(MeduimInforActivity.this);
        initialize();      //初始化控件
        control();         //设置触发事件
        vp();
        refresh();
    }

    private void refresh() {
//        progressDialog = ProgressDialog.show(mMeduimInforPresanter.context, "Loading", "Please wait...", true, true);
        mMeduimInforPresanter.refreshAllMessage(mediaId,mediaId,new FinishCallBack() {
            public void onSuccess() {
                checkCodePopWin.setCode(Utils.returnBitMap(mMeduimInforPresanter.mMeduimInforModel.getTwoCode()));
                med_mainHead.setImageBitmap(Utils.toRoundBitmap(Utils.returnBitMap(mMeduimInforPresanter.mMeduimInforModel.getWechatHead())));
                med_officialAccounts.setText(mMeduimInforPresanter.mMeduimInforModel.getMediaName());
                med_weixinNumber.setText(mMeduimInforPresanter.mMeduimInforModel.getWechatNum());
                med_followers.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getFansNum()));
                med_area.setText(mMeduimInforPresanter.mMeduimInforModel.getCity());
                med_school.setText(mMeduimInforPresanter.mMeduimInforModel.getSchool());
                med_sort.setText(mMeduimInforPresanter.mMeduimInforModel.getMedType());
                mediaId = mMeduimInforPresanter.mMeduimInforModel.getMediaId();
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreFirPrice()).equals("-1")){
                    med_multiOne.setText("不接单");
                }else {
                    med_multiOne.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreFirPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreFirPrice()).equals("-1")){
                    med_multiOne2.setText("不接单");
                }else {
                    med_multiOne2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreFirPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreSecPrice()).equals("-1")){
                    med_multiTwo.setText("不接单");
                }else {
                    med_multiTwo.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreSecPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreSecPrice()).equals("-1")){
                    med_multiTwo2.setText("不接单");
                }else {
                    med_multiTwo2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreSecPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreOtherPrice()).equals("-1")){
                    med_multiOther.setText("不接单");
                }else {
                    med_multiOther.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftMoreOtherPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreOtherPrice()).equals("-1")){
                    med_multiOther2.setText("不接单");
                }else {
                    med_multiOther2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardMoreOtherPrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftSimplePrice()).equals("-1")){
                    med_single.setText("不接单");
                }else{
                    med_single.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getSoftSimplePrice()));
                }
                if(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardSimplePrice()).equals("-1")){
                    med_single2.setText("不接单");
                }else {
                    med_single2.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getHardSimplePrice()));
                }
                med_readers.setText(String.valueOf(mMeduimInforPresanter.mMeduimInforModel.getReadNum()));
//                progressDialog.dismiss();
            }
            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }
        });
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
                {"广东", "广州市","东莞市" , "中山市", "深圳市", "惠州市", "江门市", "珠海市", "汕头市", "佛山市", "湛江市", "河源市", "肇庆市", "潮州市", "清远市", "韶关市", "揭阳市", "阳江市", "云浮市", "茂名市", "梅州市", "汕尾市"},
                {"青海", "西宁市", "海西州", "海东地区海北州", "果洛州", "玉树州", "黄南藏族自治州"},
                {"西藏", "拉萨市", "山南地区", "林芝地区", "日喀则地区", "阿里地区", "昌都地区", "那曲地区"},
                {"四川", "成都市", "绵阳市", "广元市", "达州市", "南充市", "德阳市", "广安市", "阿坝州", "巴中市", "遂宁市", "内江市", "凉山州", "攀枝花市", "乐山市", "自贡市", "泸州市", "雅安市", "宜宾市", "资阳市", "眉山市", "甘孜州"},
                {"宁夏", "银川市", "吴忠市", "中卫市", "石嘴山市", "固原市"},
                {"海南", "三亚市", "海口市", "琼海市", "文昌市", "东方市", "昌江县", "陵水县", "乐东县", "五指山市", "保亭县", "澄迈县", "万宁市", "儋州市", "临高县", "白沙县", "定安县", "琼中县", "屯昌县"},
                {"台湾", "台北市", "高雄市", "台中市", "新竹市", "基隆市", "台南市", "嘉义市"},
                {"香港",""}, {"澳门",""}
        };
        //  System.out.print(province_city.length);
        for(int i=0;i<province_city.length;i++){
            mListProvince.add(province_city[i][0]);
            m = new ArrayList<String>();
            for(int j=1;j<province_city[i].length;j++){
                m.add(province_city[i][j]);
            }
            mListCiry.add(m);
        }


        listSort = new String[]{"财经","宠物","电商","动漫","购物","旅游","美食","明星","家居","养生","商业","励志","搞笑",
                "汽车","情感","生活","时尚","体育","音乐","营销","游戏","职场","母婴","影视","科技","娱乐","高校","互联网","自媒体",
                "地区号","私密话","白领个人","报刊杂志","创业投资",
                "文学艺术", "新闻资讯","星座语录","运动健身","知识百科","其他"};
        for(int i=0;i<listSort.length;i++){
            mListSort.add(listSort[i]);
        }


    }

    private void pickViewListener(int agency){
        mOpv = new MyOptionsPickerView<String>(this);
        mOpv.setTitle("请选择");
        switch(agency) {
//            case 7:
//                mOpv.setPicker(mListProvince, mListCiry, true);
//                mOpv.setCyclic(false, false, false);
//                mOpv.setSelectOptions(0, 0, 0);
//                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int option2, int options3) {
//                        // 返回的分别是三个级别的选中位置
//                        province_city_Text = mListCiry.get(options1).get(option2);
////                        progressDialog = ProgressDialog.show(mMeduimInforPresanter.context,"Loading","Please wait...",true,true);
//                        mMeduimInforPresanter.loadingPrice(mediaId,mListProvince.get(options1), "province",
//                                mListCiry.get(options1).get(option2), "city",7, new FinishCallBack() {
//                                    public void onSuccess() {
//                                        med_area.setText(province_city_Text);
//                                        mMeduimInforPresanter.findSchool(province_city_Text,new FinishCallBack() {
//                                            public void onSuccess() {
//                                                mListSchool.clear();
//                                                listSchool = mMeduimInforPresanter.school;
//                                            }
//
//                                            public void onFailure() {
//                                            }
//                                        });
//                        //                progressDialog.dismiss();
//                                    }
//                                    public void onFailure() {
//                        //                progressDialog.dismiss();
//                                        Toast.makeText(mMeduimInforPresanter.context, "更改失败", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                });
//                break;
//            case 9:
//                mListSchool.clear();
//              //  listSchool = new String[]{"吉林大学珠海学院","北京理工大学珠海分校","北京师范大学珠海分校","暨南大学珠海校区","中山大学珠海校区"};
//                for(int i=0;i<listSchool.length;i++){
//                    mListSchool.add(listSchool[i]);
//                }
//                mListSchool.add("无");
//                mOpv.setPicker(mListSchool);
//                mOpv.setCyclic(false, false, false);
//                mOpv.setSelectOptions(0, 0, 0);
//                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int option2, int options3) {
//                        // 返回的分别是三个级别的选中位置
//                        schoolText = mListSchool.get(options1);
//               //         progressDialog = ProgressDialog.show(mMeduimInforPresanter.context,"Loading","Please wait...",true,true);
//                        mMeduimInforPresanter.loadingMessage(mediaId,schoolText, "school", 9, new FinishCallBack() {
//                            public void onSuccess() {
//                                med_school.setText(schoolText);
//                //                progressDialog.dismiss();
//                            }
//
//                            public void onFailure() {
//                                Toast.makeText(mMeduimInforPresanter.context, "更改失败", Toast.LENGTH_SHORT).show();
//                  //              progressDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//                break;
            case 6:
                mOpv.setPicker(mListSort);
                mOpv.setCyclic(false, false, false);
                mOpv.setSelectOptions(0, 0, 0);
                mOpv.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        // 返回的分别是三个级别的选中位置
                        sortText = mListSort.get(options1);
//                        progressDialog = ProgressDialog.show(mMeduimInforPresanter.context,"Loading","Please wait...",true,true);
                        mMeduimInforPresanter.loadingMessage(mediaId,sortText, "medType", 6, new FinishCallBack() {
                            public void onSuccess() {
                                med_sort.setText(sortText);
                       //         progressDialog.dismiss();
                            }

                            public void onFailure() {
                                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                        //        progressDialog.dismiss();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }

    }



    //初始化控件
    private void initialize() {
        Bundle bundle=this.getIntent().getExtras();
        mediaId = Integer.valueOf(bundle.getString("mediaId"));
        takePhotoPopWin = new TakePhotoPopWin(this);
        checkCodePopWin = new CheckCodePopWin(this);
        listSchool = new String[]{"吉林大学珠海学院","北京理工大学珠海分校","北京师范大学珠海分校","暨南大学珠海校区","中山大学珠海校区"};
        med_mainHead = (ImageView) findViewById(R.id.med_mainHead);
        med_officialAccounts = (TextView) findViewById(R.id.med_officialAccounts);
        med_weixinNumberTitle = (TextView) findViewById(R.id.med_weixinNumberTitle);
        med_weixinNumber = (TextView) findViewById(R.id.med_weixinNumber);
        med_codeTitle = (TextView) findViewById(R.id.med_codeTitle);
        med_followers = (TextView) findViewById(R.id.med_followers);
        med_area = (TextView) findViewById(R.id.med_area);
        med_schoolTitle = (TextView) findViewById(R.id.med_schoolTitle);
        med_school = (TextView) findViewById(R.id.med_school);
        med_sortTitle = (TextView) findViewById(R.id.med_sortTitle);
        med_sort = (TextView) findViewById(R.id.med_sort);
        med_multiOneTitle = (TextView) findViewById(R.id.med_multiOneTitle);
        med_multiOne = (TextView) findViewById(R.id.med_multiOne);
        med_multiOne2 = (TextView) findViewById(R.id.med_multiOne2);
        med_multiTwoTitle = (TextView) findViewById(R.id.med_multiTwoTitle);
        med_multiTwo = (TextView) findViewById(R.id.med_multiTwo);
        med_multiTwo2 = (TextView) findViewById(R.id.med_multiTwo2);
        med_multiOtherTitle = (TextView) findViewById(R.id.med_multiOtherTitle);
        med_multiOther = (TextView) findViewById(R.id.med_multiOther);
        med_multiOther2 = (TextView) findViewById(R.id.med_multiOther2);
        med_singleTitle = (TextView) findViewById(R.id.med_singleTitle);
        med_single = (TextView) findViewById(R.id.med_single);
        med_single2 = (TextView) findViewById(R.id.med_single2);
        back = (RelativeLayout) findViewById(R.id.back);
        med_readers = (TextView) findViewById(R.id.med_readers);
    }

    //设置触发事件
    private void control() {
//        med_mainHead.setOnClickListener(this);
//        med_officialAccounts.setOnClickListener(this);
//        med_weixinNumberTitle.setOnClickListener(this);
//        med_followers.setOnClickListener(this);
        med_codeTitle.setOnClickListener(this);
//        med_area.setOnClickListener(this);
//        med_schoolTitle.setOnClickListener(this);
        med_sortTitle.setOnClickListener(this);
        med_multiOneTitle.setOnClickListener(this);
        med_multiTwoTitle.setOnClickListener(this);
        med_multiOtherTitle.setOnClickListener(this);
        med_singleTitle.setOnClickListener(this);
        back.setOnClickListener(this);
        med_readers.setOnClickListener(this);
    }


    //基本信息页面跳转并携带数据
    private void data(String title,TextView textview,int logicNumber) {
        Layout layout=textview.getLayout();
        intent = new Intent();
        intent.setClass(MeduimInforActivity.this, MedSkipActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("med_change", layout.getText().toString());   //输入框内容为原内容
        bundle.putString("med_title", title);                    //对应填写项标题
        bundle.putInt("med_logic", logicNumber);                 //对应填写项代号
        intent.putExtras(bundle);//将Bundle添加到Intent
        startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值
        bundle.clear();
    }

    //广告报价页面跳转并携带数据
    private void dataPrice(TextView textview,TextView thisPriceSoft,TextView thisPriceHard,String uploadTitle){
        intent = new Intent();
        intent.setClass(MeduimInforActivity.this, MedPriceActivity.class);
        Bundle bundlePri = new Bundle();
        bundlePri.putInt("mediaId",mediaId);
        bundlePri.putString("uploadTitle", uploadTitle);
        bundlePri.putString("med_priceTitle", textview.getLayout().getText().toString());    //广告类型标题
        bundlePri.putString("med_priceSoft", thisPriceSoft.getLayout().getText().toString());//软广价格
        bundlePri.putString("med_priceHard", thisPriceHard.getLayout().getText().toString());//硬广价格
        intent.putExtras(bundlePri);//将Bundle添加到Intent
        startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值
        bundlePri.clear();
    }

    //数据回传处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if(data!=null) {
                Bundle bundle = data.getExtras();     //获取数据
                //广告报价页面回传广告价格
                if (thisTextView == med_multiOneTitle || thisTextView == med_multiTwoTitle ||
                        thisTextView == med_multiOtherTitle || thisTextView == med_singleTitle) {
                    thisPriceSoft.setText(bundle.getString("med_priceSoft"));
                    thisPriceHard.setText(bundle.getString("med_priceHard"));
                } else {
                    thisTextView.setText(bundle.getString("med_message"));  //控件内容修改为最新内容
                }
                bundle.clear();
            }
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.med_codeTitle:
                photoSort = "code";
                checkCodePopWin.checkCode();
                checkCodePopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
                break;
//            case R.id.med_followers:
//                title = "粉丝数";
//                thisTextView = med_followers;  //更改当前TextView
//                data(title, med_followers, 5);   //第一个参数为更改页面标题，第二个为当前点击控件，第三个为当前点击控件代号
//                break;
            case R.id.med_sortTitle:
                pickViewListener(6);
                mOpv.show();
                break;
            case R.id.med_schoolTitle:
                pickViewListener(9);
                mOpv.show();
                break;
            case R.id.med_area:
                pickViewListener(7);
                mOpv.show();
                break;
            //多图文第一条
            case R.id.med_multiOneTitle:
                thisTextView = med_multiOneTitle;   //当前类型
                thisPriceSoft = med_multiOne;        //当前类型软广价格
                thisPriceHard = med_multiOne2;       //当前类型硬广价格
                dataPrice(thisTextView,thisPriceSoft, thisPriceHard,"多图文第一条"); //以下同理
                break;
            //多图文第二条
            case R.id.med_multiTwoTitle:
                thisTextView = med_multiTwoTitle;
                thisPriceSoft = med_multiTwo;
                thisPriceHard = med_multiTwo2;
                dataPrice(thisTextView,thisPriceSoft, thisPriceHard,"多图文第二条");
                break;
            //多图文其他位置
            case R.id.med_multiOtherTitle:
                thisTextView = med_multiOtherTitle;
                thisPriceSoft = med_multiOther;
                thisPriceHard = med_multiOther2;
                dataPrice(thisTextView,thisPriceSoft, thisPriceHard,"多图文其他位置");
                break;
            //单图文各位置
            case R.id.med_singleTitle:
                thisTextView = med_singleTitle;
                thisPriceSoft = med_single;
                thisPriceHard = med_single2;
                dataPrice(thisTextView, thisPriceSoft, thisPriceHard,"单图文各位置");
                break;
            case R.id.med_readers:
                break;
            case R.id.back:
                MeduimInforActivity.this.finish();
                break;
            default:
                break;
        }
    }

}
