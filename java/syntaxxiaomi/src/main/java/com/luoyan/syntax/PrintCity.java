package com.luoyan.syntax;

import com.xiaomi.data.user.common.UserProfileConstants.City;

public class PrintCity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        for (City value : City.values()) {
        	//System.out.println(value.getValue() + "\t" + value.getCity() + "\t" + value.getLevel() + '\t' + value.getType());
        	System.out.println("    " + value.getValue() + " : ['" + value.getCity() + "', " + value.getLevel() + ",'" + value.getType() + "'],");
        }
	}

}
