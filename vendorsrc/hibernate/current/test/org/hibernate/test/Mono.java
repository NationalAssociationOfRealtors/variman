//$Id: Mono.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.util.Set;

public class Mono extends Top {
	
	private Set strings;

	/**
	 * Constructor for Mono.
	 * @param c
	 */
	public Mono(int c) {
		super(c);
	}

	/**
	 * Constructor for Mono.
	 */
	public Mono() {
		super();
	}

	/**
	 * Returns the strings.
	 * @return Set
	 */
	public Set getStrings() {
		return strings;
	}

	/**
	 * Sets the strings.
	 * @param strings The strings to set
	 */
	public void setStrings(Set strings) {
		this.strings = strings;
	}

}
