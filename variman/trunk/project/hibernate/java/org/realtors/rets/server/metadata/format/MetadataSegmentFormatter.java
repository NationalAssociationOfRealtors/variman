/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.ForeignKey;

public class MetadataSegmentFormatter
{
    public MetadataSegmentFormatter(PrintWriter out, int format)
    {
        mOut = out;
        mFormmatters = new HashMap();
        if (format == MetadataFormatter.COMPACT)
        {
            mFormmatters.put(MSystem.class, new CompactSystemFormatter());
            mFormmatters.put(Resource.class, new CompactResourceFormatter());
            mFormmatters.put(MClass.class, new CompactClassFormatter());
            mFormmatters.put(Table.class, new CompactTableFormatter());
            mFormmatters.put(Update.class, new CompactUpdateFormatter());
            mFormmatters.put(UpdateType.class,
                             new CompactUpdateTypeFormatter());
            mFormmatters.put(MObject.class, new CompactObjectFormatter());
            mFormmatters.put(SearchHelp.class,
                             new CompactSearchHelpFormatter());
            mFormmatters.put(EditMask.class, new CompactEditMaskFormatter());
            mFormmatters.put(Lookup.class, new CompactLookupFormatter());
            mFormmatters.put(LookupType.class,
                             new CompactLookupTypeFormatter());
            mFormmatters.put(ValidationLookup.class,
                             new CompactValidationLookupFormatter());
            mFormmatters.put(ValidationLookupType.class,
                             new CompactValidationLookupTypeFormatter());
            mFormmatters.put(ValidationExternal.class,
                             new CompactValidationExternalFormatter());
            mFormmatters.put(ValidationExternalType.class,
                             new CompactValidationExternalTypeFormatter());
            mFormmatters.put(ValidationExpression.class,
                             new CompactValidationExpressionFormatter());
            mFormmatters.put(ForeignKey.class,
                             new CompactForeignKeyFormatter());
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
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
