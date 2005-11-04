// $Id: N.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.test;
/**
 * @author Gavin King
 */
public class N {
	private Long uniqueSequence;
	private M parent;
	private String string;
	public Long getUniqueSequence() {
		return uniqueSequence;
	}
	public void setUniqueSequence(Long puniqueSequence) {
		uniqueSequence = puniqueSequence;
	}
	public M getParent() {
		return parent;
	}
	public void setParent(M pParent) {
		parent = pParent;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public boolean equals(Object other) {
		if ( !(other instanceof N) ) return false;
		N n = (N) other;
		return string.equals(n.string);
	}
	public int hashCode() {
		return string.hashCode();
	}
}