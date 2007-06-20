/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class MdcFilter extends Filter
{
    public int decide(LoggingEvent event)
    {
        String value = (String) event.getMDC(mMdcName);
        if (mMdcValue.equals(value))
        {
            return Filter.ACCEPT;
        }
        else
        {
            return Filter.DENY;
        }
    }

    public void setMdcName(String mdcName)
    {
        mMdcName = mdcName;
    }

    public void setMdcValue(String mdcValue)
    {
        mMdcValue = mdcValue;
    }

    private String mMdcName;
    private String mMdcValue;
}
