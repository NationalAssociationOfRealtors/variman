//$Id: Fumm.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.util.Locale;

public class Fumm {
	
	private Locale locale;
	private Fum fum;
	
	public FumCompositeID getId() {
		return fum.getId();
	}
	public void setId(FumCompositeID id) {
	}
	
	public Fumm() {
		super();
	}
	
	/**
	 * Returns the fum.
	 * @return Fum
	 */
	public Fum getFum() {
		return fum;
	}
	
	/**
	 * Returns the locale.
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * Sets the fum.
	 * @param fum The fum to set
	 */
	public void setFum(Fum fum) {
		this.fum = fum;
	}
	
	/**
	 * Sets the locale.
	 * @param locale The locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}






