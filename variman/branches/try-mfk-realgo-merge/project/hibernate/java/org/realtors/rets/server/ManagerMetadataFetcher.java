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
 * A {@link org.realtors.rets.server.MetadataFetcher} that fetches metadata
 * from a
 * {@link org.realtors.rets.server.metadata.MetadataManager MetadataManager}.
 */
public class ManagerMetadataFetcher implements MetadataFetcher
{
    public ManagerMetadataFetcher()
    {
        // Does nothing. Be sure to set the metadata manager before using.
    }

    public ManagerMetadataFetcher(MetadataManager metadataManager)
    {
        if (metadataManager == null) {
            throw new NullPointerException("metadataManager is null.");
        }
        mManager = metadataManager;
    }

    public List/*MetadataSegment*/ fetchMetadata(String type, String[] levels)
    {
        assertInitialized();
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
        assertInitialized();
        MetadataManager manager = getMetadataManager();
        return findSystem(manager).getVersionString();
    }

    public Date getSystemDate() throws RetsServerException
    {
        assertInitialized();
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
    
    /**
     * Sets the
     * {@link org.realtors.rets.server.metadata.MetadataManager MetadataManager}
     * used by this metadata fetcher to fetch metadata.
     *
     * @param metadataManager the metadata manager used to fetch metadata. Must
     *        not be <code>null</code>.
     */
    /*
     * This method is provided to allow the metadata manager to be set if the
     * default constructor is used.
     */
    public void setMetadataManager(MetadataManager metadataManager)
    {
        if (metadataManager == null) {
            throw new NullPointerException("metadataManager is null.");
        }
        mManager = metadataManager;
    }

    private MSystem findSystem(MetadataManager manager)
    {
        List systems = manager.find(MSystem.TABLE, "");
        return (MSystem) systems.get(0);
    }
    
    private void assertInitialized()
    {
        MetadataManager manager = getMetadataManager();
        if (manager == null) {
            throw new IllegalStateException("Must set the MetadataManager before using this ManagerMetadataFetcher object.");
        }
    }

    private MetadataManager mManager;
}
