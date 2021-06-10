
package net.swa.system.dao;

import javax.annotation.Resource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;

public class HibernateDaoSupport
{

    private SessionFactory sessionFactory;

    @Required
    @Resource(name = "sessionFactory")
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession()
    {
        if (null == sessionFactory)
        {
            return null;
        }
        else
        {
            return this.sessionFactory.getCurrentSession();
        }
    }

    protected Session getOpenSession()
    {
        if (null == sessionFactory)
        {
            return null;
        }
        else
        {
            Session session = sessionFactory.openSession();
            return session;
        }
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

}
