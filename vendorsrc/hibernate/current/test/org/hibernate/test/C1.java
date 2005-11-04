//$Id: C1.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.util.ArrayList;
import java.util.Collection;

public class C1 extends B{
	private String address;
	private C2 c2;
	private D d;
	private Collection c2s = new ArrayList();
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
	 * Returns the d.
	 * @return D
	 */
	public D getD() {
		return d;
	}
	
	/**
	 * Sets the d.
	 * @param d The d to set
	 */
	public void setD(D d) {
		this.d = d;
	}
	
	/**
	 * @return Returns the c.
	 */
	public C2 getC2() {
		return c2;
	}

	/**
	 * @param c The c to set.
	 */
	public void setC2(C2 c) {
		this.c2 = c;
	}

	/**
	 * @return Returns the cs.
	 */
	public Collection getC2s() {
		return c2s;
	}

	/**
	 * @param cs The cs to set.
	 */
	public void setC2s(Collection cs) {
		this.c2s = cs;
	}

}






