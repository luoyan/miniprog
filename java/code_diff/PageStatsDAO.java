package com.xiaomi.miui.ad.dao.statistic;

import java.util.Date;
import java.util.List;

import com.xiaomi.miui.ad.model.statistic.PageStatsData;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO
public interface PageStatsDAO {
    public static String INSERT_FIELDS = "TypeName, TypeId, Dimension, Date, PV, Click, Action, " +
            "bPV, bPVClick, bPVAction, Impression, AdClick, AdAction, " +
            "AdClickRevenue, AdActionRevenue, TimeDimension, Budget, VV, UT, AdImpressionRevenue, bVV, ValidImpression, SupportAdRate, " + 
            "AchievedRatio, JoinedCustomerNum, ConsumedCustomerNum, Balance, ConsumedDiscount, DateTime";
    public static String ALL_FIELDS = "ID, " + INSERT_FIELDS;
    public static String SUM_FIELDS = "TypeName, TypeId, Dimension, Date, sum(PV) as PV, sum(Click) as Click, sum(Action) as Action, " +
            "sum(bPV) as bPV, sum(bPVClick) as bPVClick, sum(bPVAction) as bPVAction, sum(Impression) as Impression, sum(AdClick) as AdClick, sum(AdAction) as AdAction, " +
            "sum(AdClickRevenue) as AdClickRevenue, sum(AdActionRevenue) as AdActionRevenue, TimeDimension, sum(Budget) as Budget, sum(VV) as VV, sum(UT) as UT, " + 
            "sum(AdImpressionRevenue) as AdImpressionRevenue, sum(bVV) as bVV, sum(ValidImpression) as ValidImpression, " +
            "sum(JoinedCustomerNum) as JoinedCustomerNum, sum(ConsumedCustomerNum) as ConsumedCustomerNum, sum(Balance) as Balance, sum(ConsumedDiscount) as ConsumedDiscount";
    public static String TABLE = "Page_Stats";
    
    @SQL("INSERT INTO " + TABLE + " (" + INSERT_FIELDS + ") VALUES ("
            + ":1.TypeName, :1.TypeId, :1.Dimension, :1.Date, :1.PV, :1.Click, :1.Action, "
            + ":1.bPV, :1.bPVClick, :1.bPVAction, :1.Impression, :1.AdClick, :1.AdAction, "
            + ":1.AdClickRevenue, :1.AdActionRevenue, :1.TimeDimension, :1.Budget, :1.VV, :1.UT, "
            + ":1.AdImpressionRevenue, :1.bVV, :1.ValidImpression, :1.SupportAdRate, "
            + ":1.AchievedRatio, :1.JoinedCustomerNum, :1.ConsumedCustomerNum, :1.Balance, :1.ConsumedDiscount, :1.DateTime)")
    public int insert(PageStatsData pageStatsData);
    
    @SQL("SELECT " + ALL_FIELDS + " FROM " + TABLE + " WHERE DateTime >= :1 and DateTime <= :2 and TypeId in (:3) and Dimension in (:4) and TimeDimension = :5 order by DateTime")
    public List<PageStatsData> getAllBetweenDateByTypesAndDimensions(Date startDate, Date endDate, List<String> TypeIdList, List<String> DimensionList, int TimeDimension);
    
    @SQL("select Date from " + TABLE + " WHERE TypeId in (:1) and Dimension in (:2) and TimeDimension = :3 order by DateTime desc limit 1;")
    public Date getRecentDate(List<String> TypeIdList, List<String> DimensionList, int TimeDimension);
    
    @SQL("SELECT " + SUM_FIELDS + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 and TimeDimension = :3 group by TypeId")
    public List<PageStatsData> getSumBetweenDateGroupByType(Date startDate, Date endDate, int TimeDimension);
    
    @SQL("SELECT " + SUM_FIELDS + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 and TimeDimension = :3 and TypeId in (:4) group by TypeId")
    public List<PageStatsData> getSumBetweenDateByTypesGroupByType(Date startDate, Date endDate, int TimeDimension, List<String> TypeIdList);
    
    @SQL("SELECT TypeId FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 and Dimension in (:3) and TimeDimension = :4 group by TypeId order by DateTime")
    public List<String> getAllTypeIdsBetweenDateByDimensions(Date startDate, Date endDate, List<String> DimensionList, int TimeDimension);

    @SQL("select Date from " + TABLE + " WHERE Dimension in (:1) and TimeDimension = :2 order by DateTime desc limit 1;")
    public Date getRecentDateByDimensionsAndTimeDimension(List<String> DimensionList, int TimeDimension);
    
    @SQL("select Dimension from " + TABLE + " group by Dimension;")
    public List<String> getDimensions();
    
}
