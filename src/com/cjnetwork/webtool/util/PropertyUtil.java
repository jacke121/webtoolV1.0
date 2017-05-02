package com.cjnetwork.webtool.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class PropertyUtil {

	private static String propertiesFileName = "webtool.properties";
	
	private static Properties extraProperties = new Properties();

	private static Properties properties = new Properties();
	static{
		try {
//			System.out.println(System.getProperty("user.dir"));
//			System.out.println(new File(".").getAbsolutePath());
		String configPath=Class.class.getClass().getResource("/").getPath();//这个加了个bin目录
			configPath = java.net.URLDecoder.decode(configPath,"utf-8");
			System.out.println(configPath);
			properties.load(new FileInputStream(configPath+"conf" + File.separator + propertiesFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 获取属性
	 * 
	  * YanHuan 2011-1-2上午10:30:43
	 * @param key
	 * @return	失败返回null
	 */
	public static String getProperty(String key){
		String result = null;
		if(result == null){
			result = properties.getProperty(key);
		}
		if(result == null){
			result = extraProperties.getProperty(key);
		}
		return result;
	}
	
	public static boolean addProperty(String key, String value){
		boolean result = false;
		if(extraProperties.get(key) == null){
			extraProperties.setProperty(key, value);
			result = true;
		}
		return result;
	}

	/**
	 * 利用配置文件中的数据替换
	 * YanHuan 2011-1-2下午09:37:07
	 */
	public static String replaceWithProperties(String content){
		String result = content;
		Iterator<Entry<Object, Object>> iter = properties.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Object, Object> entry = iter.next();
			result = result.replace("@@" + entry.getKey().toString(), entry.getValue().toString());
		}
		return result;
	}
	
}
