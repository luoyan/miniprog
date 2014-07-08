package com.luoyan.syntax;

import java.util.Map;


public enum ConstEnum {
	A(1, "hello"),
	B(0, "world"),
	;
	//private static final Map<Integer, ConstEnum> map = Maps.newHashMap();
	static {
		for (ConstEnum e : ConstEnum.values()) {
			System.out.println("e.id " + e.id + "\te.description " + e.description);
		}
	}
    private int id;
    private String description;
    private ConstEnum(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
