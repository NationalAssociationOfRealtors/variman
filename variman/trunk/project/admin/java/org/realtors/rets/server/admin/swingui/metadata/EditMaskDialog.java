package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MTable;

import org.realtors.rets.server.admin.swingui.AdminFrame;
import org.realtors.rets.server.admin.swingui.SwingUtils;

public class EditMaskDialog extends MetadataDialog
{
    public EditMaskDialog(boolean strictParsing, Metadata metadata, MResource resource, MTable table)
    {
        super(SwingUtils.getAdminFrame());
        
        mStrictParsing = strictParsing;
        mTable = table;
        
        setModal(true);
        setTitle(mTable.getMetadataTypeName() + " EditMasks");

        JPanel panel = new JPanel(new SpringLayout());
        int rows = 0;
        /*
         * Determine which editmasks have been selected.
         */
        mEditMaskCsv = MTable.toEditMaskId(mTable.getEditMasks());
        if (mEditMaskCsv != null)
            mSelectedEditMasks.addAll(MTable.toEditMaskIds(mEditMaskCsv));
        
        /*
         * Add all available editmask keys to the keys.
         */
        for (MEditMask editMask : resource.getMEditMasks())
        {
            mEditMaskKeys.add(editMask.getEditMaskID());    
        }
        
        mComponents = new ArrayList<JCheckBox>(mEditMaskKeys.size());
        
        for (int i = 0; i < mEditMaskKeys.size(); i++)
        {
            String editMaskKey = mEditMaskKeys.get(i);
            JCheckBox checkBox = new JCheckBox(editMaskKey);
            if (mSelectedEditMasks.contains(editMaskKey))
            {
                checkBox.setSelected(true);
            }
            mComponents.add(checkBox);
            panel.add(checkBox);
            rows++;
        }
        
        SwingUtils.SpringLayoutGrid(panel, rows, 1, 6, 6, 6, 6);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(panel);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        JButton saveButton = new JButton(new SaveButtonAction());
        saveButton.setSelected(true);
        buttonBox.add(saveButton);
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
    
    public String getEditMask()
    {
        return mEditMaskCsv;
    }

    private class SaveButtonAction extends AbstractAction
    {
        public SaveButtonAction()
        {
            super("Save");
        }

        public void actionPerformed(ActionEvent event)
        {
            int len = mComponents.size();
            
            /*
             * Update selected fields.
             */
            String delim = "";
            String editMaskCsv = "";
            for (int i = 0; i < len; i++)
            {
                if (mComponents.get(i).isSelected())
                {
                    editMaskCsv = editMaskCsv.concat(delim);
                    editMaskCsv = editMaskCsv.concat(mComponents.get(i).getText());
                    delim = ",";
                }
            }
            mEditMaskCsv = editMaskCsv;
            setResponse(JOptionPane.OK_OPTION);
            setVisible(false);
        }
    }
    
    public void setEditMask(String editMaskCsv)
    {
        mEditMaskCsv = editMaskCsv;
        mSelectedEditMasks.clear();
        if (mEditMaskCsv != null)
        {
            mSelectedEditMasks.addAll(MTable.toEditMaskIds(mEditMaskCsv));
            for (int i = 0; i < mEditMaskKeys.size(); i++)
            {
                String editMaskKey = mEditMaskKeys.get(i);
                JCheckBox checkBox = mComponents.get(i);
                checkBox.setSelected(false);
                if (mSelectedEditMasks.contains(editMaskKey))
                {
                    checkBox.setSelected(true);
                }
            }
        }
    }
    
    private boolean mStrictParsing;
    
    private List<JCheckBox> mComponents;
    private MTable           mTable;
        
    private List<String>    mEditMaskKeys       = new ArrayList<String>();
    private Set<String>     mSelectedEditMasks  = new LinkedHashSet<String>();
    private String          mEditMaskCsv;
}
