package com.luoyan.hdfsclienttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertTime {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    while ((s = in.readLine()) != null && s.length() != 0) {
	    	System.out.println(s);

			long unixMilliSeconds = Long.parseLong(s);
			Date date = new Date(unixMilliSeconds); // *1000 is to convert seconds to milliseconds
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
			
			String formattedDate = sdf.format(date);
			System.out.println(formattedDate);
	    }
	}

}
