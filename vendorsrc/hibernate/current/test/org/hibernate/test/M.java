//$Id: M.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gavin King
 */
public class M {

	   private Long uniqueSequence;
	   protected Set children;

	   public M() {
	      children = new HashSet();
	   }
	   
	   public Long getUniqueSequence() {
	      return uniqueSequence;
	   }

	   public void setUniqueSequence(Long puniqueSequence) {
	      uniqueSequence = puniqueSequence;
	   }

	   public Set getChildren() {
	      return children;
	   }
	   
	   public void setChildren(Set pset) {
	      children = pset;
	   }

	   public void addChildren(N pChildren) {
	            
	      pChildren.setParent(this);
	      children.add(pChildren);

	   }
	   
	   public void removeChildren(N pChildren) {
	      if(children.contains(pChildren)) {
	         children.remove(pChildren);
	         //pChildren.setParent(null);
	      }
	   }

	}