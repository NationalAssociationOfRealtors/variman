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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.wxwindows.wx;

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
            HibernateUtils.update(mUser);
            frame.SetStatusText("User " + mUser.getName() + " changed");
            frame.refreshUsers();
            LOG.debug("Changed user: " + mUser);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught exception", e);
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            String stackTrace = stackTraceWriter.toString();
            stackTrace = StringUtils.replace(stackTrace, "\t", "    ");
            wxLogMessage(stackTrace);
            wxLogError("Could not change user.");
        }
        finally
        {
            if (dialog != null)
            {
                dialog.Destroy();
            }
        }
    }

    private static final Logger LOG =
        Logger.getLogger(EditUserCommand.class);
    private User mUser;
}
