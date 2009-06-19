package org.realtors.rets.server.webapp;

import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.context.support.ServletContextResource;
import org.realtors.rets.server.activation.ActivationManager;
import org.realtors.rets.server.activation.ResourceBasedActivationManager;
import org.realtors.rets.server.activation.MD5ActivationStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: atillman
 * Date: Jun 9, 2009
 * Time: 1:38:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivationFilter implements Filter  {

    private static final Log logger = LogFactory.getLog(ActivationFilter.class);

    public static final String ACTIVATION_FILE = "activationFile";

    private long timeout = 3600000;
    private long starttime = 0;
    private ActivationManager activationManager;

    static final String ACTIVATE_MESSAGE = "This Variman Server has not been activated.  As a result it will" +
            "only run for an hour at a time.";


    //This is here for testing.
    void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    //This is here for testing.
    void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    //This is here for testing.
    void setActivationManager(ActivationManager activationManager) {
        this.activationManager = activationManager;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        activationManager = new ResourceBasedActivationManager(
                new ServletContextResource(filterConfig.getServletContext(),
                        filterConfig.getInitParameter(ACTIVATION_FILE)), new MD5ActivationStrategy());
        starttime = System.currentTimeMillis();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            doActivationCheck((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void doActivationCheck(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if(activationManager.isActivated() || timeLeft()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/plain");
            response.getWriter().print(ACTIVATE_MESSAGE);
        }
    }

    private boolean timeLeft() {
        return (System.currentTimeMillis() - starttime) < timeout;
    }

    public void destroy() {
        //Do nothing
    }
}
