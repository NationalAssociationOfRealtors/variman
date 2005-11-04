//$Id: InExpression.java,v 1.9 2004/06/04 05:43:46 steveebersole Exp $
package net.sf.hibernate.expression;

import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.TypedValue;
import net.sf.hibernate.util.StringHelper;

/**
 * Constrains the property to a specified list of values
 * @author Gavin King
 */
public class InExpression extends AbstractCriterion {

	private final String propertyName;
	private final Object[] values;
	
	InExpression(String propertyName, Object[] values) {
		this.propertyName = propertyName;
		this.values = values;
	}

	public String toSqlString(SessionFactoryImplementor sessionFactory, Class persistentClass, String alias, Map aliasClasses) 
	throws HibernateException {
		String params;
		if ( values.length>0 ) {
			params = StringHelper.repeat( "?, ", values.length-1 );
			params += "?";
		}
		else {
			params = StringHelper.EMPTY_STRING;
		}
		String condition = " in (" + params + ')';
		return StringHelper.join(
			" and ", 
			StringHelper.suffix(
				getColumns(sessionFactory, persistentClass, propertyName, alias, aliasClasses),
				condition
			)
		);
		
		//TODO: get SQL rendering out of this package!
	}
	
	public TypedValue[] getTypedValues(SessionFactoryImplementor sessionFactory, Class persistentClass, Map aliasClasses) throws HibernateException {
		TypedValue[] tvs = new TypedValue[ values.length ];
		for ( int i=0; i<tvs.length; i++ ) {
			tvs[i] = getTypedValue( sessionFactory, persistentClass, propertyName, values[i], aliasClasses );
		}
		return tvs;
	}

	public String toString() {
		return propertyName + " in (" + StringHelper.toString(values) + ')';
	}
	
}
