/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxPanel;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MetadataVisitor;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.ServerMetadata;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;

/**
 * A panel that provides detailed and editable information for a metadata
 * object. This is really a collection of sub-panels. Only one of these
 * sub-panels is shown at a time.  The others are hidden.
 */
public class DetailPanel extends wxPanel
{
    public DetailPanel(wxWindow parent, MetadataManager manager)
    {
        super(parent);
        mManager = manager;

        mDetailSizer = new wxBoxSizer(wxVERTICAL);
        mCurrentDetailPanel = null;

        mSystemPanel = new MSystemPanel(this);
        addDetailPanel(mSystemPanel);

        mResourcePanel = new ResourcePanel(this);
        addDetailPanel(mResourcePanel);

        mClassPanel = new MClassPanel(this);
        addDetailPanel(mClassPanel);

        mTablePanel = new TablePanel(this);
        mTablePanel.setManager(mManager);
        addDetailPanel(mTablePanel);

        mUpdatePanel = new UpdatePanel(this);
        addDetailPanel(mUpdatePanel);

        mUpdateTypePanel = new UpdateTypePanel(this);
        addDetailPanel(mUpdateTypePanel);

        mObjectPanel = new MObjectPanel(this);
        addDetailPanel(mObjectPanel);

        mSearchHelpPanel = new SearchHelpPanel(this);
        addDetailPanel(mSearchHelpPanel);

        mEditMaskPanel = new EditMaskPanel(this);
        addDetailPanel(mEditMaskPanel);

        mLookupPanel = new LookupPanel(this);
        addDetailPanel(mLookupPanel);

        mLookupTypePanel = new LookupTypePanel(this);
        addDetailPanel(mLookupTypePanel);

        mUpdateHelpPanel = new UpdateHelpPanel(this);
        addDetailPanel(mUpdateHelpPanel);

        mValidationLookupPanel = new ValidationLookupPanel(this);
        addDetailPanel(mValidationLookupPanel);

        mValidationLookupTypePanel = new ValidationLookupTypePanel(this);
        addDetailPanel(mValidationLookupTypePanel);

        mValidationExternalPanel = new ValidationExternalPanel(this);
        addDetailPanel(mValidationExternalPanel);

        mValidationExternalTypePanel = new ValidationExternalTypePanel(this);
        addDetailPanel(mValidationExternalTypePanel);

        mValidationExpressionPanel = new ValidationExpressionPanel(this);
        addDetailPanel(mValidationExpressionPanel);

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
            mEditMaskPanel.setModel(editMask);
            return mEditMaskPanel;
        }

        public Object visit(ForeignKey foreignKey)
        {
            return null;
        }

        public Object visit(Lookup lookup)
        {
            mLookupPanel.setModel(lookup);
            return mLookupPanel;
        }

        public Object visit(LookupType lookupType)
        {
            mLookupTypePanel.setModel(lookupType);
            return mLookupTypePanel;
        }

        public Object visit(MObject object)
        {
            mObjectPanel.setModel(object);
            return mObjectPanel;
        }

        public Object visit(Resource resource)
        {
            mResourcePanel.populateFrom(resource);
            return mResourcePanel;
        }

        public Object visit(SearchHelp searchHelp)
        {
            mSearchHelpPanel.setModel(searchHelp);
            return mSearchHelpPanel;
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
            mUpdatePanel.setModel(update);
            return mUpdatePanel;
        }

        public Object visit(UpdateHelp updateHelp)
        {
            mUpdateHelpPanel.setModel(updateHelp);
            return mUpdateHelpPanel;
        }

        public Object visit(UpdateType updateType)
        {
            mUpdateTypePanel.setModel(updateType);
            return mUpdateTypePanel;
        }

        public Object visit(ValidationExpression validationExpression)
        {
            mValidationExpressionPanel.setModel(validationExpression);
            return mValidationExpressionPanel;
        }

        public Object visit(ValidationExternal validationExternal)
        {
            mValidationExternalPanel.setModel(validationExternal);
            return mValidationExternalPanel;
        }

        public Object visit(ValidationExternalType validationExternalType)
        {
            mValidationExternalTypePanel.setModel(validationExternalType);
            return mValidationExternalTypePanel;
        }

        public Object visit(ValidationLookup validationLookup)
        {
            mValidationLookupPanel.setModel(validationLookup);
            return mValidationLookupPanel;
        }

        public Object visit(ValidationLookupType validationLookupType)
        {
            mValidationLookupTypePanel.setModel(validationLookupType);
            return mValidationLookupTypePanel;
        }
    }

    private wxBoxSizer mDetailSizer;
    private wxWindow mCurrentDetailPanel;
    private MSystemPanel mSystemPanel;
    private ResourcePanel mResourcePanel;
    private MClassPanel mClassPanel;
    private DetailPanelVisitor mDetailPanelVisitor;
    private TablePanel mTablePanel;
    private UpdatePanel mUpdatePanel;
    private UpdateTypePanel mUpdateTypePanel;
    private MObjectPanel mObjectPanel;
    private SearchHelpPanel mSearchHelpPanel;
    private EditMaskPanel mEditMaskPanel;
    private LookupPanel mLookupPanel;
    private LookupTypePanel mLookupTypePanel;
    private UpdateHelpPanel mUpdateHelpPanel;
    private ValidationLookupPanel mValidationLookupPanel;
    private ValidationLookupTypePanel mValidationLookupTypePanel;
    private ValidationExternalPanel mValidationExternalPanel;
    private ValidationExternalTypePanel mValidationExternalTypePanel;
    private ValidationExpressionPanel mValidationExpressionPanel;
    private MetadataManager mManager;
}
