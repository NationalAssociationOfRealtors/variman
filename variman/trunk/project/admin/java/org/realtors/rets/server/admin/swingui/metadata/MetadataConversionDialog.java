package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.attrib.AttrAlphanum;
import org.realtors.rets.common.metadata.attrib.AttrBoolean;
import org.realtors.rets.common.metadata.attrib.AttrEnum;
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
import org.realtors.rets.server.admin.swingui.AdminFrame;
import org.realtors.rets.server.admin.swingui.MetadataPanel;
import org.realtors.rets.server.admin.swingui.SwingUtils;
import org.realtors.rets.server.admin.swingui.SwingWorker;
import org.realtors.rets.server.admin.swingui.TextValuePanel;
import org.realtors.rets.server.config.RetsConfig;

public class MetadataConversionDialog extends MetadataDialog
{
    public MetadataConversionDialog(JMenu metadataMenu)
    {
        super(SwingUtils.getAdminFrame());
        
        
        setModal(true);
        setTitle("Metadata Conversion");

		int answer = (int) JOptionPane.showConfirmDialog(
					SwingUtils.getAdminFrame(),
					"This will process your existing metadata files and\n" +
					"attempt to convert them into RETS 1.7.2 compatible\n" +
					"format. The resultant file will be \"metadata.xml\"\n" +
					"and will be left in your configured metadata directory.\n" +
					"Variman and the admin tool will then use that file as\n" +
					"the primary metadata source from this point on. You\n" + 
					"may then archive and delete your old metadata files.",
					"Please Confirm",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
		if (answer != JOptionPane.OK_OPTION)
		{
        	setResponse(JOptionPane.CANCEL_OPTION);
    		setVisible(false);
			return;
		}
		
		SwingWorker worker = new SwingWorker()
        {
        	
            public Object construct()
            {
                AdminFrame.getInstance().setStatusText("Converting Metadata");

                try
                {
                    mSystem = RetsConfig.getMetadata().getSystem();
                	updateStatus("Converting System");

                	if (mSystem.getTimeZoneOffset() == null || mSystem.getTimeZoneOffset().length() < 1)
                		mSystem.setAttribute(mSystem.TIMEZONEOFFSET, "-00:00");
                	
                	/*
                	 * The NAR sample metadata used the tag "Comment" and if anyone used that as a starting
                	 * point, the Comment tag needs to be converted to COMMENT.
                	 */
                	if (mSystem.getAttributeAsString("Comments") != null)
                		mSystem.setAttribute(mSystem.COMMENTS, mSystem.getAttributeAsString("Comments"));
                	
                	for (MForeignKey foreignKey : mSystem.getMForeignKeys())
        			{
        				updateStatus("Converting ForeignKey " + foreignKey.getForeignKeyID());
        				cleanupUnderscores(foreignKey);
        			}
        			
        			for (MResource resource : mSystem.getMResources())
        			{
        				updateStatus("Converting Resource " + resource.getId());
        				cleanupUnderscores(resource);
        				
        				/*
        				 * Enumerate the RETS classes.
        				 */
        				for (MClass clazz : resource.getMClasses())
        				{
        					updateStatus("Converting Class " + clazz.getId());
        					if (clazz.getAttributeAsString("DBname") != null)
        						clazz.setAttribute("X-DBName", clazz.getAttributeAsString("DBName"));
        					cleanupUnderscores(clazz);
        					/*
        					 * Iterate over the tables for this class.
        					 */
        					for (MTable table : clazz.getMTables())
        					{
        						updateStatus("Converting Table " + table.getId());
        						if (table.getSystemName() != null)
        							table.setAttribute(table.METADATAENTRYID, table.getSystemName());
        						cleanupUnderscores(table);
        					}
        					/*
        					 * Iterate over the updates for this class.
        					 */
        					for (MUpdate update : clazz.getMUpdates())
        					{
        						updateStatus("Converting Update " + update.getId());
        						if (update.getUpdateName() != null)
        							update.setAttribute(update.METADATAENTRYID, update.getUpdateName());
        						/*
        						 * "Date" and "Version" must be converted to "UpdateTypeDate" and "UpdateTypeVersion".
        						 */
        		               	if (update.getAttributeAsString("Date") != null)
        	                		update.setAttribute(update.UPDATETYPEDATE, update.getAttributeAsString("Date"));
        		               	if (update.getAttributeAsString("Version") != null)
        	                		update.setAttribute(update.UPDATETYPEVERSION, update.getAttributeAsString("Version"));
        						cleanupUnderscores(update);
        		               	/*
        						 * Iterate over the update types for this update.
        						 */
        						for (MUpdateType updateType: update.getMUpdateTypes())
        						{
        							updateStatus("Converting UpdateType " + updateType.getId());
        							if (updateType.getSystemName() != null)
        								updateType.setAttribute(updateType.METADATAENTRYID, updateType.getSystemName());
        							cleanupUnderscores(updateType);
        						}
        					}
        				}
        				/*
        				 * Enumerate the lookups.
        				 */
        				for (MLookup lookup : resource.getMLookups())
        				{
        					updateStatus("Converting Lookup " + lookup.getId());
        					if (lookup.getLookupName() != null)
        						lookup.setAttribute(lookup.METADATAENTRYID, lookup.getLookupName());
        					cleanupUnderscores(lookup);
        					
        					for (MLookupType lookupType : lookup.getMLookupTypes())
        					{
        						updateStatus("Converting LookupType " + lookupType.getId());
        						if (lookupType.getShortValue() != null)
        							lookupType.setAttribute(lookupType.METADATAENTRYID, lookupType.getShortValue());
        						cleanupUnderscores(lookupType);
        					}
        				}
        				/*
        				 * Enumerate the objects.
        				 */
        				for (MObject obj : resource.getMObjects())
        				{
        					updateStatus("Converting Object " + obj.getId());
        					int length = obj.getVisibleName().length();
        					if (length > 0)
        					{
        						if (length > 32)
        							length = 32;
        						obj.setAttribute(obj.METADATAENTRYID, obj.getVisibleName().substring(0, length - 1));
        					}
    						cleanupUnderscores(obj);
       					}
        				/*
        				 * Enumerate the edit masks.
        				 */
        				for (MEditMask editMask : resource.getMEditMasks())
        				{
        					updateStatus("Converting EditMask " + editMask.getId());
        					if (editMask.getEditMaskID() != null)
        						editMask.setAttribute(editMask.METADATAENTRYID, editMask.getEditMaskID());
        					cleanupUnderscores(editMask);
        				}
        				/*
        				 * Enumerate the search help.
        				 */
        				for (MSearchHelp searchHelp : resource.getMSearchHelps())
        				{
        					updateStatus("Converting SearchHelp " + searchHelp.getId());
        					if (searchHelp.getSearchHelpID() != null)
        						searchHelp.setAttribute(searchHelp.METADATAENTRYID, searchHelp.getSearchHelpID());
        					cleanupUnderscores(searchHelp);
        				}
        				/*
        				 * Enumerate the update help.
        				 */
        				for (MUpdateHelp updateHelp : resource.getMUpdateHelps())
        				{
        					updateStatus("Converting UpdateHelp" + updateHelp.getId());
        					if (updateHelp.getUpdateHelpID() != null)
        						updateHelp.setAttribute(updateHelp.METADATAENTRYID, updateHelp.getUpdateHelpID());
        					cleanupUnderscores(updateHelp);
        				}
        				/*
        				 * Enumerate the validation lookups.
        				 */
        				for (MValidationLookup lookup : resource.getMValidationLookups())
        				{
        					updateStatus("Converting ValidationLookup " + lookup.getId());
        					if (lookup.getValidationLookupName() != null)
        						lookup.setAttribute(lookup.METADATAENTRYID, lookup.getValidationLookupName());
        					cleanupUnderscores(lookup);
        					/*
        					 * Enumerate the validation lookup types.
        					 */
        					for (MValidationLookupType lookupType : lookup.getMValidationLookupTypes())
        					{
        						updateStatus("Converting ValidationLookupType " + lookupType.getId());
        						if (lookupType.getValidText() != null)
        							lookupType.setAttribute(lookupType.METADATAENTRYID, lookupType.getValidText());
        						cleanupUnderscores(lookupType);
        					}
        				}
        				/*
        				 * Enumerate the validation externals.
        				 */
        				for (MValidationExternal validation : resource.getMValidationExternal())
        				{
        					updateStatus("Converting ValidationExternal " + validation.getId());
        					if (validation.getValidationExternalName() != null)
        						validation.setAttribute(validation.METADATAENTRYID, validation.getValidationExternalName());
        					cleanupUnderscores(validation);
        					/*
        					 * Enumerate the validation external types.
        					 */
        					for (MValidationExternalType externalType : validation.getMValidationExternalTypes())
        					{
        						updateStatus("Converting ValidationExternalType " + externalType.getId());
            					int length = externalType.getSearchField().length();
            					if (length > 0)
            					{
            						if (length > 32)
            							length = 32;
            						externalType.setAttribute(externalType.METADATAENTRYID, 
            													externalType.getSearchField().substring(0, length - 1));
            					}
            					cleanupUnderscores(externalType);
        					}
        				}
        				/*
        				 * Enumerate the validation expressions.
        				 */
        				for (MValidationExpression validationExpression : resource.getMValidationExpressions())
        				{
        					updateStatus("Converting ValidationExpression " + validationExpression.getId());
        					if (validationExpression.getValidationExpressionID() != null)
        						validationExpression.setAttribute(validationExpression.METADATAENTRYID, 
        														validationExpression.getValidationExpressionID());
        					cleanupUnderscores(validationExpression);
        				}
        			}
                	setResponse(JOptionPane.OK_OPTION);               	
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    // Todo: MetadataPanel.construct show error dialog
                }
                return mSystem;
            }

            public void finished()
            {
                AdminFrame.getInstance().setStatusText("Metadata converted Successfully");
            }
        };


		mPanel = new JPanel(new BorderLayout());
        mTextArea = new JTextArea();
        mTextArea.setEditable(false);
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);
 
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(mTextArea);
        mPanel.add(scrollPane, BorderLayout.CENTER);
        mPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(mPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getContentPane().add(mPanel);
        pack();
        setResizable(false);
        setSize(580, 360);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
        setResponse(JOptionPane.CANCEL_OPTION);
        worker.start();
        setVisible(true);
    }
    
    private void cleanupUnderscores(MetaObject metaObject)
    {
        for (String attribute : metaObject.getAttributeNames())
        {
        	AttrType<?> attrType	= metaObject.getAttributeType(attribute);
        	String 		value 		= metaObject.getAttributeAsString(attribute);
        	if (attrType instanceof AttrAlphanum && value != null)
        	{
        		value = value.replace("_", "");
        		try
        		{
        			metaObject.setAttribute(attribute, value, false);
        		}
        		catch (Exception e) 
        		{
        			updateStatus(e.toString());
        		} ;
        	}
        }
    }
    
    private void updateStatus(final String msg)
    {
    	SwingUtilities.invokeLater(
    		new Runnable()
    		{
    			public void run()
    			{
    				mTextArea.append(msg + "\n");
    				//mTextArea.setText(mTextArea.getText() + "\n" + msg);
    			}
    		}
    	);
    	if (false) // $$DEBUG
    	try // $$DEBUG
    	{
    		Thread.currentThread().sleep(1000);
    	}
    	catch (InterruptedException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    JPanel mPanel;
    JTextArea mTextArea;
    
    Metadata mMetadata;
    MSystem mSystem;

    private static final Logger LOG =
        Logger.getLogger(MetadataConversionDialog.class);
}
