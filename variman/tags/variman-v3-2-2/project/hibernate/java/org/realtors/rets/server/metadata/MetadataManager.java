/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.Util;

public class MetadataManager
{
    public MetadataManager()
    {
        mMetadataByLevel = new LinkedHashMap<String, Map<String,List<MetaObject>>>();
        mMetadataByPath = new LinkedHashMap<String, Map<String,MetaObject>>();
    }

    public void clear()
    {
        mMetadataByLevel.clear();
        mMetadataByPath.clear();
    }

    public void add(MetaObject metadata)
    {
        addByLevel(metadata);
        addByPath(metadata);
    }

    private void addByLevel(MetaObject metadata)
    {
        String metadataTypeName = metadata.getMetadataType().name();
        Map<String, List<MetaObject>> table = mMetadataByLevel.get(metadataTypeName);
        if (table == null) {
            table = new LinkedHashMap<String, List<MetaObject>>();
            mMetadataByLevel.put(metadataTypeName, table);
        }
        String level = metadata.getLevel();
        List<MetaObject> metadataList = table.get(level);
        if (metadataList == null) {
            metadataList = new ArrayList<MetaObject>();
            table.put(level, metadataList);
        }
        metadataList.add(metadata);
    }

    private void addByPath(MetaObject metadata)
    {
        String path = metadata.getPath();
        if (path == null || path.equals(""))
        {
            return;
        }
        String metadataTypeName = metadata.getMetadataType().name();
        Map<String, MetaObject> table = mMetadataByPath.get(metadataTypeName);
        if (table == null)
        {
            table = new LinkedHashMap<String, MetaObject>();
            mMetadataByPath.put(metadataTypeName, table);
        }
        table.put(path, metadata);
    }

    /*
     * TODO: Determine whether visibility should be set to private. This is
     * currently being used by unit tests and other methods in this class.
     */
    List<MetaObject> findByLevel(String metadataTypeName, String level)
    {
        List<MetaObject> found = Collections.emptyList();
        Map<String, List<MetaObject>> table = mMetadataByLevel.get(metadataTypeName);
        if (table != null)
        {
            List<MetaObject> metadataList = table.get(level);
            if (metadataList != null)
            {
                found = metadataList;
            }
        }

        return found;
    }

    /*
     * Currently being used by InitServlet.initGroupFilter.
     */
    public MetaObject findUniqueByLevel(String metadataTypeName, String level)
    {
        MetaObject metadata = null;
        List<MetaObject> metadataList = findByLevel(metadataTypeName, level);
        if (metadataList != null && metadataList.size() > 0) {
            metadata = metadataList.get(0);
        }
        return metadata;
    }

    public void addRecursive(MetaObject metadata)
    {
        add(metadata);
        MetadataType[] childMetadataTypes = metadata.getChildTypes();
        for (MetadataType childMetadataType : childMetadataTypes) {
            Collection<? extends MetaObject> children = metadata.getChildren(childMetadataType);
            for (MetaObject childMetadata : children)
            {
                addRecursive(childMetadata);
            }
        }
    }

    public MetaObject findByPath(String metadataTypeName, String path)
    {
        MetaObject metadata = null;
        Map<String, MetaObject> table = mMetadataByPath.get(metadataTypeName);
        if (table != null)
        {
            metadata = table.get(path);
        }

        return metadata;
    }
    
    /**
     * Find the Standard Name from the metadata for the given RETS table.
     * @param pathName A String containing the <code>resource:class:table</code> metadata path components
     * @return The metadata based Standard Name for the table.
     */
    public String findStandardNameByPath(String pathName)
    {
        if (pathName == null)
            return null;
        
        String standardName = null;
        String [] pathNames = pathName.split(":");

        if (pathNames.length > 2 && pathNames[2] != null)
        {
            MTable table = (MTable) findByPath(MetadataType.TABLE.name(), pathName);
            if (table != null)
                standardName = table.getStandardName();
        }
        return standardName;
    }
    
    /**
     * Find the System Name from the metadata for the given RETS table.
     * @param pathName A String containing the <code>resource:class:table</code> metadata path components
     * @return The metadata based System Name for the table.
     */
    public String findSystemdNameByPath(String pathName)
    {
        if (pathName == null)
            return null;
        
        String systemName = null;
        String [] pathNames = pathName.split(":");

        if (pathNames.length > 2 && pathNames[2] != null)
        {
            MTable table = (MTable) findByPath(MetadataType.TABLE.name(), pathName);
            if (table != null)
                systemName = table.getSystemName();
        }
        return systemName;
    }
    
    /*
     * TODO: Determine whether this should be removed. This is currently being
     * used only by unit tests.
     */
    Map<String, List<MetaObject>> findByPattern(String metadataTypeName, String pattern)
    {
        String[] patterns = StringUtils.split(pattern, ":");
        Map<String, List<MetaObject>> table = findByPattern(metadataTypeName, patterns);
        return table;
    }

    private Map<String, List<MetaObject>> findByPattern(String metadataTypeName, String[] patterns)
    {
        Map<String, List<MetaObject>> found = new LinkedHashMap<String, List<MetaObject>>();

        Map<String, List<MetaObject>> table = mMetadataByLevel.get(metadataTypeName);
        if (table != null)
        {
            Set<String> levels = table.keySet();
            for (String level : levels)
            {
                if (levelsMatch(level, patterns))
                {
                    List<MetaObject> metadataList = table.get(level);
                    found.put(level, metadataList);
                }
            }
        }
        return found;
    }

    /**
     * Matches a level to a level pattern.
     *
     * @param level level to test
     * @param pattern pattern to match against
     * @return true if level matches the pattern
     */
    /*
     * Only used by unit tests.
     */
    static boolean levelsMatch(String level, String pattern)
    {
        String[] patterns = StringUtils.split(pattern, ":");
        return levelsMatch(level, patterns);
    }

    private static boolean levelsMatch(String level, String[] patterns)
    {
        String[] levels = StringUtils.split(level, ":");
        int levelLength = levels.length;
        int patternLength = patterns.length;
        if (levelLength < patternLength)
        {
            return false;
        }

        boolean match = true;
        for (int i = 0; i < levelLength && i < patternLength; i++)
        {
            String lvl = levels[i];
            String pattern = patterns[i];
            if (!pattern.equals("*") && !pattern.equals(lvl))
            {
                match = false;
                break;
            }
        }

        return match;
    }

    public List<MetadataSegment> fetchMetadata(String type, String[] patterns)
    {
        MSystem system = findSystem(this);
        int version = system.getVersion();
        String systemVersion = Util.getVersionString(version);
        Date systemDate = system.getDate();

        List<MetadataSegment> metadataSegments = new ArrayList<MetadataSegment>();
        Map<String, List<MetaObject>> table = findByPattern(type, patterns);
        Set<String> levels = table.keySet();
        for (String level : levels)
        {
            List<MetaObject> metadataList = table.get(level);
            String[] levelArray = StringUtils.split(level, ":");
            MetadataSegment metadataSegment = new MetadataSegment(metadataList, levelArray, systemVersion, systemDate);
            metadataSegments.add(metadataSegment);
        }
        return metadataSegments;
    }

    public String getSystemVersion()
    {
        MSystem system = findSystem(this);
        int version = system.getVersion();
        String systemVersion = Util.getVersionString(version);
        return systemVersion;
    }

    public Date getSystemDate()
    {
        MSystem system = findSystem(this);
        Date systemDate = system.getDate();
        return systemDate;
    }

    private MSystem findSystem(MetadataManager manager)
    {
        List<?> systems = manager.findByLevel(MetadataType.SYSTEM.name(), "");
        MSystem system = (MSystem)systems.get(0);
        return system;
    }
    
    public MSystem getSystem()
    {
        return findSystem(this);
    }

    /**
     * A map of maps to lists of MetaObject:
     *
     * +--------------------+
     * | Metadata Type Name | -> +-------+
     * +--------------------+    | Level | -> MetaObject, MetaObject, ...
     * |    ...             |    +-------+
     * +--------------------+    |  ...  |
     *                           +-------+
     */
    private Map<String, Map<String, List<MetaObject>>> mMetadataByLevel;
    
    /**
     * A map of maps to MetaObject:
     *
     * +--------------------+
     * | Metadata Type Name | -> +-------+
     * +--------------------+    | path  | -> MetaObject
     * |    ...             |    +-------+
     * +--------------------+    |  ...  |
     *                           +-------+
     */
    private Map<String, Map<String, MetaObject>> mMetadataByPath;
}
