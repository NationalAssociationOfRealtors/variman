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
import org.wxwindows.wxTextEntryDialog;

public class ChangePasswordCommand extends wx
{
    public ChangePasswordCommand(User user)
    {
        mUser = user;
    }

    public void execute()
    {
        try
        {
            String newPassword = getNewPassword();
            if (newPassword == null)
            {
                return;
            }

            mUser.changePassword(newPassword);
            HibernateUtils.update(mUser);
            Admin.getAdminFrame().SetStatusText("Password changed for " +
                                                mUser.getName());
            LOG.debug("User: " + mUser);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    private String getNewPassword()
    {
        String newPassword = null;
        String message = "Enter new password for " + mUser.getName() + ":";
        wxTextEntryDialog dialog =
            new wxTextEntryDialog(Admin.getAdminFrame(), message,
                                  "Change Password", "",
                                  wxOK | wxCANCEL | wxCENTER | wxTE_PASSWORD);
        if (dialog.ShowModal() == wxID_OK)
        {
            newPassword = dialog.GetValue();
        }
        return newPassword;
    }

    private static final Logger LOG =
        Logger.getLogger(ChangePasswordCommand.class);
    private User mUser;
}
