package org.realtors.rets.server.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This filter is needed to map the top-level "/" to the servlet
 * handling index.html.  This is unfortunately needed because there's no
 * way to map a servlet only to the top-level "/", as a URL pattern
 * of "/" means the default servlet, for servlet mappings.
 *
 * @web.filter name="default-filter"
 *   description="Default filter"
 */
public class DefaultFilter implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        servletRequest.getRequestDispatcher("/index.html")
            .forward(servletRequest, servletResponse);
   }

    public void destroy()
    {
    }
}
