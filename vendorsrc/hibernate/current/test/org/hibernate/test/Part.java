//$Id: Part.java,v 1.3 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

/**
 * @author Gavin King
 */
public class Part {

	private Long id;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public void setDescription(String string) {
		description = string;
	}

	public void setId(Long long1) {
		id = long1;
	}
	
	public static class SpecialPart extends Part {}

}
