package com.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cjnetwork.webtool.util.ConfigUtil;

public class Launch {

	static final String version = "V1.0";

	public static Connection con;


	public static void main(String[] args) {
		try {
			ConfigUtil.initTargetFolder("sqlite");
			Class.forName("org.sqlite.JDBC");
			Launch.con = DriverManager
					.getConnection("jdbc:sqlite:D:/project/android/androidTower/lbstock.db");
			// Launch.con =
			// DriverManager.getConnection("jdbc:sqlite:D:/project/android/androidTower/gantadb.db");
			Launch.con.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Generator> generatorList = new ArrayList<Generator>();
		generatorList.add(new ModelGenerator());
		generatorList.add(new DaoGenerator());
		generatorList.add(new FileCopyGenerator());

		for (int i = 0; i < generatorList.size(); i++) {
			generatorList.get(i).generate();
		}

		System.out.println("complete...");
	}
}
