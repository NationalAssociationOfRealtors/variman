package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

public class RuleAddDialog extends JDialog
{
    public RuleAddDialog()
    {
        super(SwingUtils.getAdminFrame());
        setModal(true);
        setTitle("Add Rule");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();
        mRuleType = new JComboBox(new String[] {"Include", "Exclude"});
        tvp.addRow("Rule Type:", mRuleType);
        mResourceName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Resource Name:", mResourceName);
        mClassName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Class Name:", mClassName);
        JTextArea textArea = new JTextArea();
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tvp.addRow("Fields:", scrollPane);

        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new AddRuleButtonAction()));
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

    private class AddRuleButtonAction extends AbstractAction
    {
        public AddRuleButtonAction()
        {
            super("Add Rule");
        }

        public void actionPerformed(ActionEvent event)
        {
            mResponse = JOptionPane.OK_OPTION;
            setVisible(false);
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
            setVisible(false);
        }

    }

    private static final int TEXT_WIDTH = 20;
    private JComboBox mRuleType;
    private JTextField mResourceName;
    private JTextField mClassName;
    private int mResponse;
}
