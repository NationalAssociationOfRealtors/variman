//$Id: Record.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.perf;

import java.math.BigDecimal;
import java.util.Date;
//import java.util.List;

/**
 * @author Gavin King
 */
public class Record {
	
	private Long id;
	private int quantity;
	private BigDecimal price;
	private String name;
	private String description;
	private Date timestamp;
	private String serialNumber;
	//private List children;
	private Record parent;
	private int version;
	
	public Record() {}
	
	public Record(
			Long id, 
			String name, 
			String description, 
			Date timestamp, 
			int quantity, 
			BigDecimal price, 
			String serialNumber, 
			int version
	) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.timestamp = timestamp;
		this.quantity = quantity;
		this.price = price;
		this.serialNumber = serialNumber;
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/*public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}*/

	public Record getParent() {
		return parent;
	}

	public void setParent(Record parent) {
		this.parent = parent;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
