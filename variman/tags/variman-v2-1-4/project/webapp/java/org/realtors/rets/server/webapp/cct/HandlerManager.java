/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp.cct;

import java.util.Map;
import java.util.HashMap;

public class HandlerManager
{
    private static HandlerManager mInstance;

    public synchronized static HandlerManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new HandlerManager();
        }
        return mInstance;
    }

    private HandlerManager()
    {
        mHandlers = new HashMap();
    }

    public RetsHandlers getHandlers(String context)
    {
        synchronized (this)
        {
            RetsHandlers handlers = (RetsHandlers) mHandlers.get(context);
            if (handlers == null)
            {
                handlers = new RetsHandlers();
                mHandlers.put(context, handlers);
            }
            return handlers;
        }
    }

    private Map mHandlers;
}

