package com.luoyan.syntax;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import com.xiaomi.miui.ad.thrift.model.Constants;
import com.xiaomi.miui.ad.thrift.service.MiuiAdStoreService;
import org.apache.thrift.TException;

/**
 * Created by IntelliJ IDEA.
 * User: 赵冬 (zhaodong1@xiaomi.com)
 * Date: 2014/9/3
 * Time: 19:40
 */
public class A {
    public static void main(String[] args) throws TException {
        ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.LUGU);
        MiuiAdStoreService.Iface client = ClientFactory.getClient(MiuiAdStoreService.Iface.class,
                1000000);
        System.out.println(client.getString(Constants
                .REDIS_KEY_PREFIX_MIUI_BIDDING_AD_ALL_SEARCH_KEYWORDS, "1"));
        System.out.println("OK");
    }
}

