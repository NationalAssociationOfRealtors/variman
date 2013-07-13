package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hibernate.HibernateException;

import org.apache.log4j.Logger;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.GroupRules;

public class EditGroupAction extends AbstractAction
{
    public EditGroupAction(GroupsPanel groupsPanel)
    {
        super("Edit Group...");
        mGroupsPanel = groupsPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        AdminFrame frame = SwingUtils.getAdminFrame();
        GroupDialog dialog = new GroupDialog(frame);

        try
        {
            Group group = mGroupsPanel.getSelectedGroup();
            GroupRules rules = mGroupsPanel.getGroupRules();
            if (group == null)
            {
                LOG.warn("Edit null group");
            }
            dialog.editGroup(group, rules);

            if (showUntilCancelOrValid(dialog) != JOptionPane.OK_OPTION)
            {
                return;
            }

            group.setDescription(dialog.getDescription());
            HibernateUtils.update(group);
            // Update this after hibernate, just in case of DB failure
            dialog.transferToGropuRules(rules);
            frame.setStatusText("Group " + group.getName() + " changed");
            frame.refreshGroups();
            // Group rules are stored in RetsConfig
            Admin.setRetsConfigChanged(true);
            LOG.debug("Chagned group: " + group + ", record limit: " +
                      dialog.getRecordLimit());
        }
        catch (Exception e)
        {
            LOG.error("Caught", e);
        }
        finally
        {
            dialog.dispose();
        }
    }

    private int showUntilCancelOrValid(GroupDialog dialog)
        throws HibernateException
    {
        int response = JOptionPane.CANCEL_OPTION;
        while (true)
        {
            dialog.show();
            response = dialog.getResponse();
            if (response == JOptionPane.CANCEL_OPTION)
            {
                break;
            }
            if (dialog.isValidContent())
            {
                break;
            }
        }
        return response;
    }

    private static final Logger LOG =
        Logger.getLogger(EditGroupAction.class);
    private GroupsPanel mGroupsPanel;
}
