/*
 * Variman RETS Server
 *
 * Author: Dave Dribin and Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsVersion;
import org.realtors.rets.server.Util;
import org.realtors.rets.server.User;
import org.realtors.rets.server.protocol.DmqlQuery.Version;

public class SearchParameters
{
    public SearchParameters(Map/*String,String[]*/ parameterMap, RetsVersion version)
        throws RetsReplyException
    {
        this(parameterMap, version, null);
    }

    public SearchParameters(Map/*String,String[]*/ parameterMap, RetsVersion retsVersion, User user)
        throws RetsReplyException
    {
        logParameterMap(parameterMap);
        initResourceId(parameterMap);
        initClassName(parameterMap);
        initDmqlQueryAndIsStandardNames(parameterMap, retsVersion);
        initCount(parameterMap);
        initFormat(parameterMap);
        initLimit(parameterMap);
        initOffset(parameterMap);
        initSelect(parameterMap);
        mUser = user;
    }
    
    private void initResourceId(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String resourceId = getParameterValue(parameterMap, PN_SEARCH_TYPE);
        mResourceId = resourceId;
    }

    private void initClassName(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String className = getParameterValue(parameterMap, PN_CLASS);
        mClassName = className;
    }

    private void initDmqlQueryAndIsStandardNames(
            Map/*String,String[]*/ parameterMap, RetsVersion retsVersion)
        throws RetsReplyException
    {
        String query = getParameterValue(parameterMap, PN_QUERY);
        String queryType = getParameterValue(parameterMap, PN_QUERY_TYPE);
        Version dmqlVersion = null;
        if ((retsVersion == RetsVersion.RETS_1_0) && queryType.equals(Version.DMQL.getVersionString())) {
            dmqlVersion = Version.DMQL;
        } else if ((retsVersion == RetsVersion.RETS_1_5) && queryType.equals(Version.DMQL2.getVersionString())) {
            dmqlVersion = Version.DMQL2;
        } else {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                    "Invalid " + PN_QUERY_TYPE.getName() + ": " + queryType);
        }
        String standardNames = getParameterValue(parameterMap, PN_STANDARD_NAMES);
        boolean isStandardNames = true;
        if ((standardNames == null) || standardNames.equals("0")) {
            isStandardNames = false;
        } else if (standardNames.equals("1")) {
            isStandardNames = true;
        } else {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                    "Invalid " + PN_STANDARD_NAMES.getName() + ": " + standardNames);
        }
        mDmqlQuery = new DmqlQuery(query, dmqlVersion, isStandardNames);
        mIsStandardNames = isStandardNames;
    }

    private void initCount(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String count = getParameterValue(parameterMap, PN_COUNT);
        mCount = Count.getEnum(count);
        if (mCount == null) {
            mCount = NO_COUNT;
        }
    }

    private void initFormat(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        mFormat = null;
        mDtdVersion = null;
        String requestedFormat = getParameterValue(parameterMap, PN_FORMAT);
        if (requestedFormat != null) {
            mFormat = SearchFormat.getEnum(requestedFormat);
            if (mFormat == null) {
                String prefix = SearchFormat.STANDARD_XML.getName() + ":";
                if (requestedFormat.startsWith(prefix) && requestedFormat.length() > prefix.length()) {
                    mDtdVersion = requestedFormat.substring(prefix.length());
                    mFormat = SearchFormat.STANDARD_XML;
                }
            }
        }
        
        if (mFormat == null) {
            mFormat = SearchFormat.STANDARD_XML;
        }
    }

    private void initLimit(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String limit = getParameterValue(parameterMap, PN_LIMIT);
        // "NONE" will not parse, and will correctly be set to MAX_VALUE. So
        // will any other non-integer string, but we'll let it slide, rather
        // than throwing an error.
        mLimit = NumberUtils.toInt(limit, Integer.MAX_VALUE);
    }

    private void initOffset(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String offset = getParameterValue(parameterMap, PN_OFFSET);
        mOffset = NumberUtils.toInt(offset, 1);
    }

    private void initSelect(Map/*String,String[]*/ parameterMap) throws RetsReplyException
    {
        String select = getParameterValue(parameterMap, PN_SELECT);
        if (select != null) {
            mSelect = StringUtils.split(select, ",");
        } else {
            mSelect = ArrayUtils.EMPTY_STRING_ARRAY;
        }
    }

    private String getParameterValue(Map/*String,String[]*/ parameterMap, ParameterName paramName) throws RetsReplyException
    {
        String paramValue = null;
        String name = paramName.getName();
        String[] values = (String[]) parameterMap.get(name);
        if (values != null && values.length > 0) {
            paramValue = values[0];
        }
        
        if (paramValue == null && paramName.isRequired()) {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                    name + " is required");
        }
        return paramValue;
    }

    private void logParameterMap(Map/*String,String[]*/ parameterMap)
    {
        if (!LOG.isDebugEnabled())
        {
            return;
        }

        String separator = "";
        StringBuffer message = new StringBuffer();
        message.append("Parameters: {");
        Set keys = parameterMap.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            String[] value = (String[]) parameterMap.get(key);
            message.append(separator).append(key).append("=")
                .append(Arrays.asList(value));
            separator  = ", ";
        }
        message.append("}");
        LOG.debug(message.toString());
    }

    public String getClassName()
    {
        return mClassName;
    }
    
    public String getDtdVersion()
    {
        return mDtdVersion;
    }

    public SearchFormat getFormat()
    {
        return mFormat;
    }

    public String getResourceId()
    {
        return mResourceId;
    }

    public boolean isStandardNames()
    {
        return mIsStandardNames;
    }

    public String[] getSelect()
    {
        return mSelect;
    }

    public int getLimit()
    {
        return mLimit;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public Count getCount()
    {
        return mCount;
    }
    
    public DmqlQuery getDmqlQuery()
    {
        return mDmqlQuery;
    }
    
    public boolean countRequested()
    {
        return ((mCount == COUNT_AND_DATA) || (mCount == COUNT_ONLY));
    }

    public boolean dataRequested()
    {
        return ((mCount == NO_COUNT) || (mCount == COUNT_AND_DATA));
    }

    public User getUser()
    {
        return mUser;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(PN_SEARCH_TYPE.getName(), mResourceId)
            .append(PN_CLASS.getName(), mClassName)
            .append(PN_QUERY_TYPE.getName(), mDmqlQuery.getVersion().getVersionString())
            .append(PN_QUERY.getName(), mDmqlQuery.getDmql())
            .append(PN_FORMAT.getName(), mFormat.getName())
            .append("user", mUser)
            .toString();
    }

    public static class Count extends Enum
    {
        Count(String name)
        {
            super(name);
        }

        public static Count getEnum(String name)
        {
            return (Count) getEnum(Count.class, name);
        }
    }
    
    public static class ParameterName extends Enum
    {
        private boolean mRequired;
        
        ParameterName(String name, boolean required) {
            super(name);
            mRequired = required;
        }
        
        public boolean isRequired() {
            return mRequired;
        }
        
        public static ParameterName getEnum(String name)
        {
            return (ParameterName) getEnum(ParameterName.class, name);
        }
    }

    public static final Count NO_COUNT = new Count("0");
    public static final Count COUNT_AND_DATA = new Count("1");
    public static final Count COUNT_ONLY = new Count("2");
    
    private static final boolean REQUIRED = true;
    private static final boolean NOT_REQUIRED = !REQUIRED;
    public static final ParameterName PN_SEARCH_TYPE = new ParameterName("SearchType", REQUIRED);
    public static final ParameterName PN_CLASS = new ParameterName("Class", REQUIRED);
    public static final ParameterName PN_QUERY = new ParameterName("Query", REQUIRED);
    public static final ParameterName PN_QUERY_TYPE = new ParameterName("QueryType", REQUIRED);
    public static final ParameterName PN_COUNT = new ParameterName("Count", NOT_REQUIRED);
    public static final ParameterName PN_FORMAT = new ParameterName("Format", NOT_REQUIRED);
    public static final ParameterName PN_LIMIT = new ParameterName("Limit", NOT_REQUIRED);
    public static final ParameterName PN_OFFSET = new ParameterName("Offset", NOT_REQUIRED);
    public static final ParameterName PN_SELECT = new ParameterName("Select", NOT_REQUIRED);
    public static final ParameterName PN_RESTRICTION_INDICATOR = new ParameterName("RestrictionIndicator", NOT_REQUIRED); // TODO: Do something with this parameter name.
    public static final ParameterName PN_STANDARD_NAMES = new ParameterName("StandardNames", NOT_REQUIRED);
    
    private static final Logger LOG = Logger.getLogger(SearchParameters.class);
    
    private String mResourceId;
    private String mClassName;
    private DmqlQuery mDmqlQuery;
    private boolean mIsStandardNames;
    private Count mCount;
    private String mDtdVersion; // TODO: Do something with this info if format is STANDARD-XML.
    private SearchFormat mFormat;
    private int mLimit;
    private int mOffset;
    private String[] mSelect = ArrayUtils.EMPTY_STRING_ARRAY;
    private User mUser;
    
}
