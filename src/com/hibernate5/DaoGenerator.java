package com.hibernate5;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DaoGenerator implements Generator {

	StringBuilder interfaceSb;
	StringBuilder implSb;

	// private
	@Override
	public boolean generate() {
		boolean result = false;
		try {
			generateIDao();

			generateBaseDao();

			for (int i = 0; i < Globel.tableName.size(); i++) {
				generate(Globel.tableName.get(i));
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void generateBaseDao() {
		String res = "resource/BaseDao.java";
		String tar = PropertyUtil.getProperty("daoFileFolder") + File.separator
				+ "BaseDao.java";
		Globel.fileCopyPool.put(res, tar);
	}

	/**
	 * 生成dao接口 YanHuan 2011-1-2下午05:01:26
	 */
	private void generateIDao() throws Exception {
		interfaceSb = new StringBuilder();

		interfaceSb.append("package "
				+ PropertyUtil.getProperty("packagePrefix") + ".dao;\n");
		interfaceSb.append("\n");
		interfaceSb.append("import java.util.List;\n");
		interfaceSb.append("\n");
		interfaceSb.append("\n");
		interfaceSb.append("public interface IDao<T>{\n");
		interfaceSb.append("\n");
		interfaceSb.append("		T add(T object);\n");
		interfaceSb.append("		void delete(T object);\n");
		interfaceSb.append("		void update(T object) throws Exception;\n");
		interfaceSb.append("		T findById(T object);\n");
		interfaceSb.append("		List<T> listAll();\n");
		interfaceSb
				.append("		void updateAll(final List<T> list) throws Exception;\n");
		interfaceSb
				.append("		public List<T> sqlQuery(String sql) throws Exception;\n");

		interfaceSb.append("}\n");

		String fileName = PropertyUtil.getProperty("daoFileFolder")
				+ File.separator + "IDao.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bos.write(interfaceSb.toString().getBytes());
		bos.close();
	}

	private void generate(String tableName) throws Exception {
		implSb = new StringBuilder();

		implSb.append("package " + PropertyUtil.getProperty("packagePrefix")
				+ ".dao;\n");
		implSb.append("\n");
		implSb.append("import java.util.List;\n");

		// 添加的引用
		implSb.append("import java.sql.SQLException;\n");
		implSb.append("import org.hibernate.HibernateException;\n");
		implSb.append("import org.hibernate.Session;\n");
		implSb.append("import org.springframework.orm.hibernate3.HibernateCallback;\n");
		implSb.append("import org.hibernate.Query;\n");
		implSb.append("import org.hibernate.transform.Transformers;\n");

		// --------
		implSb.append("\n");
		implSb.append("import org.springframework.stereotype.Component;\n");
		implSb.append("\n");
		implSb.append("import " + PropertyUtil.getProperty("packagePrefix")
				+ ".model." + NamingUtil.getClassName(tableName) + ";\n");
		implSb.append("\n");
		implSb.append("\n");
		implSb.append("@Component\n");
		implSb.append("public class " + NamingUtil.getClassName(tableName)
				+ "Dao extends BaseDao implements IDao<"
				+ NamingUtil.getClassName(tableName) + "> {\n");
		implSb.append("\n");

		implSb.append("\n");
		implSb.append("		public " + NamingUtil.getClassName(tableName)+" add(" + NamingUtil.getClassName(tableName)
				+ " " + NamingUtil.getInstanceName(tableName) + ") {\n");
		implSb.append("			this.getHibernateTemplate().saveOrUpdate("
				+ NamingUtil.getInstanceName(tableName) + ");\n");
		implSb.append("	return 	"+NamingUtil.getInstanceName(tableName)+";\n");
		implSb.append("		}\n");

		implSb.append("\n");
		implSb.append("		public void delete("
				+ NamingUtil.getClassName(tableName) + " "
				+ NamingUtil.getInstanceName(tableName) + ") {\n");
		implSb.append("			this.getHibernateTemplate().delete("
				+ NamingUtil.getInstanceName(tableName) + ");\n");
		implSb.append("		}\n");

		implSb.append("\n");
		implSb.append("		public void update("
				+ NamingUtil.getClassName(tableName) + " "
				+ NamingUtil.getInstanceName(tableName) + ") throws Exception {\n");
		
		implSb.append("         this.getHibernateTemplate().clear();\n");
		
		implSb.append("			this.getHibernateTemplate().saveOrUpdate("
				+ NamingUtil.getInstanceName(tableName) + ");\n");
		implSb.append("		}\n");

		implSb.append("\n");
		implSb.append("		public " + NamingUtil.getClassName(tableName)
				+ " findById(" + NamingUtil.getClassName(tableName) + " "
				+ NamingUtil.getInstanceName(tableName) + ") {\n");
		implSb.append("			return this.getHibernateTemplate().get("
				+ NamingUtil.getInstanceName(tableName) + ".getClass(), "
				+ NamingUtil.getInstanceName(tableName) + "."
				+ Globel.tableKey.get(tableName) + "());\n");
		implSb.append("		}\n");

		implSb.append("\n");
		implSb.append("		@SuppressWarnings(\"unchecked\")\n");
		implSb.append("		public List<" + NamingUtil.getClassName(tableName)
				+ "> listAll() {\n");
		implSb.append("			return (List<"+NamingUtil.getClassName(tableName)+">)this.getHibernateTemplate().find(\"from "
				+ NamingUtil.getClassName(tableName) + "\");\n");
		implSb.append("		}\n");
		//批量更新
		bitchUpdate(tableName);
		// 参数查找
		AddqueryByPara(tableName);
		
		implSb.append("     }\n");
		String fileName = PropertyUtil.getProperty("daoFileFolder")
				+ File.separator + NamingUtil.getClassName(tableName)
				+ "Dao.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bos.write(implSb.toString().getBytes());
		bos.close();
	}
	
	public void bitchUpdate(String tableName){
		
		// sb.append("		@Override\n");
		implSb.append("	    @SuppressWarnings({ \"unchecked\", \"rawtypes\" }) \n");
		implSb.append("		public void updateAll(final List<"	+ NamingUtil.getClassName(tableName)+ "> list) throws Exception {\n");
		implSb.append("		this.getHibernateTemplate().execute(new HibernateCallback() {\n");
		
		implSb.append("		public Object doInHibernate(Session session) throws HibernateException, SQLException {\n");
		implSb.append("	    for (Object obj : list) {\n");
		implSb.append("	    session.saveOrUpdate(obj);\n");
		implSb.append("		}\n");
		implSb.append("	    return null;\n");
		implSb.append("		}\n");
		implSb.append("		});\n");
		implSb.append("		}\n");
	}

	public void AddqueryByPara(String tableName) {
		// sb.append("		@Override\n");
		implSb.append("		public List<"+NamingUtil.getClassName(tableName)+"> sqlQuery(final String sql) throws Exception {\n");
		implSb.append("		List<"+NamingUtil.getClassName(tableName)+"> list =(List<"+NamingUtil.getClassName(tableName)+">)getHibernateTemplate().executeFind(new HibernateCallback<Object>() {\n");
		implSb.append("		public Object doInHibernate(Session session) throws HibernateException, SQLException {\n");
		implSb.append("		Query query = session.createSQLQuery(sql).addEntity(\""+NamingUtil.getInstanceName(tableName)+"\","+ NamingUtil.getClassName(tableName) + ".class);\n");
		implSb.append("		return query.list();\n");
		implSb.append("		}\n");
		implSb.append("		});\n");
		implSb.append("		return list;\n");
		implSb.append("     }\n");
	}

}
