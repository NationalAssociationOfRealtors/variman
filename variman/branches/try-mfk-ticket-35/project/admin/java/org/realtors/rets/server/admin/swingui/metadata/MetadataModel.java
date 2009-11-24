package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.*;

import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

import org.realtors.rets.server.admin.swingui.AdminTab;
import org.realtors.rets.server.admin.swingui.SwingUtils;

public class MetadataModel extends AdminTab
{

    public MetadataModel(JMenu metadataMenu)
    {
        super(new BorderLayout());
        
        /*
         * Variman extends METADATA-CLASS with X-DBName. Set that up.
         */
        MClass.addAttribute(MClass.X_DBNAME, MetaObject.sRETSNAME, MetaObject.sREQUIRED);
        
        mMetadataMenu = metadataMenu;

        /*
         *  The windowing components must be built before Tree is populated 
         *  because when the tree selection listener is fired, the components
         *  can't be null.
         */
        mMainPanel = new JPanel(new BorderLayout());
        
        mTreePanel = new MetadataTreePanel();
        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mTreePanel, mMainPanel);
        mSplitPane.setDividerLocation(250);
        
        add(mSplitPane, BorderLayout.CENTER);

        populateTree(mTreePanel);
        //mTreePanel.collapseAll();
        mSplitPane.revalidate();
    }
    
    /**
     * Refresh the content side of the split panel.
     * @param userObject The user object from the TreeNode that is currently selected.
     */
    private void refreshMainPanel(MetaObject userObject)
    {
        mMainPanel.removeAll();
        
        JPanel panel = new JPanel(new SpringLayout());
        int rows = 0;

        for (String attribute : userObject.getAttributeNames())
        {
            AttrType<?> attrType    = userObject.getAttributeType(attribute);
            JLabel label = new JLabel(attribute + ":", SwingConstants.TRAILING);
            
            JLabel val = new JLabel(MetaObject.getNameFromAttribute(attrType));
            
            String required = "";
            if (userObject.isAttributeRequired(attribute))
                required = "Required";
            
            label.setLabelFor(val);
            panel.add(label);
            panel.add(val);
            panel.add(new JLabel(required));
            
            rows++;
        }

        SwingUtils.SpringLayoutGrid(panel, rows, 3, 6, 6, 6, 6);
        mTextScrollPane = new JScrollPane(panel);
        mMainPanel.add(mTextScrollPane, BorderLayout.CENTER);
                                   
        mSplitPane.revalidate();
    }
    
    /**
     * Populate the tree from the metadata standard types.
     */
    private void populateTree(MetadataTreePanel treePanel)
    {
        mForeignKeyNode = treePanel.addObject(null, sFOREIGNKEY);
        mResourceNode = treePanel.addObject(null, sRESOURCE);
        mClassNode = treePanel.addObject(mResourceNode, sCLASS);
        mTableNode = treePanel.addObject(mClassNode, sTABLE);
        mUpdateNode = treePanel.addObject(mClassNode, sUPDATE);
        mUpdateTypeNode = treePanel.addObject(mUpdateNode, sUPDATETYPE);
        mObjectNode = treePanel.addObject(mResourceNode, sOBJECT);
        mSearchHelpNode = treePanel.addObject(mResourceNode, sSEARCHHELP);
        mEditMaskNode = treePanel.addObject(mResourceNode, sEDITMASK);
        mLookupNode = treePanel.addObject(mResourceNode, sLOOKUP);
        mLookupTypeNode = treePanel.addObject(mLookupNode, sLOOKUPTYPE);
        mValidationLookupNode = treePanel.addObject(mResourceNode, sVALIDATIONLOOKUP);
        mValidationLookupTypeNode = treePanel.addObject(mValidationLookupNode, sVALIDATIONLOOKUPTYPE);
        mValidationExternalNode = treePanel.addObject(mResourceNode, sVALIDATIONEXTERNAL);
        mValidationExternalTypeNode = treePanel.addObject(mValidationExternalNode, sVALIDATIONEXTERNALTYPE);
        mValidationExpressionNode = treePanel.addObject(mResourceNode, sVALIDATIONEXPRESSION);
    }
 
    public void tabSelected()
    {
        mMetadataMenu.setEnabled(true);
        
        // FIXME: Remove when implemented.
        JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "This is experimental code that is not completely implemented.\nPlay at your own risk!.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);      
    }

    public void tabDeselected()
    {
        mMetadataMenu.setEnabled(false);
    }

    public void refreshTab()
    {
        mMetadataMenu.setEnabled(true);
    }
   
    class MetadataTreeNode extends DefaultMutableTreeNode
    {
        public MetadataTreeNode(Object o)
        {
            super(o);
        }
        
        public String toString()
        {
            Object node = getUserObject();
            
            if (node instanceof MetaObject)
                return ((MetaObject) node).getId();
            
            return super.toString();
        }
    }

    /**
     * The Metadata Tree used to display a JTree of the metadata. There can
     * be two types of user objects attached to each node in the tree: a String
     * containing a title such as "Resource", "Lookups", "EditMask", etc;  The other
     * type of user node is a MetaObject which references one of the metadata types
     * that the node selected represents. When nodes are added to the tree, the
     * MetaObject associated with that node needs to get added to the global metadata
     * object such that it will get persisted when needed.
     */
    class MetadataTreePanel extends JPanel 
    {
        protected MetadataTreeNode rootNode;
        protected DefaultTreeModel treeModel;
        protected JTree tree;
        private Toolkit toolkit = Toolkit.getDefaultToolkit();

        public MetadataTreePanel() 
        {
            super(new BorderLayout());
            
            /*
             * The root node is "System". 
             */
               rootNode = new MetadataTreeNode(new String("System"));

            treeModel = new DefaultTreeModel(rootNode);
            
            /*
             * Create the tree and the panel containing the Add/Remove buttons.
             */
            tree = new JTree(treeModel);
            tree.setEditable(false);
            tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
            tree.setShowsRootHandles(true);
            
            JScrollPane scrollPane = new JScrollPane(tree);

            scrollPane.setVerticalScrollBarPolicy(
                       ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            add(scrollPane, BorderLayout.CENTER);
            
            JPanel panel = new JPanel();
               
            mAddButton = new JButton("Add");
            mEditButton = new JButton("Edit");
            mDeleteButton = new JButton("Delete");
            
            //panel.add (mAddButton);
            panel.add(mEditButton);
            //panel.add (mDeleteButton);
            add(panel, BorderLayout.SOUTH); 

            /*
             * Add the tree selection listener. The listener is responsible for setting up the 
             * action buttons based on the current state (e.g. node selected).
             */
            tree.addTreeSelectionListener(new TreeSelectionListener() 
            {
                public void valueChanged(TreeSelectionEvent e)
                {
                    final MetadataTreeNode selectedNode = (MetadataTreeNode)tree.getLastSelectedPathComponent();
                    final MetaObject resource;

                    mAddButton.setEnabled(false);
                    mEditButton.setEnabled(false);
                    mDeleteButton.setEnabled(false);

                    if (selectedNode == null)
                        return;
                    
                    if (selectedNode.equals(rootNode))
                    {
                        if (mSystem == null)
                            mSystem = new MSystem();
                        
                        resource = mSystem;
                    }
                    else
                    if (selectedNode.equals(mResourceNode))
                    {
                        if (mResource == null)
                            mResource = new MResource();
                        
                        resource = mResource;
                    }
                    else
                    if (selectedNode.equals(mForeignKeyNode))
                    {
                        if (mForeignKey == null)
                            mForeignKey = new MForeignKey();
                        
                        resource = mForeignKey;
                    }
                    else
                    if (selectedNode.equals(mClassNode))
                    {
                        if (mClass == null)
                            mClass = new MClass();

                        resource = mClass;
                    }
                    else
                    if (selectedNode.equals(mTableNode))
                    {
                        if (mTable == null)
                            mTable = new MTable();
                        
                        resource = mTable;
                    }
                    else
                    if (selectedNode.equals(mUpdateNode))
                    {
                        if (mUpdate == null)
                            mUpdate = new MUpdate();
                        
                        resource = mUpdate;
                    }
                    else
                    if (selectedNode.equals(mUpdateTypeNode))
                    {
                        if (mUpdateType == null)
                            mUpdateType = new MUpdateType();
                        
                        resource = mUpdateType;
                    }
                    else
                    if (selectedNode.equals(mUpdateHelpNode))
                    {
                        if (mUpdateHelp == null)
                            mUpdateHelp = new MUpdateHelp();
                        
                        resource = mUpdateHelp;
                    }
                    else
                    if (selectedNode.equals(mObjectNode))
                    {
                        if (mObject == null)
                            mObject = new MObject();
                        
                        resource = mObject;
                    }
                    else
                    if (selectedNode.equals(mSearchHelpNode))
                    {
                        if (mSearchHelp == null)
                            mSearchHelp = new MSearchHelp();
                        
                        resource = mSearchHelp;
                    }
                    else
                    if(selectedNode.equals(mEditMaskNode))
                    {
                        if (mEditMask == null)
                            mEditMask = new MEditMask();
                        
                        resource = mEditMask;
                    }
                    else
                    if (selectedNode.equals(mLookupNode))
                    {
                        if (mLookup == null)
                            mLookup = new MLookup();
                        
                        resource = mLookup;
                    }
                    else
                    if (selectedNode.equals(mLookupTypeNode))
                    {
                        if (mLookupType == null)
                            mLookupType = new MLookupType();
                        
                        resource = mLookupType;
                    }
                    else
                    if (selectedNode.equals(mValidationLookupNode))
                    {
                        if (mValidationLookup == null)
                            mValidationLookup = new MValidationLookup();
                        
                        resource = mValidationLookup;
                    }
                    else
                    if (selectedNode.equals(mValidationLookupTypeNode))
                    {
                        if (mValidationLookupType == null)
                            mValidationLookupType = new MValidationLookupType();
                        
                        resource = mValidationLookupType;
                    }
                    else
                    if (selectedNode.equals(mValidationExternalNode))
                    {
                        if (mValidationExternal == null)
                            mValidationExternal = new MValidationExternal();
                        
                        resource = mValidationExternal;
                    }
                    else
                    if (selectedNode.equals(mValidationExternalTypeNode))
                    {
                        if (mValidationExternalType == null)
                            mValidationExternalType = new MValidationExternalType();
                        
                        resource = mValidationExternalType;
                    }
                    else
                    if (selectedNode.equals(mValidationExpressionNode))
                    {
                        if (mValidationExpression == null)
                            mValidationExpression = new MValidationExpression();
                        
                        resource = mValidationExpression;
                    }
                    else
                    {
                        mMainPanel.removeAll();
                        mMainPanel.add(new JLabel(""));
                        mSplitPane.revalidate();
                        return;
                    }
                    
                    mEditButton.removeActionListener(mEditButtonListener);
                    mEditButtonListener = new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            /*
                             * Instantiate the dialog
                             */
                            MetadataDialog dialog = new MetadataModelDialog(resource);
                            
                            dialog.setVisible(true);
                            if (dialog.getResponse() == JOptionPane.OK_OPTION)
                            {
                                /*
                                 * Rebuild the proper MetaObject with the new attributes.
                                 */
                                if (resource instanceof MResource)    
                                {
                                    mResource = new MResource();
                                    refreshMainPanel(mResource);
                                }
                                else
                                if (resource instanceof MClass)
                                {
                                    mClass = new MClass();
                                    refreshMainPanel(mClass);
                                }
                                else
                                if (resource instanceof MEditMask)
                                {
                                    mEditMask = new MEditMask();
                                    refreshMainPanel(mEditMask);
                                }
                                else
                                if (resource instanceof MForeignKey)
                                {
                                    mForeignKey = new MForeignKey();
                                    refreshMainPanel(mForeignKey);
                                }
                                else
                                if (resource instanceof MLookup)
                                {
                                    mLookup = new MLookup();
                                    refreshMainPanel(mLookup);
                                }
                                else
                                if (resource instanceof MLookupType)  
                                {
                                    mLookupType = new MLookupType();
                                    refreshMainPanel(mLookupType);
                                }
                                else
                                if (resource instanceof MObject)  
                                {
                                    mObject = new MObject();
                                    refreshMainPanel(mObject);
                                }
                                else
                                if (resource instanceof MSearchHelp)        
                                {
                                    mSearchHelp = new MSearchHelp();
                                    refreshMainPanel(mSearchHelp);
                                }
                                else
                                if (resource instanceof MSystem)    
                                {
                                    mSystem = new MSystem();
                                    refreshMainPanel(mSystem);
                                }
                                else
                                if (resource instanceof MTable)   
                                {
                                    mTable = new MTable();
                                    refreshMainPanel(mTable);
                                }
                                else
                                if (resource instanceof MUpdate)  
                                {
                                    mUpdate = new MUpdate();
                                    refreshMainPanel(mUpdate);
                                }
                                else
                                if (resource instanceof MUpdateHelp) 
                                {
                                    mUpdateHelp = new MUpdateHelp();
                                    refreshMainPanel(mUpdateHelp);
                                }
                                else
                                if (resource instanceof MUpdateType) 
                                {
                                    mUpdateType = new MUpdateType();
                                    refreshMainPanel(mUpdateType);
                                }
                                else
                                if (resource instanceof MValidationExpression)     
                                {
                                    mValidationExpression = new MValidationExpression();
                                    refreshMainPanel(mValidationExpression);
                                }
                                else
                                if (resource instanceof MValidationExternal)    
                                {
                                    mValidationExternal = new MValidationExternal();
                                    refreshMainPanel(mValidationExternal);
                                }
                                else
                                if (resource instanceof MValidationExternalType)      
                                {
                                    mValidationExternalType = new MValidationExternalType();
                                    refreshMainPanel(mValidationExternalType);
                                }
                                else
                                if (resource instanceof MValidationLookup)  
                                {
                                    mValidationLookup = new MValidationLookup();
                                    refreshMainPanel(mValidationLookup);
                                }
                                else
                                if (resource instanceof MValidationLookupType)
                                {
                                    mValidationLookupType = new MValidationLookupType();
                                    refreshMainPanel(mValidationLookupType);
                                }
                            }
                        }
                    };
                    
                    mEditButton.addActionListener(mEditButtonListener);

                    mEditButton.setEnabled(true);
                    
                    refreshMainPanel(resource);
                    mSplitPane.revalidate();
                }
            });
        }

        /** Add child to the currently selected node. */
        public MetadataTreeNode addObject(Object child) 
        {
            MetadataTreeNode parentNode = null;
            TreePath parentPath = tree.getSelectionPath();

            if (parentPath == null) 
            {
                parentNode = rootNode;
            } 
            else 
            {
                parentNode = (MetadataTreeNode) (parentPath.getLastPathComponent());
            }

            return addObject(parentNode, child, true);
        }

        public MetadataTreeNode addObject(MetadataTreeNode parent,
              Object child) 
        {
            return addObject(parent, child, true);
        }
        
        public MetadataTreeNode addObject(MetadataTreeNode parent,
                  Object child, boolean shouldBeVisible) 
        {
            MetadataTreeNode childNode = new MetadataTreeNode(child);

            if (parent == null) 
            {
                parent = rootNode;
            }

               // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
               treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

               // Make sure the user can see the lovely new node.
               if (shouldBeVisible) 
               {
                   tree.scrollPathToVisible(new TreePath(childNode.getPath()));
            }
               
            return childNode;
        }
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataModel.class);
    
    private static final String sFOREIGNKEY             = new String("ForeignKey");
    private static final String sRESOURCE               = new String("Resource");
    private static final String sCLASS                  = new String("Class");
    private static final String sTABLE                  = new String("Table");
    private static final String sUPDATE                 = new String("Update");
    private static final String sUPDATETYPE             = new String("UpdateType");
    private static final String sOBJECT                 = new String("Object");
    private static final String sSEARCHHELP             = new String("SearchHelp");
    private static final String sEDITMASK               = new String("EditMask");
    private static final String sLOOKUP                 = new String("Lookup");
    private static final String sLOOKUPTYPE             = new String("LookupType");
    private static final String sVALIDATIONLOOKUP       = new String("ValidationLookup");
    private static final String sVALIDATIONLOOKUPTYPE   = new String("ValidationLookupType");
    private static final String sVALIDATIONEXTERNAL     = new String("ValidationExternal");
    private static final String sVALIDATIONEXTERNALTYPE = new String("ValidationExternalType");
    private static final String sVALIDATIONEXPRESSION   = new String("ValidationExpression");
    
    private JButton             mAddButton;
    private ActionListener      mAddButtonListener;
    private JButton             mEditButton;
    private ActionListener      mEditButtonListener;
    private JButton             mDeleteButton;
    private ActionListener      mDeleteButtonListener;
    private MetadataTreePanel   mTreePanel;
    private JPanel              mMainPanel;
    private JSplitPane          mSplitPane;
    private JScrollPane         mTextScrollPane;
    private JMenu               mMetadataMenu;
    
    
    private MetadataTreeNode    mClassNode                      = null;
    private MetadataTreeNode    mForeignKeyNode                 = null;
    private MetadataTreeNode    mResourceNode                   = null;
    private MetadataTreeNode    mEditMaskNode                   = null;
    private MetadataTreeNode    mLookupNode                     = null;
    private MetadataTreeNode    mLookupTypeNode                 = null;
    private MetadataTreeNode    mObjectNode                     = null;
    private MetadataTreeNode    mSearchHelpNode                 = null;
    private MetadataTreeNode    mTableNode                      = null;
    private MetadataTreeNode    mUpdateNode                     = null;
    private MetadataTreeNode    mUpdateTypeNode                 = null;
    private MetadataTreeNode    mUpdateHelpNode                 = null;
    private MetadataTreeNode    mValidationLookupNode           = null;
    private MetadataTreeNode    mValidationLookupTypeNode       = null;
    private MetadataTreeNode    mValidationExternalNode         = null;
    private MetadataTreeNode    mValidationExternalTypeNode     = null;
    private MetadataTreeNode    mValidationExpressionNode       = null;
    
    private MClass                      mClass                  = null;
    private MEditMask                   mEditMask               = null;
    private MForeignKey                 mForeignKey             = null;
    private MLookup                     mLookup                 = null;
    private MLookupType                 mLookupType             = null;
    private MObject                     mObject                 = null;
    private MResource                   mResource               = null;
    private MSearchHelp                 mSearchHelp             = null;
    private MSystem                     mSystem                 = null;
    private MTable                      mTable                  = null;
    private MUpdate                     mUpdate                 = null;
    private MUpdateHelp                 mUpdateHelp             = null;
    private MUpdateType                 mUpdateType             = null;
    private MValidationExpression       mValidationExpression   = null;
    private MValidationExternal         mValidationExternal     = null;
    private MValidationExternalType     mValidationExternalType = null;
    private MValidationLookup           mValidationLookup       = null;
    private MValidationLookupType       mValidationLookupType   = null;

}

