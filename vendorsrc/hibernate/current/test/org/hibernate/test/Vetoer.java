//$Id: Vetoer.java,v 1.4 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Lifecycle;
import net.sf.hibernate.Session;

public class Vetoer implements Lifecycle {
	
	boolean onSaveCalled;
	boolean onUpdateCalled;
	boolean onDeleteCalled;
	
	private String name;
	private String[] strings;
	
	public boolean onSave(Session s) throws CallbackException {
		boolean result = !onSaveCalled;
		onSaveCalled = true;
		return result;
	}
	
	public boolean onUpdate(Session s) throws CallbackException {
		boolean result = !onUpdateCalled;
		onUpdateCalled = true;
		return result;
	}
	
	public boolean onDelete(Session s) throws CallbackException {
		boolean result = !onDeleteCalled;
		onDeleteCalled = true;
		return result;
	}
	
	public void onLoad(Session s, Serializable id) {}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getStrings() {
		return strings;
	}
	
	public void setStrings(String[] strings) {
		this.strings = strings;
	}
	
}






