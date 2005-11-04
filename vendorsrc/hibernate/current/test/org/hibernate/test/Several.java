//$Id: Several.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;


public class Several implements Serializable {
	private String id;
	private String prop;
	private Single single;
	private String string;
	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the prop.
	 * @return String
	 */
	public String getProp() {
		return prop;
	}
	
	/**
	 * Returns the single.
	 * @return Single
	 */
	public Single getSingle() {
		return single;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the prop.
	 * @param prop The prop to set
	 */
	public void setProp(String prop) {
		this.prop = prop;
	}
	
	/**
	 * Sets the single.
	 * @param single The single to set
	 */
	public void setSingle(Single single) {
		this.single = single;
	}
	
	/**
	 * Returns the string.
	 * @return String
	 */
	public String getString() {
		return string;
	}
	
	/**
	 * Sets the string.
	 * @param string The string to set
	 */
	public void setString(String string) {
		this.string = string;
	}
	
	public boolean equals(Object other) {
		return ( (Several) other ).id.equals(id) && ( (Several) other ).string.equals(string);
	}
	
	public int hashCode() {
		return id.hashCode();
	}
	
}






