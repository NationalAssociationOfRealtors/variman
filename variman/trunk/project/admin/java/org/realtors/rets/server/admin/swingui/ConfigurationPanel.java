/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel
{
    public ConfigurationPanel()
    {
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        JPanel label = new JPanel();
        label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
        JLabel tmp = new JLabel("RETS");
        label.add(tmp);
        label.add(Box.createRigidArea(new Dimension(5, 0)));
        JPanel horizontalRule = new JPanel();
        horizontalRule.setBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        horizontalRule.setLayout(new BoxLayout(horizontalRule, BoxLayout.Y_AXIS));
        horizontalRule.add(Box.createVerticalStrut(0));
        label.add(horizontalRule);

        panel2.add(label);

        JPanel retsConfig = new JPanel();
//        retsConfig.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        retsConfig.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);

        JLabel jLabel = new JLabel("Listening Port:");
//        jLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
//        gridbag.setConstraints(jLabel, c);
        retsConfig.add(jLabel, c);

        JTextField jTextField = new JTextField(10);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
//        gridbag.setConstraints(jTextField, c);
        retsConfig.add(jTextField, c);

        jLabel = new JLabel("Metadatadatadata Directory:");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
//        gridbag.setConstraints(jLabel, c);
        retsConfig.add(jLabel, c);

        jTextField = new JTextField("c:\\blah");
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
//        gridbag.setConstraints(jTextField, c);
        retsConfig.add(jTextField, c);

        panel2.add(retsConfig);

        panel2.add(new HeaderPanel("Database"));

        TextValuePanel tvp = new TextValuePanel();
        tvp.addRow("Some Text:", new JTextField());
        tvp.addRow("Blah:", new JTextField());
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        panel2.add(tvp);

//        Box labels = Box.createVerticalBox();
//        Box values = Box.createVerticalBox();
//        labels.add(new JLabel("Listening Port:"));
//        values.add(new JTextField(10));
//        labels.add(new JLabel("Metadata Directory:"));
//        values.add(new JTextField("c:\\blah"));
//        Box retsConfig = Box.createHorizontalBox();
//        retsConfig.add(labels);
//        retsConfig.add(values);
//        add(retsConfig);

//        add(Box.createVerticalGlue());
        add(panel2, BorderLayout.NORTH);
    }
}
