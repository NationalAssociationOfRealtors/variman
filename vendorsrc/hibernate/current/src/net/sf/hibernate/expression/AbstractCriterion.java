//$Id: AbstractCriterion.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.expression;

import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.TypedValue;
import net.sf.hibernate.persister.Queryable;
import net.sf.hibernate.util.StringHelper;

/**
 * Base class for <tt>Criterion</tt> implementations
 * @author Gavin King
 */
public abstract class AbstractCriterion implements Criterion {

	/**
	 * For cosmetic purposes only!
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

	protected static String[] getColumns(SessionFactoryImplementor sessionFactory, Class persistentClass, String property, String alias, Map aliasClasses) throws HibernateException {
		if ( property.indexOf('.')>0 ) {
			String root = StringHelper.root(property);
			Class clazz = (Class) aliasClasses.get(root);
			if (clazz!=null) {
				persistentClass = clazz;
				alias = root;
				property = property.substring( root.length()+1 );
			}
		}
		return ( (Queryable) sessionFactory.getPersister(persistentClass) ).toColumns(alias, property);
	}

	protected static TypedValue getTypedValue(SessionFactoryImplementor sessionFactory, Class persistentClass, String propertyName, Object value, Map aliasClasses) throws HibernateException {
		if ( propertyName.indexOf('.')>0 ) {
			String root = StringHelper.root(propertyName);
			Class clazz = (Class) aliasClasses.get(root);
			if (clazz!=null) {
				persistentClass = clazz;
				propertyName = propertyName.substring( root.length()+1 );
			}
		}
		return new TypedValue( ( (Queryable) sessionFactory.getPersister(persistentClass) ).toType(propertyName), value );
	}

}
