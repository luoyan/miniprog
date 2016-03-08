package com.luoyan.syntax;

public class andor {
	public static void main(String[] args) {
		long status = 0;
		status = 1 << 1;
		System.out.println("status = " + status);
		status = status | (1 << 2);
		System.out.println("status = " + status);
	}
}
