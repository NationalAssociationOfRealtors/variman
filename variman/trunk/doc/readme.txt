			   Rex RETS Server
			   ===============

Rex is a RETS 1.0 and 1.5 compliant server. Rex is developed under an
Open Source license (see LICENSE.TXT) at the Center for REALTOR(R)
technology.

Running Rex on Windows
======================

The Windows installer binary version of Rex requires the following:

  - Windows NT, 2000, or XP
  - Java Runtime Environment (JRE) or Java Development Kit (JDK)
    version 1.3 or higher

Rex is installed as a Windows service.  It is started and stopped
using the standard Services control panel.

Running Rex on Other Platforms
==============================

The tar+gz binary version of Rex requires only a JRE or JDK, version
1.3 or higher.  To start Rex:

  % cd <Rex installation directory>
  % java -jar rex.jar

Running Rex as a Web Application
================================

The web application version of Rex requires a JRE or JDK, version 1.3
or higher and a servlet container, such as Tomcat, Resin, or WebLogic.
Configure the "webapp" directory as a web application inside your
servlet container.

Administration
==============

Rex comes both with a GUI and a command line administration tool.  The
GUI tool is based on wx4j[1], and thus requires a special native
library.  Currently only Linux i386 and Windows are supported.  All
other platforms must use the command line utility.  To start the
administration tool:

  % cd <Rex installation directory>
  % java -jar rex-admin.jar

This will try the GUI version first, and fall back to the command
line, if the GUI fails to start.

In order to use the GUI on Windows, the DLLs should be in the path.
The current directory is automatically included in the path, so just
running as above should work.

In order to use the GUI on Linux, the shared libraries should be in
the library path.  This may be setup in /etc/ld.so.conf, or you can
also use the LD_LIBRARY_PATH environment variable.  To set this on the
command line, you could use the following commands:

  % cd <Rex installation directory>
  % LD_LIBRARY_PATH=. java -jar rex-admin.jar

To use the command line version, enter subcommands on the command
line.  To see a list of subcommands:

  % cd <Rex installation directory>
  % java -jar rex-admin.jar help

First Time Usage
================

Rex uses a database for storage of all user information.  You must
first configure your database connection information.  This can be
done via the GUI, or by editing this file:

  webapp/WEB-INF/rex/rets-config.xml

You can use rets-config.xml.dist as a template for this file.

Once you are able to connect to your database, you must use the
administration tool to create the schema.  Then, you can add, edit,
and remove users.

You also need to create a metadata mapping to the data you want to
have access to.  A sample metadata directory structure is provided on
the Rex website.  You can use this is a starting point for your
metadata mapping.

Resources
=========

Rex Website:

  http://www.crt.realtors.org/projects/rets/rex/

Support mailing list:

  http://www.crt.realtors.org/mailman/listinfo/rex-users
