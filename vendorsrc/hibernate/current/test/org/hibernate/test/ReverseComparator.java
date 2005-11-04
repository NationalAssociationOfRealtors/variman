//$Id: ReverseComparator.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;
import java.util.Comparator;

public final class ReverseComparator implements Comparator, Serializable {
	public int compare(Object x, Object y) {
		return - ( (Comparable) x ).compareTo(y);
	}
	
	public boolean equals(Object obj) {
		return obj instanceof ReverseComparator;
	}
	
	public int hashCode() {
		return 0;
	}
	
}






