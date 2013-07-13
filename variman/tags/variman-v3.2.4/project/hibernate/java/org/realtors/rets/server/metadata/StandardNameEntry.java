/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.realtors.rets.server.Util;
import org.realtors.rets.server.dmql.AndClause;

public class StandardNameEntry
{
    private String mStandardName;
    private String mElementName;
    private String mParent;

    /**
     * Constructor.
     * @param standardName A String containing the Standard Name
     * @param elementName A String containing the DTD Element Name
     * @param parent An optional string containing the colon separated path to this element.
     */
    public StandardNameEntry(String standardName, String elementName, String parent)
    {
        mStandardName = standardName;
        mElementName = elementName;
        mParent = parent;
    }

    /**
     * Returns the Standard Name
     * @return A String containing the Standard Name
     */
    public String getStandardName()
    {
        return mStandardName;
    }

    /**
     * Returns the DTD Element Name associated with this Standard Name
     * @return A String containing the Element Name
     */
    public String getElementName()
    {
        return mElementName;
    }

    /**
     * Returns the DTD Path for this Standard Name.
     * @return A String containing the DTD path.
     */
    public String getParent()
    {
        return mParent;
    }

    /**
     * Set the Element Name.
     * @param elementName A String containing the DTD Element Name
     */
    public void setElementName(String elementName)
    {
        mElementName = elementName;
    }

    /**
     * Set the path to the Parent Container for this Standard Name
     * @param parent
     */
    public void setParent (String parent)
    {
        mParent = parent;
    }
    
    public boolean equals(Object obj)
    {
        if (!(obj instanceof StandardNameEntry))
        {
            return false;
        }
        StandardNameEntry rhs = (StandardNameEntry) obj;
        return new EqualsBuilder()
            .append(mStandardName, rhs.mStandardName)
            .append(mElementName, rhs.mElementName)
            .append(mParent, rhs.mParent)
            .isEquals();
    }
    
    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("standard Name", mStandardName)
            .append("element name", mElementName)
            .append("parent path", mParent)
            .toString();
    }
}
