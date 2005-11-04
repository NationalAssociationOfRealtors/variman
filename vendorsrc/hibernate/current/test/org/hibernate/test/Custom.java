//$Id: Custom.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;


public class Custom implements Cloneable {
	long id;
	String name;
	
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException cnse) {
			throw new RuntimeException();
		}
	}
}






