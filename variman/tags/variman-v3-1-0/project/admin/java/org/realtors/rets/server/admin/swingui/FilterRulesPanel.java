package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Iterator;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;

public class FilterRulesPanel extends JPanel
{
    public FilterRulesPanel(GroupsPanel groupsPanel)
    {
        setLayout(new BorderLayout());
        JPanel box = new JPanel(new BorderLayout());
        mRulesListModel = new ListListModel();
        mRulesListModel.setFormatter(FILTER_RULE_FORMATTER);
        mRulesList = new JList(mRulesListModel);
        mRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mRulesList.getSelectionModel().addListSelectionListener(
            new OnFilterRuleSelectionChanged());
        JScrollPane scrollPane = new JScrollPane(mRulesList);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        box.add(scrollPane);
        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(box, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        mRuleAddButtonAction = new FilterRuleAddAction(groupsPanel);
        buttonBox.add(new JButton(mRuleAddButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleRemoveButtonAction = new FilterRuleRemoveButtonAction(groupsPanel);
        buttonBox.add(new JButton(mRuleRemoveButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRuleEditButtonAction = new FilterRuleEditButtonAction(groupsPanel);
        buttonBox.add(new JButton(mRuleEditButtonAction));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(buttonBox, BorderLayout.SOUTH);

    }

    public void updateRulesList(List filterRules)
    {
        mRulesListModel.setList(filterRules);
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

    private FilterRule getSelectedRule()
    {
        Object elementAt = mRulesListModel.getSelectedListElement(mRulesList);
        return (FilterRule) elementAt;
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

    private static class FilterRuleFormatter
        implements ListElementFormatter
    {
        public Object format(Object object)
        {
            FilterRule rule = (FilterRule) object;
            StringBuffer buffer = new StringBuffer();

            buffer.append("In ");
            buffer.append(rule.getResource()).append(":")
                .append(rule.getRetsClass());
            if (rule.getType() == FilterRule.INCLUDE)
            {
                buffer.append(", include ");
            }
            else
            {
                buffer.append(", exclude ");
            }
            Iterator systemNames = rule.getSystemNames().iterator();
            buffer.append(StringUtils.join(systemNames, ", "));
            return buffer.toString();
        }
    }

    private class OnFilterRuleSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateRulesButtons();
        }
    }

    private static class FilterRuleAddAction extends AbstractAction
    {
        public FilterRuleAddAction(GroupsPanel groupsPanel)
        {
            super("New Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            FilterRuleDialog dialog = new FilterRuleDialog("Add Filter Rule",
                                                           "Add Rule");
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            FilterRule filterRule = dialog.createRule();
            GroupRules rules = mGroupsPanel.getGroupRules();
            rules.addFilterRule(filterRule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private class FilterRuleRemoveButtonAction extends AbstractAction
    {
        public FilterRuleRemoveButtonAction(GroupsPanel groupsPanel)
        {
            super("Remove Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            FilterRule rule = getSelectedRule();
            if (rule == null)
            {
                LOG.warn("Attempt to remove null rule");
                return;
            }

            int rc = JOptionPane.showConfirmDialog(
                SwingUtils.getAdminFrame(),
                "Remove rule for " + FILTER_RULE_FORMATTER.format(rule));
            if (rc != JOptionPane.OK_OPTION)
            {
                return;
            }
            GroupRules rules = mGroupsPanel.getGroupRules();
            rules.removeFilterRule(rule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private class FilterRuleEditButtonAction extends AbstractAction
    {
        public FilterRuleEditButtonAction(GroupsPanel groupsPanel)
        {
            super("Edit Rule...");
            mGroupsPanel = groupsPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            FilterRuleDialog dialog =
                new FilterRuleDialog("Update Filter Rule", "Update Rule");
            FilterRule filterRule = getSelectedRule();
            dialog.populateFromRule(filterRule);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            dialog.updateRule(filterRule);
            Admin.setRetsConfigChanged(true);
            mGroupsPanel.refreshTab();
        }

        private GroupsPanel mGroupsPanel;
    }

    private static final Logger LOG =
        Logger.getLogger(FilterRulesPanel.class);
    private static final ListElementFormatter FILTER_RULE_FORMATTER =
            new FilterRuleFormatter();

    private ListListModel mRulesListModel;
    private JList mRulesList;
    private Action mRuleAddButtonAction;
    private Action mRuleEditButtonAction;
    private Action mRuleRemoveButtonAction;
}
