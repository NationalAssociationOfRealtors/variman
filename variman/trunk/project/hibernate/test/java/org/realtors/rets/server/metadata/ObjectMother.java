/*
 */
package org.realtors.rets.server.metadata;

import java.util.HashSet;
import java.util.Set;

/**
 * Utilize ObjectMother test pattern.
 */
public class ObjectMother
{
    public static MSystem createSystem()
    {
        MSystem system = new MSystem();
        return system;
    }

    public static Resource createResource()
    {
        Resource resource = new Resource();
        MSystem system = createSystem();
        resource.setSystem(system);
        resource.setResourceID("Property");
        resource.updateLevel();

        Set resources = new HashSet();
        resources.add(resource);
        system.setResources(resources);
        return resource;
    }

    public static MClass createClass()
    {
        MClass clazz = new MClass();
        Resource resource = createResource();
        clazz.setResource(resource);
        clazz.setClassName("RES");
        clazz.updateLevel();

        Set classes = new HashSet();
        classes.add(clazz);
        resource.setClasses(classes);
        return clazz;
    }

    public static Table createTable()
    {
        Table table = new Table();
        MClass clazz = createClass();
        table.setMClass(clazz);
        table.setSystemName("E_SCHOOL");
        table.updateLevel();

        Set tables = new HashSet();
        tables.add(table);
        clazz.setTables(tables);
        return table;
    }

    public static Update createUpdate()
    {
        Update update = new Update();
        update.setMClass(createClass());
        update.setUpdateName("Change");
        update.updateLevel();
        return update;
    }

    public static UpdateType createUpdateType()
    {
        UpdateType updateType = new UpdateType();
        updateType.setUpdate(createUpdate());
        updateType.updateLevel();
        return updateType;
    }

    public static MObject createMObject()
    {
        MObject object = new MObject();
        object.setResource(createResource());
        return object;
    }

    public static SearchHelp createSearchHelp()
    {
        SearchHelp searchHelp = new SearchHelp();
        searchHelp.setResource(createResource());
        searchHelp.updateLevel();
        return searchHelp;
    }

    public static EditMask createEditMask()
    {
        EditMask editMask = new EditMask();
        editMask.setResource(createResource());
        editMask.updateLevel();
        return editMask;
    }

    public static Lookup createLookup()
    {
        Lookup lookup = new Lookup();
        lookup.setResource(createResource());
        lookup.setLookupName("E_SCHOOL");
        lookup.updateLevel();
        return lookup;
    }

    public static LookupType createLookupType()
    {
        LookupType lookupType = new LookupType();
        lookupType.setLookup(createLookup());
        lookupType.updateLevel();
        return lookupType;
    }

    public static ValidationLookup createValidationLookup()
    {
        ValidationLookup validationLookup = new ValidationLookup();
        validationLookup.setResource(createResource());
        validationLookup.setValidationLookupName("School");
        validationLookup.updateLevel();
        return validationLookup;
    }

    public static ValidationLookupType createValidationLookupType()
    {
        ValidationLookupType validationLookupType = new ValidationLookupType();
        validationLookupType.setValidationLookup(createValidationLookup());
        validationLookupType.updateLevel();
        return validationLookupType;
    }

    public static ValidationExternal createValidationExternal()
    {
        ValidationExternal validationExternal = new ValidationExternal();
        validationExternal.setResource(createResource());
        validationExternal.setValidationExternalName("VET1");
        validationExternal.updateLevel();
        return validationExternal;
    }

    public static ValidationExternalType createValidationExternalType()
    {
        ValidationExternalType validationExternalType =
            new ValidationExternalType();
        validationExternalType.setValidationExternal(
            createValidationExternal());
        validationExternalType.updateLevel();
        return validationExternalType;
    }

    public static ValidationExpression createValidationExpression()
    {
        ValidationExpression validationExpression = new ValidationExpression();
        validationExpression.setResource(createResource());
        validationExpression.updateLevel();
        return validationExpression;
    }

    public static ForeignKey createForeignKey()
    {
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setSystem(createSystem());
        return foreignKey;
    }
}
