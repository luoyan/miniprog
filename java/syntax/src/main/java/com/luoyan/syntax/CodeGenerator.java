package com.luoyan.syntax;

public class CodeGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] codes = {
			    "private Double cIncome;",
			    "private Double mIncome;",
			    "private Double cCash;",
			    "private Double cCashBack;",
			    "private Double mSoldRate;",
			    "private Double cpm;",
			    "private Double cpc;",
			    "private Double cpd;",
		};
		for (String code : codes) {
			code = code.replace(";", "");
			String[] terms = code.split(" ");
			String type = terms[1];
			String variable = terms[2];
			String Val = "";
			char c = variable.charAt(0);
			if (c >= 'a' && c <= 'z') {
				//System.out.println("c1 " + (int)c);
				c = (char)((int)c + ((int)('A') - (int)('a')));
				//System.out.println("c2 " + (int)c); 
			}
			Val = "" + c + variable.substring(1);
			System.out.println("\tpublic " + type + " get" + Val + "() {");
			System.out.println("\t\treturn " + variable + ";");
			System.out.println("\t}");
			System.out.println("\t");
			System.out.println("\tpublic void set" + Val + "(" + type + " " + variable + ") {");
			System.out.println("\t\tthis." + variable + " = " + variable + ";");
			System.out.println("\t}");
		}
	}

}
