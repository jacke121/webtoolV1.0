package com.hibernate5;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.FileUtil;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

import java.io.*;

public class ConfigFileGenerator implements Generator {

	@Override
	public boolean generate() {
		boolean result = false;
		try{
			generateWebXml();
			
			generateStrutsXml();
			
			generateSpringXml();
			
//			generateHibernateXml();
			
			generateEclipseFile();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成Eclipse文件
	 * YanHuan 2011-1-3上午02:28:43
	 */
	private void generateEclipseFile() throws Exception{
		generateClasspathFile();
		
		generateDotSettinsFolder();
		
		generateDotMyeclipseFolder();
		
		generateDotProjectFile();
		
		generateDotMymetadataFile();
	}

	private void generateDotMymetadataFile() throws Exception {
		FileUtil.readAndReplaceWithProperties("resource/.mymetadata", PropertyUtil.getProperty("targetFolder") + File.separator + ".mymetadata");
	}

	private void generateDotProjectFile() throws Exception {
		FileUtil.readAndReplaceWithProperties("resource/.project", PropertyUtil.getProperty("targetFolder") + File.separator + ".project");		
	}

	private void generateDotMyeclipseFolder() {
		FileUtil.copyFolder("resource/.myeclipse", PropertyUtil.getProperty("targetFolder"));
	}

	private void generateDotSettinsFolder() {
		FileUtil.copyFolder("resource/.settings", PropertyUtil.getProperty("targetFolder"));
	}

	/**
	 * Eclipse项目文件.classpath
	 * YanHuan 2011-1-3上午02:01:57
	 */
	private void generateClasspathFile() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<classpath>\n");
		sb.append("		<classpathentry kind=\"src\" path=\"src\"/>\n");
		sb.append("		<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n");
		sb.append("		<classpathentry kind=\"con\" path=\"melibrary.com.genuitec.eclipse.j2eedt.core.MYECLIPSE_JAVAEE_5_CONTAINER\"/>\n");
		
		File jarFileLocation = new File("resource/lib");
		File[] files = jarFileLocation.listFiles();
		for(int i = 0; i < files.length; i++){
			String fileName = FileUtil.getName(files[i]);
			sb.append("		<classpathentry kind=\"lib\" path=\"WebRoot/WEB-INF/lib/" + fileName + "\"/>\n");
		}
		sb.append("		<classpathentry kind=\"output\" path=\"WebRoot/WEB-INF/classes\"/>\n");
		sb.append("</classpath>\n");
		
		String fileName = PropertyUtil.getProperty("targetFolder") + File.separator + ".classpath";
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(sb.toString().getBytes());
		bos.close(); 
	}

	private void generateHibernateXml() throws Exception{
		File resource = new File("resource/web.xml");
		String fileName = PropertyUtil.getProperty("configFileFolder") + File.separator + "web.xml";
		FileUtil.copy(resource, new File(fileName));
	}

	private void generateSpringXml() throws Exception {
		File resource = new File("resource/applicationContext.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sb.append(line + "\n");
		}
		String content = PropertyUtil.replaceWithProperties(sb.toString());
		
		//lbg baseDao处理
		content = content.replace("@@daoPackage", PropertyUtil.getProperty("packagePrefix"));
		content = content.replace("@@txPackage", PropertyUtil.getProperty("packagePrefix") + ".service");
		
		content = content.replace("@@hibernate_model", PropertyUtil.getProperty("packagePrefix") + ".model");
		
		String fileName = PropertyUtil.getProperty("configFileFolder") + File.separator + "applicationContext.xml";
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generateStrutsXml() throws Exception{
		File resource = new File("resource/struts.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
		StringBuilder sbOri = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sbOri.append(line + "\n");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		
		
		for(int i = 0; i < Globel.tableName.size(); i++){
			String tableName = Globel.tableName.get(i);
			sb.append("\n");
			sb.append("		<package name=\"" + NamingUtil.getInstanceName(tableName) + "\" extends=\"struts-default\">\n");
			sb.append("			<action name=\"" + NamingUtil.getInstanceName(tableName) + "_*\" class=\"" + NamingUtil.getInstanceName(tableName) + "Action\" method=\"{1}\">\n");
			sb.append("				<result type=\"redirectAction\" name=\"success_add\">" + NamingUtil.getInstanceName(tableName) + "_listAll</result>\n");
			sb.append("				<result type=\"redirectAction\" name=\"success_delete\">" + NamingUtil.getInstanceName(tableName) + "_listAll</result>\n");
			sb.append("				<result type=\"redirectAction\" name=\"success_update\">" + NamingUtil.getInstanceName(tableName) + "_listAll</result>\n");
	    	sb.append("				<result name=\"success_view\">/pages/" + NamingUtil.getInstanceName(tableName) + "/" + NamingUtil.getInstanceName(tableName) + "_view.jsp</result>\n");
	    	sb.append("				<result name=\"success_viewForEdit\">/pages/" + NamingUtil.getInstanceName(tableName) + "/" + NamingUtil.getInstanceName(tableName) + "_viewForEdit.jsp</result>\n");
	    	sb.append("				<result name=\"success_listAll\">/pages/" + NamingUtil.getInstanceName(tableName) + "/" + NamingUtil.getInstanceName(tableName) + "_listAll.jsp</result>\n");
	    	sb.append("			</action>\n");
	    	sb.append("		</package>\n");
		}
		sb.append("\n");
		
		String content = sbOri.toString().replace("@@action_config", sb.toString());
		
		String fileName = PropertyUtil.getProperty("configFileFolder") + File.separator + "struts.xml";
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		bos.write(content.getBytes());
		bos.close();
	}

	private void generateWebXml() {
		FileUtil.copy("resource/web.xml", PropertyUtil.getProperty("webInfFolder") + File.separator + "web.xml");
	}

}
