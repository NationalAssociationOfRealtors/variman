/*
 */
package org.realtors.rets.server;

import java.util.List;

public interface MetadataFetcher
{
    /**
     * Returns a list of metadata segments matching the specified criteria.
     *
     * @param type Metadata type
     * @param levels Array of levels to start fetching metadata from
     * @param recursive True if all metadata below the level should be fetched.
     * @return A list of <code>MetadataSegment</code> objects.
     */
    public List fetchMetadata(String type, String[] levels, boolean recursive)
        throws RetsReplyException;
}
