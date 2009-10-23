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
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.SecurityConstraints;

public class AddGroupAction extends AbstractAction
{
    public AddGroupAction()
    {
        super("Add Group...");
    }
    
    public void actionPerformed(ActionEvent event)
    {
        AdminFrame frame = SwingUtils.getAdminFrame();
        GroupDialog dialog = new GroupDialog(frame);

        try
        {
            if (showUntilCancelOrValid(dialog) != JOptionPane.OK_OPTION)
            {
                return;
            }

            Group group = new Group(dialog.getGroupName());
            group.setDescription(dialog.getDescription());

            HibernateUtils.save(group);

            // Update this after hibernate, just in case of DB failure
            RetsConfig retsConfig = Admin.getRetsConfig();
            SecurityConstraints securityConstraints =
                retsConfig.getSecurityConstraints();
            GroupRules rules =
                securityConstraints.getRulesForGroup(group.getName());
            dialog.transferToGropuRules(rules);

            frame.setStatusText("Group " + group.getName() + " added");
            frame.refreshGroups();
            // Group rules are stored in RetsConfig
            Admin.setRetsConfigChanged(true);
            LOG.debug("New group: " + group.dump());
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
        Logger.getLogger(AddGroupAction.class);
}
