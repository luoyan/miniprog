package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaomi.appstore.backend.model.AppData;
import com.xiaomi.appstore.backend.model.AppInfo;
import com.xiaomi.localclient.LocalClientFactory;
import com.xiaomi.marketing.exception.CatchableException;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
//import com.xiaomi.miui.ad.query.biz.ClusterRedisCacheBiz;
//import com.xiaomi.miui.ad.query.common.ConstantHelper;
import com.xiaomi.miui.ad.thrift.model.AppStat;
import com.xiaomi.miui.ad.thrift.model.BiddingInfo;
import com.xiaomi.miui.ad.thrift.model.BiddingStatus;
import com.xiaomi.miui.ad.thrift.model.Constants;
import com.xiaomi.miui.ad.thrift.model.CtrScore;
import com.xiaomi.miui.ad.thrift.model.RecommendAd;
import com.xiaomi.miui.ad.thrift.service.MiuiAdStoreService;
import com.xiaomi.miui.ad.util.AndroidCoder;
import com.xiaomi.miui.analytics.util.factory.RoseBeanFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Syntax {
    private static final Logger LOGGER = LoggerFactory.getLogger("consoleLogger");
    private static MiuiAdStoreService.Iface miuiAdStoreServiceClient;
    @Autowired
    private ClusterRedisCacheBiz clusterRedisCacheBiz = new ClusterRedisCacheBiz();
    
    private Map<String, String> adIdMap = new HashMap<String, String>();

    @Autowired
    private AppStoreBackendServiceProxy appStoreBackendServiceProxy;

    private void getZookeeperList(String environment) {
        System.out.println(environment + " zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
    }
    private void getBiddingInfoList() throws TException {
        List<BiddingInfo> biddingInfos = miuiAdStoreServiceClient.getBiddingInfoByStatus(
                BiddingStatus.NORMAL, 0, 100000);
        int count = 0;
        for (BiddingInfo biddingInfo : biddingInfos) {
            System.out.println(count + " : " + biddingInfo.getName() + "\t" + biddingInfo.getAppId());
            count ++;
        }
    }

    public List<String> getAllSearchAdKeyWord() throws TException {
        System.out.println(" zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        String keywordStr = miuiAdStoreServiceClient.getString(Constants
                .REDIS_KEY_PREFIX_MIUI_BIDDING_AD_ALL_SEARCH_KEYWORDS, ConstantHelper.REDIS_ALL_SEARCH_KEYWORD_VERSION);
        //System.out.println(keywordStr);
        System.out.println("keywordStr [" + keywordStr + "]");
        return Utils.getKeywordListFromBase64Content(keywordStr);
    }
    
    public Set<RecommendAd> getReadRecommendAdsBySeachQuery(String queryKeyword) throws TException {
        return getRecommendAds(Constants.REDIS_KEY_PREFIX_MIUI_BIDDING_AD_SEARCH_RECOMMEND_APP, queryKeyword.trim().toLowerCase());
    }
    
    public List<BiddingInfo> getAllBiddingInfo() throws TException {
        return miuiAdStoreServiceClient.getAllBiddingInfo(0, 10000000);
    }
    
    private void getAllPositionTypes() throws TException {
        Set<String> types = miuiAdStoreServiceClient.getAllPositionTypes();
        for (String category : types) {
            System.out.println("category [" + category + "]");
            Set<String> features = miuiAdStoreServiceClient.getManualRecommendFeaturedList(category);
            for (String feature : features) {
                System.out.println("feature : " + feature);
            }
        }
    }
    private void getAppList() throws TException, CatchableException {
        int offset = 0;
        int batchNum = 1000;
        int count = 0;
        appStoreBackendServiceProxy = new AppStoreBackendServiceProxy();
        int batchResultCount = batchNum;
        while (batchResultCount == batchNum) {
            List<AppData> appDataList = appStoreBackendServiceProxy.getAppList(offset, batchNum);
            batchResultCount = appDataList.size();
            offset += batchNum;
            //System.out.println("offset " + offset);
            for (AppData appData : appDataList) {
                System.out.println(count + " : " + appData.getAppInfo().getPackageName() + "\t" + appData.getAppInfo().getAppId() + "\t" + appData.getAppInfo().getDisplayName());
                count ++;
            }
        }
    }
    
    private Map<String, String> getIdAppNameMap() throws TException, CatchableException {
        int offset = 0;
        int batchNum = 1000;
        int count = 0;
        appStoreBackendServiceProxy = new AppStoreBackendServiceProxy();
        int batchResultCount = batchNum;
        Map<String, String> idAppNameMap = new HashMap<String, String>();
        while (batchResultCount == batchNum) {
            List<AppData> appDataList = appStoreBackendServiceProxy.getAppList(offset, batchNum);
            batchResultCount = appDataList.size();
            offset += batchNum;
            //System.out.println("offset " + offset);
            for (AppData appData : appDataList) {
                //System.out.println(count + " : " + appData.getAppInfo().getPackageName() + "\t" + appData.getAppInfo().getAppId() + "\t" + appData.getAppInfo().getDisplayName());
                count ++;
                idAppNameMap.put("" + appData.getAppInfo().getAppId(), appData.getAppInfo().getDisplayName());
            }
        }
        return idAppNameMap;
    }
    
    private void printIdPackageAppNameMap() throws TException, CatchableException {
        int offset = 0;
        int batchNum = 1000;
        int count = 0;
        appStoreBackendServiceProxy = new AppStoreBackendServiceProxy();
        int batchResultCount = batchNum;
        Map<String, String> idAppNameMap = new HashMap<String, String>();
        while (batchResultCount == batchNum) {
            List<AppData> appDataList = appStoreBackendServiceProxy.getAppList(offset, batchNum);
            batchResultCount = appDataList.size();
            offset += batchNum;
            //System.out.println("offset " + offset);
            for (AppData appData : appDataList) {
                String displayName = appData.getAppInfo().getDisplayName();
                JSONObject jsonObject = JSONObject
                        .fromObject(displayName);
                if (jsonObject.containsKey("zh_CN")) {
                    displayName = jsonObject.getString("zh_CN");
                }
                System.out.println(appData.getAppInfo().getPackageName() + "\t" + appData.getAppInfo().getAppId() + "\t" + displayName);
                count ++;
                //idAppNameMap.put("" + appData.getAppInfo().getAppId(), appData.getAppInfo().getDisplayName());
            }
        }
    }
    
    private void getAppDownloadNum() throws IOException, TException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = in.readLine()) != null && s.length() != 0) {
            List<String> packageNames = new ArrayList<String>();
            packageNames.add(s);
            List<AppStat> appStatList = miuiAdStoreServiceClient.getAppStatListByPackageNames(packageNames);
            //System.out.println("appStatList.size() " + appStatList.size());
            for (AppStat appStat : appStatList) {
                System.out.println("downloadNum " + appStat.getAllDownloadNumTotal());
            }
        }
    }
    
    private void getManualRecommendFeatureList() {
        try {
            Set<String> positionTypes = miuiAdStoreServiceClient.getAllPositionTypes();
            System.out.println("Current position types " + positionTypes);
            if (positionTypes.size() > 0) {
                //Map<String, Set<String>> currentManualRecommendFeaturedListMap = new HashMap<String, Set<String>>();
                
                for (String item : positionTypes) {
                    Set<String> set = miuiAdStoreServiceClient.getManualRecommendFeaturedList(item);
                    String info = "";
                    for (String feature : set) {
                        info = info + "," + feature;
                    }
                    System.out.println("positionType : [" + item + "] feature [" + info + "]");
                    //currentManualRecommendFeaturedListMap.put(item, set);
                }
                //System.out.println("Current manual recommend featured list map " + currentManualRecommendFeaturedListMap);
            }
        } catch (Exception e) {
            System.err.println("Failed to update manual recommend featured lists." + e);
        }
    }
    
    private Map<String, Set<RecommendAd>> getSearchAdMap() throws TException {
        List<String> keywordList = getAllSearchAdKeyWord();
        System.out.println("getAllSearchAdKeyWord " + keywordList.size());
        Map<String, Set<RecommendAd>> recommendAdsMap = new HashMap<String, Set<RecommendAd>>();
        for (String keyword : keywordList) {
            String updatedKeyword = keyword.trim().toLowerCase();
            Set<RecommendAd> recommendAds = getReadRecommendAdsBySeachQuery(updatedKeyword);
            recommendAdsMap.put(updatedKeyword, recommendAds);
            System.out.println("keyword\t" + keyword + "\t" + recommendAds.size());
        }
        return recommendAdsMap;
    }
    
    private void getTopQueryWithAds(String fileName, int maxNum, boolean showAll) throws TException, IOException, CatchableException {
        List<BiddingInfo> allBiddingInfo = getAllBiddingInfo();
        Map<String, String> adIdMap = new HashMap<String, String>();
        for (BiddingInfo biddingInfo : allBiddingInfo) {
            adIdMap.put(biddingInfo.getAppId() + "", biddingInfo.getPackageName());
        }
        System.out.println("getAllBiddingInfo " + allBiddingInfo.size());
        this.adIdMap = getIdAppNameMap();//adIdMap;

        Map<String, Set<RecommendAd>> recommendAdsMap = getSearchAdMap();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int total = 0;
        while ((line = br.readLine()) != null) {
            total++;
            String[] items = line.split("\t");
            String keyword = items[0];
            //String searchNum = items[1];
            //String downloadNum = items[2];
            String updatedKeyword = keyword.trim().toLowerCase();
            Set<RecommendAd> recommendAds = recommendAdsMap.get(updatedKeyword);
            //String info = keyword + "\t" + searchNum + "\t" + downloadNum;
            String info = keyword;
            if (recommendAds != null) {
                info = info + "\t" + recommendAds.size() + "\t";
                String appNameList = "";
                for (RecommendAd recommendAd : recommendAds) {
                    String AppName = recommendAd.getPackageName();
                    JSONObject jsonObject = JSONObject.fromObject(AppName);
                    String AppCNName = jsonObject.getString("zh_CN");
                    if (AppCNName == null)
                        AppCNName = AppName;
                    if (appNameList == "")
                        appNameList = appNameList + AppCNName;
                    else
                        appNameList = appNameList + "," + AppCNName;
                }
                info = info + appNameList;
                System.out.println(info);
            }
            else if (showAll){
                info = info + "\t" + 0;
                System.out.println(info);
            }
            if (maxNum > 0 && total >= maxNum)
                break;
        }
    }

    /*
   * recommend ad在redis中存储格式为：
   * imei id:relevancy;id:r
   * elevancy
   * */
    private Set<RecommendAd> getRecommendAds(String prefix, String imei) throws TException {
        String recommendAdStr = miuiAdStoreServiceClient.getString(prefix, imei);
        //LOGGER.debug("Recommend str {}", recommendAdStr);
        Set<RecommendAd> recommendAds = new HashSet<RecommendAd>();
        if (StringUtils.isNotEmpty(recommendAdStr)) {
            String[] items = recommendAdStr.split(Constants.REDIS_VALUE_GROUP_SEPARATOR);
            if (items.length > 0) {
                Map<String, String> appIdMap = adIdMap;
                for (String item : items) {
                    String[] subItems = item.split(Constants.REDIS_VALUE_ITEM_SEPARATOR);
                    if (2 == subItems.length) {
                        RecommendAd newRecommendAd = new RecommendAd();
                        String appId = subItems[0];
                        if (appIdMap.containsKey(appId)) {
                            newRecommendAd.setPackageName(appIdMap.get(appId));
                            newRecommendAd.setRelevancy(Double.parseDouble(subItems[1]));
                            recommendAds.add(newRecommendAd);
                        }
                    }
                }
            }
        }
        return recommendAds;
    }

    private void getAllCtrScores(String algorithmName) throws TException {
        System.out.println("zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        List<CtrScore> ctrScores = miuiAdStoreServiceClient.getAllCtrScores(algorithmName);
        System.out.println("ctrScores " + ctrScores.size());
        for (CtrScore ctrScore : ctrScores) {
            System.out.println("packageName " + ctrScore.getPackageName() + " ctr " + ctrScore.getScore() + " algorithm " + ctrScore.getAlgorithmName());
        }
    }
    
    public static void testGetKeywordList() throws TException {
        String keywordStr = miuiAdStoreServiceClient.getString("ad:11:", "1");
        List<String> keywordList = Utils.getKeywordListFromBase64Content(keywordStr);
        System.out.println("keywordStr : " + keywordStr);
        int n = 0;
        for (String keyword : keywordList) {
        //    System.out.println(n + " keyword : " + keyword);
            n += 1;
        }
        String recommendAdStr = miuiAdStoreServiceClient.getString("ad:9", "�ε�");
        System.out.println("recommendAdStr " + recommendAdStr);
    }
    
    public void getRedisString(String keyPrefix, String key) {
        String bdRecommendSearchAdsJsonStr = clusterRedisCacheBiz.getString(
        		keyPrefix,
        		key);
        System.out.println("bdRecommendSearchAdsJsonStr " + bdRecommendSearchAdsJsonStr);
    }
    
    public String getImeiMd5(String imei) {
    	String imeiMd5 = AndroidCoder.encodeMD5(imei);
    	System.out.println("origin " + imei + " md5 " + imeiMd5);
    	return imeiMd5;
    }
    
    public void addImeiToWhiteList(String imei, int type) throws TException {

        MiuiAdStoreService.Iface client = ClientFactory.getClient(MiuiAdStoreService.Iface.class,
                1000000);
        client.addImeiToWhitelist(imei, type, false);
    }
    
    public void deleteImeiFromWhiteList(String imei, int type) throws TException {

        MiuiAdStoreService.Iface client = ClientFactory.getClient(MiuiAdStoreService.Iface.class,
                1000000);
        client.deleteImeiFromWhitelist(imei, type, false);
    }
    
    private static void usage() {
        System.err.println("args envirenment=[onebox/staging/shangdi] command=[getZookeeperList/getBiddingInfoList/getAppList/getAllPositionTypes/getManualRecommendFeatureList]");
        System.err.println("args envirenment getAllCtrScores algrithm");
        System.err.println("args envirenment getTopQueryWithAds fileName maxNum showAll=0/1");
        System.err.println("algorithm = ma/recommend/simple/decay/random/nma/lrctr/purelrctr");
        System.err.println("args envirenment getRedisString keyPrefix key");
        System.err.println("args envirenment getImeiMd5 imei");
        System.err.println("args envirenment addImeiToWhiteList imei type (3=non,4=cosine,5=bd,other=default)");
        System.err.println("args envirenment deleteImeiFromWhiteList imei type (3=non,4=cosine,5=bd,other=default)");
    }
    
    public static void main(String[] args) throws TException, CatchableException, IOException {
        Syntax s = new Syntax();
        if (args.length < 2) {
            usage();
            System.exit(-1);
        }
        String environment = args[0];
        String command = args[1];
        if (!Utils.setEnvironment(environment)) {
            usage();
            System.exit(-1);
        }
        miuiAdStoreServiceClient = ClientFactory.getClient(MiuiAdStoreService.Iface.class, 100000);
        
        if (command.equals("getZookeeperList")) {
            s.getZookeeperList(environment);
        }
        else if (command.equals("getBiddingInfoList")) {
            s.getBiddingInfoList();
        }
        else if (command.equals("getAppList")) {
            s.getAppList();
        }
        else if (command.equals("getAppDownloadNum")) {
            s.getAppDownloadNum();
        }
        else if (command.equals("getAllPositionTypes")) {
            s.getAllPositionTypes();
        }
        else if (command.equals("getManualRecommendFeatureList")) {
            s.getManualRecommendFeatureList();
        }
        else if (command.equals("getAllCtrScores") && args.length == 3) {
            String algorithmName = args[2];
            s.getAllCtrScores(algorithmName);
        }
        else if (command.equals("getTopQueryWithAds") && args.length == 5) {
            String fileName = args[2];
            int maxNum = Integer.parseInt(args[3]);
            int showAll = Integer.parseInt(args[4]);
            System.out.println(environment + " zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
            s.getTopQueryWithAds(fileName, maxNum, showAll == 1);
        }
        else if (command.equals("printIdPackageAppNameMap")) {
            s.printIdPackageAppNameMap();
        }
        else if (command.equals("getAllSearchAdKeyWord")) {
            s.getAllSearchAdKeyWord();
        }
        else if (command.equals("getRedisString") && args.length == 4) {
        	String keyPrefix = args[2];
        	String key = args[3];
            s.getRedisString(keyPrefix, key);
        }
        else if (command.equals("getImeiMd5") && args.length == 3) {
        	String imei = args[2];
            s.getImeiMd5(imei);
        }
        else if (command.equals("addImeiToWhiteList") && args.length == 4) {
        	String imei = args[2];
        	int type = Integer.parseInt(args[3]);
            s.addImeiToWhiteList(imei, type);
        }
        else if (command.equals("deleteImeiFromWhiteList") && args.length == 4) {
        	String imei = args[2];
        	int type = Integer.parseInt(args[3]);
            s.deleteImeiFromWhiteList(imei, type);
        }
        else {
            usage();
            System.exit(-1);
        }
    }

}
