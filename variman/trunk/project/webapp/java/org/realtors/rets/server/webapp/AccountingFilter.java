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

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.AccountingStatistics;
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
        LOG.debug("Accumalated time: "+ statistics.getSessionAccumalatedTime());
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
            statistics = createStatistics();
            session.setAttribute(ACCOUNTING_KEY, statistics);
        }
        return statistics;
    }

    private AccountingStatistics createStatistics()
    {
        AccountingStatistics statistics = null;
//        Session session = null;
//        try
//        {
//            session = InitServlet.openSession();
//            List results = session.find(
//                "  FROM AccountingStatistics acc" +
//                " WHERE acc.user.");
//            if (results.size() == 1)
//            {
//                statistics = (AccountingStatistics) results.get(0);
//            }
//        }
//        catch (HibernateException e)
//        {
//            LOG.warn("Exception", e);
//        }
//        finally
//        {
//            try
//            {
//                session.close();
//            }
//            catch (HibernateException e)
//            {
//                LOG.warn("Couldn not close session", e);
//            }
//        }
        LOG.debug("Creating new accounting statistics");
        statistics = new AccountingStatistics();
        return statistics;
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingFilter.class);
}