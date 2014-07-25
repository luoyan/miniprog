package com.luoyan.syntax;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

import com.xiaomi.appstore.backend.model.AppData;
import com.xiaomi.appstore.thrift.model.AppMarketSearchParam;
import com.xiaomi.appstore.thrift.service.AppStoreBackendService;
import com.xiaomi.marketing.exception.CatchableException;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
//import com.xiaomi.miui.ad.query.proxy.MiuiAdStoreServiceProxy;
import com.xiaomi.miui.ad.thrift.model.AppFitModel;
import com.xiaomi.miui.ad.thrift.model.AppStore;
import com.xiaomi.miui.ad.thrift.model.AppStoreChannel;
import com.xiaomi.miui.ad.thrift.model.ClientInfo;
import com.xiaomi.miui.ad.thrift.model.ClientInfoV3;
import com.xiaomi.miui.ad.thrift.model.Constants;
import com.xiaomi.miui.ad.thrift.model.DeviceInfo;
import com.xiaomi.miui.ad.thrift.model.GameCenter;
import com.xiaomi.miui.ad.thrift.model.MediaType;
import com.xiaomi.miui.ad.thrift.model.SearchAdResult;
import com.xiaomi.miui.ad.thrift.model.UserInfo;
import com.xiaomi.miui.ad.thrift.service.MiuiAdQueryService;

public class QueryDemo {
    private static final String IMEI = "d08c5e09c81b66a8b32cd1c72518ae75";
    private static final String TRIGGER_ID = "987";
    private final AppStoreBackendService.Iface appStoreBackendServiceClient = ClientFactory.getClient(AppStoreBackendService.Iface.class, 10000);
    private static Map<Long, String> loadAppData(String fileName) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        Map<Long, String> appIdNameMap = new HashMap<Long, String>();
        while ((line = br.readLine()) != null) {
            String[] array = line.trim().split("\t");
            long appId = Long.parseLong(array[2]);
            String appName = array[4];
            appIdNameMap.put(appId, appName);
        }
        br.close();
        return appIdNameMap;
    }
    public static void main(String[] args) throws TException, InterruptedException, CatchableException, NumberFormatException, IOException {
//        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.ONEBOX);
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.STAGING);
//        调用鲁谷服务时，�?��添加-Dzookeeper.servers.resourcefile=lg_zookeeper_servers.properties
//        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.PRODUCTION);
//        MiuiAdStoreService.Iface client = ClientFactory.createClient(MiuiAdStoreService.Iface.class, 1000000, new SpecifiedEndpointChooser());
        final MiuiAdQueryService.Iface miuiAdQueryServiceClient = ClientFactory.getClient(MiuiAdQueryService.Iface.class, 1000000);

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setImei("f1a80008dfaee43ff790e7f48a64d6ba");
        clientInfo.setIp("10.0.0.0");
        clientInfo.setUserid(0);
        List<Integer> positions = new ArrayList<Integer>();
        positions.add(1);
        positions.add(2);
        positions.add(3);
        clientInfo.setPositions(positions);
        clientInfo.setPositionType(Constants.PARENT_PAGE_IDENTITY);

        //System.out.println(miuiAdQueryServiceClient.getAds(clientInfo, 0, 10, "appstore"));
        ClientInfoV3 clientInfoV3 = new ClientInfoV3();
        AppMarketSearchParam param = new AppMarketSearchParam();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setModel("Nexus One"); // 用户可见的设备名
        deviceInfo.setDevice("passion"); // 内部设备�?
        deviceInfo.setMiuiVersion("v5");
        deviceInfo.setAndroidVersion("4.4.2");
        deviceInfo.setScreenDensity("1080p");
        deviceInfo.setScreenResolution("1920*1080");
        UserInfo userInfo = new UserInfo();
        userInfo.setImei(IMEI);
        userInfo.setUserid("1");
        userInfo.setAndroidId("1");
        userInfo.setMac("54ab68ed");
        userInfo.setLanguage("zh");
        userInfo.setCountry("CN");
        userInfo.setServiceProvider("中国移动");
        userInfo.setIp("114.255.3.131"); //来自：北京市 联�?
        userInfo.setTriggerId(TRIGGER_ID);
        AppStore appStore = new AppStore();
        appStore.setChannel(AppStoreChannel.SEARCH);
        appStore.setAppFitModel(AppFitModel.PHONE);
        appStore.setSupportSearchAd(true);
        GameCenter gameCenter = new GameCenter();
        clientInfoV3.setDeviceInfo(deviceInfo);
        clientInfoV3.setUserInfo(userInfo);
        clientInfoV3.setGameCenter(gameCenter);
        clientInfoV3.setMediaType(MediaType.APP_STORE);
        clientInfoV3.setAppStore(appStore);
        String [] queryList = {"下厨房", "掌上公交"};
		for (String query : queryList) {
			param.setKeyword(query);
			param.setOffset(0);
			param.setLength(30);
			SearchAdResult res = miuiAdQueryServiceClient.getSearchAdResult(
					clientInfoV3, param);
			System.out.println("query " + query + " getAppIdsSize " + res.getAppIdsSize());

			Map<Long, String> appIdNameMap = loadAppData("out.appData");
			int i = 0;
			for (long appId : res.getAppIds()) {
				String appName = appIdNameMap.get(appId);
				if (appName == null) {
					appName = "null";
				}
				System.out.println(i + "\t" + appId + "[" + appName + "]");
				i++;
			}
        }
    }

}
