/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.MetaParseException;

public class AttrText extends AttrAbstractText {
	
	/**
	 * Constructor
	 * @param min	An int indicating the minimum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 * @param description A String containing the description for this attribute.
	 */
	public AttrText(int min, int max, String description) 
	{
		super(min, max, description);
	}
	
	/**
	 * Constructor
	 * @param min	An int indicating the maximum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 */
	public AttrText(int min, int max) {
		super(min, max);
	}

	@Override
	protected void checkContent(String value) throws MetaParseException {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (!(c == '\n' || c == '\r' || c == ' ' || c == '\t' || (c > 31 && c < 127))) {
				throw new MetaParseException("Invalid character (ordinal " + (int) c + ") at position " + i);
			}
		}
	}

}
