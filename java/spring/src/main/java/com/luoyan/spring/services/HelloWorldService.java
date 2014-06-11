package com.luoyan.spring.services;
import org.springframework.stereotype.Service;

@Service("helloWorldService")
public class HelloWorldService {
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String sayHello() {
		return "Hello from HelloWorld Service! " + name;
	}
}
