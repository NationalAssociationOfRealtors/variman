/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxSizer;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;

/**
 * A flex grid sizer used for two column layouts, where the left column is
 * text. The right column may be any widget.
 */
public class TwoColumnGridSizer extends wxFlexGridSizer
{
    public TwoColumnGridSizer(wxWindow parent)
    {
        super(0, 3, 0, 0);
        mParent = parent;
        AddGrowableCol(2);
        mNumberAdded = 0;
    }

    public void addRow(String text, wxWindow window, int options)
    {
        int paddingOptions = 0;
        if (mNumberAdded > 0)
        {
            paddingOptions = wxTOP;
        }
        wxStaticText label = new wxStaticText(mParent, -1, text);
        Add(label, 0, wxALIGN_LEFT | wxALIGN_CENTER_VERTICAL | paddingOptions,
            PADDING);
        Add(DEFAULT_SPACER_WIDTH, -1);
        Add(window, 0, paddingOptions | options, PADDING);
        mNumberAdded++;
    }

    public void addRow(String text, wxWindow window)
    {
        addRow(text, window, 0);
    }

    public void addRow(String text, wxSizer sizer, int options)
    {
        int paddingOptions = 0;
        if (mNumberAdded > 0)
        {
            paddingOptions = wxTOP;
        }
        wxStaticText label = new wxStaticText(mParent, -1, text);
        Add(label, 0, wxALIGN_LEFT | wxALIGN_CENTER_VERTICAL | paddingOptions,
            PADDING);
        Add(DEFAULT_SPACER_WIDTH, -1);
        Add(sizer, 0, paddingOptions | options, PADDING);
        mNumberAdded++;
    }

    public void add(String text, wxSizer sizer)
    {
        addRow(text, sizer, 0);
    }

    public static final int PADDING = 5;
    public static final int DEFAULT_SPACER_WIDTH = 5;
    private int mNumberAdded;
    private wxWindow mParent;
}
