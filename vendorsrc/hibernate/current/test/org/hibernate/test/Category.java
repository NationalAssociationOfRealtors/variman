//$Id: Category.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Category {
	
	public static final String ROOT_CATEGORY = "/";
	
	private long id;
	private String name;
	private List subcategories = new ArrayList();
	private Assignable assignable;
	/**
	 * Returns the id.
	 * @return long
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Returns the subcategories.
	 * @return List
	 */
	public List getSubcategories() {
		return subcategories;
	}
	
	/**
	 * Sets the subcategories.
	 * @param subcategories The subcategories to set
	 */
	public void setSubcategories(List subcategories) {
		this.subcategories = subcategories;
	}
	
	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public Assignable getAssignable() {
		return assignable;
	}

	public void setAssignable(Assignable assignable) {
		this.assignable = assignable;
	}

}






