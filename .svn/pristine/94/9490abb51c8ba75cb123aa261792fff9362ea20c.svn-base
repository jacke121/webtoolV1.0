package com.basedao;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

public class SqlHelper extends SQLiteOpenHelper {
	private static int Version = 2;
	private Context con = null;
	private SQLiteDatabase db = null;

	public SqlHelper(Context con, String dbName, CursorFactory fac, int ver) {
		super(con, dbName, fac, ver);
		this.con = con;
		db = getWritableDatabase();
	}

	public SqlHelper(Context con, String dbName, int ver) {
		this(con, dbName, null, ver);
		this.con = con;
		db = getWritableDatabase();
	}

	public SqlHelper(Context con, String dbName) {
		this(con, dbName, Version);
		this.con = con;
		db = getWritableDatabase();
	}
	
	/** 
	 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能 
	 * 第一，getReadableDatabase()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作 
	 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作 
	 */

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Toast.makeText(con, "创建数据库", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Toast.makeText(con, "更新数据库", Toast.LENGTH_SHORT).show();
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		return db.insert(table, nullColumnHack, values);
	}

	public int update(String str, ContentValues cvalues, String str2,
			String[] strs) {
		return db.update(str, cvalues, str2, strs);
	}

	public Cursor query(String str1, String[] strs1, String str2,
			String[] strs2, String str3, String str4, String str5) {
		return db.query(str1, strs1, str2, strs2, str3, str4, str5);
	}

	private static HashMap<String, String> buildColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, "ChaneseName as "
				+ SearchManager.SUGGEST_COLUMN_TEXT_1);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
		map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
		map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
		return map;
	}

	/*public Cursor queryContacts(String str) {

		HashMap<String, String> mColumnMap = buildColumnMap();
		// 为自己的列名定义别名,具体查询SearchMangger类
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(MovieDao.TABLENAME);
		builder.setProjectionMap(mColumnMap);
		SQLiteDatabase db = getReadableDatabase();
		String[] columns = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1,
				BaseColumns._ID };
		return builder.query(db, columns, Contacts.NAME + " like " + "'%" + str
				+ "%'", null, null, null, null);
	}*/

}