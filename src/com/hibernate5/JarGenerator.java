package com.hibernate5;

import com.cjnetwork.webtool.util.FileUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

import java.io.File;

public class JarGenerator implements Generator {

	private static  final String libFolder = "resource/lib";
	
	@Override
	public boolean generate() {
		boolean result = false;
		File folder = new File(libFolder);
		File[] jars = folder.listFiles();
		for(int i = 0; i < jars.length; i++){
			if(PropertyUtil.getProperty("libFileFolder") == null){
				throw new NullPointerException("jarFolder没有被初始化");
			}
			String filePath = PropertyUtil.getProperty("libFileFolder") + File.separator + FileUtil.getName(jars[i]);
			FileUtil.copy(jars[i], new File(filePath));
		}
		result = true;
		return result;
	}


}
