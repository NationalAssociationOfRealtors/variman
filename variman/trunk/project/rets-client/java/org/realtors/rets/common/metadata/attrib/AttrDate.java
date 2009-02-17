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
	public Date parse(String value, boolean strict) throws MetaParseException {
		Date d; 
		
		try 
		{
			d = RetsDateTime.parse1_7_2(value, strict);
		} 
		catch (ParseException e) 
		{
			if( strict ) 
				throw new MetaParseException(e);
			return null;
		}
		return d;
	}
	
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

	public String render(Date value) {
		return render(value, RetsVersion.RETS_1_7_2);
	}
	
	public String render(Date value, RetsVersion retsVersion)
	{
		if (value == null)
			return "";
		
	    return RetsDateTime.render(value, retsVersion);
	}

	public Class<Date> getType() {
		return Date.class;
	}
}
