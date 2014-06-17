package com.xiaonei.rose.gettingStarted.controllers;

import org.json.JSONException;
import org.json.JSONObject;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

//Controller should end with Controller, not need to implement other interface
public class UserController {

	public String test() {
	    // return string begin with @, mean show it on Screen
	    return "@" + new java.util.Date();
	}
	
	public String velocity() {
	    // return a string , means find pages in webapp/views/ begin with user-velocity.
	    // create a file named user-velocity.vm under webapp/views/
	    return "user-velocity";
	}
	
	@Get("list-by-group")
    public String listByGroup(@Param("groupId") String groupId) {
        return "@${groupId} " + groupId;
    }
	
	@Get("list-by-group-{groupId:\\d+}")
    public String listByGroup3(@Param("groupId") String groupId) {
        return "@int-${groupId}";
    }
	
	public Object json(@Param("id") String id) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", "rose");
        json.put("text", "can has Chinese");
        // rose will call json.toString() to render
        return json;
    }
}