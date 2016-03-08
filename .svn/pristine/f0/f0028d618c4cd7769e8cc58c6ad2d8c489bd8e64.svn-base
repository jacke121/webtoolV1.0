package com.cjnetwork.webtool.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

public class JspGenerator implements Generator {

	@Override
	public boolean generate() {
		boolean result = false;
		try {
			Iterator<Entry<String, List<Entry<String, String>>>> iter = Globel.tables.entrySet().iterator();
			
			while(iter.hasNext()){
				Entry<String, List<Entry<String, String>>> entry = iter.next();
				
				generateListAllJsp(entry.getKey(), entry.getValue());

				generateViewJsp(entry.getKey(), entry.getValue());

				generateViewForEditJsp(entry.getKey(), entry.getValue());
				
				generataAddJsp(entry.getKey(), entry.getValue());
			}
			
			generateIndexJsp();
			
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成首页
	 * YanHuan 2011-1-3上午01:34:25
	 */
	private void generateIndexJsp() throws Exception{
		File resource = new File("resource/index.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		String content = sbOri.toString();
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < Globel.tableName.size(); i++){
			String tableName = Globel.tableName.get(i);
			
			sb.append("	<tr>\n");
			sb.append("		<td>" + "<a href=\"" + NamingUtil.getInstanceName(tableName) + "_listAll.action" + "\" target=\"_blank\">" + NamingUtil.getClassName(tableName) + "</a>" + "</td>\n");
			sb.append("		<td>" + "<a href=\"pages/" + NamingUtil.getInstanceName(tableName) + "/" + NamingUtil.getInstanceName(tableName) + "_add.jsp\" target=\"_blank\">add</a>" + "</td>\n");
			sb.append("	</tr>\n");
		}
		sb.append("\n");
		
		content = content.replace("@@item", sb.toString());
		
		String fileName = PropertyUtil.getProperty("webRootFolder") + File.separator + "index.jsp";
		File file = new File(fileName);
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generataAddJsp(String tableName, List<Entry<String, String>> colums) throws Exception{
		File resource = new File("resource/add.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		String content = sbOri.toString().replace("@@title", NamingUtil.getInstanceName(tableName));
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < colums.size(); i++){
			if(!colums.get(i).getKey().toLowerCase().equals("id")){
				sb.append("	<tr>\n");
				sb.append("		<td>" + NamingUtil.getClassName(colums.get(i).getKey()) + "</td>\n");
				sb.append("		<td><input type =\"text\" name=\"" + NamingUtil.getInstanceName(colums.get(i).getKey()) + "\"/></td>\n");
				sb.append("	</tr>\n");
			}
		}
		sb.append("\n");
		content = content.replace("@@item", sb.toString());
		content = content.replace("@@action_uri", NamingUtil.getInstanceName(tableName) + "_add.action");
		
		String fileName = PropertyUtil.getProperty("pagesFolder") + File.separator + NamingUtil.getInstanceName(tableName) + File.separator + NamingUtil.getInstanceName(tableName) + "_add.jsp";
		File file = new File(fileName);
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generateViewForEditJsp(String tableName, List<Entry<String, String>> colums) throws Exception {
		File resource = new File("resource/viewForEdit.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		String content = sbOri.toString().replace("@@title", NamingUtil.getInstanceName(tableName));
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < colums.size(); i++){
			sb.append("	<tr>\n");
			sb.append("		<td>" + NamingUtil.getClassName(colums.get(i).getKey()) + "</td>\n");
			sb.append("		<td><input type =\"text\" name=\"" + NamingUtil.getInstanceName(colums.get(i).getKey()) + "\" value=<s:property value=\"#request." + NamingUtil.getInstanceName(tableName) + "." + colums.get(i).getKey()+ "\"/>" + "></td>\n");
			sb.append("	</tr>\n");
		}
		sb.append("\n");
		content = content.replace("@@item", sb.toString());
		content = content.replace("@@action_uri", NamingUtil.getInstanceName(tableName) + "_update.action");
		
		String fileName = PropertyUtil.getProperty("pagesFolder") + File.separator + NamingUtil.getInstanceName(tableName) + File.separator + NamingUtil.getInstanceName(tableName) + "_viewForEdit.jsp";
		File file = new File(fileName);
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generateViewJsp(String tableName, List<Entry<String, String>> colums) throws Exception {
		File resource = new File("resource/view.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		String content = sbOri.toString().replace("@@title", NamingUtil.getInstanceName(tableName));
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < colums.size(); i++){
			sb.append("	<tr>\n");
			sb.append("		<td>" + NamingUtil.getClassName(colums.get(i).getKey()) + "</td>\n");
			sb.append("		<td>" + "<s:property value=\"#request." + NamingUtil.getInstanceName(tableName) + "." + NamingUtil.getInstanceName(colums.get(i).getKey()) + "\"/></td>\n");
			sb.append("	</tr>\n");
		}
		sb.append("\n");
		content = content.replace("@@item", sb.toString());
		
		String fileName = PropertyUtil.getProperty("pagesFolder") + File.separator + NamingUtil.getInstanceName(tableName) + File.separator + NamingUtil.getInstanceName(tableName) + "_view.jsp";
		File file = new File(fileName);
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generateListAllJsp(String tableName, List<Entry<String, String>> colums) throws Exception {
		File resource = new File("resource/listAll.jsp");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		String content = sbOri.toString();
		content = content.replace("@@title", NamingUtil.getInstanceName(tableName));
		content = content.replace("@@allItem", "all" + NamingUtil.getClassName(tableName));
		content = content.replace("@@item", NamingUtil.getInstanceName(tableName));
		content = content.replace("@@item", NamingUtil.getInstanceName(tableName));
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < colums.size(); i++){
			sb.append("		<th>" + NamingUtil.getClassName(colums.get(i).getKey()) + "</th>\n");
		}
		sb.append("\n");
		content = content.replace("@@thead", sb.toString());
		
		sb = new StringBuilder();
		sb.append("\n");
		for(int i = 0; i < colums.size(); i++){
			sb.append("		<td>" + "<s:property value=\"#" + NamingUtil.getInstanceName(tableName) + "." + NamingUtil.getInstanceName(colums.get(i).getKey()) + "\"/>" + "</td>\n");
		}
		sb.append("		<td>" + "<a href=\"" + NamingUtil.getInstanceName(tableName) + "_view.action?id=<s:property value=\"#" + NamingUtil.getInstanceName(tableName) + ".id\"/>\"" + ">view</a>" + "</td>\n");
		sb.append("		<td>" + "<a href=\"" + NamingUtil.getInstanceName(tableName) + "_viewForEdit.action?id=<s:property value=\"#" + NamingUtil.getInstanceName(tableName) + ".id\"/>\"" + ">edit</a>" + "</td>\n");
		sb.append("		<td>" + "<a href=\"" + NamingUtil.getInstanceName(tableName) + "_delete.action?id=<s:property value=\"#" + NamingUtil.getInstanceName(tableName) + ".id\"/>\"" + ">delete</a>" + "</td>\n");
		sb.append("\n");
		content = content.replace("@@tdItem", sb.toString());
		
		String fileName = PropertyUtil.getProperty("pagesFolder") + File.separator + NamingUtil.getInstanceName(tableName) + File.separator + NamingUtil.getInstanceName(tableName) + "_listAll.jsp";
		File file = new File(fileName);
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

}

