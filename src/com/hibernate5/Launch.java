package com.hibernate5;

import com.hibernate5.*;
import com.cjnetwork.webtool.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class Launch {
	
	static final String version = "V1.0";

	public static void main(String[] args) {
		
		ConfigUtil.initTargetFolder("mysql");
		
		List<Generator> generatorList = new ArrayList<Generator>();
		generatorList.add(new JarGenerator());
		generatorList.add(new ModelGenerator());
//		generatorList.add(new DaoGenerator());
		generatorList.add(new ServiceGenerator());
		generatorList.add(new ActionGenerator());
		generatorList.add(new JspGenerator());
		generatorList.add(new ConfigFileGenerator());
		generatorList.add(new FileCopyGenerator());
		for(int i = 0; i < generatorList.size(); i++){
			generatorList.get(i).generate();
		}
		
		System.out.println("complete...");
	}
}
