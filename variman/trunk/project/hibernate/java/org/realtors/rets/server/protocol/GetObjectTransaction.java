/*
 */
package org.realtors.rets.server.protocol;

import java.io.File;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.ReplyCode;

import org.apache.commons.lang.math.NumberUtils;

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
            int objectId = NumberUtils.stringToInt((String) objectIdList.get(0));
            GetObjectPatternContext context =
                new GetObjectPatternContext(mParameters.getResource(),
                                            mParameters.getType(),
                                            mParameters.getResourceEntity(0),
                                            objectId);
            StringBuffer fileBuffer = new StringBuffer(mRootDirectory);
            fileBuffer.append(File.separator);
            mPatternFormatter.format(fileBuffer, context);
            String file = fileBuffer.toString();
            response.setContentType(getContentType(file));
            response.setHeader("MIME-Version", "1.0");
            FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
            }
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
            return "";
        }
    }

    private String mRootDirectory;
    private GetObjectPatternFormatter mPatternFormatter;
    private GetObjectParameters mParameters;
}
