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
}
