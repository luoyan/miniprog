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
	public static void testZookeeper() {
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.ONEBOX);
        System.out.println("onebox zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.STAGING);
        System.out.println("staging zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.SHANGDI);
        System.out.println("shangdi zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
        
	}
	public void testService() throws TException, CatchableException {
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.STAGING);
        System.out.println("staging zookeeper list : [" + ZKFacade.getZKSettings().getZKServers() + "]");
		List<BiddingInfo> biddingInfos = miuiAdStoreServiceClient.getBiddingInfoByStatus(
                BiddingStatus.NORMAL, 0, 100000);
		int count = 0;
		for (BiddingInfo biddingInfo : biddingInfos) {
			System.out.println(count + " : " + biddingInfo.getName() + "\t" + biddingInfo.getAppId());
			count ++;
		}
		int offset = 0;
		int batchNum = 10000;
		appStoreBackendServiceProxy = new AppStoreBackendServiceProxy();
        List<AppData> appDataList = appStoreBackendServiceProxy.getAppList(offset, batchNum);
        count = 0;
        for (AppData appData : appDataList) {
        	System.out.println(count + " : " + appData.getAppInfo().getPackageName() + "\t" + appData.getAppInfo().getAppId());
        	count ++;
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
	
	public static void main(String[] args) throws TException, CatchableException {
		//Syntax s = RoseBeanFactory.getBean(Syntax.class);
		Syntax s = new Syntax();

		//testZookeeper();
		//s.testService();
		//testGetKeywordList();
		testList();
	}

}
