package com.luoyan.hdfsclienttest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: alex-fang
 * Date: 14-4-25
 * Time: 上午11:31
 *
 * @desc 读取resources下的配置文件
 */
public class PropertiesHelper {
    private static final PropertiesHelper propertiesHelper = new PropertiesHelper();

    private PropertiesHelper() {
    }

    public static synchronized Properties getProperties() {
        return propertiesHelper.getPropertiesFromApplication();
    }

    private Properties getPropertiesFromApplication() {
        Properties prop = new Properties();
        InputStream contentStream = null;
        try {
            contentStream = getClass().getResourceAsStream(File.separator + "application.properties");
            prop.load(contentStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != contentStream) {
                try {
                    contentStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
}
