package org.realtors.rets.server.webapp;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.realtors.rets.server.activation.ActivationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.UnknownHostException;


/**
 * Created by IntelliJ IDEA.
 * User: atillman
 * Date: Jun 9, 2009
 * Time: 1:38:22 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JMock.class)
public class ActivationFilterTest {

    final Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    final ActivationFilter activationFilter = new ActivationFilter();
    final ActivationManager activationManager = context.mock(ActivationManager.class);
    String hostname;
    final HttpServletRequest request = context.mock(HttpServletRequest.class);
    final HttpServletResponse response = context.mock(HttpServletResponse.class);
    final FilterChain filterChain = context.mock(FilterChain.class);    

    @Before
    public void setup() throws UnknownHostException {
        activationFilter.setActivationManager(activationManager);
        hostname = java.net.InetAddress.getLocalHost().getHostName();
    }

    @Test
    public void doFilterPassesDownChainWhenNotHttpRequest() throws Exception {
        //Need to override the request and response here.
        final ServletRequest request = context.mock(ServletRequest.class);
        final ServletResponse response = context.mock(ServletResponse.class);
        context.checking(new Expectations() {{
            oneOf(filterChain).doFilter(request, response);
        }});
        activationFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void doFilterPassesDownChainWhenActivationTrue() throws Exception {
        context.checking(new Expectations() {{
            allowing(activationManager).isActivated(hostname); will(returnValue(true));
            oneOf(filterChain).doFilter(request, response);
        }});
        activationFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void doFilterPassesDownChainWhenActivationFalseAndTimeoutHasNotExpired() throws Exception {
        context.checking(new Expectations() {{
            allowing(activationManager).isActivated(hostname); will(returnValue(false));
            oneOf(filterChain).doFilter(request, response);
        }});
        activationFilter.setTimeout(3600000);
        activationFilter.setStarttime(System.currentTimeMillis());
        activationFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void doFilterReturns503AndMessageWhenActivationFalseAndTimeoutHasExpired() throws Exception {
        final PrintWriter writer = context.mock(PrintWriter.class);
        context.checking(new Expectations() {{
            allowing(activationManager).isActivated(hostname); will(returnValue(false));
            oneOf(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            oneOf(response).setContentType("text/plain");
            allowing(response).getWriter(); will(returnValue(writer));
            oneOf(writer).print(ActivationFilter.ACTIVATE_MESSAGE);
        }});
        activationFilter.setTimeout(3600000);
        activationFilter.setStarttime(0);
        activationFilter.doFilter(request, response, filterChain);
    }
}
