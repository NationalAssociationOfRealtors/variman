package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.User;
import org.apache.log4j.Logger;

public class EditUserAction extends AbstractAction
{
    public EditUserAction(UsersPanel usersPanel)
    {
        super("Edit User...");
        mUsersPanel = usersPanel;
    }

    public void actionPerformed(ActionEvent event)
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
    }

    private static final Logger LOG =
        Logger.getLogger(EditUserAction.class);
    private UsersPanel mUsersPanel;
}
