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
