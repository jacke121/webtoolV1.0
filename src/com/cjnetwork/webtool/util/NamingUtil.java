package com.cjnetwork.webtool.util;

/**
 * 命名转换工具 YanHuan 2011-1-2上午11:34:36
 */
public class NamingUtil {

	/**
	 * 获取类名 YanHuan 2011-1-2下午02:37:59
	 */
	public static String getClassName(String tableName) {
		String result = tableName;
		String field = PropertyUtil.getProperty("database_field_dict");
		String[] fields = field.split(",");
		for(int i = 0; i < fields.length; i++){
			if(fields[i].length() > 1){
				result = result.replace(fields[i], fields[i].toUpperCase().charAt(0) + fields[i].substring(1));
			}else{
				result = result.replace(fields[i], String.valueOf(fields[i].toUpperCase().charAt(0)));
			}
		}
		
		result = result.toUpperCase().charAt(0) + result.substring(1);
		return result;
	}
	
	/**
	 * 获得实例名
	 * YanHuan 2011-1-2下午02:38:38
	 */
	public static String getInstanceName(String columnName) {
		String result = "";
		result = columnName.toLowerCase().charAt(0) + getClassName(columnName).substring(1);
		return result;
	}

}
