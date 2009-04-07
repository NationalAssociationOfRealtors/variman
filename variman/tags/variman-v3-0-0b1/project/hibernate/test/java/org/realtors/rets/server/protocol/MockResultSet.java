/*
 */
package org.realtors.rets.server.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.sql.SQLException;

public class MockResultSet extends NullResultSet
{
    public MockResultSet()
    {
        mRows = new ArrayList();
        mColumns = new HashMap();
        mCurrentRow = -1;
    }

    public void setColumns(String[] columns)
    {
        mColumns.clear();
        for (int i = 0; i < columns.length; i++)
        {
            String column = columns[i];
            mColumns.put(column,  new Integer(i + 1));
        }
    }

    public int findColumn(String columnName) throws SQLException
    {
        Integer index = (Integer) mColumns.get(columnName);
        if (index == null)
        {
            throw new SQLException("Unknown column name: " + columnName);
        }
        return index.intValue();
    }

    public void addRow(Object[] row)
    {
        mRows.add(Arrays.asList(row));
    }

    private List getCurrentRow()
    {
        return (List) mRows.get(mCurrentRow);
    }

    public boolean next()
    {
        mCurrentRow++;
        return (mCurrentRow < mRows.size());
    }

    public String getString(String columnName) throws SQLException
    {
        return getString(findColumn(columnName));
    }

    public String getString(int columnIndex) throws SQLException
    {
        // Make it zero-based
        columnIndex--;
        List currentRow = getCurrentRow();
        Object object = currentRow.get(columnIndex);
        if (object != null)
        {
            return object.toString();
        }
        else
        {
            return null;
        }
    }
    
    public Object getObject(int columnIndex) throws SQLException
    {
        // Make it zero-based
        columnIndex--;
        List currentRow = getCurrentRow();
        return currentRow.get(columnIndex);
    }

    private List mRows;
    private int mCurrentRow;
    private Map mColumns;
}
