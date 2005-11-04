@echo off

rem -------------------------------------------------------------------
rem Execute SchemaExport tool
rem -------------------------------------------------------------------

set JDBC_DRIVER=C:\Progra~1\SQLLIB\java\db2java.zip;C:\mm.mysql-2.0.14\mm.mysql-2.0.14-bin.jar
set HIBERNATE_HOME=..
set LIB=%HIBERNATE_HOME%\lib
set PROPS=%HIBERNATE_HOME%\src
set CP=%JDBC_DRIVER%;%PROPS%;%HIBERNATE_HOME%\hibernate2.jar;%LIB%\commons-logging-1.0.3.jar;%LIB%\commons-collections-2.1.jar;%LIB%\commons-lang-1.0.1.jar;%LIB%\cglib-2.0-rc2.jar;%LIB%\dom4j-1.4.jar;%LIB%\odmg-3.0.jar;%LIB%\xml-apis.jar;%LIB%\xerces-2.4.0.jar;%LIB%\xalan-2.4.0.jar

java -cp %CP% net.sf.hibernate.tool.hbm2ddl.SchemaExport %*
