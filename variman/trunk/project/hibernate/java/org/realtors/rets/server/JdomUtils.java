/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.List;

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
        for (int i = 0; i < documents.size(); i++)
        {
            Document document = (Document) documents.get(i);
            newRoot.addContent(document.detachRootElement());
        }
        return new Document(newRoot);
    }
}
