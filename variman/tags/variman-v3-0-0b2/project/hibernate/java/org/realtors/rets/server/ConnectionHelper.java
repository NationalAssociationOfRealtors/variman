package org.realtors.rets.server;

import java.sql.Connection;

import org.apache.log4j.Logger;

public interface ConnectionHelper 
{
    public Connection getConnection() throws RetsServerException;
    public Connection getConnectionTransaction() throws RetsServerException;
    public void commit() throws RetsServerException;
    public void rollback() throws RetsServerException;
    public void rollback(Logger log);
	public void close(Logger log);
}
