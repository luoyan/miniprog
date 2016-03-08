package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendHttpRequest {    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SendHttpRequest.class);
	public static String sendRequest(String encodeUrl, int isPost) {

        try {
            String pathUrl = URLDecoder.decode(encodeUrl, "UTF-8");
            String url = "http://lg-miui-data-stat02.bj:8080/" + pathUrl;
            String param = null;
        	LOGGER.debug("isPost " + isPost);
        	System.out.println("isPost " + isPost);
            if (isPost == 1) {
            	int index = url.indexOf('?');
            	LOGGER.debug("index " + index);
            	System.out.println("index " + index);
            	if (index >=0 ) {
            		param = url.substring(index + 1);
            		url = url.substring(0, index);
                	LOGGER.debug("index param " + param);
                	LOGGER.debug("index url " + url);
                	System.out.println("index param " + param);
                	System.out.println("index url " + url);
            	}
            }
            URL obj = new URL(url);
            String USER_AGENT = "Mozilla/5.0";

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json");
            if (isPost == 0) {
                con.setRequestMethod("GET");
            }
            else {
                con.setRequestMethod("POST");
                if (param != null) {
                	System.out.println("param=[" + param + "]");
                	System.out.println("url=[" + url + "]");
                	System.out.println("Content-Length=[" + param.length() + "]");
                	System.out.println("Body=[" + param.length() + "]");
                    con.setRequestProperty("Content-Length", param);
                    con.setDoOutput(true);
                    con.getOutputStream().write(param.getBytes("UTF-8"));
                }
            }
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
     
            BufferedReader in = null;
            in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();
     
            //print result
            System.out.println(responseBuffer.toString());
            LOGGER.debug(responseBuffer.toString());
            return responseBuffer.toString();
            //JSONObject jsonObject = JSONObject.fromObject(responseBuffer.toString());
            //return jsonObject;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sendRequest("druid%2fv2%3fqueryType%3dtimeseries%26dataSource%3dnews_upload_log%26granularity%3dminute%26aggregations%3d%5b%7b%26quot%3btype%26quot%3b%3a%26quot%3blongSum%26quot%3b%2c%26quot%3bname%26quot%3b%3a%26quot%3bview%26quot%3b%2c%26quot%3bfieldName%26quot%3b%3a%26quot%3bview%26quot%3b%7d%2c%7b%26quot%3btype%26quot%3b%3a%26quot%3bdoubleSum%26quot%3b%2c%26quot%3bname%26quot%3b%3a%26quot%3bclick%26quot%3b%2c%26quot%3bfieldName%26quot%3b%3a%26quot%3bclick%26quot%3b%7d%5d%26intervals%3d%5b%26quot%3b2015-07-13T17%3a00%3a00%2b08%3a00%2f2015-07-13T18%3a00%3a00%2b08%3a00%26quot%3b%5d", 1);
	}

}
