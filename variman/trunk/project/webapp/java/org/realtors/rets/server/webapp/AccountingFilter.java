/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

/**
 * @web.filter name="accounting-filter"
 *   description="Keeps track of usage statistics for accounting"
 */
public class AccountingFilter implements Filter, Constants
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        MDC.put("uri", request.getRequestURI());

        LOG.debug("Begin AccountingFilter");
        AccountingStatistics statistics = getStatistics(session);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long responseDuration = System.currentTimeMillis() - startTime;
        statistics.addSessionTime(responseDuration);
        LOG.debug("Response duration: " + responseDuration);
        LOG.debug("Accumalated time: "+ statistics.getSessionTime());
        saveStatistics(statistics);
        MDC.remove("uri");
    }

    public void destroy()
    {
    }

    private AccountingStatistics getStatistics(HttpSession session)
    {
        AccountingStatistics statistics =
            (AccountingStatistics) session.getAttribute(ACCOUNTING_KEY);
        if (statistics == null)
        {
            User user = (User)
                session.getAttribute(AuthenticationFilter.AUTHORIZED_USER_KEY);
            statistics = createStatistics(user);
            session.setAttribute(ACCOUNTING_KEY, statistics);
        }
        return statistics;
    }

    private AccountingStatistics createStatistics(User user)
    {
        AccountingStatistics statistics = null;
        SessionHelper helper = new SessionHelper(InitServlet.getSessions(),
                                                 LOG);
        try
        {
            Session session = helper.beginTransaction();
            List results = session.find(
                "  FROM AccountingStatistics stats" +
                " WHERE stats.user = ?",
                user, Hibernate.entity(User.class));
            if (results.size() == 1)
            {
                LOG.debug("Found existing statistics");
                statistics = (AccountingStatistics) results.get(0);
            }
            else if (results.size() == 0)
            {
                LOG.debug("Creating new statistics");
                statistics = new AccountingStatistics();
                statistics.setUser(user);
                session.save(statistics);
            }
            helper.commit();
        }
        catch (HibernateException e)
        {
            helper.loggedRollback();
            statistics = null;
        }
        finally
        {
            helper.loggedClose();
        }

        if (statistics == null)
        {
            LOG.warn("Could not create persistent statistics, " +
                     "creating transient object");
            statistics = new AccountingStatistics();
        }
        return statistics;
    }

    private void saveStatistics(AccountingStatistics statistics)
    {
        SessionHelper helper = new SessionHelper(InitServlet.getSessions(),
                                                 LOG);
        try
        {
            LOG.debug("Saving statistics");
            Session session = helper.beginTransaction();
            session.saveOrUpdate(statistics);
            helper.commit();
        }
        catch (HibernateException e)
        {
            helper.loggedRollback();
        }
        finally
        {
            helper.loggedClose();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingFilter.class);
}