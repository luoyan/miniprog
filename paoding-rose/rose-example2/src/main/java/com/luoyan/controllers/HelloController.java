package com.luoyan.controllers;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
@Path("/hello/")
public class HelloController {
	@Get("world")
	public String index() {
		return "@hello world";
	}
}
