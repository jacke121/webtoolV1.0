package com.sqlite;

import java.util.Iterator;
import java.util.Map.Entry;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.FileUtil;


public class FileCopyGenerator implements Generator{

	@Override
	public boolean generate() {
		boolean result = false;
		try {
			Iterator<Entry<String, String>> iter = Globel.fileCopyPool.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, String> entry = iter.next();
				FileUtil.readAndReplaceWithProperties(entry.getKey(), entry.getValue());
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
