package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.config.RuleDescription;

public class RuleAddDialog extends JDialog
{
    public RuleAddDialog()
    {
        this("Add Rule", "Add Rule");
    }

    public RuleAddDialog(String title, String okButton)
    {

        super(SwingUtils.getAdminFrame());
        setModal(true);
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();
        mRuleType = new JComboBox(new String[] {"Include", "Exclude"});
        tvp.addRow("Rule Type:", mRuleType);
        mResourceName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Resource Name:", mResourceName);
        mClassName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Class Name:", mClassName);
        mFields = new JTextArea();
        mFields.setRows(5);
        mFields.setLineWrap(true);
        mFields.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(mFields);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tvp.addRow("Fields:", scrollPane);

        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new AddRuleButtonAction(okButton)));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelButtonAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
        mResponse = JOptionPane.CANCEL_OPTION;
    }

    public int getResponse()
    {
        return mResponse;
    }

    public RuleDescription.Type getType()
    {
        if (mRuleType.getSelectedIndex() == INCLUDE_INDEX)
        {
            return RuleDescription.INCLUDE;
        }
        else
        {
            return RuleDescription.EXCLUDE;
        }
    }

    public RuleDescription getRuleDescription()
    {
        RuleDescription ruleDescription = new RuleDescription();
        updateRuleDescription(ruleDescription);
        return ruleDescription;
    }

    public void updateRuleDescription(RuleDescription ruleDescription)
    {
        if (mRuleType.getSelectedIndex() == INCLUDE_INDEX)
        {
            ruleDescription.setType(RuleDescription.INCLUDE);
        }
        else
        {
            ruleDescription.setType(RuleDescription.EXCLUDE);
        }

        ruleDescription.setResource(mResourceName.getText());
        ruleDescription.setRetsClass(mClassName.getText());

        String[] systemNames = StringUtils.split(mFields.getText());
        ruleDescription.setSystemNames(Arrays.asList(systemNames));
    }

    public void setRuleDescription(RuleDescription ruleDescription)
    {
        if (ruleDescription.getType().equals(RuleDescription.INCLUDE))
        {
            mRuleType.setSelectedIndex(0);
        }
        else
        {
            mRuleType.setSelectedIndex(1);
        }

        mResourceName.setText(ruleDescription.getResource());
        mClassName.setText(ruleDescription.getRetsClass());
        String systemNames = StringUtils.join(
            ruleDescription.getSystemNames().iterator(), " ");
       mFields.setText(systemNames);
    }

    private class AddRuleButtonAction extends AbstractAction
    {
        public AddRuleButtonAction(String buttonText)
        {
            super(buttonText);
        }

        public void actionPerformed(ActionEvent event)
        {
            if (mResourceName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(
                    RuleAddDialog.this,
                    "Please enter a resource name.",
                    "Blank Resource Name",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mClassName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(
                    RuleAddDialog.this,
                    "Please enter a class name.",
                    "Blank Class Name",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mFields.getText().equals(""))
            {
                JOptionPane.showMessageDialog(
                    RuleAddDialog.this,
                    "Please enter system field names.",
                    "Blank System Fields Name",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            mResponse = JOptionPane.OK_OPTION;
            dispose();
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
            mResponse = JOptionPane.CANCEL_OPTION;
            dispose();
        }
    }

    private static final int TEXT_WIDTH = 20;
    private static final int INCLUDE_INDEX = 0;
    private JComboBox mRuleType;
    private JTextField mResourceName;
    private JTextField mClassName;
    private JTextArea mFields;
    private int mResponse;
}
