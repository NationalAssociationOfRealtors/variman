/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import java.util.List;

import org.wxwindows.wxChoice;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.admin.BooleanChoice;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.AlignmentEnum;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.UnitEnum;

public class TablePanel extends AbstractSubPanel
{
    public TablePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
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

        mMaximumLength = createNumericTextCtrl();
        grid.addRow("Max Length:", mMaximumLength, wxEXPAND);

        mDataType = createDataTypeChoice();
        grid.addRow("Data Type:", mDataType);

        mPrecision = new wxTextCtrl(this, -1);
        grid.addRow("Precision:", mPrecision, wxEXPAND);

        mSearchable = new BooleanChoice(this, -1);
        grid.addRow("Searchable:", mSearchable);

        mInterpretation = createInterpretationChoice();
        grid.addRow("Interpretation:", mInterpretation);

        mAlignment = createAlignmentChoice();
        grid.addRow("Alignment:", mAlignment);

        mUseSeparator = new BooleanChoice(this, -1);
        grid.addRow("Use Separator:", mUseSeparator);

        mMaxSelect = new wxTextCtrl(this, -1);
        grid.addRow("Max Select:", mMaxSelect, wxEXPAND);

        mUnits = createUnitsChoice();
        grid.addRow("Units:", mUnits);

        mIndex = new wxTextCtrl(this, -1);
        grid.addRow("Index:", mIndex, wxEXPAND);

        mMinimum = new wxTextCtrl(this, -1);
        grid.addRow("Minimum:", mMinimum, wxEXPAND);

        mMaximum = new wxTextCtrl(this, -1);
        grid.addRow("Maximum:", mMaximum, wxEXPAND);

        mDefault = new wxTextCtrl(this, -1);
        grid.addRow("Default:", mDefault, wxEXPAND);

        mRequired = new wxTextCtrl(this, -1);
        grid.addRow("Required:", mRequired, wxEXPAND);

        mUnique = new BooleanChoice(this, -1);
        grid.addRow("Unique:", mUnique);

        // Todo: TablePanel.TablePanel EditMasks

        mLookup = new wxChoice(this, -1);
        grid.addRow("Lookup:", mLookup, wxEXPAND);

        mSearchHelp = new wxChoice(this, -1);
        grid.addRow("Search Help:", mSearchHelp, wxEXPAND);
    }

    private wxChoice createDataTypeChoice()
    {
        String[] choices = {
            DataTypeEnum.BOOLEAN.toString(),
            DataTypeEnum.CHARACTER.toString(),
            DataTypeEnum.DATE.toString(),
            DataTypeEnum.DATETIME.toString(),
            DataTypeEnum.DECIMAL.toString(),
            DataTypeEnum.INT.toString(),
            DataTypeEnum.LONG.toString(),
            DataTypeEnum.SMALL.toString(),
            DataTypeEnum.TIME.toString(),
            DataTypeEnum.TINY.toString(),
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            choices);
    }

    private wxChoice createInterpretationChoice()
    {
        String[] choices = {
            "<none>",
            InterpretationEnum.CURRENCY.toString(),
            InterpretationEnum.LOOKUP.toString(),
            InterpretationEnum.LOOKUPBITMASK.toString(),
            InterpretationEnum.LOOKUPBITSTRING.toString(),
            InterpretationEnum.LOOKUPMULTI.toString(),
            InterpretationEnum.NUMBER.toString()
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            choices);
    }

    private wxChoice createAlignmentChoice()
    {
        String[] choices = {
            AlignmentEnum.LEFT.toString(),
            AlignmentEnum.RIGHT.toString(),
            AlignmentEnum.CENTER.toString(),
            AlignmentEnum.JUSTIFY.toString()
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            choices);
    }

    private wxChoice createUnitsChoice()
    {
        String[] choices = {
            "<none>",
            UnitEnum.ACRES.toString(),
            UnitEnum.FEET.toString(),
            UnitEnum.HECTARES.toString(),
            UnitEnum.METERS.toString(),
            UnitEnum.SQFT.toString(),
            UnitEnum.SQMETERS.toString()
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            choices);
    }

    public void setModel(Table table)
    {
        String path = table.getMClass().getResource().getPath();
        findLookups(path);
        findSeachHelps(path);

        setValue(mSystemName, table.getSystemName());
        setValue(mStandardName, table.getStandardName());
        setValue(mLongName, table.getLongName());
        setValue(mDbName, table.getDbName());
        setValue(mMaximumLength, table.getMaximumLength());
        setValue(mDataType, table.getDataType());
        setValue(mPrecision, table.getPrecision());
        setValue(mSearchable, table.isSearchable());
        setValue(mInterpretation, table.getInterpretation());
        setValue(mAlignment, table.getAlignment());
        setValue(mUseSeparator, table.isUseSeparator());
        setValue(mUnits, table.getUnits());
        setValue(mIndex, table.getIndex());
        setValue(mMinimum, table.getMinimum());
        setValue(mMaximum, table.getMaximum());
        setValue(mDefault, table.getDefault());
        setValue(mRequired, table.getRequired());
        setValue(mUnique, table.isUnique());
        setValue(mLookup, table.getLookup());
        setValue(mSearchHelp,  table.getSearchHelp());
    }

    private void findLookups(String path)
    {
        mLookups = mManager.find(Lookup.TABLE, path);
        mLookup.Clear();
        mLookup.Append("<none>");
        for (int i = 0; i < mLookups.size(); i++)
        {
            Lookup lookup = (Lookup) mLookups.get(i);
            StringBuffer buffer = new StringBuffer();
            buffer.append(lookup.getVisibleName());
            buffer.append(" (").append(lookup.getLookupName()).append(")");
            mLookup.Append(buffer.toString(), lookup);
        }
    }

    private void findSeachHelps(String path)
    {
        mSearchHelps = mManager.find(SearchHelp.TABLE, path);
        mSearchHelp.Clear();
        mSearchHelp.Append("<none>");
        for (int i = 0; i < mSearchHelps.size(); i++)
        {
            SearchHelp searchHelp = (SearchHelp) mSearchHelps.get(i);
            mSearchHelp.Append(searchHelp.getSearchHelpID(), searchHelp);
        }
    }

    private void findEditMasks(String path)
    {
        mEditMasks = mManager.find(EditMask.TABLE, path);
    }

    private void setValue(wxChoice choice, Lookup lookup)
    {
        int index = mLookups.indexOf(lookup);
        if (index == -1)
        {
            choice.SetSelection(0);
        }
        else
        {
            choice.SetSelection(index + 1);
        }
    }

    private void setValue(wxChoice choice, SearchHelp searchHelp)
    {
        int index = mSearchHelps.indexOf(searchHelp);
        if (index == -1)
        {
            choice.SetSelection(0);
        }
        else
        {
            choice.SetSelection(index + 1);
        }
    }

    public void setManager(MetadataManager manager)
    {
        mManager = manager;
    }

    private wxTextCtrl mSystemName;
    private wxTextCtrl mStandardName;
    private wxTextCtrl mLongName;
    private wxTextCtrl mShortName;
    private wxTextCtrl mDbName;
    private wxTextCtrl mMaximumLength;
    private wxChoice mDataType;
    private wxTextCtrl mPrecision;
    private BooleanChoice mSearchable;
    private wxChoice mInterpretation;
    private wxChoice mAlignment;
    private BooleanChoice mUseSeparator;
    private wxTextCtrl mMaxSelect;
    private wxChoice mUnits;
    private wxTextCtrl mIndex;
    private wxTextCtrl mMinimum;
    private wxTextCtrl mMaximum;
    private wxTextCtrl mDefault;
    private wxTextCtrl mRequired;
    private BooleanChoice mUnique;
    private wxChoice mLookup;
    private wxChoice mSearchHelp;
    private MetadataManager mManager;
    private List mLookups;
    private List mSearchHelps;
    private List mEditMasks;
}
