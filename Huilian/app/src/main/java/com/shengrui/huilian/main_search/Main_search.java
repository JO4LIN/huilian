package com.shengrui.huilian.main_search;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.shengrui.huilian.R;
import com.shengrui.huilian.base_mvp.Global.Config;
import com.shengrui.huilian.base_mvp.Global.Global;
import com.shengrui.huilian.base_mvp.net.FinishCallBack;
import com.shengrui.huilian.local_db.RecordSQLiteOpenHelper;
import com.shengrui.huilian.medium_infor.MedCheckActivity;
import com.shengrui.huilian.refresh_listview.OnRefreshListener;
import com.shengrui.huilian.refresh_listview.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 2016/7/21.
 */
public class Main_search extends Activity implements OnRefreshListener, View.OnClickListener {

    RefreshListView listView;
    private TextView search_button;
    private EditText search;
    private Main_search_presenter activePresenter;
    private RelativeLayout search_back;
    private ListView tipListView;
    private TextView tipText;
    private TextView clear;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase dbInsert = null;
    private SQLiteDatabase dbDelete = null;
    private BaseAdapter tipAdapter;
    private Cursor queryDataCursor = null;
    private Cursor hasDataCursor  = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_search);
        search_back = (RelativeLayout) findViewById(R.id.search_back);
        listView = (RefreshListView) findViewById(R.id.main_search_listView);
        tipListView = (ListView) findViewById(R.id.history_list);
        tipText = (TextView) findViewById(R.id.tipText);
        search_button = (TextView) findViewById(R.id.search_button);
        search = (EditText) findViewById(R.id.search);
        clear = (TextView) findViewById(R.id.clear_history);
        search_button.setOnClickListener(this);
        clear.setOnClickListener(this);
        listView.setVisibility(View.GONE);
        tipListView.setVisibility(View.VISIBLE);
        if(dbInsert!=null){
            if(dbInsert.isOpen()){
                dbInsert.close();
            }
        }

        if(dbDelete!=null){
            if(dbDelete.isOpen()){
                dbDelete.close();
            }
        }

        if(hasDataCursor!=null){
            if(!hasDataCursor.isClosed()){
                hasDataCursor.close();
            }
        }

        if(queryDataCursor!=null){
            if(!queryDataCursor.isClosed()){
                queryDataCursor.close();
            }
        }

        if(queryDataCursor!=null){
            if(!queryDataCursor.isClosed()){
                queryDataCursor.close();
            }
        }
        queryData("");
        if (activePresenter == null)
        {
            activePresenter= Main_search_presenter.getInstance();
        }
        activePresenter.bindData(listView);
        activePresenter.refresh();
        listView.setOnRefreshListener(this);
        search_back.setOnClickListener(this);

        // 搜索框的文本变化实时监听
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tipListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                if (search.getText().toString().equals("")){
                    if(queryDataCursor!=null){
                        if(!queryDataCursor.isClosed()){
                            queryDataCursor.close();
                        }
                    }
                    queryData("");
                    tipText.setText("历史搜索");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempName = search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                if(queryDataCursor!=null){
                    if(!queryDataCursor.isClosed()){
                        queryDataCursor.close();
                    }
                }
                queryData(tempName);
            }
        });

        tipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.title_tv);
                String name = textView.getText().toString();
                search.setText(name);
                tipText.setText("历史搜索");
                searchKey(name,1);
            }
        });


        //点击进入查看自媒体信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View ly = listView.getChildAt(i-listView.getFirstVisiblePosition());
                TextView et = (TextView) ly.findViewById(R.id.media_id);
                String mediaId = et.getText().toString();
                startActivity(new Intent(Main_search.this, MedCheckActivity.class).putExtra("mediaId", mediaId));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbDelete!=null){
            dbDelete.close();
        }
        if(dbInsert!=null){
            dbInsert.close();
        }
        if(queryDataCursor!=null){
            queryDataCursor.close();
        }
        if(hasDataCursor!=null){
            hasDataCursor.close();
        }
        helper.close();
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                SystemClock.sleep(2000);
                return null;
            }
            @Override
            protected void onPostExecute(Void result){
                Log.d("---", "iii");
               // activePresenter.onRefresh(new FinishCallBack());
                searchKey(search.getLayout().getText().toString().trim(),1);
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
                SystemClock.sleep(2000);

                return null;
            }

            //当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
            @Override
            protected void onPostExecute(Void result) {

                Log.d("--->", "chenchenchen");
             //   activePresenter.onLoadMore(new FinishCallBack());  //回调
                searchKey(search.getLayout().getText().toString().trim(),2);
                // 控制脚布局隐藏
                listView.hideFooterView();
            }
        }.execute(new Void[]{});
    }


    /**
     * 获取ArrayList数组
     *
     * @param array
     * @return
     */
    private List<String> getListArray(String[] array) {
        List<String> titleArray = new ArrayList<String>();
        for (String title : array) {
            titleArray.add(title);
        }
        return titleArray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                if(search.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
                }else{
                    if(hasDataCursor!=null){
                        if(!hasDataCursor.isClosed()){
                            hasDataCursor.close();
                        }
                    }
                    boolean hasData = hasData(search.getText().toString().trim());
                    if (!hasData) {
                        if(dbInsert!=null){
                            if(dbInsert.isOpen()){
                                dbInsert.close();
                            }
                        }
                        insertData(search.getText().toString().trim());
                        if(queryDataCursor!=null){
                            if(!queryDataCursor.isClosed()){
                                queryDataCursor.close();
                            }
                        }
                        queryData("");
                    }
                    tipText.setText("搜索结果");
                    String searchKey = search.getLayout().getText().toString().trim();
                    searchKey(searchKey, 1);
                }
                break;
            case R.id.search_back:
                if(dbDelete!=null){
                    dbDelete.close();
                }
                if(dbInsert!=null){
                    dbInsert.close();
                }
                if(queryDataCursor!=null){
                    queryDataCursor.close();
                }
                if(hasDataCursor!=null){
                    hasDataCursor.close();
                }
                helper.close();
                Main_search.this.finish();
                break;
            case R.id.clear_history:
                if(dbDelete!=null){
                    if(dbDelete.isOpen()){
                        dbDelete.close();
                    }
                }
                deleteData();
                if(queryDataCursor!=null){
                    if(!queryDataCursor.isClosed()){
                        queryDataCursor.close();
                    }
                }
                queryData("");
                break;
            default:
                break;
        }
    }

    public void searchKey(String searchKey, int agency) {
        RequestParams req = new RequestParams();
        req.put("searchType", 1);
        req.put("searchKey", searchKey);
        activePresenter.getData(Config.search, req, new FinishCallBack() {
            public void onSuccess() {
                super.onSuccess();

                listView.setVisibility(View.VISIBLE);
                tipListView.setVisibility(View.GONE);
                Log.d("--->", "数据请求成功");
            }

            public void onFailure() {
                super.onFailure();
                Log.d("--->", "数据请求失败");
                Toast.makeText(Global.context, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }, agency);
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        dbInsert = helper.getWritableDatabase();
        dbInsert.execSQL("insert into records(name) values('" + tempName + "')");
        dbInsert.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        queryDataCursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        tipAdapter = new SimpleCursorAdapter(this, R.layout.search_tip_item_layout, queryDataCursor, new String[] { "name" },
                new int[] { R.id.title_tv }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        tipListView.setAdapter(tipAdapter);
        tipAdapter.notifyDataSetChanged();
      //  queryDataCursor.close();
    }
    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        hasDataCursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return hasDataCursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        dbDelete = helper.getWritableDatabase();
        dbDelete.execSQL("delete from records");
        dbDelete.close();
    }


}
