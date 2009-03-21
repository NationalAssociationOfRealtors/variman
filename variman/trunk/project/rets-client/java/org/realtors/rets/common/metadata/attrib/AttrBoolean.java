/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrBoolean implements AttrType<Boolean> {
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param description A String containing the description for this attribute.
	 */
	public AttrBoolean(String description)
	{
		this.description = description;
	}

	/**
	 * Constructor
	 * @param description A String containing the description for this attribute.
	 */
	public AttrBoolean()
	{
		this.description = null;
	}
	
	/**
	 * Return the description of this attribute. The description field is arbitrary, but can
	 * be used for things like error text when validation fails.
	 * @return A String containing the description for this attribute.
	 */
	public String getDescription()
	{
		if (description == null)
			return "";
		
		return description;
	}

	/**
	 * Parse and optionally validate the contents of this attribute.
	 * @param value	A String containing the value to parse.
	 * @param strict A boolean that indicates whether or not strict parsing
	 * is to take place.
	 */
	public Boolean parse(String value, boolean strict) throws MetaParseException 
	{
		if (value.equals("1")) {
			return Boolean.TRUE;
		}
		if (value.equals("0")) {
			return Boolean.FALSE;
		}

		if (value.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}

		if (value.equalsIgnoreCase("Y")) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase("N")) {
			return Boolean.FALSE;
		}

		if (value.equals("")) {
			return Boolean.FALSE;
		}

		if( strict ) 
			throw new MetaParseException("Invalid boolean value: " + value);
		return false;
	}
	
	/**
	 * Render the attribute.
	 */
	public String render(Boolean value) 
	{
		if( value.booleanValue() ) return "1";

		return "0";
	}
	
	/**
	 * Return the Class type for this method.
	 */
	public Class<Boolean> getType() 
	{
		return Boolean.class;
	}
}
