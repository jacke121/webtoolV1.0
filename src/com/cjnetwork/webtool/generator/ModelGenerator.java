package com.cjnetwork.webtool.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
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

		sb.append("package " + PropertyUtil.getProperty("packagePrefix") + ".model;\n");
		sb.append("import javax.persistence.Entity;\n");
		sb.append("import javax.persistence.GeneratedValue;\n");
		sb.append("import javax.persistence.Id;\n");
		sb.append("import javax.persistence.Column;\n");

		sb.append("@@haveDate\n");

		sb.append("\n");

		sb.append("@Entity\n");
		sb.append("public class " + NamingUtil.getClassName(tableName) + " {\n");
		sb.append("\n");

		boolean haveDate = false;
		// generate field
		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);
			sb.append("		private " + entry.getValue() + " " + NamingUtil.getInstanceName(entry.getKey()) + ";\n");

			if (!haveDate) {
				if (entry.getValue().equals("Date")) {
					haveDate = true;
				}
			}
		}
		sb.append("\n");
		sb.append("\n");

		// generate get/set
		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);
			if (Globel.tableKey.get(tableName).equalsIgnoreCase(NamingUtil.getClassName(entry.getKey()))) {
				sb.append("		@Id\n");
				sb.append("		@GeneratedValue\n");
				sb.append("		@Column(name =\"" + NamingUtil.getInstanceName(entry.getKey()) + "\", nullable = false)\n");
				Globel.tableKey.put(tableName, " get" + NamingUtil.getClassName(entry.getKey()));
			}
			// if(NamingUtil.getInstanceName(entry.getKey()).equals("id")){
			// sb.append("		@Id\n");
			// sb.append("		@GeneratedValue\n");
			// }
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
			sb.append("		}\n");
			sb.append("\n");
		}

		sb.append("}\n");

		String fileName = PropertyUtil.getProperty("modelFileFolder") + File.separator
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
	Connection conn;

	@SuppressWarnings("unchecked")
	private void initTableAndColums() throws Exception {
		String dburl = PropertyUtil.getProperty("database_url");
		String dbuser = PropertyUtil.getProperty("database_user");
		String dbpassword = PropertyUtil.getProperty("database_password");
		Class.forName(PropertyUtil.getProperty("database_driver"));

		Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
		DatabaseMetaData metaDb = conn.getMetaData();
		ResultSet rsTableName = metaDb.getTables(null, null, null, new String[] { "table" });
		
		StringBuilder databaseSql=new StringBuilder();
		
		while (rsTableName.next()) {
			String tmpTableName = rsTableName.getString("table_name");
			Globel.tableName.add(tmpTableName);
			// 获取主键
			String keyName = null;
			ResultSet rs = metaDb.getPrimaryKeys(null, null, tmpTableName);
			while (rs.next()){
				keyName = rs.getString(4);
			}
			rs.close();
			// String keyColumn= getIdName(con,tmpTableName);
			Globel.tableKey.put(tmpTableName, keyName);

			Statement st = conn.createStatement();
			ResultSet rsColumsName = st.executeQuery("show create table " + tmpTableName + ";");

			rsColumsName.next();

			String returnedTableName = rsColumsName.getString("Create Table");

			databaseSql.append(returnedTableName+";\n");
			if(!returnedTableName.toLowerCase().contains("createtime")){
			returnedTableName =  returnedTableName + ";";
		
			//------
			st.executeUpdate("DROP TABLE IF EXISTS " + tmpTableName  );
			returnedTableName=returnedTableName.replace("PRIMARY KEY", "createtime TIMESTAMP(14),\n updatetime TIMESTAMP(14) ON UPDATE CURRENT_TIMESTAMP,\n LifeStatus INTEGER NOT NULL,\n upgradeFlag BIGINT NOT NULL, \n PRIMARY KEY");
			st.executeUpdate(returnedTableName);
			}
			
			//查询列
			String sql = "select * from " + tmpTableName;
			ResultSet rsColumName = st.executeQuery(sql);

			ResultSetMetaData metaRs = rsColumName.getMetaData();

			// 某列类型的精确度(类型的长度)
			// int precision = metaRs.getPrecision(i);
			// // 小数点后的位数
			// int scale = metaRs.getScale(i);
			// // 是否自动递增
			// boolean isAutoInctement = metaRs.isAutoIncrement(i);

			List<Entry<String, String>> colums = new ArrayList<Entry<String, String>>();
			for (int i = 0; i < metaRs.getColumnCount(); i++) {
				String columName = metaRs.getColumnName(i + 1);
				String columType = "";
				switch (metaRs.getColumnType(i + 1)) {
				case Types.CHAR:
					columType = "String";
					break;
				case Types.BIGINT:
					columType = "Integer";
					break;
				case Types.DATE:
					columType = "Date";
					break;
				case Types.DECIMAL:
					columType = "BigDecimal";
					break;
				case Types.INTEGER:
					columType = "Integer";
					break;
				case Types.NCHAR:
					columType = "String";
					break;
				case Types.NUMERIC:
					columType = "BigDecimal";
					break;
				case Types.NVARCHAR:
					columType = "String";
					break;

				case Types.SMALLINT:
					columType = "Integer";
					break;
				case Types.TIME:
					columType = "Time";
					break;
				case Types.TINYINT:
					columType = "Integer";
					break;
				case Types.TIMESTAMP:
					columType = "Date";
					break;
				case Types.VARCHAR:
					columType = "String";
					break;
				case Types.LONGVARCHAR:
					columType = "String";
					break;
				case Types.DOUBLE:
					columType = "Double";
					break;

				default:
					throw new Exception("数据类型不支持，orm映射异常" + metaRs.getColumnType(i + 1));
				}
				Entry<String, String> entry = new SimpleEntry(columName, columType);
				colums.add(entry);
			}
			Globel.tables.put(tmpTableName, colums);
		}
		
		//备份sql文件：
		String fileName = PropertyUtil.getProperty("configFileFolder")
				+ File.separator + "database.sql";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bos.write(databaseSql.toString().getBytes());
		bos.close();
	}

	/**
	 * 使用类来创建表
	 * 
	 * @param clazz
	 * @param delIfExist
	 *           如果表已存在，true表示删除旧表并重新建表，false表示保留旧表不再重新建
	 * @throws SQLException
	 */
	public void createTable(Class<?> clazz, boolean delIfExist) throws SQLException {
		// 如果表已经存在，则看看是否需要删除
		boolean existTable = existTable(clazz);
		if (!delIfExist && existTable) {
			return;
		}
		if (delIfExist && existTable) {
			deleteTable(clazz);
		}
		// 通过反射提取属性和属性值
		Method[] methods = clazz.getMethods();
		Map<String, String> kvMap = new HashMap<String, String>();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				String property = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
				String propertyClassName = method.getParameterTypes()[0].getName();
				kvMap.put(property, propertyClassName);
			}
		}
		// 生成sql
		String tableName = toDbCase(clazz.getName());
		StringBuffer sb = new StringBuffer("CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY,");
		for (String key : kvMap.keySet()) {
			if (key.equalsIgnoreCase("id")) {
				continue;
			}
			String columnName = toDbCase(key);
			String propertyClassName = kvMap.get(key);
			String dbTypeName = getDbTypeName(propertyClassName);
			sb.append(columnName + " " + dbTypeName + ",");
		}
		if (sb.charAt(sb.length() - 1) == ',')
			sb.delete(sb.length() - 1, sb.length());
		sb.append(");");
		String sql = sb.toString();
		// 执行sql
		PreparedStatement ps = conn.prepareStatement(sql);
		conn.setAutoCommit(true);
		ps.execute();
		conn.setAutoCommit(false);
		free(null, ps, conn);
	}

	/**
	 * 删除表
	 * 
	 * @param clazz
	 * @throws SQLException
	 */
	public void deleteTable(Class<?> clazz) throws SQLException {
		String tableName = toDbCase(clazz.getSimpleName());
		String sql = "DROP TABLE IF EXISTS " + tableName;
		// execute(sql);
	}

	/**
	 * 释放连接
	 * 
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public void free(ResultSet rs, PreparedStatement ps, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			// log.error(this, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				// log.error(this, e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// log.error(this, e);
					}
				}
			}
		}
	}

	private String getDbTypeName(String propertyClassName) {
		if ("java.lang.Integer".equals(propertyClassName) || "java.lang.Long".equals(propertyClassName)
				|| "java.lang.Character".equals(propertyClassName) || "java.lang.Short".equals(propertyClassName))
			return "INTEGER";
		if ("java.lang.Float".equals(propertyClassName) || "java.lang.Double".equals(propertyClassName))
			return "REAL";
		if ("java.util.Date".equals(propertyClassName))
			return "DATETIME";
		if ("java.lang.Boolean".equals(propertyClassName))
			return "TINYINT";
		if ("java.lang.String".equals(propertyClassName))
			return "VARCHAR";
		return "";
	}

	/**
	 * 检查数据库是否有这个表
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	public boolean existTable(Class<?> clazz) throws SQLException {
		String tableName = toDbCase(clazz.getSimpleName());
		String sql = "SELECT COUNT(*) AS table_count FROM sqlite_master WHERE TYPE='table' AND NAME=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setObject(1, tableName);
		ResultSet rs = ps.executeQuery();
		int num = 0;
		if (rs.next()) {
			num = rs.getInt("table_count");
		}
		free(null, ps, conn);
		return num > 0 ? true : false;
	}

	/**
	 * 使用sql来建表
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	/**
	 * Java命名方式转换成数据库的命名方式
	 * 
	 * @param s
	 * @return
	 */
	private static String toDbCase(String s) {
		if (s == null || s.trim().length() == 0)
			return s;
		char[] chars = s.toCharArray();
		boolean firstTime = true;
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			if (c >= 'A' && c <= 'Z') {
				char c1 = (char) (c + 32);
				sb.append(firstTime ? c1 : "_" + c1);
			} else
				sb.append(c);
			firstTime = false;
		}
		return sb.toString();
	}

	public void createTable(String sql) throws SQLException {
		// 创建表
		PreparedStatement ps = conn.prepareStatement(sql);
		conn.setAutoCommit(true);
		ps.execute();
		conn.setAutoCommit(false);
		free(null, ps, conn);
	}
	// private String getIdName(Connection conn, String tableName) {
	// String idName = "";
	// DatabaseMetaData metaData = null;
	// try {
	// metaData = conn.getMetaData();
	// ResultSet rs = metaData.getColumns(conn.getCatalog(), "%",
	// tableName, "%ID");
	// if (rs.next()) {
	// idName = rs.getString("COLUMN_NAME");
	// }
	// } catch (Exception e) {
	// // logger.error(e);
	// }
	// return idName;
	// }

}
