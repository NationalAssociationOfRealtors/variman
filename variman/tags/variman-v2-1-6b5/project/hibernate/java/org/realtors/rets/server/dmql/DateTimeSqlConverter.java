package org.realtors.rets.server.dmql;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.realtors.rets.server.Util;

public class DateTimeSqlConverter implements SqlConverter
{
    public DateTimeSqlConverter()
    {
        mDate = null;
    }

    public DateTimeSqlConverter(String dateTime)
    {
        try
        {
            mDate = sFromDmql.parse(dateTime);
        }
        catch (ParseException e)
        {
            try
            {
                mDate = sFromDmqlNoMilliseconds.parse(dateTime);
            }
            catch (ParseException e1)
            {
                throw new NestableRuntimeException(e);
            }
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
        if (!(obj instanceof DateTimeSqlConverter))
        {
            return false;
        }
        DateTimeSqlConverter rhs = (DateTimeSqlConverter) obj;
        return new EqualsBuilder()
            .append(mDate, rhs.mDate)
            .isEquals();
    }

    private static final SimpleDateFormat sFromDmql =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final SimpleDateFormat sFromDmqlNoMilliseconds =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat sToSql =
        new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");
    
    private Date mDate;
}
