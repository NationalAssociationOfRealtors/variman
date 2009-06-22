/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jdom.Document;
import org.springframework.core.io.Resource;

/**
 * A Spring dependent version of an {@link XmlMetadataLoader} that add a new
 * constructor to use Spring's resource abstraction.
 *
 * @author Danny
 */
public class SpringXmlMetadataLoader extends XmlMetadataLoader
{
    public SpringXmlMetadataLoader(String metadataDir)
    {
        super(metadataDir);
    }
    
    public SpringXmlMetadataLoader(Document document)
    {
        super(document);
    }
    
    public SpringXmlMetadataLoader(InputStream inputStream)
    {
        super(inputStream);
    }
    
    public SpringXmlMetadataLoader(Resource resource) throws IOException
    {
        File metadataDirFile = resource.getFile();
        if (!metadataDirFile.isDirectory()) {
            throw new IllegalArgumentException("resource must represent a directory.");
        }
        String metadataDir = metadataDirFile.getAbsolutePath();
        setMetadataDir(metadataDir);
    }
}
