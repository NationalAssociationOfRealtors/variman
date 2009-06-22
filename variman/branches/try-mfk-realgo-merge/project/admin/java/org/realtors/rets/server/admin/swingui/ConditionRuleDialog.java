package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.realtors.rets.server.config.ConditionRule;
import org.realtors.rets.server.config.ConditionRuleImpl;

public class ConditionRuleDialog extends JDialog
{
    public ConditionRuleDialog(String title, String okButton)
    {
        super(SwingUtils.getAdminFrame());
        setModal(true);
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();
        mResourceId = new JTextField(TEXT_WIDTH);
        tvp.addRow("Resource ID:", mResourceId);
        mClassName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Class Name:", mClassName);
        mSqlConstraint = new JTextArea();
        mSqlConstraint.setRows(5);
        mSqlConstraint.setLineWrap(true);
        mSqlConstraint.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(mSqlConstraint);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tvp.addRow("SQL Constraint:", scrollPane);

        mDmqlConstraint = new JTextArea();
        mDmqlConstraint.setRows(5);
        mDmqlConstraint.setLineWrap(true);
        mDmqlConstraint.setWrapStyleWord(true);
        scrollPane = new JScrollPane(mDmqlConstraint);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tvp.addRow("DMQL Constraint:", scrollPane);

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

    public ConditionRule createRule()
    {
        ConditionRule rule = new ConditionRuleImpl();
        updateRule(rule);
        return rule;
    }

    public void updateRule(ConditionRule rule)
    {
        rule.setResourceID(mResourceId.getText());
        rule.setRetsClassName(mClassName.getText());
        rule.setSqlConstraint(mSqlConstraint.getText());
        rule.setDmqlConstraint(mDmqlConstraint.getText());
    }

    public void populateFromRule(ConditionRule rule)
    {
        mResourceId.setText(rule.getResourceID());
        mClassName.setText(rule.getRetsClassName());
        mSqlConstraint.setText(rule.getSqlConstraint());
        mDmqlConstraint.setText(rule.getDmqlConstraint());
    }

    private class AddRuleButtonAction extends AbstractAction
    {
        public AddRuleButtonAction(String buttonText)
        {
            super(buttonText);
        }

        public void actionPerformed(ActionEvent event)
        {
            if (warnIfBlank(mResourceId, "Please enter a resource ID.",
                            "Blank Resource ID"))
            {
                return;
            }

            if (warnIfBlank(mClassName, "Please enter a class name.",
                            "Blank Class Name"))
            {
                return;
            }
            
            List/*JTextComponent*/ components = new ArrayList/*JTextComponent*/();
            components.add(mSqlConstraint);
            components.add(mDmqlConstraint);
            if (warnIfBlank(components, "Please enter SQL constraint and/or DMQL constraint.",
                            "Blank SQL and DMQL Constraints"))
            {
                return;
            }

            mResponse = JOptionPane.OK_OPTION;
            dispose();
        }
        
        private boolean warnIfBlank(List/*JTextComponent*/ components,
                String message, String title)
        {
            if (allBlank(components)) {
                JOptionPane.showMessageDialog(
                        ConditionRuleDialog.this,
                        message, title, JOptionPane.WARNING_MESSAGE);
                return true;
            }
            return false;
        }

        private boolean warnIfBlank(JTextComponent component, String message,
                                 String title)
        {
            List/*JTextComponent*/ components = new ArrayList/*JTextComponent*/();
            components.add(component);
            return warnIfBlank(components, message, title);
        }
        
        private boolean allBlank(List/*JTextComponent*/ components)
        {
            boolean allBlank = true;
            
            for (Iterator/*JTextComponent*/ iter = components.iterator(); iter.hasNext(); ) {
                JTextComponent component = (JTextComponent)iter.next();
                if (!"".equals(component.getText())) {
                    allBlank = false;
                    break;
                }
            }
            
            return allBlank;
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

    private int mResponse;
    private JTextField mResourceId;
    private JTextField mClassName;
    private JTextArea mSqlConstraint;
    private JTextArea mDmqlConstraint;
}
