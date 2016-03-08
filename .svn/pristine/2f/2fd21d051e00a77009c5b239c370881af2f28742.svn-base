package com.cjnetwork.webtool.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.cjnetwork.webtool.common.Globel;
import com.cjnetwork.webtool.util.NamingUtil;
import com.cjnetwork.webtool.util.PropertyUtil;


public class ActionGenerator implements Generator{

	@Override
	public boolean generate() {
		boolean result = false;
		try{
			for(int i = 0; i < Globel.tableName.size(); i++){
				generate(Globel.tableName.get(i));
			}
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	private void generate(String tableName) throws Exception{
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + PropertyUtil.getProperty("packagePrefix") + ".action;\n");
		sb.append("\n");
		sb.append("import java.util.List;\n");
		sb.append("import java.util.Map;\n");
		sb.append("import com.opensymphony.xwork2.ActionSupport;\n");
		sb.append("import com.opensymphony.xwork2.ModelDriven;\n");
		sb.append("import org.springframework.stereotype.Component;\n");
		sb.append("import javax.annotation.Resource;\n");
		sb.append("import org.springframework.context.annotation.Scope;\n");
		sb.append("import org.apache.struts2.interceptor.SessionAware;\n");
		sb.append("import org.apache.struts2.interceptor.RequestAware;\n");
		sb.append("\n");
		sb.append("import " + PropertyUtil.getProperty("packagePrefix") + ".model." + NamingUtil.getClassName(tableName) + ";\n");
		sb.append("import " + PropertyUtil.getProperty("packagePrefix") + ".service." + NamingUtil.getClassName(tableName) + "Service;\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("@Component\n");
		sb.append("@Scope(\"prototype\")\n");
		sb.append("public class " + NamingUtil.getClassName(tableName) + "Action extends ActionSupport implements ModelDriven<" + NamingUtil.getClassName(tableName) + ">, SessionAware, RequestAware {\n");
		sb.append("\n");
		sb.append("		private Map<Object, Object> session;\n");
		sb.append("		private Map<Object, Object> request;\n");
		sb.append("\n");
		sb.append("		private " + NamingUtil.getClassName(tableName) + " " + NamingUtil.getInstanceName(tableName) + " = new " + NamingUtil.getClassName(tableName) + "()" + ";\n");
		sb.append("		private " + NamingUtil.getClassName(tableName) + "Service " + NamingUtil.getInstanceName(tableName) + "Service;\n");
		
		sb.append("\n");
		sb.append("\n");
		sb.append("		public String add() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				" + NamingUtil.getInstanceName(tableName) + "Service.add(" + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_add\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public String delete() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				" + NamingUtil.getInstanceName(tableName) + "Service.delete(" + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_delete\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public String update() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				" + NamingUtil.getInstanceName(tableName) + "Service.update(" + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_update\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public String view() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				" + NamingUtil.getInstanceName(tableName) + " = " + NamingUtil.getInstanceName(tableName) + "Service.findById(" + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				request.put(\"" + NamingUtil.getInstanceName(tableName) + "\", " + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_view\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public String viewForEdit() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				" + NamingUtil.getInstanceName(tableName) + " = " + NamingUtil.getInstanceName(tableName) + "Service.findById(" + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				request.put(\"" + NamingUtil.getInstanceName(tableName) + "\", " + NamingUtil.getInstanceName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_viewForEdit\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public String listAll() {\n");
		sb.append("			String result = ERROR;\n");
		sb.append("			try{\n");
		sb.append("				List<" + NamingUtil.getClassName(tableName) + "> all" + NamingUtil.getClassName(tableName) + " = " + NamingUtil.getInstanceName(tableName) + "Service.listAll();\n");
		sb.append("				request.put(\"" + "all" + NamingUtil.getClassName(tableName) + "\", " + "all" + NamingUtil.getClassName(tableName) + ");\n");
		sb.append("				result = SUCCESS + \"_listAll\";\n");
		sb.append("			}catch(Exception e){\n");
		sb.append("				e.printStackTrace();\n");
		sb.append("			}\n");
		sb.append("			return result;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public " + NamingUtil.getClassName(tableName) + "Service get" +  NamingUtil.getClassName(tableName) + "Service(){\n");
		sb.append("			return this." + NamingUtil.getInstanceName(tableName) + "Service;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		@Resource\n");
		sb.append("		public void set" + NamingUtil.getClassName(tableName) + "Service(" + NamingUtil.getClassName(tableName) + "Service " + NamingUtil.getInstanceName(tableName) + "Service){\n");
		sb.append("			this." + NamingUtil.getInstanceName(tableName) + "Service = " + NamingUtil.getInstanceName(tableName) + "Service;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		@SuppressWarnings(\"unchecked\")\n");
		sb.append("		public void setSession(Map session) { \n");
		sb.append("			this.session = session;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		@SuppressWarnings(\"unchecked\")\n");
		sb.append("		public void setRequest(Map request) { \n");
		sb.append("			this.request = request;\n");
		sb.append("		}\n");
		
		sb.append("\n");
		sb.append("		public " + NamingUtil.getClassName(tableName) + " getModel() {\n");
		sb.append("			return " + NamingUtil.getInstanceName(tableName) + ";\n");
		sb.append("		}\n");
		sb.append("}\n");

		
		String fileName = PropertyUtil.getProperty("actionFileFolder") + File.separator + NamingUtil.getClassName(tableName) + "Action.java";
		File file = new File(fileName);
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(sb.toString().getBytes());
		bos.close();
	}

}
