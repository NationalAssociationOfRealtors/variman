/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List data = (List) table.get(metadata.getLevel());
        if (data == null)
        {
            data = new ArrayList();
            table.put(metadata.getLevel(), data);
        }
        data.add(metadata);
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
