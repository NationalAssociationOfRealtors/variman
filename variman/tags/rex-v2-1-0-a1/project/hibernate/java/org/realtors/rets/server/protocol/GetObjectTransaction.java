/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class GetObjectTransaction
{
    public GetObjectTransaction(GetObjectParameters parameters)
    {
        mParameters = parameters;
    }

    public void setRootDirectory(String rootDirectory)
    {
        mRootDirectory = rootDirectory;
    }

    public void setPattern(String pattern)
    {
        GetObjectPatternParser parser = new GetObjectPatternParser(pattern);
        mPatternFormatter = parser.parse();
    }

    public void execute(GetObjectResponse response)
        throws RetsServerException
    {
        try
        {
            List objectIdList = mParameters.getObjectIdList(0);
            int objectId =
                NumberUtils.stringToInt((String) objectIdList.get(0));
            GetObjectPatternContext context =
                new GetObjectPatternContext(mParameters.getResource(),
                                            mParameters.getType(),
                                            mParameters.getResourceEntity(0),
                                            objectId);
            StringBuffer fileBuffer = new StringBuffer(mRootDirectory);
            fileBuffer.append(File.separator);
            mPatternFormatter.format(fileBuffer, context);
            String file = fileBuffer.toString();
            LOG.info("GetObject file: " + file);
            response.setContentType(getContentType(file));
            response.setHeader("MIME-Version", "1.0");
            response.setHeader("Content-ID", "12345");
            response.setHeader("Object-ID", "" + objectId);
            IOUtils.copyStream(new FileInputStream(file),
                               response.getOutputStream());
        }
        catch (FileNotFoundException e)
        {
            throw new RetsReplyException(ReplyCode.NO_OBJECT_FOUND);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    private String getContentType(String file)
    {
        if (file.endsWith(".gif"))
        {
            return "image/gif";
        }
        else if (file.endsWith(".jpg"))
        {
            return "image/jpeg";
        }
        else
        {
            // Todo: Throw an HTTP exception?
            LOG.warn("Unknown file type: " + file);
            return "";
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GetObjectTransaction.class);
    private String mRootDirectory;
    private GetObjectPatternFormatter mPatternFormatter;
    private GetObjectParameters mParameters;
}
