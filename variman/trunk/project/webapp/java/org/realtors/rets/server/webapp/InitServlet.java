/*
 */
package org.realtors.rets.server.webapp;

import javax.servlet.ServletException;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;

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
        initHibernate();
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

    public static SessionFactory getSessions()
    {
        return sSessions;
    }

    public static void setSessions(SessionFactory sessions)
    {
        sSessions = sessions;
    }

    private static SessionFactory sSessions;
    private static final Logger LOG =
        Logger.getLogger(InitServlet.class);
}
