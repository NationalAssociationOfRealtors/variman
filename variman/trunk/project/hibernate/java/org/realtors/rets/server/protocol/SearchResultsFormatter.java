/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import org.realtors.rets.server.RetsServerException;

/**
 * Formats search results.
 */
public interface SearchResultsFormatter
{
    /**
     * Formats search results.
     *
     * @param context Context of results
     * @throws RetsServerException if an error occurs
     */ 
    public void formatResults(SearchFormatterContext context)
        throws RetsServerException;
}
