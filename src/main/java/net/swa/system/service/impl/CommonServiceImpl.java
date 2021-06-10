package net.swa.system.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;

import net.swa.system.dao.HibernateDaoSupport;
import net.swa.system.service.ICommonService;
import net.swa.util.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;


@Service("commonService")
public class CommonServiceImpl extends HibernateDaoSupport implements
		ICommonService {
	@SuppressWarnings("unchecked")
	public <T> JsonResult<T> search(final String[] attrNames,
			final Object[] attrValues, final String[] operators,
			final Class<T> type, final int currentPage, final int pageSize,
			final String orderBy, final String orderType) throws Exception {
		JsonResult<T> json = new JsonResult<T>();
		List<T> list = new ArrayList<T>();
		StringBuilder hql = new StringBuilder();
		hql.append("from " + type.getSimpleName() + " where 1=1 ");
		for (int i = 0; i < attrNames.length; i++) {
			String operator = operators[i];
			if (!StringUtils.isEmpty(operator)) {
				operator = operator.replace("'", "");
				String val = attrValues[i] == null ? "" : attrValues[i]
						.toString();
				if (val.indexOf("||") > 0) {
					String[] vals = val.split("\\|\\|");
					hql.append(" and (");
					for (int j = 0; j < vals.length; j++) {
						hql.append((j == 0 ? "" : " or ") + attrNames[i]
								+ operator + " :param" + i + j);
					}
					hql.append(") ");
				} else {
					if (attrValues[i] != null
							&& !StringUtils.isBlank(attrValues[i].toString())) {
						hql.append(" and " + attrNames[i] + " " + operator
								+ " :param" + i);
					}
				}
			}
		}
		if (!StringUtils.isEmpty(orderBy)) {
			hql.append(" order by " + orderBy + " " + orderType);
		}
		Query query = getCurrentSession().createQuery(hql.toString());
		for (int i = 0; i < attrValues.length; i++) {
			String val = attrValues[i] == null ? "" : attrValues[i].toString();
			if (val.indexOf("||") > 0) {
				String[] vals = val.split("\\|\\|");
				for (int j = 0; j < vals.length; j++) {
					query.setString("param" + i + j, vals[j]);
				}
			} else {
				if (attrValues[i] != null
						&& !StringUtils.isBlank(attrValues[i].toString())) {
					String str = attrValues[i].toString();
					if ("like".equalsIgnoreCase(operators[i])) {
						str = "%" + str.trim() + "%";
					}
					query.setString("param" + i, str);
				}
			}
		}

		if (pageSize > 0) {
			int from = (currentPage - 1) * pageSize;
			query.setFirstResult(from);
			query.setMaxResults(pageSize);
		}
		list = query.list();
		// 查询满足条件的总数量
		query = getCurrentSession().createQuery("select count(*) " + hql);
		for (int i = 0; i < attrValues.length; i++) {
			String val = attrValues[i] == null ? "" : attrValues[i].toString();
			if (val.indexOf("||") > 0) {
				String[] vals = val.split("\\|\\|");
				for (int j = 0; j < vals.length; j++) {
					query.setString("param" + i + j, vals[j]);
				}
			} else {
				if (attrValues[i] != null
						&& !StringUtils.isBlank(attrValues[i].toString())) {
					String str = attrValues[i].toString();
					if ("like".equalsIgnoreCase(operators[i])) {
						str = "%" + str.trim() + "%";
					}
					query.setString("param" + i, str);
				}
			}

		}

		if (pageSize > 0) {
			int totalCount = ((Number) (query.iterate().next())).intValue();
			int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
					: totalCount / pageSize + 1;
			json.setTotalSize(totalCount);
			json.setTotalPage(totalPage);
			json.setPageSize(pageSize);
			json.setCurrentPage(currentPage);
		}

		json.setResult(list);

		return json;
	}

	@SuppressWarnings("unchecked")
	public <T> T findByAttribute(final Class<T> type, final String attrName,
			final Object val) {

		Query query = getCurrentSession()
				.createQuery(
						"from " + type.getSimpleName() + " where " + attrName
								+ "=:val");
		query.setString("val", val.toString());
		List<T> list = query.list();
		if (list.size() > 0) {
			return (T) list.get(0);
		} else {
			return null;
		}

	}

	/*
	 * 根据属性查询list 限制条数
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(final String attr, final Object value,
			final Class<T> type, final Integer count, final String orderBy,
			final String orderType) {

		List<T> list = new ArrayList<T>();
		String hql = "from " + type.getSimpleName();
		if (attr != null) {
			if (value != null) {
				hql += " where " + attr + "=:param1";
			} else {
				hql += " where " + attr + " is null";
			}
		}
		if (!StringUtils.isBlank(orderBy)) {
			hql += " order by " + orderBy;
			if (!StringUtils.isBlank(orderType)) {
				hql += "  " + orderType;
			}
		}
		Query query = getCurrentSession().createQuery(hql);
		if (attr != null && value != null) {
			query.setString("param1", value.toString());
		}
		if(null!=count){
		query.setMaxResults(count);
		}
		list = query.list();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jsiu.service.ICommonService#commonUpdateStatus(java.lang.String,
	 * long[], int)
	 */

	public boolean commonUpdateStatus(final String type, final Long[] ids,
			final int status) throws Exception {
		for (int i = 0; i < ids.length; i++) {
			Query query = getCurrentSession().createQuery(
					"update " + type + " set status=:state where id=:id");
			query.setInteger("state", status);
			query.setLong("id", ids[i]);
			query.executeUpdate();
		}
		return true;
	}

	/*
	 * /* (non-Javadoc)
	 * 
	 * @see cn.jsiu.service.ICommonService#commonDelete(java.lang.String,
	 * java.lang.Long[])
	 */
	public boolean commonDelete(final String type, final Long... ids)
			throws Exception {
		for (int i = 0; i < ids.length; i++) {
			Query query = getCurrentSession().createQuery(
					"from " + type + " where id=:id");
			query.setLong("id", ids[i]);
			Object obj = query.uniqueResult();
			if (null != obj) {
				getCurrentSession().delete(obj);
			}
		}
		return true;
	}

	/**
	 * 注销
	 * 
	 * @param type
	 * @param ids
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean commonDelHisStatus(final String type,
			final String stateName, final Long[] ids, final String status)
			throws Exception {

		for (int i = 0; i < ids.length; i++) {
			Query query = getCurrentSession().createQuery(
					"update " + type + " set " + stateName
							+ "=:state where id=:id");
			query.setString("state", status);
			query.setLong("id", ids[i]);
			query.executeUpdate();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jsiu.service.ICommonService#commonAdd(java.lang.Object)
	 */
	public void commonAdd(final Object obj) {
		Long id = (Long) getCurrentSession().save(obj);
		Class<? extends Object> clz = obj.getClass();
		Method[] methods = clz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Id.class)) {
				try {
					Class<?> returnType = method.getReturnType();
					Method setMethod = clz.getDeclaredMethod(
							"s"
									+ method.getName().substring(1,
											method.getName().length()),
							returnType);
					setMethod.invoke(obj, id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jsiu.service.ICommonService#commonFind(java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public <T> T commonFind(final Class<T> type, final long id) {

		T t = (T) getCurrentSession().get(type, id);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jsiu.service.ICommonService#commonUpdate(java.lang.Object)
	 */
	public void commonUpdate(final Object obj) throws Exception {
		getCurrentSession().update(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.softweare.workplat.service.ICommonService#search(java.lang.String,
	 * java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(final String attr, final Object value,
			final Class<T> type) {
		List<T> list = new ArrayList<T>();
		String hql = "from " + type.getSimpleName();
		if (attr != null) {
			hql += " where " + attr + "=:param1";
		}
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAaaaa  getCurrentSession"+getCurrentSession());
		Query query = getCurrentSession().createQuery(hql);
		if (attr != null) {
			query.setString("param1", value.toString());
		}
		list = query.list();
		return list;
	}

	/**
     * 
     */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(final Class<T> type, final String[] attrNames,
			final Object[] attrValues) {
		List<T> list = new ArrayList<T>();
		StringBuilder hql = new StringBuilder();
		hql.append("from " + type.getSimpleName() + " where 1=1 ");
		for (int i = 0; i < attrNames.length; i++) {
			if (null != attrNames[i]) {
				String val = attrValues[i] == null ? "" : attrValues[i]
						.toString();
				if (val.indexOf("||") > 0) {
					String[] vals = val.split("\\|\\|");
					hql.append(" and (");
					for (int j = 0; j < vals.length; j++) {
						hql.append((j == 0 ? "" : " or ") + attrNames[i]
								+ "= :param" + i + j);
					}
					hql.append(") ");
				} else {
					if ("title".equals(attrNames[i])
							|| attrNames[i].contains(".title")
							|| "realName".equals(attrNames[i])) {
						hql.append(" and " + attrNames[i] + " like :param" + i);
					} else {
						if (attrValues[i] != null
								&& !StringUtils
										.isBlank(attrValues[i].toString())) {
							hql.append(" and " + attrNames[i] + " = :param" + i);
						}
					}
				}
			}
		}
		Query query = getCurrentSession().createQuery(hql.toString());
		for (int i = 0; i < attrValues.length; i++) {
			if (null != attrNames[i]) {
				String val = attrValues[i] == null ? "" : attrValues[i]
						.toString();
				if (val.indexOf("||") > 0) {
					String[] vals = val.split("\\|\\|");
					for (int j = 0; j < vals.length; j++) {
						query.setString("param" + i + j, vals[j]);
					}
				} else {
					if ("title".equals(attrNames[i])
							|| attrNames[i].contains(".title")
							|| "realName".equals(attrNames[i])) {
						query.setString("param" + i, "%" + attrValues[i] + "%");
					} else {
						if (attrValues[i] != null
								&& !StringUtils
										.isBlank(attrValues[i].toString())) {
							query.setString("param" + i,
									attrValues[i].toString());
						}
					}
				}
			}
		}

		list = query.list();
		return list;
	}

	/**
     * 
     */
	@SuppressWarnings("unchecked")
	public <T> JsonResult<T> search(final String[] attrNames,
			final Object[] attrValues, final Class<T> type,
			final int currentPage, final int pageSize, final String orderBy,
			final String orderType) throws Exception {
		JsonResult<T> json = new JsonResult<T>();
		List<T> list = new ArrayList<T>();
		StringBuilder hql = new StringBuilder();
		hql.append("from " + type.getSimpleName() + " where 1=1 ");
		for (int i = 0; i < attrNames.length; i++) {
			String val = attrValues[i] == null ? "" : attrValues[i].toString();
			if (val.indexOf("||") > 0) {
				String[] vals = val.split("\\|\\|");
				hql.append(" and (");
				for (int j = 0; j < vals.length; j++) {
					hql.append((j == 0 ? "" : " or ") + attrNames[i]
							+ "= :param" + i + j);
				}
				hql.append(") ");
			} else {
				if ("title".equals(attrNames[i])
						|| attrNames[i].contains(".title")
						|| attrNames[i].contains("_name")
						|| attrNames[i].contains("address")
						|| attrNames[i].contains("_num")
						|| attrNames[i].contains("_custom")) {
					hql.append(" and " + attrNames[i] + " like :param" + i);
				} else {
					if (attrValues[i] != null
							&& !StringUtils.isBlank(attrValues[i].toString())) {
						hql.append(" and " + attrNames[i] + " = :param" + i);
					}
				}
			}
		}
		if (!StringUtils.isEmpty(orderBy)) {
			hql.append(" order by " + orderBy + " " + orderType);
		}
		System.out.println(hql.toString());
		Query query = getCurrentSession().createQuery(hql.toString());
		for (int i = 0; i < attrValues.length; i++) {
			String val = attrValues[i] == null ? "" : attrValues[i].toString();
			if (val.indexOf("||") > 0) {
				String[] vals = val.split("\\|\\|");
				for (int j = 0; j < vals.length; j++) {
					query.setString("param" + i + j, vals[j]);
				}
			} else {
				if ("title".equals(attrNames[i])
						|| attrNames[i].contains(".title")
						|| attrNames[i].contains("_name")
						|| attrNames[i].contains("address")
						|| attrNames[i].contains("_num")
						|| attrNames[i].contains("_custom")) {
					query.setString("param" + i, "%" + attrValues[i] + "%");
				} else {
					if (attrValues[i] != null
							&& !StringUtils.isBlank(attrValues[i].toString())) {
						query.setString("param" + i, attrValues[i].toString());
					}
				}
			}

		}

		if (pageSize > 0) {
			int from = (currentPage - 1) * pageSize;
			query.setFirstResult(from);
			query.setMaxResults(pageSize);
		}
		list = query.list();

		if (pageSize > 0) {
			query = getCurrentSession().createQuery("select count(*) " + hql);
			for (int i = 0; i < attrValues.length; i++) {
				String val = attrValues[i] == null ? "" : attrValues[i]
						.toString();
				if (val.indexOf("||") > 0) {
					String[] vals = val.split("\\|\\|");
					for (int j = 0; j < vals.length; j++) {
						query.setString("param" + i + j, vals[j]);
					}
				} else {
					if ("title".equals(attrNames[i])
							|| attrNames[i].contains(".title")
							|| attrNames[i].contains("_name")
							|| attrNames[i].contains("address")
							|| attrNames[i].contains("_num")
							|| attrNames[i].contains("_custom")) {
						query.setString("param" + i, "%" + attrValues[i] + "%");
					} else {
						if (attrValues[i] != null
								&& !StringUtils
										.isBlank(attrValues[i].toString())) {
							query.setString("param" + i,
									attrValues[i].toString());
						}
					}
				}

			}
			int totalCount = ((Number) (query.iterate().next())).intValue();

			int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
					: totalCount / pageSize + 1;

			json.setTotalSize(totalCount);
			json.setTotalPage(totalPage);
			json.setPageSize(pageSize);
		}

		json.setResult(list);

		return json;
	}

	public boolean batchDelete(final List<?> list) throws Exception {
		for (Object obj : list) {
			getCurrentSession().delete(obj);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T> JsonResult<T> searchBean(final String attrName,
			final Object attrValue, final Class<T> type) throws Exception {
		JsonResult<T> json = new JsonResult<T>();
		List<T> list = new ArrayList<T>();
		StringBuilder hql = new StringBuilder();
		hql.append("from " + type.getSimpleName() + " where 1=1 ");
		if (!StringUtils.isEmpty(attrName)) {
			hql.append(" and  " + attrName + "=:param1");
		}
		Query query = getCurrentSession().createQuery(hql.toString());
		if (null != attrValue) {
			query.setString("param1", attrValue.toString());
		}

		list = query.list();

		json.setResult(list);

		return json;
	}

}
