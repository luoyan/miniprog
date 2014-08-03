package com.luoyan.syntax;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;

public class Utils {
    public static List<String> getKeywordListFromBase64Content(String base64Content) {
        List<String> ret = new ArrayList<String>();
        if (StringUtils.isNotEmpty(base64Content)) {
            String[] keywordArray = base64Content.split(";");
            for (String keywordBase64 : keywordArray) {
                String keywordDecoded = new String(Base64.decodeBase64(keywordBase64));
                ret.add(keywordDecoded);
            }
        }
        return ret;
    }
	
	public static boolean setEnvironment(String environment) {
		if (environment.equals("onbox")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.ONEBOX);
		}
		else if (environment.equals("staging")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.STAGING);
		}
		else if (environment.equals("shangdi")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.SHANGDI);
		}
		else if (environment.equals("lugu")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.LUGU);
		}
		else if (environment.equals("haihang")) {
		    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.HAIHANG);
		}
		else {
			return false;
		}
		return true;
	}
}
