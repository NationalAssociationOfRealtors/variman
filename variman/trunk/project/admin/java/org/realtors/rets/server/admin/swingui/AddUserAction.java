package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.HibernateUtils;
import org.apache.log4j.Logger;
import net.sf.hibernate.HibernateException;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 14, 2004
 * Time: 10:42:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddUserAction extends AbstractAction
{
    public AddUserAction()
    {
        super("Add User...");
    }
    
    public void actionPerformed(ActionEvent event)
    {
        AddUserDialog dialog = new AddUserDialog();
        try
        {
            if (showUntilCancelOrValid(dialog) != JOptionPane.OK_OPTION)
            {
                return;
            }

            User user = new User();
            user.setFirstName(dialog.getFirstName());
            user.setLastName(dialog.getLastName());
            user.setUsername(dialog.getUsername());
            user.changePassword(dialog.getPassword());
            user.setAgentCode(dialog.getAgentCode());
            user.setBrokerCode(dialog.getBrokerCode());

            HibernateUtils.save(user);
            AdminFrame frame = SwingUtils.getAdminFrame();
            frame.setStatusText("User " + user.getName() + " added");
            frame.refreshUsers();
            LOG.debug("New user: " + user);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
        finally
        {
            dialog.dispose();
        }
    }

    private int showUntilCancelOrValid(AddUserDialog dialog)
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

            String username = dialog.getUsername();
            if (UserUtils.findByUsername(username) == null)
            {
                break;
            }

            JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "A user already exists with this username.\n" +
                "Please choose a new username.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return response;
    }

    private static final Logger LOG =
        Logger.getLogger(AddUserAction.class);
}
