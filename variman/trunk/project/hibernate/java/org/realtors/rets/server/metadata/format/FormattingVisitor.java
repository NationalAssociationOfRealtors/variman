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
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationExpression;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

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
        formatter.setResourceName(segment.getLevels()[0]);
        formatter.format(mOut, classes);
    }

    public void visitTable(MetadataSegment segment)
    {
        Table[] tables = (Table[]) segment.getData();
        String[] levels = segment.getLevels();
        TableFormatter formatter = TableFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.format(mOut, tables);
    }

    public void visitUpdate(MetadataSegment segment)
    {
        Update[] updates = (Update[]) segment.getData();
        String[] levels = segment.getLevels();
        UpdateFormatter formatter = UpdateFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.format(mOut, updates);
    }

    public void visitUpdateType(MetadataSegment segment)
    {
        UpdateType[] updateTypes = (UpdateType[]) segment.getData();
        String[] levels = segment.getLevels();
        UpdateTypeFormatter formatter =
            UpdateTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setClassName(levels[1]);
        formatter.setUpdateName(levels[2]);
        formatter.format(mOut, updateTypes);
    }

    public void visitMObject(MetadataSegment segment)
    {
        MObject[] objects = (MObject[]) segment.getData();
        String[] levels = segment.getLevels();
        ObjectFormatter formatter = ObjectFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, objects);
    }

    public void visitSearchHelp(MetadataSegment segment)
    {
        SearchHelp[] searchHelps = (SearchHelp[]) segment.getData();
        String[] levels = segment.getLevels();
        SearchHelpFormatter formatter =
            SearchHelpFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, searchHelps);
    }

    public void visitEditMask(MetadataSegment segment)
    {
        EditMask[] editMasks = (EditMask[]) segment.getData();
        String[] levels = segment.getLevels();
        EditMaskFormatter formatter = EditMaskFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, editMasks);
    }

    public void visitLookup(MetadataSegment segment)
    {
        Lookup[] lookup = (Lookup[]) segment.getData();
        String[] levels = segment.getLevels();
        LookupFormatter formatter = LookupFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, lookup);
    }

    public void visitLookupType(MetadataSegment segment)
    {
        LookupType[] lookupTypes = (LookupType[]) segment.getData();
        String[] levels = segment.getLevels();
        LookupTypeFormatter formatter =
            LookupTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setLookupName(levels[0]);
        formatter.format(mOut, lookupTypes);
    }

    public void visitValidationLookup(MetadataSegment segment)
    {
        ValidationLookup[] validationLookups =
            (ValidationLookup[]) segment.getData();
        String[] levels = segment.getLevels();
        ValidationLookupFormatter formatter =
            ValidationLookupFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, validationLookups);
    }

    public void visitValidationLookupType(MetadataSegment segment)
    {
        ValidationLookupType[] validationLookupTypes =
            (ValidationLookupType[]) segment.getData();
        String[] levels = segment.getLevels();
        ValidationLookupTypeFormatter formatter =
            ValidationLookupTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setValidationLookupName(levels[1]);
        formatter.format(mOut, validationLookupTypes);
    }

    public void visitValidationExternal(MetadataSegment segment)
    {
        ValidationExternal[] validationExternals =
            (ValidationExternal[]) segment.getData();
        String[] levels = segment.getLevels();
        ValidationExternalFormatter formatter =
            ValidationExternalFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, validationExternals);
    }

    public void visitValidationExternalType(MetadataSegment segment)
    {
        ValidationExternalType[] validationExternalTypes =
            (ValidationExternalType[]) segment.getData();
        String[] levels = segment.getLevels();
        ValidationExternalTypeFormatter formatter =
            ValidationExternalTypeFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.setValidationExternalName(levels[1]);
        formatter.format(mOut, validationExternalTypes);
    }

    public void visitValidationExpression(MetadataSegment segment)
    {
        ValidationExpression[] validationExpressions =
            (ValidationExpression[]) segment.getData();
        String[] levels = segment.getLevels();
        ValidationExpressionFormatter formatter =
            ValidationExpressionFormatter.getInstance(mFormat);
        formatter.setVersion(segment.getVersion(), segment.getDate());
        formatter.setResourceName(levels[0]);
        formatter.format(mOut, validationExpressions);
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
