/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

/**
 * An immutable implementation of the {@link SearchTransactionStatistics}
 * interface.
 * 
 * @author Danny
 */
public class ImmutableSearchTransactionStatistics implements SearchTransactionStatistics {
    
    // State Variables -------------------------------------------------------
    private int replyCode;
    private String replyText;
    private Integer count;
    private Integer dataCount;
    
    /**
     * Constructs a new ImmutableSearchTransactionStatistics.
     * 
     * @param replyCode The RETS reply code. Must be a valid RETS reply code.
     * @param replyText The RETS reply text.
     * @param count The count of matching results that meet the criteria. May be
     *         {@code null} which indicates that a count was not requested.
     * @param dataCount The count of the number of returned data rows. May be
     *         {@code null} which indicates that a data was not requested and
     *         therefore a count is not available.
     */
    public ImmutableSearchTransactionStatistics(int replyCode, String replyText, Integer count, Integer dataCount) {
        super();
        this.replyCode = replyCode;
        this.replyText = replyText;
        this.count = count;
        this.dataCount = dataCount;
    }
    
    /*- (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchTransactionStatistics#getCount()
     */
    public Integer getCount() {
        Integer count = this.count;
        return count;
    }
    
    /*- (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchTransactionStatistics#getDataCount()
     */
    public Integer getDataCount() {
        Integer dataCount = this.dataCount;
        return dataCount;
    }
    
    /*- (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchTransactionStatistics#getReplyCode()
     */
    public int getReplyCode() {
        int replyCode = this.replyCode;
        return replyCode;
    }
    
    /*- (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchTransactionStatistics#getReplyText()
     */
    public String getReplyText() {
        String replyText = this.replyText;
        return replyText;
    }
    
}
