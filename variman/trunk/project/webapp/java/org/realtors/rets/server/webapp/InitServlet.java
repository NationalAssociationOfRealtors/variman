/*
 */
package org.realtors.rets.server.webapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataManager;

import org.apache.log4j.Logger;

/**
 * @web.servlet name="init-servlet"
 *   load-on-startup="1"
 */
public class InitServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        LOG.debug("Running init servlet");
        PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                        PasswordMethod.RETS_REALM);
        initHibernate();
        initMetadata();
        LOG.debug("Init servlet completed successfully");
    }

    private void initHibernate() throws ServletException
    {
        try
        {
            Configuration cfg = new Configuration();
            cfg.addJar("retsdb2-hbm-xml.jar");
            setSessions(cfg.buildSessionFactory());
        }
        catch (HibernateException e)
        {
            throw new ServletException("Could not initialize hibernate", e);
        }
    }

    private void initMetadata() throws ServletException
    {
        LOG.debug("Initializing metadata");
        MSystem system = findSystem();
        LOG.debug("Creating metadata manager");
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);
        LOG.debug("Adding metadata to servlet context");
        ServletContext context = getServletContext();
        context.setAttribute(MANAGER_KEY, manager);
    }

    private MSystem findSystem()
        throws ServletException
    {
        SessionHelper helper = InitServlet.createHelper();
        MSystem system = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = new ArrayList();
            Iterator iterator = session.iterate("from MSystem");
            while (iterator.hasNext())
            {
                results.add(iterator.next());
            }
            if (results.size() == 1)
            {
                system = (MSystem) results.get(0);
            }
        }
        catch (HibernateException e)
        {
            LOG.warn("Caught", e);
            helper.rollback(LOG);
            throw new ServletException("Caught", e);
        }
        finally
        {
            helper.close(LOG);
        }
        return system;
    }

    public static SessionHelper createHelper()
    {
        return new SessionHelper(sSessions);
    }

    public static SessionFactory getSessions()
    {
        return sSessions;
    }

    public static Session openSession() throws HibernateException
    {
        return sSessions.openSession();
    }

    public static void setSessions(SessionFactory sessions)
    {
        sSessions = sessions;
    }

    private static final Logger LOG =
        Logger.getLogger(InitServlet.class);

    private static SessionFactory sSessions;
}
