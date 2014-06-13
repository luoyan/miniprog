package com.luoyan.roseHelloWorld.controllers;
import net.paoding.rose.web.annotation.Path;  
import net.paoding.rose.web.annotation.rest.Get;
@Path("")
public class HelloController {
    @Get("")  
    public String index() {  
        return "@hello world";  
    }
}
