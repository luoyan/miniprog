/**
 * 
 */
package com.luoyan.syntax;

import net.sf.json.JSONObject;

/**
 * @author xiaomi
 *
 */
public class JSONObjectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("hello", "1");
		System.out.println("hello " + json.getInt("hello"));
	}

}
