[#ftl /]
[#-- $Id $ --]
<?xml version="1.0" encoding="UTF-8"?>
[#assign versions = {} /]
[#list antproj.getReference("project.classpath").iterator() as fileResource]
    [#assign filename = fileResource.name /]
    [#assign filename = filename?replace(".jar", "") /]
    [#assign idx = filename?last_index_of("-") /]
    [#if idx gt -1]
        [#assign key = filename?substring(0, idx) /]
        [#assign version = filename?replace(key + "-", "") /]
        [#assign versions = versions + {key:version} /]
    [/#if]
[/#list]
<classpath>
    <classpathentry kind="src" path="src"/>
    <classpathentry kind="src" path="test/src"/>
    <classpathentry kind="src" path="variman-2.1.11/build/hibernate/generated"/>
    <classpathentry kind="src" path="variman-2.1.11/project/admin/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/admin/test/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/datagen/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/embedded/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/hibernate/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/hibernate/test/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/importer/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/webapp/java"/>
    <classpathentry kind="src" path="variman-2.1.11/project/webapp/test/java"/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/antlr/antlr.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Antlr\antlr-2.7.5"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/hibernate/lib/ant-1.5.3.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/apple-java-extensions/AppleJavaExtensions.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/tomcat/bootstrap.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/tomcat/server/lib/catalina.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/commons-cli/commons-cli.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-cli-1.0-src\src\java"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/commons-codec/commons-codec.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-codec-1.2\src\java"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/hibernate/lib/commons-dbcp-${versions["commons-dbcp"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-dbcp-${versions["commons-dbcp"]}\src\java"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/commons-lang/commons-lang.jar" sourcepath="S:/software/DevAppsAndTools/Java/ThirdPartyProjects/Source/Apache/Commons/commons-lang-2.1/src/java"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/commons-logging-${versions["commons-logging"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-logging-${versions["commons-logging"]}-src\src\java"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/hibernate/lib/commons-pool-1.2.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-pool-${versions["commons-pool"]}-src\src\java"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/datedFileAppender-${versions["datedFileAppender"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Logging\datedFileAppender-${versions["datedFileAppender"]}\src"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/hibernate/hibernate.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Hibernate\hibernate-2.5\src"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/hibernate3.jar"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/jdbcappender-${versions["jdbcappender"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Logging\jdbcappender-${versions["jdbcappender"]}\src"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/jdom/jdom.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/jtds/jtds.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/log4j/log4j.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Logging\logging-log4j-1.2.8\src\java"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/mssqljdbc-1.2.jar"/>
    <classpathentry kind="lib" path="web/WEB-INF/lib/rg-core-${versions["rg-core"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\InternalProjects\RealGoCore\rg-core-${versions["rg-core"]}-sources.jar"/>
    <classpathentry kind="lib" path="lib/main/java/servlet-api-2.4.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/spring/spring.jar" sourcepath="S:/software/DevAppsAndTools/Java/ThirdPartyProjects/Source/SpringFramework/spring-framework-1.2.8/src"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/commons-httpclient/commons-httpclient.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-httpclient-2.0.2\src\java"/>
    <classpathentry kind="lib" path="lib/test/java/commons-io-${versions["commons-io"]}.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Apache\Commons\commons-io-${versions["commons-io"]}-src\src\java"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/httpunit/httpunit.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/junit/junit.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\junit\junit-3.8.1-sources.jar"/>
    <classpathentry kind="lib" path="variman-2.1.11/vendor/rets-client/rets-client.jar" sourcepath="S:\software\DevAppsAndTools\Java\ThirdPartyProjects\Source\Realtor\RETS\rets-client-1.0.2.6-sources.jar"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
