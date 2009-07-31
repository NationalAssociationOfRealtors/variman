package org.realtors.rets.common.metadata;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.realtors.rets.client.RetsVersion;

/**
 * MetadataElement associates the metadata attribute with the corresponding
 * attribute type and indicates the RETS Version where it first appears.
 * @author mklein
 *
 */
public class MetadataElement
{
	private String name;
	private AttrType<?> type;
	private RetsVersion firstAppears;
	private boolean required;
	
	public MetadataElement(String name, AttrType<?> type, RetsVersion version, boolean required)
	{
		this.name = name;
		this.type = type;
		this.firstAppears = version;
		this.required = required;
	}
	
	public MetadataElement(String name, AttrType<?> type, boolean required)
	{
		this(name, type, RetsVersion.RETS_1_0, required);
	}
	
	public MetadataElement(String name, AttrType<?> type, RetsVersion version)
	{
		this(name, type, version, false);
	}
	
	public MetadataElement(String name, AttrType<?> type)
	{
		this(name, type, RetsVersion.RETS_1_0, false);
	}
	
	public String getName()
	{
		return name;
	}
	
	public AttrType<?> getType()
	{
		return type;
	}
	
	public RetsVersion getFirstAppears()
	{
		return firstAppears;
	}
	
	public boolean isRequired()
	{
		return required;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (!(obj instanceof MetadataElement)) 
		{
			return false;
		}
		MetadataElement rhs = (MetadataElement) obj;
		return new EqualsBuilder()
					.append(this.name, rhs.name)
					.append(this.type, rhs.type)
					.append(this.firstAppears, rhs.firstAppears)
					.append(this.required, rhs.required)
					.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
					.append(this.name)
					.append(this.type)
					.append(this.firstAppears)
					.append(this.required)
					.toHashCode();
	}
}
