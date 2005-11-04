//$Id: One.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.util.Set;

public class One {
	Long key;
	String value;
	Set manies;
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
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Set getManies() {
		return manies;
	}
	
	public void setManies(Set manies) {
		this.manies = manies;
	}
	
	public int getV() {
		return v;
	}
	public void setV(int v) {
		this.v = v;
	}
}






