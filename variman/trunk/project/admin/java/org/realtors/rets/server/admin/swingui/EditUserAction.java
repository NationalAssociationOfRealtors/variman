package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.User;
import org.realtors.rets.server.HibernateUtils;
import org.apache.log4j.Logger;
import net.sf.hibernate.HibernateException;

public class EditUserAction extends AbstractAction
{
    public EditUserAction(UsersPanel usersPanel)
    {
        super("Edit User...");
        mUsersPanel = usersPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            User user = mUsersPanel.getSelectedUser();
            if (user == null)
            {
                LOG.warn("Change password of null user");
                return;
            }

            AdminFrame frame = SwingUtils.getAdminFrame();
            EditUserDialog dialog = new EditUserDialog(frame, user);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            user.setFirstName(dialog.getFirstName());
            user.setLastName(dialog.getLastName());
            user.setAgentCode(dialog.getAgentCode());
            user.setBrokerCode(dialog.getBrokerCode());
            HibernateUtils.update(user);
            frame.setStatusText("User " + user.getName() + " changed");
            frame.refreshUsers();
            LOG.debug("Changed user: " + user);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(EditUserAction.class);
    private UsersPanel mUsersPanel;
}
