/*
 */
package org.realtors.rets.server.protocol;

import java.sql.SQLException;

import junit.framework.TestCase;

public class MockResultSetTest extends TestCase
{
    public void testFindColumn() throws SQLException
    {
        MockResultSet resultSet = new MockResultSet();
        resultSet.setColumns(new String[] {"column1", "column2"});
        assertEquals(1, resultSet.findColumn("column1"));
        assertEquals(2, resultSet.findColumn("column2"));
        try
        {
            resultSet.findColumn("column3");
            fail("Should have thrown exception");
        }
        catch (SQLException e)
        {
            // Expected
        }
    }

    public void testGetString() throws SQLException
    {
        MockResultSet resultSet = new MockResultSet();
        resultSet.setColumns(new String[] {"column1", "column2"});
        resultSet.addRow(new String[] {"value1", "value2"});
        resultSet.addRow(new String[] {"value3", "value4"});

        assertTrue(resultSet.next());
        assertEquals("value1", resultSet.getString("column1"));
        assertEquals("value1", resultSet.getString(1));
        assertEquals("value2", resultSet.getString("column2"));
        assertEquals("value2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("value3", resultSet.getString("column1"));
        assertEquals("value3", resultSet.getString(1));
        assertEquals("value4", resultSet.getString("column2"));
        assertEquals("value4", resultSet.getString(2));
        assertFalse(resultSet.next());
    }
}
