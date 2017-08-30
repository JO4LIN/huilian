package com.shengrui.huilian.main_industry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.slide_choose.SlideChoose;

import java.util.List;
import java.util.Map;

/**
 * Created by ChenXQ on 2016/4/25.
 */
public class Main_industry extends Fragment {

    private GridView gview;
    private List<Map<String, Object>> data_list;

    private String[] iconName = { "新闻", "时尚", "搞笑", "文学", "影视", "音乐", "娱乐", "运动", "职场","美食","汽车","财经","宠物","养生","动漫",
            "商业","星座","密语","电商","情感","百科","健身","游戏","家居","母婴","互联网","投资",
            "IT","杂志","营销","励志","科技","购物","明星","创业","旅游","白领"};
    private int[] icon = { R.drawable.trade_news, R.drawable.trade_fashion, R.drawable.trade_joke, R.drawable.trade_book,
            R.drawable.trade_movie, R.drawable.trade_music,  R.drawable.trade_play, R.drawable.trade_sports, R.drawable.trade_worker,
            R.drawable.trade_food, R.drawable.trade_car, R.drawable.trade_economics, R.drawable.trade_pet, R.drawable.trade_life, R.drawable.trade_cartoon,
            R.drawable.trade_business, R.drawable.trade_constellation, R.drawable.trade_cryptolalia, R.drawable.trade_ecommerce, R.drawable.trade_emotion, R.drawable.trade_encyclopedia,
            R.drawable.trade_fitness, R.drawable.trade_game, R.drawable.trade_home, R.drawable.trade_infant, R.drawable.trade_internet, R.drawable.trade_investment,
            R.drawable.trade_it, R.drawable.trade_magazine, R.drawable.trade_markenting, R.drawable.trade_motivational, R.drawable.trade_science, R.drawable.trade_shopping,
            R.drawable.trade_star, R.drawable.trade_startbusiness, R.drawable.trade_travel, R.drawable.trade_worker,};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.main_industry_item, null);
        gview = (GridView) view.findViewById(R.id.grid_view);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (icon[i]) {
                    case R.drawable.trade_news:
                        Log.d("--->", "" + i);
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId").
                                putExtra("sortId", "1").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_fashion:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "2").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_joke:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "3").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_book:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "22").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_movie:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "17").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_music:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "35").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_play:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "40").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_sports:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "16").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_worker:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "14").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_food:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "18").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_car:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "8").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_economics:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "10").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_pet:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "28").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_life:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "13").putExtra("URL", Config.mediaType));
                        break;
                    case R.drawable.trade_cartoon:
                        startActivity(new Intent(getActivity(), SlideChoose.class).putExtra("TYPE", "sortId")
                                .putExtra("sortId", "27").putExtra("URL", Config.mediaType));
                        break;
                }
            }
        });
        GrideAdapter adapter=new GrideAdapter(getContext(),iconName,icon);
        gview.setAdapter(adapter);
        return view;
    }

}
