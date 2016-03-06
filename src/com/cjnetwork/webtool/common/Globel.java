package com.cjnetwork.webtool.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Globel {

	public static List<String> tableName = new ArrayList<String>();
	public static LinkedHashMap<String, List<Entry<String, String>>> tables = new LinkedHashMap<String, List<Entry<String, String>>>();
	public static Map<String, String> fileCopyPool = new HashMap<String, String>();//<sourceFileLocation, targetFileLocation>
	
	public static HashMap<String,String> tableKey=new HashMap<String, String>();
	
	public static HashMap<String,String> createtimeKey=new HashMap<String, String>();
	public static HashMap<String,String> updatetimeKey=new HashMap<String, String>();
	
}
