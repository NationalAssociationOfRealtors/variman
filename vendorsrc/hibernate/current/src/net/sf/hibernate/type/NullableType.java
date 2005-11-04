//$Id: NullableType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.StringHelper;

/**
 * Superclass of single-column nullable types.
 * @author Gavin King
 */
public abstract class NullableType extends AbstractType {
	
	private static final boolean IS_TRACE_ENABLED; 
	static {
		//cache this, because it was a significant performance cost
		IS_TRACE_ENABLED = LogFactory.getLog( StringHelper.qualifier( Type.class.getName() ) ).isTraceEnabled();
	}
	
	public abstract Object get(ResultSet rs, String name) throws HibernateException, SQLException;
	public abstract void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException;
	public abstract int sqlType();
	public abstract String toString(Object value) throws HibernateException;
	public abstract Object fromStringValue(String xml) throws HibernateException;
	
	public final void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		nullSafeSet(st, value, index);
	}
	
	public final void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		if (value==null) {
			if (IS_TRACE_ENABLED) LogFactory.getLog( getClass() ).trace("binding null to parameter: " + index);
			
			st.setNull( index, sqlType() );
		}
		else {
			if (IS_TRACE_ENABLED) LogFactory.getLog( getClass() ).trace("binding '" + toString(value) + "' to parameter: " + index);
			
			set(st, value, index);
		}
	}
	
	public final Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		return nullSafeGet(rs, names[0]);
	}
	
	public final Object nullSafeGet(ResultSet rs, String[] names) throws HibernateException, SQLException {
		return nullSafeGet(rs, names[0]);
	}
	
	public final Object nullSafeGet(ResultSet rs, String name) throws HibernateException, SQLException {
				
		Object value = get(rs, name);
		if ( value==null || rs.wasNull() ) {
			if (IS_TRACE_ENABLED) LogFactory.getLog( getClass() ).trace("returning null as column: " + name);
			return null;
		}
		else {
			if (IS_TRACE_ENABLED) LogFactory.getLog( getClass() ).trace( "returning '" + toString(value) + "' as column: " + name);
			return value;
		}
	}
	
	public final Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		return nullSafeGet(rs, name);
	}
	
	public final String toString(Object value, SessionFactoryImplementor pc) throws HibernateException {
		return (value==null) ? "null" : toString(value);
	}
	
	public final Object fromString(String xml) throws HibernateException {
		return ( xml==null || xml.length()==0 ) ? null : fromStringValue(xml);
	}
	
	public final int getColumnSpan(Mapping session) {
		return 1;
	}
	
	public final int[] sqlTypes(Mapping session) {
		return new int[] { sqlType() };
	}
	
	public abstract Object deepCopyNotNull(Object value) throws HibernateException;
	
	public final Object deepCopy(Object value) throws HibernateException {
		return (value==null) ? null : deepCopyNotNull(value);
	}
	
}





