package org.realtors.rets.server.testing;

public class DataGenerator
{
    public DataGenerator()
    {
        initHibernate();
    }
    
    private void initHibernate()
        throws MappingException, HibernateException
    {
        Configuration cfg = new Configuration();
        cfg.addJar("retsdb2-hbm-xml.jar");
        mSessions = cfg.buildSessionFactory();
    }
}
