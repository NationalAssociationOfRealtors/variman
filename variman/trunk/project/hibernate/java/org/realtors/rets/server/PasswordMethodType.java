/*
 */
package org.realtors.rets.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import org.apache.commons.lang.StringUtils;

public class PasswordMethodType implements UserType
{
    public int[] sqlTypes()
    {
        System.out.println("In sqlTypes");
        return TYPES;
    }

    public Class returnedClass()
    {
        System.out.println("In returnedClass");
        return PasswordMethod.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (!(x instanceof PasswordMethod) || !(y instanceof PasswordMethod))
        {
            return false;
        }

        PasswordMethod lhs = (PasswordMethod) x;
        PasswordMethod rhs = (PasswordMethod) y;
        return lhs.equals(rhs);
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException
    {
        String combined = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        String[] parsed = StringUtils.split(combined, ":", 2);
        String method = "";
        String options = "";
        if (parsed.length == 1)
        {
            method = parsed[0];
        }
        else if (parsed.length == 2)
        {
            method = parsed[0];
            options = parsed[1];
        }

        return PasswordMethod.getInstance(method, options);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException
    {
        String id;
        if (value == null)
        {
            id = "";
        }
        else
        {
            id = ((PasswordMethod) value).getId();
        }
        Hibernate.STRING.nullSafeSet(st, id, index);
    }

    public Object deepCopy(Object value) throws HibernateException
    {
        if (!(value instanceof PasswordMethod))
        {
            return null;
        }
        return ((PasswordMethod) value).deepCopy();
    }

    public boolean isMutable()
    {
        return false;
    }

    private static final int[] TYPES = {Types.VARCHAR};
}
