package com.shengrui.huilian.main_school;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;
import com.shengrui.huilian.slide_choose.SlideChoose;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ChenXQ on 2016/4/25.
 */
public class Main_school extends Fragment implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener,OnRefreshListener{


    private ExpandableListView province_expandableListView;
    private ArrayList<Group> groupList;
    private ArrayList<List<Child>> childList;
    private String city = null;
    public static int mPosition;
    int count = 0;
    private TextView mFragment_tv;
    private Group group = null;
    private Child child = null;
    private String[][] province_city = new String[][]{
            {"北京", "朝阳区", "海淀区", "通州区", "房山区", "丰台区", "昌平区", "大兴区", "顺义区", "西城区", "延庆县", "石景山区", "宣武区", "怀柔区", "崇文区", "密云县", "东城区", "门头沟区", "平谷区"},
            {"天津", "和平区", "北辰区", "河北区", "河西区", "西青区", "津南区", "东丽区", "武清区", "宝坻区", "红桥区", "大港区", "汉沽区", ",海县", "宁河县", "塘沽区", "蓟县", "南开区", "河东区"},
            {"上海", "松江区", "宝山区", "金山区", "嘉定区", "南汇区", "青浦区", "浦东新区", "奉贤区", "闵行区", "徐汇区", "静安区", "黄浦区", "普陀区", "杨浦区", "虹口区", "闸北区", "长宁区", "崇明县", "卢湾区"},
            {"重庆", "江北区", "渝北区", "沙坪坝区", "九龙坡区", "万州区", "永川市", "南岸区", "酉阳县", "北碚区", "涪陵区", "秀山县", "巴南区", "渝中区", "石柱县", "忠县", "合川市", "大渡口区", "开县", "长寿区", "荣昌县", "云阳县", "梁平县", "潼南县", "江津市", "彭水县", "璧山县", "綦江县", "大足县", "黔江区", "巫溪县", "巫山县", "垫江县", "丰都县", "武隆县", "万盛区", "铜梁县", "南川市", "奉节县", "双桥区", "城口县"},
            {"广东", "广州市", "中山市", "深圳市", "惠州市", "江门市", "珠海市", "汕头市", "佛山市", "湛江市", "河源市", "肇庆市", "潮州市", "清远市", "韶关市", "揭阳市", "阳江市", " 云浮市 ","东莞市",  " 茂名市 ", " 梅州市 ", "汕尾市"},
            {"河北", "石家庄市", "河北区", "保定市", "秦皇岛", "唐山市", "邯郸市", "邢台市", "沧州市", "承德市", "廊坊市", "衡水市", "张家口"},
            {"河南", "郑州市", "洛阳市", "焦作市", "商丘市", "信阳市", "新乡市", "安阳市", "开封市", "漯河市", "南阳市", "鹤壁市", "平顶山", "濮阳市", "许昌市", "周口市", "三门峡", "驻马店", "徐州市"},
            {"云南", "昆明市", "红河州", "大理州", "文山州", "德宏州", "曲靖市", "昭通市", "楚雄州", "保山市", "玉溪市", "丽江地区", "临沧地区", "思茅地区", "西双版纳州", "怒江州", "迪庆州"},
            {"辽宁", "沈阳市", "大连市", "丹东市", "辽阳市", "葫芦岛市", "锦州市", "朝阳市", "营口市", "鞍山市", "抚顺市", "阜新市", "本溪市", "盘锦市", "铁岭市"},
            {"黑龙江", "哈尔滨市", "齐齐哈尔市", "大庆市", "佳木斯市", "双鸭山市", "牡丹江市", "鸡西市", "黑河市", "绥化市", "鹤岗市", "伊春市", "大兴安岭地区", "七台河市"},
            {"湖南", "长沙市", "邵阳市", "常德市", "衡阳市", "株洲市", "湘潭市", "永州市", "岳阳市", "怀化市", "郴州市", "娄底市", "益阳市", "张家界市", "湘西州"},
            {"安徽", "合肥市", "芜湖市", "六安市", "宿州市", "阜阳市", "安庆市", "马鞍山市", "蚌埠市", "淮北市", "淮南市", "宣城市", "黄山市", "铜陵市", "亳州市", "池州市", "巢湖市", "滁州市"},
            {"山东", "济南市", "青岛市", "临沂市", "济宁市", "菏泽市", "烟台市", "泰安市", "淄博市", "潍坊市", "日照市", "威海市", "滨州市", "东营市", "聊城市", "德州市", "莱芜市", "枣庄市"},
            {"新疆", "乌鲁木齐市", "伊犁州", "昌吉州", "石河子市", "哈密地区", "阿克苏地区", "巴音郭楞州", "喀什地区", "塔城地区", "克拉玛依市", "和田地区", "阿勒泰州", "吐鲁番地区", "阿拉尔市", "博尔塔拉州", "五家渠市", "克孜勒苏州", "图木舒克市"},
            {"江苏", "南京市", "苏州市", "徐州市", "盐城市", "无锡市", "南通市", "连云港市", "常州市", "扬州市", "镇江市", "淮安市", "泰州市", "宿迁市"},
            {"浙江", "杭州市", "温州市", "宁波市", "台州市", "嘉兴市", "金华市", "湖州市", "绍兴市", "舟山市", "丽水市", "衢州市"},
            {"江西", "南昌市", "赣州市", "上饶市", "吉安市", "九江市", "新余市", "抚州市", "宜春市", "景德镇市", "萍乡市", "鹰潭市"},
            {"湖北", "武汉市", "宜昌市", "襄樊市", "荆州市", "恩施州", "孝感市", "黄冈市", "十堰市", "咸宁市", "黄石市", "仙桃市", "随州市", "天门市", "荆门市", "潜江市", "鄂州市", "神农架林区"},
            {"广西", "贵港市", "玉林市", "北海市", "南宁市", "柳州市", "桂林市", "梧州市", "钦州市", "来宾市", "河池市", "百色市", "贺州市", "崇左市", "防城港市"},
            {"甘肃", "兰州市", "天水市", "庆阳市", "武威市", "酒泉市", "张掖市", "陇南地区", "白银市", "定西地区", "平凉市", "嘉峪关市", "临夏回族自治州", "金昌市", "甘南州"},
            {"山西", "太原市", "大同市", "运城市", "长治市", "晋城市", "忻州市", "临汾市", "吕梁市", "晋中市", "阳泉市", "朔州市"},
            {"内蒙古", "赤峰市", "包头市", "通辽市", "呼和浩特市", "乌海市", "鄂尔多斯市", "呼伦贝尔市", "兴安盟", "巴彦淖尔盟", "乌兰察布盟", "锡林郭勒盟", "阿拉善盟"},
            {"陕西", "西安市", "咸阳市", "宝鸡市", "汉中市", "渭南市", "安康市", "榆林市", "商洛市", "延安市", "铜川市"},
            {"吉林","长春市",  "吉林市", "白山市", "白城市", "延边州", "松原市", "辽源市", "通化市", "四平市"},
            {"福建", "福州市", "漳州市", "泉州市", "厦门市", "莆田市", "宁德市", "三明市", "南平市", "龙岩市"},
            {"贵州", "贵阳市", "黔东南州", "黔南州", "遵义市", "黔西南州", "毕节地区", "铜仁地区", "安顺市", "六盘水市"},
            {"青海", "西宁市", "海西州", "海东地区海北州", "果洛州", "玉树州", "黄南藏族自治州"},
            {"西藏", "拉萨市", "山南地区", "林芝地区", "日喀则地区", "阿里地区", "昌都地区", "那曲地区"},
            {"四川", "成都市", "绵阳市", "广元市", "达州市", "南充市", "德阳市", "广安市", "阿坝州", "巴中市", "遂宁市", "内江市", "凉山州", "攀枝花市", "乐山市", "自贡市", "泸州市", "雅安市", "宜宾市", "资阳市", "眉山市", "甘孜州"},
            {"宁夏", "银川市", "吴忠市", "中卫市", "石嘴山市", "固原市"},
            {"海南", "海口市", "三亚市", "琼海市", "文昌市", "东方市", "昌江县", "陵水县", "乐东县", "五指山市", "保亭县", "澄迈县", "万宁市", "儋州市", "临高县", "白沙县", "定安县", "琼中县", "屯昌县"},
            {"台湾", "台北市", "高雄市", "台中市", "新竹市", "基隆市", "台南市", "嘉义市"},
            {"香港"}, {"澳门"}
    };
    private MyexpandableListAdapter adapter;
    private TextView mFragment2_tv = null;
    private RefreshListView listview=null;
    private Main_school_presenter activePresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_school, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        province_expandableListView = (ExpandableListView)getActivity().findViewById(R.id.province_expandablelist);
        listview= (RefreshListView)  getActivity().findViewById(R.id.typeschool_right_listview);

        //点击学校触发事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View ly = listview.getChildAt(position - listview.getFirstVisiblePosition());
                TextView et = (TextView) ly.findViewById(R.id.school_id);
                String school_id = et.getText().toString();
                Log.d("--->", school_id);
                startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "schoolId").
                        putExtra("schoolId", school_id).putExtra("URL", Config.mediaSchool));
            }
        });
        if (activePresenter == null)
        {
            activePresenter= Main_school_presenter.getInstance();
        }
        activePresenter.bindData(listview);
        initData();
        adapter = new MyexpandableListAdapter(getActivity());
        province_expandableListView.setAdapter(adapter);
        province_expandableListView.setOnChildClickListener(this);
        province_expandableListView.setOnGroupClickListener(this);
        listview.setOnRefreshListener(this);
    }


    //处理子项点击事件
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        city = province_city[i][i1 + 1];
        schoolData(1);
        adapter.setSelectedItem(i, i1);
        adapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        return false;
    }



    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }
            @Override
            protected void onPostExecute(Void result){
                Log.d("---", "iii");
                schoolData(1);
                listview.hideHeaderView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {
            //用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(500);

                return null;
            }
            //当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
            @Override
            protected void onPostExecute(Void result) {

                Log.d("--->","chenchenchen");
                schoolData(2);
                // 控制脚布局隐藏
                listview.hideFooterView();
            }
        }.execute(new Void[]{});
    }


    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        SparseBooleanArray selected;
        int old = -1;
        int parentPosition = -1;
        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            selected = new SparseBooleanArray();
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        class GroupHolder {
            TextView textView;
            ImageView imageView;

        }

        class ChildHolder {
            TextView textName;
            ImageView cityTab;
            LinearLayout cityBackground;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                groupHolder.textView.setTextSize(16);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            groupHolder.textView.setText(((Group) getGroup(groupPosition)).getProvince());
            if (isExpanded)// ture is Expanded or false is not isExpanded
            {
                groupHolder.imageView.setVisibility(View.VISIBLE);
                groupHolder.imageView.setImageResource(R.drawable.icon_to_down);
            } else {
                groupHolder.imageView.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);
                childHolder.textName = (TextView) convertView.findViewById(R.id.city);
                childHolder.cityTab = (ImageView) convertView.findViewById(R.id.city_tab);
                childHolder.cityBackground = (LinearLayout) convertView.findViewById(R.id.city_background);
                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }
            if (selected.get(childPosition)&&this.parentPosition == groupPosition) {
                    childHolder.textName.setTextColor(context.getResources().getColor(
                            R.color.tab_text_pressed_color));
                childHolder.cityBackground.setBackgroundResource(R.drawable.school_background_back);
                childHolder.cityTab.setVisibility(View.VISIBLE);
            } else {
                // convertView.setBackgroundResource(R.color.white);
                childHolder.textName.setTextColor(Color.rgb(44, 45, 45));
                childHolder.cityBackground.setBackgroundResource(R.drawable.city_background);
                childHolder.cityTab.setVisibility(View.GONE);
            }
            childHolder.textName.setText(((Child) getChild(groupPosition, childPosition)).getCity());
            return convertView;
        }

        //子项是否能够被点击
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        public void setSelectedItem(int groupPosition,int selected) {
            this.parentPosition = groupPosition;
            if (old != -1) {
                this.selected.put(old, false);
            }
            this.selected.put(selected, true);
            old = selected;
        }
    }


    public class Child {
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    class Group {
        private String province;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }



    //初始化省份城市联动的数据
    private void initData() {
        groupList = new ArrayList<Group>();
        Group group = null;
        for (int i = 0; i < province_city.length; i++) {
            group = new Group();
            group.setProvince(province_city[i][0]);
            groupList.add(group);
        }
        childList = new ArrayList<List<Child>>();
        for (int i = 0; i < province_city.length; i++) {
            ArrayList<Child> childTemp = null;
            String[] childarr = province_city[i];
            childTemp = new ArrayList<Child>();
            for (int k = 1; k < childarr.length; k++) {
                child = new Child();
                child.setCity(childarr[k]);
                childTemp.add(child);
            }
            childList.add(childTemp);
            childTemp = null;
        }
    }

    public void schoolData(int agency){
        RequestParams rq=new RequestParams();
        rq.put("CITY_NAME", "\"" +city+"\"");
        //Log.d("---->", "'珠海市'");
        activePresenter.getData1(Config.areaSchool, rq, new FinishCallBack(){
            @Override
            public void onFailure() {
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
                super.onFailure();
            }
        }, agency); //url 需改变
    }
}


