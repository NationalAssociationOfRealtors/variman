package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 13, 2004
 * Time: 3:11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersPanel extends JPanel
{
    public UsersPanel()
    {
        super(new BorderLayout());
        mUserList = new JList();
        mUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mUserList.getSelectionModel().addListSelectionListener(
            new OnSelectionChanged());

        JPanel panel = new JPanel(new BorderLayout());
        TextValuePanel tvp = new TextValuePanel();
        mFirstName = new JLabel();
        tvp.addRow("First Name:", mFirstName);
        mLastName = new JLabel();
        tvp.addRow("Last Name:", mLastName);
        mUsername = new JLabel();
        tvp.addRow("Username:", mUsername);
        mAgentCode = new JLabel();
        tvp.addRow("Agent Code:", mAgentCode);
        mBrokerCode = new JLabel();
        tvp.addRow("Broker Code:", mBrokerCode);
        panel.add(tvp, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              mUserList, panel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);
    }

    public void populateList()
    {
        final int selection = mUserList.getSelectedIndex();
        SwingWorker worker = new SwingWorker()
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
                    // Todo: UsersPanel.construct: show eror dialog
                }
                return users;
            }

            public void finished()
            {
                List users = (List) get();
                setUserLiseModel(new UserListModel(users));
                int newSelection = selection;
                if (newSelection >= users.size())
                {
                    newSelection = users.size() - 1;
                }
                mUserList.setSelectedIndex(newSelection);
            }
        };
        worker.start();
    }

    private void setUserLiseModel(UserListModel model)
    {
        mUserListModel = model;
        mUserList.setModel(model);
    }

    private void updateDetailedInfo()
    {
        int selection = mUserList.getSelectedIndex();
        if (selection != -1)
        {
            User user = mUserListModel.getUserAt(selection);
            setLabel(mFirstName, user.getFirstName());
            setLabel(mLastName, user.getLastName());
            setLabel(mUsername, user.getUsername());
            setLabel(mAgentCode, user.getAgentCode());
            setLabel(mBrokerCode, user.getBrokerCode());
        }
        else
        {
            mFirstName.setText("");
            mLastName.setText("");
            mUsername.setText("");
            mAgentCode.setText("");
            mBrokerCode.setText("");
        }
    }

    private static void setLabel(JLabel label, String text)
    {
        label.setText(text);
    }

    private class UserListModel extends AbstractListModel
    {
        public UserListModel(List users)
        {
            mUsers = users;
        }

        public int getSize()
        {
            return mUsers.size();
        }

        public Object getElementAt(int i)
        {
            User user = (User) mUsers.get(i);
            return user.getName() + " (" + user.getUsername() + ")";
        }

        public User getUserAt(int i)
        {
            return (User) mUsers.get(i);
        }

        List mUsers;
    }

    private class OnSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateDetailedInfo();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UsersPanel.class);
    private JList mUserList;
    private JLabel mFirstName;
    private JLabel mLastName;
    private JLabel mUsername;
    private JLabel mAgentCode;
    private JLabel mBrokerCode;
    private UserListModel mUserListModel;
}