package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JavaDecode {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    while ((s = in.readLine()) != null && s.length() != 0) {
	    	//System.out.println(s);
	    	System.out.println(URLDecoder.decode(s, "UTF-8"));
	    }
	}

}
