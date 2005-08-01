/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;

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

    public List fetchMetadata(String type, String[] levels)
    {
        MetadataManager manager = getMetadataManager();
        MSystem system = findSystem(manager);

        List segments = new ArrayList();
        Map metadata = manager.findByPattern(type, levels);
        Set levelSet = metadata.keySet();
        for (Iterator i = levelSet.iterator(); i.hasNext();)
        {
            String level = (String) i.next();
            List data = (List) metadata.get(level);
            String[] levelArray = StringUtils.split(level, ":");
            segments.add(new MetadataSegment(data, levelArray,
                                             system.getVersionString(),
                                             system.getDate()));
        }
        return segments;
    }

    public String getSystemVersion() throws RetsServerException
    {
        MetadataManager manager = getMetadataManager();
        return findSystem(manager).getVersionString();
    }

    public Date getSysetmDate() throws RetsServerException
    {
        MetadataManager manager = getMetadataManager();
        return findSystem(manager).getDate();
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
