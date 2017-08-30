package com.shengrui.huilian.main_recommend;

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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;


public class Main_recommend extends Fragment implements OnRefreshListener {
	RefreshListView listView;
	private Main_recommend_presenter activePresenter;
	View mView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View friendView3 = inflater.inflate(R.layout.main_recommend,container,false);
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		listView = (RefreshListView) friendView3.findViewById(R.id.recommend_listView);
		if (activePresenter == null)
		{
			activePresenter= Main_recommend_presenter.getInstance();
		}
		activePresenter.bindData(listView);
		activePresenter.refresh();

		activePresenter.onRefresh(new FinishCallBack(){
			@Override
			public void onFailure() {
				Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
				super.onFailure();
			}
		});
		//点击进入查看自媒体信息
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
				TextView et = (TextView) ly.findViewById(R.id.media_id);
				String mediaId = et.getText().toString();
				startActivity(new Intent(getActivity(), MedCheckActivity.class).putExtra("mediaId", mediaId));
			}
		});
		listView.setOnRefreshListener(this);
		mView=friendView3;
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
				Log.d("---", "iii");
				activePresenter.onRefresh(new FinishCallBack(){
					@Override
					public void onFailure() {
						Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
						super.onFailure();
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
				SystemClock.sleep(500);

				return null;
			}

			//当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
			@Override
			protected void onPostExecute(Void result) {

				Log.d("--->","chenchenchen");
				activePresenter.onLoadMore(new FinishCallBack(){
					@Override
					public void onFailure() {
						Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
						super.onFailure();
					}
				});//回调
				// 控制脚布局隐藏
				listView.hideFooterView();
			}
		}.execute(new Void[]{});
	}

}
