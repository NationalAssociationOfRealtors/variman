/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.attrib.AttrAbstractText;
import org.realtors.rets.common.metadata.MetaParseException;

public class AttrGenericText extends AttrAbstractText {
	private String mChars;
	/**
	 * Constructor
	 * @param min	An int indicating the minimum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 * @param chars A String containing the accepted characters for this attribute.
	 */
	public AttrGenericText(int min, int max, String chars) {
		super(min, max, null);
		this.mChars = chars;
	}
	
	/**
	 * Constructor
	 * @param min	An int indicating the minimum length of this attribute.
	 * @param max	An int indicating the maximum length of this attribute.
	 * @param chars A String containing the accepted characters for this attribute.
	 * @param description A String containing the description for this attribute.
	 */
	public AttrGenericText(int min, int max, String chars, String description) {
		super(min, max, description);
		this.mChars = chars;
	}


	@Override
	protected void checkContent(String value) throws MetaParseException {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (this.mChars.indexOf(c) == -1) {
				throw new MetaParseException("Invalid char (" + c + ") at position " + i);
			}
		}
	}

}
