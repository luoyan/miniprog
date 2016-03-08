package com.xiaomi.miui.ad.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.hadoop.io.Text;

import com.google.common.collect.ImmutableSet;




public class TestSessionLog {

    private static final Set<String> AD_STORE_AND_QUERY_VALID_DETAIL_KEYS = ImmutableSet.of("positions", "miui_ad_alg", "miui_ad_exp",
            "miui_ad_ads", "category_id", "position_type", "position", "package_name", "consumption_type",
            "download_no", "price", "consumption");
	private static void parseLog(String line) {
        String contentStr = line;
    	JSONObject rawLogJsonObject = JSONObject.fromObject(contentStr);
    	if (rawLogJsonObject.containsKey("ad_action_log")) {
    		System.out.println("ad_billing_detail-ad_action_log");
            //context.getCounter("AdMiddletiersSession", "ad_billing_detail-ad_action_log").increment(1);
    		JSONObject adActionLogObject = rawLogJsonObject.getJSONObject("ad_action_log");
			if (adActionLogObject.containsKey("action_type") && adActionLogObject.getString("action_type").equals("APP_START_DOWNLOAD")
					&& adActionLogObject.containsKey("media_type_list") && adActionLogObject.getJSONArray("media_type_list").size() == 1
					) {
	    		System.out.println("ad_billing_detail-ad_action_log-media_type_list");
                //context.getCounter("AdMiddletiersSession", "ad_billing_detail-ad_action_log-media_type_list").increment(1);
				int mediaType = Integer.parseInt(adActionLogObject.getJSONArray("media_type_list").getString(0));
				if (mediaType == com.xiaomi.miui.ad.core.common.enums.directionalconf.MediaType.APP_STORE.getId()) {
            		if (adActionLogObject.containsKey("imei")) {
        	    		System.out.println("ad_middletiers_download_detail_appstore-imei");
                        //context.getCounter("AdMiddletiersSession", "ad_middletiers_download_detail_appstore-imei").increment(1);
                        if (!rawLogJsonObject.containsKey("fee")) {
                            //context.getCounter("AdMiddletiersSession", "ad_middletiers_download_detail_appstore-imei-no-fee").increment(1);
                            return;
                        }
            		    JSONObject actionJsonObject = new JSONObject();
            		    //setDate(actionJsonObject, rawLogJsonObject, null, logFields[1]);
            		    actionJsonObject.put("Service", "AD");
            		    actionJsonObject.put("Type", "algorithm_download_detail");
            		    actionJsonObject.put("Path", "");
                        JSONObject detailJsonObject = new JSONObject();
                        for (String detailKey : AD_STORE_AND_QUERY_VALID_DETAIL_KEYS) {
                            if (adActionLogObject.containsKey(detailKey)) {
                                detailJsonObject.put(detailKey, adActionLogObject.get(detailKey));
                            }
                        }
                        int ad_id = adActionLogObject.getInt("ad_id");                                
                        String publisherCategoryId = adActionLogObject.getString("publisher_category_id");
                        String[] publisherCategoryIdArray = publisherCategoryId.split("_");
                        if (publisherCategoryIdArray == null || publisherCategoryIdArray.length != 2) {
                            //context.getCounter("AdMiddletiersSession", "ad_middletiers_download_detail_appstore-imei-invalid-publisher_category_id").increment(1);
                            return;
                        }
                        int categoryId = Integer.parseInt(publisherCategoryIdArray[0]);
                        String position = publisherCategoryIdArray[1];
                        String packageName = null;
                        //if (!SessionUtils.adCreativeIdPackageNameMap.containsKey(ad_id)) {
                        //	context.getCounter("AdMiddletiersSession", "ad_middletiers_download_detail_appstore-unknown-adCreativeId").increment(1);
                        	packageName = "unknown_ad_id_" + ad_id;
                        //}
                        //else {
                        //    packageName = SessionUtils.adCreativeIdPackageNameMap.get(ad_id);
                        //}
                        detailJsonObject.put("download_no", 1);
                        detailJsonObject.put("package_name", packageName);
                        detailJsonObject.put("position", position);
                        detailJsonObject.put("categoryId", categoryId);
                        detailJsonObject.put("consumption_type", "featured");
                    	//JSONObject bidding_info = rawLogJsonObject.getJSONObject("bidding_info");
                    	//JSONObject algorithm_info = rawLogJsonObject.getJSONObject("algorithm_info");
                        int price = Integer.parseInt(rawLogJsonObject.getString("fee")) / 1000;
                        detailJsonObject.put("price", price);
                        detailJsonObject.put("consumption", price);
                        //detailJsonObject.put("miui_ad_alg", algorithm_info.getString("algorithm_name"));
                        //detailJsonObject.put("miui_ad_exp", algorithm_info.getString("exp_id"));
                        actionJsonObject.put("Detail", detailJsonObject);
        	    		System.out.println("ad_middletiers_download_detail_appstore-imei-done");

                        //context.write(new Text(adActionLogObject.getString("imei")), new Text(actionJsonObject.toString()));
                	}
                	else {
                        //context.getCounter("AdMiddletiersSession", "ad_middletiers_download_detail_appstore-no-imei").increment(1);
                	}
				}
			}
    	}
	}
	public static void main(String[] args) throws IOException {
		/*
		String fileName = args[0];

		BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = br.readLine()) != null) {
            parseLog(line);
        }*/

        Map<Long, String> map = new HashMap<Long, String>();
        Long ad_id = Long.parseLong("100");
        Integer ad_id2 = Integer.parseInt("100");
        map.put(ad_id, "com.hello");
        System.out.println("map.containsKey(ad_id) " + map.containsKey(ad_id));
        System.out.println("map.containsKey(ad_id2) " + map.containsKey(ad_id2));
	}

}
