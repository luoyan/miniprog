package com.xiaomi.miui.ad.log;

import net.sf.json.JSONObject;

import org.apache.thrift.TBase;

import com.xiaomi.miui.ad.thrift.model.MiuiLogScribeInfo;

public class LogHandler {
	public interface TabParser {
		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo, String[] items);
	};
	public interface JSONParser {
		public TBase parse(MiuiLogScribeInfo miuiLogScribeInfo, JSONObject jsonObject);
	}; 
	private String logType;
	private TabParser tabParser;
	private JSONParser jsonParser;
	public LogHandler(String logType, TabParser tabParser, JSONParser jsonParser) {
		this.logType = logType;
		this.tabParser = tabParser;
		this.jsonParser = jsonParser;
	}
	
	public String getLogType() {
		return this.logType;
	}
	
	public TabParser getTabParser() {
		return this.tabParser;
	}
	
	public JSONParser getJSONParser() {
		return this.jsonParser;
	}
}
