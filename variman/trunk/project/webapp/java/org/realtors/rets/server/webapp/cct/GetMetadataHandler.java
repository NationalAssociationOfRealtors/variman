/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.ManagerMetadataFetcher;
import org.realtors.rets.server.MetadataFetcher;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.webapp.WebApp;

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

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        GetMetadataParameters parameters =
            new GetMetadataParameters(request.getParameterMap());
        mTransaction.execute(out, parameters, mMetadataFetcher);
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
    private GetMetadataTransaction mTransaction;
    private MetadataFetcher mMetadataFetcher;
}
