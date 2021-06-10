
package net.swa.index.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import net.swa.index.beans.entity.ApkVersion;
import net.swa.index.service.ApkVersionService;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.util.JsonResult;

@Service("apkVersionService")
public class ApkVersionServiceImpl extends HibernateDaoSupport implements ApkVersionService
{

    @Override
    public Map<String, Object> add(ApkVersion model)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        Session session = getCurrentSession();
        session.save(model);
        map.put("success", true);
        map.put("message", "true");
        return map;
    }

    @Override
    public ApkVersion queryById(Long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ApkVersion queryLastVersion()
    {
        ApkVersion m = null;
        Session session = getCurrentSession();
        String hql = "from ApkVersion order by id desc";
        Query query = session.createQuery(hql);
        query.setMaxResults(10);
        List<ApkVersion> l = query.list();
        if (null != l && l.size() > 0)
        {
            m = (ApkVersion) l.get(0);
        }
        return m;
    }

    @Override
    public JsonResult<ApkVersion> queryPage(String name , int currentPage , int pageSize , String orderBy , String orderType)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> update(ApkVersion model)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        Session session = getCurrentSession();
        session.update(model);
        map.put("success", true);
        map.put("message", "true");
        return map;
    }

    @Override
    public Map<String, Object> save(ApkVersion model)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        Session session = getCurrentSession();
        session.save(model);
        map.put("success", true);
        map.put("message", "true");
        return map;
    }

}