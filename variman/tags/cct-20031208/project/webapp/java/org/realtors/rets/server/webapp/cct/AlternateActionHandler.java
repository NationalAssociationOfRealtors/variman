/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;

public class AlternateActionHandler extends BaseServletHandler
{
    public static final String NAME = "/actionAlt";

    public String getName()
    {
        return NAME;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Alternate action message");
    }
}
