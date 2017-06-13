package com.sqlite;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

public class ModelGenerator implements Generator {

	@Override
	public boolean generate() {
		boolean result = false;
		try {
			initTableAndColums();
			Iterator<Entry<String, List<Entry<String, String>>>> iter = Globel.tables.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, List<Entry<String, String>>> entry = iter.next();
				generate(entry.getKey(), entry.getValue());
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成orm映射文件 YanHuan 2011-1-2下午12:22:32
	 */
	private void generate(String tableName, List<Entry<String, String>> colums) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + PropertyUtil.getProperty("sqlitepackage") + ".basedao;\n");

		sb.append("@@haveDate\n\n");


		sb.append("public class " + NamingUtil.getClassName(tableName) + " {\n\n");

		boolean haveDate = false;
		// generate field
		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);
			sb.append("		public " + entry.getValue() + " " + NamingUtil.getInstanceName(entry.getKey()) + ";\n");

			if (!haveDate) {
				if (entry.getValue().equals("Date")) {
					haveDate = true;
				}
			}
		}
		sb.append("\n\n");

		// generate get/set
		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);

			sb.append("		public " + entry.getValue() + " get" + NamingUtil.getClassName(entry.getKey()) + "() {\n");
			sb.append("			return this." + NamingUtil.getInstanceName(entry.getKey()) + ";\n");
			sb.append("		}\n");
			sb.append("\n");
			sb.append("		public void set" + NamingUtil.getClassName(entry.getKey()) + "(" + entry.getValue() + " "
					+ NamingUtil.getInstanceName(entry.getKey()) + ") {\n");

			if (NamingUtil.getClassName(entry.getKey()).toLowerCase().indexOf("createtime") >= 0) {
				Globel.createtimeKey.put(tableName, "set" + NamingUtil.getClassName(entry.getKey()));
			} else if (NamingUtil.getClassName(entry.getKey()).toLowerCase().indexOf("updatetime") >= 0) {
				Globel.updatetimeKey.put(tableName, "set" + NamingUtil.getClassName(entry.getKey()));
			}

			sb.append("			this." + NamingUtil.getInstanceName(entry.getKey()) + " = "
					+ NamingUtil.getInstanceName(entry.getKey()) + ";\n");
			sb.append("		}\n\n");
		}

		sb.append("}\n");

		String fileName = PropertyUtil.getProperty("daoFileFolder") + File.separator
				+ NamingUtil.getClassName(tableName) + ".java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

		if (haveDate) {
			bos.write(sb.toString().replace("@@haveDate", "import java.util.Date;").getBytes());
		} else {
			bos.write(sb.toString().replace("@@haveDate", "").getBytes());
		}
		bos.close();
	}

	/**
	 * 获取数据库中欧诺个表名和字段名 YanHuan 2011-1-2下午12:19:12
	 */
	@SuppressWarnings("unchecked")
	private void initTableAndColums() throws Exception {

		Statement stat = Launch.con.createStatement();
		Launch.con.setAutoCommit(true);  
		ResultSet tablers = stat.executeQuery("SELECT * FROM sqlite_master where type='table'");
		List<String> tableNames = new ArrayList<String>();
		List<String> sqls= new ArrayList<String>();
		List<String> sqlstable= new ArrayList<String>();
		// ResultSet rs = stat.executeQuery("select * from people;");
		while (tablers.next()) {
			String tmpTableName = tablers.getString("name");
			if(tmpTableName.equals("sqlite_sequence")){
				continue;
			}
			tableNames.add(tmpTableName);
			

			String sql = tablers.getString("sql");
			if(!sql.toLowerCase().contains("createtime")){
				sqls.add(sql+";");
				sqlstable.add(tmpTableName);
			}
		}
		if(sqls.size()>0){
			for (int i = 0; i < sqls.size(); i++) {
				String sql=sqls.get(i);
				
				 sql =sql.replace(");", ", [createtime] TimeStamp NOT NULL DEFAULT (datetime('now','localtime')), [updatetime] TimeStamp NOT NULL, [LifeStatus] INTEGER NOT NULL  DEFAULT 1, [upgradeFlag] BIGINT NOT NULL  DEFAULT 0);");
				 
				int value= stat.executeUpdate("drop table if exists "+sqlstable.get(i));
				if(value==-1){
					
				}
				 stat.executeUpdate(sql);
			}
		}
		
		Launch.con.setAutoCommit(false);  
//		ResultSet rsTableName = stat.executeQuery("select name from sqlite_master where type='table' order by name;"); // and
																																							// name='people'

		int length = tableNames.size();
		for (int i = 0; i < length; i++) {
			String tmpTableName = tableNames.get(i);
			ResultSet rs = stat.executeQuery("PRAGMA table_info(" + tmpTableName + ")"); // and
																													// name='people'
			List<Entry<String, String>> colums = new ArrayList<Entry<String, String>>();
			while (rs.next()) {
				String columName = rs.getString("name");
				System.out.print(" type = " + rs.getString("type"));
				System.out.print(" notnull = " + rs.getInt("notnull"));
				if (rs.getInt("pk") == 1) {
					Globel.tableKey.put(tmpTableName, columName);
				}
				System.out.print(" cid = " + rs.getInt("cid"));
				System.out.println("");
				String columType = "";
				if (rs.getString("type").toLowerCase().equals("integer")) {
					columType = "Integer";
				} else if (rs.getString("type").toLowerCase().equals("int")) {
					columType = "Integer";
				} else if (rs.getString("type").toLowerCase().equals("string")) {
					columType = "String";
				} else if (rs.getString("type").toUpperCase().equals("DATETIME")
						|| rs.getString("type").toUpperCase().equals("DATE")
						|| rs.getString("type").toUpperCase().equals("TIME")
						|| rs.getString("type").toUpperCase().equals("TIMESTAMP")) {
					columType = "Date";
				} else if (rs.getString("type").toLowerCase().equals("bigint")) {
					columType = "Long";
				} else if (rs.getString("type").toLowerCase().equals("char")) {
					columType = "String";
				}

				else if (rs.getString("type").toLowerCase().equals("char")
						|| rs.getString("type").toLowerCase().equals("text")) {
					columType = "String";
				}

				else if (rs.getString("type").toLowerCase().startsWith("decimal")) {
					columType = "BigDecimal";
				}

				else if (rs.getString("type").toUpperCase().equals("NCHAR")) {
					columType = "String";
				}

				else if (rs.getString("type").toUpperCase().startsWith("NUMERIC")) {
					columType = "float";
				} else if (rs.getString("type").toUpperCase().startsWith("NVARCHAR")) {
					columType = "String";
				}else if (rs.getString("type").toUpperCase().startsWith("NVARCHAR")
						|| rs.getString("type").toUpperCase().startsWith("VARCHAR")) {
					columType = "String";
				} else if (rs.getString("type").toUpperCase().startsWith("SMALLINT")) {
					columType = "Integer";
				} else if (rs.getString("type").toUpperCase().startsWith("DOUBLE")) {
					columType = "Double";
				}
				Entry<String, String> entry = new SimpleEntry(columName, columType);
				colums.add(entry);
			}

			Globel.tables.put(tmpTableName, colums);
		}

	}

}
