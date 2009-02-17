package org.realtors.rets.server.metadata.format;

import java.util.Date;
import java.util.Set;
import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;

import org.realtors.rets.server.protocol.TableGroupFilter;

/**
 * The only point of this class is to promote mutable methods to public access.
 */
public class MutableFormatterContext extends FormatterContext
{
    public MutableFormatterContext()
    {
    }

    public MutableFormatterContext(String version, Date date, boolean recursive,
                                   PrintWriter writer, FormatterLookup lookup,
                                   RetsVersion retsVersion)
    {
        super(version, date, recursive, writer, lookup, retsVersion);
    }

    public void setVersion(String version)
    {
        super.setVersion(version);
    }
    
    public void setRetsVersion(RetsVersion retsVersion)
    {
    	super.setRetsVersion(retsVersion);
    }
    
    public void setDate(Date date)
    {
        super.setDate(date);
    }

    public void setRecursive(boolean recursive)
    {
        super.setRecursive(recursive);
    }

    public void setWriter(PrintWriter writer)
    {
        super.setWriter(writer);
    }

    public void setLookup(FormatterLookup lookup)
    {
        super.setLookup(lookup);
    }

    public void setTableFilter(TableGroupFilter groupFilter, Set groups)
    {
        super.setTableFilter(groupFilter, groups);
    }
}
