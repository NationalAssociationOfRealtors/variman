package org.hibernate.test;

import java.util.Map;


public class B extends A {
	private int count;
	private Map map;
	
	/**
	 * Returns the count.
	 * @return int
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Sets the count.
	 * @param count The count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}






