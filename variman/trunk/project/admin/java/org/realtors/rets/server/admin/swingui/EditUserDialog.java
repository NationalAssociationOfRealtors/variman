package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;

import javax.swing.*;

import org.realtors.rets.server.User;

public class EditUserDialog extends JDialog
{
    public EditUserDialog(Frame parent, User user)
    {
        super(parent);
        setTitle("Edit User: " + user.getName());
        setModal(true);

        setSize(320, 240);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, parent);
        mResponse = JOptionPane.CANCEL_OPTION;
    }

    public int getResponse()
    {
        return mResponse;
    }

    private int mResponse;
}
