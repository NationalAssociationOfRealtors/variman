/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.MetadataSegmentVisitor;
import org.realtors.rets.server.metadata.ServerMetadata;

import org.apache.log4j.Logger;

public class FormattingVisitor extends MetadataSegmentVisitor
{
    public FormattingVisitor(PrintWriter out, int format)
    {
        mOut = out;
        mFormat = format;
    }

    public void visitMSystem(MetadataSegment segment)
    {
        SystemFormatter formatter = SystemFormatter.getInstance(mFormat);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitResource(MetadataSegment segment)
    {
        ResourceFormatter formatter = ResourceFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.format(mOut, segment.getDataList());
    }

    public void visitMClass(MetadataSegment segment)
    {
        ClassFormatter formatter = ClassFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(segment.getLevels()[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitTable(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        TableFormatter formatter = TableFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitUpdate(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        UpdateFormatter formatter = UpdateFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitUpdateType(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        UpdateTypeFormatter formatter =
            UpdateTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.setUpdateName(levels[2]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitMObject(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ObjectFormatter formatter = ObjectFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitSearchHelp(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        SearchHelpFormatter formatter =
            SearchHelpFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitEditMask(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        EditMaskFormatter formatter = EditMaskFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitLookup(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        LookupFormatter formatter = LookupFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitLookupType(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        LookupTypeFormatter formatter =
            LookupTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setLookupName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitValidationLookup(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ValidationLookupFormatter formatter =
            ValidationLookupFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitValidationLookupType(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ValidationLookupTypeFormatter formatter =
            ValidationLookupTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setValidationLookupName(levels[1]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitValidationExternal(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ValidationExternalFormatter formatter =
            ValidationExternalFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitValidationExternalType(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ValidationExternalTypeFormatter formatter =
            ValidationExternalTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setValidationExternalName(levels[1]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitValidationExpression(MetadataSegment segment)
    {
        String[] levels = segment.getLevels();
        ValidationExpressionFormatter formatter =
            ValidationExpressionFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, segment.getDataList());
    }

    public void visitForeignKey(MetadataSegment segment)
    {
    }

    public void visitServerMetadata(MetadataSegment segment)
    {
        ServerMetadata[] data = segment.getData();
        LOG.warn("Cannot handle: " + data.getClass().getName());
    }

    public void visitObject(Object object)
    {
        LOG.warn("Should not get here: " + object);
    }

    public void visit(MetadataSegment segment)
    {
        try
        {
            ServerMetadata[] data = segment.getData();
            Class arrayType = data.getClass().getComponentType();
            Method method = getMethod(arrayType);
            method.invoke(this, new Object[] {segment});
        }
        catch (Exception e)
        {
            LOG.warn("Caught", e);
        }
    }

    private Method getMethod(Class clazz)
        throws NoSuchMethodException
    {
        Method method = null;

        // Try all super classes
        Class superClass = clazz;
        while ((method == null) && (superClass != Object.class))
        {
            try
            {
                String className = superClass.getName();
                className = className.substring(className.lastIndexOf('.') + 1);
                String name = "visit" + className;
                LOG.info("Checking dispatch to: " + name);
                method = getClass().getMethod(
                    name, new Class[]{MetadataSegment.class});
            }
            catch (NoSuchMethodException e)
            {
                superClass = superClass.getSuperclass();
            }
        }

        // Default to visit(Object), bubbling up the exception which shouldn't
        // happen
        if (method == null)
        {
            LOG.info("Dispatching to visitObject()");
            method = getClass().getMethod("visitObject",
                                          new Class[]{Object.class});
        }

        return method;
    }

    private static final Logger LOG =
        Logger.getLogger(FormattingVisitor.class);
    protected int mFormat;
    protected PrintWriter mOut;
}
