package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.GroupUtils;
import org.apache.log4j.Logger;
import net.sf.hibernate.HibernateException;

public class AddGroupAction extends AbstractAction
{
    public AddGroupAction()
    {
        super("Add Group...");
    }
    
    public void actionPerformed(ActionEvent event)
    {
        AddGroupDialog dialog = new AddGroupDialog();
        try
        {
            if (showUntilCancelOrValid(dialog) != JOptionPane.OK_OPTION)
            {
                return;
            }

            Group group = new Group();
            group.setName(dialog.getGroupName());
            group.setDescription(dialog.getDescription());

            HibernateUtils.save(group);
            AdminFrame frame = SwingUtils.getAdminFrame();
            frame.setStatusText("Group " + group.getName() + " added");
            frame.refreshGroups();
            LOG.debug("New group: " + group);
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

    private int showUntilCancelOrValid(AddGroupDialog dialog)
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

            String groupName = dialog.getGroupName();
            if (GroupUtils.findByName(groupName) == null)
            {
                break;
            }

            JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "A group already exists with this name.\n" +
                "Please choose a new name.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return response;
    }

    private static final Logger LOG =
        Logger.getLogger(AddGroupAction.class);
}
