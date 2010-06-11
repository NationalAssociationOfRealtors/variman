/*
 */
package org.realtors.rets.server.protocol;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.GlobalTestSetup;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.RetsDTD;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.UnitEnum;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RetsConfigDao;
import org.realtors.rets.server.config.XmlRetsConfigDao;
import org.realtors.rets.server.dmql.SimpleDmqlMetadata;

public class StandardXMLFormatterTest extends LinesEqualTestCase
{
    public void testFormatting() throws RetsServerException
    {
        init();
        
        StandardXMLFormatter formatter =
            new StandardXMLFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345", "1000"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");
        metadata.addString("LivingArea", "r_SQFT");
        MTable table = new MTable();
        table.setUniqueId(Long.valueOf(1));
        table.setUnits(UnitEnum.SQFT.toString());
        table.setDBName("r_SQFT");
        table.setDataType("Int");
        metadata.addTable("LivingArea", table);
        
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(COLUMNS), metadata, RetsVersion.RETS_1_7_2);
        context.setClassStandardName("ResidentialProperty");
        context.setResourceStandardName("Property");
        context.setStandardNames(true);
        formatter.formatResults(context);
        assertLinesEqual(
            "<REData>\n" +
            "<REProperties>\n" +
            "<ResidentialProperty>\n" +
            "<Listing>\n" +
            "<StreetAddress>\n" +
            "<StreetName>Main St.</StreetName>\n" +
            "<PostalCode>12345</PostalCode>\n" +
            "</StreetAddress>\n" +
            "</Listing>\n" +
            "<LivingArea>\n" +
            "<Area Type=\"INTEGER\" Units=\"SqFeet\">1000</Area>\n" +
            "</LivingArea>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public void testNoUnits() throws RetsServerException
    {
        String[] columns = new String[] {"r_STNAME", "r_ZIP_CODE"};
        StandardXMLFormatter formatter =
            new StandardXMLFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(columns);
        results.addRow(new String[] {"Main St.", "12345"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");

        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(columns), metadata,
                                       RetsVersion.RETS_1_7_2);
        context.setClassStandardName("ResidentialProperty");
        context.setResourceStandardName("Property");
        context.setStandardNames(true);
        formatter.formatResults(context);
        assertLinesEqual(
            "<REData>\n" +
            "<REProperties>\n" +
            "<ResidentialProperty>\n" +
            "<Listing>\n" +
            "<StreetAddress>\n" +
            "<StreetName>Main St.</StreetName>\n" +
            "<PostalCode>12345</PostalCode>\n" +
            "</StreetAddress>\n" +
            "</Listing>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public void testNoStreetNameColumn() throws RetsServerException
    {
        String[] columns = new String[] {"r_ZIP_CODE", "r_SQFT"};
        StandardXMLFormatter formatter =
            new StandardXMLFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(columns);
        results.addRow(new String[] {"12345", "1000"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");
        metadata.addString("LivingArea", "r_SQFT");
        MTable table = new MTable();
        table.setUniqueId(Long.valueOf(1));
        table.setUnits(UnitEnum.SQFT.toString());
        table.setDBName("r_SQFT");
        table.setDataType("Int");
        metadata.addTable("LivingArea", table);
 
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(columns), metadata,
                                       RetsVersion.RETS_1_7_2);
        context.setClassStandardName("ResidentialProperty");
        context.setResourceStandardName("Property");
        context.setStandardNames(true);
        formatter.formatResults(context);
        assertLinesEqual(
            "<REData>\n" +
            "<REProperties>\n" +
            "<ResidentialProperty>\n" +
            "<Listing>\n" +
            "<StreetAddress>\n" +
            "<PostalCode>12345</PostalCode>\n" +
            "</StreetAddress>\n" +
            "</Listing>\n" +
            "<LivingArea>\n" +
            "<Area Type=\"INTEGER\" Units=\"SqFeet\">1000</Area>\n" +
            "</LivingArea>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }
    
    private void init() throws RetsServerException
    {
        GlobalTestSetup.globalSetup();
        RetsConfigDao configDao = RetsServer.getRetsConfigDao();
        if (configDao == null)
        {
            File basePath = new File(".");
            String configFile = basePath.getAbsolutePath() + "/build/rets-server/variman/WEB-INF/rets/rets-config.xml";
            configDao = new XmlRetsConfigDao(configFile);
            RetsDTD.setBasePath(basePath.getAbsolutePath() + "/build/rets-server/variman");
        }
        RetsConfig config = configDao.loadRetsConfig();
        RetsServer.setRetsConfiguration(config);
        mMetadataManager = RetsServer.getMetadataManager();
        
        /*
         * Build the metadata. This is needed for the process that matches the DTD against the metadata
         * for the StandardXMLFormatter.
         */
        MResource resource = new MResource();
        resource.setResourceID("Property");
        resource.setStandardName("Property");
   
        MClass clazz = new MClass();
        clazz.setClassName("RES");
        clazz.setStandardName("ResidentialProperty");
        
        MTable table1 = new MTable();
        table1.setStandardName("StreetName");
        table1.setSystemName("StreetName");
        table1.setDBName("r_STNAME");
        table1.setDataType("Character");
        
        MTable table2 = new MTable();
        table2.setStandardName("PostalCode");
        table2.setSystemName("zip");
        table2.setDBName("r_ZIP_CODE");
        table2.setDataType("Character");
        
        MTable table3 = new MTable();
        table3.setStandardName("LivingArea");
        table3.setSystemName("SqFt");
        table3.setDBName("r_SQFT");
        table3.setUnits("SqFt");
        table3.setDataType("Int");

        mSystem = new MSystem();
        mSystem.addChild(MetadataType.RESOURCE, resource);
        resource.addChild(MetadataType.CLASS, clazz);
        clazz.addChild(MetadataType.TABLE, table1);
        clazz.addChild(MetadataType.TABLE, table2);
        clazz.addChild(MetadataType.TABLE, table3);
        /*
         * Set the MetadataManager to use this metadata.
         */
        mMetadataManager.clear();
        mMetadataManager.addRecursive(mSystem);
    }
    
    private MetadataManager mMetadataManager = null;
    private MSystem mSystem = null;

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE", "r_SQFT"};
}
