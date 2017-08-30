package com.shengrui.huilian.main_dry_cargo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.shengrui.huilian.R;

/**
 * Created by ChenXQ on 2016/6/22.
 */
public class Main_dry_cargo_webview extends Activity {
    private RelativeLayout back;
    WebView webView;
    //网页的网址
    String url;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dry_cargo_webview);

        webView = (WebView)findViewById(R.id.webView);
        back = (RelativeLayout) findViewById(R.id.setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_dry_cargo_webview.this.finish();
            }
        });
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        bundle = this.getIntent().getExtras();
        url = bundle.getString("newsUrl");
        Log.d("++++++++++",url);

        webView.loadUrl(url);
        webViewSettings(webView);
        webView.setWebViewClient(new WebViewClientDemo());

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void webViewSettings(WebView webView2) {
        // TODO Auto-generated method stub
        WebSettings settings = webView.getSettings();
        //支持通过js打开新的窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setJavaScriptEnabled(true);
    }

    //在WebView中显示页面，而不是在默认的浏览器中显示页面
    private class WebViewClientDemo extends WebViewClient {
        @SuppressWarnings("unused")
        public boolean shouldOverideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }
}
