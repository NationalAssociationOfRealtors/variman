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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel label = new JPanel();
        label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
        JLabel tmp = new JLabel("RETS");
        label.add(tmp);
        label.add(Box.createRigidArea(new Dimension(5, 0)));
        JPanel horizontalRule = new JPanel();
        horizontalRule.setBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        horizontalRule.setLayout(new BoxLayout(horizontalRule, BoxLayout.Y_AXIS));
        horizontalRule.add(Box.createVerticalStrut(0));
        label.add(horizontalRule);

        add(label);
        Box labels = Box.createVerticalBox();
        Box values = Box.createVerticalBox();
        labels.add(new JLabel("Listening Port:"));
        values.add(new JTextField(10));
        labels.add(new JLabel("Metadata Directory:"));
        values.add(new JTextField("c:\\blah"));
        Box retsConfig = Box.createHorizontalBox();
        retsConfig.add(labels);
        retsConfig.add(values);
        add(retsConfig);

        add(Box.createVerticalGlue());
    }
}
