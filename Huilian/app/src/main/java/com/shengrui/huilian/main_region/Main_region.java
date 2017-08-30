package com.shengrui.huilian.main_region;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.main_region.region_slide_choose.Region_slide_choose;
import com.shengrui.huilian.main_region.select_city.CityListActivity;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;


public class Main_region extends Fragment implements OnRefreshListener{
	RefreshListView listView;
	private Main_region_presenter activePresenter;
	View mView;
	RelativeLayout enter_region;
	private TextView location;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View friendView3 = inflater.inflate(R.layout.main_region,container,false);
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		listView = (RefreshListView) friendView3.findViewById(R.id.region_listView);
		enter_region = (RelativeLayout) friendView3.findViewById(R.id.enter_region);
		location = (TextView) friendView3.findViewById(R.id.location);
		if(Global.location==null){
			location.setText("无法定位到当前地区");
		}else{
			location.setText(Global.location);
		}
		if (activePresenter == null)
		{
			activePresenter= Main_region_presenter.getInstance();
		}
		activePresenter.bindData(listView);
		activePresenter.refresh();

		activePresenter.hotCity(new FinishCallBack());
		//点击进入查看自媒体信息
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
				TextView name = (TextView) ly.findViewById(R.id.region_city);
				String cityName = name.getText().toString();
				startActivityForResult(new Intent(getActivity(), Region_slide_choose.class).putExtra("cityName", cityName),0);
			}
		});
		mView=friendView3;
		listView.setOnRefreshListener(this);
		enter_region.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), CityListActivity.class));
			}
		});
		return friendView3;

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
				activePresenter.refresh();
				activePresenter.hotCity(new FinishCallBack(){
					@Override
					public void onFailure() {
						super.onFailure();
						Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
					}
				});
				listView.hideHeaderView();
			}
		}.execute(new Void[]{});
	}

	@Override
	public void onLoadingMore() {
		new AsyncTask<Void, Void, Void>() {

			//用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(0);
				return null;
			}

			//当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
			@Override
			protected void onPostExecute(Void result) {
				// 控制脚布局隐藏
				listView.hideFooterView();
			}
		}.execute(new Void[]{});
	}
}
