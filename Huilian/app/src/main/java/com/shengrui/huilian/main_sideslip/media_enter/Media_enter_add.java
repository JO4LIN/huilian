package com.shengrui.huilian.main_sideslip.media_enter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.main_sideslip.task_details.Main_task_details;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.medium_infor.MeduimInforActivity;

public class Media_enter_add extends Activity implements View.OnClickListener{
	ListView listView;
	private Media_enter_add_presenter activePresenter;
	private RelativeLayout back;
	private TextView media_add;
	private Bundle bundle=null;
	private int task=1;
	private RelativeLayout init;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_enter_add);
		back = (RelativeLayout) findViewById(R.id.back);
		media_add = (TextView) findViewById(R.id.media_add);
		back.setOnClickListener(this);
		media_add.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.media_enter_add_listview);
		init = (RelativeLayout) findViewById(R.id.init_task);

		if (activePresenter == null)
		{
			activePresenter= Media_enter_add_presenter.getInstance();
		}
		activePresenter.bindData(listView);
		activePresenter.refresh();


		activePresenter.onRefresh(new FinishCallBack(){
			@Override
			public void onFailure() {
				super.onFailure();
				Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		listView.setEmptyView(init);
		bundle=this.getIntent().getExtras();
		task = Integer.valueOf(bundle.getString("task"));
		if(task==0){
//			media_add.setVisibility(View.GONE);
			//点击进入查看自媒体信息
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
					TextView et = (TextView) ly.findViewById(R.id.media_id);
					String mediaId = et.getText().toString();
					startActivity(new Intent(Media_enter_add.this, Main_task_details.class).putExtra("mediaId", mediaId));
				}
			});
		}
//		else if(task==1){
//			//点击进入查看自媒体信息
//			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//					View ly = listView.getChildAt(i - listView.getFirstVisiblePosition());
//					TextView et = (TextView) ly.findViewById(R.id.media_id);
//					String mediaId = et.getText().toString();
//					startActivity(new Intent(Media_enter_add.this, MeduimInforActivity.class).putExtra("mediaId", mediaId));
//				}
//			});
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back:
				finish();
				break;
			case R.id.media_add:
				startActivity(new Intent(this,Media_enter_code.class));
				break;
		}
	}
}
