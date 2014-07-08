package com.luoyan.checkin;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.KeyEvent;  
import android.webkit.WebView;  
import android.webkit.WebViewClient;  
import android.widget.Toast;

public class MainActivity extends Activity {
	private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        setContentView(R.layout.webview);  
        webView = (WebView) findViewById(R.id.webView1);  
          
        webView.getSettings().setJavaScriptEnabled(true);//����ʹ�ù�ִ��JS�ű�  
        webView.getSettings().setBuiltInZoomControls(true);//����ʹ֧������  
//      webView.getSettings().setDefaultFontSize(5);
        
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){  
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
                // TODO Auto-generated method stub  
                view.loadUrl(url);// ʹ�õ�ǰWebView������ת  
                return true;//true��ʾ���¼��ڴ˴�����������Ҫ�ٹ㲥  
            }  
            @Override   //ת�����ʱ�Ĵ���  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                // TODO Auto-generated method stub  
                //Toast.makeText(WebViewTest.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
            }  
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    @Override   //Ĭ�ϵ���˼������˳�Activity�����������������ʹ������WebView�ڷ���  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {  
            webView.goBack();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
}
