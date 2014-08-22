package com.luoyan.syntax;


public class Simple {
	A m_a = new A();
	Simple() {
		System.out.println("Construct Simple");
	}
	int add(int a, int b) {
		System.out.println("add in Simple");
		return m_a.add(a, b);
	}
}
