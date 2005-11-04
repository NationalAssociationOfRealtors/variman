//$Id: Type.java,v 1.15 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Defines a mapping from a Java type to an JDBC datatype. This interface is intended to
 * be implemented by applications that need custom types.<br>
 * <br>
 * Implementors should usually be immutable and <b>must</b> certainly be threadsafe.
 * Implementors must override <tt>equals()</tt>.
 * @author Gavin King
 */
public interface Type extends Serializable {
	
	/**
	 * Return true if the implementation is castable to
	 * <tt>AssociationType</tt>. This does not necessarily imply that
	 * the type actually represents an association.
	 * @see AssociationType
	 * @return boolean
	 */
	public boolean isAssociationType();
	/**
	 * Is this type a collection type.
	 */
	public boolean isPersistentCollectionType();
	
	/**
	 * Is this type a component type. If so, the implementation
	 * must be castable to <tt>AbstractComponentType</tt>. A component
	 * type may own collections or associations and hence must provide
	 * certain extra functionality.
	 * @see AbstractComponentType
	 * @return boolean
	 */
	public boolean isComponentType();
	
	/**
	 * Is this type an entity type?
	 * @return boolean
	 */
	public boolean isEntityType();
	
	/**
	 * Is this an "object" type.
	 *
	 * I.e. a reference to a persistent entity
	 * that is not modelled as a (foreign key) association.
	 */
	public boolean isObjectType();
	
	/**
	 * Return the SQL type codes for the columns mapped by this type. The codes
	 * are defined on <tt>java.sql.Types</tt>.
	 * @see java.sql.Types
	 * @return the typecodes
	 * @throws MappingException
	 */
	public int[] sqlTypes(Mapping mapping) throws MappingException;
	
	/**
	 * How many columns are used to persist this type.
	 */
	public int getColumnSpan(Mapping mapping) throws MappingException;
	
	/**
	 * The class returned by <tt>nullSafeGet()</tt> methods. This is used to establish 
	 * the class of an array of this type.
	 *
	 * @return Class
	 */
	public Class getReturnedClass();
	
	/**
	 * Compare two instances of the class mapped by this type for persistence
	 * "equality", ie. Equality of persistent state.
	 *
	 * @param x
	 * @param y
	 * @return boolean
	 * @throws HibernateException
	 */
	public boolean equals(Object x, Object y) throws HibernateException;
	
	/**
	 * Should the parent be considered dirty, given both the old and current field or element value?
	 * @param old the old value
	 * @param current the current value
	 * @param session
	 * @return true if the field is dirty
	 */
	public boolean isDirty(Object old, Object current, SessionImplementor session) throws HibernateException;
	/**
	 * Has the parent object been modified, compared to the current database state?
	 * @param oldHydratedState the database state, in a "hydrated" form, with identifiers unresolved
	 * @param currentState the current state of the object
	 * @param session
	 * @return true if the field has been modified
	 */
	public boolean isModified(Object oldHydratedState, Object currentState, SessionImplementor session) throws HibernateException;
	
	/**
	 * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
	 * should handle possibility of null values.
	 *
	 * @see Type#hydrate(ResultSet, String[], SessionImplementor, Object) alternative, 2-phase property initialization
	 * @param rs
	 * @param names the column names
	 * @param session
	 * @param owner the parent entity
	 * @return Object
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException;
	
	/**
	 * Retrieve an instance of the mapped class from a JDBC resultset. Implementations
	 * should handle possibility of null values. This method might be called if the
	 * type is known to be a single-column type.
	 *
	 * @param rs
	 * @param name the column name
	 * @param session
	 * @param owner the parent entity
	 * @return Object
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) throws HibernateException, SQLException;
	
	/**
	 * Write an instance of the mapped class to a prepared statement. Implementors
	 * should handle possibility of null values. A multi-column type should be written
	 * to parameters starting from <tt>index</tt>.
	 *
	 * @param st
	 * @param value the object to write
	 * @param index statement parameter index
	 * @param session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException;
	
	/**
	 * A representation of the value to be embedded in an XML element.
	 *
	 * @param value
	 * @param factory
	 * @return String
	 * @throws HibernateException
	 */
	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException;
	
	/**
	 * Parse the XML representation of an instance.
	 *
	 * @param xml
	 * @param factory
	 * @return an instance of the type
	 * @throws HibernateException
	 */
	public Object fromString(String xml) throws HibernateException;
	
	/**
	 * Returns the abbreviated name of the type.
	 *
	 * @return String the Hibernate type name
	 */
	public String getName();
	
	/**
	 * Return a deep copy of the persistent state, stopping at entities and at
	 * collections.
	 *
	 * @param value generally a collection element or entity field
	 * @return Object a copy
	 */
	public Object deepCopy(Object value) throws HibernateException;
	
	/**
	 * Are objects of this type mutable. (With respect to the referencing object ...
	 * entities and collections are considered immutable because they manage their
	 * own internal state.)
	 *
	 * @return boolean
	 */
	public boolean isMutable();
	
	/**
	 * Return a cacheable "disassembled" representation of the object.
	 * @param value the value to cache
	 * @param session the session
	 * @return the disassembled, deep cloned state
	 */
	public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException;
	
	/**
	 * Reconstruct the object from its cached "disassembled" state.
	 * @param cached the disassembled state from the cache
	 * @param session the session
	 * @param owner the parent entity object
	 * @return the the object
	 */
	public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException;
	
	/**
	 * Does this type implement a well-behaved <tt>equals()</tt> method.
	 * (ie. one that is consistent with <tt>Type.equals()</tt>.) Strictly,
	 * if this method returns <tt>true</tt> then <tt>x.equals(y)</tt> implies
	 * <tt>Type.equals(x, y)</tt> and also <tt>Type.equals(x, y)</tt> implies
	 * that <b>probably</b> <tt>x.equals(y)</tt>
	 * 
	 * @deprecated we are not using this anymore
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see Type#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean hasNiceEquals();
	
	/**
	 * Retrieve an instance of the mapped class, or the identifier of an entity or collection, from a JDBC resultset.
	 * This is useful for 2-phase property initialization - the second phase is a call to <tt>resolveIdentifier()</tt>.
	 * @see Type#resolveIdentifier(Object, SessionImplementor, Object)
	 * @param rs
	 * @param names the column names
	 * @param session the session
	 * @param owner the parent entity
	 * @return Object an identifier or actual value
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public Object hydrate(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException;
	
	/**
	 * Map identifiers to entities or collections. This is the second phase of 2-phase property initialization.
	 * @see Type#hydrate(ResultSet, String[], SessionImplementor, Object)
	 * @param value an identifier or value returned by <tt>hydrate()</tt>
	 * @param owner the parent entity
	 * @param session the session
	 * @return the given value, or the value associated with the identifier
	 * @throws HibernateException
	 */
	public Object resolveIdentifier(Object value, SessionImplementor session, Object owner) throws HibernateException;
	
	public Object copy(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException;
}






