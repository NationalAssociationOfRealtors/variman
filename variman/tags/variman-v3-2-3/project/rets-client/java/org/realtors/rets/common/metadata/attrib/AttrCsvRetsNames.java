/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrCsvRetsNames implements AttrType<String>
{
	private static final AttrType<String> RETSNAME = MetaObject.sRETSNAME;

	/**
	 * Parse and optionally validate the contents of this attribute.
	 * @param value A String containing the value to parse.
	 * @param strict A boolean that indicates whether or not strict parsing
	 * is to take place.
	 */
	public String parse(String value, boolean strict) throws MetaParseException
	{
		/*
		 * If strict parsing is not being used, simply return the value.
		 */
		if( !strict )
			return value;
		String[] retsNames = StringUtils.split(value, ",");
		for (String retsName : retsNames) {
			RETSNAME.parse(retsName, strict);
		}
		return value;
	}

	/**
	 * Return the description of this attribute. The description field is arbitrary, but can
	 * be used for things like error text when validation fails.
	 * @return A String containing the description for this attribute.
	 */
	public String getDescription()
	{
		String description = "A comma-separated list of RETSNAMEs. RETSNAME is Alpha Numeric including an underscore, 1 to 64 characters";
		return description;
	}

	/**
	 * Return the Class type.
	 */
	public Class<String> getType()
	{
		return String.class;
	}

	/**
	 * Render the attribute.
	 */
	public String render(String value)
	{
		return value;
	}
}
