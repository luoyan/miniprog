package com.luoyan.syntax;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class MyJson {
	public static void main(String[] args) {
		JsonA a = new JsonA("hello", 10);
		//a.setName("hello");
		//a.setAge(10);
		JSONObject jsonOjbect = JSONObject.fromObject(a);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(new JsonA("a", 1));
		jsonArray.add(new JsonA("b", 2));
		jsonArray.add(new JsonA("c", 3));
		System.out.println("json object " + jsonOjbect.toString());
		System.out.println("json Array object " + jsonArray.toString());
		JSONObject b = JSONObject.fromObject("{\"age\":10,\"name\":\"hello\"}");
		System.out.println("b age " + b.get("age") + " name " + b.get("name"));
		JSONArray jsonArray3 = JSONArray.fromObject("[{\"age\":1,\"name\":\"a\"},{\"age\":2,\"name\":\"b\"},{\"age\":3,\"name\":\"c\"}]");
		for (int i = 0; i < jsonArray3.size(); i++) {
			System.out.println("i " + i + " age " + ((JSONObject)jsonArray3.get(i)).get("age") 
					+ " name " + ((JSONObject)jsonArray3.get(i)).get("name"));
		}
	}
}
