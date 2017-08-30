package com.shengrui.huilian.main_sideslip.task_details;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.main_sideslip.indent_details.Specific_indent.Specific_indent_details;
import com.shengrui.huilian.main_sideslip.task_details.Specific_task.Specific_task_details;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;

public class Accomplish_task_details extends Fragment implements OnRefreshListener {
	private RefreshListView listView;
	private Accomplish_task_details_presenter activePresenter;
	private View mView;
	private Bundle bundle;
	private int mediaId;
	private RelativeLayout init;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);

		View friendView3 = inflater.inflate(R.layout.task_details_accomplish,container,false);
		bundle=getActivity().getIntent().getExtras();
		mediaId = Integer.valueOf(bundle.getString("mediaId"));
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		listView = (RefreshListView) friendView3.findViewById(R.id.accomplish_task_listView);
		init = (RelativeLayout) friendView3.findViewById(R.id.init_task);
		if (activePresenter == null)
		{
			activePresenter= Accomplish_task_details_presenter.getInstance();
		}
		activePresenter.bindData(listView);

		taskData(1);

		//点击进入查看自媒体信息
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				View ly =listView.getChildAt(i-listView.getFirstVisiblePosition());
				TextView et = (TextView) ly.findViewById(R.id.indentId);
				String indentId = et.getText().toString();
				startActivity(new Intent(getActivity(), Specific_task_details.class).putExtra("indentId", indentId).putExtra("progress", "1"));
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
				taskData(1);
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
				taskData(2);
				// 控制脚布局隐藏
				listView.hideFooterView();
			}
		}.execute(new Void[]{});
	}

	public void taskData(int agency){
		RequestParams req = new RequestParams();
		req.put("progress",1);
		req.put("mediaId", mediaId);
		activePresenter.getData(Config.CheckTaskURL, req, new FinishCallBack() {
			public void onSuccess() {
				super.onSuccess();
				Log.d("--->", "数据请求成功");
				listView.setEmptyView(init);
			}

			public void onFailure() {
				super.onFailure();
				Log.d("--->", "数据请求失败");
				listView.setEmptyView(init);
				Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
			}
		}, agency);
	}

}
