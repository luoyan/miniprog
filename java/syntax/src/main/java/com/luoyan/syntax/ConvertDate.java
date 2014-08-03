package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {
	public static void main(String[] args) throws IOException, ParseException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    while ((s = in.readLine()) != null && s.length() != 0) {
	    	//System.out.println(s);
	    	String dateStr = s;
	    	Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateStr);
	    	long time = date.getTime();
	    	System.out.println(time);
	    }
	}
}
