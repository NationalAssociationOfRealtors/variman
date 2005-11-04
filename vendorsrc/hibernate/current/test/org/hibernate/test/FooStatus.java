//$Id: FooStatus.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;

import net.sf.hibernate.PersistentEnum;

public final class FooStatus implements PersistentEnum, Serializable {
	private short code;
	
	public static final FooStatus ON = new FooStatus(1);
	public static final FooStatus OFF = new FooStatus(0);
	
	private FooStatus(int code) {
		this.code= (short) code;
	}
	
	public static FooStatus fromInt(int code) {
		if ( code==1 ) {
			return ON;
		}
		else {
			return OFF;
		}
	}
	public int toInt() {
		return code;
	}
	
	private Object readResolve() {
		return fromInt(code);
	}
	
}







