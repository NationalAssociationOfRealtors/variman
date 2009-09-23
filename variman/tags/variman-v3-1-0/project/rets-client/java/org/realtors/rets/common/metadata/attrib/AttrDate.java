/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.util.RetsDateTime;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrDate implements AttrType<Date> {
	
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param description A String containing the description for this attribute.
	 */
	public AttrDate(String description) 
	{
		this.description 	= description;
	}
	
	/**
	 * Constructor
	 */
	public AttrDate()
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
	 * Parse the timestamp.
	 * @param value	A String containing the value to parse.
	 * @param strict A boolean that indicates whether or not strict parsing
	 * is to take place.
	 */
	public Date parse(String value, boolean strict) throws MetaParseException {
		Date d;
		
		try 
		{
			d = RetsDateTime.parse1_7_2(value, strict);
			if (d == null)
				d = RetsDateTime.parse(value, RetsVersion.RETS_1_0, strict);
		} 
		catch (ParseException e) 
		{
			if( strict ) 
				throw new MetaParseException(e);
			try
			{
				d = RetsDateTime.parse(value, RetsVersion.RETS_1_0, strict);
			}
			catch (ParseException f)
			{
				if (strict)
					throw new MetaParseException(f);
				return null;
			}
		}
		return d;
	}
	
	/**
	 * Parse the timestamp using the rules for the specified RETS version.
	 * @param value	A String containing the value to parse.
	 * @param retsVersion The RETS version of the data parsing algorithm to use.
	 * @param strict A boolean that indicates whether or not strict parsing
	 * is to take place.
	 */
	public Date parse(String value, RetsVersion retsVersion, boolean strict) throws MetaParseException {
		Date d; 
		
		try 
		{
			d = RetsDateTime.parse(value, retsVersion, strict);
		} 
		catch (ParseException e) 
		{
			if( strict ) 
				throw new MetaParseException(e);
			return null;
		}
		return d;
	}

	/**
	 * Parse the timestamp using the RETS 1.7.2 rules.
	 * @param value	A String containing the value to parse.
	 */
	public String render(Date value) {
		return render(value, RetsVersion.RETS_1_7_2);
	}
	
	/**
	 * Parse the timestamp using the specified RETS version rules.
	 * @param value	A String containing the value to parse.
	 * @param retsVersion The RETS version of the data parsing algorithm to use.
	 * @param strict A boolean that indicates whether or not strict parsing
	 * is to take place.
	 */
	public String render(Date value, RetsVersion retsVersion)
	{
		if (value == null)
			return "";
		
	    return RetsDateTime.render(value, retsVersion);
	}

	/**
	 * Return the Class type for this method.
	 */
	public Class<Date> getType() {
		return Date.class;
	}
}
