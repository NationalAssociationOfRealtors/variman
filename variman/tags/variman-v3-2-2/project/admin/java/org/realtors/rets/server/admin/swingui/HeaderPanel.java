package org.realtors.rets.server.admin.swingui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class HeaderPanel extends JPanel
{
    public HeaderPanel(String title)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new JLabel(title));
        add(Box.createRigidArea(new Dimension(5, 0)));

        JPanel horizontalRule = new JPanel();
        horizontalRule.setLayout(new BoxLayout(horizontalRule,
                                               BoxLayout.Y_AXIS));
        horizontalRule.add(Box.createVerticalGlue());
        horizontalRule.add(new JSeparator());
        add(horizontalRule);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
