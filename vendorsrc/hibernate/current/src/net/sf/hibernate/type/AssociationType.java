//$Id: AssociationType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.persister.Joinable;

/**
 * A type that represents some kind of association between entities.
 * @see net.sf.hibernate.engine.Cascades
 * @author Gavin King
 */
public interface AssociationType extends Type {
	
	/**
	 * Get the foreign key directionality of this association
	 */
	public ForeignKeyDirection getForeignKeyDirection();
	
	//TODO: move these to a new JoinableType abstract class, 
	//extended by EntityType and PersistentCollectionType:
	
	/**
	 * Is the foreign key the primary key of the table?
	 */
	public boolean usePrimaryKeyAsForeignKey();
	
	/**
	 * Get the "persister" for this association - a class or 
	 * collection persister
	 */
	public Joinable getJoinable(SessionFactoryImplementor factory) throws MappingException;
	
	/**
	 * Get the columns referenced by this association.
	 */
	public String[] getReferencedColumns(SessionFactoryImplementor factory) throws MappingException;
	
	public Class getAssociatedClass(SessionFactoryImplementor factory) throws MappingException;
}






