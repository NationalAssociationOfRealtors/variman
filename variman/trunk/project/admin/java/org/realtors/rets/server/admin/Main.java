/*
 */
package org.realtors.rets.server.admin;

import org.wxwindows.wxApp;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSize;

public class Main extends wxApp
{
    public boolean OnInit()
    {
        wxInitAllImageHandlers();
        AdminFrame frame = new AdminFrame("Rex Administration",
                                    new wxPoint(50, 50), new wxSize(640, 480));
        frame.Show(true);
        frame.initDatabase();
        return true;
    }

    public static void main(String[] args)
    {
        Main main = new Main();
        main.MainLoop();
    }
}
