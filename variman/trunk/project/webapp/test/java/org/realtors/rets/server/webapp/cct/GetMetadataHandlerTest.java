/*
 * Created on Sep 16, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.WebApp;

import org.xml.sax.SAXException;

/**
 * @author kgarner
 */
public class GetMetadataHandlerTest extends LocalTestCase
{
    public void setUp() throws IOException, ServletException
    {
        // Create metadata for the system to use
        MetadataManager manager = new MetadataManager();
        MSystem system = new MSystem();
        system.setDate(new Date());
        manager.addRecursive(system);
        WebApp.setMetadataManager(manager);

        ServletRunner servletRunner = new ServletRunner();
        servletRunner.registerServlet("/rets/cct/getMetadata",
                                      TestHandlerServlet.class.getName());
        mClient = servletRunner.newClient();
        InvocationContext invocation = mClient.newInvocation(METADATA_URL);
        TestHandlerServlet servlet =
            (TestHandlerServlet) invocation.getServlet();
        mGetMetadata = new GetMetadataHandler();
        servlet.setHandler(mGetMetadata);
    }

    public void testEvaluateParameters()
        throws MalformedURLException, IOException, SAXException
    {
        // Test with all options
        String query = METADATA_URL +
                       "?Type=METADATA-SYSTEM&ID=*&Format=COMPACT";
        mGetMetadata.reset();
        WebResponse response = mClient.getResponse(query);
        
        ValidationResult result = new ValidationResult();
        result.setStatus(StatusEnum.PASSED);
        mGetMetadata.validateParameters(result);
        
        assertEquals(StatusEnum.PASSED, result.getStatus());

        // Test with only required options
        query = METADATA_URL + "?Type=METADATA-SYSTEM&ID=*";
        mGetMetadata.reset();
        response = mClient.getResponse(query);

        result = new ValidationResult();
        result.setStatus(StatusEnum.PASSED);
        mGetMetadata.validateParameters(result);

        assertEquals(StatusEnum.PASSED, result.getStatus());

        //testing ID set to wrong value
        query = METADATA_URL + "?Type=METADATA-SYSTEM&ID=8";
        mGetMetadata.reset();
        response = mClient.getResponse(query);

        result = new ValidationResult();
        result.setStatus(StatusEnum.PASSED);
        mGetMetadata.validateParameters(result);

        assertEquals(StatusEnum.FAILED, result.getStatus());
        List messages = result.getMessages();
        assertEquals(1, messages.size());
    }    

    private ServletUnitClient mClient;
    private GetMetadataHandler mGetMetadata;
    private SessionFactory mSessions;
    private static final Logger LOG =
        Logger.getLogger(GetMetadataHandlerTest.class);
    public static final String METADATA_URL =
        "http://localhost/rets/cct/getMetadata";
}
