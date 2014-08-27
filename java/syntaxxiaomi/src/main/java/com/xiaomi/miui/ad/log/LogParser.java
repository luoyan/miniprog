package com.xiaomi.miui.ad.log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.appstore.thrift.model.AppMarketSearchParam;
import com.xiaomi.miui.ad.thrift.model.ClientInfo;
import com.xiaomi.miui.ad.thrift.model.ClientInfoV3;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAlgorithm;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAlgorithmExposeDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogAlgorithmDownloadDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogAppStore;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogBiddingEffectRecord;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogConsumptionDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogFictionEventDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogOtherBiddingEffectRecord;
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;
import com.xiaomi.miui.ad.thrift.model.SearchAdResult;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAppStoreSearchExpose;
import com.xiaomi.miui.ad.util.ThriftHelper;

public class LogParser {
	private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
	public static void usage() {
		System.out.println("LogParser logType=query_service/store_service/appstore logFileName maxRecordNum");
	}
	
	public static void parseAppstoreLog(String fileName, int maxRecordNum) {
		
	}
	
	public static void main(String[] args) throws IOException, TException {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			usage();
			System.exit(-1);
		}
		String logType = args[0];
		String logFileName = args[1];
		int maxRecordNum = Integer.parseInt(args[2]);
		if (logType.equals("query_service")) {
			MiuiAdQueryServiceLogParser.parse(logFileName, maxRecordNum);
		}
		else if (logType.equals("store_service")) {
			MiuiAdStoreServiceLogParser.parse(logFileName, maxRecordNum);
		}
		else if (logType.equals("appstore")) {
			parseAppstoreLog(logFileName, maxRecordNum);
		}
		else {
			usage();
			System.exit(-1);
		}

	}

}
