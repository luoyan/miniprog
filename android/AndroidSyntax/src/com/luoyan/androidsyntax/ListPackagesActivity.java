package com.luoyan.androidsyntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ListPackagesActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PackageManager pm = this.getPackageManager();
		//get the packages, can use other flag for more information
		List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		
		//init the list
		List<HashMap<String, Object>> map = new ArrayList<HashMap<String,Object>>();
		
		for(PackageInfo p : packageInfos) {
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("icon", p.applicationInfo.loadIcon(pm));
			m.put("name", p.applicationInfo.loadLabel(pm).toString() + "/" + p.packageName);
			map.add(m);
		}
		
		Collections.sort(map, new ListPackagesComparator());//to sort the hash map by unicode value
		
		ListAdapter adapter = new ListPackagesAdapter(this, map);
		
		setListAdapter(adapter);//set the adapter to this listview
	}
	
	/**
	 * a easy sort comparator
	 */
	public class ListPackagesComparator implements Comparator<HashMap<String, Object>> {

		public int compare(HashMap<String, Object> lhs,
				HashMap<String, Object> rhs) {
			String str1 = lhs.get("name").toString();
			String str2 = rhs.get("name").toString();
			
			if(str1.toUpperCase().compareTo(str2.toUpperCase()) < 0)
				return -1;
			else if(str1.toUpperCase().compareTo(str2.toUpperCase()) > 0)
				return 1;
			else
				return 0;
		}
		
	}
	
	/**
	 * a test adapter to display the list data
	 */
	public class ListPackagesAdapter extends BaseAdapter {
		
		private List<HashMap<String, Object>> mMap;
		private LayoutInflater mLayoutInflater;
		
		public ListPackagesAdapter(Context context, List<HashMap<String, Object>>map) {
			mMap = map;
			mLayoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mMap.size();
		}

		public Object getItem(int position) {
			return mMap.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, Object> map = mMap.get(position);
			View view = mLayoutInflater.inflate(R.layout.list_packages, null);
			ImageView iv = (ImageView)view.findViewById(R.id.test2_image);
			TextView tv = (TextView)view.findViewById(R.id.test2_text);
			iv.setBackgroundDrawable((Drawable) map.get(("icon")));
			tv.setText(map.get("name").toString());
			return view;
		}
		
	}
}
