package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.config.RuleDescription;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.admin.Admin;

public class RuleAddAction extends AbstractAction
{
    public RuleAddAction(GroupsPanel groupsPanel)
    {
        super("New Rule...");
        mGroupsPanel = groupsPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        RuleAddDialog dialog = new RuleAddDialog();
        dialog.show();
        if (dialog.getResponse() != JOptionPane.OK_OPTION)
        {
            return;
        }

        RuleDescription ruleDescription = dialog.getRuleDescription();
        GroupRules rules = mGroupsPanel.getGroupRules();
        rules.addRule(ruleDescription);
        Admin.setRetsConfigChanged(true);
        mGroupsPanel.populateList();
    }

    private GroupsPanel mGroupsPanel;
}
