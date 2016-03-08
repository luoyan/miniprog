/**
 * 
 */
package com.luoyan.syntax;

import java.util.Random;

/**
 * @author xiaomi
 *
 */
public class RandomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random r1 = new Random(20);
		for (int i = 0; i < 200; i++) {
			int n = r1.nextInt(100);
			System.out.println("" + n);
		}
	}

}
