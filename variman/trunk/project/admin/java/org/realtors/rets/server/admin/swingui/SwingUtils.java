package org.realtors.rets.server.admin.swingui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

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

    public static AdminFrame getAdminFrame()
    {
        return sAdminFrame;
    }

    public static void setAdminFrame(AdminFrame adminFrame)
    {
        sAdminFrame = adminFrame;
    }

    private static SpringLayout.Constraints getConstraintsForComponent(
                                                        int row, 
                                                        int col,
                                                        Container parent,
                                                        int cols) 
    {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }
    
    public static void SpringLayoutGrid(    
                                Container parent,
                                int rows, 
                                int cols,
                                int initialX, 
                                int initialY,
                                int xPad, 
                                int yPad) 
    {
        SpringLayout layout;
        layout = (SpringLayout) parent.getLayout();
        
        Spring x = Spring.constant(initialX);
        for (int col = 0; col < cols; col++)
        {
            Spring width = Spring.constant(0);
            /*
             * Determine the maximum width for all components in this column.
             */
            for (int row = 0; row < rows; row++)
            {
                width = Spring.max(width, getConstraintsForComponent(row, col, parent, cols).getWidth());
            }
            /*
             * Set the width for each component in this column.
             */
            for (int row = 0; row < rows; row++)
            {
                SpringLayout.Constraints constraints = getConstraintsForComponent(row, col, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }
        
        Spring y = Spring.constant(initialY);
        for (int row = 0; row < rows; row++)
        {
            Spring height = Spring.constant(0);
            /*
             * Determine the maximum height for all components in this row.
             */
            for (int col = 0; col < cols; col++)
            {
                height = Spring.max(height, getConstraintsForComponent(row, col, parent, cols).getHeight());
            }
            /*
             * Set the height for each component in this row.
             */
            for (int col = 0; col < cols; col++)
            {
                SpringLayout.Constraints constraints = getConstraintsForComponent(row, col, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }
        /*
         * Finally, set the constraints for the parent container.
         */
        SpringLayout.Constraints parentConstraints = layout.getConstraints(parent);
        parentConstraints.setConstraint(SpringLayout.SOUTH, y);
        parentConstraints.setConstraint(SpringLayout.EAST, x);
    }

    private static AdminFrame sAdminFrame;
}
