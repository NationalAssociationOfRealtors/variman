/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.util.List;
import java.util.Collections;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;
import org.realtors.rets.server.RetsServer;

import org.apache.log4j.Logger;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxListCtrl;
import org.wxwindows.wxPanel;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;
import org.wxwindows.wxJWorker;

public class UsersPage extends wxPanel
{
    public UsersPage(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        mList = new wxListCtrl(this, -1, wxDefaultPosition, wxDefaultSize,
                               wxLC_REPORT | wxSUNKEN_BORDER);
        mList.InsertColumn(0, "First Name");
        mList.InsertColumn(1, "Last Name");
        mList.InsertColumn(2, "Username");
        mList.SetColumnWidth(0, 150);
        mList.SetColumnWidth(1, 150);
        mList.SetColumnWidth(2, 150);
        box.Add(mList, 1, wxEXPAND);

        SetSizer(box);
    }

    public void populateList()
    {
        final wxWindowDisabler disabler = new wxWindowDisabler();
        mList.DeleteAllItems();
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                List users = Collections.EMPTY_LIST;
                try
                {
                    users = findAllUsers();
                }
                catch (HibernateException e)
                {
                    LOG.error("Caught exception", e);
                }
                return users;
            }

            public void finished()
            {
                List users = (List) get();
                for (int i = 0; i < users.size(); i++)
                {
                    User user = (User) users.get(i);
                    mList.InsertItem(i, user.getFirstName());
                    mList.SetItem(i, 1, user.getLastName());
                    mList.SetItem(i, 2, user.getUsername());
                }
                disabler.delete();
            }
        };
        worker.start();
    }

    private List findAllUsers() throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginSession();
            return session.find("SELECT user " +
                                "FROM User user " +
                                "ORDER BY user.lastName");
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UsersPage.class);

    private wxListCtrl mList;
}
