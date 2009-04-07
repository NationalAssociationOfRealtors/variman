/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestUtil
{
    public static String toSql(SqlConverter sqlConverter)
    {
        StringWriter sql = new StringWriter();
        sqlConverter.toSql(new PrintWriter(sql));
        return sql.toString();
    }
}
