//$Id: MetaType.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * @author Gavin King
 */
public class MetaType extends AbstractType {
	
	private final Map values;
	private final Map keys;
	private final Type baseType;
	
	public MetaType(Map values, Type baseType) {
		this.baseType = baseType;
		this.values = values;
		keys = new HashMap();
		Iterator iter = values.entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry me = (Map.Entry) iter.next();
			keys.put( me.getValue(), me.getKey() );
		}
	}

	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return baseType.sqlTypes(mapping);
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		return baseType.getColumnSpan(mapping);
	}

	public Class getReturnedClass() {
		return Class.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return Hibernate.CLASS.equals(x, y);
	}

	public Object nullSafeGet(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {
		Object key = baseType.nullSafeGet(rs, names, session, owner);
		return key==null ? null : values.get(key);
	}

	public Object nullSafeGet(
		ResultSet rs,
		String name,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {
		Object key = baseType.nullSafeGet(rs, name, session, owner);
		return key==null ? null : values.get(key);
	}

	public void nullSafeSet(
		PreparedStatement st,
		Object value,
		int index,
		SessionImplementor session)
		throws HibernateException, SQLException {
		baseType.nullSafeSet(st, value==null ? null : keys.get(value), index, session);
	}

	public String toString(Object value, SessionFactoryImplementor factory)
		throws HibernateException {
		return Hibernate.CLASS.toString(value, factory);
	}

	public Object fromString(String xml)
		throws HibernateException {
		return Hibernate.CLASS.fromString(xml);
	}

	public String getName() {
		return baseType.getName(); //TODO!
	}

	public Object deepCopy(Object value) throws HibernateException {
		return Hibernate.CLASS.deepCopy(value);
	}

	public boolean isMutable() {
		return false;
	}

	public boolean hasNiceEquals() {
		return true;
	}

}
