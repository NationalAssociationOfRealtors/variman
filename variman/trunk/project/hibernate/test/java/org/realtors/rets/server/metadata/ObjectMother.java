/*
 */
package org.realtors.rets.server.metadata;

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
        resource.setSystem(createSystem());
        resource.setResourceID("Property");
        resource.updateLevel();
        return resource;
    }

    public static MClass createClass()
    {
        MClass clazz = new MClass();
        clazz.setResource(createResource());
        clazz.setClassName("RES");
        clazz.updateLevel();
        return clazz;
    }

    public static Table createTable()
    {
        Table table = new Table();
        table.setMClass(createClass());
        table.setSystemName("E_SCHOOL");
        table.updateLevel();
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

    public static ValidationExternalType createValidationExternlType()
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
}
