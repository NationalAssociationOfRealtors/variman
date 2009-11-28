/*
 */
package org.realtors.rets.server.metadata;

import java.util.Date;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

/**
 * Utilize ObjectMother test pattern.
 */
public class ObjectMother
{
    public static MSystem createSystem()
    {
        MSystem system = new MSystem();
        system.setDate(new Date());
        return system;
    }

    public static MResource createResource()
    {
        MResource resource = new MResource();
        MSystem system = createSystem();
        resource.setResourceID("Property");
        resource.setStandardName(ResourceStandardNameEnum.PROPERTY.toString());
        system.addChild(MetadataType.RESOURCE, resource);
        return resource;
    }

    public static MClass createClass()
    {
        MClass clazz = new MClass();
        MResource resource = createResource();
        clazz.setClassName("RES");
        clazz.setStandardName(ClassStandardNameEnum.RESIDENTIAL.toString());
        resource.addChild(MetadataType.CLASS, clazz);
        return clazz;
    }

    public static MTable createTable()
    {
        MTable table = new MTable();
        MClass clazz = createClass();
        table.setSystemName("E_SCHOOL");
        clazz.addChild(MetadataType.TABLE, table);
        return table;
    }

    public static MUpdate createUpdate()
    {
        MUpdate update = new MUpdate();
        MClass clazz = createClass();
        update.setUpdateName("Change");
        clazz.addChild(MetadataType.UPDATE, update);
        return update;
    }

    public static MUpdateHelp createUpdateHelp()
    {
        MUpdateHelp updateHelp = new MUpdateHelp();
        MResource resource = createResource();
        updateHelp.setUpdateHelpID("1");
        updateHelp.setValue("Enter the number in the following format");
        resource.addChild(MetadataType.UPDATE_HELP, updateHelp);
        return updateHelp;
    }

    public static MUpdateType createUpdateType()
    {
        MUpdateType updateType = new MUpdateType();
        MUpdate update = createUpdate();
        update.addChild(MetadataType.UPDATE_TYPE, updateType);
        return updateType;
    }

    public static MObject createMObject()
    {
        MObject object = new MObject();
        MResource resource = createResource();
        resource.addChild(MetadataType.OBJECT, object);
        return object;
    }

    public static MSearchHelp createSearchHelp()
    {
        MSearchHelp searchHelp = new MSearchHelp();
        MResource resource = createResource();
        resource.addChild(MetadataType.SEARCH_HELP, searchHelp);
        return searchHelp;
    }

    public static MEditMask createEditMask()
    {
        MEditMask editMask = new MEditMask();
        MResource resource = createResource();
        resource.addChild(MetadataType.EDITMASK, editMask);
        return editMask;
    }

    public static MLookup createLookup()
    {
        MLookup lookup = new MLookup();
        MResource resource = createResource();
        lookup.setLookupName("E_SCHOOL");
        resource.addChild(MetadataType.LOOKUP, lookup);
        return lookup;
    }

    public static MLookupType createLookupType()
    {
        MLookupType lookupType = new MLookupType();
        MLookup lookup = createLookup();
        lookupType.setValue("303");
        lookup.addChild(MetadataType.LOOKUP_TYPE, lookupType);
        return lookupType;
    }

    public static MValidationLookup createValidationLookup()
    {
        MValidationLookup validationLookup = new MValidationLookup();
        MResource resource = createResource();
        validationLookup.setValidationLookupName("School");
        resource.addChild(MetadataType.VALIDATION_LOOKUP, validationLookup);
        return validationLookup;
    }

    public static MValidationLookupType createValidationLookupType()
    {
        MValidationLookupType validationLookupType = new MValidationLookupType();
        MValidationLookup validationLookup = createValidationLookup();
        validationLookup.addChild(MetadataType.VALIDATION_LOOKUP_TYPE, validationLookupType);
        return validationLookupType;
    }

    public static MValidationExternal createValidationExternal()
    {
        MValidationExternal validationExternal = new MValidationExternal();
        MResource resource = createResource();
        validationExternal.setValidationExternalName("VET1");
        resource.addChild(MetadataType.VALIDATION_EXTERNAL, validationExternal);
        return validationExternal;
    }

    public static MValidationExternalType createValidationExternalType()
    {
        MValidationExternalType validationExternalType =
            new MValidationExternalType();
        MValidationExternal validationExternal = createValidationExternal();
        validationExternal.addChild(MetadataType.VALIDATION_EXTERNAL_TYPE, validationExternalType);
        return validationExternalType;
    }

    public static MValidationExpression createValidationExpression()
    {
        MValidationExpression validationExpression = new MValidationExpression();
        MResource resource = createResource();
        resource.addChild(MetadataType.VALIDATION_EXPRESSION, validationExpression);
        return validationExpression;
    }

    public static MForeignKey createForeignKey()
    {
        MForeignKey foreignKey = new MForeignKey();
        MSystem system = createSystem();
        system.addChild(MetadataType.FOREIGN_KEYS, foreignKey);
        return foreignKey;
    }
}
