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
        //ʵ����WebView���� 
        webview = new WebView(this); 
        //����WebView���ԣ��ܹ�ִ��Javascript�ű� 
        webview.getSettings().setJavaScriptEnabled(true); 
        //������Ҫ��ʾ����ҳ 
        webview.loadUrl("http://www.51cto.com/"); 
        //����Web��ͼ 
        setContentView(webview); 
    } 
     
    @Override
    //���û��� 
    //����Activity���onKeyDown(int keyCoder,KeyEvent event)���� 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack()��ʾ����WebView����һҳ�� 
            return true; 
        } 
        return false; 
    }
}
