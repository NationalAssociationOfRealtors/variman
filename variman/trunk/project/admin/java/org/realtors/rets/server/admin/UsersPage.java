/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.apache.log4j.Logger;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxContextMenuEvent;
import org.wxwindows.wxContextMenuListener;
import org.wxwindows.wxJUtil;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxListBox;
import org.wxwindows.wxMenu;
import org.wxwindows.wxPanel;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSplitterWindow;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;

import java.util.Collections;
import java.util.List;

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
        TwoColumnGridSizer grid = new TwoColumnGridSizer(panel);
        mFirstName = new wxStaticText(panel, -1, "");
        grid.addRow("First Name:", mFirstName);
        mLastName = new wxStaticText(panel, -1, "");
        grid.addRow("Last Name:", mLastName);
        mUsername = new wxStaticText(panel, -1, "");
        grid.addRow("Username:", mUsername);

        pBox.Add(grid, 0, wxALL, 5);
        panel.SetSizer(pBox);

        splitter.SplitVertically(mListBox, panel, 200);
        box.Add(splitter, 1, wxEXPAND);

        SetSizer(box);
        EVT_LISTBOX(USERS_BOX, new OnSelectUser());
        EVT_LISTBOX_DCLICK(USERS_BOX, new OnDoubleClickUser());
        EVT_CONTEXT_MENU(new OnContextMenu());
        EVT_MENU(ADD_USER, new OnAddUser());
        EVT_MENU(REMOVE_USER, new OnRemoveUser());
        EVT_MENU(EDIT_USER, new OnEditUser());
        EVT_MENU(CHANGE_PASSWORD, new OnChangePassword());
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
        wxBeginBusyCursor();
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
                    wxJUtil.logError("Could not get users.", t);
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
                wxEndBusyCursor();
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

    private User getSelectedUser()
    {
        int selection = mListBox.GetSelection();
        if (selection != -1)
        {
            return (User) mListBox.GetClientData(selection);
        }
        else
        {
            return null;
        }
    }

    private void editUser()
    {
        User user = getSelectedUser();
        if (user == null)
        {
            LOG.warn("Selection should not be empty");
            return;
        }
        new EditUserCommand(user).execute();
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
            User user = getSelectedUser();
            if (user == null)
            {
                LOG.warn("Selection should not be empty");
                return;
            }
            new RemoveUserCommand(user).execute();
        }
    }

    private class OnEditUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            editUser();
        }
    }

    private class OnDoubleClickUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            editUser();
        }
    }

    private class OnChangePassword implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            User user = getSelectedUser();
            if (user == null)
            {
                LOG.warn("Selection should not be empty");
                return;
            }
            new ChangePasswordCommand(user).execute();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UsersPage.class);
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
