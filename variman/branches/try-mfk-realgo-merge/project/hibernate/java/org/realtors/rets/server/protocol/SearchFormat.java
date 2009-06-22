/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import org.apache.commons.lang.enums.Enum;

public class SearchFormat extends Enum
{
    public static final SearchFormat COMPACT = new SearchFormat("COMPACT");
    public static final SearchFormat COMPACT_DECODED =
        new SearchFormat("COMPACT-DECODED");
    public static final SearchFormat STANDARD_XML =
        new SearchFormat("STANDARD-XML");

    private SearchFormat(String format)
    {
        super(format);
    }

    public static SearchFormat getEnum(String format)
    {
        return (SearchFormat) getEnum(SearchFormat.class, format);
    }
    
}
