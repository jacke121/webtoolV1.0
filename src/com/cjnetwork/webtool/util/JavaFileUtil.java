package com.cjnetwork.webtool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JavaFileUtil {

	
	public static String printJavaFile(String location) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("StringBuilder sb = new StringBuilder();\n");

		File file = new File(location);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String tmp = br.readLine();
		while(tmp != null){
			sb.append("sb.append(\"" + tmp.replace("\"", "\\\"") + "\\n\");\n");
			tmp = br.readLine();
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		String fileLocation = "E:\\MyEclipseWorkBench\\webtoolV1.0\\src\\com\\cjnetwork\\webtool\\aatest\\User.java";
		System.out.println(JavaFileUtil.printJavaFile(fileLocation));
	}
}
