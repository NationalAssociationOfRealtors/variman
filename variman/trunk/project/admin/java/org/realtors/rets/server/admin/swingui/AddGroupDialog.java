package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

public class AddGroupDialog extends JDialog
{
    public AddGroupDialog()
    {
        super(SwingUtils.getAdminFrame());
        setModal(true);
        setTitle("Add Group");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();
        mGroupName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Group Name:", mGroupName);
        mDescription = new JTextField(TEXT_WIDTH);
        tvp.addRow("Description:", mDescription);
        mRecordLimit = new WholeNumberField(0, TEXT_WIDTH);
        tvp.addRow("Record Limit:", mRecordLimit);

        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new AddGroupButtonAction()));
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

    public String getGroupName()
    {
        return mGroupName.getText();
    }

    public String getDescription()
    {
        return mDescription.getText();
    }

    public int getRecordLimit()
    {
        return mRecordLimit.getValue();
    }

    private class AddGroupButtonAction extends AbstractAction
    {
        public AddGroupButtonAction()
        {
            super("Add Group");
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
    private int mResponse;
    private JTextField mGroupName;
    private JTextField mDescription;
    private WholeNumberField mRecordLimit;
}
