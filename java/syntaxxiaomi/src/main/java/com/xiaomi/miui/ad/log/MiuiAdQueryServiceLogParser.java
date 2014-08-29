package com.xiaomi.miui.ad.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.appstore.thrift.model.AppMarketSearchParam;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.AlgorithmDownloadDetailJSONParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.AlgorithmDownloadDetailTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.AppStoreTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.BiddingEffectRecordTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.BiddingStatusChangeTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.ConsumptionDetailTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.FictionEventDetailTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.FictionTabParser;
import com.xiaomi.miui.ad.log.MiuiAdStoreServiceLogParser.OtherBiddingEffectRecordTabParser;
import com.xiaomi.miui.ad.thrift.model.ClientInfo;
import com.xiaomi.miui.ad.thrift.model.ClientInfoV3;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAlgorithm;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAlgorithmExposeDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAppStoreSearchExpose;
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;
import com.xiaomi.miui.ad.thrift.model.SearchAdResult;
import com.xiaomi.miui.ad.util.ThriftHelper;

public class MiuiAdQueryServiceLogParser {
	private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
	public static TBase parseAlgorithmExposeDetail(MiuiLogScribeInfo miuiLogScribeInfo, JSONObject jsonObject) {

    	MiuiAdQueryServiceLogAlgorithmExposeDetail miuiAdQueryServiceLogAlgorithmExposeDetail = new MiuiAdQueryServiceLogAlgorithmExposeDetail();
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setScribeInfo(miuiLogScribeInfo);
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setLogType(jsonObject.getString("log_type"));
    	
    	ClientInfo clientInfo = new ClientInfo();
    	JSONObject client_info = jsonObject.getJSONObject("client_info");

    	if (client_info.has("imei"))
    		clientInfo.setImei(client_info.getString("imei"));
    	clientInfo.setUserid(Integer.parseInt(client_info.getString("user_id")));
    	JSONArray positions = client_info.getJSONArray("positions");
    	List<Integer> positionList = new ArrayList<Integer>();
    	for (int i = 0; i < positions.size(); i++) {
    		positionList.add(positions.getInt(i));
    	}
    	clientInfo.setPositions(positionList);
    	clientInfo.setPositionType(client_info.getString("position_type"));
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setClientInfo(clientInfo);
    	
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setExperiment(jsonObject.getString("miui_ad_exp"));
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setAlgorithmName(jsonObject.getString("miui_ad_alg"));
    	
    	List<String> packageNameList = new ArrayList<String>();
    	JSONArray miui_ad_ads = jsonObject.getJSONArray("miui_ad_ads");
    	for (int i = 0; i < miui_ad_ads.size();i++ ) {
    		packageNameList.add(miui_ad_ads.getString(i));
    	}
    	miuiAdQueryServiceLogAlgorithmExposeDetail.setPackageNameList(packageNameList);
    	return miuiAdQueryServiceLogAlgorithmExposeDetail;
	}
	
	public class AlgorithmExposeDetailJSONParser implements LogHandler.JSONParser {

		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo,
				JSONObject jsonObject) {
			return parseAlgorithmExposeDetail(miuiLogScribeInfo, jsonObject);
		}
	}
	
	public static TBase parseAppStoreSearchExpose(MiuiLogScribeInfo miuiLogScribeInfo, JSONObject jsonObject) {
    	String clientInfoV3Str = jsonObject.getString("client_info_v3");
    	ClientInfoV3 clientInfoV3 = new ClientInfoV3();
    	try {
    	    ThriftHelper.jsonStringToThrift(clientInfoV3, new String(Base64.decodeBase64
                (clientInfoV3Str)));
    	}
    	catch (TException e) {
    		LOGGER.warn("warning format clientInfoV3 [\n" + clientInfoV3Str + "\n");
    		return null;
    	}

    	String searchParamStr = jsonObject.getString("search_param");
        AppMarketSearchParam searchParam = new AppMarketSearchParam();
    	try {
    	    ThriftHelper.jsonStringToThrift(searchParam, new String(Base64.decodeBase64
                (searchParamStr)));
    	}
    	catch (TException e) {
    		LOGGER.warn("warning format searchParam [\n" + searchParamStr + "\n");
    		return null;
    	}

    	String searchAdResultStr = jsonObject.getString("search_ad_result");
        SearchAdResult searchAdResult = new SearchAdResult();
    	try {
            ThriftHelper.jsonStringToThrift(searchAdResult, new String(Base64.decodeBase64(searchAdResultStr)));
    	}
    	catch (TException e) {
    		LOGGER.warn("warning format searchAdResult [\n" + searchAdResultStr + "\n");
    		return null;
    	}
        
        MiuiAdQueryServiceLogAppStoreSearchExpose miuiAdQueryServiceLogAppStoreSearchExpose = new MiuiAdQueryServiceLogAppStoreSearchExpose();
        miuiAdQueryServiceLogAppStoreSearchExpose.setScribeInfo(miuiLogScribeInfo);
        miuiAdQueryServiceLogAppStoreSearchExpose.setClientInfoV3(clientInfoV3);
        miuiAdQueryServiceLogAppStoreSearchExpose.setLogType(jsonObject.getString("log_type"));
        miuiAdQueryServiceLogAppStoreSearchExpose.setSearchParam(searchParam);
        miuiAdQueryServiceLogAppStoreSearchExpose.setSearchAdResult(searchAdResult);
        return miuiAdQueryServiceLogAppStoreSearchExpose;
	}

	public class AppStoreSearchExposeJSONParser implements LogHandler.JSONParser {

		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo,
				JSONObject jsonObject) {
			return parseAppStoreSearchExpose(miuiLogScribeInfo, jsonObject);
		}
	}
	
	public static TBase parseAlgorithm(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
		if (items.length != 4 && items.length != 3)
			return null;

		String miuiAdAlgorithm = items[0];
		String clientInfoStr = items[1];
		int unknown1 = Integer.parseInt(items[2]);
        MiuiAdQueryServiceLogAlgorithm miuiAdQueryServiceLogAlgorithm = new MiuiAdQueryServiceLogAlgorithm();
        miuiAdQueryServiceLogAlgorithm.setScribeInfo(miuiLogScribeInfo);
        miuiAdQueryServiceLogAlgorithm.setAlgorithmName(miuiAdAlgorithm);
        miuiAdQueryServiceLogAlgorithm.setClientInfoStr(clientInfoStr);
        miuiAdQueryServiceLogAlgorithm.setUnknown1(unknown1);
        if (items.length == 4) {
        	String packageNameList = items[3];
        	miuiAdQueryServiceLogAlgorithm.setPackageNameList(packageNameList);
        }
        return miuiAdQueryServiceLogAlgorithm;
	}

	public class AlgorithmTabParser implements LogHandler.TabParser {

		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
			return parseAlgorithm(miuiLogScribeInfo, items);
		}
	}
	
	public void parse(String fileName, int maxRecordNum) throws IOException, TException {
		Map<String, LogHandler> logHandlerMap = new HashMap<String, LogHandler>();
		logHandlerMap.put("algorithm_expose_detail", new LogHandler("algorithm_expose_detail", 
				null, new AlgorithmExposeDetailJSONParser()));
		logHandlerMap.put("app_store_search_expose", new LogHandler("app_store_search_expose", null, new AppStoreSearchExposeJSONParser()));
		Map<String, Long> logCountMap = new HashMap<String, Long>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int total = 0;
        int count = 0;
        int exceptionNum = 0;
        int badFormatNum = 0;
        int AppStoreSearchExposeNum = 0;
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
				if (itemLevel1Array.length != 3) {
					LOGGER.warn("warning format itemLevel1Array length = "
							+ itemLevel1Array.length + " [" + line.trim() + "]");
					badFormatNum++;
					continue;
				} else {
					String scribeInfo = itemLevel1Array[0];
					String time = itemLevel1Array[1];
					String itemLevel2 = new String(
							Base64.decodeBase64(itemLevel1Array[2]));
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("scribeInfo", scribeInfo);
					jsonObject.put("time", time);
					MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
					miuiLogScribeInfo.setScribeInfo(scribeInfo);
					miuiLogScribeInfo.setTime(time);
					String logType = null;

					if (itemLevel2.startsWith("{")) {
						JSONObject itemLevel2Object = JSONObject
								.fromObject(itemLevel2);
						logType = itemLevel2Object.getString("log_type");
						LogHandler logHandler = logHandlerMap.get(logType);
						if (logHandler == null || logHandler.getJSONParser() == null) {
							LOGGER.debug("unknown logType " + logType + " itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
							continue;
						}
						TBase thriftObject = logHandler.getJSONParser().parse(miuiLogScribeInfo, itemLevel2Object);
						if (thriftObject == null) {
							LOGGER.debug("badformat logType " + logType + " itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
							continue;
						}
					} else {
						String[] itemLevel2Array = itemLevel2.split("\t");
						logType = itemLevel2Array[0];
						LogHandler logHandler = logHandlerMap.get(logType);
						if (logHandler == null) {
							logHandler = new LogHandler(logType, new AlgorithmTabParser(), null);
						}
						if (logHandler == null || logHandler.getTabParser() == null) {
							LOGGER.debug("unknown logType " + logType + "itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
							continue;
						}
						TBase thriftObject = logHandler.getTabParser().parse(miuiLogScribeInfo, itemLevel2Array);
						if (thriftObject == null) {
							LOGGER.debug("badformat logType " + logType + " itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
							continue;
						}
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
        LOGGER.info("total " + total + " valid " + count + " exception " + exceptionNum + " badFormatNum " + badFormatNum + " AppStoreSearchExposeNum " + AppStoreSearchExposeNum);
        Iterator iter = logCountMap.entrySet().iterator(); 
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			LOGGER.info("logType " + key + " count " + val);
		}
        br.close();
	}
}
