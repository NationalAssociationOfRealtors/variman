package org.realtors.rets.server.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.protocol.GetObjectParameters;
import org.realtors.rets.server.protocol.GetObjectTransaction;

/**
 * @web.servlet name="object-servlet"
 * @web.servlet-mapping  url-pattern="/objects/*"
 */
public class ObjectServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String uri = request.getRequestURI();
        String componentPath = uri;
        componentPath = componentPath.substring(contextPath.length());
        componentPath = componentPath.substring(servletPath.length());

        String[] pathComponents = StringUtils.split(componentPath, "/");
        if (LOG.isDebugEnabled())
        {
            LOG.debug("URI: " + uri);
            LOG.debug("Component path: " + componentPath);
            LOG.debug("Path Components: " + Arrays.asList(pathComponents));
        }

        if ((pathComponents.length != 4) || uri.endsWith("/"))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, uri);
            return;
        }

        String resource = pathComponents[0];
        String type = pathComponents[1];
        String key = pathComponents[2];
        String id = pathComponents[3];

        GetObjectParameters getObjectParameters =
            new GetObjectParameters(resource, type, key + ":" + id);
        GetObjectTransaction transaction =
            new GetObjectTransaction(getObjectParameters);
        transaction.setRootDirectory(WebApp.getGetObjectRoot());
        transaction.setPattern(WebApp.getGetObjectPattern());
        List allFiles = transaction.findAllFileObjects();
        if (allFiles.size() == 0)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, uri);
            return;
        }
        else if (allFiles.size() > 1)
        {
            LOG.error("Too many files: URI: " + uri + ", " + allFiles);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        File file = (File) allFiles.get(0);
        LOG.debug("File: " + file);
        response.setContentType(transaction.getContentType(file.getPath()));
        IOUtils.copyStream(new FileInputStream(file),
                           response.getOutputStream());
    }

    private static final Logger LOG =
        Logger.getLogger(ObjectServlet.class);
}
