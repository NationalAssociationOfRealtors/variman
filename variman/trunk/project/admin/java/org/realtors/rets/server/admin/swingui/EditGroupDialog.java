package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.TimeRestriction;
import net.sf.hibernate.HibernateException;

public class EditGroupDialog extends JDialog
{
    public EditGroupDialog(Frame parent)
    {
        super(parent);
        setModal(true);
        setTitle("Add Group");
        mEditMode = false;

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        TextValuePanel tvp = new TextValuePanel();

        mGroupName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Group Name:", mGroupName);
        mDescription = new JTextField(TEXT_WIDTH);
        tvp.addRow("Description:", mDescription);
        mRecordLimit = new WholeNumberField(0, TEXT_WIDTH);
        tvp.addRow("Record Limit:", mRecordLimit);
        mTimeRestriction = new TimeRestrictionPanel();
        tvp.addRow("Time Restriction:", mTimeRestriction);

        mEnableQueryCountLimit = new JCheckBox("Query Count Limit:");
        mQueryCountLimit = new QueryCountLimitPanel();
        mEnableQueryCountLimit.addActionListener(new QueryCountLimitAction());
        tvp.addRow(mEnableQueryCountLimit, mQueryCountLimit);

        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        mSubmitButtonAction = new SubmitButtonAction();
        buttonBox.add(new JButton(mSubmitButtonAction));
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

    public EditGroupDialog(Frame parent, Group group, GroupRules rules)
    {
        this(parent);
        editGroup(group, rules);
    }

    public void editGroup(Group group, GroupRules rules)
    {
        setTitle("Edit Group: " + group.getName());
        mSubmitButtonAction.setName("Update Group");
        mEditMode = true;

        mGroupName.setText(group.getName());
        mGroupName.setEditable(false);
        mDescription.setText(group.getDescription());

        mRecordLimit.setValue(rules.getRecordLimit());
        mTimeRestriction.setTimeRestriction(rules.getTimeRestriction());

        if (rules.hasNoQueryLimit())
        {
            mEnableQueryCountLimit.setSelected(false);
        }
        else
        {
            mEnableQueryCountLimit.setSelected(true);
            mQueryCountLimit.setLimit(rules.getQueryCountLimit());
            mQueryCountLimit.setLimitPeriod(rules.getQueryCountLimitPeriod());
        }
        syncQueryCountComponents();
    }

    public void updateRules(GroupRules rules)
    {
        if (mEnableQueryCountLimit.isSelected())
        {
            rules.setQueryCountLimit(mQueryCountLimit.getLimit(),
                                     mQueryCountLimit.getLimitPeriod());
        }
        else
        {
            rules.setNoQueryCountLimit();
        }
        rules.setRecordLimit(getRecordLimit());
        rules.setTimeRestriction(getTimeRestriction());
    }

    private void syncQueryCountComponents()
    {
        mQueryCountLimit.setEnabled(mEnableQueryCountLimit.isSelected());
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

    public TimeRestriction getTimeRestriction()
    {
        return mTimeRestriction.getTimeRestriction();
    }

    public boolean isValidContent() throws HibernateException
    {
        boolean isValidContent = true;
        isValidContent &= mTimeRestriction.isValidContent();
        // Group name is uneditable, so assume it's still valid
        if (!mEditMode)
            isValidContent &= isValidGroupName();
        return isValidContent;
    }

    private boolean isValidGroupName()
        throws HibernateException
    {
        String groupName = getGroupName();
        if (StringUtils.isEmpty(groupName))
        {
            JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "The group must not be empty.\n" +
                "Please choose a new name.", "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (GroupUtils.findByName(groupName) != null)
        {
            JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "A group already exists with this name.\n" +
                "Please choose a new name.", "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return  false;
        }

        return true;
    }

    private class SubmitButtonAction extends AbstractAction
    {
        public SubmitButtonAction()
        {
            super("Add Group");
        }

        public void actionPerformed(ActionEvent event)
        {
            mResponse = JOptionPane.OK_OPTION;
            setVisible(false);
        }

        public void setName(String name)
        {
            putValue(NAME, name);
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

    private class QueryCountLimitAction implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            syncQueryCountComponents();
        }
    }

    private static final int TEXT_WIDTH = 25;
    private JTextField mGroupName;
    private JTextField mDescription;
    private WholeNumberField mRecordLimit;
    private int mResponse;
    private TimeRestrictionPanel mTimeRestriction;
    private JCheckBox mEnableQueryCountLimit;
    private QueryCountLimitPanel mQueryCountLimit;
    private boolean mEditMode;
    private SubmitButtonAction mSubmitButtonAction;
}
