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
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.protocol.GetObjectParameters;
import org.realtors.rets.server.protocol.GetObjectTransaction;
import org.realtors.rets.server.protocol.GetObjectResponse;

/**
 * @web.servlet name="get-object-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getObject"
 */
public class GetObjectServlet extends RetsServlet
{
    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        GetObjectParameters parameters =
            new GetObjectParameters(request.getParameterMap());
        GetObjectTransaction transaction =
            new GetObjectTransaction(parameters);
        RetsConfig retsConfig = RetsServer.getRetsConfiguration();
        transaction.setRootDirectory(retsConfig.getGetObjectRoot());
        transaction.setPhotoPattern(retsConfig.getPhotoPattern());
        transaction.setObjectSetPattern(retsConfig.getObjectSetPattern());
        StringBuffer location = ServletUtils.getContextPath(request);
        location.append("/objects/");
        transaction.setBaseLocationUrl(location.toString());
        transaction.execute(new Response(response));
    }

    protected boolean isXmlResponse()
    {
        return true;
    }

    private static class Response implements GetObjectResponse
    {
        public Response(HttpServletResponse httpResponse)
        {
            mHttpResponse = httpResponse;
        }

        public OutputStream getOutputStream() throws IOException
        {
            return mHttpResponse.getOutputStream();
        }

        public void setContentType(String contentType)
        {
            mHttpResponse.setContentType(contentType);
        }

        public void setHeader(String name, String value)
        {
            mHttpResponse.setHeader(name, value);
        }

        private HttpServletResponse mHttpResponse;
    }
}
