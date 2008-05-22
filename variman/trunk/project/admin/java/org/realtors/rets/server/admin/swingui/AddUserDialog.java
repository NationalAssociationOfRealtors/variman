package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 14, 2004
 * Time: 12:09:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddUserDialog extends JDialog
{
    public AddUserDialog()
    {
        super(SwingUtils.getAdminFrame());
        setModal(true);
        setTitle("Add User");

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();

        mFirstName = new JTextField(TEXT_WIDTH);
        tvp.addRow("First Name:", mFirstName);
        mLastName = new JTextField(TEXT_WIDTH);
        tvp.addRow("Last Name:", mLastName);
        mUsername = new JTextField(TEXT_WIDTH);
        tvp.addRow("Username:", mUsername);
        mPassword = new JPasswordField(TEXT_WIDTH);
        tvp.addRow("Password:", mPassword);
        mAgentCode = new JTextField(TEXT_WIDTH);
        tvp.addRow("Agent Code:", mAgentCode);
        mBrokerCode = new JTextField(TEXT_WIDTH);
        tvp.addRow("Broker Code:", mBrokerCode);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new AddUserButtonAction()));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelButtonAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
        mResponse = JOptionPane.CANCEL_OPTION;
    }

    public int getResponse()
    {
        return mResponse;
    }

    public String getUsername()
    {
        return mUsername.getText();
    }

    public String getAgentCode()
    {
        return mAgentCode.getText();
    }

    public String getBrokerCode()
    {
        return mBrokerCode.getText();
    }

    public String getFirstName()
    {
        return mFirstName.getText();
    }

    public String getLastName()
    {

        return mLastName.getText();
    }

    public String getPassword()
    {
        return new String(mPassword.getPassword());
    }

    private class AddUserButtonAction extends AbstractAction
    {
        public AddUserButtonAction()
        {
            super("Add User");
        }

        public void actionPerformed(ActionEvent event)
        {
            mResponse = JOptionPane.OK_OPTION;
            setVisible(false);
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
            setVisible(false);
        }
    }

    private static final int TEXT_WIDTH = 20;
    private JTextField mFirstName;
    private JTextField mLastName;
    private JTextField mUsername;
    private JPasswordField mPassword;
    private JTextField mAgentCode;
    private JTextField mBrokerCode;
    private int mResponse;
}
