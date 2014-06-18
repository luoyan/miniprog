package com.xiaonei.rose.gettingStarted.controllers;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;

@Path("")
public class HelloController {

    public String world(Invocation inv) {
        inv.addModel("now", new Date());
        return "hello-world";
    }
}