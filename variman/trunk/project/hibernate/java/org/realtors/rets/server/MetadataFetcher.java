/*
 */
package org.realtors.rets.server;

import java.util.List;

public interface MetadataFetcher
{
    /**
     * Returns a list of metadata segments matching the specified type and
     * levels.
     *
     * @param type Metadata type to fetch
     * @param levels The levels to match
     * @return A list of MetadataSegment objects
     * @throws RetsServerException if an error occurs
     */
    public List fetchMetadata(String type, String[] levels)
        throws RetsServerException;
}
