/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.util.Collections;
import java.util.List;

import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

import org.apache.log4j.Logger;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxContextMenuEvent;
import org.wxwindows.wxContextMenuListener;
import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxListBox;
import org.wxwindows.wxMenu;
import org.wxwindows.wxPanel;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSplitterWindow;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;

public class UsersPage extends wxPanel
{
    public UsersPage(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxSplitterWindow splitter = new wxSplitterWindow(this, -1);
        mListBox = new wxListBox(splitter, USERS_BOX);

        panel = new wxPanel(splitter);
        wxBoxSizer pBox = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);

        wxStaticText label;

        label = new wxStaticText(panel, -1, "First Name:");
        grid.Add(label, 0, wxALIGN_LEFT | wxALIGN_CENTER_VERTICAL | wxRIGHT |
                           wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1);
        mFirstName = new wxStaticText(panel, -1, "");
        grid.Add(mFirstName, 0, wxBOTTOM, 5);

        label = new wxStaticText(panel, -1, "Last Name:");
        grid.Add(label, 0, wxALIGN_LEFT | wxALIGN_CENTER_VERTICAL | wxRIGHT |
                           wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1);
        mLastName = new wxStaticText(panel, -1, "");
        grid.Add(mLastName, 0, wxBOTTOM, 5);

        label = new wxStaticText(panel, -1, "Username:");
        grid.Add(label, 0, wxALIGN_LEFT | wxALIGN_CENTER_VERTICAL | wxRIGHT |
                           wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1);
        mUsername = new wxStaticText(panel, -1, "");
        grid.Add(mUsername, 0, wxBOTTOM, 5);
        pBox.Add(grid, 0, wxALL, 5);
        panel.SetSizer(pBox);

        splitter.SplitVertically(mListBox, panel, 200);
        box.Add(splitter, 1, wxEXPAND);

        SetSizer(box);
        EVT_LISTBOX(USERS_BOX, new OnSelectUser());
        EVT_CONTEXT_MENU(new OnContextMenu());
        EVT_MENU(ADD_USER, new OnAddUser());
        EVT_MENU(REMOVE_USER, new OnRemoveUser());
    }

    private void updateDetailedInfo()
    {
        int selection = mListBox.GetSelection();
        if (selection != -1)
        {
            User user = (User) mListBox.GetClientData(selection);
            mFirstName.SetLabel(user.getFirstName());
            mLastName.SetLabel(user.getLastName());
            mUsername.SetLabel(user.getUsername());
        }
        else
        {
            mFirstName.SetLabel("");
            mLastName.SetLabel("");
            mUsername.SetLabel("");
        }
    }

    public void populateList()
    {
        final wxWindowDisabler disabler = new wxWindowDisabler();
        mListBox.Clear();
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                List users = Collections.EMPTY_LIST;
                try
                {
                    users = UserUtils.findAll();
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                }
                return users;
            }

            public void finished()
            {
                List users = (List) get();
                for (int i = 0; i < users.size(); i++)
                {
                    User user = (User) users.get(i);
                    String text =
                        user.getName() + " (" + user.getUsername() + ")";
                    mListBox.Append(text, user);
                }
                updateDetailedInfo();
                disabler.delete();
            }
        };
        worker.start();
    }

    private void showContextMenu(wxPoint position)
    {
        wxMenu popup = new wxMenu();
        popup.Append(ADD_USER, "Add User...");
        popup.Append(REMOVE_USER, "Remove User...");
        popup.Append(EDIT_USER, "Edit User...");
        popup.Append(CHANGE_PASSWORD, "Change Password...");
        if (mListBox.GetSelection() == -1)
        {
            popup.Enable(REMOVE_USER, false);
            popup.Enable(EDIT_USER, false);
            popup.Enable(CHANGE_PASSWORD, false);
        }
        PopupMenu(popup, position);
        popup.delete();
    }

    private class OnSelectUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            updateDetailedInfo();
        }
    }

    private class OnContextMenu implements wxContextMenuListener
    {
        public void handleEvent(wxContextMenuEvent event)
        {
            showContextMenu(ScreenToClient(event.GetPosition()));
        }
    }

    private class OnAddUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new AddUserCommand().execute();
        }
    }

    private class OnRemoveUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            int selection = mListBox.GetSelection();
            if (selection == -1)
            {
                return;
            }
            User user = (User) mListBox.GetClientData(selection);
            new RemoveUserCommand(user).execute();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UsersPage.class);
    private static final int SPACER_WIDTH = 10;
    private static final int USERS_BOX = wxNewId();
    private static final int ADD_USER = wxNewId();
    private static final int REMOVE_USER = wxNewId();
    private static final int EDIT_USER = wxNewId();
    private static final int CHANGE_PASSWORD = wxNewId();

    private wxListBox mListBox;
    private wxStaticText mFirstName;
    private wxStaticText mLastName;
    private wxStaticText mUsername;
    private wxPanel panel;
}
