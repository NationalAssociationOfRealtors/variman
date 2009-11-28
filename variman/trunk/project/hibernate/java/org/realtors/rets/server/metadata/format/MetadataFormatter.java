/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;

import org.realtors.rets.common.metadata.MetaObject;

public abstract class MetadataFormatter
{
    public static final int COMPACT = 0;
    public static final int STANDARD = 1;

    public static void setDefaultFormat(int format)
    {
        sDefaultFormat = format;
    }

    public static int getDefaultFormat()
    {
        return sDefaultFormat;
    }

    public abstract void format(FormatterContext context,
                                Collection<MetaObject> data,
                                String[] levels);

    public static final int RESOURCE_LEVEL = 0;
    public static final int CLASS_LEVEL = 1;
    public static final int LOOKUP_LEVEL = 1;
    public static final int VALIDATION_LOOKUP_LEVEL = 1;
    public static final int VALIDATION_EXTERNAL_LEVEL = 1;
    public static final int UPDATE_LEVEL = 2;

    private static int sDefaultFormat = COMPACT;
}
