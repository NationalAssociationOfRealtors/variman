//$Id: RelationalModel.java,v 1.7 2004/06/04 01:27:42 steveebersole Exp $
package net.sf.hibernate.mapping;

import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.dialect.Dialect;

/**
 * A relational object which may be created using DDL
 * @author Gavin King
 */
public interface RelationalModel {
	public String sqlCreateString(Dialect dialect, Mapping p) throws HibernateException;
	public String sqlDropString(Dialect dialect);
}







