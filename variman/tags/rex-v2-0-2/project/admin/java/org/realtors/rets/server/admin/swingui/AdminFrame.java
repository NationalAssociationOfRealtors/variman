/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class AdminFrame extends JFrame implements ActionListener
{
    public AdminFrame(String title)
    {
        super(title);
        addWindowListener(new OnClose());
        Container content = getContentPane();

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        getContentPane().add(label);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem item = new JMenuItem("Save", KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                   KeyEvent.CTRL_MASK));
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Quit", KeyEvent.VK_Q);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                   KeyEvent.CTRL_MASK));
        item.addActionListener(new OnQuit());
        menu.add(item);
        setJMenuBar(menuBar);

        mTabbedPane = new JTabbedPane();
        mTabbedPane.addTab("Configuration", new ConfigurationPanel());
        mTabbedPane.addTab("Users", new JPanel());
        content.add(mTabbedPane, BorderLayout.CENTER);

        setSize(640, 480);
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

    private JTabbedPane mTabbedPane;
}
