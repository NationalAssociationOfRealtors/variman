/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Splits a single compact metadata XML file into multiple files and
 * directories. Each metadata segment is in its own file, and directories are
 * created for each metadata level.
 */
public class CompactSplitter
{
    public CompactSplitter()
    {
        mFileMap = new HashMap();
        mFileMap.put("METADATA-SYSTEM", new SystemFile());
        mFileMap.put("METADATA-RESOURCE", new ResourceFile());
        mFileMap.put("METADATA-FOREIGN_KEYS", new ForeignKeysFile());
        mFileMap.put("METADATA-FOREIGNKEYS", new ForeignKeysFile());
        mFileMap.put("METADATA-CLASS", new ClassFile());
        mFileMap.put("METADATA-TABLE", new TableFile());
        mFileMap.put("METADATA-UPDATE", new UpdateFile());
        mFileMap.put("METADATA-UPDATE_TYPE", new UpdateTypeFile());
        mFileMap.put("METADATA-OBJECT", new ObjectFile());
        mFileMap.put("METADATA-SEARCH_HELP", new SearchHelpFile());
        mFileMap.put("METADATA-EDITMASK", new EditMaskFile());
        mFileMap.put("METADATA-LOOKUP", new LookupFile());
        mFileMap.put("METADATA-LOOKUP_TYPE", new LookupTypeFile());
        mFileMap.put("METADATA-UPDATE_HELP", new UpdateHelpFile());
        mFileMap.put("METADATA-VALIDATION_LOOKUP", new ValidationLookupFile());
        mFileMap.put("METADATA-VALIDATION_LOOKUP_TYPE",
                     new ValidationLookupTypeFile());
        mFileMap.put("METADATA-VALIDATION_EXPRESSION",
                     new ValidationExpressionFile());
        mFileMap.put("METADATA-VALIDATION_EXTERNAL",
                     new ValidationExternalFile());
        mFileMap.put("METADATA-VALIDATION_EXTERNAL_TYPE",
                     new ValidationExternalTypeFile());
    }

    public void splitCompactFile(String sourceFile, String baseDirectory)
        throws IOException, JDOMException
    {
        mSourceFile = sourceFile;
        mBaseDirectory = baseDirectory;
        SAXBuilder builder = new SAXBuilder();
        Document compactDocument = builder.build(new File(mSourceFile));
        XMLOutputter outputter = new XMLOutputter();

        List elements = compactDocument.getRootElement().getChildren();
        Document segmentDocument = new Document();
        for (int i = 0; i < elements.size(); i++)
        {
            Element element = (Element) elements.get(i);
            File file = getFile(element);
            if (file == null)
            {
                continue;
            }
            file.getParentFile().mkdirs();
            segmentDocument.setRootElement((Element) element.clone());
            outputter.output(segmentDocument, new FileWriter(file));
        }
    }

    /**
     * Maps a metadata element to a file.
     *
     * @param element A metadata element
     * @return the file that should be used to store this element
     */
    private File getFile(Element element)
    {
        String name = element.getName();
        ElementFile elementFile =
            (ElementFile) mFileMap.get(name);
        if (elementFile == null)
        {
            System.out.println("Skipping: " + name);
            return null;
        }
        System.out.println("Handling: " + name);
        return new File(mBaseDirectory, elementFile.getFile(element));
    }

    private String getResource(Element element)
    {
        return element.getAttributeValue(RESOURCE);
    }

    private String getMClass(Element element)
    {
        return element.getAttributeValue(CLASS);
    }

    private String getUpdate(Element element)
    {
        return element.getAttributeValue(UPDATE);
    }

    private String getLookup(Element element)
    {
        return element.getAttributeValue(LOOKUP);
    }

    private String getValidationLookup(Element element)
    {
        return element.getAttributeValue(VALIDATION_LOOKUP);
    }

    private String getValidationExternal(Element element)
    {
        return element.getAttributeValue(VALIDATION_EXTERNAL);
    }

    private interface ElementFile
    {
        public String getFile(Element element);
    }

    private class SystemFile implements ElementFile
    {
        public String getFile(Element element)
        {
            return "systemMetadata.xml";
        }
    }

    private class ResourceFile implements ElementFile
    {
        public String getFile(Element element)
        {
            return "resourceMetadata.xml";
        }
    }

    private class ForeignKeysFile implements ElementFile
    {
        public String getFile(Element element)
        {
            return "foreignKeys.xml";
        }
    }

    private class ClassFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return CLASS + FS + resource + XML;
        }
    }

    private class TableFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String mClass = getMClass(element);
            return CLASS + FS + TABLE + FS + mClass + XML;
        }
    }

    private class UpdateFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String mClass = getMClass(element);
            return CLASS + FS + UPDATE + FS + mClass + XML;
        }
    }

    private class UpdateTypeFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String update = getUpdate(element);
            return CLASS + FS + UPDATE + UPDATE_TYPE + FS + update + XML;
        }
    }

    private class ObjectFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return OBJECT + FS + resource + XML;
        }
    }

    private class SearchHelpFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return SEARCH_HELP + FS + resource + XML;
        }
    }

    private class EditMaskFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return EDITMASK + FS + resource + XML;
        }
    }

    private class LookupFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return LOOKUP + FS + resource + XML;
        }
    }

    private class LookupTypeFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String lookup = getLookup(element);
            return LOOKUP + FS + LOOKUP_TYPE + FS + lookup + XML;
        }
    }

    private class UpdateHelpFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return UPDATE_HELP + FS + resource + XML;
        }
    }

    private class ValidationLookupFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return VALIDATION_LOOKUP + FS + resource + XML;
        }
    }

    private class ValidationLookupTypeFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String validationLookup = getValidationLookup(element);
            return VALIDATION_LOOKUP + FS + VALIDATION_LOOKUP_TYPE + FS +
                validationLookup + XML;
        }
    }

    private class ValidationExpressionFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return VALIDATION_EXPRESSION + FS + resource + XML;
        }
    }

    private class ValidationExternalFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String resource = getResource(element);
            return VALIDATION_EXTERNAL + FS + resource + XML;
        }
    }

    private class ValidationExternalTypeFile implements ElementFile
    {
        public String getFile(Element element)
        {
            String validationExternal = getValidationExternal(element);
            return VALIDATION_EXTERNAL + FS + VALIDATION_EXTERNAL_TYPE + FS +
                validationExternal + XML;
        }
    }

    public static void main(String[] args)
    {
        try
        {
            CompactSplitter compactSplitter = new CompactSplitter();
            compactSplitter.splitCompactFile(args[0], args[1]);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    private static final String FS = "/";
    private static final String CLASS = "Class";
    private static final String RESOURCE = "Resource";
    private static final String TABLE = "Table";
    private static final String UPDATE = "Update";
    private static final String UPDATE_TYPE = "UpdateType";
    private static final String OBJECT = "Object";
    private static final String SEARCH_HELP = "SearchHelp";
    private static final String EDITMASK = "EditMask";
    private static final String LOOKUP = "Lookup";
    private static final String LOOKUP_TYPE = "LookupType";
    private static final String UPDATE_HELP = "UpdateHelp";
    private static final String VALIDATION_LOOKUP = "ValidationLookup";
    private static final String VALIDATION_LOOKUP_TYPE = "ValidatoinLookupType";
    private static final String VALIDATION_EXPRESSION = "ValidationExpression";
    private static final String VALIDATION_EXTERNAL = "ValidationExternal";
    private static final String VALIDATION_EXTERNAL_TYPE =
        "ValidationExternalType";
    private static final String XML = ".xml";

    private String mSourceFile;
    private String mBaseDirectory;
    private Map mFileMap;
}
