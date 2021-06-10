
package net.swa.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import net.swa.system.beans.entity.Dict;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.system.service.IDictService;
import net.swa.util.JsonResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;


@Service("dictService")
public class DictServiceImpl extends HibernateDaoSupport implements IDictService
{
    @SuppressWarnings("unchecked")
    public List<Dict> getDictType() throws Exception
    {
        List<Dict> dicType = new ArrayList<Dict>();
        Query query = getCurrentSession().createQuery("from Dict d group by d.title");
        dicType = query.list();
        return dicType;
    }

    @Override
    public void updateDicNum(final Long[] ids , final Long[] num)
    {
        for (int i = 0; i < ids.length; i++)
        {
            Query query = getCurrentSession().createQuery("update Dict" + " set dictPaixu=:num where id=:id");
            query.setLong("num", num[i]);
            query.setLong("id", ids[i]);
            query.executeUpdate();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Dict> getDictType2()
    {
        List<Dict> dicType = new ArrayList<Dict>();
        Query query = getCurrentSession().createQuery("from Dict d where title in ('电池型号','电池品牌') group by d.title");
        dicType = query.list();
        return dicType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonResult<String> openSessiondelete(Long[] ids)
    {
        Session session = getOpenSession();
        Transaction tx = session.beginTransaction();
        Query query = null;
        String hql = "from Battery  where pinPai=:pinPai or xingHao=:xingHao";
        query = session.createQuery(hql);
        JsonResult<String> json = new JsonResult<String>();
        for (int i = 0; i < ids.length; i++)
        {
            if (null != ids[i])
            {
                Dict d = (Dict) session.get(Dict.class, ids[i]);
                if (null != d)
                {
                    query.setString("pinPai", d.getKey());
                    query.setString("xingHao", d.getKey());
                    List l = query.list();
                    if (null != l && l.size() > 0)
                    {
                        json.setMessage("字典数据正在使用不可以删除");
                        json.setSuccess(false);
                        tx.rollback();
                        session.close();
                        break;
                    }
                    else
                    {
                        session.delete(d);
                    }
                }
            }
        }
        if (session.isOpen())
        {
            json.setMessage("删除成功");
            json.setSuccess(true);
            tx.commit();
            session.close();
        }
        return json;
    }
}
