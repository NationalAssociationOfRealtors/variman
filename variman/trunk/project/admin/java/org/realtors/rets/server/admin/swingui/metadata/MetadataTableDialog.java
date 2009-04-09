package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JTextField;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.attrib.AttrBoolean;
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
import org.realtors.rets.server.admin.swingui.SwingUtils;
import org.realtors.rets.server.admin.swingui.TextValuePanel;
import org.realtors.rets.server.config.RetsConfig;

public class MetadataTableDialog extends MetadataDialog
{
    public MetadataTableDialog(boolean strictParsing, Metadata metadata, MResource resource, MTable table)
    {
        super(SwingUtils.getAdminFrame());
        
        mStrictParsing = strictParsing;
        mTable = table;
        
        setModal(true);
        setTitle(mTable.getMetadataTypeName());

        JPanel panel = new JPanel(new SpringLayout());
        int rows = 0;
        int empty = 0;
        
        mEditMaskKeys.add("");
 		for (MEditMask editMask : resource.getMEditMasks())
		{
			mEditMaskKeys.add(editMask.getEditMaskID());
		}
 		
 		mForeignKeyKeys.add("");
 		for (MForeignKey foreignKey : metadata.getSystem().getMForeignKeys())
 		{
 			mForeignKeyKeys.add(foreignKey.getForeignKeyID());
 		}
		
        mLookupKeys.add("");
        mLookupValues.add("");
		for (MLookup lookup : resource.getMLookups())
		{
			mLookupKeys.add(lookup.getLookupName());
			mLookupValues.add(lookup.getVisibleName());
		}
		
		mSearchHelpKeys.add("");
		for (MSearchHelp searchHelp : resource.getMSearchHelps())
		{
			mSearchHelpKeys.add(searchHelp.getSearchHelpID());
		}
        
        mComponents = new ArrayList<JComponent>(mTable.getAttributeNames().length);
		
        for (String attribute : mTable.getAttributeNames())
        {
        	AttrType<?>	attrType 	= mTable.getAttributeType(attribute);
    		JComponent	component;
       		JLabel		label 		= new JLabel(attribute + ":", JLabel.TRAILING);
        	String		value 		= mTable.getAttributeAsString(attribute);
    		
        	if (value == null)
        	{
        		value = new String("");
        		empty++;
        	}
 
    		if (attribute.equals("DataType"))
    		{
    			mDataTypeBox = new JComboBox(sDataTypes);
    			component = mDataTypeBox;
    			mDataTypeBox.setSelectedItem(value);
    			if (value == null || value.length() == 0)
    				mDataTypeBox.setSelectedItem("Character");
    		}
    		else
    		if (attribute.equals("Interpretation"))
    		{
    			mInterpretationBox = new JComboBox(sInterpretation);
    			component = mInterpretationBox;
    			mInterpretationBox.setSelectedItem(value);
    			mInterpretationBox.addActionListener(new ActionListener()
    			{
    				public void actionPerformed(ActionEvent e)
    				{
    					/*
    					 * If the selected item contains "Lookup", then activate the LookupName
    					 * combobox.
    					 */
    					JComboBox box = (JComboBox)e.getSource();
    					String value = (String)box.getSelectedItem();
    					if (mLookupNameBox != null)
    					{
    						if (value.startsWith("Lookup"))
    							mLookupNameBox.setEnabled(true);
    						else
    							mLookupNameBox.setEnabled(false);
    						// FIXME: Lookup Multi not functional
    					}
    				}
    			});
    		}
    		else
    		if (attribute.equals("Alignment"))
    		{
    			mAlignmentBox = new JComboBox(sAlignment);
    			component = mAlignmentBox;
    			mAlignmentBox.setSelectedItem(value);
    		}
    		else
    		if (attribute.equals("EditMaskID"))
    		{
    			int index = 0;
    			component = new JComboBox(mEditMaskKeys.toArray());
    			for (int i = 0; i < mEditMaskKeys.size(); i++)
    			{
    				if (mEditMaskKeys.get(i).equals(value))
    				{
    					index = i;
    					break;
    				}
    			}
    			((JComboBox)component).setSelectedIndex(index);
    		}
       		else
    		if (attribute.equals("ForeignKeyName"))
    		{
    			int index = 0;
    			mForeignKeyBox = new JComboBox(mForeignKeyKeys.toArray());
    			mForeignKeyBox.addActionListener(new ActionListener()
    			{
    				public void actionPerformed(ActionEvent e)
    				{
    					/*
    					 * If the selected item contains "Lookup", then activate the LookupName
    					 * combobox.
    					 */
    					JComboBox box = (JComboBox)e.getSource();
    					String value = (String)box.getSelectedItem();
    					if (mForeignFieldBox != null)
    					{
    						if (value.length() > 0)
    							mForeignFieldBox.setEnabled(true);
    						else
    							mForeignFieldBox.setEnabled(false);
    						// FIXME: Lookup Multi not functional
    					}
    				}
    			});
    			component = mForeignKeyBox;
       			for (int i = 0; i < mForeignKeyKeys.size(); i++)
    			{
    				if (mForeignKeyKeys.get(i).equals(value))
    				{
    					index = i;
    					break;
    				}
    			}
    			((JComboBox)component).setSelectedIndex(index);
    		}
    		else
	   		if (attribute.equals("ForeignField"))
    		{
    			String ForeignKey = mTable.getAttributeAsString(attribute);
               	mForeignFieldBox = new JTextField(TEXT_WIDTH);
    			component = mForeignFieldBox;
    			if (ForeignKey == null || ForeignKey.length() == 0)
    				mForeignFieldBox.setEnabled(false);
    		}
    		else
    		if (attribute.equals("SearchHelpID"))
    		{
    			int index = 0;
    			component = new JComboBox(mSearchHelpKeys.toArray());
    			for (int i = 0; i < mSearchHelpKeys.size(); i++)
    			{
    				if (mSearchHelpKeys.get(i).equals(value))
    				{
    					index = i;
    					break;
    				}
    			}
    			((JComboBox)component).setSelectedIndex(index);
    		}
       		else
    		if (attribute.equals("Units"))
    		{
    			mUnitsBox = new JComboBox(sUnits);
    			component = mUnitsBox;
    			mUnitsBox.setSelectedItem(value);
    		}
    		else
    		if (attribute.equals("LookupName"))
    		{
    			mLookupNameBox = new JComboBox(mLookupValues.toArray());
    			component = mLookupNameBox;
    			String interpretation = (String)mInterpretationBox.getSelectedItem();   						
    			// FIXME: Lookup Multi not functional
    			if (!interpretation.startsWith("Lookup"))
    				mLookupNameBox.setEnabled(false);
    			else
    			if (value != null)
    			{
    	   			int index = 0;
        			for (int i = 0; i < mLookupValues.size(); i++)
        			{
        				if (mLookupKeys.get(i).equals(value))
        				{
        					index = i;
        					break;
        				}
        			}
    				mLookupNameBox.setSelectedIndex(index);
    			}
    		}
    		else
    		if (attrType instanceof AttrBoolean)
    		{
    			component = new JComboBox(sBoolean);
        		// FIXME: We should check boolean/lookup and take the values from LookupTypes. See RETS spec.
    			if (value.equals("0"))
    				value = "False";
    			if (value.equals("1"))
    				value = "True";
    			((JComboBox)component).setSelectedItem(value);
    		}
    		else
    		{
            	component = new JTextField(TEXT_WIDTH);
            	((JTextField)component).setText(value);
    		}

        	component.setToolTipText(attrType.getDescription());

        	mComponents.add(component);
        	label.setLabelFor(component);
        	panel.add(label);
        	panel.add(component);
        	rows++;
        }
        mTextFieldColor = mComponents.get(0).getBackground();
        SwingUtils.SpringLayoutGrid(panel, rows, 2, 6, 6, 6, 6);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(panel);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        JButton addButton = new JButton(new AddEditButtonAction());
        addButton.setSelected(true);
        buttonBox.add(addButton);
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelButtonAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
        setResponse(JOptionPane.CANCEL_OPTION);
    }

    private class AddEditButtonAction extends AbstractAction
    {
        public AddEditButtonAction()
        {
            super("Save");
        }

        public void actionPerformed(ActionEvent event)
        {
        	int len = mTable.getAttributeNames().length;
        	int row = 0;
        	List<String> invalid = new ArrayList<String>();
        	
        	/*
        	 * Reset the background color for the TextFields.
        	 */
        	for (int i = 0; i < len; i++)
        	{
        		mComponents.get(i).setBackground(mTextFieldColor);
        	}
        	
            for (String attribute : mTable.getAttributeNames())
            {
            	String value = null;
            	
            	if (mComponents.get(row) instanceof JTextField)
            	{
            		value = ((JTextField)mComponents.get(row)).getText();
            	}
            	else
            	if (mComponents.get(row) instanceof JComboBox)
            	{
                   	AttrType<?>	attrType = mTable.getAttributeType(attribute);
            		value = (String)((JComboBox)mComponents.get(row)).getSelectedItem();
            		
            		if (attribute.equals("LookupName"))
            		{
            			/*
            			 * See if the interpretation is a lookup. If so, then then LookupName
            			 * must be processed.
            			 */
            			String interpretation = (String)mInterpretationBox.getSelectedItem();
   						// FIXME: Lookup Multi not functional
            			if (!interpretation.startsWith("Lookup"))
            				value = null;
            			else
            			{
            				/*
            				 * Translate the displayable value to the key.
            				 */
            	   			int index = 0;
                			for (int i = 0; i < mLookupValues.size(); i++)
                			{
                				if (mLookupValues.get(i).equals(value))
                				{
                					index = i;
                					break;
                				}
                			}
                			value = mLookupKeys.get(index);
            			}
            		}
            		if (attrType instanceof AttrBoolean)
            		{
                   		// FIXME: We should check boolean/lookup and take the values from LookupTypes. See RETS spec.
                		if (value.equals("True"))
                			value = "1";
                		else
                		if (value.equals("False"))
                			value = "0";
            		}
            	}
      
              	if ((value == null || value.length() < 1) && 
            			mTable.isAttributeRequired(attribute))
            	{
            		mComponents.get(row).setBackground(Color.pink);
            		invalid.add(attribute);
            	}
            	else
                	/*
                	 * Good to go. Clear the old value if it is not a rqeuired field.
                	 */
                	try 
                	{ 
            			if (!mTable.isAttributeRequired(attribute))
            				mTable.setAttribute(attribute, "", false);
                	} 
                	catch (Exception e) {};
                	
            	
            	if (value != null && value.length() > 0)
            	{
	            	try
	            	{
	            		mTable.setAttribute(attribute, value, mStrictParsing);
	            	}
	            	catch (Exception e)
	            	{
	            		mComponents.get(row).setBackground(Color.pink);
	            		invalid.add(attribute);
	            	}
            	}
            	row++;
            }
        	if (invalid.isEmpty())
        	{
        		setResponse(JOptionPane.OK_OPTION);
        		setVisible(false);
        	}
        	else
        	{
        		String msg = new String();
        		for (String attribute : invalid)
        		{
        			String required = "\n";
        			
        			AttrType<?> attrType = mTable.getAttributeType(attribute);
        			
        			if (mTable.isAttributeRequired(attribute))
        				required = " (REQUIRED)\n";
        			
        			msg += attribute + ": " + attrType.getDescription() + required;
        		}
                JOptionPane.showMessageDialog(
                        SwingUtils.getAdminFrame(),
                        msg,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
        	}
        }
    }

    private class CancelButtonAction extends AbstractAction
    {
        public CancelButtonAction()
        {
            super("Cancel");
        }

        public void actionPerformed(ActionEvent event)
        {
            setResponse(JOptionPane.CANCEL_OPTION);
            setVisible(false);
        }
    }

    private static final String sAlignment [] = {	"Left",
    												"Right",
    												"Center",
    												"Justify"
    											};
    
    private static final String sDataTypes [] = {	"Boolean",
	    											"Character",
	    											"Date", 
	    											"DateTime",
	    											"Time",
	    											"Tiny",
	    											"Small",
	    											"Int",
	    											"Long",
	    											"Decimal"
												};
    
    private static final String sInterpretation [] = {
    												"",
    												"Number",
    												"Currency",
    												"Lookup",
    												"LookupMulti",
    												};
    
    private static final String sUnits [] =		{	"",
    												"Feet",
    												"Meters",
    												"SqFt",
    												"SqMeters",
    												"Acres",
    												"Hectares"
    											};
    
    private boolean mStrictParsing;
    
    private Color 			mTextFieldColor;
    private List<JComponent> mComponents;
    private MTable 			mTable;
    
    private JComboBox		mAlignmentBox = null;
    private JComboBox		mDataTypeBox = null;
    private JComboBox		mForeignKeyBox = null;
    private JTextField		mForeignFieldBox = null;
    private JComboBox		mInterpretationBox = null;
    private JComboBox		mLookupNameBox = null;
    private JComboBox		mSearchHelpIDBox = null;
    private JComboBox		mUnitsBox = null;
    
    private List<String>	mEditMaskKeys = new ArrayList<String>();
    private List<String>	mForeignKeyKeys = new ArrayList<String>();
    private List<String>	mLookupKeys = new ArrayList<String>();
    private List<String>	mLookupValues = new ArrayList<String>();
    private List<String>	mSearchHelpKeys = new ArrayList<String>();
}
