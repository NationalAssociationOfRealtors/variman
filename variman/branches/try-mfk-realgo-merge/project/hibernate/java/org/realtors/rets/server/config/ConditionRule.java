/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

public interface ConditionRule
{
    public String getResourceID();

    public void setResourceID(String resourceId);

    public String getRetsClassName();

    public void setRetsClassName(String retsClassName);

    public String getSqlConstraint();

    public void setSqlConstraint(String sqlConstraint);

    public String getDmqlConstraint(); // TODO: Change this to return DmqlQuery

    public void setDmqlConstraint(String dmqlConstraint); // TODO: Change this to accept DmqlQuery
    
}
