/*
 */
package org.realtors.rets.server.webapp;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.beans.IntrospectionException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.cfg.Configuration;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.auth.NonceReaper;
import org.realtors.rets.server.webapp.auth.NonceTable;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * @web.servlet name="init-servlet"
 *   load-on-startup="1"
 */
public class InitServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        initLog4J();
        try
        {
            LOG.debug("Running init servlet");
            WebApp.setServletContext(getServletContext());
            PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                            PasswordMethod.RETS_REALM);
            initRetsConfiguration();
            initHibernate();
            initMetadata();
            initNonceTable();
            LOG.info("Init servlet completed successfully");
        }
        catch (ServletException e)
        {
            LOG.fatal("Caught", e);
            Throwable rootCause = e.getRootCause();
            if (rootCause != null)
            {
                LOG.fatal("Caused by", rootCause);
            }
            throw e;
        }
        catch (RuntimeException e)
        {
            LOG.fatal("Caught", e);
            throw e;
        }
        catch (Error e)
        {
            LOG.fatal("Caught", e);
            throw e;
        }
    }

    private String getContextInitParameter(String name, String defaultValue)
    {
        String value = getServletContext().getInitParameter(name);
        if (value == null)
        {
            value = defaultValue;
        }
        return value;
    }

    private String resolveFromConextRoot(String file) throws ServletException
    {
        try
        {
            // Resolve file, relative to the context root directory
            URL prefix = new File(getServletContext().getRealPath("/")).toURL();
            return new URL(prefix, file).getFile();
        }
        catch (MalformedURLException e)
        {
            throw new ServletException(e);
        }
    }

    /**
     * Initialize log4j. First, the application's context is checked for the
     * file name, and then the servlet context is checked.
     *
     * @throws ServletException
     */
    private void initLog4J() throws ServletException
    {
        String log4jInitFile =
            getContextInitParameter("log4j-init-file",
                                    "WEB-INF/classes/log4j.lcf");
        log4jInitFile = resolveFromConextRoot(log4jInitFile);
        WebApp.setLog4jFile(log4jInitFile);
        WebApp.loadLog4j();
    }

    private void initRetsConfiguration() throws ServletException
    {
        try
        {
            String configFile =
                getContextInitParameter("rets-config-file",
                                        "WEB-INF/classes/rets-config.xml");
            configFile = resolveFromConextRoot(configFile);
            mRetsConfig = RetsConfig.initFromXml(new FileReader(configFile));
            LOG.debug(mRetsConfig);

            ServletContext context = getServletContext();
            String getObjectRoot =
                mRetsConfig.getGetObjectRoot(context.getRealPath("/"));
            WebApp.setGetObjectRoot(getObjectRoot);
            LOG.debug("GetObject root: " + getObjectRoot);

            String getObjectPattern =
                mRetsConfig.getGetObjectPattern("pictures/%k-%i.jpg");
            WebApp.setGetObjectPattern(getObjectPattern);
            LOG.debug("GetObject pattern: " + getObjectPattern);
        }
        catch (IntrospectionException e)
        {
            throw new ServletException(e);
        }
        catch (SAXException e)
        {
            throw new ServletException(e);
        }
        catch (IOException e)
        {
            throw new ServletException(e);
        }
    }

    private void initHibernate() throws ServletException
    {
        try
        {
            LOG.debug("Initializing hibernate");
            Configuration cfg = new Configuration();
            cfg.addJar("rex-hbm-xml.jar");
            WebApp.setSessions(cfg.buildSessionFactory());
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
        WebApp.setMetadataManager(manager);
    }

    private MSystem findSystem()
        throws ServletException
    {
        SessionHelper helper = WebApp.createHelper();
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
            helper.commit();
        }
        catch (HibernateException e)
        {
            LOG.warn("Caught", e);
            helper.rollback(LOG);
            throw new ServletException(e);
        }
        finally
        {
            helper.close(LOG);
        }
        return system;
    }

    private void initNonceTable()
    {
        NonceTable nonceTable = new NonceTable();
        int initialTimeout = mRetsConfig.getNonceInitialTimeout();
        if (initialTimeout != -1)
        {
            nonceTable.setInitialTimeout(
                initialTimeout * DateUtils.MILLIS_IN_MINUTE);
            LOG.debug("Set initial nonce timeout to " + initialTimeout +
                      " minutes");
        }

        int successTimeout = mRetsConfig.getNonceSuccessTimeout();
        if (successTimeout != -1)
        {
            nonceTable.setSuccessTimeout(
                successTimeout * DateUtils.MILLIS_IN_MINUTE);
            LOG.debug("Set success nonce timeout to " + successTimeout +
                      " minutes");
        }
        WebApp.setNonceTable(nonceTable);
        WebApp.setNonceReaper(new NonceReaper(nonceTable));
    }

    public void destroy()
    {
        WebApp.getReaper().stop();
    }

    private static final Logger LOG =
        Logger.getLogger(InitServlet.class);
    private RetsConfig mRetsConfig;
}
