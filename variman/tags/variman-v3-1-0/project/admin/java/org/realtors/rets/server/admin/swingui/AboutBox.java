package org.realtors.rets.server.admin.swingui;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        String buildDate = Admin.getBuildDate();

        JPanel content = new JPanel();
        JLabel label = new JLabel(
            "<html><center>" +
            "<h1>" + Admin.ADMIN_NAME + "</h1>" +
            "<p>Version " + version + " (" + buildDate + ")</p>" +
            "<p></p>" +
            "<p>Copyright &copy; 2004-2009,<br>" +
            "The Center for REALTOR&reg; Technology</p>" +
            "</center></html>");
        content.add(label);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.addMouseListener(new CloseOnClickListener());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, frame);
    }

    private class CloseOnClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent event)
        {
            dispose();
        }
    }
}
