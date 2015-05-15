/**
 * 
 */
package com.luoyan.checkin;

import android.app.ListActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

/**
 * @author fool
 *
 */
public class CheckInListActivity extends ListActivity {
	 
	static final String[] urls = new String[] { 
		"http://bbs.xiaomi.cn/qiandao/?mobile=2",
		"http://www.miui.com/forum.php",
		"http://geek.miui.com/",
		"http://weekly.mioffice.cn/myreport",
		"http://zc-stage1-miui-ad01.bj:8090/razorflow/dashboard_quickstart/index.html",
		};
	private WebView webview;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.listview, this.urls));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CheckInListActivity.this, MainActivity.class);
				Bundle b = new Bundle();
				String url = (String)(((TextView) view).getText());
				b.putString("url", url); //Your id
				intent.putExtras(b); //Put your id to your next Intent
				startActivity(intent);
			}
		});
 
	}
}