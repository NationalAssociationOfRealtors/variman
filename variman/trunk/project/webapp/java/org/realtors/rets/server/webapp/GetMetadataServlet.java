/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class GetMetadataServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String type = request.getParameter("Type");
        String id = request.getParameter("ID");
        String format = request.getParameter("Format");
        if (format == null)
        {
            format = COMPACT_FORMAT;
        }

        

        response.setContentType("text/xml");
        InputStream inStream = getResource("metadata_response.xml");
        OutputStream outStream = response.getOutputStream();
        copyIOStream(inStream, outStream);
        sleep(100L);
    }

    public static final String COMPACT_FORMAT = "COMPACT";
}
