/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

public class TestSqlConverter implements SqlConverter
{
    public TestSqlConverter(String string)
    {
        mString = string;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mString);
    }

    private String mString;
}
