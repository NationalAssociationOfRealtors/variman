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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;

public class GetObjectTransaction
{
    public GetObjectTransaction(GetObjectParameters parameters)
    {
        mParameters = parameters;
        mPatternContext = new GetObjectPatternContext();
        mBoundaryGenerator = DEFAULT_BOUNDARY_GENERATOR;
        mBaseLocationUrl = "http://images.invalid/";
        mBlockLocation = false;
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

    public void setBoundaryGenerator(
        MultipartBoundaryGenerator boundaryGenerator)
    {
        mBoundaryGenerator = boundaryGenerator;
    }

    public void execute(GetObjectResponse response)
        throws RetsServerException
    {
        try
        {
            List allFiles = findAllFileDescriptors();
            if (allFiles.size() == 0)
            {
                throw new RetsReplyException(ReplyCode.NO_OBJECT_FOUND);
            }
            else if (!mParameters.isMultipartId())
            {
                FileDescriptor fileDescriptor =
                    (FileDescriptor) allFiles.get(0);
                executeSinglePart(response, fileDescriptor);
            }
            else
            {
                executeMultipart(response, allFiles);
            }
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    private void executeSinglePart(GetObjectResponse response,
                                   FileDescriptor fileDescriptor)
        throws IOException
    {
        String filePath = fileDescriptor.file.getPath();
        LOG.info("GetObject file path: " + filePath);
        response.setContentType(getContentType(filePath));
        response.setHeader("MIME-Version", "1.0");
        response.setHeader("Content-ID", fileDescriptor.objectKey);
        response.setHeader("Object-ID", "" + fileDescriptor.objectId);
        if (useLocation())
        {
            response.setHeader("Location", getLocationUrl(fileDescriptor));
        }
        else
        {
            IOUtils.copyStream(new FileInputStream(fileDescriptor.file),
                               response.getOutputStream());
        }
    }

    private void executeMultipart(GetObjectResponse response, List allFiles)
        throws IOException
    {
        String boundary = mBoundaryGenerator.generateBoundary();
        LOG.debug("Multipart boundary: " + boundary);
        response.setContentType("multipart/parallel; " +
                                "boundary=\"" + boundary +"\"");
        response.setHeader("MIME-Version", "1.0");
        DataOutputStream out = new DataOutputStream(response.getOutputStream());

        for (int i = 0; i < allFiles.size(); i++)
        {
            FileDescriptor fileDescriptor = (FileDescriptor) allFiles.get(i);
            String filePath = fileDescriptor.file.getPath();
            LOG.info("GetObject (mulitpart) file path: " + filePath);
            out.writeBytes(CRLF + "--" + boundary + CRLF);
            out.writeBytes("Content-Type: " + getContentType(filePath) + CRLF);
            out.writeBytes("Content-ID: " + fileDescriptor.objectKey + CRLF);
            out.writeBytes("Object-ID: " + fileDescriptor.objectId + CRLF);
            if (useLocation())
            {
                out.writeBytes("Location: " + getLocationUrl(fileDescriptor) +
                               CRLF);
                out.writeBytes(CRLF);
            }
            else
            {
                out.writeBytes(CRLF);
                IOUtils.copyStream(new FileInputStream(fileDescriptor.file),
                                   out);
            }
        }
        out.writeBytes(CRLF + "--" + boundary + "--" + CRLF);
        out.close();
    }

    public String getContentType(String file)
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

    private boolean useLocation()
    {
        return (!mBlockLocation) && mParameters.getUseLocation();
    }

    private String getLocationUrl(FileDescriptor fileDescriptor)
    {
        StringBuffer location = new StringBuffer();
        location.append(mBaseLocationUrl);
        location.append(mParameters.getResource()).append("/");
        location.append(mParameters.getType()).append("/");
        location.append(fileDescriptor.objectKey).append("/");
        location.append(fileDescriptor.objectId);
        return location.toString();
    }

    public List /* File */ findAllFileObjects()
    {
        List allFiles = findAllFileDescriptors();
        List fileObjects = new ArrayList(allFiles.size());
        for (int i = 0; i < allFiles.size(); i++)
        {
            FileDescriptor descriptor = (FileDescriptor) allFiles.get(i);
            fileObjects.add(descriptor.file);
        }
        return fileObjects;
    }

    private List /* FileDescriptor */ findAllFileDescriptors()
    {
        List files = new ArrayList();
        int numberOfResources = mParameters.numberOfResources();
        for (int i = 0; i < numberOfResources; i++)
        {
            String resourceEntity = mParameters.getResourceEntity(i);
            List objectIdList = mParameters.getObjectIdList(i);
            for (int j = 0; j < objectIdList.size(); j++)
            {
                String objectIdString = (String) objectIdList.get(j);
                if (objectIdString.equals("*"))
                {
                    scanForFiles(resourceEntity, files);
                }
                else
                {
                    int objectId = NumberUtils.stringToInt(objectIdString);
                    if (objectId == 0)
                    {
                        objectId = 1;
                    }
                    addFileIfExists(resourceEntity, objectId, files);
                }
            }
        }
        return files;
    }

    private void scanForFiles(String resourceEntity, List files)
    {
        int objectId = 1;
        while (addFileIfExists(resourceEntity, objectId, files))
        {
            objectId++;
        }
    }
    
    private boolean addFileIfExists(String resourceEntity, int objectId,
                                    List files)
    {
        mPatternContext.setKey(resourceEntity);
        mPatternContext.setObjectId(objectId);
        StringBuffer fileBuffer = new StringBuffer(mRootDirectory);
        fileBuffer.append(File.separator);
        mPatternFormatter.format(fileBuffer, mPatternContext);
        String filePath = fileBuffer.toString();
        File file = new File(filePath);
        if (file.exists() && file.isFile())
        {
            LOG.debug("File " + filePath + " exists");
            files.add(new FileDescriptor(resourceEntity, objectId,
                                         file));
            return true;
        }
        else
        {
            LOG.debug("File " + filePath + " does not exist");
            return false;
        }
    }

    public void setBaseLocationUrl(String baseLocationUrl)
    {
        mBaseLocationUrl = baseLocationUrl;
    }

    public void setBlockLocation(boolean blockLocation)
    {
        mBlockLocation = blockLocation;
    }

    private static class FileDescriptor
    {
        public FileDescriptor(String objectKey, int objectId, File file)
        {
            this.objectKey = objectKey;
            this.objectId = objectId;
            this.file = file;
        }

        public String objectKey;
        public int objectId;
        public File file;
    }

    private static class BoundaryGenerator implements MultipartBoundaryGenerator
    {
        public String generateBoundary()
        {
            return DigestUtils.shaHex("" + System.currentTimeMillis());
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GetObjectTransaction.class);
    private static final String CRLF = "\r\n";
    public static final MultipartBoundaryGenerator DEFAULT_BOUNDARY_GENERATOR =
        new BoundaryGenerator();
    private String mRootDirectory;
    private GetObjectPatternFormatter mPatternFormatter;
    private GetObjectPatternContext mPatternContext;
    private GetObjectParameters mParameters;
    private MultipartBoundaryGenerator mBoundaryGenerator;
    private String mBaseLocationUrl;
    private boolean mBlockLocation;
}
