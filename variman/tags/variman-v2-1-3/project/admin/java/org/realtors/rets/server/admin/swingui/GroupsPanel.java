package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.config.ConditionRule;

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
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(tvp, BorderLayout.NORTH);
        panel.add(createRulesPanel(), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              mGroupList, panel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        mPopup = new JPopupMenu();
        mAddGroupAction = new AddGroupAction();
        mPopup.add(mAddGroupAction);
        mRemoveGroupAction = new RemoveGroupAction(this);
        mPopup.add(mRemoveGroupAction);

        PopupListener popupListener = new PopupListener();
        mGroupList.addMouseListener(popupListener);
        splitPane.addMouseListener(popupListener);

    }

    private JPanel createRulesPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        mFilterRulesPanel = new FilterRulesPanel(this);
        panel.add(mFilterRulesPanel);
        mConditionRulesPanel = new ConditionRulesPanel(this);
        panel.add(mConditionRulesPanel);
        return panel;
    }

    public Action getAddGroupAction()
    {
        return mAddGroupAction;
    }

    public Action getRemoveGroupAciton()
    {
        return mRemoveGroupAction;
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
            updateRulesList(group);
        }
        else
        {
            mName.setText("");
            mDescription.setText("");
            mRemoveGroupAction.setEnabled(false);
            mFilterRulesPanel.unselectGroup();
            mConditionRulesPanel.unselectGroup();
        }
    }

    private void updateRulesList(Group group)
    {
        RetsConfig retsConfig = Admin.getRetsConfig();
        List filterRules = Collections.EMPTY_LIST;
        List conditionRules = Collections.EMPTY_LIST;
        SecurityConstraints securityConstraints =
            retsConfig.getSecurityConstraints();
        mGroupRules = securityConstraints.getRulesForGroup(group.getName());
        if (mGroupRules != null)
        {
            filterRules = mGroupRules.getFilterRules();
            conditionRules = mGroupRules.getConditionRules();
        }
        mFilterRulesPanel.updateRulesList(filterRules);
        mConditionRulesPanel.updateRulesList(conditionRules);
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

    private AddGroupAction mAddGroupAction;
    private JList mGroupList;
    private JLabel mName;
    private JLabel mDescription;
    private JPopupMenu mPopup;
    private ListListModel mGroupListModel;
    private RemoveGroupAction mRemoveGroupAction;
    private GroupRules mGroupRules;
    private FilterRulesPanel mFilterRulesPanel;
    private ConditionRulesPanel mConditionRulesPanel;
}
