/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrPositiveNumeric implements AttrType<Integer> {
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param description A String containing the description for this attribute.
	 */
	public AttrPositiveNumeric(String description) 
	{
		this.description 	= description;
	}
	
	/**
	 * Constructor
	 */
	public AttrPositiveNumeric()
	{
		this.description	= null;
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
	public Integer parse(String value, boolean strict) throws MetaParseException {
		try {
			Integer I = new Integer(value);
			if (I > 0)
				return I;
			if (strict)
			    throw new MetaParseException("The value must be a positive number greater than zero.");
			return 0;
		} catch (NumberFormatException e) {
			if( strict ) 
				throw new MetaParseException(e);
			return 0;
		}
	}

	/**
	 * Render the attribute.
	 */
	public String render(Integer value) {
		return value.toString();
	}

	/**
	 * Return the Class type.
	 */
	public Class<Integer> getType() {
		return Integer.class;
	}
}
