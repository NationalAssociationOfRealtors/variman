package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.realtors.rets.server.Util;

public class EqualClause implements SqlConverter
{
    public EqualClause(String field, SqlConverter sqlConverter)
    {
        mField = field;
        mSqlConverter = sqlConverter;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mField);
        out.print(" = ");
        mSqlConverter.toSql(out);
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mField)
            .append(mSqlConverter)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof EqualClause))
        {
            return false;
        }
        EqualClause rhs = (EqualClause) obj;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .append(mSqlConverter, rhs.mSqlConverter)
            .isEquals();
    }

    private String mField;
    private SqlConverter mSqlConverter;
}
