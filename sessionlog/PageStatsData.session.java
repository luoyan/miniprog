package com.xiaomi.miui.ad.model;

import java.util.Date;
public class PageStatsData {
    private int ID;
    private String TypeId;
    private String TypeName;
    private Date Date;
    private String Dimension;
    private long PV;
    private long Click;
    private long Action;
    private long bPV;
    private long bPVClick;
    private long bPVAction;
    private long Impression;
    private long AdClick;
    private long AdAction;
    private double AdClickRevenue;
    private double AdActionRevenue;
    
    public PageStatsData() {
    	
    }
    public PageStatsData(String TypeId, Date Date, long PV, long Click, long Action, long bPV, long bPVClick, long bPVAction, 
    		long Impression, long AdClick, long AdAction, long AdClickRevenue, long AdActionRevenue) {
    	this.TypeId = TypeId;
    	this.Date = Date;
    	this.PV = PV;
    	this.Click = Click;
    	this.Action = Action;
    	this.bPV = bPV;
    	this.bPVClick = bPVClick;
    	this.bPVAction = bPVAction;
    	this.Impression = Impression;
    	this.AdClick = AdClick;
    	this.AdAction = AdAction;
    	this.AdClickRevenue = AdClickRevenue;
    	this.AdActionRevenue = AdActionRevenue;
    }
    
    public int getID() {
    	return ID;
    }
    
    public void setID(int ID) {
    	this.ID = ID;
    }
    
    public String getTypeId() {
    	return TypeId;
    }
    
    public void setTypeId(String TypeId) {
    	this.TypeId = TypeId;
    }

    public String getTypeName() {
    	return TypeName;
    }
    
    public void setTypeName(String TypeName) {
    	this.TypeName = TypeName;
    }

    public Date getDate() {
    	return Date;
    }
    
    public void setDate(Date Date) {
    	this.Date = Date;
    }
    
    public String getDimension() {
    	return Dimension;
    }
    
    public void setDimension(String Dimension) {
    	this.Dimension = Dimension;
    }
    
    public long getPV() {
    	return PV;
    }
    
    public void setPV(long PV) {
    	this.PV = PV;
    }
    
    
    public long getClick() {
    	return Click;
    }
    
    public void setClick(long Click) {
    	this.Click = Click;
    }
    
    public long getAction() {
    	return Action;
    }
    
    public void setAction(long Action) {
    	this.Action = Action;
    }

    public long getbPV() {
    	return bPV;
    }
    
    public void setbPV(long bPV) {
    	this.bPV = bPV;
    }
    
    public long getbPVClick() {
    	return bPVClick;
    }
    
    public void setbPVClick(long bPVClick) {
    	this.bPVClick = bPVClick;
    }

    public long getbPVAction() {
    	return bPVAction;
    }
    
    public void setbPVAction(long bPVAction) {
    	this.bPVAction = bPVAction;
    }

    public long getImpression() {
    	return Impression;
    }
    
    public void setImpression(long Impression) {
    	this.Impression = Impression;
    }

    public long getAdClick() {
    	return AdClick;
    }
    
    public void setAdClick(long AdClick) {
    	this.AdClick = AdClick;
    }

    public long getAdAction() {
    	return AdAction;
    }
    
    public void setAdAction(long AdAction) {
    	this.AdAction = AdAction;
    }
    
    public double getAdClickRevenue() {
    	return AdClickRevenue;
    }
    
    public void setAdClickRevenue(double AdClickRevenue) {
    	this.AdClickRevenue = AdClickRevenue;
    }
    
    public double getAdActionRevenue() {
    	return AdActionRevenue;
    }
    
    public void setAdActionRevenue(double AdActionRevenue) {
    	this.AdActionRevenue = AdActionRevenue;
    }

    public double getCoverage()
    {
        return this.PV == 0 ? 0 : (this.bPV * 1.0 / this.PV);
    }

    public double getDepth()
    {
        return this.bPV == 0 ? 0 : (this.Impression * 1.0 / this.bPV);
    }

    public double getCTR()
    {
        return this.Impression == 0 ? 0 : (this.AdClick * 1.0 / this.Impression);
    }

    public double getConversion()
    {
        return this.Impression == 0 ? 0 : (this.AdAction * 1.0 / this.Impression);
    }

    public double getIY()
    {
        return this.PV == 0 ? 0 : (this.Impression * 1.0 / this.PV);
    }

    public double getCY()
    {
        return this.PV == 0 ? 0 : (this.AdClick * 1.0 / this.PV);
    }

    public double getAY()
    {
        return this.PV == 0 ? 0 : (this.AdAction * 1.0 / this.PV);
    }

    public double getCPC()
    {
        return this.AdClick == 0 ? 0 : (this.AdClickRevenue * 1.0 / this.AdClick);
    }

    public double getCPA()
    {
        return this.AdAction == 0 ? 0 : (this.AdActionRevenue * 1.0 / this.AdAction);
    }

    public double getRPM()
    {
        return this.PV == 0 ? 0 : ((this.AdClickRevenue + this.AdActionRevenue) * 1000.0 / this.PV);
    }

    public double getPCR()
    {
        return this.PV == 0 ? 0 : (this.Click * 1.0 / this.PV);
    }

    public double getBiddedPCR()
    {
        return this.bPV == 0 ? 0 : (this.bPVClick * 1.0 / this.bPV);
    }

    public double getPAR()
    {
        return this.PV == 0 ? 0 : (this.Action * 1.0 / this.PV);
    }

    public double getBiddedPAR()
    {
        return this.bPV == 0 ? 0 : (this.bPVAction * 1.0 / this.bPV);
    }
}
