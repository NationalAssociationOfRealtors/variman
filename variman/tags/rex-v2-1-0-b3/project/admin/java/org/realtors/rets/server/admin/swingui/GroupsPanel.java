package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RuleDescription;
import org.realtors.rets.server.config.SecurityConstraints;

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
        panel.setLayout(new BorderLayout());
        panel.add(new HeaderPanel("Rules"), BorderLayout.NORTH);

        JPanel box = new JPanel(new BorderLayout());
        mRulesListModel = new ListListModel();
        mRulesListModel.setFormatter(RULE_FORMATTER);
        mRulesList = new JList(mRulesListModel);
        mRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mRulesList.getSelectionModel().addListSelectionListener(
            new OnRuleSelectionChanged());
        box.add(mRulesList);
        box.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        panel.add(box, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        mRuleAddButtonAction = new RuleAddAction(this);
        buttonBox.add(new JButton(mRuleAddButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleRemoveButtonAction = new RuleRemoveButtonAction(this);
        buttonBox.add(new JButton(mRuleRemoveButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleEditButtonAction = new RuleEditButtonAction(this);
        buttonBox.add(new JButton(mRuleEditButtonAction));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(buttonBox, BorderLayout.SOUTH);
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

    public RuleDescription getSelectedRule()
    {
        Object elementAt = mRulesListModel.getSelectedListElement(mRulesList);
        return (RuleDescription) elementAt;
    }

    private void updateComponentsFromSelection()
    {
        int selection = mGroupList.getSelectedIndex();
        if (selection != -1)
        {
            Group group = (Group) mGroupListModel.getElementAt(selection);
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

            mRuleAddButtonAction.setEnabled(false);
            mRuleEditButtonAction.setEnabled(false);
            mRuleRemoveButtonAction.setEnabled(false);
            mRulesListModel.setList(Collections.EMPTY_LIST);
        }
    }

    private void updateRulesList(Group group)
    {
        RetsConfig retsConfig = Admin.getRetsConfig();
        List ruleDescriptions = Collections.EMPTY_LIST;
        SecurityConstraints securityConstraints =
            retsConfig.getSecurityConstraints();
        mGroupRules = securityConstraints.getRulesForGroup(group.getName());
        if (mGroupRules != null)
        {
            ruleDescriptions = mGroupRules.getRules();
        }
        mRulesListModel.setList(ruleDescriptions);
        mRulesList.clearSelection();
        updateRulesButtons();
    }

    private void updateRulesButtons()
    {
        mRuleAddButtonAction.setEnabled(true);
        if (mRulesList.getSelectedIndex() != -1)
        {
            mRuleEditButtonAction.setEnabled(true);
            mRuleRemoveButtonAction.setEnabled(true);
        }
        else
        {
            mRuleEditButtonAction.setEnabled(false);
            mRuleRemoveButtonAction.setEnabled(false);
        }
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

    private static class RuleDescriptionFormatter
        implements ListElementFormatter
    {
        public Object format(Object object)
        {
            RuleDescription description = (RuleDescription) object;
            StringBuffer buffer = new StringBuffer();

            buffer.append(description.getResource()).append(":")
                .append(description.getRetsClass());
            return buffer.toString();
        }
    }

    private class OnRuleSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateRulesButtons();
        }
    }

    private static class RuleRemoveButtonAction extends AbstractAction
    {
        public RuleRemoveButtonAction(GroupsPanel groupsPanel)
        {
            super("Remove Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            RuleDescription rule = mGroupsPanel.getSelectedRule();
            if (rule == null)
            {
                LOG.warn("Attempt to remove null rule");
                return;
            }

            int rc = JOptionPane.showConfirmDialog(
                SwingUtils.getAdminFrame(),
                "Remove rule for " + RULE_FORMATTER.format(rule));
            if (rc != JOptionPane.OK_OPTION)
            {
                return;
            }
            GroupRules rules = mGroupsPanel.getGroupRules();
            rules.removeRule(rule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.populateList();
        }

        private GroupsPanel mGroupsPanel;
    }

    private static class RuleEditButtonAction extends AbstractAction
    {
        public RuleEditButtonAction(GroupsPanel groupsPanel)
        {
            super("Edit Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            RuleAddDialog dialog = new RuleAddDialog("Update Rule",
                                                     "Update Rule");
            RuleDescription ruleDescription = mGroupsPanel.getSelectedRule();
            dialog.setRuleDescription(ruleDescription);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            dialog.updateRuleDescription(ruleDescription);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.populateList();
        }

        private GroupsPanel mGroupsPanel;
    }

    private static final Logger LOG =
        Logger.getLogger(GroupsPanel.class);
    private static final ListElementFormatter RULE_FORMATTER =
            new RuleDescriptionFormatter();

    private AddGroupAction mAddGroupAction;
    private JList mGroupList;
    private JLabel mName;
    private JLabel mDescription;
    private JPopupMenu mPopup;
    private ListListModel mGroupListModel;
    private RemoveGroupAction mRemoveGroupAction;
    private JList mRulesList;
    private ListListModel mRulesListModel;
    private Action mRuleAddButtonAction;
    private RuleEditButtonAction mRuleEditButtonAction;
    private RuleRemoveButtonAction mRuleRemoveButtonAction;
    private GroupRules mGroupRules;
}
