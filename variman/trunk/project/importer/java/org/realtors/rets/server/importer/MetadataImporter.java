package org.realtors.rets.server.importer;

import java.util.Enumeration;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.HibernateException;

import org.realtors.rets.client.RetsSession;
import org.realtors.rets.client.MetadataTable;

import org.realtors.rets.server.data.RETSData;
import org.realtors.rets.server.data.RETSDataElement;
import org.realtors.rets.server.data.RETSHistoryData;
import org.realtors.rets.server.data.RETSHistoryDataElement;
import org.realtors.rets.server.data.RETSHistoryMultiSet;
import org.realtors.rets.server.data.RETSMultiSet;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;


public class MetadataImporter
{

    /**
     * Creates a new <code>MetadataImporter</code> instance.
     *
     */
    public MetadataImporter()
        throws Exception
    {
        initHibernate();
    }

    private void initHibernate()
        throws MappingException, HibernateException
    {
        Configuration cfg = new Configuration();
        cfg.addClass(RETSData.class);
        cfg.addClass(RETSDataElement.class);
        cfg.addClass(RETSHistoryData.class);
        cfg.addClass(RETSHistoryDataElement.class);
        cfg.addClass(RETSHistoryMultiSet.class);
        cfg.addClass(RETSMultiSet.class);
        cfg.addClass(MClass.class);
        cfg.addClass(EditMask.class);
        cfg.addClass(ForeignKey.class);
        cfg.addClass(Lookup.class);
        cfg.addClass(LookupType.class);
        cfg.addClass(MObject.class);
        cfg.addClass(Resource.class);
        cfg.addClass(SearchHelp.class);
        cfg.addClass(MSystem.class);
        cfg.addClass(Table.class);
        cfg.addClass(Update.class);
        cfg.addClass(UpdateHelp.class);
        cfg.addClass(UpdateType.class);
        cfg.addClass(ValidationExpression.class);
        cfg.addClass(ValidationExternal.class);
        cfg.addClass(ValidationExternalType.class);
        cfg.addClass(ValidationLookup.class);
        cfg.addClass(ValidationLookupType.class);
        mSessions = cfg.buildSessionFactory();
    }
    
    public void doIt()
        throws Exception
    {
        RetsSession session =
            new RetsSession("http://demo.crt.realtors.org:6103/login");
        session.login("Joe", "Schmoe");
        MetadataTable mt = session.getSystemMetadata();
        session.logout();
        parseMetadata(mt);
    }

    private void parseMetadata(MetadataTable mt)
    {
        System.out.println(mt);
    }
       

    public static final void main(String args[])
        throws Exception
    {
        MetadataImporter mi = new MetadataImporter();
        mi.doIt();
    }

    private SessionFactory mSessions;
    
    private static final String CVSID =
        "$Id: MetadataImporter.java,v 1.2 2003/06/19 21:54:06 kgarner Exp $";
}
