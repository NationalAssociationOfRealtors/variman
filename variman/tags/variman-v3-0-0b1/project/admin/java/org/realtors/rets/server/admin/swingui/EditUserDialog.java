package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.realtors.rets.server.User;

public class EditUserDialog extends JDialog
{
    public EditUserDialog(Frame parent, User user)
    {
        super(parent);
        setModal(true);
        setTitle("Edit User: " + user.getName());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        TextValuePanel tvp = new TextValuePanel();

        mFirstName = new JTextField(user.getFirstName(), TEXT_WIDTH);
        tvp.addRow("First Name:", mFirstName);
        mLastName = new JTextField(user.getLastName(), TEXT_WIDTH);
        tvp.addRow("Last Name:", mLastName);
        mAgentCode = new JTextField(user.getAgentCode(), TEXT_WIDTH);
        tvp.addRow("Agent Code:", mAgentCode);
        mBrokerCode = new JTextField(user.getBrokerCode(), TEXT_WIDTH);
        tvp.addRow("Broker Code:", mBrokerCode);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton(new SaveChangesButtonAction()));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelButtonAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, parent);
        mResponse = JOptionPane.CANCEL_OPTION;
    }

    public int getResponse()
    {
        return mResponse;
    }

    public String getFirstName()
    {
        return mFirstName.getText();
    }

    public String getLastName()
    {
        return mLastName.getText();
    }

    public String getAgentCode()
    {
        return mAgentCode.getText();
    }

    public String getBrokerCode()
    {
        return mBrokerCode.getText();
    }

    private class SaveChangesButtonAction extends AbstractAction
    {
        public SaveChangesButtonAction()
        {
            super("Save Changes");
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

    private static final int TEXT_WIDTH = 25;
    private int mResponse;
    private JTextField mFirstName;
    private JTextField mLastName;
    private JTextField mAgentCode;
    private JTextField mBrokerCode;
}
