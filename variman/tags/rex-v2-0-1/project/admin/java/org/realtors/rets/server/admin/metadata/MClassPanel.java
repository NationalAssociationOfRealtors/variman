/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxChoice;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.ClassStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;

public class MClassPanel extends AbstractSubPanel
{
    public MClassPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mClassName = new wxTextCtrl(this, -1);
        grid.addRow("Class Name:", mClassName, wxEXPAND);

        mStandardName = createStandardName();
        grid.addRow("Standard Name:", mStandardName);

        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);
    }

    private wxChoice createStandardName()
    {
        String[] standardNames = {
            "<none>",
            ClassStandardNameEnum.COMMON_INTEREST.toString(),
            ClassStandardNameEnum.LOTS_AND_LAND.toString(),
            ClassStandardNameEnum.MULTI_FAMILY.toString(),
            ClassStandardNameEnum.RESIDENTIAL.toString()
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            standardNames);
    }

    public void updateFrom(MClass clazz)
    {
        setValue(mClassName, clazz.getClassName());
        setValue(mStandardName, clazz.getStandardName());
        setValue(mVisibleName, clazz.getVisibleName());
    }

    private wxTextCtrl mClassName;
    private wxChoice mStandardName;
    private wxTextCtrl mVisibleName;
}
