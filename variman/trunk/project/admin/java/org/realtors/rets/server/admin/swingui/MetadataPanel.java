package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.DateFormat;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.hibernate.HibernateException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.admin.swingui.metadata.MetadataClassDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataGenericDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataResourceDialog;
import org.realtors.rets.server.admin.swingui.metadata.MetadataTableDialog;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
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

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.JdomUtils;
import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.config.RetsConfig;

public class MetadataPanel extends AdminTab
{
    public MetadataPanel(JMenu metadataMenu)
    {
        super(new BorderLayout());
        
        /*
         * Variman extends METADATA-CLASS with X-DBname. Set that up.
         */
        MClass.addAttributes("X-DBName", MClass.sRETSNAME);
        
        mMetadataMenu = metadataMenu;
        mMetadataMenu.add(new CreateMetadataAction());
        mStrictParsingMenuItem = new JCheckBoxMenuItem("Strict Parsing");
        mStrictParsingMenuItem.setSelected(true);
        mStrictParsing = true;
        mStrictParsingMenuItem.addItemListener(new ItemListener()
        {
        	public void itemStateChanged(ItemEvent e)
        	{
        		mStrictParsing = true;
        		if (e.getStateChange() == ItemEvent.DESELECTED)
        			mStrictParsing = false;
        	}
        });
        mMetadataMenu.add(mStrictParsingMenuItem);
        mMetadataMenu.setEnabled(false);

        /*
         *  The windowing components must be built before Tree is populated 
         *  because when the tree selection listener is fired, the components
         *  can't be null.
         */
        //mMetadata = RetsConfig.getMetadata();
        //mSystem = mMetadata.getSystem();
        mMainPanel = new JPanel(new BorderLayout());
        //mTreePanel = new MetadataTreePanel();
        //mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mTreePanel, mMainPanel);
        //mSplitPane.setDividerLocation(250);
        //add(mSplitPane, BorderLayout.CENTER);
                           
        //PopupListener popupListener = new PopupListener();
        //mSplitPane.addMouseListener(popupListener);

        /*
         * OK, it is safe to populate the tree.
         */
        //populateTree(mTreePanel);
        //mTreePanel.collapseAll();
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
        	AttrType<?> attrType	= ((MetaObject)userObject).getAttributeType(attribute);
        	String 		value 		= ((MetaObject)userObject).getAttributeAsString(attribute);
        	if (value == null)
        		value = new String("");
        	JLabel label = new JLabel(attribute + ":", JLabel.TRAILING);
        	
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
    	mForeignKeyNode = treePanel.addObject(null, new String("ForeignKey"));
    	mResourceNode = treePanel.addObject(null, new String("Resource"));
    	
		if (mMetadata != null)
		{
			for (MForeignKey foreignKey : mSystem.getMForeignKeys())
			{
				treePanel.addObject(mForeignKeyNode, foreignKey);
			}
			
			for (MResource resource : mSystem.getMResources())
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
				for (MClass clazz : resource.getMClasses())
				{
					MetadataTreeNode classNode = treePanel.addObject(resourceNode, clazz);
					/*
					 * Iterate over the tables for this class.
					 */
					for (MTable table : clazz.getMTables())
					{
						treePanel.addObject(classNode, table);
					}
					/*
					 * Iterate over the updates for this class.
					 */
					for (MUpdate update : clazz.getMUpdates())
					{
						MetadataTreeNode node = treePanel.addObject(classNode, new String("Updates"));
						MetadataTreeNode updateNode = treePanel.addObject(node, update);
						/*
						 * Iterate over the update types for this update.
						 */
						for (MUpdateType updateType: update.getMUpdateTypes())
						{
							treePanel.addObject(updateNode, updateType);
						}
					}
				}
				/*
				 * Enumerate the lookups.
				 */
				for (MLookup lookup : resource.getMLookups())
				{
					if (lookupNode == null)
						lookupNode = treePanel.addObject(resourceNode, new String("Lookups"));
					
					MetadataTreeNode node = treePanel.addObject(lookupNode, lookup);
					
					for (MLookupType lookupType : lookup.getMLookupTypes())
					{
						treePanel.addObject(node, lookupType);
					}
				}
				/*
				 * Enumerate the objects.
				 */
				for (MObject obj : resource.getMObjects())
				{
					if (objectNode == null)
						objectNode = treePanel.addObject(resourceNode, new String("Object"));
					
					treePanel.addObject(objectNode, obj);
				}
				/*
				 * Enumerate the edit masks.
				 */
				for (MEditMask editMask : resource.getMEditMasks())
				{
					if (editMaskNode == null)
						editMaskNode = treePanel.addObject(resourceNode, new String("EditMask"));
					
					treePanel.addObject(editMaskNode, editMask);
				}
				/*
				 * Enumerate the search help.
				 */
				for (MSearchHelp searchHelp : resource.getMSearchHelps())
				{
					if (searchHelpNode == null)
						searchHelpNode = treePanel.addObject(resourceNode, new String("SearchHelp"));
					
					treePanel.addObject(searchHelpNode, searchHelp);
				}
				/*
				 * Enumerate the update help.
				 */
				for (MUpdateHelp updateHelp : resource.getMUpdateHelps())
				{
					if (updateHelpNode == null)
						updateHelpNode = treePanel.addObject(resourceNode, new String("UpdateHelp"));
					
					treePanel.addObject(updateHelpNode, updateHelp);
				}
				/*
				 * Enumerate the validation lookups.
				 */
				for (MValidationLookup lookup : resource.getMValidationLookups())
				{
					if (validationLookupNode == null)
						validationLookupNode = treePanel.addObject(resourceNode, new String("ValidationLookup"));
					
					MetadataTreeNode node = treePanel.addObject(validationLookupNode, lookup);
					/*
					 * Enumerate the validation lookup types.
					 */
					for (MValidationLookupType lookupType : lookup.getMValidationLookupTypes())
					{
						treePanel.addObject(node, lookupType);
					}
				}
				/*
				 * Enumerate the validation externals.
				 */
				for (MValidationExternal validation : resource.getMValidationExternal())
				{
					if (validationExternalNode == null)
						validationExternalNode = treePanel.addObject(resourceNode, new String("ValidationExternal"));
					
					MetadataTreeNode node = treePanel.addObject(validationExternalNode, validation);
					/*
					 * Enumerate the validation external types.
					 */
					for (MValidationExternalType externalType : validation.getMValidationExternalTypes())
					{
						treePanel.addObject(node, externalType);
					}
				}
				/*
				 * Enumerate the validation expressions.
				 */
				for (MValidationExpression validationExpression : resource.getMValidationExpressions())
				{
					if (validationExpressionNode == null)
						validationExpressionNode = treePanel.addObject(resourceNode, new String("ValidationExpression"));
					
					treePanel.addObject(validationExpressionNode, validationExpression);
				}
			}
		}
    }
    
    private void updateComponentsFromSelection()
    {
    	System.out.println("updateComponentsFromSelection()."); // $$DEBUG
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
    	System.out.println("refreshTab()"); // $$DEBUG
        mMetadataMenu.setEnabled(true);
    	//if (true)  // $$DEBUG
    		//return;// $$DEBUG
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                List groups = Collections.EMPTY_LIST;
                try
                {
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    // Todo: MetadataPanel.construct show error dialog
                }
                return groups;
            }

            public void finished()
            {
                mMetadata = RetsConfig.getMetadata();
                mSystem = mMetadata.getSystem();
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
            }
        };
        worker.start();
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
    	               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
    	     *    
    	     * The general states for the "ADD" button are:
    	     * 
			 * Node Selected			Action
			 * -------------			-------------------------
			 * "System"					Add not allowed.
			 * "ForeignKey"				Add foreign key.
			 * "Resource"				Add MResource.
			 * "Lookups"				Add MLookup.
			 * "Object"					Add MObject.
			 * "EditMask"				Add MEditMask.
			 * "SearchHelp"				Add MSearchHelp.
			 * "UpdateHelp"				Add MUpdateHelp.
			 * "ValidationLookup"		Add MValidationLookup.
			 * "ValidationExternal"		Add MValidationExternal.
			 * "ValidationExpression"	Add MValidationExpression.
			 * MResource				Add MClass, MLookup, MObject, MEditMask
			 * 								MSearchHelp, MUpdateHelp, MValidationLookup,
			 * 								MValidationExternal or MValidationExpression.
			 * MClass					Add MTable or MUpdate.
			 * MTable					Add not allowed.
			 * MUpdate					Add MUpdateType.
			 * MLookup					Add MLookupType.
			 * MLookupType				Add not allowed.
			 * MObject					Add not allowed.
			 * MEditMask				Add not allowed.
			 * MSearchHelp				Add not allowed.
			 * MUpdateHelp				Add not allowed.
			 * MValidationLookup		Add MValidationLookupType.
			 * MValidationLookupType	Add not allowed.
			 * MValidationExternal		Add MValidationExternalType
			 * MValidationExternalType	Add not allowed.
			 * MValidationExpression	Add not allowed.
    	     */
    	    tree.addTreeSelectionListener(new TreeSelectionListener() 
    	    {
    	    	public void valueChanged(TreeSelectionEvent e)
    	    	{
    	    		final MetadataTreeNode selectedNode = (MetadataTreeNode)tree.getLastSelectedPathComponent();

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
        	    	    mAddButtonListener = new ActionListener()
        	    	    {
        	    	    	public void actionPerformed(ActionEvent e)
        	    	    	{
        	    	    		MetadataTreeNode node = selectedNode;
        	    	    		MetadataType metadataType = null;
        	    	    		MetaObject resource = null;
        	    	    		
        	    	    		if (userObject instanceof MResource)        	    	    		
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
			    	    			
			    	    			String answer = (String) JOptionPane.showInputDialog(
			    	    											SwingUtils.getAdminFrame(),
			    	    											"Add Which?",
			    	    											"Add to Resource",
			    	    											JOptionPane.QUESTION_MESSAGE,
			    	    											null,
			    	    											ddl,
			    	    											"Class");
			    	    			if (answer == null)
			    	    				return;
			    	    			
		   	    	    			if (answer.equals("Class"))
			    	    			{
		    	    	    			metadataType = MetadataType.CLASS;
		    	    	    			resource = new MClass();
			    	    			}
			    	    			else       	    	    			
		   	    	    			if (answer.equals("Lookup"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "Lookups" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("Lookups"))
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
		   	    	    					node = addObject(selectedNode, new String ("Lookups"));
		   	    	    				}
		    	    	    			metadataType = MetadataType.LOOKUP;
		    	    	    			resource = new MLookup();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("Object"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "Object" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("Object"))
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
		   	    	    					node = addObject(selectedNode, new String ("Object"));
		   	    	    				}
		    	    					metadataType = MetadataType.OBJECT;
		    	    	    			resource = new MObject();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("EditMask"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "EditMask" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("EditMask"))
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
		   	    	    					node = addObject(selectedNode, new String ("EditMask"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.EDITMASK;
		    	    	    			resource = new MEditMask();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("SearchHelp"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "SearchHelp" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("SearchHelp"))
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
		   	    	    					node = addObject(selectedNode, new String ("SearchHelp"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.SEARCH_HELP;
		    	    	    			resource = new MSearchHelp();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("UpdatehHelp"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "UpdateHelp" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("UpdateHelp"))
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
		   	    	    					node = addObject(selectedNode, new String ("UpdateHelp"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.UPDATE_HELP;
		    	    	    			resource = new MUpdateHelp();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("ValidationLookup"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "ValidationLookup" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("ValidationLookup"))
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
		   	    	    					node = addObject(selectedNode, new String ("ValidationLookup"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.VALIDATION_LOOKUP;
		    	    	    			resource = new MValidationLookup();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("ValidationExternal"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "ValidationExternal" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("ValidationExternal"))
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
		   	    	    					node = addObject(selectedNode, new String ("ValidationExternal"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.VALIDATION_EXTERNAL;
		    	    	    			resource = new MValidationExternal();
			    	    			}
			    	    			else       	    	    			
		    	    				if (answer.equals("ValidationExpression"))
			    	    			{
		   	    	    				boolean found = false;
		   	    	    				/*
		   	    	    				 * Determine if the "ValidationExpression" node is already in the tree. If so, switch
		   	    	    				 * to it. Otherwise, add it.
		   	    	    				 */
		   	    	    				for (int i = 0; i < selectedNode.getChildCount(); i++)
		   	    	    				{
		   	    	    					MetadataTreeNode treeNode = (MetadataTreeNode)selectedNode.getChildAt(i);
		   	    	    					Object userObject = treeNode.getUserObject();
		   	    	    					if (userObject instanceof String && userObject.toString().equals("ValidationExpression"))
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
		   	    	    					node = addObject(selectedNode, new String ("ValidationExpression"));
		   	    	    				}

		    	    	    			metadataType = MetadataType.VALIDATION_EXPRESSION;
		    	    	    			resource = new MValidationExpression();
			    	    			}
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MClass)
        	    	    		{
        	    	    			/*
        	    	    			 * We can only add Table and Update metadata to METADATA-CLASS.
        	    	    			 */
        	    	    			Object[] ddl = {"Table",
        	    	    							"Update"};
        	    	    			
        	    	    			String answer = (String) JOptionPane.showInputDialog(
        	    	    											SwingUtils.getAdminFrame(),
        	    	    											"Add Which?",
        	    	    											"Add to Class",
        	    	    											JOptionPane.QUESTION_MESSAGE,
        	    	    											null,
        	    	    											ddl,
        	    	    											"Table");
        	    	    			if (answer == null)
        	    	    				return;
        	    	    			
        	    	    			if (answer.equals("Table"))
        	    	    			{
            	    	    			metadataType = MetadataType.TABLE;
            	    	    			resource = new MTable();
        	    	    			}
        	    	    			else
           	    	    			if (answer.equals("Update"))
        	    	    			{
            	    	    			metadataType = MetadataType.UPDATE;
            	    	    			resource = new MUpdate();
        	    	    			}
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MEditMask)
        	    	    		{
           	    	    			metadataType = MetadataType.EDITMASK;
        	    	    			resource = new MEditMask();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MForeignKey)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.FOREIGNKEYS;
        	    	    			resource = new MForeignKey();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MLookup)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.LOOKUP_TYPE;
        	    	    			resource = new MLookupType();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MLookupType)        	    	    		
        	    	    		{
           	    	    			metadataType = MetadataType.LOOKUP_TYPE;
        	    	    			resource = new MLookupType();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MObject)        	    	    		
        	    	    		{
           	    	    			metadataType = MetadataType.OBJECT;
        	    	    			resource = new MObject();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MSearchHelp)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.SEARCH_HELP;
        	    	    			resource = new MSearchHelp();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MSystem)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MTable)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MUpdate)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MUpdateHelp)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MUpdateType)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MValidationExpression)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MValidationExternal)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MValidationExternalType)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MValidationLookup)        	    	    		
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
        	    	    		else
        	    	    		if (userObject instanceof MValidationLookupType)
        	    	    		{
        	    	    			metadataType = MetadataType.CLASS;
        	    	    			resource = new MClass();
        	    	    		}
          	    	    		/*
        	    	    		 * Instantiate the default or custom dialog, depending on the type of
        	    	    		 * the metadata.
        	    	    		 */      	    	    		
        	    	    		MetadataDialog dialog = new MetadataGenericDialog(mStrictParsing, mMetadata, resource);
           	    	    		
        	    	    		if (resource instanceof MTable)
        	    	    		{
        	    	    			/*
        	    	    			 * UserObject had better be an MClass, but check anyway.
        	    	    			 */
        	    	    			if (userObject instanceof MClass)
        	    	    			{
            	    	    			/*
            	    	    			 * Get the parent Resource and invoke the custom dialog.
            	    	    			 */
           	    	    				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            	    	    			Object parentObject = parentNode.getUserObject();
            	    	    			dialog = new MetadataTableDialog(mStrictParsing,
            	    	    											mMetadata, 
            	    	    											(MResource)parentObject, 
            	    	    											(MTable)resource);
        	    	    			}
        	    	    			else // $$DEBUG
        	    	    				System.out.println("*** userObject is not MClass when dealing with a table!!!"); // $$DEBUG
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
        	    	    		System.out.println("Dialog returns: " + dialog.getResponse()); // $$DEBUG
        	    	    		if (dialog.getResponse() == JOptionPane.OK_OPTION)
        	    	    		{
        	    	    			if (((MetaObject)userObject).getChild(metadataType, resource.getId()) == null)
        	    	    			{
	        	    	    			/*
	        	    	    			 * Add the object to the tree and to the metadata. 
	        	    	    			 */
	               	    	    		MetadataTreeNode newNode = addObject(node, resource);
	               	    	    		//tree.setSelectionPath(new TreePath(newNode.getPath()));
	    	    						((MetaObject)userObject).addChild(metadataType, resource);
	
	    	    						Admin.setRetsConfigChanged(true);
	        	    	                //refreshMainPanel(resource);
	    	    						refreshMainPanel(userObject);
	        	            	    	debugPrint(); // $$DEBUG
        	    	    			}
        	    	    			else
        	    	    			{
    	       	    	    			JOptionPane.showMessageDialog(
												SwingUtils.getAdminFrame(),
												((MetaObject)resource).getId() + " already exists!");
        	    	    			}
        	    	    		}
        	    	    	}
        	    	    };
        	    	    mAddButton.addActionListener(mAddButtonListener);
        	    	    
        	    	    mEditButton.removeActionListener(mEditButtonListener);
        	    	    mEditButtonListener = new ActionListener()
        	    	    {
        	    	    	public void actionPerformed(ActionEvent e)
        	    	    	{
        	    	    		/*
        	    	    		 * Instantiate the default or custom dialog, depending on the type of
        	    	    		 * the metadata.
        	    	    		 */
        	    	    		MetadataDialog dialog = new MetadataGenericDialog(mStrictParsing, mMetadata, userObject);
        	    	    		
        	    	    		if (userObject instanceof MTable)
        	    	    		{
        	    	    			/*
        	    	    			 * Get the parent Resource and invoke the custom dialog.
        	    	    			 */
       	    	    				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
        	    	    			Object parentObject = parentNode.getUserObject();
        	    	    			if (parentObject instanceof MResource)
        	    	    				dialog = new MetadataTableDialog(mStrictParsing,
        	    	    												mMetadata, 
        	    	    												(MResource) parentObject,
        	    	    												(MTable) userObject);
        	    	    			else // $$DEBUG
        	    	    				System.out.println("*** Can't locate parent Resource." ); // $$DEBUG
        	    	    		}
        	    	    		else
           	    	    		if (userObject instanceof MResource)
        	    	    		{
        	    	    			/*
        	    	    			 * Invoke the custom dialog.
        	    	    			 */
       	    	    				dialog = new MetadataResourceDialog(mStrictParsing,
        	    	    												mMetadata, 
        	    	    												(MResource) userObject);
        	    	    		}
           	    	    		else
           	    	    		if (userObject instanceof MClass)
        	    	    		{
        	    	    			/*
        	    	    			 * Invoke the custom dialog.
        	    	    			 */
       	    	    				dialog = new MetadataClassDialog(mStrictParsing,
        	    	    												mMetadata, 
        	    	    												(MClass) userObject);
        	    	    		}
           	    	    		
        	    	    		dialog.setVisible(true);
        	    	    		System.out.println("Dialog returns: " + dialog.getResponse()); // $$DEBUG
        	    	    		//tree.validate(); // $$DEBUG
        	    	    		if (dialog.getResponse() == JOptionPane.OK_OPTION)
        	    	    		{
        	    	                Admin.setRetsConfigChanged(true);
        	    	                refreshMainPanel(userObject);
        	            	    	debugPrint(); // $$DEBUG
        	    	    		}
        	    	    	}
        	    	    };
        	    	    
        	    	    mEditButton.addActionListener(mEditButtonListener);

        	    	    mEditButton.setEnabled(true);
        	    	    
        	    	    mDeleteButton.removeActionListener(mDeleteButtonListener);
        	    	    mDeleteButtonListener = new ActionListener()
        	    	    {
        	    	    	public void actionPerformed(ActionEvent e)
        	    	    	{
        	    	    		if (userObject instanceof MetaObject)
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
	       	    	    				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
	        	    	    			Object parentObject = parentNode.getUserObject();
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
		       	    													((MetaObject)userObject).getMetadataType(), 
		       	    													(MetaObject)userObject);		       	    						
		        	    	                removeCurrentNode();
		        	    	                /*
		        	    	                 * And set the parent as selected.
		        	    	                 */
		              	    	    		tree.setSelectionPath(new TreePath(parentNode.getPath()));		       	    						
		        	    	                
		        	    	                refreshMainPanel(parentObject);
		        	            	    	debugPrint(); // $$DEBUG
	        	    	    			}
	        	    	    		}
        	    	    		}
        	    	    	}
        	    	    };
        	    	    mDeleteButton.addActionListener(mDeleteButtonListener);
        	    	    mDeleteButton.setEnabled(true);
    	    			
        	    	    if (userObject instanceof MResource
        	    	    	||	userObject instanceof MClass
        	    	    	||	userObject instanceof MUpdate
        	    	    	||	userObject instanceof MLookup
        	    	    	||	userObject instanceof MValidationLookup
        	    	    	||	userObject instanceof MValidationExternal)
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
                   	    	    		MetadataTreeNode newNode = addObject(mForeignKeyNode, resource);
                   	    	    		tree.setSelectionPath(new TreePath(newNode.getPath()));

   	    	    						mSystem.addChild(MetadataType.FOREIGNKEYS, resource);

   	    	    						Admin.setRetsConfigChanged(true);
            	    	                refreshMainPanel(resource);
            	            	    	debugPrint(); // $$DEBUG
            	    	    		}
            	    	    	}
            	    	    };
            	    	    mAddButton.addActionListener(mAddButtonListener);
        	    	    	mAddButton.setEnabled(true);
        	    	    	// FIXME: not implemented
            	    	    mDeleteButton.removeActionListener(mDeleteButtonListener);
            	    	    mDeleteButtonListener = new ActionListener()
            	    	    {
            	    	    	public void actionPerformed(ActionEvent e)
            	    	    	{
            	    	    		if (userObject instanceof MetaObject)
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
    	       	    	    				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
    	        	    	    			Object parentObject = parentNode.getUserObject();
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
    		       	    													((MetaObject)userObject).getMetadataType(), 
    		       	    													(MetaObject)userObject);		       	    						
    		        	    	                removeCurrentNode();
    		        	    	                /*
    		        	    	                 * And set the parent as selected.
    		        	    	                 */
    		              	    	    		tree.setSelectionPath(new TreePath(parentNode.getPath()));		       	    						
    		        	    	                
    		        	    	                refreshMainPanel(parentObject);
    		        	            	    	debugPrint(); // $$DEBUG
    	        	    	    			}
    	        	    	    		}
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
                   	    	    		MetadataTreeNode newNode = addObject(mResourceNode, resource);
                   	    	    		tree.setSelectionPath(new TreePath(newNode.getPath()));

   	    	    						mSystem.addChild(MetadataType.RESOURCE, resource);

   	    	    						Admin.setRetsConfigChanged(true);
            	    	                refreshMainPanel(resource);
            	            	    	debugPrint(); // $$DEBUG
            	    	    		}
            	    	    	}
            	    	    };
            	    	    mAddButton.addActionListener(mAddButtonListener);
        	    	    	mDeleteButton.setEnabled(true);
        	    	    	// FIXME: Delete not implemented yet
        	    	    }
        	    	    else
        	    	    {
    	    	    		/*
    	    	    		 * One of the Strings under a given resource has been selected 
    	    	    		 * (e.g. "Lookups", "Object", etc. Set up the buttons for this
    	    	    		 * context.
    	    	    		 */
    	    	    		// FIXME: Need to implement this code.
        	    	    	System.out.println("**** Misc string selected!!"); // $$DEBUG
        	    	    }

        	    	    mMainPanel.removeAll();
        	    	    mMainPanel.add(new JLabel(""));
    	    			mSplitPane.revalidate();
    	    			
	    				System.out.println("selected " + userObject); // $$DEBUG
	    			}
        	    	debugPrint(); // $$DEBUG
    	    	}
    	    });
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
    	      MetadataTreeNode currentNode = (MetadataTreeNode) (currentSelection
    	          .getLastPathComponent());
    	      MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
    	      if (parent != null) 
    	      {
    	          treeModel.removeNodeFromParent(currentNode);
    	          // TODO: Remove the corresponding entry from mMetadata.
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
    		tree.setSelectionPath(tree.getPathForRow(0));
    	}

    	public void debugPrint()
    	{
			for (MForeignKey foreignKey : mSystem.getMForeignKeys())
			{
				System.out.println(foreignKey.getMetadataTypeName());
			}
			
			for (MResource resource : mSystem.getMResources())
			{
				System.out.println(resource.getMetadataTypeName() + ": " + resource.getId());
				/*
				 * Enumerate the RETS classes and populate the tree.
				 */
				for (MClass clazz : resource.getMClasses())
				{
					System.out.println("  " + clazz.getMetadataTypeName() + ": " + clazz.getId());
					/*
					 * Iterate over the tables for this class.
					 */
					for (MTable table : clazz.getMTables())
					{
						System.out.println("    " + table.getMetadataTypeName() + ": " + table.getId());
					}
					/*
					 * Iterate over the updates for this class.
					 */
					for (MUpdate update : clazz.getMUpdates())
					{
						System.out.println("    " + update.getMetadataTypeName() + ": " + update.getId());
						/*
						 * Iterate over the update types for this update.
						 */
						for (MUpdateType updateType: update.getMUpdateTypes())
						{
							System.out.println("    " + updateType.getMetadataTypeName() + ": " + updateType.getId());
						}
					}
				}
				/*
				 * Enumerate the lookups.
				 */
				for (MLookup lookup : resource.getMLookups())
				{
					System.out.println("  " + lookup.getMetadataTypeName() + ": " + lookup.getId());
					
					for (MLookupType lookupType : lookup.getMLookupTypes())
					{
						System.out.println("    " + lookupType.getMetadataTypeName() + ": " + lookupType.getId());
					}
				}
				/*
				 * Enumerate the objects.
				 */
				for (MObject obj : resource.getMObjects())
				{
					System.out.println("  " + obj.getMetadataTypeName() + ": " + obj.getId());
				}
				/*
				 * Enumerate the edit masks.
				 */
				for (MEditMask editMask : resource.getMEditMasks())
				{
					System.out.println("  " + editMask.getMetadataTypeName() + ": " + editMask.getId());
				}
				/*
				 * Enumerate the search help.
				 */
				for (MSearchHelp searchHelp : resource.getMSearchHelps())
				{
					System.out.println("  " + searchHelp.getMetadataTypeName() + ": " + searchHelp.getId());
				}
				/*
				 * Enumerate the update help.
				 */
				for (MUpdateHelp updateHelp : resource.getMUpdateHelps())
				{
					System.out.println("  " + updateHelp.getMetadataTypeName() + ": " + updateHelp.getId());
				}
				/*
				 * Enumerate the validation lookups.
				 */
				for (MValidationLookup lookup : resource.getMValidationLookups())
				{
					System.out.println("  " + lookup.getMetadataTypeName() + ": " + lookup.getId());
					/*
					 * Enumerate the validation lookup types.
					 */
					for (MValidationLookupType lookupType : lookup.getMValidationLookupTypes())
					{
						System.out.println("    " + lookupType.getMetadataTypeName() + ": " + lookupType.getId());
					}
				}
				/*
				 * Enumerate the validation externals.
				 */
				for (MValidationExternal validation : resource.getMValidationExternal())
				{
					System.out.println("  " + validation.getMetadataTypeName() + ": " + validation.getId());
					/*
					 * Enumerate the validation external types.
					 */
					for (MValidationExternalType externalType : validation.getMValidationExternalTypes())
					{
						System.out.println("    " + externalType.getMetadataTypeName() + ": " + externalType.getId());
					}
				}
				/*
				 * Enumerate the validation expressions.
				 */
				for (MValidationExpression validationExpression : resource.getMValidationExpressions())
				{
					System.out.println("  " + validationExpression.getMetadataTypeName() + ": " + validationExpression.getId());
				}
			}
		}
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataPanel.class);

    private JButton				mAddButton;
    private ActionListener		mAddButtonListener;
    private JButton				mEditButton;
    private ActionListener		mEditButtonListener;
    private JButton				mDeleteButton;
    private ActionListener		mDeleteButtonListener;
    private MetadataTreePanel 	mTreePanel;
    private JPanel				mMainPanel;
    private JSplitPane			mSplitPane;
    private JScrollPane			mTextScrollPane;
    private JPopupMenu 			mPopup;
    private JMenu 				mMetadataMenu;
    private boolean				mStrictParsing;
    private JMenuItem			mStrictParsingMenuItem;
    
    private Metadata 			mMetadata;
    private MSystem				mSystem;
    
	private MetadataTreeNode 	mEditMaskNode				= null;
	private MetadataTreeNode	mForeignKeyNode				= null;
	private MetadataTreeNode 	mLookupNode					= null;
	private MetadataTreeNode 	mObjectNode					= null;
	private MetadataTreeNode 	mResourceNode				= null;
	private MetadataTreeNode 	mSearchHelpNode				= null;
	private MetadataTreeNode 	mUpdateHelpNode				= null;
	private MetadataTreeNode 	mValidationLookupNode		= null;
	private MetadataTreeNode 	mValidationExternalNode		= null;
	private MetadataTreeNode 	mValidationExpressionNode	= null;
}
