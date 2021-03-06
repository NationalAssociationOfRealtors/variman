package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.util.SortedSet;

import javax.swing.*;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.HibernateUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import net.sf.hibernate.HibernateException;

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
                "Are you sure you wnat to delete " + group + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION)
            {
                return;
            }

            HibernateUtils.delete(group);
            mGroupsPanel.populateList();
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(RemoveGroupAction.class);
    private GroupsPanel mGroupsPanel;
}
