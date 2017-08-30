package com.shengrui.huilian.refresh_listview;

public interface OnRefreshListener {

	/**
	 * 下拉刷新
	 */
	void onDownPullRefresh();

	/**
	 * 上拉加载更多
	 */
	void onLoadingMore();
}
