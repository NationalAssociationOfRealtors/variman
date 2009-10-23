package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.HibernateException;

import org.apache.log4j.Logger;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 13, 2004
 * Time: 3:11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersPanel extends AdminTab
{
    public UsersPanel(JMenu userMenu)
    {
        super(new BorderLayout());
        mUserListModel = new ListListModel();
        mUserList = new JList(mUserListModel);
        mUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mUserList.getSelectionModel().addListSelectionListener(
            new OnUserSelectionChanged());
        mUserList.addMouseListener(new ListDoubleClickListener());

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
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(tvp, BorderLayout.NORTH);

        JPanel groupsPanel = createGroupsPanel();
        panel.add(groupsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mUserList);
        scrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              scrollPane, panel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        mUserMenu = userMenu;
        mUserMenu.setEnabled(false);
        mPopup = new JPopupMenu();

        mAddUserAction = new AddUserAction();
        mUserMenu.add(mAddUserAction);
        mPopup.add(mAddUserAction);

        mRemoveUserAction = new RemoveUserAction(this);
        mUserMenu.add(mRemoveUserAction);
        mPopup.add(mRemoveUserAction);

        mChangePasswordAction = new ChangePasswordAction(this);
        mUserMenu.add(mChangePasswordAction);
        mPopup.add(mChangePasswordAction);

        mEditUserAction = new EditUserAction(this);
        mUserMenu.add(mEditUserAction);
        mPopup.add(mEditUserAction);

        PopupListener popupListener = new PopupListener();
        mUserList.addMouseListener(popupListener);
        splitPane.addMouseListener(popupListener);
    }

    private JPanel createGroupsPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new HeaderPanel("Groups"), BorderLayout.NORTH);

        JPanel box = new JPanel(new BorderLayout());
        mGroupsListModel = new ListListModel();
        mGroupsList = new JList(mGroupsListModel);
        mGroupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mGroupsList.getSelectionModel().addListSelectionListener(
            new OnGroupSelectionChanged());
        JScrollPane scrollPane = new JScrollPane(mGroupsList);
        scrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        box.add(scrollPane);
        box.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        panel.add(box, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        mAddGroupButtonAction = new AddGroupButtonAction(this);
        buttonBox.add(new JButton(mAddGroupButtonAction));
        buttonBox.add(Box.createHorizontalStrut(5));
        mRemoveGroupButtonAction = new RemoveGroupButtonAction(this);
        buttonBox.add(new JButton(mRemoveGroupButtonAction));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(buttonBox, BorderLayout.SOUTH);
        return panel;
    }

    public User getSelectedUser()
    {
        return (User) mUserList.getSelectedValue();
    }

    public Group getSelectedGroup()
    {
        return (Group) mGroupsList.getSelectedValue();
    }

    public void tabSelected()
    {
        mUserMenu.setEnabled(true);
    }

    public void tabDeselected()
    {
        mUserMenu.setEnabled(false);
    }

    public void refreshTab()
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
                mUserListModel.setList(users);

                int newSelection = selection;
                if (newSelection >= users.size())
                {
                    newSelection = users.size() - 1;
                }
                mUserList.setSelectedIndex(newSelection);
                updateComponentsFromSelection();
            }
        };
        worker.start();
    }

    private void updateComponentsFromSelection()
    {
        User user = getSelectedUser();
        if (user != null)
        {
            setLabel(mFirstName, user.getFirstName());
            setLabel(mLastName, user.getLastName());
            setLabel(mUsername, user.getUsername());
            setLabel(mAgentCode, user.getAgentCode());
            setLabel(mBrokerCode, user.getBrokerCode());
            mRemoveUserAction.setEnabled(true);
            mChangePasswordAction.setEnabled(true);
            mEditUserAction.setEnabled(true);

            updateGroupList(user);
        }
        else
        {
            mFirstName.setText("");
            mLastName.setText("");
            mUsername.setText("");
            mAgentCode.setText("");
            mBrokerCode.setText("");
            mRemoveUserAction.setEnabled(false);
            mChangePasswordAction.setEnabled(false);
            mEditUserAction.setEnabled(false);

            mAddGroupButtonAction.setEnabled(false);
            mRemoveGroupButtonAction.setEnabled(false);
            mGroupsListModel.setList(Collections.EMPTY_LIST);
        }
    }

    private void updateGroupList(User user)
    {
        try
        {
            SortedSet groups = UserUtils.getGroups(user);
            mGroupsListModel.setList(new ArrayList(groups));
            updateGroupButtons();
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private void updateGroupButtons()
    {
        mAddGroupButtonAction.setEnabled(true);
        if (mGroupsList.getSelectedIndex() != -1)
        {
            mRemoveGroupButtonAction.setEnabled(true);
        }
        else
        {
            mRemoveGroupButtonAction.setEnabled(false);
        }
    }

    private static void setLabel(JLabel label, String text)
    {
        label.setText(text);
    }

    private class OnUserSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateComponentsFromSelection();
        }
    }

    private class OnGroupSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateGroupButtons();
        }
    }

    private class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        public void mouseReleased(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent event)
        {
            if (mPopup.isPopupTrigger(event))
            {
                mPopup.show(event.getComponent(), event.getX(), event.getY());
            }
        }
    }

    private class ListDoubleClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent event)
        {
            if (event.getClickCount() == 2)
            {
                int index = mUserList.locationToIndex(event.getPoint());
                mUserList.ensureIndexIsVisible(index);
                mUserList.setSelectedIndex(index);
                mEditUserAction.actionPerformed(null);
            }
        }
    }

    private static class AddGroupButtonAction extends AbstractAction
    {
        public AddGroupButtonAction(UsersPanel usersPanel)
        {
            super("Add Group...");
            mUsersPanel = usersPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            try
            {
                List availableGroups = GroupUtils.findAll();
                User user = mUsersPanel.getSelectedUser();
                availableGroups.removeAll(UserUtils.getGroups(user));
                Group[] selectionValues =
                    (Group[]) availableGroups.toArray(
                        new Group[availableGroups.size()]);
                Group group = (Group) JOptionPane.showInputDialog(
                    mUsersPanel, "Enter Group:", "Add Group",
                    JOptionPane.PLAIN_MESSAGE, null, selectionValues, null);
                if (group == null)
                {
                    return;
                }
                SortedSet groups = UserUtils.getGroups(user);
                groups.add(group);
                UserUtils.updateGroups(user, groups);
                mUsersPanel.refreshTab();
            }
            catch (Exception e)
            {
                LOG.error("Caught", e);
            }
        }

        private UsersPanel mUsersPanel;

    }

    private static class RemoveGroupButtonAction extends AbstractAction
    {
        public RemoveGroupButtonAction(UsersPanel usersPanel)
        {
            super("Remove Group...");
            mUsersPanel = usersPanel;
        }

        public void actionPerformed(ActionEvent event)
        {
            try
            {
                Group group = mUsersPanel.getSelectedGroup();
                if (group == null)
                {
                    LOG.warn("Removing null group");
                    return;
                }
                User user = mUsersPanel.getSelectedUser();
                SortedSet groups = UserUtils.getGroups(user);
                groups.remove(group);
                UserUtils.updateGroups(user, groups);
                mUsersPanel.refreshTab();
            }
            catch (Exception e)
            {
                LOG.error("Caught", e);
            }
        }

        private UsersPanel mUsersPanel;
    }

    private static final Logger LOG =
        Logger.getLogger(UsersPanel.class);
    private JList mUserList;
    private JLabel mFirstName;
    private JLabel mLastName;
    private JLabel mUsername;
    private JLabel mAgentCode;
    private JLabel mBrokerCode;
    private JPopupMenu mPopup;
    private AddUserAction mAddUserAction;
    private RemoveUserAction mRemoveUserAction;
    private ChangePasswordAction mChangePasswordAction;
    private EditUserAction mEditUserAction;
    private JList mGroupsList;
    private AddGroupButtonAction mAddGroupButtonAction;
    private RemoveGroupButtonAction mRemoveGroupButtonAction;
    private ListListModel mUserListModel;
    private ListListModel mGroupsListModel;
    private JMenu mUserMenu;
}
