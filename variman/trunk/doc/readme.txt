			 Variman RETS Server
			 ===================

Variman is a RETS 1.0, 1.5, 1.7 and 1.7.2  compliant server. Variman 
is developed under an Open Source license (see LICENSE.TXT) at the 
Center for REALTOR(R) technology.

Running Variman on Windows
==========================

The Windows installer binary version of Variman requires the
following:

  - Windows NT, 2000, or XP
  - Java Runtime Environment (JRE) or Java Development Kit (JDK)
    version 1.5 or higher

Variman is installed as a Windows service.  It is started and stopped
using the standard Services control panel.

Running Variman on Other Platforms
==================================

The tar+gz binary version of Variman requires only a JRE or JDK,
version 1.5 or higher.  To start Variman:

  % cd <Variman installation directory>
  % java -jar variman.jar

Running Variman as a Web Application
====================================

The web application version of Variman requires a JRE or JDK, version
1.5 or higher and a servlet container, such as Tomcat, Resin, or
WebLogic.  Configure the "variman" directory as a web application
inside your servlet container.

Activation
==========
Variman must be activated in order to run beyond an hour. Please visit
http://www.crt.realtors.org/projects/rets/variman/support/activation.php
to get an activation code.

Administration
==============

Variman comes both with a GUI and a command line administration tool.
The GUI tool is Swing based, so it will run in any Java environment
with a GUI.  Environments without a GUI, such as a server environment
may use the command line tools, or use something like X-Windows port
forwarding.  To start the administration tool:

  % cd <Variman installation directory>
  % java -jar variman-admin.jar

This will try the GUI version first, and fall back to the command
line, if the GUI fails to start. The metadata editor is only available
in GUI mode.

In order to use the GUI on Windows, the DLLs should be in the path.
The current directory is automatically included in the path, so just
running as above should work.

In order to use the GUI on Linux, the shared libraries should be in
the library path.  This may be setup in /etc/ld.so.conf, or you can
also use the LD_LIBRARY_PATH environment variable.  To set this on the
command line, you could use the following commands:

  % cd <Variman installation directory>
  % LD_LIBRARY_PATH=. java -jar variman-admin.jar

To use the command line version, enter subcommands on the command
line.  To see a list of subcommands:

  % cd <Variman installation directory>
  % java -jar variman-admin.jar help

First Time Usage
================

Variman uses a database for storage of all user information.  You must
first configure your database connection information.  This can be
done via the GUI, or by editing this file:

  variman/WEB-INF/rets/rets-config.xml

You can use rets-config.xml.dist as a template for this file.

Once you are able to connect to your database, you must use the
administration tool to create the schema.  Then, you can add, edit,
and remove users.

You also need to create a metadata mapping to the data you want to
have access to.  A sample metadata directory structure is provided on
the Variman website.  You can use this is a starting point for your
metadata mapping.

There is also a metadata editor that as part of the GUI, as well as
a migration tool to help migrate pre RETS 1.7.2 metadata for existing
users. Also see the readme.metadata file in this directory.

Resources
=========

Variman Website:

  http://www.crt.realtors.org/projects/rets/variman/

Support mailing list:

  http://www.crt.realtors.org/mailman/listinfo/variman-users
