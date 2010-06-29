package org.realtors.rets.server.admin.swingui;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class MacUtils
{
    public static void registerApplicationListeners()
    {
        Application application = Application.getApplication();
        application.addApplicationListener(new AdminApplicationHandler());
    }

    private static class AdminApplicationHandler extends ApplicationAdapter
    {
        public void handleQuit(ApplicationEvent event)
        {
            AdminFrame adminFrame = SwingUtils.getAdminFrame();
            if (adminFrame.quit())
            {
                event.setHandled(true);
            }
            else
            {
                event.setHandled(false);
            }
        }

        public void handleAbout(ApplicationEvent event)
        {
            event.setHandled(true);
            SwingUtils.getAdminFrame().showAboutBox();
        }
    }
}
