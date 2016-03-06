package com.cjnetwork.webtool.util;

import java.io.File;

public class ConfigUtil {

	/**
	 * 加载 配置信息
	 * YanHuan 2011-1-2上午10:21:27
	 */
	public static boolean loadConfig(){
		boolean result = false;
		
		result = true;
		return result;
	}

	/**
	 * 初始化目标路径
	 * 清空目标路径中的文件，或是创建目标路径
	 * YanHuan 2011-1-2上午10:28:20
	 */
	public static void initTargetFolder(String type) {
		String targetFolderPath = PropertyUtil.getProperty("targetFolder");
		if(targetFolderPath == null){
			throw new NullPointerException("目标路径(targetFolder)为null，未在properties文件中设置!");
		}else{
			File targetFolder = new File(targetFolderPath);
			if(targetFolder.exists()){
				FileUtil.deleteFolder(targetFolderPath);
			}
			targetFolder.mkdirs();
		}
		
		String packagePrefix = PropertyUtil.getProperty("packagePrefix");
		if(packagePrefix == null){
			throw new NullPointerException("目标包名(packagePrefix)为null，未在properties文件中设置!");
		}else{
			String rootFolder = new File(targetFolderPath).getPath();
			if("sqlite".equals(type)){
				String daoFileFolder = rootFolder + File.separator + "src" + File.separator + packagePrefix.replace(".", File.separator) + File.separator + "dao";
				String configFileFolder = rootFolder + File.separator + "src";
				PropertyUtil.addProperty("daoFileFolder", daoFileFolder);
				PropertyUtil.addProperty("configFileFolder", configFileFolder);
				new File(daoFileFolder).mkdirs();
				new File(configFileFolder).mkdirs();
			}else {
				String modelFileFolder = rootFolder + File.separator + "src" + File.separator + packagePrefix.replace(".", File.separator) + File.separator + "model";
				String daoFileFolder = rootFolder + File.separator + "src" + File.separator + packagePrefix.replace(".", File.separator) + File.separator + "dao";
				String serviceFileFolder = rootFolder + File.separator + "src" + File.separator + packagePrefix.replace(".", File.separator) + File.separator + "service";
				String actionFileFolder = rootFolder + File.separator + "src" + File.separator + packagePrefix.replace(".", File.separator) + File.separator + "action";
				String configFileFolder = rootFolder + File.separator + "src";
				String webRootFolder = rootFolder + File.separator + "WebRoot";
				String webInfFolder = webRootFolder + File.separator + "WEB-INF";
				String webMetaFolder = webRootFolder + File.separator + "META-INF";
				String libFileFolder = webInfFolder + File.separator + "lib";
				String pagesFolder = webRootFolder + File.separator + "pages";


				PropertyUtil.addProperty("modelFileFolder", modelFileFolder);
				PropertyUtil.addProperty("daoFileFolder", daoFileFolder);
				PropertyUtil.addProperty("serviceFileFolder", serviceFileFolder);
				PropertyUtil.addProperty("actionFileFolder", actionFileFolder);
				PropertyUtil.addProperty("libFileFolder", libFileFolder);
				PropertyUtil.addProperty("configFileFolder", configFileFolder);
				PropertyUtil.addProperty("webRootFolder", webRootFolder);
				PropertyUtil.addProperty("webInfFolder", webInfFolder);
				PropertyUtil.addProperty("webMetaFolder", webMetaFolder);
				PropertyUtil.addProperty("pagesFolder", pagesFolder);

				new File(modelFileFolder).mkdirs();
				new File(daoFileFolder).mkdirs();
				new File(serviceFileFolder).mkdirs();
				new File(actionFileFolder).mkdirs();
				new File(libFileFolder).mkdirs();
				new File(configFileFolder).mkdirs();
				new File(webRootFolder).mkdirs();
				new File(webInfFolder).mkdirs();
				new File(webMetaFolder).mkdirs();
				new File(pagesFolder).mkdirs();
			}
		}
	}

}
