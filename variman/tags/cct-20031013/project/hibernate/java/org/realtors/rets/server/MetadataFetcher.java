/*
 */
package org.realtors.rets.server;

import org.realtors.rets.server.metadata.MetadataSegment;

public interface MetadataFetcher
{
    /**
     * Returns a list of metadata segments matching the specified criteria.
     *
     * @param type Metadata type
     * @param levels Array of levels to start fetching metadata from
     * @return A list of <code>MetadataSegment</code> objects.
     */
    public MetadataSegment fetchMetadata(String type, String[] levels)
        throws RetsReplyException;
}
