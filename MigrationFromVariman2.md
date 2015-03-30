## Migrating from Variman 2.x to Variman 3.x ##

Variman has undergone a major revision with the 3.x line. It is now RETS 1.5, 1.7 and 1.7.2 compliant. But, compliancy also involves specific metadata implementations, so as part of the process of migrating to Variman 3.x, you will need to take some specific steps to migrate your metadata. The GUI Admin tool that is provided with each Variman release can assist, and it does have a feature to help attempt to migrate and normalize your metadata in a compliant form.

The first thing to note is that earlier releases had Variman rooted under a directory called '''`webapp`'''. That has changed to '''`variman`''' to allow for consistency between the standalone and web-server versions of Variman and to allow the GUI Admin tool to work with both versions. That means that when you finally install it, you will need to move your configuration files from their old location to the new. In other words, the new version of Variman will not install over the top of the old seamlessly.

The GUI Admin tool now has the ability to edit your Metadata. Please see the [Variman Manual](http://www.crt.realtors.org/projects/rets/variman/documentation/manual/) for further details and example usage.

### Activation ###

With the Variman 3.x version, we have added code that requires the software to be activated. Activation is simple and quick.  Prior to activation, the service will shut itself down after an hour of start-up and signal this in the log files.

Activation Steps:
  * Install Variman
  * Once installed, visit http://www.crt.realtors.org/projects/rets/variman/support/activation.php and enter your Variman Host Name (e.g. demo.crt.realtors.org) and your email address. If you are in doubt about the canonical name, check the Variman log files and it will be displayed there.
  * The Activation Server will send you an activation file with instructions where to place the file.

Why are we requiring activation:
Variman is still FREE to download and use. The support mailing list does not identify how extensively Variman is used, and it is difficult for us to get feedback from the users. Because of this, we created a simple process for activating in an attempt to understand the usage patterns of Variman.   The process only logs the Host URL and email address, and does not communicate with the live server in an manner. The overall goal is to create a mechanism to provide better support for the product without adding inconvenience for the end user.  As with all the code of Variman, it is completely open source, so you are able to disable the activation at any time - just keep in mind, this is our window in continued support and increased understanding of Variman in the wild.


### Metadata ###

In your '''`METADATA-SYSTEM`''' file, if you modeled your metadata from the
NAR sample metadata, it incorrectly used '''`Comments`''' as a tag. Change
that to '''`COMMENTS`'''.

In your '''`METADATA-CLASS`''' files, you must change the field '''`DBName`''' to '''`X-DBName`''' for Variman 3.x to work properly. The DBName field is an
extension to the RETS CLASS metadata for Variman's use ... it is not
part of the RETS spec. To properly extend metadata, the new fields
must have '''`X-`''' prepended to their names to be compliant.

In your '''`METADATA-UPDATE`''' files, the columns '''`Version`''' and '''`Date`''' must
be changed to '''`UpdateTypeVersion`''' and '''`UpdateTypeDate`'''.

In your '''`METADATA-VALIDATE_EXTERNAL_TYPE`''' files, the attribute
'''`ValidationExternal`''' must be renamed to '''`ValidationExternalName`'''.

If you modeled your metadata after the NAR sample metadata, it
included underscores in names in many places where the RETS spec does
not allow underscores. If this is the case, when using the Metadata
Editor, disable the '''`Strict`''' option in the '''`Metadata`''' Menu, otherwise it
will not accept those names. The migration tool will remove
underscores, but you do need to verify your data to be sure it did so
properly.

The format for RETS 1.7.2 dates in metadata now follows the RETS date
spec everywhere.

Note that there is also an anomaly in the RETS 1.7.2 spec as released
to the KeyField in '''`METADATA-RESOURCE`''' and the various references to
that field in '''`METADATA-FOREIGN_KEYS`''', '''`METADATA-TABLE`''', etc. In '''`METADATA-RESOURCE`''', the '''`!KeyField`''' is declared as a '''`RETSID`''', yet it refers to the
'''`SystemName`''' in the other tables, and they are declared as '''`RETSNAME`'''. In
a '''`RETSNAME`''', underscores are permitted, but they are not in a '''`RETSID`'''.
So, you may need to remove underscores elsewhere to be compliant.

On initial startup, there are some nuances to properly configure
Variman 3.1.1 to recognize your existing metadata. You can do this
one of a couple of ways:

  * Copy your existing '''`webapp/WEB-INF/rets-config.xml`''' file to the '''`variman/WEB-INF`''' directory.
  * Manually edit the '''`rets-config.xml`''' file to properly reference you metadata directory;
  * Use the Admin Tool to update the '''`rets-config.xml`''' file.

If you plan to edit metadata using the new editor and you ever change
the metadata directory, you may get into a case where the tool won't
recognize your metadata:

When the tool loads, it attempts to read metadata from the directory
contained in the '''`rets-config.xml`''' file. This happens before the GUI is
displaying its first page. So, if you change that directory, the tool
must be told to reload the metadata. You do that by '''`FILE->SAVE`''' in
order to save the configuration, then you select the '''`METADATA`''' tab.
That activates the '''`METADATA`''' Menu item. Select '''`RELOAD METADATA`''' and it
should pick up your existing metadata and then render it. You need to
'''`FILE->SAVE`''' one more time to write that copy of the metadata to the new
'''`metadata.xml`''' file.

You may also try to do an automatic migration of your metadata by
selecting the '''`METADATA->MIGRATE`''' menu item in the admin tool.
You may also need to deselect the '''`STRICT`''' option to relax tests
for compliance. If you do this, your metadata may not be strictly
compliant, but more of it will automatically convert. You will then need
to manually correct compliance issues. If you toggle '''`STRICT`''', remember
to '''`RELOAD METADATA`'''.

From this point onward, the metadata is kept in the file '''`metadata.xml`'''
and the other files can be archived and removed if so desired.