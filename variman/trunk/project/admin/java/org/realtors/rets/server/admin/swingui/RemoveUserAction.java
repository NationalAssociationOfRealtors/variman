package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 14, 2004
 * Time: 10:43:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveUserAction extends AbstractAction
{
    public RemoveUserAction()
    {
        super("Remove User...");
    }
    
    public void actionPerformed(ActionEvent event)
    {
        System.out.println("Removing user...");
    }
}
