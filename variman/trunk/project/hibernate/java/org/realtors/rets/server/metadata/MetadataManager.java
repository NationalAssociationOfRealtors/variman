/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

public class MetadataManager
{
    public MetadataManager()
    {
        mTables = new HashMap();
        mTableByPath = new HashMap();
    }

    public void add(ServerMetadata metadata)
    {
        addByLevel(metadata);
        addByPath(metadata);
    }

    private void addByLevel(ServerMetadata metadata)
    {
        Map table = (Map) mTables.get(metadata.getTableName());
        if (table == null)
        {
            table = new HashMap();
            mTables.put(metadata.getTableName(), table);
        }
        List data = getList(table, metadata.getLevel());
        data.add(metadata);
//        data = getList(table, "*");
//        data.add(metadata);
    }

    private List getList(Map table, String key)
    {
        List data = (List) table.get(key);
        if (data == null)
        {
            data = new ArrayList();
            table.put(key, data);
        }
        return data;
    }

    private void addByPath(ServerMetadata metadata)
    {
        String path = metadata.getPath();
        if (path.equals(""))
        {
            return;
        }
        Map table = (Map) mTableByPath.get(metadata.getTableName());
        if (table == null)
        {
            table = new HashMap();
            mTableByPath.put(metadata.getTableName(), table);
        }
        table.put(path, metadata);
    }

    public List find(String tableName, String level)
    {
        LOG.debug("find: " + tableName + ", level: " + level);
        List found = Collections.EMPTY_LIST;
        Map table = (Map) mTables.get(tableName);
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
        Map table = (Map) mTableByPath.get(tableName);
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

        Map table = (Map) mTables.get(tableName);
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

//            List data = getList(table, "*");
//            for (int i = 0; i < data.size(); i++)
//            {
//                ServerMetadata metadata = (ServerMetadata) data.get(i);
//                if (levelsMatch(metadata.getLevel(), patternElements))
//                {
//                    List atLevel = getList(found, metadata.getLevel());
//                    atLevel.add(metadata);
//                }
//            }
        }
        return found;
    }

    public static boolean levelsMatch(String level, String pattern)
    {
        String[] patternElements = StringUtils.split(pattern, ":");
        return levelsMatch(level, patternElements);
    }

    private static boolean levelsMatch(String level, String[] patternElements)
    {
        String[] levelElements = StringUtils.split(level, ":");
        if (levelElements.length < patternElements.length)
        {
            return false;
        }

        // Pad "*" on front of short patterns so that the lenghts are equals
        if (patternElements.length < levelElements.length)
        {
            String[] newPattern = new String[levelElements.length];
            int numStarsToAdd = newPattern.length - patternElements.length;
            for (int i = 0; i < newPattern.length; i++)
            {
                if (i < numStarsToAdd)
                {
                    newPattern[i] = "*";
                }
                else
                {
                    newPattern[i] = patternElements[i - numStarsToAdd];
                }
            }
        }

        boolean match = true;
        for (int i = 0; i < levelElements.length && i < patternElements.length;
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

    private static final Logger LOG =
        Logger.getLogger(MetadataManager.class);
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
    private Map mTables;
    private Map mTableByPath;
}
