package org.realtors.rets.common.metadata;

import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;
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
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.RetsDateTime;
import org.realtors.rets.common.util.TagBuilder;
/**
 * This class is responsible for serializing the metadata into the RETS COMPACT format.
 * @author mklein
 *
 */
public class MetadataCompactFormatter 
{
	/**
	 * Constructor.
	 * @param metadata The Metadata
	 * @param out A PrintWriter to receive the compact formatted data
	 * @param retsVersion The RETS Version to use for date formatting. Default RETS_1_7_2.
	 */
	public MetadataCompactFormatter(Metadata metadata, PrintWriter out, RetsVersion retsVersion)
	{
		mOut = out;
		mRetsVersion = retsVersion;
		mSystem = metadata.getSystem();
		mSystemVersion = mSystem.getAttributeAsString(MSystem.VERSION);
		mSystemDate = RetsDateTime.render(mSystem.getDate(), mRetsVersion);
	}
	
	/**
	 * Format a RETS RESOURCE and dependents (RETS CLASS).
	 * @param resource An MResource containing the RETS RESOURCE.
	 */
	public void formatClasses(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MClass clazz : resource.getMClasses())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-CLASS")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.CLASSVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.CLASSDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(clazz.getAttributeNames());
			}
			
			formatDataRow(clazz);
		}
		if (tag != null)
			tag.close();
		for (MClass clazz : resource.getMClasses())
		{
			formatTables(resource, clazz);
		}
	}
	
	public void formatEditMasks(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MEditMask editMask : resource.getMEditMasks())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-EDITMASK")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.EDITMASKVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.EDITMASKDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(editMask.getAttributeNames());
			}
			
			formatDataRow(editMask);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatForeignKeys()
	{
		TagBuilder tag = null;
		
		for (MForeignKey foreignKey : mSystem.getMForeignKeys())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-FOREIGNKEYS")
								.appendAttribute("Version", mSystemVersion)
								.appendAttribute("Date", mSystemDate)
								.beginContentOnNewLine()
								.appendColumns(foreignKey.getAttributeNames());
			}
			
			formatDataRow(foreignKey);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatLookups(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MLookup lookup : resource.getMLookups())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-LOOKUP")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.LOOKUPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.LOOKUPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(lookup.getAttributeNames());
			}
			
			formatDataRow(lookup);
		}
		if (tag != null)
			tag.close();
		for (MLookup lookup : resource.getMLookups())
		{
			formatLookupTypes(resource, lookup);
		}
	}
	
	public void formatLookupTypes(MResource resource, MLookup lookup)
	{
		TagBuilder tag = null;
		
		for (MLookupType lookupType : lookup.getMLookupTypes())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-LOOKUP_TYPE")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.LOOKUPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.LOOKUPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("Lookup", lookup.getLookupName())
								.beginContentOnNewLine()
								.appendColumns(lookupType.getAttributeNames());
			}
			
			formatDataRow(lookupType);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatResources()
	{
		TagBuilder tag = null;
		
		for (MResource resource : mSystem.getMResources())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-RESOURCE")
								.appendAttribute("Version", mSystemVersion)
								.appendAttribute("Date", mSystemDate)
								.beginContentOnNewLine()
								.appendColumns(resource.getAttributeNames());
			}
			
			formatDataRow(resource);
		}
		if (tag != null)
			tag.close();
		
		for (MResource resource : mSystem.getMResources())
		{
			formatClasses(resource);
			formatObjects(resource);
			formatSearchHelp(resource);
			formatEditMasks(resource);
			formatUpdateHelp(resource);
			formatLookups(resource);
			formatValidationLookup(resource);
			formatValidationExternal(resource);
			formatValidationExpression(resource);
		}
	}
	
	public void formatObjects(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MObject object : resource.getMObjects())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-OBJECT")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.OBJECTVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.OBJECTDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(object.getAttributeNames());
			}
			
			formatDataRow(object);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatSearchHelp(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MSearchHelp searchHelp : resource.getMSearchHelps())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-SEARCH_HELP")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.SEARCHHELPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.SEARCHHELPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(searchHelp.getAttributeNames());
			}
			
			formatDataRow(searchHelp);
		}
		if (tag != null)
			tag.close();
	}
	
	
	public void formatSystem()
	{
		/*
		 * System metadata is a kludge!
		 */
		
        TagBuilder tag = new TagBuilder(mOut, "METADATA-SYSTEM")
            .appendAttribute("Version", mSystemVersion)
            .appendAttribute("Date", mSystemDate)
            .beginContentOnNewLine();

        new TagBuilder(mOut, "SYSTEM")
            .appendAttribute("SystemID", mSystem.getSystemID())
            .appendAttribute("SystemDescription", mSystem.getSystemDescription())
            .appendAttribute("TimeZoneOffset", mSystem.getTimeZoneOffset())
            .close();

        TagBuilder.simpleTag(mOut, "COMMENTS", mSystem.getComments());
        tag.end();
	}
	
	public void formatTables(MResource resource, MClass clazz)
	{
		TagBuilder tag = null;
		
		for (MTable table : clazz.getMTables())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-TABLE")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.CLASSVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.CLASSDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("Class", clazz.getClassName())
								.beginContentOnNewLine()
								.appendColumns(table.getAttributeNames());
			}
			
			formatDataRow(table);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatUpdates(MResource resource, MClass clazz)
	{
		TagBuilder tag = null;
		
		for (MUpdate update : clazz.getMUpdates())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-UPDATE")
								.appendAttribute("Version", mSystemVersion)
								.appendAttribute("Date", mSystemDate)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("Class", clazz.getClassName())
								.beginContentOnNewLine()
								.appendColumns(update.getAttributeNames());
			}
			
			formatDataRow(update);
		}
		if (tag != null)
			tag.close();
		for (MUpdate update : clazz.getMUpdates())
		{
			formatUpdateTypes(resource, clazz, update);
		}
	}
	
	public void formatUpdateTypes(MResource resource, MClass clazz, MUpdate update)
	{
		TagBuilder tag = null;
		
		for (MUpdateType updateType : update.getMUpdateTypes())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-UPDATE_TYPE")
								.appendAttribute("Version", mSystemVersion)
								.appendAttribute("Date", mSystemDate)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("Class", clazz.getClassName())
								.appendAttribute("Update", update.getUpdateName())
								.beginContentOnNewLine()
								.appendColumns(updateType.getAttributeNames());
			}
			
			formatDataRow(updateType);
		}
		if (tag != null)
			tag.close();
	}

	public void formatUpdateHelp(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MUpdateHelp updateHelp : resource.getMUpdateHelps())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-UPDATE_HELP")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.UPDATEHELPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.UPDATEHELPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(updateHelp.getAttributeNames());
			}
			
			formatDataRow(updateHelp);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatValidationExpression(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MValidationExpression expression : resource.getMValidationExpressions())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-VALIDATION_EXPRESSION")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.VALIDATIONEXPRESSIONVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.VALIDATIONEXPRESSIONDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
									.beginContentOnNewLine()
								.appendColumns(expression.getAttributeNames());
			}
			
			formatDataRow(expression);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatValidationExternal(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MValidationExternal external : resource.getMValidationExternals())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-VALIDATION_EXTERNAL")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.VALIDATIONEXTERNALVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.VALIDATIONEXTERNALDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
									.beginContentOnNewLine()
								.appendColumns(external.getAttributeNames());
			}
			
			formatDataRow(external);
		}
		if (tag != null)
			tag.close();
		for (MValidationExternal external : resource.getMValidationExternals())
		{
			formatValidationExternalType(resource, external);
		}
	}
	
	public void formatValidationExternalType(MResource resource, MValidationExternal external)
	{
		TagBuilder tag = null;
		
		for (MValidationExternalType externalType : external.getMValidationExternalTypes())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-VALIDATION_EXTERNAL_TYPE")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.VALIDATIONEXTERNALVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.VALIDATIONEXTERNALDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("ValidationExternalName", external.getValidationExternalName())
								.beginContentOnNewLine()
								.appendColumns(externalType.getAttributeNames());
			}
			
			formatDataRow(externalType);
		}
		if (tag != null)
			tag.close();
	}
	
	public void formatValidationLookup(MResource resource)
	{
		TagBuilder tag = null;
		
		for (MValidationLookup validationLookup : resource.getMValidationLookups())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-VALIDATION_LOOKUP")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.VALIDATIONLOOKUPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.VALIDATIONLOOKUPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.beginContentOnNewLine()
								.appendColumns(validationLookup.getAttributeNames());
			}
			
			formatDataRow(validationLookup);
		}
		if (tag != null)
			tag.close();
		for (MValidationLookup validationLookup : resource.getMValidationLookups())
		{
			formatValidationLookupTypes(resource, validationLookup);
		}
	}
	
	public void formatValidationLookupTypes(MResource resource, MValidationLookup lookup)
	{
		TagBuilder tag = null;
		
		for (MValidationLookupType lookupType : lookup.getMValidationLookupTypes())
		{
			if (tag == null)
			{
				tag = new TagBuilder(mOut, "METADATA-VALIDATION_LOOKUP_TYPE")
								.appendAttribute("Version", resource.getAttributeAsString(MResource.VALIDATIONLOOKUPVERSION))
								.appendAttribute("Date", resource.getDateAttribute(MResource.VALIDATIONLOOKUPDATE), mRetsVersion)
								.appendAttribute("Resource", resource.getResourceID())
								.appendAttribute("ValidationLookup", lookup.getValidationLookupName())
								.beginContentOnNewLine()
								.appendColumns(lookupType.getAttributeNames());
			}
			
			formatDataRow(lookupType);
		}
		if (tag != null)
			tag.close();
	}


	public void formatDataRow(MetaObject metaObject)
	{
		DataRowBuilder row = new DataRowBuilder(mOut);
		
		row.begin();
		for (String attribute : metaObject.getAttributeNames())
        {
        	String value = metaObject.getAttributeAsString(attribute);
        	if (value == null)
        		value = sEmpty;
        	row.append(value);
        }
		row.end();
	}

	public void output()
	{
		TagBuilder tag = new TagBuilder(mOut, "RETS").beginContentOnNewLine();
		
		formatSystem();
		
		formatForeignKeys();
		/*
		 * Process all METADATA-RESOURCE.
		 */
		formatResources();

		tag.close();
	}
	
	private PrintWriter mOut;
	private RetsVersion mRetsVersion;
	private MSystem mSystem;
	private String mSystemDate;
	private String mSystemVersion;
	
	private static final String sEmpty = "";
}
