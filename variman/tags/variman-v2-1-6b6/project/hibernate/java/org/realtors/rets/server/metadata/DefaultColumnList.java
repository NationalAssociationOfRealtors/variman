package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.EqualsBuilder;

public class DefaultColumnList
{
    public DefaultColumnList()
    {
        mColumns = new TreeMap();
    }

    public void add(int order, String columnName)
    {
        mColumns.put(new DefaultColumnKey(order, columnName), columnName);
    }

    public List getColumnNames()
    {
        return new ArrayList(mColumns.values());
    }

    private static class DefaultColumnKey implements Comparable
    {
        public DefaultColumnKey(int order, String columnName)
        {
            mOrder = new Integer(order);
            mName = columnName;
        }

        public int compareTo(Object o)
        {
            DefaultColumnKey rhs = (DefaultColumnKey) o;
            int compared = mOrder.compareTo(rhs.mOrder);
            if (compared == 0)
            {
                compared = mName.compareTo(rhs.mName);
            }
            return compared;
        }

        public boolean equals(Object obj)
        {
            if (!(obj instanceof DefaultColumnKey))
            {
                return false;
            }
            DefaultColumnKey rhs = (DefaultColumnKey) obj;
            return new EqualsBuilder()
                .append(mOrder, rhs.mOrder)
                .append(mName, rhs.mName)
                .isEquals();
        }

        private Integer mOrder;
        private String mName;
    }

    private Map mColumns;
}
