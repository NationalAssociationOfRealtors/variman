/*
 */
package org.realtors.rets.server;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.MetadataManager;

import org.apache.commons.lang.StringUtils;

public class ManagerMetadataFetcher extends BaseMetadataFetcher
{
    public ManagerMetadataFetcher()
    {
        this(null);
    }

    public ManagerMetadataFetcher(MetadataManager manager)
    {
        mManager = manager;
    }

    public List fetchMetadata(String type, String[] levels, boolean recursive)
        throws RetsReplyException
    {
        // Always need system to get version and date
        MetadataManager manager = getMetadataManager();
        MSystem system = findSystem(manager);
        String version = system.getVersionString();
        Date date = system.getDate();
        List metadataResults = new ArrayList();

        List metadata = manager.find(type, StringUtils.join(levels, ":"));
        metadataResults.add(new MetadataSegment(metadata, levels, version,
                                                date));
        if (recursive)
        {
            recurseChildren(metadata,  metadataResults, version, date);
        }
        return metadataResults;
    }

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
