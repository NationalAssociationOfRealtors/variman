package org.realtors.rets.server.importer;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;

import org.realtors.rets.client.RetsSession;
import org.realtors.rets.client.MetadataTable;
import org.realtors.rets.client.Metadata;

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
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;
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
        parseMetadata(session);
        session.logout();
    }

    private void parseMetadata(RetsSession rSession)
        throws Exception
    {
        Session hSession = mSessions.openSession();
        Transaction tx = hSession.beginTransaction();

        MSystem hSystem = system(rSession, hSession);
        Set hResources = resource(rSession, hSession, hSystem);
        
        tx.commit();
        hSession.close();
    }

    private MSystem system(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tSystem =
            rSession.getMetadataTable(MetadataTable.SYSTEM);
        MSystem hSystem = new MSystem();

        hSession.save(hSystem);
        return hSystem;
    }

    private Set resource(RetsSession rSession, Session hSession,
                         MSystem hSystem)
        throws HibernateException,ParseException
    {
        MetadataTable tResource =
            rSession.getMetadataTable(MetadataTable.RESOURCE);

        Set hResources = new HashSet();
        List resources = tResource.getDataRows("");
        Iterator i = resources.iterator();
        while (i.hasNext())
        {
            Metadata md = (Metadata) i.next();
            Resource hResource = new Resource();

            hResource.setSystemid(hSystem);
            // do more stuff
            hResource.setResourceID(md.getAttribute("ResouceID"));
            hResource.setStandardName(ResourceStandardNameEnum.fromString(
                                          md.getAttribute("StandardName")));
            hResource.setVisibleName(md.getAttribute("VisibleName"));
            hResource.setDescription(md.getAttribute("Description"));
            hResource.setKeyField(md.getAttribute("KeyField"));
            hResource.setClassVersion(md.getAttribute("ClassVersion"));

            DateFormat dateFormat = DateFormat.getDateInstance();
            hResource.setClassDate(
                dateFormat.parse(md.getAttribute("ClassDate")));
                                     

            hSession.save(hResource);
            hResources.add(hResource);
        }

        hSystem.setResources(hResources);

        // change this
        return null;
    }

    public static final void main(String args[])
        throws Exception
    {
        MetadataImporter mi = new MetadataImporter();
        mi.doIt();
    }

    private SessionFactory mSessions;
    
    private static final String CVSID =
        "$Id: MetadataImporter.java,v 1.4 2003/06/23 21:52:02 kgarner Exp $";
}
