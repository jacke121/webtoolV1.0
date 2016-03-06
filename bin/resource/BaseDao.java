package @@packagePrefix.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class BaseDao extends HibernateDaoSupport{

	@Resource(name="sessionFactory")
	public void setHibernateTemplate(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int executeSQL(final String sql) throws Exception {
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						return query.executeUpdate();
					}
				});
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int executeSQL(final String sql, final String param, final Object value) throws Exception {
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						if (value instanceof Collection) {
							query.setParameterList(param, (Collection) value);
						} else {
							query.setParameter(param, value);
						}
						return query.executeUpdate();
					}
				});
		return result;
	}

	@SuppressWarnings({"rawtypes", "unchecked" })
	public int executeSQL(final String sql, final Map<String, Object> param) throws Exception {
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						for (Entry<String, Object> entry : param.entrySet()) {
							if (entry.getValue() instanceof Collection) {
								query.setParameterList(entry.getKey(), (Collection) entry.getValue());
							} else {
								query.setParameter(entry.getKey(), entry.getValue());
							}
						}
						return query.executeUpdate();
					}
				});
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List executeSQLQuery(final String sql) throws Exception {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				return query.list();
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List executeSQLQuery(final String sql, final String param, final Object value) throws Exception {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if (value instanceof Collection) {
					query.setParameterList(param, (Collection) value);
				} else {
					query.setParameter(param, value);
				}
				return query.list();
			}
		});
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List executeSQLQuery(final String sql, final Map<String, Object> param) throws Exception {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				for (Entry<String, Object> entry : param.entrySet()) {
					if (entry.getValue() instanceof Collection) {
						query.setParameterList(entry.getKey(), (Collection) entry.getValue());
					} else {
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
				return query.list();
			}
		});
		return list;
	}
	
	//获取总数
	public int getTotal(String hql) throws Exception {
		return getHibernateTemplate().find(hql).size();
	}

	
	@SuppressWarnings("rawtypes")
	
	public List getList(String page,String rows,final String hql) throws Exception {
		final int currentpage=Integer.parseInt((page==null||page=="0")?"1":page);//第几页
		final int pagesize=Integer.parseInt((rows==null||rows=="0")?"10":rows);//每页多少行
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setFirstResult((currentpage-1)*pagesize);
				query.setMaxResults(pagesize);
				return query.list();
			}
		});
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getListSql(final int currentpage, final int pagesize, final String sql,final LinkedHashMap<String, Class> tables) throws Exception {

		SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
		SQLQuery query =  sessionFactory.openSession().createSQLQuery(sql);
		for (Map.Entry<String, Class> entry : tables.entrySet()) {

			query.addEntity(entry.getKey(), entry.getValue());
		}
		query.setFirstResult((currentpage - 1) * pagesize);// 从第一条记录开始
		query.setMaxResults(pagesize);// 取pagesize条数据
		return query.list();
	}

	
	//多表查询
	
	@SuppressWarnings("rawtypes")
	public List executeSQLMultipleQuery(final String sql,final LinkedHashMap<String,Class> tables) throws Exception {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query =session.createSQLQuery(sql);
				for (Map.Entry<String, Class> entry : tables.entrySet()) {
					
					query.addEntity(entry.getKey(), entry.getValue());
				}
//				session.createSQLQuery("SELECT {user.*}, {role.*}  FROM user u, role r WHERE u.roleId= r.id")   .addEntity("user", User.class).addEntity("role", Role.class)
				return query.list();
			}
		});
		return list;
	}
	
	
}
