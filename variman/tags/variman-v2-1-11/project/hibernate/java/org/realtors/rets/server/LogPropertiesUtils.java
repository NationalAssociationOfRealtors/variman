package org.realtors.rets.server;

import java.util.Properties;

public class LogPropertiesUtils implements LogPropertiesConstants
{
    public static Properties createDefaultLoggingProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(LOG_APPENDER,  LOG_APPENDER_SIZE);
        properties.setProperty(LOG_LEVEL, LOG_LEVEL_NORMAL);
        properties.setProperty(SQL_LOG_LEVEL, SQL_LEVEL_DISABLED);
        return properties;
   }
}
