/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Table;

public class StandardForeignKeyFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection foreignKeys,
            String[] levels)
	{
		if (foreignKeys.size() == 0)
		{
			return;
		}
		
	    PrintWriter out = context.getWriter();
		TagBuilder metadata = new TagBuilder(context.getWriter(),
		                             "METADATA-FOREIGN_KEYS")
				 .appendAttribute("Version", context.getVersion())
				 .appendAttribute("Date", context.getDate(), context.getRetsVersion())
				 .beginContentOnNewLine();
		
		for (Iterator iterator = foreignKeys.iterator(); iterator.hasNext();)
		{
	        ForeignKey foreignKey = (ForeignKey) iterator.next();
	        
	        TagBuilder tag = new TagBuilder(out, "ForeignKey")
			 	.beginContentOnNewLine();
	        
			TagBuilder.simpleTag(out, "ForeignKeyID", foreignKey.getForeignKeyID());
	        
			Table table = foreignKey.getParentTable();
	        String [] paths = new String[2];
	        String name = null;
	                
	        if (table != null)
	        {
		        paths = StringUtils.split(table.getLevel(), ":");
		        name = table.getSystemName();
	        }
		        
	        TagBuilder.simpleTag(out, "ParentResourceID", paths[0]);
			TagBuilder.simpleTag(out, "ParentClassID", paths[1]);
			TagBuilder.simpleTag(out, "ParentSystemName", name);

	        paths[0] = null;
	        paths[1] = null;
	        name = null;
	        
		    table = foreignKey.getChildTable();
		    
		    if (table != null)
		    {
		    	paths = StringUtils.split(table.getLevel(), ":");
		    	name = table.getSystemName();
		    }
	    	TagBuilder.simpleTag(out, "ChildResourceID", paths[0]);
			TagBuilder.simpleTag(out, "ChildClassID", paths[1]);
			TagBuilder.simpleTag(out, "ChildSystemName", name);

			TagBuilder.simpleTag(out, "ConditionalParentField", foreignKey.getConditionalParentField());
			TagBuilder.simpleTag(out, "ConditionalParentValue", foreignKey.getConditionalParentValue());

			tag.close();
		}
		
		metadata.close();
	}
}
