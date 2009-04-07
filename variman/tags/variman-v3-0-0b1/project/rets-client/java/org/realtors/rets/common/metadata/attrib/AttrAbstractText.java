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

public abstract class AttrAbstractText implements AttrType<String> 
{
	/** The minimum length for this attribute. */
	protected int min;
	/** The maximum length for this attribute. */
	protected int max;
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param min	An int indicating the minimum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 * @param description A String containing the description for this attribute.
	 */
	public AttrAbstractText(int min, int max, String description) 
	{
		this.description 	= description;
		this.min 			= min;
		this.max 			= max;
	}
	
	/**
	 * Constructor
	 * @param min	An int indicating the maximum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 */
	public AttrAbstractText(int min, int max)
	{
		this(min, max, null);
	}

	/**
	 * Parse and optionally validate the contents of this attribute.
	 * @param value	A String containing the value to parse.
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
		/*
		 * Validate the minimum and maximum lengths for this attribute.
		 */
		int l = value.length();
		if (this.min != 0 && l < this.min) 
		{
			throw new MetaParseException("Value too short (min " + this.min + "): " + l);
		}
		if (this.max != 0 && l > this.max) 
		{
			throw new MetaParseException("Value too long (max " + this.max + "): " + l);
		}
		/*
		 * Invoke the attribute specific content checker.
		 */
		checkContent(value);
		return value;
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
	/**
	 * Attribute specific content checker.
	 * @param value
	 * @throws MetaParseException
	 */
	protected abstract void checkContent(String value) throws MetaParseException;

}
