package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;

import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.admin.swingui.metadata.MetadataClassDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataConversionDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataGenericDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataResourceDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataTableDialog;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.attrib.AttrBoolean;
import org.realtors.rets.common.metadata.attrib.AttrPositiveNumeric;
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

import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.metadata.MetadataDao;

public class MetadataPanel extends AdminTab
{
    public MetadataPanel(JMenu metadataMenu)
    {
        super(new BorderLayout());
                
        mMetadataMenu = metadataMenu;
        mMetadataMenu.add(new MetadataReloadAction());
        mStrictParsingMenuItem = new JCheckBoxMenuItem("Strict");
        mStrictParsingMenuItem.setToolTipText("Enable or disable strict adherence to the RETS 1.7.2 spec when parsing metadata.");
        RetsConfig retsConfig = Admin.getRetsConfig();
        mStrictParsing = retsConfig.getStrictParsing();
        mStrictParsingMenuItem.setSelected(mStrictParsing);
        mStrictParsingMenuItem.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                mStrictParsing = true;
                if (e.getStateChange() == ItemEvent.DESELECTED)
                    mStrictParsing = false;
                RetsConfig retsConfig = Admin.getRetsConfig();
                retsConfig.setStrictParsing(mStrictParsing);
            }
        });
        mMetadataMenu.add(mStrictParsingMenuItem);
        
        JMenuItem conversionMenuItem = new JMenuItem("Migrate");
        conversionMenuItem.setToolTipText("Migrate Variman RETS 1.5 and earlier metadata to current standards.");
        conversionMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                MetadataConversionDialog dialog = new MetadataConversionDialog(mMetadataMenu);
                if (dialog.getResponse() == JOptionPane.OK_OPTION)
                {
                    mMetadata = dialog.getMetadata();
                    mSystem = mMetadata.getSystem();
                    Admin.setRetsConfigChanged(true);
                    refreshTab();
                }
            }
        });
        mMetadataMenu.add(conversionMenuItem);
        
        mMetadataMenu.setEnabled(false);
        
        /*
         *  The windowing components must be built before Tree is populated 
         *  because when the tree selection listener is fired, the components
         *  can't be null.
         */
        mMainPanel = new JPanel(new BorderLayout());
    }
    
    /**
     * Since the metadata is no longer stored as a singleton, return it to the
     * caller if needed.
     * @return The current Metadata
     */
    public Metadata getMetadata()
    {
        return mMetadata;
    }
    
    /**
     * Refresh the content side of the split panel.
     * @param userObject The user object from the TreeNode that is currently selected.
     */
    private void refreshMainPanel(Object userObject)
    {
        mMainPanel.removeAll();
        
        JPanel panel = new JPanel(new SpringLayout());
        int rows = 0;

        for (String attribute : ((MetaObject)userObject).getAttributeNames())
        {
            AttrType<?> attrType    = ((MetaObject)userObject).getAttributeType(attribute);
            String      value       = ((MetaObject)userObject).getAttributeAsString(attribute);
            if (value == null)
                value = new String("");
            JLabel label = new JLabel(attribute + ":", SwingConstants.TRAILING);
            
            if (attrType instanceof AttrBoolean)
            {
                if (value.equals("1"))
                    value = "True";
                else
                if (value.equals("0"))
                    value = "False";
            }
            
            JLabel val = new JLabel(value);
            
            if (userObject instanceof MResource && 
                   attrType instanceof AttrPositiveNumeric && 
                   attribute.equals(MResource.CLASSCOUNT))
            {
                /*
                 * This is the class count field. Determine the count.
                 */
                value = Integer.toString(((MResource) userObject).getClassCount());
                val = new JLabel(value);
            }
               
            label.setLabelFor(val);
            panel.add(label);
            panel.add(val);
            rows++;
        }

        SwingUtils.SpringLayoutGrid(panel, rows, 2, 6, 6, 6, 6);
        mTextScrollPane = new JScrollPane(panel);
        mMainPanel.add(mTextScrollPane, BorderLayout.CENTER);
                                   
        mSplitPane.revalidate();
    }
    
    /**
     * Populate the tree from the metadata.
     */
    private void populateTree(MetadataTreePanel treePanel)
    {
        Comparator<MetaObject> MetaObjectComparator = new Comparator<MetaObject>()
            {
                public int compare (MetaObject one, MetaObject two)
                {
                    return one.getId().compareTo(two.getId());
                }
            };
        
        mForeignKeyNode = treePanel.addObject(null, sFOREIGNKEY);
        mResourceNode = treePanel.addObject(null, sRESOURCE);
        
        if (mMetadata != null)
        {
            MForeignKey [] foreignKeys = mSystem.getMForeignKeys();
            Arrays.sort(foreignKeys, MetaObjectComparator);
            
            for (MForeignKey foreignKey : mSystem.getMForeignKeys())
            {
                treePanel.addObject(mForeignKeyNode, foreignKey);
            }
            
            MResource [] resources = mSystem.getMResources();
            Arrays.sort(resources, MetaObjectComparator);
            
            for (MResource resource : resources)
            {
                MetadataTreeNode editMaskNode = null;
                MetadataTreeNode lookupNode = null;
                MetadataTreeNode objectNode = null;
                MetadataTreeNode searchHelpNode = null;
                MetadataTreeNode updateHelpNode = null;
                MetadataTreeNode validationLookupNode = null;
                MetadataTreeNode validationExternalNode = null;
                MetadataTreeNode validationExpressionNode = null;
                
                MetadataTreeNode resourceNode = treePanel.addObject(mResourceNode, resource);

                /*
                 * Enumerate the RETS classes and populate the tree.
                 */
                MClass [] classes = resource.getMClasses();
                Arrays.sort(classes, MetaObjectComparator);
                
                for (MClass clazz : classes)
                {
                    MetadataTreeNode classNode = treePanel.addObject(resourceNode, clazz);
                    /*
                     * Iterate over the tables for this class.
                     */
                    MTable [] tables = clazz.getMTables();
                    Arrays.sort(tables, MetaObjectComparator);
                    
                    for (MTable table : tables)
                    {
                        treePanel.addObject(classNode, table);
                    }
                    /*
                     * Iterate over the updates for this class.
                     */
                    MUpdate [] updates = clazz.getMUpdates();
                    Arrays.sort(updates, MetaObjectComparator);
                    
                    for (MUpdate update : updates)
                    {
                        MetadataTreeNode node = treePanel.addObject(classNode, sUPDATE);
                        MetadataTreeNode updateNode = treePanel.addObject(node, update);
                        /*
                         * Iterate over the update types for this update.
                         */
                        MUpdateType [] updateTypes = update.getMUpdateTypes();
                        Arrays.sort(updateTypes, MetaObjectComparator);
                      
                        for (MUpdateType updateType: updateTypes)
                        {
                            treePanel.addObject(updateNode, updateType);
                        }
                    }
                }
                /*
                 * Enumerate the lookups.
                 */
                MLookup [] lookups = resource.getMLookups();
                Arrays.sort(lookups, MetaObjectComparator);
                
                for (MLookup lookup : lookups)
                {
                    if (lookupNode == null)
                        lookupNode = treePanel.addObject(resourceNode, sLOOKUP);
                    
                    MetadataTreeNode node = treePanel.addObject(lookupNode, lookup);
                    
                    MLookupType [] lookupTypes = lookup.getMLookupTypes();
                    Arrays.sort(lookupTypes, MetaObjectComparator);
                    
                    for (MLookupType lookupType : lookupTypes)
                    {
                        treePanel.addObject(node, lookupType);
                    }
                }
                /*
                 * Enumerate the objects.
                 */
                MObject [] objects = resource.getMObjects();
                Arrays.sort(objects, MetaObjectComparator);
                
                for (MObject obj : objects)
                {
                    if (objectNode == null)
                        objectNode = treePanel.addObject(resourceNode, sOBJECT);
                    
                    treePanel.addObject(objectNode, obj);
                }
                /*
                 * Enumerate the edit masks.
                 */
                MEditMask [] editMasks = resource.getMEditMasks();
                Arrays.sort(editMasks, MetaObjectComparator);
                
                for (MEditMask editMask : editMasks)
                {
                    if (editMaskNode == null)
                        editMaskNode = treePanel.addObject(resourceNode, sEDITMASK);
                    
                    treePanel.addObject(editMaskNode, editMask);
                }
                /*
                 * Enumerate the search help.
                 */
                MSearchHelp [] searchHelps = resource.getMSearchHelps();
                Arrays.sort(searchHelps, MetaObjectComparator);
                
                for (MSearchHelp searchHelp : searchHelps)
                {
                    if (searchHelpNode == null)
                        searchHelpNode = treePanel.addObject(resourceNode, sSEARCHHELP);
                    
                    treePanel.addObject(searchHelpNode, searchHelp);
                }
                /*
                 * Enumerate the update help.
                 */
                MUpdateHelp [] updateHelps = resource.getMUpdateHelps();
                Arrays.sort(updateHelps, MetaObjectComparator);
                
                for (MUpdateHelp updateHelp : updateHelps)
                {
                    if (updateHelpNode == null)
                        updateHelpNode = treePanel.addObject(resourceNode, sUPDATEHELP);
                    
                    treePanel.addObject(updateHelpNode, updateHelp);
                }
                /*
                 * Enumerate the validation lookups.
                 */
                MValidationLookup [] validationLookups = resource.getMValidationLookups();
                Arrays.sort(validationLookups, MetaObjectComparator);
                
                for (MValidationLookup lookup : validationLookups)
                {
                    if (validationLookupNode == null)
                        validationLookupNode = treePanel.addObject(resourceNode, sVALIDATIONLOOKUP);
                    
                    MetadataTreeNode node = treePanel.addObject(validationLookupNode, lookup);
                    /*
                     * Enumerate the validation lookup types.
                     */
                    MValidationLookupType [] validationLookupTypes = lookup.getMValidationLookupTypes();
                    Arrays.sort(validationLookupTypes, MetaObjectComparator);
                    
                    for (MValidationLookupType lookupType : validationLookupTypes)
                    {
                        treePanel.addObject(node, lookupType);
                    }
                }
                /*
                 * Enumerate the validation externals.
                 */
                MValidationExternal [] validationExternals = resource.getMValidationExternals();
                Arrays.sort(validationExternals, MetaObjectComparator);
                
                for (MValidationExternal validation : validationExternals)
                {
                    if (validationExternalNode == null)
                        validationExternalNode = treePanel.addObject(resourceNode, sVALIDATIONEXTERNAL);
                    
                    MetadataTreeNode node = treePanel.addObject(validationExternalNode, validation);
                    /*
                     * Enumerate the validation external types.
                     */
                    MValidationExternalType [] validationExternalTypes = validation.getMValidationExternalTypes();
                    Arrays.sort(validationExternalTypes, MetaObjectComparator);
                    
                    for (MValidationExternalType externalType : validationExternalTypes)
                    {
                        treePanel.addObject(node, externalType);
                    }
                }
                /*
                 * Enumerate the validation expressions.
                 */
                MValidationExpression [] validationExpressions = resource.getMValidationExpressions();
                Arrays.sort(validationExpressions, MetaObjectComparator);
                
                for (MValidationExpression validationExpression : validationExpressions)
                {
                    if (validationExpressionNode == null)
                        validationExpressionNode = treePanel.addObject(resourceNode, sVALIDATIONEXPRESSION);
                    
                    treePanel.addObject(validationExpressionNode, validationExpression);
                }
            }
        }
    }
 
    public void tabSelected()
    {
        mMetadataMenu.setEnabled(true);
    }

    public void tabDeselected()
    {
        mMetadataMenu.setEnabled(false);
    }

    public void refreshTab()
    {
        mMetadataMenu.setEnabled(true);

        SwingWorker worker = new SwingWorker()
        {
            
            public Object construct()
            {
                if (mMetadata == null)
                {
                    AdminFrame.getInstance().setStatusText("Loading Metadata ...");
                    AdminFrame.getInstance().setWaitCursor();

                    try
                    {
                        MetadataDao metadataDao = Admin.getMetadataDao();
                        mMetadata = metadataDao.getMetadata();
                        mSystem = mMetadata.getSystem();
                    }
                    catch (Throwable t)
                    {
                        LOG.error("Caught exception", t);
                        // Todo: MetadataPanel.construct show error dialog
                    }
                }
                return mSystem;
            }

            public void finished()
            {
                removeAll();
                
                mTreePanel = new MetadataTreePanel();
                int oldLocation = 250;
                if (mSplitPane != null) 
                    oldLocation = mSplitPane.getLastDividerLocation();
                mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mTreePanel, mMainPanel);
                mSplitPane.setDividerLocation(oldLocation);
                add(mSplitPane, BorderLayout.CENTER);

                populateTree(mTreePanel);
                mTreePanel.collapseAll();
                mSplitPane.revalidate();
                AdminFrame.getInstance().setStatusText("Metadata Loaded Successfully");
                AdminFrame.getInstance().setDefaultCursor();
            }
        };
        worker.start();
    }
    
    public class MetadataReloadAction extends AbstractAction
    {
        public MetadataReloadAction()
        {
            super("Reload Metadata");
        }

        public void actionPerformed(ActionEvent event)
        {
            final AdminFrame frame = AdminFrame.getInstance();
            frame.setStatusText("Reloading Metadata ...");
            
            SwingWorker worker = new SwingWorker()
            {
                public Object construct()
                {
                    Object o;
                    try
                    {
                        MetadataDao metadataDao = Admin.getMetadataDao();
                        o = metadataDao.getMetadata();
                    }
                    catch  (Throwable e)
                    {
                        LOG.error("Caught", e);
                        return e;
                    }
                    return o;
                }

                public void finished()
                {
                    Object o = get();
                    if (o instanceof Throwable)
                    {
                        Throwable t = (Throwable) o;
                        frame.setStatusText("Unable to reload Metadata: " +
                                            t.getMessage());
                    }
                    else
                    {
                        Metadata m = (Metadata) o;
                        mMetadata = m;
                        mSystem = m.getSystem();
                        
                        refreshTab();
                        frame.setStatusText("Metadata successfully reloaded.");
                    }
                }
            };
            worker.start();
        }
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
             * The root node is "System". If we have metadata, use it.
             */
            rootNode = new MetadataTreeNode(mMetadata.getSystem());

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
            
            panel.add (mAddButton);
            panel.add(mEditButton);
            panel.add (mDeleteButton);
            add(panel, BorderLayout.SOUTH);

            /*
             * Add the tree selection listener. The listener is responsible for setting up the 
             * action buttons based on the current state (e.g. node selected).
             */
            tree.addTreeSelectionListener(new MetadataTreeListener(tree));
        }
            
        /** Remove all nodes except the root node. */
        public void clear() 
        {
            rootNode.removeAllChildren();
            treeModel.reload();
        }    
               
        /** Remove the currently selected node. */
        public void removeCurrentNode() 
        {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) 
            {
              MetadataTreeNode currentNode = (MetadataTreeNode) 
                                          (currentSelection.getLastPathComponent());
              MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
              if (parent != null) 
              {
                  treeModel.removeNodeFromParent(currentNode);
                  return;
              }
            }

           // Either there was no selection, or the root was selected.
           toolkit.beep();
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
        
        /*
         * The tree can be built in random order, so it shows up with
         * all nodes expanded. Collapse everything and then expand to
         * the "Resource" node.
         */
        public void collapseAll()
        {
            int row = tree.getRowCount() - 1;
            while (row >= 0)
            {
                tree.collapseRow(row);
                row--;
            }
            
            tree.scrollPathToVisible(new TreePath(mResourceNode.getPath()));
            tree.getSelectionModel().setSelectionPath(tree.getPathForRow(0));
        }
    }
    
    private class MetadataTreeListener implements TreeSelectionListener
    {
        JTree            mTree;
        
        MetadataTreeListener(JTree tree)
        {
            super();
            mTree = tree;
        }
        /*
         * Add the tree selection listener. The listener is responsible for setting up the 
         * action buttons based on the current state (e.g. node selected).
         *    
         * The general states for the "ADD" button are:
         * 
         * Node Selected            Action
         * -------------            -------------------------
         * "System"                 Add not allowed.
         * "ForeignKey"             Add foreign key.
         * "Resource"               Add MResource.
         * "Lookups"                Add MLookup.
         * "Object"                 Add MObject.
         * "EditMask"               Add MEditMask.
         * "SearchHelp"             Add MSearchHelp.
         * "UpdateHelp"             Add MUpdateHelp.
         * "ValidationLookup"       Add MValidationLookup.
         * "ValidationExternal"     Add MValidationExternal.
         * "ValidationExpression"   Add MValidationExpression.
         * MResource                Add MClass, MLookup, MObject, MEditMask
         *                                 MSearchHelp, MUpdateHelp, MValidationLookup,
         *                                 MValidationExternal or MValidationExpression.
         * MClass                   Add MTable or MUpdate.
         * MTable                   Add not allowed.
         * MUpdate                  Add MUpdateType.
         * MLookup                  Add MLookupType.
         * MLookupType              Add not allowed.
         * MObject                  Add not allowed.
         * MEditMask                Add not allowed.
         * MSearchHelp              Add not allowed.
         * MUpdateHelp              Add not allowed.
         * MValidationLookup        Add MValidationLookupType.
         * MValidationLookupType    Add not allowed.
         * MValidationExternal      Add MValidationExternalType
         * MValidationExternalType  Add not allowed.
         * MValidationExpression    Add not allowed.
         */
        
        private class MetaObjectAddButtonListener implements ActionListener
        {
            String mAnswer;
            MetaObject mMetaObject;
            MetadataTreeNode mSelectedNode;
            
            MetaObjectAddButtonListener(MetaObject userObject, MetadataTreeNode selectedNode, String answer)
            {
                super();
                mAnswer = answer;
                mMetaObject = userObject;
                mSelectedNode = selectedNode;
            }

            MetaObjectAddButtonListener(MetaObject userObject, MetadataTreeNode selectedNode)
            {
                this(userObject, selectedNode, null);
            }
            
            public void actionPerformed(ActionEvent e)
            {
                MetadataType metadataType = null;
                MetadataTreeNode node = mSelectedNode;
                MetaObject resource = null;
                
                /*
                 * mMetaObject is the parent object that was selected.
                 */
                if (mMetaObject instanceof MResource)                                
                {
                    /*
                     * We can only add METADATA-CLASS, METADATA-LOOKUP, METADATA-OBJECT,
                     * METADATA-EDITMASK, METADATA-SEARCH_HELP, METADATA-UPDATE_HELP,
                     * METADATA_VALIDATION_LOOKUP, METADATA_VALIDATION_EXTERNAL and
                     * METADATA-VALIDATION_EXPRESSION to METADATA-RESOURCE nodes.
                     */
                    Object[] ddl = {"Class",
                                    "Lookup",
                                    "Object",
                                    "EditMask",
                                    "SearchHelp",
                                    "UpdateHelp",
                                    "ValidationLookup",
                                    "ValidationExternal",
                                    "ValidationExpression"};
                    
                    if (mAnswer == null)
                        mAnswer = (String) JOptionPane.showInputDialog(
                                                    SwingUtils.getAdminFrame(),
                                                    "Add Which?",
                                                    "Add to Resource",
                                                    JOptionPane.QUESTION_MESSAGE,
                                                    null,
                                                    ddl,
                                                    "Class");
                    if (mAnswer == null)
                        return;
                    
                    if (mAnswer.equals("Class"))
                    {
                        metadataType = MetadataType.CLASS;
                        resource = new MClass();
                    }
                    else                                   
                    if (mAnswer.equals("Lookup"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "Lookups" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sLOOKUP))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */                                                                                              
                               node = mTreePanel.addObject(mSelectedNode, sLOOKUP);
                           }
                        metadataType = MetadataType.LOOKUP;
                        resource = new MLookup();
                    }
                    else                                   
                    if (mAnswer.equals("Object"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "Object" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sOBJECT))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */                                               
                               node = mTreePanel.addObject(mSelectedNode, sOBJECT);
                           }
                        metadataType = MetadataType.OBJECT;
                        resource = new MObject();
                    }
                    else                                   
                    if (mAnswer.equals("EditMask"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "EditMask" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sEDITMASK))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sEDITMASK);
                           }

                        metadataType = MetadataType.EDITMASK;
                        resource = new MEditMask();
                    }
                    else                                   
                    if (mAnswer.equals("SearchHelp"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "SearchHelp" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals("sSEARCHHELP"))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sSEARCHHELP);
                           }

                        metadataType = MetadataType.SEARCH_HELP;
                        resource = new MSearchHelp();
                    }
                    else                                   
                    if (mAnswer.equals("UpdatehHelp"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "UpdateHelp" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sUPDATEHELP))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sUPDATEHELP);
                           }

                        metadataType = MetadataType.UPDATE_HELP;
                        resource = new MUpdateHelp();
                    }
                    else                                   
                    if (mAnswer.equals("ValidationLookup"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "ValidationLookup" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sVALIDATIONLOOKUP))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sVALIDATIONLOOKUP);
                           }

                        metadataType = MetadataType.VALIDATION_LOOKUP;
                        resource = new MValidationLookup();
                    }
                    else                                   
                    if (mAnswer.equals("ValidationExternal"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "ValidationExternal" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sVALIDATIONEXTERNAL))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sVALIDATIONEXTERNAL);
                           }

                        metadataType = MetadataType.VALIDATION_EXTERNAL;
                        resource = new MValidationExternal();
                    }
                    else                                   
                    if (mAnswer.equals("ValidationExpression"))
                    {
                           boolean found = false;
                           /*
                            * Determine if the "ValidationExpression" node is already in the tree. If so, switch
                            * to it. Otherwise, add it.
                            */
                           for (int i = 0; i < mSelectedNode.getChildCount(); i++)
                           {
                               MetadataTreeNode treeNode = (MetadataTreeNode)mSelectedNode.getChildAt(i);
                               Object userObject = treeNode.getUserObject();
                               if (userObject instanceof String && userObject.toString().equals(sVALIDATIONEXPRESSION))
                               {
                                   found = true;
                                   node = treeNode;
                                   break;
                               }
                           }
                           if (!found)
                           {
                               /*
                                * Never used before, so add the heading node and make it the current node.
                                */
                               node = mTreePanel.addObject(mSelectedNode, sVALIDATIONEXPRESSION);
                           }

                        metadataType = MetadataType.VALIDATION_EXPRESSION;
                        resource = new MValidationExpression();
                    }
                }
                else
                if (mMetaObject instanceof MClass)
                {
                    /*
                     * We can only add Table and Update metadata to METADATA-CLASS.
                     */
                    Object[] ddl = {"Table",
                                    "Update"};
                    
                    mAnswer = (String) JOptionPane.showInputDialog(
                                                    SwingUtils.getAdminFrame(),
                                                    "Add Which?",
                                                    "Add to Class",
                                                    JOptionPane.QUESTION_MESSAGE,
                                                    null,
                                                    ddl,
                                                    "Table");
                    if (mAnswer == null)
                        return;
                    
                    if (mAnswer.equals("Table"))
                    {
                        metadataType = MetadataType.TABLE;
                        resource = new MTable();
                    }
                    else
                       if (mAnswer.equals("Update"))
                    {
                        metadataType = MetadataType.UPDATE;
                        resource = new MUpdate();
                    }
                }
                else
                if (mMetaObject instanceof MLookup)                                
                {
                    metadataType = MetadataType.LOOKUP_TYPE;
                    resource = new MLookupType();
                }
                else
                if (mMetaObject instanceof MUpdate)                                
                {
                    metadataType = MetadataType.UPDATE_TYPE;
                    resource = new MUpdateType();
                }
                else
                if (mMetaObject instanceof MValidationExternal)                                
                {
                    metadataType = MetadataType.VALIDATION_EXTERNAL_TYPE;
                    resource = new MValidationExternalType();
                }
                else
                if (mMetaObject instanceof MValidationLookup)                                
                {
                    metadataType = MetadataType.VALIDATION_LOOKUP_TYPE;
                    resource = new MValidationLookupType();
                }
                else
                {
                    LOG.warn("*** Unexpected User Object selected: " + mMetaObject);
                    resource = null;
                }
                
                /*
                 * Clear mAnswer for next pass.
                 */
                mAnswer = null;
                
                /*
                 * Instantiate the default or custom dialog, depending on the type of
                 * the metadata.
                 */                              
                MetadataDialog dialog = new MetadataGenericDialog(mStrictParsing, mMetadata, resource);
                   
                if (resource instanceof MTable)
                {
                    /*
                     * mMetaObject had better be an MClass, but check anyway.
                     */
                    if (mMetaObject instanceof MClass)
                    {
                        /*
                         * Get the parent Resource and invoke the custom dialog.
                         */
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) mSelectedNode.getParent();
                        Object parentObject = parentNode.getUserObject();
                        dialog = new MetadataTableDialog(mStrictParsing,
                                                        mMetadata, 
                                                        (MResource)parentObject, 
                                                        (MTable)resource);
                    }
                    else
                        LOG.debug("*** mMetaObject is not MClass when dealing with a table!!!");
                }
                else
                if (resource instanceof MClass)
                {
                    /*
                     * Invoke the custom dialog.
                     */
                    dialog = new MetadataClassDialog(mStrictParsing,
                                                    mMetadata, 
                                                    (MClass)resource);
                }
                
                dialog.setVisible(true);
                if (dialog.getResponse() == JOptionPane.OK_OPTION)
                {
                    if (mMetaObject.getChild(metadataType, resource.getId()) == null)
                    {
                        /*
                         * Add the object to the tree and to the metadata. 
                         */
                        mTreePanel.addObject(node, resource);
                        mMetaObject.addChild(metadataType, resource);

                        Admin.setRetsConfigChanged(true);
                        refreshMainPanel(mMetaObject);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                                SwingUtils.getAdminFrame(),
                                ((MetaObject)resource).getId() + " already exists!");
                    }
                }
            }
        }
        
        private class MetaObjectDeleteButtonListener implements ActionListener
        {
            MetaObject mMetaObject;
            MetadataTreeNode mSelectedNode;
            
            MetaObjectDeleteButtonListener(MetaObject userObject, MetadataTreeNode selectedNode)
            {
                super();
                mMetaObject = userObject;
                mSelectedNode = selectedNode;
            }
            
            public void actionPerformed(ActionEvent e)
            {
                if (mMetaObject instanceof MetaObject)
                {
                    int answer = (int) JOptionPane.showConfirmDialog(
                                                SwingUtils.getAdminFrame(),
                                                "Are you sure?",
                                                "Delete Confirmation",
                                                JOptionPane.OK_CANCEL_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);
                    if (answer == JOptionPane.OK_OPTION)
                    {
                       /*
                        * Locate the parent node.
                        */
                       DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) mSelectedNode.getParent();
                       Object parentObject = null;
                       
                        if (parentNode != null)
                            parentObject = parentNode.getUserObject();
                        if (parentObject instanceof MetaObject)
                        {
                            /*
                             * The parent object is some form of Metadata, so we're good to go.
                             */
                            Admin.setRetsConfigChanged(true);
                            /*
                             * Remove the object from the parent.
                             */
                            ((MetaObject)parentObject).deleteChild(
                                                           ((MetaObject)mMetaObject).getMetadataType(), 
                                                           (MetaObject)mMetaObject);                                               
                            mTreePanel.removeCurrentNode();
                            /*
                             * And set the parent as selected.
                             */
                            mTree.getSelectionModel().setSelectionPath(new TreePath(parentNode.getPath()));                                               
                            
                            refreshMainPanel(parentObject);
                        }
                        else
                        if (parentObject != null)
                        {
                            /*
                             * FOREIGNKEY, RESOURCE, LOOKUP, OBJECT, EDITMASK, SEARCHHELP, UPDATEHELP,
                             * VALIDATIONLOOKUP, VALIDATIONEXTERNAL and VALIDATIONEXPRESSION
                             * is the parent. These are simple string nodes that need special handling.
                             */
                            if (parentObject.equals(sRESOURCE))
                                mSystem.deleteChild(MetadataType.RESOURCE, (MetaObject) mMetaObject);
                            else
                            if (parentObject.equals(sFOREIGNKEY))
                                mSystem.deleteChild(MetadataType.FOREIGN_KEYS, (MetaObject) mMetaObject);
                            else
                            {
                                DefaultMutableTreeNode parentsParentNode = (DefaultMutableTreeNode) parentNode.getParent();

                                MetaObject resource = null;
                                
                                if (parentsParentNode != null)
                                    resource = (MetaObject) parentsParentNode.getUserObject();
                                if (resource != null)
                                {
                                    if (parentObject.equals(sLOOKUP))
                                        resource.deleteChild(MetadataType.LOOKUP, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sOBJECT))
                                        resource.deleteChild(MetadataType.OBJECT, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sEDITMASK))
                                        resource.deleteChild(MetadataType.EDITMASK, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sSEARCHHELP))
                                        resource.deleteChild(MetadataType.SEARCH_HELP, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sUPDATEHELP))
                                        resource.deleteChild(MetadataType.UPDATE_HELP, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sVALIDATIONLOOKUP))
                                        resource.deleteChild(MetadataType.VALIDATION_LOOKUP, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sVALIDATIONEXTERNAL))
                                        resource.deleteChild(MetadataType.VALIDATION_EXTERNAL, (MetaObject) mMetaObject);
                                    else
                                    if (parentObject.equals(sVALIDATIONEXPRESSION))
                                        resource.deleteChild(MetadataType.VALIDATION_EXPRESSION, (MetaObject) mMetaObject);
                                }
                            }
                            
                            Admin.setRetsConfigChanged(true);
                            mTreePanel.removeCurrentNode();
                            /*
                             * And set the parent as selected.
                             */
                            mTree.getSelectionModel().setSelectionPath(new TreePath(parentNode.getPath()));                                               

                            mMainPanel.removeAll();
                            mMainPanel.add(new JLabel(""));
                            mSplitPane.revalidate();
                        }
                    }
                }
            }
        }
        
        private class MetaObjectEditButtonListener implements ActionListener
        {
            MetaObject mMetaObject;
            MetadataTreeNode mSelectedNode;
            
            MetaObjectEditButtonListener(MetaObject userObject, MetadataTreeNode selectedNode)
            {
                super();
                mMetaObject = userObject;
                mSelectedNode = selectedNode;
            }
            
            public void actionPerformed(ActionEvent e)
            {
                /*
                 * Instantiate the default or custom dialog, depending on the type of
                 * the metadata.
                 */
                MetadataDialog dialog = new MetadataGenericDialog(mStrictParsing, mMetadata, mMetaObject);
                
                if (mMetaObject instanceof MTable)
                {
                    /*
                     * Get the parent Resource and invoke the custom dialog.
                     */
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) mSelectedNode.getParent().getParent();
                    Object parentObject = parentNode.getUserObject();
                    if (parentObject instanceof MResource)
                        dialog = new MetadataTableDialog(mStrictParsing,
                                                        mMetadata, 
                                                        (MResource) parentObject,
                                                        (MTable) mMetaObject);
                    else 
                        LOG.warn("*** Can't locate parent Resource for " + mMetaObject);
                }
                else
                if (mMetaObject instanceof MResource)
                {
                    /*
                     * Invoke the custom dialog.
                     */
                   dialog = new MetadataResourceDialog(mStrictParsing,
                                                        mMetadata, 
                                                        (MResource) mMetaObject);
                }
                else
                if (mMetaObject instanceof MClass)
                {
                    /*
                     * Invoke the custom dialog.
                     */
                    dialog = new MetadataClassDialog(mStrictParsing,
                                                     mMetadata, 
                                                     (MClass) mMetaObject);
                }
                   
                dialog.setVisible(true);
                if (dialog.getResponse() == JOptionPane.OK_OPTION)
                {
                    Admin.setRetsConfigChanged(true);
                    refreshMainPanel(mMetaObject);
                }
            }
        }
        
        /**
         * A new node has been selected. Set up the buttons based on state.
         */
        public void valueChanged(TreeSelectionEvent e)
        {
            final MetadataTreeNode selectedNode = (MetadataTreeNode)mTree.getLastSelectedPathComponent();

            mAddButton.setEnabled(false);
            mEditButton.setEnabled(false);
            mDeleteButton.setEnabled(false);

            if (selectedNode == null)
                return;
            
            final Object userObject = selectedNode.getUserObject();
            if (userObject instanceof MetaObject)
            {

                mAddButton.setEnabled(false);
                mAddButton.removeActionListener(mAddButtonListener);
                mAddButtonListener = new MetaObjectAddButtonListener((MetaObject)userObject, selectedNode);
                mAddButton.addActionListener(mAddButtonListener);
                
                mEditButton.removeActionListener(mEditButtonListener);
                mEditButtonListener = new MetaObjectEditButtonListener((MetaObject)userObject, selectedNode);
                mEditButton.addActionListener(mEditButtonListener);
                mEditButton.setEnabled(true);
                
                mDeleteButton.removeActionListener(mDeleteButtonListener);
                mDeleteButtonListener = new MetaObjectDeleteButtonListener((MetaObject)userObject, selectedNode);
                mDeleteButton.addActionListener(mDeleteButtonListener);
                if (!(userObject instanceof MSystem))
                    mDeleteButton.setEnabled(true);
                
                if (userObject instanceof MResource
                    ||    userObject instanceof MClass
                    ||    userObject instanceof MUpdate
                    ||    userObject instanceof MLookup
                    ||    userObject instanceof MValidationLookup
                    ||    userObject instanceof MValidationExternal)
                {
                    mAddButton.setEnabled(true);
                }
                
                refreshMainPanel(userObject);
            }
            else
            if (userObject != null)
            {
                if (selectedNode.equals(mForeignKeyNode))
                {
                    mAddButton.removeActionListener(mAddButtonListener);
                    mAddButtonListener = new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            MForeignKey resource = new MForeignKey();
                            
                            MetadataDialog dialog = new MetadataGenericDialog(mStrictParsing, mMetadata, resource);
                                                                   
                            dialog.setVisible(true);
                            if (dialog.getResponse() == JOptionPane.OK_OPTION)
                            {
                                MetadataTreeNode newNode = mTreePanel.addObject(mForeignKeyNode, resource);
                                mTree.getSelectionModel().setSelectionPath(new TreePath(newNode.getPath()));
                                
                                mSystem.addChild(MetadataType.FOREIGN_KEYS, resource);
                                
                                Admin.setRetsConfigChanged(true);
                                refreshMainPanel(resource);
                            }
                        }
                    };
                    mAddButton.addActionListener(mAddButtonListener);
                    mAddButton.setEnabled(true);

                    mDeleteButton.removeActionListener(mDeleteButtonListener);
                    mDeleteButtonListener = new ActionListener()
                    {
                           public void actionPerformed(ActionEvent e)
                        {
                            /*
                             * In this context, the FOREIGN-KEYS node has been selected. Delete
                             * everything beneath it.
                             */
                            int answer = (int) JOptionPane.showConfirmDialog(
                                                        SwingUtils.getAdminFrame(),
                                                        "Are you sure?",
                                                        "Delete Confirmation",
                                                        JOptionPane.OK_CANCEL_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE);
                            if (answer == JOptionPane.OK_OPTION)
                            {
                                /*
                                 * The FOREIGNKEY node is a MetadataTreeNode containing a literal string.
                                 * We have to go to the mSystem object to properly delete the children.
                                 */
                                Admin.setRetsConfigChanged(true);
                                mSystem.deleteAllChildren(MetadataType.FOREIGN_KEYS);
                                mTreePanel.removeCurrentNode();
                                mForeignKeyNode = mTreePanel.addObject(null, sFOREIGNKEY);
                                mMainPanel.removeAll();
                                mMainPanel.add(new JLabel(""));
                                mSplitPane.revalidate();
                            }
                        }
                    };
                    mDeleteButton.addActionListener(mDeleteButtonListener);
                    mDeleteButton.setEnabled(true);
                }
                else
                if (selectedNode.equals(mResourceNode))
                {
                    mAddButton.setEnabled(true);
                    mAddButton.removeActionListener(mAddButtonListener);
                    mAddButtonListener = new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            MResource resource = new MResource();
                            
                            MetadataDialog dialog = new MetadataResourceDialog(mStrictParsing, mMetadata, resource);
                                                                   
                            dialog.setVisible(true);
                            if (dialog.getResponse() == JOptionPane.OK_OPTION)
                            {
                                MetadataTreeNode newNode = mTreePanel.addObject(mResourceNode, resource);
                                mTree.getSelectionModel().setSelectionPath(new TreePath(newNode.getPath()));
                                
                                mSystem.addChild(MetadataType.RESOURCE, resource);
                                
                                Admin.setRetsConfigChanged(true);
                                refreshMainPanel(resource);
                            }
                        }
                    };
                    mAddButton.addActionListener(mAddButtonListener);

                    mDeleteButton.removeActionListener(mDeleteButtonListener);
                    mDeleteButtonListener = new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            /*
                             * In this context, the RESOURCE node has been selected. Delete
                             * everything beneath it.
                             */
                               int answer = (int) JOptionPane.showConfirmDialog(
                                                        SwingUtils.getAdminFrame(),
                                                        "Are you sure?",
                                                        "Delete Confirmation",
                                                        JOptionPane.OK_CANCEL_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE);
                               if (answer == JOptionPane.OK_OPTION)
                               {
                                   /*
                                    * The RESOURCE node is a MetadataTreeNode containing a literal string.
                                    * We have to go to the mSystem object to properly delete the children.
                                    */
                                Admin.setRetsConfigChanged(true);
                                mSystem.deleteAllChildren(MetadataType.RESOURCE);
                                mTreePanel.removeCurrentNode();
                                mResourceNode = mTreePanel.addObject(null, sRESOURCE);
                                mMainPanel.removeAll();
                                mMainPanel.add(new JLabel(""));
                                mSplitPane.revalidate();
                            }
                        }
                    };
                    mDeleteButton.addActionListener(mDeleteButtonListener);
                    mDeleteButton.setEnabled(true);
                }
                else
                {
                    /*
                     * One of the Strings under a given resource has been selected 
                     * (e.g. "Lookups", "Object", etc. Set up the buttons for this
                     * context.
                     * 
                     * In this case, our parent node is the "userObject" for this
                     * context and will be a MetaObject. Determine which it is and 
                     * then invoke the add button class as if we'd actually
                     * selected the Resource node.
                     */
                    MetadataTreeNode parentNode = (MetadataTreeNode) selectedNode.getParent();
                    Object parentObject = null;
                    if (parentNode != null)
                        parentObject = parentNode.getUserObject();
                    
                    if (parentObject != null && parentObject instanceof MResource)
                    {
                        mAddButton.setEnabled(true);
                        mAddButton.removeActionListener(mAddButtonListener);
                        mAddButtonListener = new MetaObjectAddButtonListener((MetaObject)parentObject, parentNode, userObject.toString());
                        mAddButton.addActionListener(mAddButtonListener);
                    }
                }

                mMainPanel.removeAll();
                mMainPanel.add(new JLabel(""));
                mSplitPane.revalidate();
                
                LOG.debug("Tree Objected selected: " + userObject);
            }
        }
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataPanel.class);
    
    private static final String sRESOURCE               = new String("Resource");
    private static final String sFOREIGNKEY             = new String("ForeignKey");
    private static final String sLOOKUP                 = new String("Lookup");
    private static final String sOBJECT                 = new String("Object");
    private static final String sEDITMASK                = new String("EditMask");
    private static final String sSEARCHHELP                = new String("SearchHelp");
    private static final String sUPDATE                    = new String("Update");
    private static final String sUPDATEHELP                = new String("UpdateHelp");
    private static final String sVALIDATIONLOOKUP        = new String("ValidationLookup");
    private static final String sVALIDATIONEXTERNAL        = new String("ValidationExternal");
    private static final String sVALIDATIONEXPRESSION    = new String("ValidationExpression");

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
    private boolean             mStrictParsing;
    private JMenuItem           mStrictParsingMenuItem;
    
    private Metadata            mMetadata;
    private MSystem             mSystem;
    
    private MetadataTreeNode    mForeignKeyNode = null;
    private MetadataTreeNode    mResourceNode   = null;
}
