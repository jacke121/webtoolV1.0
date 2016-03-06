package com.mysql;

import java.util.ArrayList;
import java.util.List;

import com.cjnetwork.webtool.generator.ActionGenerator;
import com.cjnetwork.webtool.generator.ConfigFileGenerator;
import com.cjnetwork.webtool.generator.DaoGenerator;
import com.cjnetwork.webtool.generator.FileCopyGenerator;
import com.cjnetwork.webtool.generator.Generator;
import com.cjnetwork.webtool.generator.JarGenerator;
import com.cjnetwork.webtool.generator.JspGenerator;
import com.cjnetwork.webtool.generator.ModelGenerator;
import com.cjnetwork.webtool.generator.ServiceGenerator;
import com.cjnetwork.webtool.util.ConfigUtil;

public class Launch {
	
	static final String version = "V1.0";

	public static void main(String[] args) {
		
		ConfigUtil.initTargetFolder("mysql");
		
		List<Generator> generatorList = new ArrayList<Generator>();
		generatorList.add(new JarGenerator());
		generatorList.add(new ModelGenerator());
		generatorList.add(new DaoGenerator());
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
