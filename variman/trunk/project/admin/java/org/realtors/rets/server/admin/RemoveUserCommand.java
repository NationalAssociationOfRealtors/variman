/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.PrintWriter;
import java.io.StringWriter;

import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.wxwindows.wx;

public class RemoveUserCommand extends wx
{
    public RemoveUserCommand()
    {
        mUser = null;
    }

    public RemoveUserCommand(User user)
    {
        mUser = user;
    }

    public void execute()
    {
        try
        {
            AdminFrame frame = Admin.getAdminFrame();
            String username = getUsername();
            if (username == null)
            {
                return;
            }

            User user = UserUtils.findByUsername(username);
            if (user != null)
            {
                UserUtils.delete(user);
                LOG.debug("User deleted: " + user);
                frame.SetStatusText("User " + user.getName() + " removed");
            }
            else
            {
                wxMessageBox("User not found: " + username,
                             "User Not Found", wxOK | wxICON_EXCLAMATION,
                             frame);
            }
            frame.refreshUsers();
        }
        catch (HibernateException e)
        {
            LOG.error("Caught exception", e);
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            String stackTrace = stackTraceWriter.toString();
            stackTrace = StringUtils.replace(stackTrace, "\t", "    ");
            wxLogMessage(stackTrace);
            wxLogError("Could not remove user.");
        }
    }

    private String getUsername()
    {
        AdminFrame frame = Admin.getAdminFrame();
        if (mUser != null)
        {
            int response = wxMessageBox(
                "Are you sure you want to delete " + mUser.getName() + "?",
                "Confirm Delete", wxYES_NO | wxNO_DEFAULT | wxICON_QUESTION,
                frame);
            if (response != wxYES)
            {
                return null;
            }
            return mUser.getUsername();
        }

        String username = null;    
        RemoveUserDialog dialog = new RemoveUserDialog(frame);
        if (dialog.ShowModal() == wxID_OK)
        {
            username = dialog.getUsername();
        }
        dialog.Destroy();
        return username;
    }

    private static final Logger LOG =
        Logger.getLogger(RemoveUserCommand.class);
    private User mUser;
}
