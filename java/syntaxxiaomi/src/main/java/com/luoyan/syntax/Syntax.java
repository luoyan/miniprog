package com.luoyan.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaomi.appstore.backend.model.AppData;
import com.xiaomi.appstore.backend.model.AppInfo;
import com.xiaomi.localclient.LocalClientFactory;
import com.xiaomi.marketing.exception.CatchableException;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import com.xiaomi.miui.ad.thrift.model.BiddingInfo;
import com.xiaomi.miui.ad.thrift.model.BiddingStatus;
import com.xiaomi.miui.ad.thrift.model.Constants;
import com.xiaomi.miui.ad.thrift.service.MiuiAdStoreService;
import com.xiaomi.miui.analytics.util.factory.RoseBeanFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Syntax {
    private static MiuiAdStoreService.Iface miuiAdStoreServiceClient = ClientFactory.getClient(MiuiAdStoreService.Iface.class, 100000);

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
	
	public static void testGetKeywordList() throws TException {
        String keywordStr = miuiAdStoreServiceClient.getString("ad:11:", "1");
        List<String> keywordList = Utils.getKeywordListFromBase64Content(keywordStr);
        System.out.println("keywordStr : " + keywordStr);
        int n = 0;
        for (String keyword : keywordList) {
        //	System.out.println(n + " keyword : " + keyword);
        	n += 1;
        }
        String recommendAdStr = miuiAdStoreServiceClient.getString("ad:9", "�ε�");
        System.out.println("recommendAdStr " + recommendAdStr);
	}
	
	public static void testString() {
		//String str = new String("\xe5\xbe\xae\xe8\xa7\x86");
		//System.out.println(str);
	}
	
	public static void testList() {
		List<Long> array = new ArrayList<Long>();
		array.add((long)0);
		array.add((long)1);
		array.add((long)3);
		array.add((long)4);
		array.add((long)5);
		array.add(2, (long)2);
		int i = 0;
		for (long n : array) {
			System.out.println("array[" + i + "] = " + n);
			i++;
		}
	}
	private static void usage() {
		System.err.println("args envirenment=[onebox/staging/shangdi] command=[getZookeeperList/getBiddingInfoList/getAppList]");
	}
	
	public static void main(String[] args) throws TException, CatchableException {
		Syntax s = new Syntax();
		if (args.length != 2) {
			usage();
			System.exit(-1);
		}
		String environment = args[0];
		String command = args[1];
		if (environment.equals("onbox")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.ONEBOX);
		}
		else if (environment.equals("staging")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.STAGING);
		}
		else if (environment.equals("shangdi")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.SHANGDI);
		}
		else {
			usage();
			System.exit(-1);
		}
		if (command.equals("getZookeeperList")) {
			s.getZookeeperList(environment);
		}
		else if (command.equals("getBiddingInfoList")) {
			s.getBiddingInfoList();
		}
		else if (command.equals("getAppList")) {
			s.getAppList();
		}
		else {
			usage();
			System.exit(-1);
		}
	}

}
