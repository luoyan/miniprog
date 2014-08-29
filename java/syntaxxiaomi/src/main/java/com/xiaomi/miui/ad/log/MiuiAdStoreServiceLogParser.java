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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogAlgorithmDownloadDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogAppStore;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogBiddingEffectRecord;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogBiddingStatusChange;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogConsumptionDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogFiction;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogFictionEventDetail;
import com.xiaomi.miui.ad.thrift.model.MiuiAdStoreServiceLogOtherBiddingEffectRecord;
import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;

public class MiuiAdStoreServiceLogParser {
	private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
	public static TBase parseAlgorithmDownloadDetailByJson(MiuiLogScribeInfo miuiLogScribeInfo, JSONObject jsonObject) {
		MiuiAdStoreServiceLogAlgorithmDownloadDetail miuiAdStoreServiceLogAlgorithmDownloadDetail = new MiuiAdStoreServiceLogAlgorithmDownloadDetail();
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setScribeInfo(miuiLogScribeInfo);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setLogType(jsonObject.getString("log_type"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setImei(jsonObject.getString("imei"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPackageName(jsonObject.getString("package_name"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setCategoryId(Integer.parseInt(jsonObject.getString("category_id")));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPosition(jsonObject.getString("position"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumptionType(jsonObject.getString("consumption_type"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setDownloadNo(Long.parseLong(jsonObject.getString("download_no")));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPrice(Long.parseLong(jsonObject.getString("price")));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumption(Long.parseLong(jsonObject.getString("consumption")));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdAlgorithm(jsonObject.getString("miui_ad_alg"));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdExperiment(jsonObject.getString("miui_ad_exp"));
		return miuiAdStoreServiceLogAlgorithmDownloadDetail;
	}
	
	public static TBase parseAppStore(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
    	long timestamp = Long.parseLong(items[2]);
    	String appStoreContent = new String(Base64.decodeBase64(items[1]));
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
    	return miuiAdStoreServiceLogAppStore;
	}
	
	public static TBase parseAlgorithmDownloadDetailByTab(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
		MiuiAdStoreServiceLogAlgorithmDownloadDetail miuiAdStoreServiceLogAlgorithmDownloadDetail = new MiuiAdStoreServiceLogAlgorithmDownloadDetail();
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setScribeInfo(miuiLogScribeInfo);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setLogType(items[0]);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setImei(items[1]);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPackageName(items[2]);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setCategoryId(Integer.parseInt(items[3]));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPosition(items[4]);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumptionType(items[5]);
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setDownloadNo(Long.parseLong(items[6]));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setPrice(Long.parseLong(items[7]));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setConsumption(Long.parseLong(items[8]));
		miuiAdStoreServiceLogAlgorithmDownloadDetail.setMiuiAdAlgorithm(items[9]);
		return miuiAdStoreServiceLogAlgorithmDownloadDetail;
	}
	
	public static TBase parseConsumptionDetail(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
    	MiuiAdStoreServiceLogConsumptionDetail miuiAdStoreServiceLogConsumptionDetail = new MiuiAdStoreServiceLogConsumptionDetail();
    	miuiAdStoreServiceLogConsumptionDetail.setScribeInfo(miuiLogScribeInfo);
    	miuiAdStoreServiceLogConsumptionDetail.setLogType(items[0]);
    	miuiAdStoreServiceLogConsumptionDetail.setImei(items[1]);
    	miuiAdStoreServiceLogConsumptionDetail.setPackageName(items[2]);
    	miuiAdStoreServiceLogConsumptionDetail.setCategoryId(Integer.parseInt(items[3]));
    	miuiAdStoreServiceLogConsumptionDetail.setPosition(items[4]);
    	miuiAdStoreServiceLogConsumptionDetail.setConsumptionType(items[5]);
    	miuiAdStoreServiceLogConsumptionDetail.setDownloadNo(Long.parseLong(items[6]));
    	miuiAdStoreServiceLogConsumptionDetail.setPrice(Long.parseLong(items[7]));
    	miuiAdStoreServiceLogConsumptionDetail.setConsumption(Long.parseLong(items[8]));
    	return miuiAdStoreServiceLogConsumptionDetail;
	}
	
	public static TBase parseBiddingEffectRecord(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
    	MiuiAdStoreServiceLogBiddingEffectRecord miuiAdStoreServiceLogBiddingEffectRecord = new MiuiAdStoreServiceLogBiddingEffectRecord();
    	miuiAdStoreServiceLogBiddingEffectRecord.setScribeInfo(miuiLogScribeInfo);
    	miuiAdStoreServiceLogBiddingEffectRecord.setLogType(items[0]);
    	miuiAdStoreServiceLogBiddingEffectRecord.setPackageName(items[1]);
    	miuiAdStoreServiceLogBiddingEffectRecord.setDownloadNo(Long.parseLong(items[2]));
    	miuiAdStoreServiceLogBiddingEffectRecord.setPrice(Long.parseLong(items[3]));
    	miuiAdStoreServiceLogBiddingEffectRecord.setConsumption(Long.parseLong(items[4]));
		return miuiAdStoreServiceLogBiddingEffectRecord;
	}
	
	public static TBase parseOtherBiddingEffectRecord(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
    	MiuiAdStoreServiceLogOtherBiddingEffectRecord miuiAdStoreServiceLogOtherBiddingEffectRecord = new MiuiAdStoreServiceLogOtherBiddingEffectRecord();
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setScribeInfo(miuiLogScribeInfo);
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setLogType(items[0]);
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setPackageName(items[1]);
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setMediaType(Integer.parseInt(items[2]));
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setDownloadNo(Long.parseLong(items[3]));
    	miuiAdStoreServiceLogOtherBiddingEffectRecord.setConsumption(Long.parseLong(items[4]));
		return miuiAdStoreServiceLogOtherBiddingEffectRecord;
	}
	
	public static TBase parseFictionEventDetail(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
    	MiuiAdStoreServiceLogFictionEventDetail miuiAdStoreServiceLogFictionEventDetail = new MiuiAdStoreServiceLogFictionEventDetail();
    	miuiAdStoreServiceLogFictionEventDetail.setScribeInfo(miuiLogScribeInfo);
    	miuiAdStoreServiceLogFictionEventDetail.setLogType(items[0]);
    	miuiAdStoreServiceLogFictionEventDetail.setEventType(items[1]);
    	miuiAdStoreServiceLogFictionEventDetail.setImei(items[2]);
    	miuiAdStoreServiceLogFictionEventDetail.setBookId(items[3]);
    	miuiAdStoreServiceLogFictionEventDetail.setBookName(items[4]);
    	miuiAdStoreServiceLogFictionEventDetail.setPackageName(items[5]);
    	miuiAdStoreServiceLogFictionEventDetail.setBookNum(Long.parseLong(items[6]));
    	miuiAdStoreServiceLogFictionEventDetail.setPrice(Long.parseLong(items[7]));
		return miuiAdStoreServiceLogFictionEventDetail;
	}

    private static List<String> getListFromJsonArray(JSONObject adLogJsonObject, String key) {
    	List<String> list = new ArrayList<String>();
        if (adLogJsonObject.containsKey(key)) {
            JSONArray jsonArray = adLogJsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.size();i++) {
            	list.add(jsonArray.getString(i));
            }
        }
        return list;
    }
    
	public static TBase parseFiction(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
		MiuiAdStoreServiceLogFiction miuiAdStoreServiceLogFiction = new MiuiAdStoreServiceLogFiction();
		miuiAdStoreServiceLogFiction.setScribeInfo(miuiLogScribeInfo);
		miuiAdStoreServiceLogFiction.setLogType(items[0]);
        String decodedContent = new String(Base64.decodeBase64(items[1]));
        JSONObject jsonObject = JSONObject.fromObject(decodedContent);
		miuiAdStoreServiceLogFiction.setEventType(getListFromJsonArray(jsonObject, "e"));
		miuiAdStoreServiceLogFiction.setPackageName(getListFromJsonArray(jsonObject, "ai"));
		miuiAdStoreServiceLogFiction.setWifiType(getListFromJsonArray(jsonObject, "n"));
		miuiAdStoreServiceLogFiction.setUnknownCt(getListFromJsonArray(jsonObject, "ct"));
		miuiAdStoreServiceLogFiction.setUnknownAt(getListFromJsonArray(jsonObject, "at"));
		miuiAdStoreServiceLogFiction.setUnknownT(getListFromJsonArray(jsonObject, "t"));
		miuiAdStoreServiceLogFiction.setUnknownS(getListFromJsonArray(jsonObject, "s"));
		miuiAdStoreServiceLogFiction.setUnknownPi(getListFromJsonArray(jsonObject, "pi"));
		miuiAdStoreServiceLogFiction.setUnknownC(getListFromJsonArray(jsonObject, "c"));
		miuiAdStoreServiceLogFiction.setTimestamp(Long.parseLong(items[2]));
		return miuiAdStoreServiceLogFiction;
	}
	
	public static TBase parseBiddingStatusChange(MiuiLogScribeInfo miuiLogScribeInfo, String[] items) {
		MiuiAdStoreServiceLogBiddingStatusChange miuiAdStoreServiceLogBiddingStatusChange = new MiuiAdStoreServiceLogBiddingStatusChange();
		miuiAdStoreServiceLogBiddingStatusChange.setScribeInfo(miuiLogScribeInfo);
		miuiAdStoreServiceLogBiddingStatusChange.setLogType(items[0]);
		miuiAdStoreServiceLogBiddingStatusChange.setPackageName(items[1]);
		String[] status = items[2].split("==>");
		miuiAdStoreServiceLogBiddingStatusChange.setFromStatus(status[0]);
		miuiAdStoreServiceLogBiddingStatusChange.setToStatus(status[1]);
		return miuiAdStoreServiceLogBiddingStatusChange;
	}
	
	public static void parse(String fileName, int maxRecordNum) throws IOException {
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
				if (itemLevel1Array.length != 3) {
					LOGGER.warn("warning format itemLevel1Array length = "
							+ itemLevel1Array.length + " [" + line.trim() + "]");
					badFormatNum++;
				} else {
					String scribeInfo = itemLevel1Array[0];
					String time = itemLevel1Array[1];
					String itemLevel2 = new String(
							Base64.decodeBase64(itemLevel1Array[2]));
					MiuiLogScribeInfo miuiLogScribeInfo = new MiuiLogScribeInfo();
					miuiLogScribeInfo.setScribeInfo(scribeInfo);
					miuiLogScribeInfo.setTime(time);
					if (itemLevel2.startsWith("{")) {
						JSONObject itemLevel2Object = JSONObject
								.fromObject(itemLevel2);
						if (itemLevel2Object.getString("log_type").equals(
								"algorithm_download_detail")) {
							parseAlgorithmDownloadDetailByJson(
									miuiLogScribeInfo, itemLevel2Object);
						} else {
							LOGGER.debug("itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
						}
					} else {
						String[] itemLevel2Array = itemLevel2.split("\t");
						if (itemLevel2Array[0].equals("APP_STORE")) {
							parseAppStore(miuiLogScribeInfo, itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("algorithm_download_detail")) {
							if (itemLevel2Array.length != 10) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseAlgorithmDownloadDetailByTab(
									miuiLogScribeInfo, itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("consumption_detail")) {
							if (itemLevel2Array.length != 9) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseConsumptionDetail(miuiLogScribeInfo,
									itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("bidding_effect_record")) {
							if (itemLevel2Array.length != 5) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseBiddingEffectRecord(miuiLogScribeInfo,
									itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("other_bidding_effect_record")) {
							if (itemLevel2Array.length != 5) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseOtherBiddingEffectRecord(miuiLogScribeInfo,
									itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("fiction_event_detail")) {
							if (itemLevel2Array.length != 8) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseFictionEventDetail(miuiLogScribeInfo,
									itemLevel2Array);
						} else if (itemLevel2Array[0].equals("FICTION")) {
							if (itemLevel2Array.length != 3) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseFiction(miuiLogScribeInfo, itemLevel2Array);
						} else if (itemLevel2Array[0]
								.equals("bidding_status_change")) {
							if (itemLevel2Array.length != 3) {
								LOGGER.warn("bad format logType "
										+ itemLevel2Array[0] + "\n"
										+ itemLevel2 + "\n");
								badFormatNum++;
								continue;
							}
							parseBiddingStatusChange(miuiLogScribeInfo,
									itemLevel2Array);
						} else {
							LOGGER.debug("itemLevel2 [\n" + itemLevel2 + "\n");
							badFormatNum++;
						}
					}
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
        br.close();
	}
}
