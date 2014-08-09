package com.xiaomi.miui.ad.webpages.controllers;

import com.xiaomi.miui.ad.biz.DemoBiz;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.var.Model;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alexfang on 2014/8/7.
 */
@Path("")
public class DemoController {
    @Autowired
    private Invocation inv;

    @Autowired
    private DemoBiz demoBiz;


    @Get("")
    public String defaultController() {
        return "r:demo";
    }

    @Get("demo")
    public String demo() {
        Model model = inv.getModel();
        model.add("demos", demoBiz.getAllDemo());
        return "demo";
    }
    
    @Get("get_json")
    public String get_json() {
    	Model model = inv.getModel();
    	String json = "{\"firstName\":\"Bill\",\"lastName\":\"Gates\",\"age\":60}";
    	model.add("data", json);
    	return "@" + json;
    }
    
    @Get("get_json_start")
    public String get_json_index() {
    	return "get_json";
    }
    
    @Get("line_labels")
    public String line_labels() {
    	return "line-labels";
    }
    
    @Get("get_data")
    public String get_data() {
    	String json = "{\"data1\":[7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6], \"data2\":[3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]}";
    	return "@" + json;
    }
}
