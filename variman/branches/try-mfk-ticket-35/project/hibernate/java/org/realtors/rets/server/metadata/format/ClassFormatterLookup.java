/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

/**
 * An implementation of FormatterLookup that uses the Class of the first
 * element of the collection.
 */
public class ClassFormatterLookup implements FormatterLookup
{
    public ClassFormatterLookup(int format)
    {
        // TODO: Create two pre-defined formatters Maps for each format. Then
        // choose based on format. No reason to create new MetadataFormatter
        // objects each time. (First, confirm that MetadataFormatters are
        // stateless.)
        mFormatters = new HashMap<Class, MetadataFormatter>();
        if (format == MetadataFormatter.COMPACT)
        {
            mFormatters.put(MSystem.class, new CompactSystemFormatter());
            mFormatters.put(MResource.class, new CompactResourceFormatter());
            mFormatters.put(MClass.class, new CompactClassFormatter());
            mFormatters.put(MTable.class, new CompactTableFormatter());
            mFormatters.put(MUpdate.class, new CompactUpdateFormatter());
            mFormatters.put(MUpdateType.class,
                             new CompactUpdateTypeFormatter());
            mFormatters.put(MObject.class, new CompactObjectFormatter());
            mFormatters.put(MSearchHelp.class,
                             new CompactSearchHelpFormatter());
            mFormatters.put(MEditMask.class, new CompactEditMaskFormatter());
            mFormatters.put(MLookup.class, new CompactLookupFormatter());
            mFormatters.put(MUpdateHelp.class,
                             new CompactUpdateHelpFormatter());
            mFormatters.put(MLookupType.class,
                             new CompactLookupTypeFormatter());
            mFormatters.put(MValidationLookup.class,
                             new CompactValidationLookupFormatter());
            mFormatters.put(MValidationLookupType.class,
                             new CompactValidationLookupTypeFormatter());
            mFormatters.put(MValidationExternal.class,
                             new CompactValidationExternalFormatter());
            mFormatters.put(MValidationExternalType.class,
                             new CompactValidationExternalTypeFormatter());
            mFormatters.put(MValidationExpression.class,
                             new CompactValidationExpressionFormatter());
            mFormatters.put(MForeignKey.class,
                             new CompactForeignKeyFormatter());
        }
        else if (format == MetadataFormatter.STANDARD)
        {
            mFormatters.put(MSystem.class, new StandardSystemFormatter());
            mFormatters.put(MResource.class, new StandardResourceFormatter());
            mFormatters.put(MClass.class, new StandardClassFormatter());
            mFormatters.put(MTable.class, new StandardTableFormatter());
            mFormatters.put(MUpdate.class, new StandardUpdateFormatter());
            mFormatters.put(MUpdateType.class,
                             new StandardUpdateTypeFormatter());
            mFormatters.put(MObject.class, new StandardObjectFormatter());
            mFormatters.put(MSearchHelp.class,
                             new StandardSearchHelpFormatter());
            mFormatters.put(MEditMask.class, new StandardEditMaskFormatter());
            mFormatters.put(MLookup.class, new StandardLookupFormatter());
            mFormatters.put(MUpdateHelp.class,
                             new StandardUpdateHelpFormatter());
            mFormatters.put(MLookupType.class,
                             new StandardLookupTypeFormatter());
            mFormatters.put(MValidationLookup.class,
                             new StandardValidationLookupFormatter());
            mFormatters.put(MValidationLookupType.class,
                             new StandardValidationLookupTypeFormatter());
            mFormatters.put(MValidationExternal.class,
                             new StandardValidationExternalFormatter());
            mFormatters.put(MValidationExternalType.class,
                             new StandardValidationExternalTypeFormatter());
            mFormatters.put(MValidationExpression.class,
                             new StandardValidationExpressionFormatter());
            mFormatters.put(MForeignKey.class,
                             new StandardForeignKeyFormatter());
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    /**
     * Lookup a formatter from a class.  Package scope for testing
     *
     * @param clazz Class to lookup
     * @return formatter for the class, or <code>null</code>
     */
    MetadataFormatter lookupFormatter(Class clazz)
    {
        MetadataFormatter metadataFormatter = recursivelyLookupFormatter(clazz);
        return metadataFormatter;
    }

    public MetadataFormatter lookupFormatter(Collection<MetaObject> metadataCollection)
    {
        Iterator<MetaObject> i = metadataCollection.iterator();
        if (i.hasNext())
        {
            MetaObject metadata = i.next();
            Class clazz = metadata.getClass();
            MetadataFormatter metadataFormatter = recursivelyLookupFormatter(clazz);
            return metadataFormatter;
        }
        else
        {
            return NULL_FORMATTER;
        }
    }

    /*
     * Searches for an appropriate formatter for the specified class. The
     * appropriate formatter is based off the specified class or one of its
     * super classes. This allows the appropriate formatter to be found when
     * metadata classes are subclassed.
     */
    private MetadataFormatter recursivelyLookupFormatter(Class clazz)
    {
        MetadataFormatter metadataFormatter = null;
        if (clazz != null) {
            metadataFormatter = mFormatters.get(clazz);
            if (metadataFormatter == null) {
                Class superClazz = clazz.getSuperclass();
                metadataFormatter = recursivelyLookupFormatter(superClazz);
            }
        }
        return metadataFormatter;
    }

    private Map<Class, MetadataFormatter> mFormatters;
    private static final MetadataFormatter NULL_FORMATTER =
        new NullMetadataFormatter();
}
