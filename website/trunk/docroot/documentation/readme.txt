			   Rex RETS Server
			   ===============

Rex is a RETS 1.0 and 1.5 compliant server. Rex is developed under an
Open Source license at the Center for REALTOR(R) technology.

Running Rex on Windows
======================

The Windows installer binary version of Rex requires the following:

  - Windows NT, 2000, or XP
  - Java Runtime Environment (JRE) or Java Development Kit (JDK)
    version 1.4 or higher

Rex is installed as a Windows service.  It is started and stopped
using the standard Services control panel.

Running Rex on Other Platforms
==============================

The tar+gz binary version of Rex requires only a JRE or JDK, version
1.4 or higher.  To start Rex:

  % cd <Rex installation directory>
  % java -jar rex.jar

Running Rex as a Web Application
================================

The web application version of Rex requires a JRE or JDK, version 1.4
or higher and a servlet container, such as Tomcat, Resin, or WebLogic.
Configure the "webapp" directory as a web application inside your
servlet container.

