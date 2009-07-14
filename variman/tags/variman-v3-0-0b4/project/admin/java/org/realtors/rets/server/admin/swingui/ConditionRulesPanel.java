package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.ConditionRule;
import org.realtors.rets.server.config.GroupRules;

public class ConditionRulesPanel extends JPanel
{
    public ConditionRulesPanel(GroupsPanel groupsPanel)
    {
        setLayout(new BorderLayout());

        JPanel box = new JPanel(new BorderLayout());
        mRulesListModel = new ListListModel();
        mRulesListModel.setFormatter(CONDITION_RULE_FORMATTER);
        mRulesList = new  JList(mRulesListModel);
        mRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mRulesList.getSelectionModel().addListSelectionListener(
            new OnConditionRuleSelectionChanged());
        JScrollPane scrollPane = new JScrollPane(mRulesList);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        box.add(scrollPane);
        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(box, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        mRuleAddButtonAction = new ConditionRuleAddButtonAction(groupsPanel);
        buttonBox.add(new JButton(mRuleAddButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleRemoveButtonAction =
            new ConditionRuleRemoveButtonAction(groupsPanel);
        buttonBox.add(new JButton(mRuleRemoveButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleEditButtonAction = new ConditionRuleEditButtonAction(groupsPanel);
        buttonBox.add(new JButton(mRuleEditButtonAction));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(buttonBox, BorderLayout.SOUTH);
    }

    public void updateRulesList(List conditionRules)
    {
        mRulesListModel.setList(conditionRules);
        mRulesList.clearSelection();
        updateRulesButtons();
    }

    public void unselectGroup()
    {
        mRuleAddButtonAction.setEnabled(false);
        mRuleEditButtonAction.setEnabled(false);
        mRuleRemoveButtonAction.setEnabled(false);
        mRulesListModel.setList(Collections.EMPTY_LIST);
    }

    private ConditionRule getSelectedRule()
    {
        Object elementAt = mRulesListModel.getSelectedListElement(mRulesList);
        return (ConditionRule) elementAt;
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

    private static class ConditionRuleFormatter implements ListElementFormatter
    {
        public Object format(Object object)
        {
            ConditionRule rule = (ConditionRule) object;
            StringBuffer buffer = new StringBuffer();

            buffer.append("In ");
            buffer.append(rule.getResource()).append(":")
                .append(rule.getRetsClass());
            buffer.append(", SQL constraint: ");
            buffer.append(rule.getSqlConstraint());

            return buffer.toString();
        }
    }

    private class OnConditionRuleSelectionChanged
        implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateRulesButtons();
        }
    }

    private static class ConditionRuleAddButtonAction extends AbstractAction
    {
        public ConditionRuleAddButtonAction(GroupsPanel groupsPanel)
        {
            super("New Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            ConditionRuleDialog dialog =
                new ConditionRuleDialog("Add Condition Rule", "Add Rule");

            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            ConditionRule rule = dialog.createRule();
            GroupRules rules = mGroupsPanel.getGroupRules();
            rules.addConditionRule(rule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private class ConditionRuleRemoveButtonAction extends AbstractAction
    {
        public ConditionRuleRemoveButtonAction(GroupsPanel groupsPanel)
        {
            super("Remove Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            ConditionRule rule = getSelectedRule();
            if (rule == null)
            {
                LOG.warn("Attempt to remove null condition rule");
            }


            int rc = JOptionPane.showConfirmDialog(
                SwingUtils.getAdminFrame(),
                "Remove rule for " + CONDITION_RULE_FORMATTER.format(rule));
            if (rc != JOptionPane.OK_OPTION)
            {
                return;
            }
            GroupRules rules = mGroupsPanel.getGroupRules();
            rules.removeConditionRule(rule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private class ConditionRuleEditButtonAction extends AbstractAction
    {
        public ConditionRuleEditButtonAction(GroupsPanel groupsPanel)
        {
            super("Edit Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            ConditionRuleDialog dialog =
                new ConditionRuleDialog("Update Condition Rule", "Update Rule");
            ConditionRule rule = getSelectedRule();
            dialog.populateFromRule(rule);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            dialog.updateRule(rule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private static final Logger LOG =
        Logger.getLogger(ConditionRulesPanel.class);
    private static final ListElementFormatter CONDITION_RULE_FORMATTER =
        new ConditionRuleFormatter();

    private ListListModel mRulesListModel;
    private JList mRulesList;
    private Action mRuleAddButtonAction;
    private Action mRuleRemoveButtonAction;
    private Action mRuleEditButtonAction;
}
