/*
 * Created on Jul 28, 2003
 *
 */
package org.realtors.rets.server.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Table;

/**
 * @author kgarner
 */
public class CreateSchema extends RetsHelpers
{
    public CreateSchema()
        throws HibernateException
    {
        super();
        mLineSeperator = System.getProperty("line.separator");
        mTypeMappings = new HashMap();
        loadTypeMapping();
    }
    
    private void loadTypeMapping()
    {
        Properties props = new Properties();
        try
        {
            InputStream is =
                getClass().getResourceAsStream("retstypemappins.properties");
            if (is != null)
            {
                props.load(is);
            }
        }
        catch (IOException e)
        {
            LOG.warn("Error loading retstypemappings.properties", e);
        }

        mTypeMappings.put("boolean",
                          props.getProperty("rets.db.boolean", "BOOL"));
        mTypeMappings.put("character",
                          props.getProperty("rets.db.character", "VARCHAR"));
        mTypeMappings.put("date", props.getProperty("rets.db.date", "DATE"));
        mTypeMappings.put("datetime",
                          props.getProperty("rets.db.datetime", "TIMESTAMP"));
        mTypeMappings.put("time",
                          props.getProperty("rets.db.time", "TIME"));
        mTypeMappings.put("tiny", props.getProperty("rets.db.tiny", "INT2"));
        mTypeMappings.put("small", props.getProperty("rets.db.small", "INT2"));
        mTypeMappings.put("int", props.getProperty("rets.db.int", "INT4"));
        mTypeMappings.put("long", props.getProperty("rets.db.long", "INT8"));
        mTypeMappings.put("decimal",
                          props.getProperty("rets.db.decimal", "NUMERIC"));
    }

    public String createTables()
    {
        Iterator i = mClasses.values().iterator();
        StringBuffer sb = new StringBuffer();
        while (i.hasNext())
        {
            MClass clazz = (MClass) i.next();
            sb.append("CREATE TABLE ").append(clazz.getClassName());
            sb.append(" (").append(mLineSeperator);
            Iterator j = clazz.getTables().iterator();
            while (j.hasNext())
            {
                Table table = (Table) j.next();
                sb.append("\t").append(table.getDbName()).append(" ");
                switch (table.getDataType().toInt())
                {
                    case 0:
                        sb.append(mTypeMappings.get("boolean"));
                        break;
                    case 1:
                        sb.append(mTypeMappings.get("character")).append("(");
                        sb.append(table.getMaximumLength()).append(")");
                        break;
                    case 2:
                        sb.append(mTypeMappings.get("date"));
                        break;
                    case 3:
                        sb.append(mTypeMappings.get("datetime"));
                        break;
                    case 4:
                        sb.append(mTypeMappings.get("time"));
                        break;
                    case 5:
                        sb.append(mTypeMappings.get("tiny"));
                        break;
                    case 6:
                        sb.append(mTypeMappings.get("small"));
                        break;
                    case 7:
                        sb.append(mTypeMappings.get("int"));
                        break;
                    case 8:
                        sb.append(mTypeMappings.get("long"));
                        break;
                    case 9:
                        sb.append(mTypeMappings.get("decimal"));
                        break;
                }
                if (j.hasNext())
                {
                    sb.append(",");
                }
                sb.append(mLineSeperator);
            }
            sb.append(");").append(mLineSeperator);
        }

        return sb.toString();
    }

    public static void main(String[] args) throws HibernateException
    {
        CreateSchema cs = new CreateSchema();
        cs.loadMetadata();
        String schema = cs.createTables();
        System.out.print(schema);
    }

    private Map mTypeMappings;
    private String mLineSeperator;
    private static Logger LOG = Logger.getLogger(CreateSchema.class);
}