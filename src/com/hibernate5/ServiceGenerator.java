package com.hibernate5;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;

public class ServiceGenerator implements Generator {
	StringBuilder serviceSb ;
	
	@Override
	public boolean generate() {
		boolean result = false;
		try {
			for (int i = 0; i < Globel.tableName.size(); i++) {
				generate(Globel.tableName.get(i));
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void generate(String tableName) throws Exception {
		serviceSb= new StringBuilder();

		serviceSb.append("package " + PropertyUtil.getProperty("packagePrefix") + ".service;\n");
		serviceSb.append("\n");
		serviceSb.append("import java.util.List;\n");
		serviceSb.append("\n");
		serviceSb.append("import org.hibernate.SessionFactory;\n");
		serviceSb.append("import javax.annotation.Resource;\n");

		serviceSb.append("import javax.annotation.Resource;\n");
		serviceSb.append("import org.hibernate.query.NativeQuery;\n");
		serviceSb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		serviceSb.append("import org.springframework.orm.hibernate5.HibernateTemplate;\n");
		serviceSb.append("import org.springframework.stereotype.Service;\n");

		serviceSb.append("import java.util.Date;\n");
		serviceSb.append("\n");
		serviceSb.append("import " + PropertyUtil.getProperty("packagePrefix") + ".model." + NamingUtil.getClassName(tableName) + ";\n");
		serviceSb.append("\n");
		serviceSb.append("\n");
		serviceSb.append("@Service\n");
		serviceSb.append("public class " + NamingUtil.getClassName(tableName) + "Service {\n");
		serviceSb.append("\n");
		serviceSb.append("\n");
		serviceSb.append("\tpublic SessionFactory getSessionFactory() {\n" +
				"\t\treturn sessionFactory;\n" +
				"\t}\n" +
				"\t@Autowired\n" +
				"\tpublic void setSessionFactory(SessionFactory sessionFactory) {\n" +
				"\t\tthis.sessionFactory = sessionFactory;\n" +
				"\t}\n" +
				"\n" +
				"\t@Resource\n" +
				"\tprivate SessionFactory sessionFactory;\n" +
				"\n" +
				"\tprivate HibernateTemplate hibernateTemplate;\n" +
				"\n" +
				"\tprivate HibernateTemplate getHibernateTemplate() {\n" +
				"\t\tif (hibernateTemplate == null) {\n" +
				"\t\t\thibernateTemplate = new HibernateTemplate(sessionFactory);\n" +
				"\t\t}\n" +
				"\t\treturn hibernateTemplate;\n" +
				"\t}");
		serviceSb.append("		public "+ NamingUtil.getClassName(tableName) +" add(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		
		
		 List<Entry<String, String>> colus=Globel.tables.get(tableName);
		 
		if(colus!=null && colus.size()>0){
			for (Entry<String, String> entry : colus) {
				if( entry.getKey().toLowerCase().equals("createtime")){
					serviceSb.append("		" +NamingUtil.getInstanceName(tableName) + "."+Globel.createtimeKey.get(tableName)+"(new Date());\n");
				}
			}
			//添加时间
		}
		
		serviceSb.append("			return (" + NamingUtil.getClassName(tableName) + ") getHibernateTemplate().save(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public void delete(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		serviceSb.append("			getHibernateTemplate().delete(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public void update(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") throws Exception {\n");
		
		 
			if(colus!=null && colus.size()>0){
				for (Entry<String, String> entry : colus) {
					if( entry.getKey().toLowerCase().equals("updatetime")){
						//添加时间和更新时间
						serviceSb.append("		   " +NamingUtil.getInstanceName(tableName) + "."+Globel.updatetimeKey.get(tableName)+"(new Date());\n");
					}
				}
			}
		
		serviceSb.append("			getHibernateTemplate().update(" + NamingUtil.getInstanceName(tableName) + ");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public " + NamingUtil.getClassName(tableName) + " findById(" + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		serviceSb.append("			return (" + NamingUtil.getClassName(tableName) + ") sessionFactory.openSession().byId(" + NamingUtil.getInstanceName(tableName) + ".getId()+\"\");\n");
		serviceSb.append("		}\n");

		serviceSb.append("\n");
		serviceSb.append("		public List<" + NamingUtil.getClassName(tableName) + "> listAll() {\n");
		serviceSb.append("			return null;//" + NamingUtil.getInstanceName(tableName) + "Dao.listAll();\n");
		serviceSb.append("		}\n");

		//批量更新
//		AddBitchUpdate(tableName);
		
//		implSb.append("		public void updateAll(final List<"	+ NamingUtil.getClassName(tableName)+ "> list) throws Exception {\n");
		//添加查找的方法
		
		AddQueryByPara(tableName);
		
		serviceSb.append("}\n");
		
		String fileName = PropertyUtil.getProperty("serviceFileFolder") + File.separator + NamingUtil.getClassName(tableName) + "Service.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(serviceSb.toString().getBytes());
		bos.close();
	}

	public void AddQueryByPara(String tableName){

		serviceSb.append("		public List<" + NamingUtil.getClassName(tableName) + "> sqlQuery(String sql) throws Exception {\n");

		serviceSb.append("		 \tNativeQuery<"+ NamingUtil.getClassName(tableName)+ "> query = getSessionFactory().openSession().createNativeQuery(sql, "+ NamingUtil.getClassName(tableName)+ ".class);\n" +
				"\t\t\tList<"+ NamingUtil.getClassName(tableName)+ "> list = query.getResultList();\n");
		serviceSb.append("			return list;\n");
		serviceSb.append("		}\n");
	}
	public void AddBitchUpdate(String tableName){
		serviceSb.append("		public void updateAll(final List<"	+ NamingUtil.getClassName(tableName)+ "> list) throws Exception {\n");
		serviceSb.append("		 this." + NamingUtil.getInstanceName(tableName) + "Dao.updateAll(list);\n");
		serviceSb.append("		}\n");
	}
}
