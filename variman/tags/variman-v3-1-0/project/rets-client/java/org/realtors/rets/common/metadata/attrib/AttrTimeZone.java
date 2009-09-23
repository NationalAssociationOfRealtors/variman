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

public class AttrTimeZone implements AttrType<Integer> 
{
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param min	An int indicating the minimum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 * @param description A String containing the description for this attribute.
	 */
	public AttrTimeZone(String description) 
	{
		this.description 	= description;
	}
	
	/**
	 * Constructor
	 */
	public AttrTimeZone()
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
	public Integer parse(String value, boolean strict) throws MetaParseException 
	{
		try 
		{
			// See if the only character is a Z. 
			if (value.compareTo("Z") == 0)
				return new Integer(0);
			
			// Remove the colon and the plus character if present.
			String candidate = value;
			int i = candidate.indexOf("+");
			
			if (i > -1)
				candidate = candidate.substring(0, i) + candidate.substring(i + 1);
			
			i = candidate.indexOf(":");
			if (i > -1)
				candidate = candidate.substring(0, i) + candidate.substring(i + 1);
			
			return Integer.parseInt(candidate);
		} 
		catch (NumberFormatException f)
		{
			if( strict ) 
				throw new MetaParseException(f);
		}
		return null;
	}

	/**
	 * Render the attribute.
	 */
	public String render(Integer value) 
	{
		String TimeZoneValue = "Z";
		
		if (value < 0)
		{
			TimeZoneValue = "-";
			value = -value;
		}
		else if (value > 0)
		{
			TimeZoneValue = "+";
		}
		if (TimeZoneValue.compareTo("Z") == 0)
			return TimeZoneValue;
		
		String hours = Integer.toString(value / 100);
		String mins = Integer.toString(value % 100);
		
		while (hours.length() < 2)
			hours = "0" + hours;
		
		while (mins.length() < 2)
			mins = "0" + mins;
		
		return TimeZoneValue + hours + ":" + mins;
	}

	/**
	 * Attribute specific content checker.
	 * @param value
	 * @throws MetaParseException
	 */
	public Class<Integer> getType() 
	{
		return Integer.class;
	}
}
