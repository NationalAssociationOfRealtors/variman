/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ServerMetadata;

import org.apache.log4j.Logger;

public class FormattingVisitor
{
    public FormattingVisitor(PrintWriter out, int format)
    {
        mOut = out;
        mFormat = format;
    }

    public void visitMSystem(MetadataSegment segment)
    {
        MSystem[] system = (MSystem[]) segment.getData();
        SystemFormatter formatter = SystemFormatter.getInstance(mFormat);
        formatter.format(mOut, system[0]);
    }

    public void visitResource(MetadataSegment segment)
    {
        Resource[] resources = (Resource[]) segment.getData();
        ResourceFormatter formatter = ResourceFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.format(mOut, resources);
    }

    public void visitMClass(MetadataSegment segment)
    {
        MClass[] classes = (MClass[]) segment.getData();
        ClassFormatter formatter = ClassFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResource(segment.getLevels()[0]);
        formatter.format(mOut, classes);
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
//        Class superClass = clazz;
//        while ((method == null) && (superClass != Object.class))
//        {
            try
            {
                String className = clazz.getName();
                className = className.substring(className.lastIndexOf('.') + 1);
                String name = "visit" + className;
                LOG.info("Checking dispatch to: " + name);
                method = getClass().getMethod(
                    name, new Class[]{MetadataSegment.class});
            }
            catch (NoSuchMethodException e)
            {
//                superClass = superClass.getSuperclass();
            }
//        }

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
