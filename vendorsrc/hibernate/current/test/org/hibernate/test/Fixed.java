//$Id: Fixed.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Gavin King
 */
public class Fixed extends Broken {
	private Set set;
	private List list = new ArrayList();

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}
