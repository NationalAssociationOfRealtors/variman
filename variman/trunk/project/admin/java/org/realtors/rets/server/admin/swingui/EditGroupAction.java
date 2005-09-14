package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.GroupRules;
import net.sf.hibernate.HibernateException;

public class EditGroupAction extends AbstractAction
{
    public EditGroupAction(GroupsPanel groupsPanel)
    {
        super("Edit Group...");
        mGroupsPanel = groupsPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            Group group = mGroupsPanel.getSelectedGroup();
            GroupRules rules = mGroupsPanel.getGroupRules();
            if (group == null)
            {
                LOG.warn("Edit null group");
            }

            AdminFrame frame = SwingUtils.getAdminFrame();
            EditGroupDialog dialog = new EditGroupDialog(frame, group, rules);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            group.setDescription(dialog.getDescription());
            HibernateUtils.update(group);
            // Update this after hibernate, just in case of DB failure
            rules.setRecordLimit(dialog.getRecordLimit());
            rules.setTimeRestriction(dialog.getTimeRestriction());
            frame.setStatusText("Group " + group.getName() + " changed");
            frame.refreshGroups();
            // Group rules are stored in RetsConfig
            Admin.setRetsConfigChanged(true);
            LOG.debug("Chagned group: " + group + ", record limit: " +
                      dialog.getRecordLimit());
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(EditGroupAction.class);
    private GroupsPanel mGroupsPanel;
}
