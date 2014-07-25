package com.luoyan.syntax;

import com.xiaomi.appstore.backend.model.AppData;
import com.xiaomi.appstore.backend.model.AppInfo;
import com.xiaomi.appstore.common.LanguageUtilities;
import com.xiaomi.appstore.thrift.service.AppStoreBackendService;
import com.xiaomi.appstore.thrift.service.ListParam;
import com.xiaomi.marketing.exception.CatchableException;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miui.analytics.util.encode.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: alex-fang
 * Date: 14-6-12
 * Time: 下午1:23
 */
@Service
public class AppStoreBackendServiceProxy {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppStoreBackendServiceProxy.class);
    private final AppStoreBackendService.Iface client = ClientFactory.getClient(AppStoreBackendService.Iface.class, 10000);

    public List<AppData> getAppList(int offset, int length) throws TException, CatchableException {
        ListParam listParam = new ListParam();
        listParam.setOffset(offset);
        listParam.setLength(length);
        listParam.setSort(0);
        return client.getAppList(listParam);
    }

    public String getAppDisplayName(String packageName) {
        String displayName = "";
        try {
            AppData appData = client.getAppDataByPackageNameHash(Md5Util.getMd5(packageName).toLowerCase());
            if (null != appData) {
                AppInfo appInfo = appData.getAppInfo();
                if (appInfo != null && StringUtils.isNotEmpty(appInfo.getDisplayName())) {
                    displayName = LanguageUtilities.getLocaleString(appInfo.getDisplayName(), "zh", "CN");
                }
            }
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
        return displayName;
    }
}
