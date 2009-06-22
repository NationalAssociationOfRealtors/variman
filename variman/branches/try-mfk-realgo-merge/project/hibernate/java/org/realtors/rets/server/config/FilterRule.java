/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.List;

import org.apache.commons.lang.enums.Enum;

public interface FilterRule {

    public Type getType();

    public void setType(Type type);

    public String getResourceID();

    public void setResourceID(String resourceId);

    public String getRetsClassName();

    public void setRetsClassName(String retsClassName);

    public List/*String*/getSystemNames();

    public void setSystemNames(List/*String*/systemNames);

    public void addSystemName(String systemName);

    public static class Type extends Enum
    {
        public static final Type INCLUDE = new Type("include");
        public static final Type EXCLUDE = new Type("exclude");
        
        Type(String type)
        {
            super(type);
        }
        
        public static List getEnumList()
        {
            return getEnumList(Type.class);
        }
    }

}