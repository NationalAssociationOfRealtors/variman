/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.util.List;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxJUtil;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxPanel;
import org.wxwindows.wxSplitterWindow;
import org.wxwindows.wxTreeCtrl;
import org.wxwindows.wxTreeEvent;
import org.wxwindows.wxTreeItemId;
import org.wxwindows.wxTreeListener;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;

import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.admin.metadata.DetailPanel;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataLoader;
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
import org.realtors.rets.server.metadata.MetadataManager;

public class MetadataPanel extends wxPanel
{
    public MetadataPanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxSplitterWindow splitter = new wxSplitterWindow(this, -1);
        mTree = new wxTreeCtrl(splitter, METADATA_TREE, wxDefaultPosition,
                               wxDefaultSize, wxTR_DEFAULT_STYLE);

        mMetadataManager = new MetadataManager();
        mDetailPanel = new DetailPanel(splitter, mMetadataManager);
        splitter.SplitVertically(mTree, mDetailPanel, 300);

        box.Add(splitter, 1, wxEXPAND);
        SetSizer(box);

        EVT_TREE_SEL_CHANGED(METADATA_TREE, new OnSelectionChanged());
        mNodeTextVisitor = new NodeTextVisitor();
    }

    public void populateTree()
    {
        wxBeginBusyCursor();
        final wxWindowDisabler disabler = new wxWindowDisabler();
        mTree.DeleteAllItems();
        mDetailPanel.switchToPanelFor(null);
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                try
                {
                    String metadataDir = Admin.getRetsConfig().getMetadataDir();
                    metadataDir =
                        IOUtils.resolve(Admin.getWebAppRoot(), metadataDir);
                    MetadataLoader loader = new MetadataLoader();
                    return loader.parseMetadataDirectory(metadataDir);
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    wxJUtil.logError("Could not load metadata.", t);
                    return null;
                }
            }

            public void finished()
            {
                MSystem system = (MSystem) get();
                if (system != null)
                {
                    wxTreeItemId root = mTree.AddRoot(getNodeText(system));
                    mTree.SetJData(root, system);
                    recursivelyAddMetadata(root, system);
                    mTree.UnselectAll();
                    mMetadataManager.clear();
                    mMetadataManager.addRecursive(system);
                }
                disabler.delete();
                wxEndBusyCursor();
            }
        };
        worker.start();
    }

    private void recursivelyAddMetadata(wxTreeItemId node,
                                        ServerMetadata metadata)
    {
        List children = metadata.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            ServerMetadata[] child = (ServerMetadata[]) children.get(i);
            for (int j = 0; j < child.length; j++)
            {
                ServerMetadata childMetadata = child[j];
                String text = getNodeText(childMetadata);
                wxTreeItemId childNode = mTree.AppendItem(node, text);
                mTree.SetJData(childNode, childMetadata);
                recursivelyAddMetadata(childNode, childMetadata);
            }
        }
    }

    private String getNodeText(ServerMetadata metadata)
    {
        String nodeText = (String) metadata.accept(mNodeTextVisitor);
        return nodeText == null ? "" : nodeText;
    }

    private class OnSelectionChanged implements wxTreeListener
    {
        public void handleEvent(wxTreeEvent event)
        {
            wxTreeItemId selection = event.GetItem();
            ServerMetadata metadata =
                (ServerMetadata) mTree.GetJData(selection);
            mDetailPanel.switchToPanelFor(metadata);
        }
    }

    private static class NodeTextVisitor implements MetadataVisitor
    {
        public Object visit(MSystem system)
        {
            // There can be only one...
            return "System";
        }

        public Object visit(Resource resource)
        {
            return "R: " + resource.getVisibleName();
        }

        public Object visit(MClass clazz)
        {
            return "C: " + clazz.getVisibleName();
        }

        public Object visit(Table table)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("T: ").append(table.getLongName());
            buffer.append(" (").append(table.getSystemName()).append(")");
            return buffer.toString();
        }

        public Object visit(Update update)
        {
            return "U: " + update.getUpdateName();
        }

        public Object visit(UpdateType updateType)
        {
            StringBuffer buffer = new StringBuffer();
            Table table = updateType.getTable();
            buffer.append("UT: ").append(table.getLongName());
            buffer.append(" (").append(table.getSystemName()).append(")");
            return buffer.toString();
        }

        public Object visit(MObject object)
        {
            return "O: " + object.getVisibleName();
        }

        public Object visit(Lookup lookup)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("L: ").append(lookup.getVisibleName());
            buffer.append(" (").append(lookup.getLookupName()).append(")");
            return buffer.toString();
        }

        public Object visit(LookupType lookupType)
        {
            return "LT: " + lookupType.getLongValue();
        }

        public Object visit(SearchHelp searchHelp)
        {
            return "SH: " + searchHelp.getSearchHelpID();
        }

        public Object visit(EditMask editMask)
        {
            return "EM: " + editMask.getEditMaskID();
        }

        public Object visit(UpdateHelp updateHelp)
        {
            return "UH: " + updateHelp.getUpdateHelpID();
        }

        public Object visit(ValidationLookup validationLookup)
        {
            return "VL: " + validationLookup.getValidationLookupName();
        }

        public Object visit(ValidationLookupType validationLookupType)
        {
            return "VLT: " + validationLookupType.getValidText();
        }

        public Object visit(ValidationExternal validationExternal)
        {
            return "VE: " + validationExternal.getValidationExternalName();
        }

        public Object visit(ValidationExternalType validationExternalType)
        {
            return "VET: " + validationExternalType.getTableName();
        }

        public Object visit(ValidationExpression validationExpression)
        {
            return "VEXP: " + validationExpression.getValidationExpressionID();
        }

        public Object visit(ForeignKey foreignKey)
        {
            return "FK: " + foreignKey.getForeignKeyID();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataPanel.class);
    private static final int METADATA_TREE = wxNewId();
    private wxTreeCtrl mTree;
    private NodeTextVisitor mNodeTextVisitor;
    private DetailPanel mDetailPanel;
    private MetadataManager mMetadataManager;
}
