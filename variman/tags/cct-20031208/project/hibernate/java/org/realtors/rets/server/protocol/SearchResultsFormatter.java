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
