/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.User;

import org.apache.log4j.Logger;

import org.wxwindows.wx;
import org.wxwindows.wxJUtil;

public class EditUserCommand extends wx
{
    public EditUserCommand(User user)
    {
        mUser = user;
    }

    public void execute()
    {
        EditUserDialog dialog = null;
        try
        {
            AdminFrame frame = Admin.getAdminFrame();
            dialog = new EditUserDialog(frame, this.mUser);
            if (dialog.ShowModal() != wxID_OK)
            {
                return;
            }

            mUser.setFirstName(dialog.getFirstName());
            mUser.setLastName(dialog.getLastName());
            mUser.setAgentCode(dialog.getAgentCode());
            mUser.setBrokerCode(dialog.getBrokerCode());
            HibernateUtils.update(mUser);
            frame.SetStatusText("User " + mUser.getName() + " changed");
            frame.refreshUsers();
            LOG.debug("Changed user: " + mUser);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught exception", e);
            wxJUtil.logError("Could not edit user", e);
        }
        finally
        {
            Destroy(dialog);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(EditUserCommand.class);
    private User mUser;
}
