package com.hibernate5;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.FileUtil;

import java.util.Iterator;
import java.util.Map.Entry;


public class FileCopyGenerator implements Generator {

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
