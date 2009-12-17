/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

/**
 * Statistics about a search transaction.
 * 
 * @author Danny
 */
public interface SearchTransactionStatistics {

    /**
     * @return The RETS reply code returned to the client.
     */
    public int getReplyCode();
    
    /**
     * @return The RETS reply text returned to the client.
     */
    public String getReplyText();
    
    /**
     * @return The count returned to the client. May be {@code null} if the
     *         client did not request a count to be returned.
     */
    public Integer getCount();
    
    /**
     * @return The count of the data rows returned to the client. May be
     *         {@code null} if the client did not request data to be returned.
     */
    public Integer getDataCount();

}
