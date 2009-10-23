package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 14, 2004
 * Time: 10:43:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveUserAction extends AbstractAction
{
    public RemoveUserAction(UsersPanel usersPanel)
    {
        super("Remove User...");
        mUsersPanel = usersPanel;
    }
    
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            User user = mUsersPanel.getSelectedUser();
            if (user == null)
            {
                LOG.warn("Remove user on null user");
                return;
            }

            AdminFrame frame = SwingUtils.getAdminFrame();
            int response = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + user.getName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION)
            {
                return;
            }

            UserUtils.delete(user);
            LOG.debug("User deleted: " + user);
            frame.setStatusText("User " + user.getName() + " removed.");
            frame.refreshUsers();
        }
        catch (Exception e)
        {
            LOG.error("Caught", e);
        }

    }

    private static final Logger LOG =
        Logger.getLogger(RemoveUserAction.class);
    private UsersPanel mUsersPanel;
}
