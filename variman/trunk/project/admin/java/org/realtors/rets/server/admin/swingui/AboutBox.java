package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;

import javax.swing.*;

import org.realtors.rets.server.admin.Admin;

public class AboutBox extends JDialog
{
    public AboutBox(Frame frame)
    {
        super(frame);
        setTitle("About");
        setModal(true);
        setSize(50, 50);

        String version = Admin.getVersion();

        JPanel content = new JPanel();
        JLabel label = new JLabel(
            "<html><center>" +
            "<h1>Rex Admin</h1>" +
            "<p>Version " + version + "</p>" +
            "<p></p>" +
            "<p>Copyright &copy; 2004-2005,<br>" +
            "The Center for REALTOR&reg; Technology</p>" +
            "</center></html>");
        content.add(label);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, frame);
    }
}
