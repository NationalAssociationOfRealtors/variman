/*
 */
package org.realtors.rets.server;

import java.util.List;

import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MetadataSegment;

import org.apache.commons.lang.StringUtils;

/**
 * Fetches metadata from a MetadataManager.
 */
public class ManagerMetadataFetcher implements MetadataFetcher
{
    public ManagerMetadataFetcher()
    {
        this(null);
    }

    public ManagerMetadataFetcher(MetadataManager manager)
    {
        mManager = manager;
    }

    public MetadataSegment fetchMetadata(String type, String[] levels)
        throws RetsReplyException
    {
        // Always need system to get version and date
        MetadataManager manager = getMetadataManager();
        MSystem system = findSystem(manager);

        List metadata = manager.find(type, StringUtils.join(levels, ":"));
        return new MetadataSegment(metadata, levels, system.getVersionString(),
                                   system.getDate());
    }

    /**
     * Returns the metadata manager to use for fetching metadata. By default,
     * this returns the manager specified in the constructor. However, this
     * may be overriden by subclasses for different behavior.
     *
     * @return the metadata manager to use for fetching metadata
     */
    protected MetadataManager getMetadataManager()
    {
        return mManager;
    }

    private MSystem findSystem(MetadataManager manager)
    {
        List systems = manager.find(MSystem.TABLE, "");
        return (MSystem) systems.get(0);
    }

    private MetadataManager mManager;
}
