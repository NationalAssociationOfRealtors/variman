package org.realtors.rets.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GlobalTestSetup
{
    private static boolean sInitialized = false;

    public static void globalSetup()
    {
        if (sInitialized)
            return;

        ClassPathXmlApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("classpath*:/spring-appContext.xml");

        RetsServer retsServer = new RetsServer();
        retsServer.setApplicationContext(applicationContext);

        sInitialized = true;
    }
}
