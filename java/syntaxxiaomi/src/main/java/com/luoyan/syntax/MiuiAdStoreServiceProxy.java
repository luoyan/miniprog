package com.luoyan.syntax;

import com.xiaomi.localclient.LocalClientFactory;
import com.xiaomi.miui.ad.thrift.model.*;
import com.xiaomi.miui.ad.thrift.service.MiuiAdStoreService;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: alex-fang
 * Date: 13-8-19
 * Time: 下午3:30
 */
@Service
public class MiuiAdStoreServiceProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiuiAdStoreServiceProxy.class);
    private final MiuiAdStoreService.Iface miuiAdStoreServiceClient = LocalClientFactory.getClient(MiuiAdStoreService.Iface.class);

    public List<BiddingInfo> getBiddingInfo(BiddingStatus status, int start, int len) throws TException {
        return miuiAdStoreServiceClient.getBiddingInfoByStatus(status, start, len);
    }

    public boolean updateExposeNo(String packageName, long exposeNo) throws TException {
        return miuiAdStoreServiceClient.updateExposeNo(packageName, exposeNo);
    }

    public ExposeAndClickStatistic getExposeAndClickStatistic(String packageName) throws TException {
        return miuiAdStoreServiceClient.getExposeAndClickStatistic(packageName);
    }

    public boolean insertExposeAndClickStatistic(ExposeAndClickStatistic exposeAndClickStatistic) throws TException {
        return miuiAdStoreServiceClient.insertExposeAndClickStatistic(exposeAndClickStatistic);
    }

    public List<CtrScore> getMACtrScores() throws TException {
        return miuiAdStoreServiceClient.getAllCtrScores(Constants.MA_CTR_ALGORITHM_NAME);
    }

    public double getMAAverageScore() throws TException {
        return miuiAdStoreServiceClient.getAverageScore(Constants.MA_CTR_ALGORITHM_NAME);
    }

    public Set<String> getManualRecommendFeatureList(String positionType) throws TException {
        return miuiAdStoreServiceClient.getManualRecommendFeaturedList(positionType);
    }

    public Set<String> getAbnormalPackageNames() throws TException {
        String packageNamesStr = miuiAdStoreServiceClient.getString(
                Constants.REDIS_KEY_PREFIX_ABNORMAL_STATUS_APP, Constants.REDIS_KEY_ABNORMAL);
        if (StringUtils.isNotEmpty(packageNamesStr)) {
            return new HashSet<String>(
                    Arrays.asList(packageNamesStr.split(Constants.REDIS_ABNORMAL_VALUE_SEPARATOR)));
        } else {
            return new HashSet<String>();
        }
    }

    public Set<String> getAllPositionTypes() throws TException {
        return miuiAdStoreServiceClient.getAllPositionTypes();
    }

    public List<BiddingInfo> getAllBiddingInfo() throws TException {
        return miuiAdStoreServiceClient.getAllBiddingInfo(0, 10000000);
    }

    public List<AdInfo> getAllAdInfo() throws TException {
        return miuiAdStoreServiceClient.getAllAdInfo(0, 10000000);
    }

    /**
     * @param packageName
     * @return
     * @throws TException
     */
    public List<BiddingEffect> getRecentBiddingEffectList(String packageName) throws TException {
        return miuiAdStoreServiceClient.getBiddingEffectByPackageName(packageName, 0, 5);
    }

    public List<AppStat> getAppStatListByPackageNames(List<String> packageNames) throws TException {
        return miuiAdStoreServiceClient.getAppStatListByPackageNames(packageNames);
    }
}
