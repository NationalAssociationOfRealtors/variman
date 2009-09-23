/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata.attrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

import org.realtors.rets.common.metadata.MetaParseException;

public class AttrEnum extends AttrAbstractText {
	
	/**
	 * Constructor
	 * @param values A string array containing the enumerated values.
	 * @param description A String containing the description for this attribute.
	 */
	public AttrEnum(String [] values, String description) 
	{
		super(0, 0, description);
		this.map = new HashMap<String,String>();
		for (String value : values)  this.map.put(value, value);
		this.map = Collections.unmodifiableMap(this.map);
		this.description 	= description;
	}
	
	/**
	 * Constructor
	 * @param values A string array containing the enumerated values.
	 */
	public AttrEnum(String[] values) {
		this(values, null);
	}

	@Override
	protected void checkContent(String value) throws MetaParseException {
		if( !this.map.containsKey(value) ) 
			throw new MetaParseException("Invalid key: " + value);
	}
	
	public String [] toArray()
	{
		ArrayList<String> names = new ArrayList<String>();
		Iterator iter = map.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			names.add(key);
		}
		String [] stringArray = new String[names.size()];
		stringArray = names.toArray(stringArray);
		
		return stringArray;
	}

	private Map<String,String> map;
}
