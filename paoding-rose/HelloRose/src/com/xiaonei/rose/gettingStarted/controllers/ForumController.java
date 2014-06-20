package com.xiaonei.rose.gettingStarted.controllers;
import java.util.Date;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.Path;  
import net.paoding.rose.web.var.Model;

@Path("myforum")
public class ForumController {
	private TestService service;
    @Get("topic")
    public String getTopics() {
        //
        return "topiclist";
    }
    @Get("topictime")
    public String world(Invocation inv) {
        inv.addModel("now", new Date());
        return "topiclist";
    }
    @Get("bean")
    public String test(Model model) {
        Bean bean = new Bean();
        bean.setBeanValue("this_is_a_bean");
        model.add("mybean", bean);
        return "topiclist";
    }
    @Get("array")
    public String test2(Model model) {
        String[] array = {"111","222","333"};
        model.add("array", array);
        return "topiclist";
    }
    @Get("getDao")
    public String getDao() {
        Test t = service.getTest();
    	//Test t = new Test();
    	//t.setId(1);
    	//t.setMsg("100");
        return "@t.id " + t.getId() + " t.msg " + t.getMsg();
    }
    public String throwError() {
    	int a = 1;
    	int b = 0;
    	int c = a / b;
    	return "@throwError";
    }
}
