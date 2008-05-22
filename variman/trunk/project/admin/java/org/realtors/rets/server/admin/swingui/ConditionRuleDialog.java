package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.realtors.rets.server.config.ConditionRule;

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
        mResourceName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Resource Name:", mResourceName);
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
        ConditionRule rule = new ConditionRule();
        updateRule(rule);
        return rule;
    }

    public void updateRule(ConditionRule rule)
    {
        rule.setResource(mResourceName.getText());
        rule.setRetsClass(mClassName.getText());
        rule.setSqlConstraint(mSqlConstraint.getText());
    }

    public void populateFromRule(ConditionRule rule)
    {
        mResourceName.setText(rule.getResource());
        mClassName.setText(rule.getRetsClass());
        mSqlConstraint.setText(rule.getSqlConstraint());
    }

    private class AddRuleButtonAction extends AbstractAction
    {
        public AddRuleButtonAction(String buttonText)
        {
            super(buttonText);
        }

        public void actionPerformed(ActionEvent event)
        {
            if (warnIfBlank(mResourceName, "Please enter a resource name.",
                            "Blank Resource Name"))
            {
                return;
            }

            if (warnIfBlank(mClassName, "Please enter a class name.",
                            "Blank Class Name"))
            {
                return;
            }

            if (warnIfBlank(mSqlConstraint, "Please enter SQL constraint.",
                            "Blank SQL Constraint"))
            {
                return;
            }

            mResponse = JOptionPane.OK_OPTION;
            dispose();
        }

        private boolean warnIfBlank(JTextComponent component, String message,
                                 String title)
        {
            if (component.getText().equals(""))
            {
                JOptionPane.showMessageDialog(
                    ConditionRuleDialog.this,
                    message, title, JOptionPane.WARNING_MESSAGE);
                return true;
            }
            return false;
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
    private JTextField mResourceName;
    private JTextField mClassName;
    private JTextArea mSqlConstraint;
}
