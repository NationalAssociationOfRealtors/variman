/*
 */
package org.realtors.rets.server.metadata;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Visits a metadata segment based on the type of metadata in the data list.
 */
public abstract class MetadataSegmentVisitor
{
    public abstract void visitMSystem(MetadataSegment segment);

    public abstract void visitResource(MetadataSegment segment);

    public abstract void visitMClass(MetadataSegment segment);

    public abstract void visitTable(MetadataSegment segment);

    public abstract void visitUpdate(MetadataSegment segment);

    public abstract void visitUpdateType(MetadataSegment segment);

    public abstract void visitMObject(MetadataSegment segment);

    public abstract void visitSearchHelp(MetadataSegment segment);

    public abstract void visitEditMask(MetadataSegment segment);

    public abstract void visitLookup(MetadataSegment segment);

    public abstract void visitLookupType(MetadataSegment segment);

    public abstract void visitValidationLookup(MetadataSegment segment);

    public abstract void visitValidationLookupType(MetadataSegment segment);

    public abstract void visitValidationExternal(MetadataSegment segment);

    public abstract void visitValidationExternalType(MetadataSegment segment);

    public abstract void visitValidationExpression(MetadataSegment segment);

    public abstract void visitForeignKey(MetadataSegment segment);

    public void visitEmpty(MetadataSegment segment)
    {
    }

    public void dispatch(MetadataSegment segment)
    {
        try
        {
            List dataList = segment.getDataList();
            if (dataList.size() == 0)
            {
                visitEmpty(segment);
                return;
            }
            Class arrayType = dataList.get(0).getClass();
            Method method = getMethod(arrayType);
            method.invoke(this, new Object[] {segment});
        }
        catch (Exception e)
        {
            LOG.warn("Caught", e);
        }
    }

    private Method getMethod(Class clazz)
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

        return method;
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataSegmentVisitor.class);
}
