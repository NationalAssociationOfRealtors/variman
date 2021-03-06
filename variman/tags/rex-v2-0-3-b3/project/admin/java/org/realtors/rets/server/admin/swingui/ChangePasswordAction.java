package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import net.sf.hibernate.HibernateException;
import org.apache.log4j.Logger;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.User;

public class ChangePasswordAction extends AbstractAction
{
    public ChangePasswordAction(UsersPanel usersPanel)
    {
        super("Change Password...");
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
            ChangePasswordDialog dialog = new ChangePasswordDialog(frame, user);
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            user.changePassword(dialog.getPassword());
            HibernateUtils.update(user);
            frame.setStatusText("Password changed for " + user.getName());
            LOG.debug("Password changed for: " + user);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }


    private static final Logger LOG =
        Logger.getLogger(ChangePasswordAction.class);
    private UsersPanel mUsersPanel;
}
