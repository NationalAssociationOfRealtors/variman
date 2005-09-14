package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.config.TimeRestriction;

public class GroupsPanel extends JPanel
{
    public GroupsPanel()
    {
        super(new BorderLayout());
        mGroupList = new JList();
        mGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mGroupList.getSelectionModel().addListSelectionListener(
            new OnSelectionChanged());
        mGroupList.addMouseListener(new ListDoubleClickListener());

        JPanel panel = new JPanel(new BorderLayout());
        TextValuePanel tvp = new TextValuePanel();
        mName = new JLabel();
        tvp.addRow("Name:", mName);
        mDescription = new JLabel();
        tvp.addRow("Description:", mDescription);
        mRecordLimit = new JLabel("None");
        tvp.addRow("Record Limit:", mRecordLimit);
        mTimeRestriction = new JLabel("");
        tvp.addRow("Time Restriction:", mTimeRestriction);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(tvp, BorderLayout.NORTH);
        panel.add(createRulesPanel(), BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mGroupList);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              scrollPane, panel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        mPopup = new JPopupMenu();
        mAddGroupAction = new AddGroupAction();
        mPopup.add(mAddGroupAction);
        mRemoveGroupAction = new RemoveGroupAction(this);
        mPopup.add(mRemoveGroupAction);
        mEditGroupAction = new EditGroupAction(this);
        mPopup.add(mEditGroupAction);

        PopupListener popupListener = new PopupListener();
        mGroupList.addMouseListener(popupListener);
        splitPane.addMouseListener(popupListener);

    }

    private JComponent createRulesPanel()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        mFilterRulesPanel = new FilterRulesPanel(this);
        tabbedPane.addTab("Filter Rules", mFilterRulesPanel);
        mConditionRulesPanel = new ConditionRulesPanel(this);
        tabbedPane.addTab("Condition Rules", mConditionRulesPanel);
        return tabbedPane;
    }

    public Action getAddGroupAction()
    {
        return mAddGroupAction;
    }

    public Action getRemoveGroupAction()
    {
        return mRemoveGroupAction;
    }

    public Action getEditGroupAction()
    {
        return mEditGroupAction;
    }

    public GroupRules getGroupRules()
    {
        return mGroupRules;
    }

    private void updateComponentsFromSelection()
    {
        int selection = mGroupList.getSelectedIndex();
        Group group = null;
        if (selection != -1)
        {
            group = (Group) mGroupListModel.getElementAt(selection);
            mName.setText(group.getName());
            mDescription.setText(group.getDescription());
            mRemoveGroupAction.setEnabled(true);
            mEditGroupAction.setEnabled(true);
            updateRulesList(group);
        }
        else
        {
            mName.setText("");
            mDescription.setText("");
            mRecordLimit.setText("");
            mTimeRestriction.setText("");
            mRemoveGroupAction.setEnabled(false);
            mEditGroupAction.setEnabled(false);
            mFilterRulesPanel.unselectGroup();
            mConditionRulesPanel.unselectGroup();
        }
    }

    private void updateRulesList(Group group)
    {
        RetsConfig retsConfig = Admin.getRetsConfig();
        SecurityConstraints securityConstraints =
            retsConfig.getSecurityConstraints();
        mGroupRules = securityConstraints.getRulesForGroup(group.getName());
        int recordLimit = mGroupRules.getRecordLimit();
        if (recordLimit == 0)
        {
            mRecordLimit.setText("0 (None)");
        }
        else
        {
            mRecordLimit.setText(Integer.toString(recordLimit));
        }
        TimeRestriction timeRestriction = mGroupRules.getTimeRestriction();
        if (timeRestriction == null)
        {
            mTimeRestriction.setText("None");
        }
        else
        {
            StringBuffer text = new StringBuffer();
            if (timeRestriction.getPolicy() == TimeRestriction.ALLOW)
            {
                text.append("Allow between ");
            }
            else
            {
                text.append("Deny between ");
            }
            DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
            Date date = timeRestriction.getStartAsCalendar().getTime();
            text.append(formatter.format(date)).append(" and ");
            date = timeRestriction.getEndAsCalendar().getTime();
            text.append(formatter.format(date));
            mTimeRestriction.setText(text.toString());
        }
        mFilterRulesPanel.updateRulesList(mGroupRules.getFilterRules());
        mConditionRulesPanel.updateRulesList(mGroupRules.getConditionRules());
    }

    public void populateList()
    {
        final int selection = mGroupList.getSelectedIndex();
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                List groups = Collections.EMPTY_LIST;
                try
                {
                    groups = GroupUtils.findAll();
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    // Todo: GroupsPanel.construct show error dialog
                }
                return groups;
            }

            public void finished()
            {
                List groups = (List) get();
                setGroupListModel(new ListListModel(groups));
                int newSelection = selection;
                if (newSelection >= groups.size())
                {
                    newSelection = groups.size() - 1;
                }
                mGroupList.setSelectedIndex(newSelection);
                updateComponentsFromSelection();
            }
        };
        worker.start();
    }

    private void setGroupListModel(ListListModel model)
    {
        mGroupListModel = model;
        mGroupList.setModel(model);
    }

    public Group getSelectedGroup()
    {
        return (Group) mGroupList.getSelectedValue();
    }

    private class OnSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateComponentsFromSelection();
        }
    }

    private class ListDoubleClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent event)
        {
            if (event.getClickCount() == 2)
            {
                int index = mGroupList.locationToIndex(event.getPoint());
                mGroupList.ensureIndexIsVisible(index);
                mGroupList.setSelectedIndex(index);
                mEditGroupAction.actionPerformed(null);
            }
        }
    }

    private class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        public void mouseReleased(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent event)
        {
            if (mPopup.isPopupTrigger(event))
            {
                mPopup.show(event.getComponent(), event.getX(), event.getY());
            }
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GroupsPanel.class);

    private JList mGroupList;
    private JLabel mName;
    private JLabel mDescription;
    private JLabel mRecordLimit;
    private JPopupMenu mPopup;
    private ListListModel mGroupListModel;
    private AddGroupAction mAddGroupAction;
    private RemoveGroupAction mRemoveGroupAction;
    private EditGroupAction mEditGroupAction;
    private GroupRules mGroupRules;
    private FilterRulesPanel mFilterRulesPanel;
    private ConditionRulesPanel mConditionRulesPanel;
    private JLabel mTimeRestriction;
}
