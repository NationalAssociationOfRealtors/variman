/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.dmql;

public abstract class LookupListType
{
    public static final LookupListType AND = new And();
    public static final LookupListType OR = new Or();
    public static final LookupListType NOT = new Not();

    public String getLookupMultiPrefix()
    {
        return "";
    }

    public String getLookupPrefix()
    {
        return "";
    }

    public String getLookupSuffix()
    {
        return "";
    }

    public String getSqlOperator()
    {
        return "";
    }

    private static class And extends LookupListType
    {
        public String getSqlOperator()
        {
            return " AND ";
        }

        public String toString()
        {
            return "and";
        }
    }

    private static class Or extends LookupListType
    {
        public String getSqlOperator()
        {
            return " OR ";
        }

        public String toString()
        {
            return "or";
        }
    }

    private static class Not extends LookupListType
    {
        public String getLookupMultiPrefix()
        {
            return "NOT ";
        }

        public String getLookupPrefix()
        {
            return "NOT (";
        }

        public String getLookupSuffix()
        {
            return ")";
        }

        public String getSqlOperator()
        {
            return " OR ";
        }

        public String toString()
        {
            return "not";
        }
    }
}
