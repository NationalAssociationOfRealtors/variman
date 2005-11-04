//$Id: Broken.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gavin King
 */
public class Broken implements Serializable {
	private Long id;
	private String otherId;
	private Date timestamp;
	public Long getId() {
		return id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setId(Long long1) {
		id = long1;
	}

	public void setTimestamp(Date date) {
		timestamp = date;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String string) {
		otherId = string;
	}
	
	public boolean equals(Object other) {
		if ( !(other instanceof Broken) ) return false;
		Broken that = (Broken) other;
		return this.id.equals(that.id) && this.otherId.equals(that.otherId);
	}
	
	public int hashCode() {
		return 1;
	}

}
