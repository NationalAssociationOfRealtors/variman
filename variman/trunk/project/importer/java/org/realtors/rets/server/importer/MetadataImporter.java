package org.realtors.rets.server.importer;

import org.realtors.rets.client.RetsSession;

public class MetadataImporter
{
    public static final void main(String args[])
    {
        MetadataImporter mi = new MetadataImporter();
        mi.doIt();
    }

    public void doIt()
    {
        RetsSession session =
            new RetsSession("http://demo.crt.realtors.org:6103/login");
        session.login("Joe", "Schmoe");
    }
    
    private static final String CVSID = "$Id: MetadataImporter.java,v 1.1 2003/06/18 17:10:44 kgarner Exp $";
}
