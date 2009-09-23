package org.realtors.rets.server.protocol;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Mar 4, 2005
 * Time: 3:34:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectStream
{
    public InputStream getInputStream();

    public String getMimeType();
}
