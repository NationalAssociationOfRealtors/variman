/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxScrolledWindow;
import org.wxwindows.wxWindow;
import org.wxwindows.wxBoxSizer;

import org.realtors.rets.server.admin.TwoColumnGridSizer;

public abstract class AbstractSubPanel extends wxScrolledWindow
{
    protected AbstractSubPanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);

        addContent(grid);

        box.Add(grid, 1, wxEXPAND | wxALL, 5);
        SetSizer(box);
        SetScrollRate(0, 10);
    }

    protected abstract void addContent(TwoColumnGridSizer grid);
}
