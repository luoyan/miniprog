package com.xiaonei.rose.gettingStarted.controllers;
import java.util.Date;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.Path;  

@Path("myforum")
public class ForumController {
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
}
