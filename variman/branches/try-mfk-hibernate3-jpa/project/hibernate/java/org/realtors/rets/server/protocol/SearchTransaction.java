/*
 * Variman RETS Server
 *
 * Author: Julie Szmyd, Mark Klein
 * Copyright (c) 2004 - 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;

import javax.persistence.EntityManagerFactory;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataManager;

public interface SearchTransaction
{
    public void setParameters(SearchParameters parameters)
        throws RetsServerException;
    
    public void setExecuteQuery(boolean executeQuery);

    public void execute(PrintWriter out, MetadataManager manager,
                        EntityManagerFactory emf)
        throws RetsServerException;
}
