//$Id: C2.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.util.ArrayList;
import java.util.Collection;

public class C2 extends B {
	private String address;
	private C1 c1;
	private Collection c1s = new ArrayList();
	/**
	 * Returns the address.
	 * @return String
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 * @param address The address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * @return Returns the c.
	 */
	public C1 getC1() {
		return c1;
	}

	/**
	 * @param c The c to set.
	 */
	public void setC1(C1 c) {
		this.c1 = c;
	}

	/**
	 * @return Returns the cs.
	 */
	public Collection getC1s() {
		return c1s;
	}

	/**
	 * @param cs The cs to set.
	 */
	public void setC1s(Collection cs) {
		this.c1s = cs;
	}

}






