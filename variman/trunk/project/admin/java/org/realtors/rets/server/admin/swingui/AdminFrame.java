/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.admin.AdminUtils;

public class AdminFrame extends JFrame implements ActionListener
{
    public AdminFrame(String title)
    {
        super(title);
        initConfig();

        addWindowListener(new OnClose());
        Container content = getContentPane();

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        getContentPane().add(label);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuItem item = new JMenuItem("Save", KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Quit", KeyEvent.VK_Q);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask));
        item.addActionListener(new OnQuit());
        menu.add(item);
        setJMenuBar(menuBar);

        mTabbedPane = new JTabbedPane();
        mTabbedPane.addTab("Configuration", new ConfigurationPanel());
        mTabbedPane.addTab("Users", new JPanel());
        content.add(mTabbedPane, BorderLayout.CENTER);

        setSize(640, 480);
    }

    private void initConfig()
    {
        try
        {
            AdminUtils.initConfig();
        }
        catch (RetsServerException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    public void quit()
    {
        dispose();
        System.exit(0);
    }

    private class OnClose extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            quit();
        }
    }

    private class OnQuit implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            quit();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        JMenuItem source = (JMenuItem)(e.getSource());
        String newline = "\n";
        String s = "Action event detected."
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")";
        System.out.println(s);
    }

    // Returns just the class name -- no package info.
    protected String getClassName(Object o)
    {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }

    private static final Logger LOG =
        Logger.getLogger(AdminFrame.class);

    private JTabbedPane mTabbedPane;
}
