package com.xiaomi.miui.ad.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.AlgorithmDownloadDetailJSONParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.AlgorithmDownloadDetailTabParser;
import com.xiaomi.miui.ad.thrift.model.MiuiAppStoreLogHttpRequest;
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;
import com.xiaomi.miui.ad.util.HttpHelper;

public class MiuiAppStoreServiceLogParser {
	private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
	private static TBase parseHttpRequest(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) throws MalformedURLException {

		if (items.length != 5)
			return null;
		String logType = items[4];
		String[] columns = items[3].trim().split("\\|");
		if (columns.length != 2) {
			LOGGER.warn("bad format columns.length " + columns.length + " logType " + logType);
			return null;
		}
		long timestamp = Long.parseLong(columns[0]);
		String itemLevel2 = new String(
				Base64.decodeBase64(columns[1]));
		String[] itemLevel3Array = itemLevel2.split(",");
		if (itemLevel3Array.length != 3) {
			LOGGER.warn("bad format itemLevel3Array.length " + itemLevel3Array.length + " itemLevel2 [\n" + itemLevel2 + "\n]");
			return null;
		}

		//LOGGER.debug("logType " + logType + " timestamp " + timestamp + " itemLevel2 [\n" + itemLevel2 + "\n");
		MiuiAppStoreLogHttpRequest miuiAppStoreLogHttpRequest = new MiuiAppStoreLogHttpRequest();
		miuiAppStoreLogHttpRequest.setScribeInfo(miuiLogScribeInfo);
		miuiAppStoreLogHttpRequest.setLogType(logType);
		miuiAppStoreLogHttpRequest.setTimestamp(timestamp);
		String url = itemLevel3Array[0];
		String path = new URL(url).getPath();
		miuiAppStoreLogHttpRequest.setPath(path);
		Map<String, String> httpParams = HttpHelper.getHttpRequestParam(url);
		miuiAppStoreLogHttpRequest.setHttpParams(httpParams);
		String httpMethod = itemLevel3Array[1].split(":")[0];
		String httpMethodPath = itemLevel3Array[1].split(":")[1];
		miuiAppStoreLogHttpRequest.setHttpMethod(httpMethod);
		miuiAppStoreLogHttpRequest.setHttpMethodPath(httpMethodPath);
		String userIdKey = itemLevel3Array[2].split(":")[0];
		if (!userIdKey.equals("userid"))
			return null;
		long userId = Long.parseLong(itemLevel3Array[2].split(":")[1]);
		miuiAppStoreLogHttpRequest.setUserId(userId);
		return miuiAppStoreLogHttpRequest;
	}
	
	public class HttpRequestTabParser implements LogHandler.TabParser {

		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
			try {
				return parseHttpRequest(miuiLogScribeInfo, items);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	public void parse(String fileName, int maxRecordNum) throws IOException, TException {

		Map<String, LogHandler> logHandlerMap = new HashMap<String, LogHandler>();
		logHandlerMap.put("http_request", new LogHandler("http_request", 
				new HttpRequestTabParser(), null));
		Map<String, Long> logCountMap = new HashMap<String, Long>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int total = 0;
        int count = 0;
        int exceptionNum = 0;
        int badFormatNum = 0;
        while ((line = br.readLine()) != null) {
        	try {

				String[] itemLevel1Array = line.trim().split("\t");
				if (itemLevel1Array.length == 1) {
					continue;
				}
				if (maxRecordNum > 0 && total >= maxRecordNum) {
					break;
				}
				total++;
				if (itemLevel1Array.length != 5) {
					LOGGER.warn("warning format itemLevel1Array length = "
							+ itemLevel1Array.length + " [" + line.trim() + "]");
					badFormatNum++;
					continue;
				} else {
					String scribeInfo = itemLevel1Array[0];
					String time = itemLevel1Array[1];
					MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
					miuiLogScribeInfo.setScribeInfo(scribeInfo);
					miuiLogScribeInfo.setTime(time);
					String ip = itemLevel1Array[2];
					miuiLogScribeInfo.setIp(ip);
					String logType = itemLevel1Array[4];
					TBase thriftObject = parseHttpRequest(miuiLogScribeInfo, itemLevel1Array);
					if (thriftObject == null) {
						LOGGER.warn("warning format itemLevel1Array length = "
								+ itemLevel1Array.length + " [" + line.trim() + "]");
						badFormatNum++;
						continue;
					}
					Long logCount = logCountMap.get(logType);
					if (logCount == null) {
						logCount = 0L;
					}
					logCount++;
					logCountMap.put(logType, logCount);
				}
				count++;
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        		LOGGER.warn("failed to parse log [\n" + line + "\n");
        		exceptionNum++;
        	}
        }
        LOGGER.info("total " + total + " valid " + count + " exception " + exceptionNum + " badFormatNum " + badFormatNum);
        Iterator iter = logCountMap.entrySet().iterator(); 
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			LOGGER.info("logType " + key + " count " + val);
		}
	}
}
