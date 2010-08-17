/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.config;

import java.util.EventListener;

/**
 * An event listener which can listen when the RETS configuration changes.
 * 
 * @author Danny
 */
public interface RetsConfigChangedListener extends EventListener {

    /**
     * Called when the RETS configuration has changed.
     */
    public void retsConfigChanged();

}
