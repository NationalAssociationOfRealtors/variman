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
        resource.setSystemid(createSystem());
        resource.setResourceID("Property");
        resource.updateLevel();
        return resource;
    }

    public static MClass createClass()
    {
        MClass clazz = new MClass();
        clazz.setResourceid(createResource());
        clazz.setClassName("RES");
        clazz.updateLevel();
        return clazz;
    }

    public static Table createTable()
    {
        Table table = new Table();
        table.setClassid(createClass());
        table.setSystemName("E_SCHOOL");
        table.updateLevel();
        return table;
    }

    public static Update createUpdate()
    {
        Update update = new Update();
        update.setClassid(createClass());
        update.setUpdateName("Change");
        update.updateLevel();
        return update;
    }

    public static UpdateType createUpdateType()
    {
        UpdateType updateType = new UpdateType();
        updateType.setUpdateid(createUpdate());
        updateType.updateLevel();
        return updateType;
    }

    public static SearchHelp createSearchHelp()
    {
        SearchHelp searchHelp = new SearchHelp();
        searchHelp.setResourceid(createResource());
        searchHelp.updateLevel();
        return searchHelp;
    }

    public static EditMask createEditMask()
    {
        EditMask editMask = new EditMask();
        editMask.setResourceid(createResource());
        editMask.updateLevel();
        return editMask;
    }

    public static Lookup createLookup()
    {
        Lookup lookup = new Lookup();
        lookup.setResourceid(createResource());
        lookup.setLookupName("E_SCHOOL");
        lookup.updateLevel();
        return lookup;
    }

    public static LookupType createLookupType()
    {
        LookupType lookupType = new LookupType();
        lookupType.setLookupid(createLookup());
        lookupType.updateLevel();
        return lookupType;
    }

    public static ValidationLookup createValidationLookup()
    {
        ValidationLookup validationLookup = new ValidationLookup();
        validationLookup.setResourceid(createResource());
        validationLookup.setValidationLookupName("School");
        validationLookup.updateLevel();
        return validationLookup;
    }

    public static ValidationLookupType createValidationLookupType()
    {
        ValidationLookupType validationLookupType = new ValidationLookupType();
        validationLookupType.setValidationLookupID(createValidationLookup());
        validationLookupType.updateLevel();
        return validationLookupType;
    }

    public static ValidationExternal createValidationExternal()
    {
        ValidationExternal validationExternal = new ValidationExternal();
        validationExternal.setResourceid(createResource());
        validationExternal.setValidationExternalName("VET1");
        validationExternal.updateLevel();
        return validationExternal;
    }

    public static ValidationExternalType createValidationExternlType()
    {
        ValidationExternalType validationExternalType =
            new ValidationExternalType();
        validationExternalType.setValidationExternalID(
            createValidationExternal());
        validationExternalType.updateLevel();
        return validationExternalType;
    }

    public static ValidationExpression createValidationExpression()
    {
        ValidationExpression validationExpression = new ValidationExpression();
        validationExpression.setResourceid(createResource());
        validationExpression.updateLevel();
        return validationExpression;
    }
}
