/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

public class FormatterContext
{
    public FormatterContext(String version, Date date, boolean recursive,
                            PrintWriter writer,
                            FormatterLookup lookup)
    {
        mVersion = version;
        mDate = date;
        mRecursive = recursive;
        mWriter = writer;
        mLookup = lookup;
    }

    public String getVersion()
    {
        return mVersion;
    }

    public Date getDate()
    {
        return mDate;
    }

    public boolean isRecursive()
    {
        return mRecursive;
    }

    public PrintWriter getWriter()
    {
        return mWriter;
    }

    public void format(Collection data, String[] levels)
    {
        MetadataFormatter formatter = mLookup.lookupFormatter(data);
        formatter.format(this, data, levels);
    }

    public static final boolean RECURSIVE = true;
    public static final boolean NOT_RECURSIVE = false;

    private String mVersion;
    private Date mDate;
    private boolean mRecursive;
    private PrintWriter mWriter;
    private FormatterLookup mLookup;
}
