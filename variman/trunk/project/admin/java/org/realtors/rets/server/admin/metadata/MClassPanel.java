/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxPanel;
import org.wxwindows.wxWindow;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxTextCtrl;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.metadata.MClass;

public class MClassPanel extends wxPanel
{
    public MClassPanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);

        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);
        mClassName = new wxTextCtrl(this, -1);
        grid.addRow("Class Name:", mClassName, wxEXPAND);
        mStandardName = new wxTextCtrl(this, -1);
        grid.addRow("Standard Name:", mStandardName, wxEXPAND);
        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);

        box.Add(grid, 1, wxEXPAND | wxALL, 5);
        SetSizer(box);
    }

    public void updateFrom(MClass clazz)
    {
        mClassName.SetValue(clazz.getClassName());
        AdminUtils.setValue(mStandardName, clazz.getStandardName());
        mVisibleName.SetValue(clazz.getVisibleName());
    }

    private wxTextCtrl mClassName;
    private wxTextCtrl mStandardName;
    private wxTextCtrl mVisibleName;
}
