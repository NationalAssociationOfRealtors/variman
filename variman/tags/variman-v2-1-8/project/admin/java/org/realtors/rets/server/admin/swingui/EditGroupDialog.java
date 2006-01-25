package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.TimeRestriction;

public class EditGroupDialog extends JDialog
{
    public EditGroupDialog(Frame parent, Group group, GroupRules rules)
    {
        super(parent);
        setModal(true);
        setTitle("Edit Group: " + group.getName());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        TextValuePanel tvp = new TextValuePanel();

        mDescription = new JTextField(group.getDescription(), TEXT_WIDTH);
        tvp.addRow("Description:", mDescription);
        mRecordLimit = new WholeNumberField(rules.getRecordLimit(), TEXT_WIDTH);
        tvp.addRow("Record Limit:", mRecordLimit);
        mTimeRestriction = new TimeRestrictionPanel(rules.getTimeRestriction());
        tvp.addRow("Time Restriction:", mTimeRestriction);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new SaveChangesButtonAction()));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelButtonAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, parent);
        mResponse = JOptionPane.CANCEL_OPTION;
    }

    public int getResponse()
    {
        return mResponse;
    }

    public String getDescription()
    {
        return mDescription.getText();
    }

    public int getRecordLimit()
    {
        return mRecordLimit.getValue();
    }

    public TimeRestriction getTimeRestriction()
    {
        return mTimeRestriction.getTimeRestriction();
    }

    public boolean isValidContent()
    {
        return mTimeRestriction.isValidContent();
    }

    public boolean validateFields()
    {
        return mTimeRestriction.isValidContent();
    }

    private class SaveChangesButtonAction extends AbstractAction
    {
        public SaveChangesButtonAction()
        {
            super("Save Changes");
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


    private static final int TEXT_WIDTH = 25;
    private JTextField mDescription;
    private WholeNumberField mRecordLimit;
    private int mResponse;
    private TimeRestrictionPanel mTimeRestriction;
}
