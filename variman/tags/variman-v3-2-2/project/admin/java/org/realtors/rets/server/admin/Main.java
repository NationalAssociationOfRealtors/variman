/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.PrintStream;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.realtors.rets.server.HibernateUtils;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            initLog4J();
            Admin.initSystemProperties();
            if (args.length == 0)
            {
                SwingMain.main(args);
            }
            else
            {
                cliMain(args);
            }
        }
        catch (Throwable t)
        {
            LOG.error("Caught exception", t);
        }
    }

    private static void initLog4J()
    {
        ClassLoader contextClassLoader =
            Thread.currentThread().getContextClassLoader();
        DOMConfigurator.configure(
            contextClassLoader.getResource(
                Admin.PROJECT_NAME + "-admin-log4j.xml"));
    }

    private static void cliMain(String[] args)
        throws Exception
    {
        if (args.length == 0)
        {
            printHelp(System.out);
            return;
        }

        String subCommand = args[0];
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        if (subCommand.equals("show-users"))
        {
            showUsers(subArgs);
        }
        else if (subCommand.equals("add-user"))
        {
            addUser(subArgs);
        }
        else if (subCommand.equals("remove-user"))
        {
            removeUser(subArgs);
        }
        else if (subCommand.equals("create-schema"))
        {
            createSchema(subArgs);
        }
        else if (subCommand.equals("create-data-schema"))
        {
            createDataSchema(subArgs);
        }
        else if (subCommand.equals("create-properties"))
        {
            createProperties(subArgs);
        }
        else if (subCommand.equals("help"))
        {
            printHelp(System.out);
        }
        else
        {
            System.out.println("Unknown subcommand: " + subCommand);
            System.out.println();
            printHelp(System.out);
        }
    }

    private static void printHelp(PrintStream out)
    {
        out.println(
            "Subcommands are:\n" +
            "  show-users               Shows all users\n" +
            "  add-user                 Adds a new user\n" +
            "  remove-user              Removes a user\n" +
            "  create-schema            Creates database schema\n" +
            "  create-data-schema       " +
            "Create database schema from metadata\n" +
            "  create-properties        Create dummy property data\n" +
            "  help                     Print this summary\n");
    }

    private static void showUsers(String[] args)
        throws RetsServerException, HibernateException
    {
        if (args.length != 0)
        {
            System.err.println("Usage: show-users");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        List users = UserUtils.findAll();
        for (int i = 0; i < users.size(); i++)
        {
            User user = (User) users.get(i);
            System.out.println(user.getName() + " (" + user.getUsername() +
                               ")");
        }
    }

    private static void addUser(String[] args)
        throws Exception
    {
        if (args.length != 4)
        {
            System.err.println("Usage: add-user <username> <firstname> " +
                               "<lastname> <password>");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        User user = new User();
        user.setUsername(args[0]);
        user.setFirstName(args[1]);
        user.setLastName(args[2]);
        user.changePassword(args[3]);
        HibernateUtils.save(user);
        System.out.println("Added user " + user.getUsername());
    }

    private static void removeUser(String[] args)
        throws Exception
    {
        if (args.length != 1)
        {
            System.err.println("Usage: remove-user <username>");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        String userName = args[0];
        User user = UserUtils.findByUsername(userName);
        if (user == null)
        {
            System.out.println("User not found: " + userName);
            return;
        }
        UserUtils.delete(user);
        System.out.println("Removed user " + userName);
    }

    private static void createSchema(String[] args)
        throws Exception
    {
        if (args.length != 0)
        {
            System.err.println("Usage: create-schema");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        Configuration config = Admin.getHibernateConfiguration();
        System.out.println("Creating schema (this will take a bit...)");
        new SchemaExport(config).create(false, true);
    }

    private static void createDataSchema(String[] args)
        throws Exception
    {
        if (args.length != 0)
        {
            System.err.println("Usage: create-schema");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        new CreateDataSchemaCommand(Admin.getRetsConfig()).execute();
    }

    private static void createProperties(String[] args)
        throws Exception
    {
        if (args.length != 1)
        {
            System.err.println("Usage: create-properties <num-properties>");
            return;
        }
        AdminUtils.initConfig();
        AdminUtils.initDatabase();

        int numProperties = NumberUtils.toInt(args[0]);
        new CreatePropertiesCommand(numProperties).execute();
        System.out.println("Created " + numProperties + " properties");
    }

    private static final Logger LOG =
        Logger.getLogger(Main.class);
}
