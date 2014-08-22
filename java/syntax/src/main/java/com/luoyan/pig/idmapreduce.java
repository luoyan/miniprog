package com.luoyan.pig;

import java.io.IOException;
import org.apache.pig.PigServer;
public class idmapreduce {
	public static void main(String[] args) {
		try {
			PigServer pigServer = new PigServer("mapreduce");
			runIdQuery(pigServer, "passwd");
		} catch (Exception e) {
		}
	}
	public static void runIdQuery(PigServer pigServer, String inputFile)
			throws IOException {
		pigServer.registerQuery("A = load '" + inputFile
				+ "' using PigStorage(':');");
		pigServer.registerQuery("B = foreach A generate $0 as id;");
		pigServer.store("B", "idout");
	}
}
