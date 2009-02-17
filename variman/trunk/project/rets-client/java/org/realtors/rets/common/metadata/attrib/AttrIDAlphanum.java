/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import org.realtors.rets.common.metadata.MetaParseException;

public class AttrIDAlphanum extends AttrAbstractText {

	public AttrIDAlphanum(int min, int max) {
		super(min, max);
	}

	@Override
	protected void checkContent(String value) throws MetaParseException {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (!Character.isLetterOrDigit(c)) {
				// 1.7.2 Unfortunately, there is an inconsistency in the spec vs the declared
				// standard names in that the standard names use a period instead of an underscore.
				// Until that is resolved, we'll need to allow both.
				// FIXME: 1.7.2 spec IDALPHANUM and standard names wrt to periods and underscores.
				if ("_.".indexOf(c) == -1) {
					throw new MetaParseException("Invalid IDAlphanum character at position " + i + ": " + c);
				}
			}
		}
	}
}
