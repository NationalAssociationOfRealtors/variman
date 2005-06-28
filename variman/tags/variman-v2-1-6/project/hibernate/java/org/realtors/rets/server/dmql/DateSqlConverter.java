package org.realtors.rets.server.dmql;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.exception.NestableRuntimeException;

import org.realtors.rets.server.Util;

public class DateSqlConverter implements SqlConverter
{
    public DateSqlConverter()
    {
        mDate = null;
    }

    public DateSqlConverter(String date)
    {
        try
        {
            mDate = sFromDmql.parse(date);
        }
        catch (ParseException e)
        {
            throw new NestableRuntimeException(e);
        }
    }

    public void toSql(PrintWriter out)
    {
        out.print(sToSql.format(getDateForFormatting()));
    }

    private Date getDateForFormatting()
    {
        if (mDate == null)
        {
            return new Date();
        }
        else
        {
            return mDate;
        }
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mDate)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof DateSqlConverter))
        {
            return false;
        }
        DateSqlConverter rhs = (DateSqlConverter) obj;
        return new EqualsBuilder()
            .append(mDate, rhs.mDate)
            .isEquals();
    }

    private static final SimpleDateFormat sFromDmql =
        new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sToSql =
        new SimpleDateFormat("''yyyy-MM-dd''");
    private Date mDate;
}
