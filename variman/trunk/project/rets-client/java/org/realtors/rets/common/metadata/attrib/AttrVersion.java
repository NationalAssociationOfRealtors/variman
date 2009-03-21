/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

/**
 * A version is a string formatted "major.minor.release".  This gets converted
 * to an integer such as major * 10,000,000 + minor * 100,000 + release.
 */
public class AttrVersion implements AttrType<Integer> {
	
	/** A description of this attribute. */
	protected String description;

	/**
	 * Constructor
	 * @param description A String containing the description for this attribute.
	 */
	public AttrVersion(String description) 
	{
		this.description 	= description;
	}
	
	/**
	 * Constructor
	 */
	public AttrVersion()
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
		String[] parts = StringUtils.split(value, ".");
		int major, minor, release;
		if (strict && parts != null && parts.length != 3) {
			throw new MetaParseException("Invalid version: " + value + ", " + parts.length + " parts");
		}
		try {
			major = Integer.parseInt(this.getPart(parts,0));
			minor = Integer.parseInt(this.getPart(parts,1));
			release = Integer.parseInt(this.getPart(parts,2));
		} catch (NumberFormatException e) {
			throw new MetaParseException("Invalid version: " + value, e);
		}
		if ((major < 100) && (major >= 0) && (minor < 100) && (minor >= 0) && (release < 100000) && (release >= 0)) {
			return new Integer(major * 10000000 + minor * 100000 + release);
		}
		if( strict ) 
			throw new MetaParseException("Invalid version: " + value);
		return 0;
	}
	
	/**
	 * Return the component parts of the version.
	 * @param parts A String array containing the RETS major, minor and update versions.
	 * @param part An int indicating which component to return.
	 * @return A String containing the version component.
	 */
	private String getPart(String[] parts, int part){
		if( parts != null && parts.length > part ) return parts[part];
		return "0";
	}

	/**
	 * Render the attribute.
	 */
	public String render(Integer value) {
		int ver = value.intValue();
		int release = ver % 100000;
		int minor = (ver / 100000) % 100;
		int major = (ver / 10000000);
		String minstr = Integer.toString(minor);
		String relstr = Integer.toString(release);
		while (minstr.length() < 2) {
			minstr = "0" + minstr;
		}
		while (relstr.length() < 5) {
			relstr = "0" + relstr;
		}
		return major + "." + minstr + "." + relstr;
	}

	/**
	 * Return the Class type.
	 */
	public Class<Integer> getType() {
		return Integer.class;
	}
}
