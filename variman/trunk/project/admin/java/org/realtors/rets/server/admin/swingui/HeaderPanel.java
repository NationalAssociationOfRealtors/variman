package org.realtors.rets.server.admin.swingui;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 10, 2004
 * Time: 11:59:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class HeaderPanel extends JPanel
{
    public HeaderPanel(String title)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new JLabel(title));
        add(Box.createRigidArea(new Dimension(5, 0)));

        JPanel horizontalRule = new JPanel();
        horizontalRule.setBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        // This trick creates a zero height panel.  The resulting output is
        // just the top and botom borders butt right up against each other
        // forming a solid horizontal line.
        horizontalRule.setLayout(new BoxLayout(horizontalRule,
                                               BoxLayout.Y_AXIS));
        horizontalRule.add(Box.createVerticalStrut(0));

        add(horizontalRule);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
