/* $Id: SearchTransactionStatisticsBuilder.java 47818 2009-05-22 22:53:58Z danny $ */
/* Copyright: Copyright (c) 2009 RealGo Inc. All rights reserved. */
package org.realtors.rets.server.protocol;

/**
 * A builder of {@link SearchTransactionStatistics} objects.
 * 
 * @author Danny
 */
public class SearchTransactionStatisticsBuilder {

    // State Variables -------------------------------------------------------
    private int replyCode;
    private String replyText;
    private Integer count;
    private Integer dataCount;
    
    /**
     * Sets the replyCode to the specified value.
     * 
     * @param replyCode The replyCode to set.
     */
    public void setReplyCode(int replyCode) {
        this.replyCode = replyCode;
    }
    
    /**
     * Sets the replyText to the specified value.
     * 
     * @param replyText The replyText to set.
     */
    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
    
    /**
     * Sets the count to the specified value.
     * 
     * @param count The count to set.
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    
    /**
     * Sets the dataCount to the specified value.
     * 
     * @param dataCount The dataCount to set.
     */
    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }
    
    /**
     * Builds a new {@link SearchTransactionStatistics} with the values set on
     * this builder.
     * 
     * @return A new {@link SearchTransactionStatistics}.
     */
    public SearchTransactionStatistics build() {
        SearchTransactionStatistics searchTransactionStatistics = new ImmutableSearchTransactionStatistics(this.replyCode, this.replyText, this.count, this.dataCount);
        return searchTransactionStatistics;
    }
    
}
