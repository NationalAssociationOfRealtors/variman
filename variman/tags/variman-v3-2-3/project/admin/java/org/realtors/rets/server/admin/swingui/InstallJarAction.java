package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.admin.Admin;

public class InstallJarAction extends AbstractAction
{
    public InstallJarAction()
    {
        super("Install Jar File...");
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new JarFileChooser());
            AdminFrame frame = SwingUtils.getAdminFrame();
            int result = fileChooser.showDialog(frame, "Install");
            if (result != JFileChooser.APPROVE_OPTION)
            {
                return;
            }
            File sourceJarFile = fileChooser.getSelectedFile();
            String homeDirectory = Admin.getHomeDirectory();
            File libDirectory = new File(homeDirectory, "variman/WEB-INF/lib");
            IOUtils.copyFile(sourceJarFile, libDirectory);
            JOptionPane.showMessageDialog(
                frame,"You must restart " + Admin.ADMIN_NAME + " and " +
                      Admin.SERVER_NAME + " for\n" +
                      "them to recognize the new Jar file.", "Restart Warning",
                JOptionPane.WARNING_MESSAGE);
        }
        catch (IOException e)
        {
            LOG.error("Caught", e);
        }
    }

    private class JarFileChooser extends FileFilter
    {
        public boolean accept(File file)
        {
            if (file.isDirectory())
            {
                return true;
            }

            if (file.getPath().endsWith(".jar"))
            {
                return true;
            }
            return false;
        }

        public String getDescription()
        {
            return "Jar Files";
        }

    }

    private static final Logger LOG =
        Logger.getLogger(InstallJarAction.class);
}
