package com.luoyan.syntax;

public class A {
	public A() {
		System.out.println("Construct A");
	}
	int add(int a, int b) {
		System.out.println("add in A");
		return a+b;
	}
}
