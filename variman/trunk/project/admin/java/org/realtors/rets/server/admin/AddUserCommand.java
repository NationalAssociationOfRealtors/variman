/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.wxwindows.wx;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class AddUserCommand extends wx
{
    public void execute()
    {
        SessionHelper helper = Admin.createSessionHelper();
        try
        {
            AdminFrame frame = Admin.getAdminFrame();
            AddUserDialog addUserDialog = new AddUserDialog(frame);
            int response = addUserDialog.ShowModal();
            addUserDialog.Destroy();
            if (response == wxID_CANCEL)
            {
                return;
            }

            User user = new User();
            user.setFirstName(addUserDialog.getFirstName());
            user.setLastName(addUserDialog.getLastName());
            user.setUsername(addUserDialog.getUsername());
            user.changePassword(addUserDialog.getPassword());
            Session session = helper.beginTransaction();
            session.save(user);
            helper.commit();
            frame.SetStatusText("User " + user.getName() + " added");
            LOG.debug("New user: " + user);
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
        Logger.getLogger(AddUserCommand.class);
}
