package com.cjnetwork.webtool.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;
import com.mysql.jdbc.UpdatableResultSet;

public class ServiceGenerator implements Generator {
	StringBuilder serviceSb ;
	
	@Override
	public boolean generate() {
		boolean result = false;
		try {
			for (int i = 0; i < Globel.tableName.size(); i++) {
				generate(Globel.tableName.get(i));
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void generate(String tableName) throws Exception {
		serviceSb= new StringBuilder();

		serviceSb.append("package " + PropertyUtil.getProperty("packagePrefix") + ".service;\n");
		serviceSb.append("\n");
		serviceSb.append("import java.util.List;\n");
		serviceSb.append("\n");
		serviceSb.append("import org.springframework.stereotype.Component;\n");
		serviceSb.append("import javax.annotation.Resource;\n");
		serviceSb.append("import java.util.Date;\n");
		serviceSb.append("\n");
		serviceSb.append("import " + PropertyUtil.getProperty("packagePrefix") + ".model." + NamingUtil.getClassName(tableName) + ";\n");
		serviceSb.append("import " + PropertyUtil.getProperty("packagePrefix") + ".dao." + NamingUtil.getClassName(tableName) + "Dao;\n");
		serviceSb.append("\n");
		serviceSb.append("\n");
		serviceSb.append("@Component\n");
		serviceSb.append("public class " + NamingUtil.getClassName(tableName) + "Service {\n");
		serviceSb.append("\n");
		serviceSb.append("		private " + NamingUtil.getClassName(tableName) + "Dao " + NamingUtil.getInstanceName(tableName) + "Dao;\n");

		serviceSb.append("\n");
		serviceSb.append("\n");
		serviceSb.append("		public "+ NamingUtil.getClassName(tableName) +" add(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		
		
		 List<Entry<String, String>> colus=Globel.tables.get(tableName);
		 
		if(colus!=null && colus.size()>0){
			for (Entry<String, String> entry : colus) {
				if( entry.getKey().toLowerCase().equals("createtime")){
					serviceSb.append("		" +NamingUtil.getInstanceName(tableName) + "."+Globel.createtimeKey.get(tableName)+"(new Date());\n");
				}
			}
			//添加时间
		}
		
	
		
		serviceSb.append("			return " + NamingUtil.getInstanceName(tableName) + "Dao.add(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public void delete(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		serviceSb.append("			" + NamingUtil.getInstanceName(tableName) + "Dao.delete(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public void update(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") throws Exception {\n");
		
		 
			if(colus!=null && colus.size()>0){
				for (Entry<String, String> entry : colus) {
					if( entry.getKey().toLowerCase().equals("updatetime")){
						//添加时间和更新时间
						
						serviceSb.append("		   " +NamingUtil.getInstanceName(tableName) + "."+Globel.updatetimeKey.get(tableName)+"(new Date());\n");
					}
				}
				//添加时间
			}
		
		
			
		
		serviceSb.append("			" + NamingUtil.getInstanceName(tableName) + "Dao.update(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public " + NamingUtil.getClassName(tableName) + " findById(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		serviceSb.append("			return " + NamingUtil.getInstanceName(tableName) + "Dao.findById(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public List<" + NamingUtil.getClassName(tableName) + "> listAll() {\n");
		serviceSb.append("			return " + NamingUtil.getInstanceName(tableName) + "Dao.listAll();\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public " + NamingUtil.getClassName(tableName) + "Dao get" + NamingUtil.getClassName(tableName) + "Dao(){\n");
		serviceSb.append("			return this." + NamingUtil.getInstanceName(tableName) + "Dao;\n");
		serviceSb.append("		}\n");
		
		serviceSb.append("\n");
		serviceSb.append("		@Resource\n");
		serviceSb.append("		public void set" + NamingUtil.getClassName(tableName) + "Dao(" + NamingUtil.getClassName(tableName) + "Dao " + NamingUtil.getInstanceName(tableName) + "Dao){\n");
		serviceSb.append("			this." + NamingUtil.getInstanceName(tableName) + "Dao = " + NamingUtil.getInstanceName(tableName) + "Dao;\n");
		serviceSb.append("		}\n");

		//批量更新
		AddBitchUpdate(tableName);
		
//		implSb.append("		public void updateAll(final List<"	+ NamingUtil.getClassName(tableName)+ "> list) throws Exception {\n");
		//添加查找的方法
		
		AddQueryByPara(tableName);
		
		serviceSb.append("}\n");
		
		String fileName = PropertyUtil.getProperty("serviceFileFolder") + File.separator + NamingUtil.getClassName(tableName) + "Service.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(serviceSb.toString().getBytes());
		bos.close();
	}

	public void AddQueryByPara(String tableName){
		
		serviceSb.append("		@SuppressWarnings(\"unchecked\")\n");
		serviceSb.append("		public List<" + NamingUtil.getClassName(tableName) + "> sqlQuery(String sql) throws Exception {\n");
		serviceSb.append("			return (List<"+ NamingUtil.getClassName(tableName)+">) this." + NamingUtil.getInstanceName(tableName) + "Dao.sqlQuery(sql);\n");
		serviceSb.append("		}\n");
	}
	public void AddBitchUpdate(String tableName){
		serviceSb.append("		public void updateAll(final List<"	+ NamingUtil.getClassName(tableName)+ "> list) throws Exception {\n");
		serviceSb.append("		 this." + NamingUtil.getInstanceName(tableName) + "Dao.updateAll(list);\n");
		serviceSb.append("		}\n");
	}
}
