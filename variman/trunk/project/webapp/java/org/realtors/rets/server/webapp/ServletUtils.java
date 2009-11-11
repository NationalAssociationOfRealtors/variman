package org.realtors.rets.server.webapp;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.webapp.io.CountingHttpServletResponse;
import org.realtors.rets.server.webapp.io.CountingServletResponse;

public class ServletUtils
{
    public static StringBuffer getContextPath(HttpServletRequest request)
    {
        StringBuffer contextPath = new StringBuffer();
        contextPath.append(request.getScheme()).append("://");
        contextPath.append(request.getServerName());
        contextPath.append(":").append(request.getServerPort());
        contextPath.append(request.getContextPath());
        return contextPath;
    }

    /**
     * Makes the servlet response countable. That is, the returned servlet
     * response will have a {@code getByteCount()} method returning the number
     * of bytes written to the response stream.
     * 
     * @param servletResponse The servlet response to make countable.
     * @return a countable servlet response.
     * @since 0.40.15
     */
    public static ServletResponse makeCountable(ServletResponse servletResponse) {
        if (servletResponse == null) {
            return null;
        }
        ServletResponse countingServletResponse;
        if (servletResponse instanceof HttpServletResponse) {
            countingServletResponse = new CountingHttpServletResponse((HttpServletResponse)servletResponse);
        } else {
            countingServletResponse = new CountingServletResponse(servletResponse);
        }
        return countingServletResponse;
    }
}
