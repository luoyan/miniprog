package com.luoyan.checkin;

import android.app.Activity;
import android.view.KeyEvent; 
import android.webkit.WebView;
import android.os.Bundle; 

public class CheckInActivity extends Activity {
    private WebView webview; 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        //实例化WebView对象 
        webview = new WebView(this); 
        //设置WebView属性，能够执行Javascript脚本 
        webview.getSettings().setJavaScriptEnabled(true); 
        //加载需要显示的网页 
        webview.loadUrl("http://www.51cto.com/"); 
        //设置Web视图 
        setContentView(webview); 
    } 
     
    @Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        } 
        return false; 
    }
}
