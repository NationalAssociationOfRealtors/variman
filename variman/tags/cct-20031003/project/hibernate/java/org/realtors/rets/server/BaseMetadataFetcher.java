/*
 */
package org.realtors.rets.server;

import java.util.List;
import java.util.Date;
import java.util.Arrays;

import org.realtors.rets.server.metadata.ServerMetadata;
import org.realtors.rets.server.metadata.MetadataSegment;

public abstract class BaseMetadataFetcher implements MetadataFetcher
{
    protected void recurseChildren(List parents, List metadataResults,
                                 String version, Date date)
    {
        for (int i = 0; i < parents.size(); i++)
        {
            ServerMetadata metadata = (ServerMetadata) parents.get(i);
            List children = metadata.getChildren();
            String[] childLevels = metadata.getPathAsArray();
            for (int j = 0; j < children.size(); j++)
            {
                ServerMetadata[] child = (ServerMetadata[]) children.get(j);
                metadataResults.add(new MetadataSegment(child, childLevels,
                                                        version, date));
                recurseChildren(Arrays.asList(child), metadataResults,
                                version, date);
            }
        }
    }
}
