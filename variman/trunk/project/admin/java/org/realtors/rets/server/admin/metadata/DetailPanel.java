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
import org.wxwindows.wxScrolledWindow;
import org.wxwindows.wxSize;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataVisitor;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.ServerMetadata;

/**
 * A panel that provides detailed and editable information for a metadata
 * object. This is really a collection of sub-panels. Only one of these
 * sub-panels is shown at a time.  The others are hidden.
 */
public class DetailPanel extends wxPanel
{
    public DetailPanel(wxWindow parent)
    {
        super(parent);
        mDetailSizer = new wxBoxSizer(wxVERTICAL);
        mCurrentDetailPanel = null;

        mSystemPanel = new MSystemPanel(this);
        addDetailPanel(mSystemPanel);

        mResourcePanel = new ResourcePanel(this);
        addDetailPanel(mResourcePanel);

        mClassPanel = new MClassPanel(this);
        addDetailPanel(mClassPanel);

        mTablePanel = new TablePanel(this);
        addDetailPanel(mTablePanel);

        SetSizer(mDetailSizer);

        mDetailPanelVisitor = new DetailPanelVisitor();
    }

    private void addDetailPanel(wxWindow window)
    {
        mDetailSizer.Add(window, 1, wxEXPAND);
        mDetailSizer.Show(window, false);
    }

    private void showDetailPanel(wxWindow window)
    {
        if (mCurrentDetailPanel != null)
        {
            mDetailSizer.Show(mCurrentDetailPanel, false);
        }
        if (window != null)
        {
            mDetailSizer.Show(window, true);
        }
        mDetailSizer.Layout();
        mCurrentDetailPanel = window;
    }

    /**
     * Switches to the correct panel for the type of metadata that's passed
     * in.
     *
     * @param metadata The metadata used to determine the panel
     */
    public void switchToPanelFor(ServerMetadata metadata)
    {
        wxPanel detailPanel = null;
        if (metadata != null)
        {
            detailPanel = (wxPanel) metadata.accept(mDetailPanelVisitor);
        }
        showDetailPanel(detailPanel);
    }

    /**
     * Visitor that returns the correct detail panel based on the metadata
     * type.
     */
    private class DetailPanelVisitor implements MetadataVisitor
    {
        public Object visit(MClass clazz)
        {
            mClassPanel.updateFrom(clazz);
            return mClassPanel;
        }

        public Object visit(EditMask editMask)
        {
            return null;
        }

        public Object visit(ForeignKey foreignKey)
        {
            return null;
        }

        public Object visit(Lookup lookup)
        {
            return null;
        }

        public Object visit(LookupType lookupType)
        {
            return null;
        }

        public Object visit(MObject object)
        {
            return null;
        }

        public Object visit(Resource resource)
        {
            mResourcePanel.populateFrom(resource);
            return mResourcePanel;
        }

        public Object visit(SearchHelp searchHelp)
        {
            return null;
        }

        public Object visit(MSystem system)
        {
            mSystemPanel.populateFrom(system);
            return mSystemPanel;
        }

        public Object visit(Table table)
        {
            mTablePanel.setModel(table);
            return mTablePanel;
        }

        public Object visit(Update update)
        {
            return null;
        }

        public Object visit(UpdateHelp updateHelp)
        {
            return null;
        }

        public Object visit(UpdateType updateType)
        {
            return null;
        }

        public Object visit(ValidationExpression validationExpression)
        {
            return null;
        }

        public Object visit(ValidationExternal validationExternal)
        {
            return null;
        }

        public Object visit(ValidationExternalType validationExternalType)
        {
            return null;
        }

        public Object visit(ValidationLookup validationLookup)
        {
            return null;
        }

        public Object visit(ValidationLookupType validationLookupType)
        {
            return null;
        }
    }

    private wxBoxSizer mDetailSizer;
    private wxWindow mCurrentDetailPanel;
    private MSystemPanel mSystemPanel;
    private ResourcePanel mResourcePanel;
    private MClassPanel mClassPanel;
    private DetailPanelVisitor mDetailPanelVisitor;
    private TablePanel mTablePanel;
}
