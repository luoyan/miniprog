package com.luoyan.syntax;

import java.util.Set;

import redis.clients.jedis.Jedis;
public class JedisTest {
	private static void usage() {
		System.err.println("command keys host");
	}
	private static String get(Jedis jedis, String key) {
		try {
			String value = jedis.get(key);
			return value;
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		String command = args[0];
		String host = args[1];
		if (command.equals("keys")) {
		    Jedis jedis = new Jedis(host);
		    Set<String> jedisResultSet = jedis.keys("*");
		    for (String key : jedisResultSet) {
		        String value = get(jedis, key);
		        System.out.println("key [" + key + "] value [" + value + "]");
            }
		}
		else {
			usage();
			System.exit(-1);
		}
	}
}
