/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.util.Iterator;

/**
 * @author kgarner
 */
public interface ValidationResults
{
    public ValidationResult getResultByName(String name);
    public void addResult(ValidationResult result);
    public Iterator iterator();
}