package com.xiaomi.miui.ad.dao.statistic;

import java.util.Date;
import java.util.List;

import com.xiaomi.miui.ad.model.PageStatsData;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO
public interface PageStatsDAO {
    public static String INSERT_FIELDS = "TypeName, TypeId, Dimension, Date, PV, Click, Action, " +
            "bPV, bPVClick, bPVAction, Impression, AdClick, AdAction, " +
            "AdClickRevenue, AdActionRevenue";
    public static String ALL_FIELDS = "ID, " + INSERT_FIELDS;
    public static String SUM_FIELDS_GROUP_BY_DATE = "Date, sum(PageView) as PageView, sum(PageClick) as PageClick, sum(PageAction) as PageAction, " +
            "sum(bPageView) as bPageView, sum(bPageClick) as bPageClick, sum(bPageAction) as bPageAction, sum(Impression) as Impression, sum(AdClick) as AdClick, sum(AdAction) as AdAction, " +
    		"sum(Revenue) as Revenue";
    public static String TABLE = "Page_Stats";
    
    @SQL("INSERT INTO " + TABLE + " (" + INSERT_FIELDS + ") VALUES ("
            + ":1.TypeName, :1.TypeId, :1.Dimension, :1.Date, :1.PV, :1.Click, :1.Action, "
            + ":1.bPV, :1.bPVClick, :1.bPVAction, :1.Impression, :1.AdClick, :1.AdAction, "
            + ":1.AdClickRevenue, :1.AdActionRevenue)")
    public int insert(PageStatsData pageStatsData);
    
    @SQL("SELECT " + ALL_FIELDS + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 order by Date")
    public List<PageStatsData> getAllBetweenDate(Date startDate, Date endDate);
    
    @SQL("SELECT " + SUM_FIELDS_GROUP_BY_DATE + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 group by Date order by Date")
    public List<PageStatsData> getAllBetweenDateGroupByDate(Date startDate, Date endDate);

    @SQL("SELECT " + ALL_FIELDS + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 and TypeId = :3 and Dimension = :4 order by Date")
    public List<PageStatsData> getAllBetweenDateByTypeAndDimension(Date startDate, Date endDate, String TypeId, String Dimension);

    @SQL("SELECT " + ALL_FIELDS + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 and TypeId in (:3) and Dimension = :4 order by Date")
    public List<PageStatsData> getAllBetweenDateByTypesAndDimension(Date startDate, Date endDate, List<String> TypeIdList, String Dimension);
    
    @SQL("SELECT " + SUM_FIELDS_GROUP_BY_DATE + ", Type" + " FROM " + TABLE + " WHERE Date >= :1 and Date <= :2 group by Type order by Type")
    public List<PageStatsData> getAllBetweenDateGroupByType(Date startDate, Date endDate);
}
