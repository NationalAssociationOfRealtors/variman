/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxScrolledWindow;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.Table;

public class TablePanel extends wxScrolledWindow
{
    public TablePanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);

        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);
        mSystemName = new wxTextCtrl(this, -1);
        grid.addRow("System Name:", mSystemName, wxEXPAND);

        mStandardName = new wxTextCtrl(this, -1);
        grid.addRow("Standard Name:", mStandardName, wxEXPAND);

        mLongName = new wxTextCtrl(this, -1);
        grid.addRow("Long Name:", mLongName, wxEXPAND);

        mShortName = new wxTextCtrl(this, -1);
        grid.addRow("Short Name:", mShortName, wxEXPAND);

        mDbName = new wxTextCtrl(this, -1);
        grid.addRow("DB Name:", mDbName, wxEXPAND);

        mMaximumLength = new wxTextCtrl(this, -1);
        grid.addRow("Max Length:", mMaximumLength, wxEXPAND);

        mDataType = new wxTextCtrl(this, -1);
        grid.addRow("Data Type:", mDataType, wxEXPAND);

        mPrecision = new wxTextCtrl(this, -1);
        grid.addRow("Precision:", mPrecision, wxEXPAND);

        mSearchable = new wxTextCtrl(this, -1);
        grid.addRow("Searchable:", mSearchable, wxEXPAND);

        mInterpretation = new wxTextCtrl(this, -1);
        grid.addRow("Interpretation:", mInterpretation, wxEXPAND);

        mAlignment = new wxTextCtrl(this, -1);
        grid.addRow("Alignment:", mAlignment, wxEXPAND);

        mUseSeparator = new wxTextCtrl(this, -1);
        grid.addRow("Use Separator:", mUseSeparator, wxEXPAND);

        mMaxSelect = new wxTextCtrl(this, -1);
        grid.addRow("Max Select:", mMaxSelect, wxEXPAND);

        mUnits = new wxTextCtrl(this, -1);
        grid.addRow("Units:", mUnits, wxEXPAND);

        mIndex = new wxTextCtrl(this, -1);
        grid.addRow("Index:", mIndex, wxEXPAND);

        mMinimum = new wxTextCtrl(this, -1);
        grid.addRow("Minimum:", mMinimum, wxEXPAND);

        mMaximum = new wxTextCtrl(this, -1);
        grid.addRow("Maximum:", mMaximum, wxEXPAND);

        box.Add(grid, 1, wxEXPAND | wxALL, 5);
        SetSizer(box);
        SetScrollRate(0, 10);
    }

    public void setModel(Table table)
    {
        AdminUtils.setValue(mSystemName, table.getSystemName());
        AdminUtils.setValue(mStandardName, table.getStandardName());
        AdminUtils.setValue(mLongName, table.getLongName());
        AdminUtils.setValue(mShortName, table.getShortName());
        AdminUtils.setValue(mDbName, table.getDbName());
        AdminUtils.setValue(mMaximumLength, table.getMaximumLength());
        AdminUtils.setValue(mDataType, table.getDataType());
        AdminUtils.setValue(mPrecision, table.getPrecision());
        AdminUtils.setValue(mSearchable, table.isSearchable());
        AdminUtils.setValue(mInterpretation, table.getInterpretation());
        AdminUtils.setValue(mAlignment, table.getAlignment());
        AdminUtils.setValue(mUseSeparator, table.isUseSeparator());
        AdminUtils.setValue(mUnits, table.getUnits());
        AdminUtils.setValue(mIndex, table.getIndex());
        AdminUtils.setValue(mMinimum, table.getMinimum());
        AdminUtils.setValue(mMaximum, table.getMaximum());
    }

    private wxTextCtrl mSystemName;
    private wxTextCtrl mStandardName;
    private wxTextCtrl mLongName;
    private wxTextCtrl mShortName;
    private wxTextCtrl mDbName;
    private wxTextCtrl mMaximumLength;
    private wxTextCtrl mDataType;
    private wxTextCtrl mPrecision;
    private wxTextCtrl mSearchable;
    private wxTextCtrl mInterpretation;
    private wxTextCtrl mAlignment;
    private wxTextCtrl mUseSeparator;
    private wxTextCtrl mMaxSelect;
    private wxTextCtrl mUnits;
    private wxTextCtrl mIndex;
    private wxTextCtrl mMinimum;
    private wxTextCtrl mMaximum;
}
