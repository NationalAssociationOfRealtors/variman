package org.realtors.rets.common.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CaseInsensitiveTreeMap<K, V> extends TreeMap<K, V> 
{
	/** 
	 * Maintain the keys in an ordered array. This is in order to return them
	 * in the same order they were entered for display purposes.
	 */
	private List<K> 			keys 		= new ArrayList<K>();
	private transient Set<K>	keySet 		= null;
	
	public CaseInsensitiveTreeMap(Map<K, V> map) 
	{
		this();
		this.putAll(map);
		/*
		 * Save the key set locally.
		 */
		keys.addAll(map.keySet());
	}

	public CaseInsensitiveTreeMap() 
	{
		super(new CaseInsensitiveComparator());
	}

	/**
	 * Return the key set in the same order it was entered.
	 */
	public Set<K> keySet()
	{
		if (keySet == null)
		{
			keySet = new AbstractSet<K>()
			{
				public java.util.Iterator<K> iterator()
				{
					return new Iterator<K>();
				}
				
				public int size()
				{
					return keys.size();
				}
				
				public boolean contains(Object o)
				{
					return keys.contains(o);
				}
				
				public boolean remove(Object o)
				{
					int oldSize = keys.size();
					keys.remove(o);
					CaseInsensitiveTreeMap.this.remove(o);
					return oldSize != keys.size();
				}
				
				public void clear()
				{
					keys.clear();
					this.clear();
				}
			};
		}
		return keySet;
	}
	
	public V put(K key, V value)
	{
		keys.add(key);
		return super.put(key, value);
	}
	
	private class Iterator<I> implements java.util.Iterator<K>
	{
		int cursor 			= -1;
		int next 			= 0;
		int size 			= keys.size();
		
		public boolean hasNext()
		{
			return next < size;
		}
		
		public K next()
		{
			cursor = next++;
			return keys.get(cursor);
		}
		
		public void remove()
		{
			if (cursor == -1)
				throw new NoSuchElementException();
			
			CaseInsensitiveTreeMap.this.remove(keys.get(cursor));
			keys.remove(cursor);
			cursor = -1;
		}
	}
}
