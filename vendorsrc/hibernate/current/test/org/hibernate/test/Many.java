//$Id: Many.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

public class Many {
	Long key;
	One one;
	private int x;
	private int v;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public void setKey(Long key) {
		this.key = key;
	}
	
	public Long getKey() {
		return this.key;
	}
	
	public void setOne(One one) {
		this.one = one;
	}
	
	public One getOne() {
		return this.one;
	}
	public int getV() {
		return v;
	}
	public void setV(int v) {
		this.v = v;
	}
}






