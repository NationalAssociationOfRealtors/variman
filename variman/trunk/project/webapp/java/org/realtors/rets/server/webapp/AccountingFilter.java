/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

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

import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.AccountingStatisticsUtils;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;

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
        LOG.debug("Accumulated time: " + statistics.getSessionTime());
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
            statistics = findOrCreateStatistics(user);
            session.setAttribute(ACCOUNTING_KEY, statistics);
        }
        return statistics;
    }

    private AccountingStatistics findOrCreateStatistics(User user)
    {
        AccountingStatistics statistics = null;
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            statistics = AccountingStatisticsUtils.findByUser(user, helper);
            if (statistics == null)
            {
                LOG.debug("Creating new statistics");
                statistics = new AccountingStatistics();
                statistics.setUser(user);
                session.save(statistics);
            }
            helper.commit();
        }
        catch (Exception e)
        {
            LOG.warn("Exception", e);
            statistics = null;
        }
        finally
        {
            helper.close(LOG);
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
        try
        {
            LOG.debug("Saving statistics");
            HibernateUtils.update(statistics);
        }
        catch (Exception e)
        {
            LOG.warn("Exception", e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingFilter.class);
}
