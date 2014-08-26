package com.luoyan.syntax;

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
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;
import com.xiaomi.miui.ad.thrift.model.SearchAdResult;
import com.xiaomi.miui.ad.thrift.model.MiuiAdQueryServiceLogAppStoreSearchExpose;
import com.xiaomi.miui.ad.util.ThriftHelper;

public class LogParser {
	private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
	public static void usage() {
		System.out.println("LogParser logType=query_service/store_service/appstore logFileName maxRecordNum");
	}
	
	public static void parseQueryServiceLog(String fileName, int maxRecordNum) throws IOException, TException {
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
                        //jsonObject.put("itemLevel2", itemLevel2Object);
                        jsonObject.put("itemLevel2", itemLevel2Object);
                    	MiuiAdQueryServiceLogAlgorithmExposeDetail miuiAdQueryServiceLogAlgorithmExposeDetail = new MiuiAdQueryServiceLogAlgorithmExposeDetail();
                    	miuiAdQueryServiceLogAlgorithmExposeDetail.setScribeInfo(miuiLogScribeInfo);
                    	miuiAdQueryServiceLogAlgorithmExposeDetail.setLogType(itemLevel2Object.getString("log_type"));
                    	
                    	ClientInfo clientInfo = new ClientInfo();
                    	JSONObject client_info = itemLevel2Object.getJSONObject("client_info");
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
                    	
                    	miuiAdQueryServiceLogAlgorithmExposeDetail.setExperiment(itemLevel2Object.getString("miui_ad_exp"));
                    	miuiAdQueryServiceLogAlgorithmExposeDetail.setAlgorithmName(itemLevel2Object.getString("miui_ad_alg"));
                    	
                    	List<String> packageNameList = new ArrayList<String>();
                    	JSONArray miui_ad_ads = itemLevel2Object.getJSONArray("miui_ad_ads");
                    	for (int i = 0; i < miui_ad_ads.size();i++ ) {
                    		packageNameList.add(miui_ad_ads.getString(i));
                    	}
                    	miuiAdQueryServiceLogAlgorithmExposeDetail.setPackageNameList(packageNameList);
                    	
                    	//ThriftHelper.jsonStringToThrift(miuiAdQueryServiceLogAlgorithmExposeDetail, jsonObject.toString());
                    }
                    else if (itemLevel2Object.get("log_type").equals("app_store_search_expose")) {
                    	String clientInfoV3Str = itemLevel2Object.getString("client_info_v3");
                    	ClientInfoV3 clientInfoV3 = new ClientInfoV3();
                    	try {
                    	    ThriftHelper.jsonStringToThrift(clientInfoV3, new String(Base64.decodeBase64
                                (clientInfoV3Str)));
                    	}
                    	catch (TException e) {
                    		LOGGER.warn("warning format clientInfoV3 [\n" + clientInfoV3Str + "\n");
                    		return;
                    	}

                    	String searchParamStr = itemLevel2Object.getString("search_param");
                        AppMarketSearchParam searchParam = new AppMarketSearchParam();
                    	try {
                    	    ThriftHelper.jsonStringToThrift(searchParam, new String(Base64.decodeBase64
                                (searchParamStr)));
                    	}
                    	catch (TException e) {
                    		LOGGER.warn("warning format searchParam [\n" + searchParamStr + "\n");
                    		return;
                    	}

                    	String searchAdResultStr = itemLevel2Object.getString("search_ad_result");
                        SearchAdResult searchAdResult = new SearchAdResult();
                    	try {
                            ThriftHelper.jsonStringToThrift(searchAdResult, new String(Base64.decodeBase64(searchAdResultStr)));
                    	}
                    	catch (TException e) {
                    		LOGGER.warn("warning format searchAdResult [\n" + searchAdResultStr + "\n");
                    		return;
                    	}
                        
                        MiuiAdQueryServiceLogAppStoreSearchExpose miuiAdQueryServiceLogAppStoreSearchExpose = new MiuiAdQueryServiceLogAppStoreSearchExpose();
                        miuiAdQueryServiceLogAppStoreSearchExpose.setScribeInfo(miuiLogScribeInfo);
                        miuiAdQueryServiceLogAppStoreSearchExpose.setClientInfoV3(clientInfoV3);
                        miuiAdQueryServiceLogAppStoreSearchExpose.setLogType(itemLevel2Object.getString("log_type"));
                        miuiAdQueryServiceLogAppStoreSearchExpose.setSearchParam(searchParam);
                        miuiAdQueryServiceLogAppStoreSearchExpose.setSearchAdResult(searchAdResult);
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
                		String miuiAdAlgorithm = itemLevel3Array[0];
                		String clientInfoStr = itemLevel3Array[1];
                		int unknown1 = Integer.parseInt(itemLevel3Array[2]);
                		String packageNameList =  itemLevel3Array[3];
                		JSONObject jsonObjectLevel2 = new JSONObject();
                		jsonObjectLevel2.put("miuiAdAlgorithm", miuiAdAlgorithm);
                		jsonObjectLevel2.put("clientInfoStr", clientInfoStr);
                		jsonObjectLevel2.put("unknown1", unknown1);
                		jsonObjectLevel2.put("packageNameList", packageNameList);
                        jsonObject.put("itemLevel2", jsonObjectLevel2);
                        MiuiAdQueryServiceLogAlgorithm miuiAdQueryServiceLogAlgorithm = new MiuiAdQueryServiceLogAlgorithm();
                        miuiAdQueryServiceLogAlgorithm.setScribeInfo(miuiLogScribeInfo);
                        miuiAdQueryServiceLogAlgorithm.setAlgorithmName(miuiAdAlgorithm);
                        miuiAdQueryServiceLogAlgorithm.setClientInfoStr(clientInfoStr);
                        miuiAdQueryServiceLogAlgorithm.setUnknown1(unknown1);
                        miuiAdQueryServiceLogAlgorithm.setPackageNameList(packageNameList);
                	}
                }
                //LOGGER.info("\n" + jsonObject.toString() + "\n");
            }
            count ++;
            if (maxRecordNum > 0 && count >= maxRecordNum)
            	break;
        }
        br.close();
	}

	public static void parseStoreServiceLog(String fileName, int maxRecordNum) throws IOException {
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
                MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
                miuiLogScribeInfo.setScribeInfo(scribeInfo);
                miuiLogScribeInfo.setTime(time);
                if (itemLevel2.startsWith("{")) {
                	JSONObject itemLevel2Object = JSONObject.fromObject(itemLevel2);
                	if (itemLevel2Object.getString("log_type").equals("algorithm_download_detail")) {
                		MiuiAdStoreServiceLogAlgorithmDownloadDetail miuiAdStoreServiceLogAlgorithmDownloadDetail = new MiuiAdStoreServiceLogAlgorithmDownloadDetail();
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setScribeInfo(miuiLogScribeInfo);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setLogType(itemLevel2Object.getString("log_type"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setImei(itemLevel2Object.getString("imei"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPackageName(itemLevel2Object.getString("package_name"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setCategoryId(Integer.parseInt(itemLevel2Object.getString("category_id")));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPosition(itemLevel2Object.getString("position"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumptionType(itemLevel2Object.getString("consumption_type"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setDownloadNo(Long.parseLong(itemLevel2Object.getString("download_no")));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPrice(Long.parseLong(itemLevel2Object.getString("price")));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumption(Long.parseLong(itemLevel2Object.getString("consumption")));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdAlgorithm(itemLevel2Object.getString("miui_ad_alg"));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdExperiment(itemLevel2Object.getString("miui_ad_exp"));
                	}
                	else {
                    	LOGGER.debug("itemLevel2 [\n" + itemLevel2 + "\n");
                	}
                }
                else {
                    String[] itemLevel2Array = itemLevel2.split("\t");
                    if (itemLevel2Array[0].equals("APP_STORE")) {
                    	long timestamp = Long.parseLong(itemLevel2Array[2]);
                    	String appStoreContent = new String(Base64.decodeBase64(itemLevel2Array[1]));
                    	//LOGGER.debug("appStoreContent [\n" + appStoreContent + "\n");
                    	String[] appStoreContentArray = appStoreContent.split("\t");
                    	String clientInfoV3Str = appStoreContentArray[0];
                    	String packageName = appStoreContentArray[1];
                    	String downloadUrl = appStoreContentArray[2];
                    	MiuiAdStoreServiceLogAppStore miuiAdStoreServiceLogAppStore = new MiuiAdStoreServiceLogAppStore();
                    	miuiAdStoreServiceLogAppStore.setScribeInfo(miuiLogScribeInfo);
                    	miuiAdStoreServiceLogAppStore.setLogType("APP_STORE");
                    	miuiAdStoreServiceLogAppStore.setClientInfoV3(clientInfoV3Str);
                    	miuiAdStoreServiceLogAppStore.setPackageName(packageName);
                    	miuiAdStoreServiceLogAppStore.setDownloadUrl(downloadUrl);
                    	miuiAdStoreServiceLogAppStore.setTimestamp(timestamp);
                    }
                    else if (itemLevel2Array[0].equals("algorithm_download_detail")) {
                    	if (itemLevel2Array.length != 10) {
                    		LOGGER.warn("bad format logType " + itemLevel2Array[0] + "\n" + itemLevel2 + "\n");
                    		return;
                    	}
                		MiuiAdStoreServiceLogAlgorithmDownloadDetail miuiAdStoreServiceLogAlgorithmDownloadDetail = new MiuiAdStoreServiceLogAlgorithmDownloadDetail();
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setScribeInfo(miuiLogScribeInfo);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setLogType(itemLevel2Array[0]);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setImei(itemLevel2Array[1]);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPackageName(itemLevel2Array[2]);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setCategoryId(Integer.parseInt(itemLevel2Array[3]));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPosition(itemLevel2Array[4]);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumptionType(itemLevel2Array[5]);
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setDownloadNo(Long.parseLong(itemLevel2Array[6]));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPrice(Long.parseLong(itemLevel2Array[7]));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumption(Long.parseLong(itemLevel2Array[8]));
                		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdAlgorithm(itemLevel2Array[9]);
                    }
                    else if (itemLevel2Array[0].equals("consumption_detail")) {
                    	if (itemLevel2Array.length != 9) {
                    		LOGGER.warn("bad format logType " + itemLevel2Array[0] + "\n" + itemLevel2 + "\n");
                    		return;
                    	}
                    	MiuiAdStoreServiceLogConsumptionDetail miuiAdStoreServiceLogConsumptionDetail = new MiuiAdStoreServiceLogConsumptionDetail();
                    	miuiAdStoreServiceLogConsumptionDetail.setScribeInfo(miuiLogScribeInfo);
                    	miuiAdStoreServiceLogConsumptionDetail.setLogType(itemLevel2Array[0]);
                    	miuiAdStoreServiceLogConsumptionDetail.setImei(itemLevel2Array[1]);
                    	miuiAdStoreServiceLogConsumptionDetail.setPackageName(itemLevel2Array[2]);
                    	miuiAdStoreServiceLogConsumptionDetail.setCategoryId(Integer.parseInt(itemLevel2Array[3]));
                    	miuiAdStoreServiceLogConsumptionDetail.setPosition(itemLevel2Array[4]);
                    	miuiAdStoreServiceLogConsumptionDetail.setConsumptionType(itemLevel2Array[5]);
                    	miuiAdStoreServiceLogConsumptionDetail.setDownloadNo(Long.parseLong(itemLevel2Array[6]));
                    	miuiAdStoreServiceLogConsumptionDetail.setPrice(Long.parseLong(itemLevel2Array[7]));
                    	miuiAdStoreServiceLogConsumptionDetail.setConsumption(Long.parseLong(itemLevel2Array[8]));
                    }
                    else if (itemLevel2Array[0].equals("bidding_effect_record")) {
                    	if (itemLevel2Array.length != 5) {
                    		LOGGER.warn("bad format logType " + itemLevel2Array[0] + "\n" + itemLevel2 + "\n");
                    		return;
                    	}
                    	MiuiAdStoreServiceLogBiddingEffectRecord miuiAdStoreServiceLogBiddingEffectRecord = new MiuiAdStoreServiceLogBiddingEffectRecord();
                    	miuiAdStoreServiceLogBiddingEffectRecord.setScribeInfo(miuiLogScribeInfo);
                    	miuiAdStoreServiceLogBiddingEffectRecord.setLogType(itemLevel2Array[0]);
                    	miuiAdStoreServiceLogBiddingEffectRecord.setPackageName(itemLevel2Array[1]);
                    	miuiAdStoreServiceLogBiddingEffectRecord.setDownloadNo(Long.parseLong(itemLevel2Array[2]));
                    	miuiAdStoreServiceLogBiddingEffectRecord.setPrice(Long.parseLong(itemLevel2Array[3]));
                    	miuiAdStoreServiceLogBiddingEffectRecord.setConsumption(Long.parseLong(itemLevel2Array[4]));
                    }
                	else {
                    	LOGGER.debug("itemLevel2 [\n" + itemLevel2 + "\n");
                	}
                }
            }
            count ++;
            if (maxRecordNum > 0 && count >= maxRecordNum)
            	break;
        }
        br.close();
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
			parseQueryServiceLog(logFileName, maxRecordNum);
		}
		else if (logType.equals("store_service")) {
			parseStoreServiceLog(logFileName, maxRecordNum);
		}
		else if (logType.equals("store")) {
			parseAppstoreLog(logFileName, maxRecordNum);
		}
		else {
			usage();
			System.exit(-1);
		}

	}

}
