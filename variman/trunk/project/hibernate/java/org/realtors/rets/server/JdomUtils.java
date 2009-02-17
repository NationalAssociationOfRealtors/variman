/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.List;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

public class JdomUtils
{
    /**
     * Merge multiple documents into a single document. The root element from
     * each document is detached and added to a new document, under a new root
     * element. Since they are detached, the original documents will be empty.
     *
     * @param documents list of Document objects
     * @param newRoot Root element in the new, merged document
     * @return a new Document
     */
    public static Document mergeDocuments(List documents, Element newRoot)
    {
    	DocType docType = null;
    	Document merged = null;
    	
        for (int i = 0; i < documents.size(); i++)
        {
            Document document = (Document) documents.get(i);
            
            DocType checkDocType = document.getDocType();
            if (checkDocType != null && checkDocType.getElementName().equals("METADATA-SYSTEM"))
            {
            	docType = (DocType)checkDocType.clone();
            }
            
            newRoot.addContent(document.detachRootElement());
        }
        
        merged = new Document(newRoot);
        if (docType != null)
        	merged.setDocType(docType);
        
        return merged;
    }
}
