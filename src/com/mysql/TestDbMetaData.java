package com.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class TestDbMetaData {


	public static void main(String[] args) throws Exception {
		String dburl = "jdbc:mysql://127.0.0.1:3306/salesmanagement";
		String dbuser = "root";
		String dbpwd = "sys123";
		Class.forName("com.mysql.jdbc.Driver");
		List<String> tableName = new ArrayList<String>();
		Map<String, List<Entry<String, String>>> tables = new HashMap<String, List<Entry<String, String>>>();

		Connection con = DriverManager.getConnection(dburl, dbuser, dbpwd);
		DatabaseMetaData metaDb = con.getMetaData();
		
		 
		ResultSet rsTableName = metaDb.getTables(null, null, null, new String[] { "table" });
		while (rsTableName.next()) {
			String tmpTableName = rsTableName.getString("table_name");
			tableName.add(tmpTableName);
			String sql = "select * from " + tmpTableName;
			Statement st = con.createStatement();
			ResultSet rsColumName = st.executeQuery(sql);

			ResultSetMetaData metaRs = rsColumName.getMetaData();
			List<Entry<String, String>> colums = new ArrayList<Entry<String, String>>();
			for (int i = 1; i <= metaRs.getColumnCount(); i++) {
				System.out.print( metaRs.getColumnName(i)+","+metaRs.getColumnClassName(i).substring(metaRs.getColumnClassName(i).lastIndexOf(".") + 1));
				System.out.println(",是否为空："+metaRs.isNullable(i));//0 不能为空   1可以为空
    //System.out.println(metaRs.getColumnLabel(i));
    //System.out.println(metaRs.getColumnDisplaySize(i));
				String columName = metaRs.getColumnName(i);
				 // 某列类型的精确度(类型的长度)
                int precision = metaRs.getPrecision(i);
                // 小数点后的位数 
                int scale = metaRs.getScale(i);
                // 是否自动递增 
                boolean isAutoInctement = metaRs.isAutoIncrement(i);
			String aa=	metaRs.getColumnTypeName(i);
				String columType = "";
				switch (metaRs.getColumnType(i )) {
				case Types.CHAR:
					columType = "String";
					break;
				case Types.BIGINT:
					columType = "int";
					break;
				case Types.DATE:
					columType = "Date";
					break;
				case Types.DECIMAL:
					columType = "int";
					break;
				case Types.INTEGER:
					columType = "int";
					break;
				case Types.NCHAR:
					columType = "String";
					break;
				case Types.NUMERIC:
					columType = "int";
					break;
				case Types.NVARCHAR:
					columType = "String";
					break;
				case Types.SMALLINT:
					columType = "int";
					break;
				case Types.TIME:
					columType = "Date";
					break;
				case Types.TINYINT:
					columType = "int";
					break;
				case Types.TIMESTAMP:
					columType = "Date";
					break;
				case Types.VARCHAR:
					columType = "String";
					break;
				default:
					throw new Exception("数据类型不支持，orm映射异常");
				}
				Entry<String, String> entry = new SimpleEntry(columName, columType);
				colums.add(entry);
			}
			tables.put(tmpTableName, colums);
		}

		Iterator<Entry<String, List<Entry<String, String>>>> iter = tables.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, List<Entry<String, String>>> entry = iter.next();
			System.out.println("表名：" + entry.getKey());
			for (int i = 0; i < entry.getValue().size(); i++) {
				System.out.print(entry.getValue().get(i).getKey() + " " + entry.getValue().get(i).getValue());
			}
			System.out.println();
		}
		System.out.println("complete...");
	}
}
