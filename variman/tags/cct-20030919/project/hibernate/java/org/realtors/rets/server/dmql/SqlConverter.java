/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

public interface SqlConverter
{
    void toSql(PrintWriter out);
}
