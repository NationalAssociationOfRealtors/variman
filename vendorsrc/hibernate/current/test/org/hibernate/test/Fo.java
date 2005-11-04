//$Id: Fo.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;

public final class Fo {
	
	public static Fo newFo() {
		return new Fo();
	}
	
	private Fo() {}
	
	private byte[] buf;
	private Serializable serial;
	private long version;
	private int x;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public byte[] getBuf() {
		return buf;
	}
	
	
	public Serializable getSerial() {
		return serial;
	}
	
	
	public void setBuf(byte[] buf) {
		this.buf = buf;
	}
	
	
	public void setSerial(Serializable serial) {
		this.serial = serial;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
}







