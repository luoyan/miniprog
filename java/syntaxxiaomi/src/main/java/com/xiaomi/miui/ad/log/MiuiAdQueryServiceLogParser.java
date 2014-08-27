package com.xiaomi.miui.ad.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.appstore.thrift.model.AppMarketSearchParam;
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
	
	public static TBase parseAlgorithm(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {

		String miuiAdAlgorithm = items[0];
		String clientInfoStr = items[1];
		int unknown1 = Integer.parseInt(items[2]);
		String packageNameList =  items[3];
        MiuiAdQueryServiceLogAlgorithm miuiAdQueryServiceLogAlgorithm = new MiuiAdQueryServiceLogAlgorithm();
        miuiAdQueryServiceLogAlgorithm.setScribeInfo(miuiLogScribeInfo);
        miuiAdQueryServiceLogAlgorithm.setAlgorithmName(miuiAdAlgorithm);
        miuiAdQueryServiceLogAlgorithm.setClientInfoStr(clientInfoStr);
        miuiAdQueryServiceLogAlgorithm.setUnknown1(unknown1);
        miuiAdQueryServiceLogAlgorithm.setPackageNameList(packageNameList);
        return miuiAdQueryServiceLogAlgorithm;
	}
	
	public static void parse(String fileName, int maxRecordNum) throws IOException, TException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int count = 0;
        while ((line = br.readLine()) != null) {
            String[] itemLevel1Array = line.trim().split("\t");
            if (itemLevel1Array.length == 1) {
            	continue;
            }
            if (itemLevel1Array.length != 3) {
            	LOGGER.warn("warning format itemLevel1Array length = " + itemLevel1Array.length + " [" + line.trim() + "]");
            }
            else {
            	String scribeInfo = itemLevel1Array[0];
            	String time = itemLevel1Array[1];
                String itemLevel2 = new String(Base64.decodeBase64(itemLevel1Array[2]));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("scribeInfo", scribeInfo);
                jsonObject.put("time", time);
                MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
                miuiLogScribeInfo.setScribeInfo(scribeInfo);
                miuiLogScribeInfo.setTime(time);
                if (itemLevel2.startsWith("{")) {
                	JSONObject itemLevel2Object = JSONObject.fromObject(itemLevel2);
                    if (itemLevel2Object.get("log_type").equals("algorithm_expose_detail")) {
                    	parseAlgorithmExposeDetail(miuiLogScribeInfo, itemLevel2Object);
                    }
                    else if (itemLevel2Object.get("log_type").equals("app_store_search_expose")) {
                    	parseAppStoreSearchExpose(miuiLogScribeInfo, itemLevel2Object);
                    }
                    else {
                    	LOGGER.warn("warning format itemLevel2Object log_type = " + itemLevel2Object.get("log_type") + "  [\n" + itemLevel2 + "\n]");
                    }
                }
                else {
                	String[] itemLevel3Array = itemLevel2.split("\t");
                	if (itemLevel3Array.length != 4) {
                    	LOGGER.warn("warning format itemLevel3Array length = " + itemLevel3Array.length + " [" + itemLevel2 + "]");
                	} 
                	else {
                		parseAlgorithm(miuiLogScribeInfo, itemLevel3Array);
                	}
                }
            }
            count ++;
            if (maxRecordNum > 0 && count >= maxRecordNum)
            	break;
        }
        br.close();
	}
}
