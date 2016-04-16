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
	private void generate(String tableName, List<Entry<String, String>> columns) throws Exception {
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
		implSb.append("import android.database.sqlite.SQLiteStatement;\n");
		
		
		implSb.append("import java.util.concurrent.locks.Lock;\n");
		implSb.append("import java.util.concurrent.locks.ReentrantLock;\n");

		implSb.append("\n\n");
		implSb.append("public class " + NamingUtil.getClassName(tableName) + "Dao{\n");
		implSb.append("Lock lock = new ReentrantLock();\n");
		implSb.append("    SimpleDateFormat dfu = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
		implSb.append(" public static final String TABLENAME =\"" + NamingUtil.getClassName(tableName) + "\";\n");
		implSb.append("  COLUMNINDEXS cOLUMNINDEXS=new COLUMNINDEXS();\n");
		implSb.append("  COLUMNS cOLUMNS=new COLUMNS();\n");
		implSb.append(" public final Object SYNC= new Object();\n");
		implSb.append(" private final SQLiteOpenHelper mOpenHelper;\n");
		implSb.append(" public " + NamingUtil.getClassName(tableName) + "Dao(SQLiteOpenHelper openHelper){\n");
implSb.append("   mOpenHelper=openHelper;\n  ");
		
		Statement stat = Launch.con.createStatement();
		ResultSet rs = stat.executeQuery("SELECT type,sql FROM sqlite_master where (type='table' or type= 'index') and tbl_name='" + tableName + "'");
		while (rs.next()) {
			String type = rs.getString("type");
			if(type.equals("table")	){
				String sql = rs.getString("sql");
				sql= sql.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
				sql=sql.replaceAll("\r\n", "");
				sql=sql.replaceAll("\n", "");
				
//				sql= sql.replaceAll("\r\n" ,"\" + \n  \"" );
				implSb.append("        mOpenHelper.getWritableDatabase().execSQL(\"" +sql + "\"); \n");
			}
			else if(type.equals("index")){
				String sql = rs.getString("sql");
				sql= sql.replace("CREATE INDEX", "CREATE INDEX IF NOT EXISTS");
				implSb.append("        mOpenHelper.getWritableDatabase().execSQL(\"" +sql + "\"); \n");
			}

			
		}
		rs.close();
		
		implSb.append("      }\n");

		// 查询
		implSb.append("   public Cursor query(String whereClause, String []whereArgs){\n");

		implSb.append("     final String sql = \"SELECT *\"\n");
		implSb.append("     + \" FROM \" + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+\" WHERE \"+whereClause);\n");

		implSb.append("     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);\n");

		implSb.append("     }\n\n\n");
		// + cOLUMNS.username + ","+ cOLUMNS.ID
		//-------------insertList
		implSb.append("       public  boolean insertList( SparseArray<"+NamingUtil.getClassName(tableName)+"> list) {\n");
		implSb.append("              if ( null == list || list.size() <= 0) {\n");
		implSb.append("                  return false;\n");
		implSb.append("              }\n");
		implSb.append("                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();\n");
		implSb.append("                      try {\n");
		           
		implSb.append("   	            String sql =\"insert into " +tableName + "(\" \n");
		
         
		String wenhao="";
		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);
			String columnName = NamingUtil.getInstanceName(entry.getKey());
			if(i==columns.size()-1)
				implSb.append("   + cOLUMNS." + columnName);
			else implSb.append("   + cOLUMNS." + columnName +  "+\",\"");
				wenhao+=",?";
		}
	
		implSb.append("        + \") \" + \"values("+wenhao.substring(1)+")\";\n");
		implSb.append("   	            SQLiteStatement stat = db.compileStatement(sql);\n");
		implSb.append("   	            db.beginTransaction();\n");
        implSb.append("   	             for (int i=0;i<list.size();i++)  {\n");
        implSb.append("   "+NamingUtil.getClassName(tableName)+" entity = list.get(i);  \n");
		
		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);
			String columnName = NamingUtil.getInstanceName(entry.getKey());
			 if (entry.getValue().contains("Int")||entry.getValue().startsWith("Long")) {
				 
					implSb.append("  if(null==entity."+ columnName + ") ");
					 if(columnName.toLowerCase().equals("lifestatus")){
						 implSb.append("   stat.bindLong("+(i+1)+",1);else\n");
					 }else if(columnName.toLowerCase().equals("upgradeflag")){
						 implSb.append("   stat.bindLong("+(i+1)+",1);else\n");
					 }else{
						 implSb.append("   stat.bindNull("+(i+1)+"); else\n"); 
					 }
					
				implSb.append("   stat.bindLong(" +(i+1)+",entity."+ columnName + ");\n");
			} else if (entry.getValue().startsWith("String")) {
				implSb.append("  if(null==entity."+ columnName + "||entity."+ columnName +".length()==0) ");
				implSb.append("   stat.bindNull("+(i+1)+");else\n");
				implSb.append("   stat.bindString(" +(i+1)+",entity."+ columnName + ");\n");
			}
			else if (entry.getValue().toLowerCase().startsWith("float")||entry.getValue().toLowerCase().startsWith("double")) {
				implSb.append("   stat.bindDouble(" +(i+1)+",entity."+ columnName + ");\n");
			}
			else if (entry.getValue().contains("Date")) {
//					implSb.append("  if(null==entity."+ columnName + ") ");
//					implSb.append("   stat.bindNull("+(i+1)+"); else\n");
				implSb.append("   stat.bindString(" +(i+1)+",dfu.format(new Date()));\n");
			} 
			 
		}
		  
		implSb.append("  		              long result = stat.executeInsert();\n");		 
		implSb.append("  		                if (result < 0) {\n");		 
		implSb.append("  		                    return false;\n");		 
		implSb.append("  		                }\n");		 
		implSb.append("  		            }\n");		 
		implSb.append("  		            db.setTransactionSuccessful();\n");		 
		implSb.append("  		        } catch (Exception e) {\n");		 
		implSb.append("  		            e.printStackTrace();\n");		 
		implSb.append("             return false;\n");	
		implSb.append("         } finally {\n");	
		implSb.append("             try {\n");	
		implSb.append("                     if (null != db) {\n");	
		implSb.append("                         db.endTransaction();\n");	 
		implSb.append("                     }\n");	
		implSb.append("                 } catch (Exception e) {\n");	
		implSb.append("                     e.printStackTrace();\n");	
		implSb.append("     	            }\n");	
		implSb.append("             }\n");	
		implSb.append("             return true;\n");	
		implSb.append("         }\n");	
		/////////-------updateList
		implSb.append("       public  boolean updateList( SparseArray<"+NamingUtil.getClassName(tableName)+"> list) {\n");
		implSb.append("              if ( null == list || list.size() <= 0) {\n");
		implSb.append("                  return false;\n");
		implSb.append("              }\n");
		implSb.append("                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();\n");
		implSb.append("                      try {\n");
		           
		implSb.append("   	            String sql =\"update " +tableName+" set ");
		
		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);
			String columnName = NamingUtil.getInstanceName(entry.getKey());
			if(i==columns.size()-1){
				implSb.append(columnName +  "=? where id=?\";\n");
			}
			else{
				implSb.append(columnName +  "=?,");
			}
		}
		implSb.append("   	            SQLiteStatement stat = db.compileStatement(sql);\n");
		implSb.append("   	            db.beginTransaction();\n");
        implSb.append("   	             for (int i=0;i<list.size();i++)  {\n");
        implSb.append("   "+NamingUtil.getClassName(tableName)+" entity = list.get(i);  \n");
		
		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);
			String columnName = NamingUtil.getInstanceName(entry.getKey());
			 if (entry.getValue().contains("Int")||entry.getValue().startsWith("Long")) {
				 
					implSb.append("  if(null==entity."+ columnName + ") ");
					 if(columnName.toLowerCase().equals("lifestatus")){
						 implSb.append("   stat.bindLong("+(i+1)+",1);else\n");
					 }else if(columnName.toLowerCase().equals("upgradeflag")){
						 implSb.append("   stat.bindLong("+(i+1)+",1);else\n");
					 }else{
						 implSb.append("   stat.bindNull("+(i+1)+"); else\n"); 
					 }
					
				implSb.append("   stat.bindLong(" +(i+1)+",entity."+ columnName + ");\n");
			} else if (entry.getValue().startsWith("String")) {
				implSb.append("  if(null==entity."+ columnName + "||entity."+ columnName +".length()==0) ");
				implSb.append("   stat.bindNull("+(i+1)+");else\n");
				implSb.append("   stat.bindString(" +(i+1)+",entity."+ columnName + ");\n");
			}
			else if (entry.getValue().toLowerCase().startsWith("float")||entry.getValue().toLowerCase().startsWith("double")) {
				implSb.append("   stat.bindDouble(" +(i+1)+",entity."+ columnName + ");\n");
			}
			else if (entry.getValue().contains("Date")) {
//					implSb.append("  if(null==entity."+ columnName + ") ");
//					implSb.append("   stat.bindNull("+(i+1)+"); else\n");
				implSb.append("   stat.bindString(" +(i+1)+",dfu.format(new Date()));\n");
			} 
			 if(i==columns.size()-1){
					implSb.append("   stat.bindLong(" +(i+2)+",entity.id);\n"); 
			 }
			 
		}
		  
		implSb.append("  		              long result = stat.executeUpdateDelete();\n");		 
		implSb.append("  		                if (result < 0) {\n");		 
		implSb.append("  		                    return false;\n");		 
		implSb.append("  		                }\n");		 
		implSb.append("  		            }\n");		 
		implSb.append("  		            db.setTransactionSuccessful();\n");		 
		implSb.append("  		        } catch (Exception e) {\n");		 
		implSb.append("  		            e.printStackTrace();\n");		 
		implSb.append("             return false;\n");	
		implSb.append("         } finally {\n");	
		implSb.append("             try {\n");	
		implSb.append("                     if (null != db) {\n");	
		implSb.append("                         db.endTransaction();\n");	 
		implSb.append("                     }\n");	
		implSb.append("                 } catch (Exception e) {\n");	
		implSb.append("                     e.printStackTrace();\n");	
		implSb.append("     	            }\n");	
		implSb.append("             }\n");	
		implSb.append("             return true;\n");	
		implSb.append("         }\n");	
//--------------
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

		genDataColumns(columns);
		implSb.append("       list.append(index++,entity);\n");
		implSb.append("       }\n");

		implSb.append("      cursor.close(); \n");
		implSb.append("      return list; \n");

		implSb.append("      }\n");
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
		genDataColumns(columns);
		
		implSb.append("       list.append(index++,entity);\n");
		implSb.append("       }\n");

		implSb.append("      cursor.close(); \n");
		implSb.append("      return list; \n");

		implSb.append("      }\n");
		implSb.append("      }catch(Exception ex){ \n  ex.printStackTrace();");
		implSb.append("       }finally{ \n if (cursor!= null) cursor.close();\n    }   return null;\n }");
		// 批量更新
		modifytable(tableName, 1);
		modifytable(tableName, 2);// 参数查找
		implSb.append("		public Long getUpgrade( SQLiteDatabase db ){\n");
		implSb.append("	    Long strid = 0l;\n");
				implSb.append("	    Cursor cursor =  mOpenHelper.getReadableDatabase().rawQuery(\"select last_insert_rowid() from " + tableName+"\", null);\n");
						implSb.append("	    if (cursor.moveToFirst())\n");
								implSb.append("	        strid = cursor.getLong(0);\n");
										implSb.append("	    cursor.close();\n");
												implSb.append("	    return strid+1;\n");
														implSb.append("	}\n");
		genColumns(columns, 1);
		genColumns(columns, 2);


		implSb.append("       private boolean update0(SQLiteDatabase db, " + NamingUtil.getClassName(tableName)
				+ " entity, String whereClause, String []whereArgs){ \n");

		implSb.append("       ContentValues cv=new ContentValues(1); \n");

		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());

			if (columnName.toLowerCase().equals("updatetime")) {

				implSb.append("    cv.put(cOLUMNS.updatetime, dfu.format(new Date()));\n");

			} else if (columnName.toLowerCase().equals("createtime")) {
			} else {
				implSb.append("    cv.put(cOLUMNS." + columnName + ", entity." + columnName + " );\n");
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
		implSb.append("     }\n         }\n");

		String fileName = PropertyUtil.getProperty("daoFileFolder") + File.separator + NamingUtil.getClassName(tableName)
				+ "Dao.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(implSb.toString().getBytes());
		bos.close();
	}

	
	void genDataColumns(List<Entry<String, String>> columns){
		for (int i = 0; i < columns.size(); i++) {
			Entry<String, String> entry = columns.get(i);

			String columnName = NamingUtil.getInstanceName(entry.getKey());
			if (entry.getValue().startsWith("Int")) {

				implSb.append("    entity." + columnName + "=cursor.isNull(cOLUMNINDEXS." + columnName
						+ " )? -1 :cursor.getInt(cOLUMNINDEXS." + columnName + ");\n");
			} else if (entry.getValue().startsWith("String")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(cOLUMNINDEXS." + columnName
						+ " )? \"\" :cursor.getString(cOLUMNINDEXS." + columnName + ");\n");
			}
			else if (entry.getValue().startsWith("float")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(cOLUMNINDEXS." + columnName
						+ " )?0 :cursor.getFloat(cOLUMNINDEXS." + columnName + ");\n");
			}
			else if (entry.getValue().startsWith("Long")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(cOLUMNINDEXS." + columnName
						+ " )?0 :cursor.getLong(cOLUMNINDEXS." + columnName + ");\n");
			}
			else if (entry.getValue().startsWith("Double")) {
				implSb.append("    entity." + columnName + "=cursor.isNull(cOLUMNINDEXS." + columnName
						+ " )? 0 :cursor.getDouble(cOLUMNINDEXS." + columnName + ");\n");
			}
		}
	}
	public void genColumns(List<Entry<String, String>> colums, int type) {

		if (type == 1) {

			implSb.append("   public final class COLUMNINDEXS{\n");
			for (int i = 0; i < colums.size(); i++) {
				Entry<String, String> entry = colums.get(i);

				String columnName = NamingUtil.getInstanceName(entry.getKey());

				implSb.append("    public final int " + columnName + "=" + i+";\n");
			}
			implSb.append("   }\n");
		} else if (type == 2) {
			implSb.append("   public final class COLUMNS{\n");
			for (int i = 0; i < colums.size(); i++) {
				Entry<String, String> entry = colums.get(i);

				String columnName = NamingUtil.getInstanceName(entry.getKey());

				implSb.append("    public final String " + columnName + "=\"[" + columnName + "]\";\n");
			}
			implSb.append("   }\n");
		}

	}

	public void modifytable(String tableName, int type) {

		

		String pk = Globel.tableKey.get(tableName);

		
		// 编辑
		if (type == 1) {
			implSb.append("       public boolean update(" + NamingUtil.getClassName(tableName) + " entity){\n ");
			implSb.append("        lock.lock();\n ");
			 
			implSb.append("        SQLiteDatabase db=mOpenHelper.getWritableDatabase();\n");
			implSb.append("         try{\n ");
			//implSb.append("         entity.upgradeFlag=getUpgrade(db);\n ");
			implSb.append("         return update0(db, entity, cOLUMNS." + pk
					+ "+\"=?\", new String[]{String.valueOf(entity." + pk + ")} );\n ");
			implSb.append("        }catch (Exception e) { e.printStackTrace();\n} finally {\n ");
			implSb.append("        lock.unlock();\n ");
			implSb.append("        }\n ");
			implSb.append("                     return false;\n ");
			implSb.append("        }\n ");

		}
		// 删除
		if (type == 2) {
			
			//清空表
			implSb.append("         public boolean clearData(SQLiteDatabase db) {\n ");
			implSb.append("                 try {\n ");
			implSb.append("                     db.execSQL(\"delete from "+tableName+"\");\n ");
			implSb.append("                     return true;\n ");
			implSb.append("                 } catch (Exception e) {\n ");
			implSb.append("                     e.printStackTrace();\n ");
			implSb.append("                     return false;\n ");
			implSb.append("                 }\n ");
			implSb.append("             }\n ");
			
			
			implSb.append("       public boolean delete(" + NamingUtil.getClassName(tableName) + " entity){\n ");
			implSb.append("        SQLiteDatabase db=null;\n ");
			implSb.append("         try{\n ");
			implSb.append("         return delete0(db=mOpenHelper.getWritableDatabase(), cOLUMNS." + pk
					+ "+\"=?\", new String[]{String.valueOf(entity." + pk + ")} );\n ");
			implSb.append("         }finally{\n ");
			implSb.append("         }\n        }\n");

		}

	
	}
}
