package com.luoyan.androidsyntax;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AppListActivity extends ListActivity {
	static final String[] Apps = new String[] { 
		"ListPackages",
		"PhoneContacts",
		};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.listview, this.Apps));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent(AppListActivity.this,
							ListPackagesActivity.class);
					startActivity(intent);
				}
				else if (position == 1) {
					Intent intent = new Intent(AppListActivity.this,
							PhoneContactsActivity.class);
					startActivity(intent);
				}
			}
		});
 
	}
}
