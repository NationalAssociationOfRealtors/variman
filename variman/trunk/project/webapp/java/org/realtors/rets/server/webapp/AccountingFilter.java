/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

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
        statistics.addTime(responseDuration);
        LOG.debug("Response duration: " + responseDuration);
        LOG.debug("Accumalated time: "+ statistics.getAccumaltedTime());
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
            LOG.debug("Creating new accounting statistics");
            statistics = new AccountingStatistics();
            session.setAttribute(ACCOUNTING_KEY, statistics);
        }
        return statistics;
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingFilter.class);
}