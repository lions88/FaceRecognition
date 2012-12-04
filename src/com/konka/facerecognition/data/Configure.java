package com.konka.facerecognition.data;

import java.util.Properties;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 常量存于此配置类中
 */
public class Configure {
	public static final String API_KEY = "api_key";
	public static final String API_SECRET = "api_secret";
	
	private static Properties sProps;
	
	static{
		sProps = new Properties();
		sProps.setProperty(API_KEY, "6022c8b994c2b56861f94a39919e0f03");
		sProps.setProperty(API_SECRET, "nP2VdyB03W4aaYuDnX8dydaOSTl47ZTL");
	}
	
	public static String getValue(String key){
		return sProps.getProperty(key);
	}
	
	public static void updateProperties(String key,String value) {
		sProps.setProperty(key, value);
	}
}
