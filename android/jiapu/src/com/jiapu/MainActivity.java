package com.jiapu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
/*
	Runnable mRun = new Runnable()
	{
		;
	};*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.family_tree);
/*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		} */
		String urlContent;
			urlContent = getUrlContent();
			if (urlContent == null)
				Log.d("INFO: ", "null");
			else
				Log.d("INFO: ", urlContent);
		//new Thread(mRun).start();
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	public String getUrlContent() {
		try {
			Log.d("INFO: ", "1");
			//String defaultUrl = "http://14.18.207.195:8000/search?person=%E7%BD%97%E8%BF%9E%E5%BA%86";
			String defaultUrl = "http://www.baidu.com/";
            URL url = new URL(defaultUrl);
			Log.d("INFO: ", "2");
            URLConnection conection = url.openConnection();
			Log.d("INFO: ", "3");
            conection.setConnectTimeout(3000);
            conection.connect();
			Log.d("INFO: ", "4");
            // Getting file length
            int lenghtOfFile = conection.getContentLength();
            // Create a Input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);
            // Output stream to write file
            //OutputStream output = new FileOutputStream(
            //        "/sdcard/9androidnet.jpg");
            String outputStr = "";

            byte data[] = new byte[1024];
            long total = 0;
            int count = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                // writing data to file
                //output.write(data, 0, count);
                for (int i = 0; i < count; i++) {
                    outputStr = outputStr + data[count]; 
                }
            }
            // flushing output
            // output.flush();
            // closing streams
            // output.close();
            input.close();
        } catch (SocketTimeoutException e) {
            //connectionTimeout=true;
        	if (e != null && e.getMessage() != null) {
        	    Log.e("Error: ", e.getMessage());
        	}
        	else {
        		Log.e("Error: ", "null for SocketTimeoutException");
        	}
        } catch (Exception e) {
        	if (e != null && e.getMessage() != null) {
        	    Log.e("Error: ", e.getMessage());
        	}
        	else if (e == null){
        		Log.e("Error: ", "null for others e == null");
        	}
        	else if (e.getMessage() == null){
        		Log.e("Error: ", "null for others e.getMessage() == null");
        		StackTraceElement[] elements = e.getStackTrace();
        		Log.e("Error: ", "StackTraceElement.length " + elements.length);
        		for (int i = 0 ; i < elements.length ; i++) {
        			Log.e("Error: ", i + " : " + elements[i].getFileName() + " : " + elements[i].getLineNumber());
        		}
        	}
        }
		return null;
	}
}
