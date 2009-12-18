package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.SecurityConstraints;

public class RemoveGroupAction extends AbstractAction
{
    public RemoveGroupAction(GroupsPanel groupsPanel)
    {
        super("Remove Group...");
        mGroupsPanel = groupsPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            Group group = mGroupsPanel.getSelectedGroup();
            if (group == null)
            {
                LOG.warn("Remove group on null group");
                return;
            }

            AdminFrame frame = SwingUtils.getAdminFrame();
            SortedSet users = GroupUtils.getUsers(group);
            if (users.size() != 0)
            {
                JOptionPane.showMessageDialog(
                    frame,
                    "Cannot remove group.  It is s still in use by " +
                    "the following users:\n" +
                    StringUtils.join(users.iterator(), ", "));
                return;
            }

            int response = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + group + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION)
            {
                return;
            }

            HibernateUtils.delete(group);

            // Group rules are stored in RetsConfig
            RetsConfig retsConfig = Admin.getRetsConfig();
            SecurityConstraints securityConstraints =
                retsConfig.getSecurityConstraints();
            securityConstraints.removeRulesForGroup(group.getName());
            Admin.setRetsConfigChanged(true);

            mGroupsPanel.refreshTab();
        }
        catch (Exception e)
        {
            LOG.error("Caught", e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(RemoveGroupAction.class);
    private GroupsPanel mGroupsPanel;
}
