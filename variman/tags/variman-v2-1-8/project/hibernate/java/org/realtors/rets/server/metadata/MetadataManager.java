/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class MetadataManager
{
    public MetadataManager()
    {
        mTablesByLevel = new HashMap();
        mTablesByPath = new HashMap();
    }

    public void clear()
    {
        mTablesByLevel.clear();
        mTablesByPath.clear();
    }

    public void add(ServerMetadata metadata)
    {
        addByLevel(metadata);
        addByPath(metadata);
    }

    private static Map getMap(Map map, String key)
    {
        Map subMap = (Map) map.get(key);
        if (subMap == null)
        {
            subMap = new HashMap();
            map.put(key, subMap);
        }
        return subMap;
    }

    private static List getList(Map map, String key)
    {
        List subList = (List) map.get(key);
        if (subList == null)
        {
            subList = new ArrayList();
            map.put(key, subList);
        }
        return subList;
    }

    private void addByLevel(ServerMetadata metadata)
    {
        Map table = getMap(mTablesByLevel, metadata.getTableName());
        List data = getList(table, metadata.getLevel());
        data.add(metadata);
    }

    private void addByPath(ServerMetadata metadata)
    {
        String path = metadata.getPath();
        if (path.equals(""))
        {
            return;
        }
        Map table = getMap(mTablesByPath, metadata.getTableName());
        table.put(path, metadata);
    }

    public List find(String tableName, String level)
    {
        List found = Collections.EMPTY_LIST;
        Map table = (Map) mTablesByLevel.get(tableName);
        if (table != null)
        {
            List data = (List) table.get(level);
            if (data != null)
            {
                found = data;
            }
        }

        return found;
    }

    public ServerMetadata findUnique(String tableName, String level)
    {
        return (ServerMetadata) find(tableName, level).get(0);
    }

    public void addRecursive(ServerMetadata metadata)
    {
        add(metadata);
        List children = metadata.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            ServerMetadata[] child = (ServerMetadata[]) children.get(i);
            for (int j = 0; j < child.length; j++)
            {
                ServerMetadata childMetadata = child[j];
                addRecursive(childMetadata);
            }
        }
    }

    public ServerMetadata findByPath(String tableName, String path)
    {
        ServerMetadata metadata = null;
        Map table = (Map) mTablesByPath.get(tableName);
        if (table != null)
        {
            metadata = (ServerMetadata) table.get(path);
        }

        return metadata;
    }

    public Map findByPattern(String tableName, String pattern)
    {
        String[] patternElements = StringUtils.split(pattern, ":");
        return findByPattern(tableName, patternElements);
    }

    public Map findByPattern(String tableName, String[] patternElements)
    {
        Map found = new HashMap();

        Map table = (Map) mTablesByLevel.get(tableName);
        if (table != null)
        {
            Set levels = table.keySet();
            for (Iterator i = levels.iterator(); i.hasNext();)
            {
                String level = (String) i.next();
                if (levelsMatch(level, patternElements))
                {
                    found.put(level, table.get(level));
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
    public static boolean levelsMatch(String level, String pattern)
    {
        String[] patternElements = StringUtils.split(pattern, ":");
        return levelsMatch(level, patternElements);
    }

    private static boolean levelsMatch(String level, String[] patternElements)
    {
        String[] levelElements = StringUtils.split(level, ":");
        int levelLength = levelElements.length;
        if (levelLength < patternElements.length)
        {
            return false;
        }

        boolean match = true;
        for (int i = 0; i < levelLength && i < patternElements.length;
             i++)
        {
            String levelElement = levelElements[i];
            String patternElement = patternElements[i];
            if (!patternElement.equals("*") &&
                !patternElement.equals(levelElement))
            {
                match = false;
                break;
            }
        }

        return match;
    }

    /**
     * A map of maps to lists:
     *
     * +------------+
     * | Table name | -> +-------+
     * +------------+    | Level | -> Data, data, ...
     * |    ...     |    +-------+
     * +------------+    |  ...  |
     *                   +-------+
     */
    private Map mTablesByLevel;
    private Map mTablesByPath;
}
