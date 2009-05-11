package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
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
import org.realtors.rets.common.metadata.attrib.AttrDate;
import org.realtors.rets.common.metadata.attrib.AttrEnum;
import org.realtors.rets.common.metadata.attrib.AttrPositiveNumeric;
import org.realtors.rets.common.metadata.attrib.AttrVersion;
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

public class MetadataClassDialog extends MetadataDialog
{
    public MetadataClassDialog(boolean strictParsing, Metadata metadata, MClass clazz)
    {
        super(SwingUtils.getAdminFrame());
        
        mClass = clazz;
        mStrictParsing = strictParsing;
        
        setModal(true);
        setTitle(mClass.getMetadataTypeName());

        JPanel panel = new JPanel(new SpringLayout());
        int rows = 0;
        int empty = 0;
        mComponents = new ArrayList<JComponent>(mClass.getAttributeNames().length);
        
        for (String attribute : mClass.getAttributeNames())
        {
            AttrType<?>   attrType     	= mClass.getAttributeType(attribute);
            JComponent    component;
            JLabel        label         = new JLabel(attribute + ":", JLabel.TRAILING);
            String        value         = mClass.getAttributeAsString(attribute);
            
            if (value == null)
            {
                value = new String("");
                empty++;
            }
            
            if (attrType instanceof AttrBoolean)
            {
                // FIXME: We should check boolean/lookup and take the values from LookupTypes. See RETS spec.
                   if (value.equals("0"))
                    value = "False";
                if (value.equals("1"))
                    value = "True";
                component = new JComboBox(sBoolean);
                ((JComboBox)component).setSelectedItem(value);
            }
            else
            if (attrType instanceof AttrDate)
            {
                /*
                 * This will be one of the date versions. If the value is empty, populate
                 * it from the System version date.
                 */
                component = new JTextField(TEXT_WIDTH);
                if (value == null || value.length() == 0)
                {
                    value = metadata.getSystem().getAttributeAsString(MSystem.DATE);
                }
                ((JTextField)component).setText(value);
            }
            else
            if (attrType instanceof AttrEnum)
            {
                if (value.length() > 0)
                {
                    String [] values = ((AttrEnum)attrType).toArray();
                    component = new JComboBox(values);
                    ((JComboBox)component).setSelectedItem(value);
                }
                else
                {
                    component = new JComboBox();
                    ((JComboBox)component).setEnabled(false);
                }
            }
            else
            if (attrType instanceof AttrVersion)
            {
                /*
                 * This will be one of the versions. If the value is empty, populate
                 * it from the System version.
                 */
            	component = new JTextField(TEXT_WIDTH);

                if (value == null || value.length() == 0)
                {
                    value = metadata.getSystem().getAttributeAsString(MSystem.VERSION);
                }
                ((JTextField)component).setText(value);
            }
            else
            {
                component = new JTextField(TEXT_WIDTH);
                ((JTextField)component).setText(value);
            }

            if (attrType != null)
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
            int len = mClass.getAttributeNames().length;
            int row = 0;
            List<String> invalid = new ArrayList<String>();
            
            /*
             * Reset the background color for the TextFields.
             */
            for (int i = 0; i < len; i++)
            {
                mComponents.get(i).setBackground(mTextFieldColor);
            }
            
            for (String attribute : mClass.getAttributeNames())
            {
                AttrType<?> attrType    = mClass.getAttributeType(attribute);
                String value = null;
                                
                if (mComponents.get(row) instanceof JTextField)
                {
                    value = ((JTextField)mComponents.get(row)).getText();
                }
                else
                if (mComponents.get(row) instanceof JComboBox)
                {
                    value = (String)((JComboBox)mComponents.get(row)).getSelectedItem();
                    if (attrType instanceof AttrBoolean)
                    {
                        if (value == null)
                            value = "0";
                        else
                        if (value.equals("True"))
                            value = "1";
                        else
                        if (value.equals("False"))
                            value = "0";
                    }
                }
                
                if ((value == null || value.length() < 1) && 
                        mClass.isAttributeRequired(attribute))
                {
                    mComponents.get(row).setBackground(Color.pink);
                    invalid.add(attribute);
                }
                else
                    /*
                     * Good to go. Clear the old value if it isn't a required field.
                     */
                    try 
                    { 
                        if (!mClass.isAttributeRequired(attribute))
                            mClass.setAttribute(attribute, "", false);
                    } 
                    catch (Exception e) {};
                    
                
                if (value != null && value.length() > 0)
                {
                    try
                    {
                        mClass.setAttribute(attribute, value, mStrictParsing);
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
                    
                    AttrType<?> attrType = mClass.getAttributeType(attribute);
                    
                    if (mClass.isAttributeRequired(attribute))
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
    
    private MClass              mClass;
    private List<JComponent>    mComponents;
    private boolean             mStrictParsing;
    private Color               mTextFieldColor;

}
