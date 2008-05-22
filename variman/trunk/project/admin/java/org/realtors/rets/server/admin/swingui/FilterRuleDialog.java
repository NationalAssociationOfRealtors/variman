package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.server.config.FilterRule;

public class FilterRuleDialog extends JDialog
{
    public FilterRuleDialog(String title, String okButton)
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


    public FilterRule createRule()
    {
        FilterRule filterRule = new FilterRule();
        updateRule(filterRule);
        return filterRule;
    }

    public void updateRule(FilterRule filterRule)
    {
        if (mRuleType.getSelectedIndex() == INCLUDE_INDEX)
        {
            filterRule.setType(FilterRule.INCLUDE);
        }
        else
        {
            filterRule.setType(FilterRule.EXCLUDE);
        }

        filterRule.setResource(mResourceName.getText());
        filterRule.setRetsClass(mClassName.getText());

        String[] systemNames = StringUtils.split(mFields.getText());
        filterRule.setSystemNames(Arrays.asList(systemNames));
    }

    public void populateFromRule(FilterRule filterRule)
    {
        if (filterRule.getType().equals(FilterRule.INCLUDE))
        {
            mRuleType.setSelectedIndex(0);
        }
        else
        {
            mRuleType.setSelectedIndex(1);
        }

        mResourceName.setText(filterRule.getResource());
        mClassName.setText(filterRule.getRetsClass());
        String systemNames = StringUtils.join(
            filterRule.getSystemNames().iterator(), " ");
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

            if (warnIfBlank(mFields, "Please enter system field names.",
                            "Blank System Field Names"))
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
                    FilterRuleDialog.this,
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
    private static final int INCLUDE_INDEX = 0;

    private JComboBox mRuleType;
    private JTextField mResourceName;
    private JTextField mClassName;
    private JTextArea mFields;
    private int mResponse;
}
