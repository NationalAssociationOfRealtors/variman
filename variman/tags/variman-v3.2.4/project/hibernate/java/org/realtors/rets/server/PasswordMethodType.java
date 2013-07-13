/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.io.Serializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.apache.commons.lang.StringUtils;

/**
 * Converts a database string in the form {@code method + ":" + options} into
 * a {@link PasswordMethod} object and vice versa.
 */
public class PasswordMethodType implements Serializable, UserType
{
    private static final long serialVersionUID = -1948749150730071046L;

	public int[] sqlTypes()
    {
        return TYPES;
    }

    public Class<PasswordMethod> returnedClass()
    {
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

    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable)value;
    }

    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }

    private static final int[] TYPES = {Types.VARCHAR};
}
