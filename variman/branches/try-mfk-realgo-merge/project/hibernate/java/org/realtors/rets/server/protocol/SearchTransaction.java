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

import java.io.PrintWriter;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataManager;

/**
 * A search transaction writes a search response to a supplied print writer.
 */
public interface SearchTransaction
{
    /**
     * Writes the search transaction response to the specified print writer
     * based on the specified search parameters and using the specified
     * metadata manager.
     * 
     * @param out the print writer to write the search transaction response to.
     *        Must not be <code>null</code>.
     * @param searchParameters the search parameters defining what to search
     *        for. Must not be <code>null</code>.
     * @param metadataManager the metadata manager used to resolve metadata
     *        referenced in the search parameters. Must not be
     *        <code>null</code>.
     * @return The statistics of the search transaction.
     * @throws RetsServerException if unable to completely write all search
     *         transaction response data.
     */
    public SearchTransactionStatistics execute(PrintWriter out, SearchParameters searchParameters, MetadataManager metadataManager) throws RetsServerException;
}
