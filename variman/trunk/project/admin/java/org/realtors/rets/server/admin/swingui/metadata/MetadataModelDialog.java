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
import org.realtors.rets.common.metadata.MetaParseException;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
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
import org.realtors.rets.server.admin.swingui.SwingUtils;
import org.realtors.rets.server.admin.swingui.TextValuePanel;
import org.realtors.rets.server.config.RetsConfig;

public class MetadataModelDialog extends MetadataDialog
{
    public MetadataModelDialog(MetaObject object)
    {
        super(SwingUtils.getAdminFrame());
        
        mMetaObject = object;
        
        mAttributeNames = MetaObject.getStandardAttributeNames();
        
        setModal(true);
        setTitle(mMetaObject.getMetadataTypeName());

        mPanel = new JPanel(new SpringLayout());
        mRows = 0;
        int empty = 0;
        mAttributes = new ArrayList<JLabel>(mMetaObject.getAttributeNames().length);
        mAttributeTypes = new ArrayList<JComboBox>(mMetaObject.getAttributeNames().length);
        mRequiredBoxes = new ArrayList<JComboBox>(mMetaObject.getAttributeNames().length);
        
        for (String attribute : mMetaObject.getAttributeNames())
        {
            AttrType<?> attrType        = mMetaObject.getAttributeType(attribute);
               
            JComboBox   component       = new JComboBox(mAttributeNames);;
            JLabel      label           = new JLabel(attribute + ":", JLabel.TRAILING);
            JComboBox   required        = new JComboBox(sRequired);
               
            mAttributes.add(label);
            mAttributeTypes.add(component);
            mRequiredBoxes.add(required);
               
            label.setLabelFor(component);
            component.setSelectedItem(MetaObject.getNameFromAttribute(attrType));
            if (mMetaObject.isAttributeRequired(attribute))
                required.setSelectedIndex(1);
                   
            mPanel.add(label);
            mPanel.add(component);
            mPanel.add(required);
            
            mRows++;
        }

        SwingUtils.SpringLayoutGrid(mPanel, mRows, nCOLS, 6, 6, 6, 6);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(mPanel);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        JButton addButton = new JButton(new AddEditButtonAction());
        addButton.setSelected(true);
        buttonBox.add(addButton);
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new SaveButtonAction()));
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
            super("New");
        }

        public void actionPerformed(ActionEvent event)
        {
            /*
             * Add a component to the dialog for new data.
             */
            String answer = (String) JOptionPane.showInputDialog(
                    SwingUtils.getAdminFrame(),
                    "Attribute Name?",
                    "Add Attribute",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    "X-Attribute");
            
            if (answer != null)
            {
                /*
                 * See if the name is a valid name.
                 */
                char[] chars = answer.toCharArray();
                if (!answer.startsWith("X-"))
                {
                      JOptionPane.showMessageDialog(
                            SwingUtils.getAdminFrame(),
                            "Metadata extensions must begin with \"X-\"!");    
                    return;
                }
                for (int i = 2; i < chars.length; i++) 
                {
                    char c = chars[i];
                    if (!Character.isLetterOrDigit(c)) 
                    {
                        /* 
                         * Allow for dash and underscore.
                         */
                        if ("-_".indexOf(c) == -1) 
                        {
                              JOptionPane.showMessageDialog(
                                    SwingUtils.getAdminFrame(),
                                    "\"" + answer + 
                                    "\" can only contain alphanumeric characters,\n" +
                                    "a dash, or an underscore!");    
                            return;
                        }
                    }
                }

                /*
                 * See if the attribute already exists.
                 */
                if (mMetaObject.getAttributeType(answer) != null)
                {
                      JOptionPane.showMessageDialog(
                            SwingUtils.getAdminFrame(),
                            answer + " already exists!");    
                    return;
                }

                for (int i = 0; i < mRows; i++)
                {
                    String candidate = mAttributes.get(i).getText();
                    /*
                     * The labels have a colon appended. Remove it.
                     */
                    candidate = candidate.substring(0, candidate.length() - 1);
                    
                    if (candidate.equals(answer))
                    {
                          JOptionPane.showMessageDialog(
                                SwingUtils.getAdminFrame(),
                                candidate + " already exists!");    
                        return;
                    }
                }
                
                JComboBox    component   = new JComboBox(mAttributeNames);
                JLabel       label       = new JLabel(answer + ":", JLabel.TRAILING);
                JComboBox    required    = new JComboBox(sRequired);

                mAttributes.add(label);
                mAttributeTypes.add(component);
                mRequiredBoxes.add(required);
                   
                label.setLabelFor(component);
                mPanel.add(label);
                mPanel.add(component);
                mPanel.add(required);
                mRows++;

                SwingUtils.SpringLayoutGrid(mPanel, mRows, nCOLS, 6, 6, 6, 6);
                pack();
                mPanel.repaint();
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
    
    private class SaveButtonAction extends AbstractAction
    {
        public SaveButtonAction()
        {
            super("Save");
        }

        public void actionPerformed(ActionEvent event)
        {
            for (int i = 0; i < mRows; i++)
            {
                String key = (String)mAttributes.get(i).getText();
        
                /*
                 * The labels have a colon appended. Remove it.
                 */
                key = key.substring(0, key.length() - 1);
                               
                String value = (String)mAttributeTypes.get(i).getSelectedItem();
                int index = mRequiredBoxes.get(i).getSelectedIndex();
                AttrType<?> attrType    = MetaObject.getAttributeFromName(value);
                boolean required = (index == 1 ? true : false);
                
                MetaObject.updateClassAttribute(mMetaObject,  key, attrType, required);
            }   
            setResponse(JOptionPane.OK_OPTION);
            setVisible(false);
        }
    }

    private static int          nCOLS    = 3;
    private static String []    sRequired = {
                                                "",
                                                "Required"
                                            };

    private String []           mAttributeNames;

    private List<JComboBox>     mAttributeTypes;
    private List<JLabel>        mAttributes;
    private List<JComboBox>     mRequiredBoxes;

    private JPanel              mPanel;
    private MetaObject          mMetaObject;
    private int                 mRows;
}
