/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public abstract class ServerMetadata
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
}
