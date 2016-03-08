package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class ConvertMd5 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    while ((s = in.readLine()) != null && s.length() != 0) {
	    	System.out.println(s);
	    	String imei = s;
	    	String md5 = DigestUtils.md5Hex(imei);
			System.out.println(md5);
	    }
	}

}
