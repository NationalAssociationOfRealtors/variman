/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.MetadataSegmentVisitor;
import org.realtors.rets.server.metadata.ServerMetadata;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationExpression;

import org.apache.log4j.Logger;

public class FormattingVisitor
{
    public FormattingVisitor(PrintWriter out, int format)
    {
        mOut = out;
        mFormmatters = new HashMap();
        if (format == MetadataFormatter.COMPACT)
        {
            mFormmatters.put(MSystem.class,
                             SystemFormatter.getInstance(format));
            mFormmatters.put(Resource.class,
                             ResourceFormatter.getInstance(format));
            mFormmatters.put(MClass.class,
                             ClassFormatter.getInstance(format));
            mFormmatters.put(Table.class,
                             TableFormatter.getInstance(format));
            mFormmatters.put(Update.class,
                             UpdateFormatter.getInstance(format));
            mFormmatters.put(UpdateType.class,
                             UpdateTypeFormatter.getInstance(format));
            mFormmatters.put(MObject.class,
                             ObjectFormatter.getInstance(format));
            mFormmatters.put(SearchHelp.class,
                             SearchHelpFormatter.getInstance(format));
            mFormmatters.put(EditMask.class,
                             EditMaskFormatter.getInstance(format));
            mFormmatters.put(Lookup.class,
                             LookupFormatter.getInstance(format));
            mFormmatters.put(LookupType.class,
                             LookupTypeFormatter.getInstance(format));
            mFormmatters.put(ValidationLookup.class,
                             ValidationLookupFormatter.getInstance(format));
            mFormmatters.put(
                ValidationLookupType.class,
                ValidationLookupTypeFormatter.getInstance(format));
            mFormmatters.put(ValidationExternal.class,
                             ValidationExternalFormatter.getInstance(format));
            mFormmatters.put(
                ValidationExternalType.class,
                ValidationExternalTypeFormatter.getInstance(format));
            mFormmatters.put(
                ValidationExpression.class,
                ValidationExpressionFormatter.getInstance(format));
        }
    }

    public MetadataFormatter getFormatter(Class clazz)
    {
        return (MetadataFormatter) mFormmatters.get(clazz);
    }

    public void format(MetadataSegment segment)
    {
        List dataList = segment.getDataList();
        if (dataList.size() > 0)
        {
            // Use the first element as the type for *all* elements. This
            // assumes that all elements are of the same type
            MetadataFormatter formatter =
                getFormatter(dataList.get(0).getClass());
            formatter.setVersion(segment.getVersion(), segment.getDate());
            formatter.setLevels(segment.getLevels());
            formatter.format(mOut, segment.getDataList());
        }
    }

    protected PrintWriter mOut;
    private Map mFormmatters;
}
