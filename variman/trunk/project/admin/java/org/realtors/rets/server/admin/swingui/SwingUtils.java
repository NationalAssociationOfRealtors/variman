package org.realtors.rets.server.admin.swingui;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 13, 2004
 * Time: 12:06:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SwingUtils
{
    public static void centerOnFrame(Component component, Frame frame)
    {
        Point frameLocation = frame.getLocationOnScreen();
        Dimension frameSize = frame.getSize();
        int frameCenterX = frameLocation.x + (frameSize.width / 2);
        int frameCenterY = frameLocation.y + (frameSize.height / 2);
        centerRelativeTo(component, frameCenterX, frameCenterY);
    }

    public static void centerOnScreen(Component component)
    {
        Dimension screenSize = component.getToolkit().getScreenSize();
        int screenCenterX = screenSize.width / 2;
        int screenCenterY = screenSize.height / 2;
        centerRelativeTo(component, screenCenterX, screenCenterY);
    }

    public static void centerRelativeTo(Component component, int x, int y)
    {
        Dimension componentSize = component.getSize();
        component.setLocation((x - (componentSize.width / 2)),
                              (y - (componentSize.height / 2)));
    }
}
