/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.ManagerMetadataFetcher;
import org.realtors.rets.server.MetadataFetcher;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

public class GetMetadataHandler extends BaseServletHandler
{
    public GetMetadataHandler()
    {
        mTransaction = new GetMetadataTransaction();
        mMetadataFetcher = new ServletMetadataFetcher();
        if (sMetadataTypes == null)
        {
            sMetadataTypes = new HashSet();
            sMetadataTypes.add("METADATA-SYSTEM");
            sMetadataTypes.add("METADATA-RESOURCE");
            sMetadataTypes.add("METADATA-FOREIGN_KEY");
            sMetadataTypes.add("METADATA-OBJECT");
            sMetadataTypes.add("METADATA-LOOKUP");
            sMetadataTypes.add("METADATA-LOOKUP_TYPE");
            sMetadataTypes.add("METADATA-SEARCH_HELP");
            sMetadataTypes.add("METADATA-EDITMASK");
            sMetadataTypes.add("METADATA-VALIDATION_LOOKUP");
            sMetadataTypes.add("METADATA-VALIDATION_LOOKUP_TYPE");
            sMetadataTypes.add("METADATA-VALIDATION_EXTERNAL");
            sMetadataTypes.add("METADATA-VALIDATION_EXPRESSION");
            sMetadataTypes.add("METADATA-CLASS");
            sMetadataTypes.add("METADATA-TABLE");
            sMetadataTypes.add("METADATA-UPDATE_HELP");
        }
    }

    public String getName()
    {
        return NAME;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        super.doGet(request, response);
        addRetsVersion(response);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        try
        {
            GetMetadataParameters parameters =
                new GetMetadataParameters(request.getParameterMap());
            mTransaction.execute(out, parameters, mMetadataFetcher);
        }
        catch(RetsReplyException e)
        {
            out.println("<RETS ReplyCode=\"" + e.getReplyCode() +
                        "\" ReplyText=\"" + e.getMeaning() + "\"/>\n");
        }
        catch(Exception e)
        {
            LOG.error("Caught", e);
            out.println("<RETS ReplyCode=\"20513\" " +
                        "ReplyText=\"Miscellaneous error\"/>\n");
        }
    }

    /**
     * Gets the metadata manager from the servlet request.
     */
    class ServletMetadataFetcher extends ManagerMetadataFetcher
    {
        protected MetadataManager getMetadataManager()
        {
            return WebApp.getMetadataManager();
        }
    }
    
    protected void validateParameters(ValidationResult result)
    {
        super.validateParameters(result);

        // Required arguements
        String type = (String) mActualParameters.get("Type");
        if (type == null)
        {
            result.setStatus(StatusEnum.FAILED);
            result.addMessage("getMetadata required parameter 'Type' missing.");
        }
        else
        {
            if (!sMetadataTypes.contains(type))
            {
                // We probably shouldn't drop a message here, but I'd like
                // to test this some more.
                result.addMessage(
                    "Notice: getMetadata 'Type' not a standard type");
            }
        }

        String id = (String) mActualParameters.get("ID");
        if (id == null)
        {
            result.setStatus(StatusEnum.FAILED);
            result.addMessage("getMetadata required parameter 'ID' missing.");
        }
        else
        {
            if (type != null &&
                (type.equals("METADATA-SYSTEM") ||
                type.equals("METADATA-RESOURCE")))
            {
                if (!(id.equals("*") || id.equals("0")))
                {
                    result.setStatus(StatusEnum.FAILED);
                    result.addMessage(
                        "getMetadata parameter 'ID' formatted incorrectly");
                }
            }
        }
        
        // Optional arguements
        String format = (String) mActualParameters.get("Format");
        if (format != null)
        {
            boolean valid =
                matches(format, "STANDARD-XML:RETS-METADATA-\\d{8}.dtd") ||
                format.equals("COMPACT") ||
                format.equals("STANDARD-XML");

            if (!valid)
            {
                result.setStatus(StatusEnum.FAILED);
                result.addMessage("getMetadata 'Format' parameter invalid");
            }
        }
    }

    private static Set sMetadataTypes = null;
    public static final String NAME = "/getMetadata";
    private static final Logger LOG =
        Logger.getLogger(GetMetadataHandler.class);
    private GetMetadataTransaction mTransaction;
    private MetadataFetcher mMetadataFetcher;
}
