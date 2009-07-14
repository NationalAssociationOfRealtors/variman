package org.realtors.rets.server.webapp;

import javax.servlet.http.HttpServletRequest;

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
}
