package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.awt.Frame;
import java.awt.Component;
import java.awt.Color;

import javax.swing.*;
import javax.swing.border.Border;

import org.realtors.rets.server.User;
import org.realtors.rets.server.HibernateUtils;
import org.apache.log4j.Logger;
import net.sf.hibernate.HibernateException;

public class ChangePasswordAction extends AbstractAction
{
    public ChangePasswordAction(UsersPanel usersPanel)
    {
        super("Change Password...");
        mUsersPanel = usersPanel;
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            User user = mUsersPanel.getSelectedUser();
            if (user == null)
            {
                LOG.warn("Change password of null user");
                return;
            }

            AdminFrame frame = SwingUtils.getAdminFrame();
            GetPasswordDialog dialog = new GetPasswordDialog(
                frame, "Enter new password for " + user.getName() + ":");
            dialog.show();
            if (dialog.getResponse() != JOptionPane.OK_OPTION)
            {
                return;
            }

            user.changePassword(dialog.getPassword());
            HibernateUtils.update(user);
            frame.setStatusText("Password changed for " + user.getName());
            LOG.debug("Password changed for: " + user);
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private class GetPasswordDialog extends JDialog
    {
        public GetPasswordDialog(Frame frame, String message)
        {
            super(frame);
            setTitle("Change Password");
            setModal(true);

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            JLabel label = new JLabel(message);
            label.setAlignmentX(1.0f);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            content.add(label);
            mPasswordField = new JPasswordField();
            content.add(mPasswordField);

            Box buttonBox = Box.createHorizontalBox();
            buttonBox.add(Box.createHorizontalGlue());
            buttonBox.add(new JButton(new ChangePasswordButtonAction()));
            buttonBox.add(Box.createHorizontalStrut(5));
            buttonBox.add(new JButton(new CancelButtonAction()));
            buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            content.add(buttonBox);
            content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            getContentPane().add(content);
            pack();
            setResizable(false);
            SwingUtils.centerOnFrame(this, frame);
            mResponse = JOptionPane.CANCEL_OPTION;
        }

        public String getPassword()
        {
            return mPassword;
        }

        public int getResponse()
        {
            return mResponse;
        }

        private class ChangePasswordButtonAction extends AbstractAction
        {
            public ChangePasswordButtonAction()
            {
                super("Change Password");
            }

            public void actionPerformed(ActionEvent event)
            {
                mResponse = JOptionPane.OK_OPTION;
                mPassword = new String(mPasswordField.getPassword());
                dispose();
            }
        }

        private class CancelButtonAction extends AbstractAction
        {
            public CancelButtonAction()
            {
                super("Cancel");
            }

            public void actionPerformed(ActionEvent event)
            {
                mResponse = JOptionPane.CANCEL_OPTION;
                dispose();
            }

        }

        private int mResponse;
        private JPasswordField mPasswordField;
        private String mPassword;
    }

    private static final Logger LOG =
        Logger.getLogger(ChangePasswordAction.class);
    private UsersPanel mUsersPanel;
}
