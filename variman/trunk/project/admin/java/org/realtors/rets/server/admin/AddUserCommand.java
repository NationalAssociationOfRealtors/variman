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

import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.wxwindows.wx;

public class AddUserCommand extends wx
{
    public void execute()
    {
        AddUserDialog dialog = null;
        try
        {
            AdminFrame frame = Admin.getAdminFrame();
            dialog = new AddUserDialog(frame);
            if (showUntilCancelOrValid(dialog) != wxID_OK)
            {
                return;
            }

            User user = new User();
            user.setFirstName(dialog.getFirstName());
            user.setLastName(dialog.getLastName());
            user.setUsername(dialog.getUsername());
            user.changePassword(dialog.getPassword());
            HibernateUtils.save(user);
            frame.SetStatusText("User " + user.getName() + " added");
            frame.refreshUsers();
            LOG.debug("New user: " + user);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught exception", e);
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            String stackTrace = stackTraceWriter.toString();
            stackTrace = StringUtils.replace(stackTrace, "\t", "    ");
            wxLogMessage(stackTrace);
            wxLogError("Could not add user.");
        }
        finally
        {
            if (dialog != null)
            {
                dialog.Destroy();
            }
        }
    }

    private int showUntilCancelOrValid(AddUserDialog dialog)
        throws HibernateException
    {
        int response = wxID_CANCEL;
        while (true)
        {
            response = dialog.ShowModal();
            if (response == wxID_CANCEL)
            {
                break;
            }

            String username = dialog.getUsername();
            if (UserUtils.findByUsername(username) == null)
            {
                break;
            }
            wxMessageBox("A user already exists with this username.\n" +
                         "Please choose a new username.", "Error",
                         wxOK | wxICON_EXCLAMATION, Admin.getAdminFrame());
        };
        return response;
    }

    private static final Logger LOG =
        Logger.getLogger(AddUserCommand.class);
}
