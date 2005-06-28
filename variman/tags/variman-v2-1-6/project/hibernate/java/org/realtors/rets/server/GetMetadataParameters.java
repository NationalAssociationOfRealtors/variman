/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.format.MetadataFormatter;
import org.realtors.rets.server.protocol.TransactionParameters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class GetMetadataParameters extends TransactionParameters
{
    static
    {
        initValidTypes();
    }

    /**
     * Initialize from a map of HTTP parameters. <code>parameterMap</code> is
     * a map where the key is the parameter name, and the value is an array of
     * strings.
     *
     * @param parameterMap Map of HTTP parameters.
     * @throws RetsReplyException If the HTTP parameters are invalid.
     * @see javax.servlet.ServletRequest#getParameterMap
     */
    public GetMetadataParameters(Map parameterMap) throws RetsReplyException
    {
        this(parameterMap, null);
    }

    public GetMetadataParameters(Map parameterMap, User user)
        throws RetsReplyException
    {
        String type = getParameter(parameterMap, "Type");
        String id = getParameter(parameterMap, "ID");
        String formatString = getParameter(parameterMap, "Format");
        LOG.debug("type=" + type + ", id=" + id + ", format=" +
                  formatString);

        mType = cleanUpType(type);
        mFormat = parseFormat(formatString);
        mIds = StringUtils.split(id, ":");

        // Clean up id
        mRecursive = false;
        String lastId = mIds[mIds.length - 1];
        if (lastId.equals("*"))
        {
            mRecursive = true;
            // chop off "*"
            mIds = shrinkByOne(mIds);
        }
        else if (lastId.equals("0"))
        {
            mRecursive = false;
            // chop off "0"
            mIds = shrinkByOne(mIds);
        }
        mUser = user;
    }

    private static void initValidTypes()
    {
        sValidTypes = new HashSet();
        sValidTypes.add(MSystem.TABLE);
        sValidTypes.add(Resource.TABLE);
        sValidTypes.add(MClass.TABLE);
        sValidTypes.add(Table.TABLE);
        sValidTypes.add(Update.TABLE);
        sValidTypes.add(UpdateType.TABLE);
        sValidTypes.add(MObject.TABLE);
        sValidTypes.add(SearchHelp.TABLE);
        sValidTypes.add(EditMask.TABLE);
        sValidTypes.add(Lookup.TABLE);
        sValidTypes.add(LookupType.TABLE);
        sValidTypes.add(ValidationLookup.TABLE);
        sValidTypes.add(ValidationLookupType.TABLE);
        sValidTypes.add(ValidationExternal.TABLE);
        sValidTypes.add(ValidationExternalType.TABLE);
        sValidTypes.add(ValidationExpression.TABLE);
    }

    private String cleanUpType(String type) throws RetsReplyException
    {
        String cleanType = type.toUpperCase();
        if (!cleanType.startsWith("METADATA-"))
        {
            throw new RetsReplyException(ReplyCode.INVALID_TYPE, type);
        }

        cleanType = cleanType.substring("METADATA-".length());
        if (!sValidTypes.contains(cleanType))
        {
            throw new RetsReplyException(ReplyCode.INVALID_TYPE, type);
        }
        return cleanType;
    }

    private int parseFormat(String formatString) throws RetsReplyException
    {
        if (formatString == null)
        {
            return MetadataFormatter.COMPACT;
        }

        formatString = formatString.toUpperCase();
        if (formatString.equals("COMPACT"))
        {
            return MetadataFormatter.COMPACT;
        }
        else if (formatString.equals("STANDARD-XML"))
        {
            return MetadataFormatter.STANDARD;
        }
        else
        {
            throw new RetsReplyException(ReplyCode.MISC_ERROR,
                                         "Unknown format: " +
                                         formatString);
        }
    }

    private static String[] shrinkByOne(String[] array)
    {
        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, array.length - 1);
        return newArray;
    }


    public int getFormat()
    {
        return mFormat;
    }

    public void setFormat(int format)
    {
        mFormat = format;
    }

    public String[] getIds()
    {
        return mIds;
    }

    public void setIds(String[] ids)
    {
        mIds = ids;
    }

    public boolean isRecursive()
    {
        return mRecursive;
    }

    public void setRecursive(boolean recursive)
    {
        mRecursive = recursive;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String type)
    {
        mType = type;
    }

    public User getUser()
    {
        return mUser;
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataParameters.class);
    private int mFormat;
    private String mType;
    private String[] mIds;
    private boolean mRecursive;
    private static Set sValidTypes;
    private User mUser;
}
