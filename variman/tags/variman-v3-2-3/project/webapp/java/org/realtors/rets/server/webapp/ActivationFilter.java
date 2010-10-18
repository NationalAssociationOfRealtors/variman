package org.realtors.rets.server.webapp;

import org.springframework.web.context.support.ServletContextResource;
import org.realtors.rets.server.activation.ActivationManager;
import org.realtors.rets.server.activation.ResourceBasedActivationManager;
import org.realtors.rets.server.activation.MD5ActivationStrategy;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @web.filter
 *      name="activation-filter"
 *      description="Checks to see if Variman has been activated."
 */
public class ActivationFilter implements Filter  {

    private static final Logger LOG = Logger.getLogger(ActivationFilter.class);

    public static final String ACTIVATION_FILE = "activationFile";
    
    private static String sHostName = null;

    private long timeout = 3600000;    
    private long starttime = 0;
    private ActivationManager activationManager;

    static final String ACTIVATE_MESSAGE = "This Variman Server has not been activated.  As a result it will " +
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
                        "/WEB-INF/activation.properties"), new MD5ActivationStrategy());
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

        if (sHostName == null)
            sHostName = java.net.InetAddress.getLocalHost().getHostName();
        
        if(activationManager.isActivated(sHostName)) {
            LOG.info("Variman is activated.");
            filterChain.doFilter(request, response);
        } else {
            if(timeLeft()) {
                LOG.warn("Variman needs to be activated or it will stop working after an hour.  " +
                        "Please visit http://www.crt.realtors.org/projects/rets/variman/support/activation.php" +
                        " to get an activation code.  When creating your activation code please use the hostname '" + sHostName + "'.");
                filterChain.doFilter(request, response);

            } else {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.setContentType("text/plain");
                response.getWriter().print(ACTIVATE_MESSAGE);
                LOG.error("Variman needs to be activated in order to run more than an hour.  " +
                        "Please visit http://www.crt.realtors.org/projects/rets/variman/support/activation.php" +
                        " to get an activation code.  When creating your activation code please use the hostname '" + sHostName + "'.");
            }
        }
    }

    private boolean timeLeft() {
        return (System.currentTimeMillis() - starttime) < timeout;
    }

    public void destroy() {
        //Do nothing
    }
}
