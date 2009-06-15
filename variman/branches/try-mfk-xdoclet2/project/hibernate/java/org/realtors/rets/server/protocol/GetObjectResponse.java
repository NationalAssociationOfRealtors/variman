/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.OutputStream;
import java.io.IOException;

public interface GetObjectResponse
{
    public OutputStream getOutputStream() throws IOException;

    public void setContentType(String contentType);

    public void setHeader(String name, String value);
}
