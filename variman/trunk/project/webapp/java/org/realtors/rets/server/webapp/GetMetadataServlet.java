/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMetadataServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/xml");
        InputStream inStream = getResource("metadata_response.xml");
        OutputStream outStream = response.getOutputStream();
        copyIOStream(inStream, outStream);
    }
}
