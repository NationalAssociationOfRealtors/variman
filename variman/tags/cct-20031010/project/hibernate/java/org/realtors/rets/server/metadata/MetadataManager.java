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
    }

    public void add(ServerMetadata metadata)
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
}
