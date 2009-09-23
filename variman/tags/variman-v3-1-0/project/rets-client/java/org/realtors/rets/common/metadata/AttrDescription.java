package org.realtors.rets.common.metadata;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * AttrDescription wraps an attribute type with its name and a brief message
 * that can be used to display errors when parsing fails.
 * @author mklein
 *
 */
public class AttrDescription
{
	private String name;
	private AttrType<?> type;
	private String description;
	
	public AttrDescription(String name, AttrType<?> type, String description)
	{
		this.name = name;
		this.type = type;
		this.description = description;
	}
	
	public String getName()
	{
		return name;
	}
	
	public AttrType<?> getType()
	{
		return type;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (!(obj instanceof AttrDescription)) 
		{
			return false;
		}
		AttrDescription rhs = (AttrDescription) obj;
		return new EqualsBuilder()
					.append(this.name, rhs.name)
					.append(this.type, rhs.type)
					.append(this.description, rhs.description)
					.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
					.append(this.name)
					.append(this.type)
					.append(this.description)
					.toHashCode();
	}
}
