package com.luoyan.syntax;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

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
}
