package com.sqlite;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

public class DaoGenerator implements Generator {
	StringBuilder implSb;
	// private
	@Override
	public boolean generate() {
		boolean result = false;
		try {
			generateBaseDao();
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
	private void generateBaseDao() {
		String res = "resource/SqlHelper.java";
		String tar = PropertyUtil.getProperty("daoFileFolder") + File.separator + "SqlHelper.java";
		Globel.fileCopyPool.put(res, tar);
	}
	/**
	 * 生成dao接口 YanHuan 2011-1-2下午05:01:26
	 */
	private void generate(String tableName, List<Entry<String, String>> colums) throws Exception {
		implSb = new StringBuilder();

		implSb.append("package " + PropertyUtil.getProperty("sqlitepackage") + ".basedao;\n");
		implSb.append("\n");
//		implSb.append("import " + PropertyUtil.getProperty("sqlitepackage")+"." +NamingUtil.getClassName(tableName)+ ";\n");
		implSb.append("import java.util.Date;\n");
		implSb.append("import android.text.TextUtils;\n");
		implSb.append("import android.database.Cursor;\n");
		implSb.append("import android.content.ContentValues;\n");
		implSb.append("import android.database.sqlite.SQLiteDatabase;\n");
		implSb.append("import android.database.sqlite.SQLiteOpenHelper;\n");
		implSb.append("import java.text.SimpleDateFormat;\n");
		implSb.append("import android.util.SparseArray;\n");

		implSb.append("\n\n");
		implSb.append("public class " + NamingUtil.getClassName(tableName) + "Dao{\n");
		implSb.append(" public static final String TABLENAME =\"" + NamingUtil.getClassName(tableName) + "\";\n");

		implSb.append(" public static final Object SYNC= new Object();\n");
		implSb.append(" private final SQLiteOpenHelper mOpenHelper;\n");
		implSb.append(" public " + NamingUtil.getClassName(tableName) + "Dao(SQLiteOpenHelper openHelper){\n");
		implSb.append("   mOpenHelper=openHelper;\n   }\n");

		// 查询
		implSb.append("   public Cursor query(String whereClause, String []whereArgs){\n");

		implSb.append("     final String sql = \"SELECT *\"\n");
		implSb.append("     + \" FROM \" + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+\" WHERE \"+whereClause);\n");

		implSb.append("     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);\n");

		implSb.append("     }\n\n\n");
		// + COLUMNS.username + ","+ COLUMNS.ID
//---
		implSb.append("     public SparseArray<"+ NamingUtil.getClassName(tableName)+"> queryBySql(String sql, String []whereArgs){\n");

		implSb.append("     Cursor cursor=null;\n");
		implSb.append("     int index=0;\n");
		implSb.append("     try{\n");
		implSb.append("       synchronized(SYNC){\n");
		implSb.append("      if ( (cursor =  mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs))==null || cursor.getCount()<1)return null;\n");
		implSb.append("     SparseArray<" + NamingUtil.getClassName(tableName) + "> list = new SparseArray<"
				+ NamingUtil.getClassName(tableName) + ">(cursor.getCount());\n");
		implSb.append("      while (cursor.moveToNext()){\n");

		implSb.append("      " + NamingUtil.getClassName(tableName) + " entity=new " + NamingUtil.getClassName(tableName)
				+ "(); \n");

		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());
			if (entry.getValue().startsWith("Int")) {

				implSb.append("    entity." + columnName + "=cursor.isNull(COLUMNINDEXS." + columnName
						+ " )? -1 :cursor.getInt(COLUMNINDEXS." + columnName + ");\n");
			} else if (entry.getValue().startsWith("String")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(COLUMNINDEXS." + columnName
						+ " )? \"\" :cursor.getString(COLUMNINDEXS." + columnName + ");\n");
			}
		}
		implSb.append("       list.append(index++,entity);\n");
		implSb.append("       }; \n");

		implSb.append("      cursor.close(); \n");
		implSb.append("      return list; \n");

		implSb.append("      } \n");
		implSb.append("      }catch(Exception ex){ \n  ex.printStackTrace();");
		implSb.append("       }finally{ \n if (cursor!= null) cursor.close();\n    }   return null;\n }\n");
		//------------
		implSb.append("     public SparseArray<"+ NamingUtil.getClassName(tableName)+"> queryToList(String whereClause, String []whereArgs){\n");

		implSb.append("     Cursor cursor=null;\n");
		implSb.append("     int index=0;\n");
		implSb.append("     try{\n");
		implSb.append("       synchronized(SYNC){\n");
		implSb.append("      if ( (cursor = query(whereClause, whereArgs) )==null || cursor.getCount()<1)return null;\n");
		implSb.append("     SparseArray<" + NamingUtil.getClassName(tableName) + "> list = new SparseArray<"
				+ NamingUtil.getClassName(tableName) + ">(cursor.getCount());\n");
		implSb.append("      while (cursor.moveToNext()){\n");

		implSb.append("      " + NamingUtil.getClassName(tableName) + " entity=new " + NamingUtil.getClassName(tableName)
				+ "(); \n");

		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());
			if (entry.getValue().startsWith("Int")) {

				implSb.append("    entity." + columnName + "=cursor.isNull(COLUMNINDEXS." + columnName
						+ " )? -1 :cursor.getInt(COLUMNINDEXS." + columnName + ");\n");
			} else if (entry.getValue().startsWith("String")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(COLUMNINDEXS." + columnName
						+ " )? \"\" :cursor.getString(COLUMNINDEXS." + columnName + ");\n");
			}
		}
		implSb.append("       list.append(index++,entity);\n");
		implSb.append("       }; \n");

		implSb.append("      cursor.close(); \n");
		implSb.append("      return list; \n");

		implSb.append("      } \n");
		implSb.append("      }catch(Exception ex){ \n  ex.printStackTrace();");
		implSb.append("       }finally{ \n if (cursor!= null) cursor.close();\n    }   return null;\n }");
		modifytable(tableName, 0);
		// 批量更新
		modifytable(tableName, 1);
		modifytable(tableName, 2);// 参数查找
		implSb.append("		public int getUpgrade( SQLiteDatabase db ){\n");
		implSb.append("	    int strid = 0;\n");
				implSb.append("	    Cursor cursor = db.rawQuery(\"select last_insert_rowid() from " + tableName+"\", null);\n");
						implSb.append("	    if (cursor.moveToFirst())\n");
								implSb.append("	        strid = cursor.getInt(0);\n");
										implSb.append("	    cursor.close();\n");
												implSb.append("	    return strid+1;\n");
														implSb.append("	}\n");
		genColumns(colums, 1);
		genColumns(colums, 2);
		// insert0
		implSb.append("       private int insert0(SQLiteDatabase db, " + NamingUtil.getClassName(tableName)
				+ " entity){ \n");

		implSb.append("       ContentValues cv=new ContentValues(); \n");

		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());

			if (columnName.toLowerCase().equals("updatetime")) {

				implSb.append("    SimpleDateFormat dfu = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
				implSb.append("    cv.put(COLUMNS.updatetime, dfu.format(new Date()));");

			} else if (columnName.toLowerCase().equals("createtime")) {

				implSb.append("    SimpleDateFormat df = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n"
						+ "    cv.put(COLUMNS.createtime, df.format(new Date()));\n");
			} else if (columnName.toLowerCase().equals(Globel.tableKey.get(tableName))) {
			} else {
				implSb.append("    cv.put(COLUMNS." + columnName + ", entity." + columnName + " );\n");
			}
		}
		
		implSb.append("          int strid=-1;  \n");
		implSb.append("        if(db.insert(TABLENAME, null, cv)>0){ \n");
		
		
		implSb.append("         Cursor cursor = db.rawQuery(\"select last_insert_rowid() from \"+TABLENAME,null); \n");
		
		implSb.append("         if(cursor.moveToFirst()) \n");
		implSb.append("          strid= cursor.getInt(0); \n ");
		implSb.append("         cursor.close();\n ");
		implSb.append("           }\n");
		implSb.append("          return strid; \n");
		implSb.append("         }\n");

		implSb.append("       private boolean update0(SQLiteDatabase db, " + NamingUtil.getClassName(tableName)
				+ " entity, String whereClause, String []whereArgs){ \n");

		implSb.append("       ContentValues cv=new ContentValues(1); \n");

		for (int i = 0; i < colums.size(); i++) {
			Entry<String, String> entry = colums.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());

			if (columnName.toLowerCase().equals("updatetime")) {

				implSb.append("    SimpleDateFormat dfu = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
				implSb.append("    cv.put(COLUMNS.updatetime, dfu.format(new Date()));\n");

			} else if (columnName.toLowerCase().equals("createtime")) {
			} else {
				implSb.append("    cv.put(COLUMNS." + columnName + ", entity." + columnName + " );\n");
			}
		}
		implSb.append("        return db.update(TABLENAME, cv, whereClause, whereArgs) >0; \n");
		implSb.append("     }\n");
		// ---
		implSb.append(" private boolean delete0(SQLiteDatabase db, String whereClause, String []whereArgs){\n");
		implSb.append("        return db.delete(TABLENAME, whereClause, whereArgs) >0; \n");
		implSb.append("     }\n");

		implSb.append(" public boolean drop(SQLiteDatabase db) {\n");
		implSb.append("        try { \n");
		implSb.append("     db.execSQL(\"drop table if exists " + NamingUtil.getClassName(tableName) + "\");\n");
		implSb.append("     return true;\n");
		implSb.append("     } catch (Exception e) {\n");
		implSb.append("     e.printStackTrace();\n");
		implSb.append("    return false;\n");
		implSb.append("     }\n");
		implSb.append("     }\n");

	
		Statement stat = Launch.con.createStatement();
		ResultSet rs = stat.executeQuery("SELECT * FROM sqlite_master where type='table' and name='" + tableName + "'");

		// ResultSet rs = stat.executeQuery("select * from people;");
		while (rs.next()) {
			System.out.println("name = " + rs.getString("name"));

			implSb.append("      public void createTable(SQLiteDatabase db) {\n");

			String sql = rs.getString("sql");
			sql= sql.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
			sql=sql.replaceAll("\r\n", "");
			sql=sql.replaceAll("\n", "");
			
//			sql= sql.replaceAll("\r\n" ,"\" + \n  \"" );
			implSb.append("        db.execSQL(\"" +sql + "\"); \n");

			implSb.append("     }\n");
		}

		implSb.append("     }\n");
		String fileName = PropertyUtil.getProperty("daoFileFolder") + File.separator + NamingUtil.getClassName(tableName)
				+ "Dao.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(implSb.toString().getBytes());
		bos.close();
	}

	public void genColumns(List<Entry<String, String>> colums, int type) {

		if (type == 1) {

			implSb.append("   public static final class COLUMNINDEXS{\n");
			for (int i = 0; i < colums.size(); i++) {
				Entry<String, String> entry = colums.get(i);

				String columnName = NamingUtil.getInstanceName(entry.getKey());

				implSb.append("    public static final int " + columnName + "=" + i+";\n");
			}
			implSb.append("   }\n");
		} else if (type == 2) {
			implSb.append("   public static final class COLUMNS{\n");
			for (int i = 0; i < colums.size(); i++) {
				Entry<String, String> entry = colums.get(i);

				String columnName = NamingUtil.getInstanceName(entry.getKey());

				implSb.append("    public static final String " + columnName + "=\"[" + columnName + "]\";\n");
			}
			implSb.append("   }\n");
		}

	}

	public void modifytable(String tableName, int type) {

		

		String pk = Globel.tableKey.get(tableName);

		// 新增
		if (type == 0) {
			implSb.append("       public int insert(" + NamingUtil.getClassName(tableName) + " entity){\n ");
			implSb.append("        SQLiteDatabase db=mOpenHelper.getWritableDatabase();\n ");
			implSb.append("         try{\n ");
			implSb.append("         entity.lifeStatus=1;\n ");
			implSb.append("         entity.upgradeFlag=getUpgrade(db);\n ");
			implSb.append("         return insert0(db, entity);\n ");
		}
		// 编辑
		if (type == 1) {
			implSb.append("       public boolean update(" + NamingUtil.getClassName(tableName) + " entity){\n ");
			implSb.append("        SQLiteDatabase db=mOpenHelper.getWritableDatabase();\n ");
			implSb.append("         try{\n ");
			implSb.append("         entity.upgradeFlag=getUpgrade(db);\n ");
			implSb.append("         return update0(db, entity, COLUMNS." + pk
					+ "+\"=?\", new String[]{String.valueOf(entity." + pk + ")} );\n ");

		}
		// 删除
		if (type == 2) {
			implSb.append("       public boolean delete(" + NamingUtil.getClassName(tableName) + " entity){\n ");
			implSb.append("        SQLiteDatabase db=null;\n ");
			implSb.append("         try{\n ");
			implSb.append("         return delete0(db=mOpenHelper.getWritableDatabase(), COLUMNS." + pk
					+ "+\"=?\", new String[]{String.valueOf(entity." + pk + ")} );\n ");
		}

		implSb.append("         }finally{\n ");
		implSb.append("         if (db!=null) db.close();\n ");

		implSb.append("         }\n        }\n");

	}
}
