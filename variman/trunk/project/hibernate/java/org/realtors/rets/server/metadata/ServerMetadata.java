/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class ServerMetadata implements Identifiable
{
    public List getChildren()
    {
        return new ArrayList();
    }

    public String getRetsId()
    {
        return null;
    }

    public String[] getPathAsArray()
    {
        return StringUtils.split(getPath(), ":");
    }

    public String getPath()
    {
        return "";
    }

    public abstract Object accept(MetadataVisitor visitor);

    public abstract String getTableName();

    public abstract String getLevel();
}
