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
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.wxwindows.wx;

public class RemoveUserCommand extends wx
{
    public void execute()
    {
        SessionHelper helper = Admin.createSessionHelper();
        try
        {
            AdminFrame frame = Admin.getAdminFrame();
            RemoveUserDialog dialog =
                new RemoveUserDialog(frame);
            if (dialog.ShowModal() == wxID_CANCEL)
            {
                return;
            }
            dialog.Destroy();

            String username = dialog.getUsername();
            User user = null;
            Session session = helper.beginTransaction();
            List users = session.find(
                "SELECT user " +
                "  FROM User user " +
                " WHERE user.username = ?",
                username, Hibernate.STRING);
            if (users.size() == 1)
            {
                user = (User) users.get(0);
                session.delete(user);
                LOG.debug("User deleted: " + user);
                frame.SetStatusText("User " + user.getName() + " removed");
            }
            else
            {
                LOG.debug("Expecting 1 user, found: " + users.size());
                wxMessageBox("User not found: " + username,
                             "User Not Found", wxOK | wxICON_EXCLAMATION,
                             frame);
            }
            helper.commit();
        }
        catch (HibernateException e)
        {
            helper.rollback(LOG);
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
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(RemoveUserCommand.class);
}
