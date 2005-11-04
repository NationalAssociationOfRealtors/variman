//$Id: Parent.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

public class Parent {
	private long id;
	private int count;
	private Child child;
	private Object any;
	private int x;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getCount() {
		return count;
	}
	
	
	public long getId() {
		return id;
	}
	
	
	public void setCount(int count) {
		this.count = count;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public Child getChild() {
		return child;
	}
	
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	public Object getAny() {
		return any;
	}

	public void setAny(Object any) {
		this.any = any;
	}

}







