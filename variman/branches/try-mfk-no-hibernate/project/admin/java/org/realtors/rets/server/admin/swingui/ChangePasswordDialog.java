package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.realtors.rets.server.User;

public class ChangePasswordDialog extends JDialog
{
    public ChangePasswordDialog(Frame frame, User user)
    {
        super(frame);
        setTitle("Change Password: " + user.getName());
        setModal(true);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(
            "Enter new password for " + user.getName() + ":");
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
