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

import org.realtors.rets.common.metadata.MetadataType;
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
        sValidTypes = new HashSet<String>();
        sValidTypes.add(MetadataType.SYSTEM.name());
        sValidTypes.add(MetadataType.RESOURCE.name());
        sValidTypes.add(MetadataType.CLASS.name());
        sValidTypes.add(MetadataType.TABLE.name());
        sValidTypes.add(MetadataType.UPDATE.name());
        sValidTypes.add(MetadataType.UPDATE_TYPE.name());
        sValidTypes.add(MetadataType.OBJECT.name());
        sValidTypes.add(MetadataType.SEARCH_HELP.name());
        sValidTypes.add(MetadataType.EDITMASK.name());
        sValidTypes.add(MetadataType.LOOKUP.name());
        sValidTypes.add(MetadataType.LOOKUP_TYPE.name());
        sValidTypes.add(MetadataType.VALIDATION_LOOKUP.name());
        sValidTypes.add(MetadataType.VALIDATION_LOOKUP_TYPE.name());
        sValidTypes.add(MetadataType.VALIDATION_EXTERNAL.name());
        sValidTypes.add(MetadataType.VALIDATION_EXTERNAL_TYPE.name());
        sValidTypes.add(MetadataType.VALIDATION_EXPRESSION.name());
        sValidTypes.add(MetadataType.FOREIGN_KEYS.name());
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
            return MetadataFormatter.STANDARD;
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
    private static Set<String> sValidTypes;
    private User mUser;
}
