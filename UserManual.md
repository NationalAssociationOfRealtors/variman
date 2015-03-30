# Variman User's Manual #

'''Table of Contents'''

## 1. Introduction ##

Variman is a RETS [[[#bib-rets|7]]] 1.5, 1.7 and 1.7.2 compliant server. Variman is developed under a BSD-style license at the Center for REALTOR(R) Technology [[[#bib-crt|1]]]. The latest version of Variman and its manual may always be found at the Variman web site [[[#bib-variman|8]]].

## 2. Installation ##

There are a number of installation options for Variman. The easiest method is to us one of the standalone versions that use an embedded version of Apache Tomcat [[[#bib-tomcat|2]]]. The only other requirements to run this version is Java [[[#bib-java|3]]] 5.0 or higher and a relational database. However, Variman is developed as a Java web application, so you can use your own application server, such as WebLogic, WebSphere, or Resin, if you already have licenses for one.

### 2.1. Windows Installation ###

If you are running on Windows, a normal Windows installer is provided that installs into the Program Files directory. You need to have Java installed prior to installing Variman. The installer also adds items into the Start Menu and a Windows Service. The Service can be used to start and stop the server, like any other Windows Service. Before running Variman, you should first configure it. There are a number of items that must be setup.

### 2.2. Non-Windows Installation ###

For other platforms, such as Linux and Mac OS X, a gzipped tar file is provided. This archive does not contain any support for init scripts or startup items. The Variman server is distributed as an executable Jar file.

### 2.3. Web Application Installation ###

A version of Variman without Tomcat is available as a zip file or gzipped tar file. This distribution includes only the web application and the administration tool. An application server is required to run the web application. However, only Java Servlets are used, so you do not need any other J2EE technology, like EJB.

Depending on your application server, you will need to pick up the WEB-INF directory from the tar or zip file and place it in the appropriate place.

## 3. Configuration ##

This chapter describes how to configure and run Variman.

### 3.1. First Time Configuration ###

Before running Variman for the first time, there are a few steps you need to take. Variman requires access to a database. Currently, Variman supports PostgreSQL [[[#bib-postgres|6]]] and Microsoft SQL Server. This database is where RETS data is stored, as well as Variman specific information. Variman requires read/write access to the database, as well as the ability to create new tables.

#### 3.1.1. Variman Setup ####

Beneath the WEB-INF/rets directory you will find two files: rets-config.xml.dist and rets-logging.properties.dist. You will need to either rename or copy these to rets-config.xml and rets-logging.properties in the same directory before you can even run the admin tool. You may want to do some hand initiailization with your favorite editor of the rets-logging.properties file to set up a higher logging level until you get the server functional. Once it is functional, set the level back to INFO or the log data will get large quite fast.

#### 3.1.2. PostgreSQL Setup ####

PostgreSQL must be configured to accept TCP/IP connections, as this is how the JDBC driver connects. This is done adding the following setting in `postgresql.conf`:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{ tcpip\_socket = true
> }}} 

You may also have to edit `pg_hba.conf` to make network connections permissible. See the documentation in that file for details.

It is probably a good idea to create a user other than the default `postgres` superuser. This user must have read and write access, and permissions to create new tables in the database used by Variman.

The JDBC driver for Postgres version 8.3.x is included with the distribution.

#### 3.1.3. Microsoft SQL Server Setup ####

First, either choose an existing database, or create a new database for use with Variman. Then, create a new user that has the following role memberships:

  * `public`
  * `db_owner`
  * `db_datareader`
  * `db_datawriter`

The Open Source jTDS [[[#bib-jtds|5]]] driver is included with Variman. The only other supported driver is the commercial JSQLConnect driver from JNetDirect [[[#bib-jnetdirect|4]]]. The driver that Microsoft offers is buggy and does not work with Variman.

#### 3.1.4. Installing a JDBC Driver ####

The easiest way to install your JDBC driver is to use the Administration Tool. Choose the Install Jar command from the File menu. You must then restart the Administration Tool and/or Variman to use the new JDBC driver.

#### 3.1.5. Configuring Database Properties ####

From the Configuration tab of the Administration Tool, the current database properties are listed. You will have change these to suit your environment. Click on the Edit button, to bring up the Database Properties dialog, as in [[#fig-db|Figure 1, “Database Properties Window”]]. You should use the Test Connection button to make sure that the properties are entered correctly. If the test fails, then neither the Administration Tool nor Variman will be able to access your database.

'''Figure 1. Database Properties Window'''

![http://variman.googlecode.com/svn/wiki/db.png](http://variman.googlecode.com/svn/wiki/db.png)

#### 3.1.6. Creating the Schema ####

Variman needs tables for it's own use, such as user information. Before you create users and groups, you need to create these tables by choosing the Create Schema command from the Database menu.

### 3.2. Configuring RETS Parameters ###

From the main Configuration panel, as seen in [[#fig-config|Figure 2, “Configuration Tab”]], you can configure various RETS parameters.

'''Figure 2. Configuration Tab'''

![http://variman.googlecode.com/svn/wiki/config.png](http://variman.googlecode.com/svn/wiki/config.png)

The Listening Port is the TCP port number that the Variman server listens on. 6103 is the standard, well-known port number for RETS. The Metadata Directory points to the root directory of the metadata hierarchy. Configuring metadata is explained in [[#deploy-metadata|Section 4.1, “Configuring Metadata”]].

### 3.3. Configuring GetObject for Multimedia Objects ###

The RETS GetObject transaction allows you to associate multimedia objects with a property listing. These multimedia objects, called ''objects'' from now on, are grouped into ''types''. The most common type of object is a photograph. However, you are not limited to only photos for RETS object types. RETS provides some well-known object types, as described in [[#tab-object-types|Table 1, “Well-Known Object Types”]], but this list is only a starting point. If you have objects that do not fall into any of these well-known types, you may use your own type. You should always one of the well-known types, if possible, so that RETS clients can properly access the objects automatically.

A RETS client may only retrieve objects of the same type in a single transaction. Each transaction must also include at least one listing ID, called the ''resource key'' in RETS terminology. For each resource key, there may be zero or more objects within a type, all numbered sequentially beginning with 1. The object number is called the ''object ID''. A RETS client may request a specific object by ID, a range of objects by ID, or all of the objects for a given resource key. Each object may also include a text description which a RETS client should provide to the user. The object ID 0 is special and designates the ''default object''. For example, some RETS clients may only present the user with a single photo, and the default object allows you to configure which photo is used in these circumstances.

'''Table 1. Well-Known Object Types'''

| Object Type | Description |
|:------------|:------------|
| `Photo` | An image, usually a photograph for the object key. |
| `Thumbnail` | A lower-resolution image. |
| `Plat` | An image of the property boundaries. |
| `Video` | A short video or movie. |
| `Audio` | A sound clip. |
| `Map` | An image showing a map of the property. |
| `VRImage` | A multiple-view, possibly interactive image related to the property. |

The usual behavior is for all objects matching the RETS client's criteria to be sent back to the client in a single GetObject response. For a request that returns a large number of objects, however, this may be impractical. The RETS specification allows the RETS server to return only a URL, instead of the object itself. This URL is called a ''location URL''.

GetObject configuration in Variman is done by the Get Object Root, Photo Pattern, and Object Set Pattern parameters in [[#fig-config|Figure 2, “Configuration Tab”]]. There are three ways to map multimedia objects to a listing ID> The first is using photo patterns, described in [[#deploy-getobject-photo-patterns|Section 3.3.1, “Using Photo Patterns”]] the second is using object sets, described in [[#deploy-getobject-object-sets|Section 3.3.2, “Using Object Sets”]], and the third is to use a combination of the Get Object Root and the contents of a database table to determine the location of the object. This is described in more detail by [[#deploy-getobject-database|Section 3.3.3, “Using A Database Table”]]. Object sets are far more customizable, but require more work to implement.

#### 3.3.1. Using Photo Patterns ####

Photo patterns use pattern substitutions to locate files on the local files system. It uses only the the Get Object Root and Photo Pattern parameters. The Get Object Root is a directory where the photos are located. The Photo Pattern is a percent substitution pattern that describes the format of the actual file names. The possible patterns and their substitutions are listed in [[#tab-photo-pattern|Table 2, “Photo Patterns”]].

'''Table 2. Photo Patterns'''

| Pattern | Substitution |
|:--------|:-------------|
| %k | Resource Key |
| %i | Object ID |
| %I | Object ID, or empty if the ID is 0 |

For example, if you have pictures in `/home/variman/pictures/`. The key is used as the base file name, with each photo given a number. Say there are three pictures for the property ID `LN1001`, the full paths for these pictures are:

  * `/home/variman/pictures/LN1001_1.jpg`
  * `/home/variman/pictures/LN1001_2.jpg`
  * `/home/variman/pictures/LN1001_3.jpg`

> The, you could set the Get Object Root to `/home/variman/pictures` and the Photo Pattern to `%k_%i.jpg`. The `%k` gets replaced with the object ID, and the `%i` gets replaced with the photo number.

The `%k` percent pattern may be proceeded by a number modifier that truncates the number of characters used during substitution. This allows files to be split up into multiple directories for large installations. If the number is positive, the characters are taken from the front, where if the number is negative, the characters are taken from the end. So, with a pattern of `%-2k/%k_%i.jpg`, the first picture for `LN1001`, `LN1002`, `LN1101` would be at the following paths:

  * `/home/variman/pictures/01/LN1001_1.jpg`
  * `/home/variman/pictures/02/LN1002_1.jpg`
  * `/home/variman/pictures/01/LN1101_1.jpg`

Location URLs given for objects that match a photo pattern always point back to Variman. These are specially coded URLs that Variman uses to decode which object to provide to the client.

#### 3.3.2. Using Object Sets ####

Instead of using a pattern to map to the photos themselves, an object set pattern may be used to locate an object set file, which is just an XML file describing all the multimedia files. This approach is more flexible for the following reasons:

  * Object file names do not need to match any pattern. This is useful where object IDs embedded in file names may not be sequential. This often happens when an object is deleted, but the other objects are not renumbered.
  * The default object is configurable. It need not be the first object ID.
  * Types of objects other than photos, such as documents and video can be provided.
  * Text descriptions may be supplied with objects.
  * Objects need not be stored on the local filesystem. An external media server could be used, and Variman will access these objects using HTTP. Location URLs may optionally point directly to the media server.

> As described above, there must be one oject set file per listing ID. The Object Set Pattern field in [[#fig-config|Figure 2, “Configuration Tab”]] is used to locate object sets. Only the `%k` pattern listed in [[#tab-photo-pattern|Table 2, “Photo Patterns”]] is available, and the Get Object Root is still used as the root directory. For example, if the Get Object Root is set to `/home/variman/objects`, and the Object Set Pattern is `%k.xml`, then the file for listing ID `LN1` would be `/home/variman/objects/LN1.xml`.

The default behavior for Variman is to provide location URLs that point back to the RETS server. However, if an external media server exists, Variman may provide URLs directly to the media server. These location URLs to an external media server are called ''remote URLs'', since the URLs point to a remote server, not locally to Variman. This remote URL behavior is optional, though. You can have a media server, and not use remote URLs. In this case, Variman acts as a proxy between the RETS client and the media server. Also note that if location URLs are not requested by the RETS client, Variman also acts as a proxy. There is no way to tell a RETS client to use location URLs exclusively, so there is no way to disable this proxying.

Before getting into the details, here is an example object set file for a hypothetical `LN1` property, `LN1.xml`:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{ <?xml version="1.0" ?>
> <object-set
> > xmlns="http://www.crt.realtors.org/xml/variman/object-set/1"
> > resource-key="LN1"
> > base="http://media.example.com/pictures/"
> > remote-locations="true">
> > 

&lt;object-group type="Photo"&gt;


> > > <object src="LN1-1.jpg"
> > > > description="Beautiful frontal view of home." />

> > > 

&lt;object src="LN1-2.jpg" description=""&gt;


> > > 

&lt;/object&gt;


> > > 

&lt;object default="true" src="LN1-5.jpg" /&gt;


> > > <object src="LN1-6.jpg"
> > > > description="Large wooded lot in backyard." />

> > > 

&lt;object src="LN1-9.jpg" /&gt;


> > > 

&lt;object src="LN1-13.jpg" /&gt;


> > > <object src="LN1-14.jpg"
> > > > description="Large screened in back porch." />

> > > 

&lt;object src="LN1-17.jpg" /&gt;


> > > 

&lt;object src="LN1-22.jpg" /&gt;


> > > 

&lt;object src="LN1-24.jpg" /&gt;



> > 

&lt;/object-group&gt;


> > 

&lt;object-group type="Documents" base="documents/"&gt;


> > > <object src="somedocument.pdf"
> > > > description="Some basic document." />

> > > <object default="true" src="disclosures.pdf"
> > > > description="Disclosure statement from current owners." />

> > > <object src="2002 tax records.rtf"
> > > > description="County tax records for 2002." />

> > 

&lt;/object-group&gt;


> > 

&lt;object-group type="Video" base="other/"&gt;


> > > <object src="LN1\_virtual\_tour.mov"
> > > > description="Take a virtual tour of this house." />

> > 

&lt;/object-group&gt;



> 

Unknown end tag for &lt;/object-set&gt;


> }}} 

The main tag is `<object-set>`. The `resource-key` attribute is required and must match the listing ID. Following the `<object-set>` tag must be one or more `<object-group>` tags. There should be one object group for each type of multimedia object. The `type` attribute must match the `Type` parameter used by the RETS client. Well known types must be used where appropriate. See Section 11.4.1 of the RETS specification for a list of well known types.

##### 3.3.2.1. The 

&lt;object-set&gt;

 Tag #####

The `<object-set>` tag is the top level tag. It has the following attributes:

'''Table 3. 

&lt;object-set&gt;

 Attributes'''

| Attribute | Description | Default |
|:----------|:------------|:--------|
| xmlns | The XML namespace URL. It must be set to `http://www.crt.realtors.org/xml/variman/object-set/1` | Required. |
| resource-key | The resource key, which is usually the listing ID for properties. This must match the listing ID in the file name. | Required. |
| base | The base URL to use for resolving subsequent relative URLs. | A `file://` URL to the location of this object set file. |
| remote-locations | If set to `true`, then Variman will provide a URL directly to the multimedia server, if requested by the client. If set to `false`, all location URLs provided will point back to Variman. If the object does not point to an external media server (i.e. it is a local URL), then a local location URL is used. | `false`. |

##### 3.3.2.2. The 

&lt;object-group&gt;

 Tag #####

The `<object-group>` tag represents a group of multimedia objects of the same type. A RETS client specifies an object type during the GetObject transaction, and object groups map one-to-one to these object types.

'''Table 4. 

&lt;object-group&gt;

 Attributes'''

| Attribute | Description | Default |
|:----------|:------------|:--------|
| type | The type of multimedia object in this group. A well-known object type must be used, if appropriate. RETS well-known types are: `Photo`, `Plat`, `Video`, `Audio`, `Thumbnail`, `Map`, `VRImage`. | Required. |
| base | The base URL to use for resolving relative URLs in child `<object>` tags. | The base URL of the `<object-set>` tag. |

##### 3.3.2.3. The 

&lt;object&gt;

 Tag #####

The `<object>` tag represents a single multimedia object. In terms of RETS, every multimedia type has zero or more individual objects. These objects are numbered sequentially, starting from 1. RETS also has the concept of a default object.

'''Table 5. 

&lt;object&gt;

 Attributes'''

| Attribute | Description | Default |
|:----------|:------------|:--------|
| src | The URL to the actual object. This may be, and usually is, a relative URL, which is resolved against the previous `base` attributes. This works in a very similar way to the `src` attribute of the HTML `<img>` tag. | Required. |
| description | A text description of this object. | No description. |
| default | If set to `true`, use this as the default object. Only one object may be marked as default. If more than one is marked as default, the first one marked will be used. | `false`. |

#### 3.3.3. Using A Database Table ####

The third way to manage the multimedia object uses a combination of the Get Object Root and a path that will be extracted from a database table. The path that is extracted will be appended to the Get Object Root and the resulting path will be used to access the object. This method is disabled by default and requires a manual change by you to the `variman/WEB-INF/spring-appContext.xml ` file.

In order to enable this feature, locate this line in the `spring-appContext.xml` file in your deployment directory:

|<tablestyle="width:90%; background-color:#E0E0E0"> ` <!--bean id="customObjectSet" class="org.realtors.rets.server.protocol.DatabaseObjectSet"/--> ` |
|:----------------------------------------------------------------------------------------------------------------------------------------------------|

> and change it to read:

|<tablestyle="width:90%; background-color:#E0E0E0"> ` <bean id="customObjectSet" class="org.realtors.rets.server.protocol.DatabaseObjectSet"/> ` |
|:-----------------------------------------------------------------------------------------------------------------------------------------------|

When you initialize the database as described in [[#schema|Section 3.1.6, “Creating the Schema”]], a table called `rets_media` is created. This database table consists of the following columns:

'''Table 6. `rets_media` Database Columns'''

| Column Name | Description |
|:------------|:------------|
| `resource_key` | The resource key for a group of objects. Typeically, this should be the same as the `ListingID` for your listings. |
| `object_id` | This is a numeric field that is used to determine the unique object by Listing. |
| `media_type` | This is the well known name for the media type as identified by [[#tab-object-types|Table 1, “Well-Known Object Types”]]. This is used as part of the index into the table and must not be `null`. If you only use one media type, you can alter the column in the database to set it's default value (e.g. "Photo"). |
| `description` | This is a description of the media (e.g. "Bedroom 1"). |
| `resource_file_path` | The path to the file containing the object. This path will be appended to the Get Object Root path to create the final path to the object. |
| `modification_timestamp` | This is a timestamp that should be changed only when the underlying object or description has changed. |
| `id` | This is an integer field that can be used to uniquely identify an object. It must be unique within all objects in this table. |

This table has a composite key as it's primary key. This key is made up of the `resource_id`, `object_id` and `media_type` columns. The combination of the three columns must be unique for each object in the database.

### 3.4. Users ###

Users are managed through the Users Tab as seen in [[#fig-users|Figure 3, “Users Tab”]]. Users may be created, removed, and edited from the User menu or contextual menu. Users may also be added and removed from groups.

'''Figure 3. Users Tab'''

![http://variman.googlecode.com/svn/wiki/[http://variman.googlecode.com/svn/wiki/agentclass.png](http://variman.googlecode.com/svn/wiki/[http://variman.googlecode.com/svn/wiki/agentclass.png)]

### 3.5. Groups ###

Groups are managed through the Groups Tab as seen in [[#fig-groups|Figure 4, “Groups Tab”]] and the Group Dialog, as seen in [[#fig-group-dialog|Figure 5, “Group Dialog”]]. Groups may be created, edited, and removed from the Group menu or contextual menu. The main purpose of groups is to restrict data in a number of ways, described below.

'''Figure 4. Groups Tab'''

![http://variman.googlecode.com/svn/wiki/groups.png](http://variman.googlecode.com/svn/wiki/groups.png)

'''Figure 5. Group Dialog'''

![http://variman.googlecode.com/svn/wiki/group-dialog.png](http://variman.googlecode.com/svn/wiki/group-dialog.png)

#### 3.5.1. Record Limits ####

If a record limit is enabled for a group, members of this group will only be able to retrieve that many records per query, even if more records match the search criteria. This limit always overrides any user supplied limit. If a user is a member of multiple groups with record limits, the lowest limit is taken.

#### 3.5.2. Time Restrictions ####

If a time restriction is enabled for a group, then members of this group are only allowed during the specified time periods. Any access outside the allowed time periods results in an authentication failure. Time restrictions may be setup as inclusive or exclusive. Inclusive restrictions means the user is only allowed to access the server in this time period. They will be denied access outside this period. For example, "Allow between 9:00AM and 5:00PM" means the user is allowed access between 9AM and 5PM and denied access before 9AM and after 5PM. Exclusive restrictions means the user is only allowed to access outside this time period. They will be denied access within this period. For example, "Deny between 9:00AM and 5:00PM" means the user is allowed before 9AM and after 5PM and denied access between 9AM and 5PM. If a user is a member of multiple groups, the restrictions applied to that user is a union of all allowed time periods of all the time restrictions.

#### 3.5.3. Query Count Limits ####

If a query count limit is enabled for a group, then members of this group are only allowed a certain number of search queries in a given time period. A user exceeding their query count limit will receive a RETS error on all subsequent searches until the time period has elapsed. The possible time periods are: per day, per hour, and per minute. For example, "5 queries per minute" means that more than 5 queries per minute result in an error until one minute has elapsed. Then, the user will be allowed at most 5 more queries. If a user is a member of multiple groups, the most restrictive query count is applied

#### 3.5.4. Filter Rules ####

Filter rules allow RETS system names to be filtered out. If none of a users' groups contain any rules, then they have access to all system names provided in the metadata. Rules are written for a particular resource and class. There two types of filter rules: include and exclude. Include rules includes only the fields listed. Exclude rules includes all fields except the ones listed. An example rule is shown in [[#fig-filter-rule|Figure 6, “Filter Rule Dialog”]]. The individual fields are listed with spaces between them. If a user is a member of multiple groups, then the fields allowed to be seen are a union of the fields of each group's set of fields.

'''Figure 6. Filter Rule Dialog'''

![http://variman.googlecode.com/svn/wiki/filter_rule.png](http://variman.googlecode.com/svn/wiki/filter_rule.png)

#### 3.5.5. Condition Rules ####

Condition rules allows the generated SQL to be modified and given extra conditions. For example, there may be a column in the database called `idx_viewable` with values of `Y` or `N`, which determines whether or not this row should be visible to IDX users. To make only the rows which are viewable for IDX available, you would use the SQL constraint `idx_viewable = 'Y'`, as shown in [[#fig-condition-rule|Figure 7, “Condition Rule Dialog”]]. This gets appended on to the `WHERE` clause of the generated SQL using an `AND` statement. If a user is a member of multiple groups that have condition rules, each SQL constraint is appended to the `WHERE` clause in a separate `AND` statement. So one group could restrict access to rows that are IDX viewable, while another could restrict based on price. Condition rules are also tied to a specific resource and class. The SQL constraint must use the SQL column names, not RETS system names. This allows some powerful behavior, because it could use columns in the database that are not mapped to RETS system names.

'''Figure 7. Condition Rule Dialog'''

![http://variman.googlecode.com/svn/wiki/condition_rule.png](http://variman.googlecode.com/svn/wiki/condition_rule.png)

## 4. Metadata ##

This chapter describes how to configure the metadata using the Metadata Editor in the Adminstrative Tool. You may also create metadata by hand as one or more XML files.

### 4.1. Configuring Metadata ###

Metadata describes how to map property data in your relational database to RETS names. The metadata is stored as compact format metadata, split into multiple files or in a single metadata.xml file if creating the metadata using the Metadata Editor in the Administrative Tool. For more information on compact metadata format, see the RETS specification. A sample set of metadata is provided on the Variman website, and can be used as a starting point.

### 4.2. Example Schema ###

For the purposes of this discussion, let's use a very simple sample schema that describes our database. Our database will have a table representing the Agent, a table for Residential Property, and a table for Commercial Property. The SQL to create it is:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           create table agent (
> agent\_id          int unique primary key,
> first\_name        varchar (30),
> last\_name         varchar (30),
> phone             varchar (12),
> modified\_time     timestamp without time zone,
> status            char (2)
> );
> create table res\_prop (
> > listing\_id        int unique primary key,
> > agent\_id          int references agent (agent\_id),
> > street\_name       varchar (80),
> > street\_number     varchar (10),
> > city              varchar (20),
> > state             varchar (2),
> > zip               varchar (10),
> > asking            int,
> > status            char (2),
> > modified\_time     timestamp without time zone,
> > beds              int,
> > baths             int

> );
> create table com\_prop (
> > listing\_id        int unique primary key,
> > agent\_id          int references agent (agent\_id),
> > street\_name       varchar (80),
> > street\_number     varchar (80),
> > city              varchar (20),
> > state             varchar (2),
> > zip               varchar (10),
> > asking            int,
> > status            char (2),
> > modified\_time     timestamp without time zone,
> > sqft              int,
> > offices           int

> );
> }}} 

### 4.3. Create SYSTEM Entry ###

The first step (after configuring and testing data base access as described earlier in this manual), we need to create the `SYSTEM` metadata entry. Select the METADATA tab and you should see:

'''Figure 8. Metadata Tab'''

![http://variman.googlecode.com/svn/wiki/editor.png](http://variman.googlecode.com/svn/wiki/editor.png)

Since metadata is usually represented as a tree hierarchy, the editor will do the same. The left panel in this case shows an empty tree since nothing has yet been added to it. The right panel shows the the current value for the node selected. In this case, it is also empty.

The first thing we want to do is identify the system. Select the System node in the tree and select the Edit button. A dialog box will pop up that allows you to enter text for the `SystemID`, `SystemDescription`, the `Date` and `Version` values that relate to this version of the metadata, the `COMMENTS` and `TimeZoneOffset` for this server. Again, refer to the RETS documentation for the legal values for these fields.

### Note ###

> Beginning with version 3.0 of Variman, for compliance purposes, metadata will be strictly parsed according to the RETS 1.7.2 Specification. This strictness check may be disabled by toggling the Metadata->Strict menu item.

For this example, let's use the following details for our hpothetical MLS:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           SystemID:     CRTDemo
> Description:  CRT Demo
> Date:         2009-05-15T00:00:00Z
> Version:      1.01.00001
> Comments:     Example MLS for Demo Purposes
> TimeZone:     -08:00
> }}} 

> Press the Save button to complete the dialog. This will preserve what you entered and should show up in the details panel whenever the System node in the tree is selected. The metadata is not yet saved on disk. You may either save it with the File->Save menu item, or you will be prompted as to whether or not you want to save the data when you exit the Administrative Tool if you currently have any unsaved changed.

### 4.4. Create RETS Resources ###

The next step will be to create the RETS Resources for our MLS. For this example, we only have two: Agent and Property. Let's set up the Agent Resource first.

#### 4.4.1. Create Agent Resource and Classes ####

Select the Resource node in the tree and press the Add button.This will bring up the dialog for adding a Resource. If you properly initialized the Date and Version in the `SYSTEM` metadata, the various date and version fields will be pre-initialized with those values. Since we are creating a new site, there is no reason to adjust any of these fields:

'''Figure 9. Add Resource Dialog'''

![http://variman.googlecode.com/svn/wiki/agentdialog.png](http://variman.googlecode.com/svn/wiki/agentdialog.png)

Now that the Agent resource is created, we need to create the RETS Classes associated with that resource. The Class can be thought of as a SQL table and in fact, using a RETS extension, that's how we will tell Variman where to find the Agent data. From this point on, there will be confusion as to what "table" means because within a database it refers to a collection of like elements, whereas in RETS, it refers to a single data element. We'll use "Table" with a capital "T" to mean a RETS "Table", and "table" with a lowercase "t" to represent a relational database "table".

Select the Agent node in the tree and press Add. This will pop up a dialog box that will allow you to specify whether or not you want to add Class Lookup Object EditMask SearchHelp UpdateHelp ValidationLookup ValidationExternal. Select Class entry in the drop down list and then press OK in order to launch the dialog for adding the RETS Class.

We've decided that we will preserve history such that no tuples will be deleted from the database. For that reason, we will have a status value that indicates the particular agent entity has been logically deleted. We also need to tell Variman which table in our database relates to this Class. It so happens in this case the `Agent` Class will relate to the `agent` table in the database. That is done with the `X-DBName` field.

Let's use the following details for the Agent Class:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           ClassName:        AGT
> VisibleName:      Agent
> StandardName:     REAgent
> Description:      Agent
> ClassTimeStamp:   ModifiedTimestamp
> DeletedFlagField  Status
> DeletedFlagValue  DE
> X-DBName          agent
> }}} 

> Press the Save button to complete the dialog. Your tree view should now show:

'''Figure 10. Agent Class Entered'''

![http://variman.googlecode.com/svn/wiki/agentclass.png](http://variman.googlecode.com/svn/wiki/agentclass.png)

With the Class in place, it is time to enter the RETS Table information in order to relate the RETS data to the corresponding elements in the database. Select the AGT node in the tree and press Add. This will pop up a dialog box that will allow you to specify whether or not you want to add Table or Update information. Select Table and press OK in order to launch the dialog for adding the RETS Table data. Here is the example for entering `AgentID`:

'''Figure 11. Agent ID Dialog'''

![http://variman.googlecode.com/svn/wiki/agentiddialog.png](http://variman.googlecode.com/svn/wiki/agentiddialog.png)

You can fill in the details for the other elements the same way. But first, we would like to create an `EditMask` for the `Phone` element and a `Lookup` for the `Status` element. We do this the same way we enter the RETS Class, but select either EditMask or Lookup. Select the Agent node and then press Add to begin.

Let's use the following for the Phone Number EditMask:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           MetadataEntryID:  PhoneMask
> EditMaskID:       PhoneMask
> Value:            [2-9][0-8][0-9]-[2-9][0-9]{2}-[0-9]{4}
> }}} 

> And, let's use the following for the Lookup:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           MetadataEntryID:  AgentStatus
> LookupName:       AgentStatus
> VisibleName:      Status
> }}} 

> And for the Lookup Types:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           MetadataEntryID:  AgentDeleted
> LongValue:        Agent Deleted
> ShortValue:       Deleted
> Value:            DE
> MetadataEntryID:  AgentActive
> LongValue:        Agent Active
> ShortValue        Active
> Value:            AC
> }}} 

> After everything has been entered, your tree should now look like:

'''Figure 12. Agent Metadata Entered'''

![http://variman.googlecode.com/svn/wiki/agententered.png](http://variman.googlecode.com/svn/wiki/agententered.png)

#### 4.4.2. Create Property Resource and Classes ####

As with the `Agent` resource, to enter the `Property` resource, begin by selecting the Resource node in the tree and press the Add button.This will bring up the dialog for adding a Resource as above. Here's what it should look like:

'''Figure 13. Property Dialog'''

![http://variman.googlecode.com/svn/wiki/propertydialog.png](http://variman.googlecode.com/svn/wiki/propertydialog.png)

For the Property classes, we will have `ResidentialProperty` and `Commercial`. Let's use the following details for these Classes:

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           ClassName:        ResidentialProperty
> VisibleName:      ResidentialProperty
> StandardName:     ResidentialProperty
> Description:      Single Family Residence
> ClassTimeStamp:   ModifiedTimestamp
> DeletedFlagField  Status
> DeletedFlagValue  DE
> X-DBName          res\_prop
> ClassName:        Commercial
> VisibleName:      Commercial
> StandardName:
> Description:      Commercial Office Space
> ClassTimeStamp:   ModifiedTimestamp
> DeletedFlagField  Status
> DeletedFlagValue  DE
> X-DBName          com\_prop
> }}} 

We also know that we are going to use a Lookup and EditMask as we did above. We might as well enter them now. We can use the same exact process and data as we did above with the Agent Resource. Your tree view should now show:

'''Figure 14. Property Classes, EditMask and Lookup Entered'''

![http://variman.googlecode.com/svn/wiki/propertyentered.png](http://variman.googlecode.com/svn/wiki/propertyentered.png)

The last step is to enter the Table data for both Property Classes. Let's use the following details for Residential (only non-default values shown below):

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           MetadataEntryID:  ListingID
> SystemName:       ListingID
> StandardName:     ListingID
> LongName:         Listing Number
> DBName            listing\_id
> ShortName:        LN
> MaximumLength:    7
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          9999999
> Unique:           true
> ModTimeStamp:     true
> MetadataEntryID:  StreetNumber
> SystemName:       StreetNumber
> StandardName:     StreetNumber
> LongName:         Street Number
> DBName            street\_number
> ShortName:        STNum
> MaximumLength:    10
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  StreetName
> SystemName:       StreetName
> StandardName:     StreetName
> LongName:         Street Name
> DBName            street\_name
> ShortName:        STName
> MaximumLength:    80
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  City
> SystemName:       City
> StandardName:     City
> LongName:         City
> DBName            city
> ShortName:        City
> MaximumLength:    20
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  State
> SystemName:       State
> StandardName:     StateOrProvince
> LongName:         State
> DBName            state
> ShortName:        State
> MaximumLength:    2
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  ZipCode
> SystemName:       ZipCode
> StandardName:     PostalCode
> LongName:         Zip Code
> DBName            zip
> ShortName:        Zip
> MaximumLength:    10
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  ListPrice
> SystemName:       ListPrice
> StandardName:     ListPrice
> LongName:         Listing Price
> DBName            asking
> ShortName:        Price
> MaximumLength:    10
> DataType:         int
> Searchable:       True
> Interpretation:   Currency
> UseSeparator:     True
> ModTimeStamp:     true
> MetadataEntryID:  Status
> SystemName:       Status
> StandardName:     ListingStatus
> LongName:         Listing Status
> DBName            status
> ShortName:        Status
> MaximumLength:    2
> DataType:         Character
> Searchable:       True
> Interpretation:   Lookup
> LookupName:       Status
> ModTimeStamp:     true
> MetadataEntryID:  ModifiedTimestamp
> SystemName:       ModifiedTimestamp
> StandardName:     ModificationTimestamp
> LongName:         Listing Modified Date
> DBName            modified\_time
> ShortName:        Modified
> DataType:         DateTime
> Searchable:       True
> MetadataEntryID:  Bedrooms
> SystemName:       Beds
> StandardName:     Beds
> LongName:         Bedrooms
> DBName            beds
> ShortName:        Beds
> MaximumLength:    2
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          99
> ModTimeStamp:     true
> MetadataEntryID:  Baths
> SystemName:       Baths
> StandardName:     Baths
> LongName:         Bathrooms
> DBName            baths
> ShortName:        Baths
> MaximumLength:    2
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          99
> ModTimeStamp:     true
> }}} 

And for Commercial (only non-default values shown below):

||<tablestyle="width:90%; background-color:#E0E0E0"> {{{           MetadataEntryID:  ListingID
> SystemName:       ListingID
> StandardName:     ListingID
> LongName:         Listing Number
> DBName            listing\_id
> ShortName:        LN
> MaximumLength:    7
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          9999999
> Unique:           true
> ModTimeStamp:     true
> MetadataEntryID:  StreetNumber
> SystemName:       StreetNumber
> StandardName:     StreetNumber
> LongName:         Street Number
> DBName            street\_number
> ShortName:        STNum
> MaximumLength:    10
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  StreetName
> SystemName:       StreetName
> StandardName:     StreetName
> LongName:         Street Name
> DBName            street\_name
> ShortName:        STName
> MaximumLength:    80
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  City
> SystemName:       City
> StandardName:     City
> LongName:         City
> DBName            city
> ShortName:        City
> MaximumLength:    20
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  State
> SystemName:       State
> StandardName:     StateOrProvince
> LongName:         State
> DBName            state
> ShortName:        State
> MaximumLength:    2
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  ZipCode
> SystemName:       ZipCode
> StandardName:     PostalCode
> LongName:         Zip Code
> DBName            zip
> ShortName:        Zip
> MaximumLength:    10
> DataType:         Character
> Searchable:       True
> ModTimeStamp:     true
> MetadataEntryID:  ListPrice
> SystemName:       ListPrice
> StandardName:     ListPrice
> LongName:         Listing Price
> DBName            asking
> ShortName:        Price
> MaximumLength:    10
> DataType:         int
> Searchable:       True
> Interpretation:   Currency
> UseSeparator:     True
> ModTimeStamp:     true
> MetadataEntryID:  Status
> SystemName:       Status
> StandardName:     ListingStatus
> LongName:         Listing Status
> DBName            status
> ShortName:        Status
> MaximumLength:    2
> DataType:         Character
> Searchable:       True
> Interpretation:   Lookup
> LookupName:       Status
> ModTimeStamp:     true
> MetadataEntryID:  ModifiedTimestamp
> SystemName:       ModifiedTimestamp
> StandardName:     ModificationTimestamp
> LongName:         Listing Modified Date
> DBName            modified\_time
> ShortName:        Modified
> DataType:         DateTime
> Searchable:       True
> MetadataEntryID:  SQFT
> SystemName:       SQFT
> StandardName:
> LongName:         Rentable Area
> DBName            sqft
> ShortName:        SQFT
> MaximumLength:    5
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          99999
> ModTimeStamp:     true
> MetadataEntryID:  Offices
> SystemName:       Offices
> StandardName:
> LongName:         Offices
> DBName            offices
> ShortName:        Offices
> MaximumLength:    3
> DataType:         Int
> Searchable:       True
> Interpretation:   Number
> Alignment:        Right
> Minimum:          1
> Maximum:          999
> ModTimeStamp:     true
> }}} 

After entering all the details above using the Table dialog as we did above, your tree should look like (with Agent node collapsed and window resized to show more details):

'''Figure 15. Agent Class Entered'''

![http://variman.googlecode.com/svn/wiki/fullypopulated.png](http://variman.googlecode.com/svn/wiki/fullypopulated.png)

We have one more element to add to each Class: the AgentID. This references a foreign key what we'll create first. Here's the Agent/ResidentialProperty foreign key:

'''Figure 16. Foreign Key'''

![http://variman.googlecode.com/svn/wiki/foreignkey.png](http://variman.googlecode.com/svn/wiki/foreignkey.png)

Do the same for the Agent/Commercial foreign key and then we can add the AgentID element to both Classes:

'''Figure 17. Agent ID'''

![http://variman.googlecode.com/svn/wiki/agentid.png](http://variman.googlecode.com/svn/wiki/agentid.png)

Finally, use the File->Save menu item to save the data. The resulting file will be called `metadata.xml` in the directory you selected for the location for your Metadata in [[#rets|Section 3.2, “Configuring RETS Parameters”]].

## Bibliography ##

[1](1.md) ''Center for REALTOR Technology''.  http://www.realtors.org/crt/ .

[2](2.md) ''Apache Tomcat Web Site''.  http://tomcat.apache.org/ .

[3](3.md) ''Java Web Site''.  http://java.sun.com/ .

[4](4.md) ''JNetDirect Web Site''.  http://www.jnetdirect.com/ .

[5](5.md) ''jTDS Web Site''.  http://jtds.sourceforge.net/ .

[6](6.md) ''PostgreSQL Web Site''.  http://www.postgresql.org/ .

[7](7.md) ''RETS Web Site''.  http://www.rets.org/ .

[8](8.md) ''Variman Web Site''.  http://www.crt.realtors.org/projects/rets/variman/ .