/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

public class UserAgentUtils
{
    private UserAgentUtils() {
    	// prevents instantiation.
    }
    
    /**
     * Extracts the product name from the specified User-Agent header value.
     * Returns <code>null</code> if unable to extract the product name.
     * 
     * @param userAgentHeaderValue the value of the User-Agent header.
     * @return the product name if successfully extracted, otherwise
     *         <code>null</code>.
     */
    public static String extractProductName(final String userAgentHeaderValue)
    {
    	String productName = null;
    	if (userAgentHeaderValue == null) {
    		return productName;
    	}
    	
        final String[] productInfo = userAgentHeaderValue.split("/", 2);
        if (productInfo.length >= 1) {
            final String potentialProductName = productInfo[0].trim();
            if (potentialProductName.length() > 0) {
            	productName = potentialProductName;
            }
        }
		return productName;
    }
    
    /**
     * Extracts the optional product version from the specified User-Agent
     * header value. Returns an empty string if no product version was found.
     * Returns <code>null</code> if unable to extract the product version
     * because of an invalid User-Agent header value, for example if
     * <code>userAgentHeaderValue</code> is <code>null</code>.
     * <p>
     * A User-Agent header value of the form <code>/</code> or
     * <code>/&lt;product-version&gt;</code> (that is, the product name is
     * empty) will still return an empty string or the product version,
     * respectively. Use {@link #extractProductName(String)} to test if a
     * valid product name exists in the header value.</p>
     * 
     * @param userAgentHeaderValue the value of the User-Agent header.
     * @return the product version if successfully extracted, otherwise an
     *         empty string.
     */
    public static String extractProductVersion(final String userAgentHeaderValue)
    {
    	String productVersion = null;
    	if (userAgentHeaderValue == null) {
    		return productVersion;
    	}

        final String[] productInfo = userAgentHeaderValue.split("/", 2);
        if (productInfo.length == 1) { // There were no slashes
        	productVersion = "";
        } else if (productInfo.length == 2) {
            productVersion = productInfo[1].trim();
        }
		return productVersion;
    }
        
}
