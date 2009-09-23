package org.realtors.rets.server.activation;

import org.springframework.core.io.Resource;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import java.util.Properties;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: atillman
 * Date: Jun 9, 2009
 * Time: 2:28:11 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JMock.class)
public class ResourceBasedActivationManagerTest {

    final Mockery context = new JUnit4Mockery();
    //The activation information that will get passed to the ActivationStrategy
    final String email = "test@test.com";
    final String host = "test.hotst.com";
    final String code = "testcode";


    @Test
    public void validActivationCodeReturnsIsActivatedTrue() throws Exception {

        //Create the activationManager and run the test
        ResourceBasedActivationManager activationManager = new ResourceBasedActivationManager(
                createResource(true, email, code), createStrategy(true, host, email, code));
        assertTrue("isActivated should be true when the resources exists, and the activation code is valid",
                activationManager.isActivated(host));
    }

    @Test
    public void invalidActivationCodeReturnsIsActivatedFalse() throws Exception {
        //THe resource exists, but the code returned will be invalid
        ResourceBasedActivationManager activationManager = new ResourceBasedActivationManager(
                createResource(true, email, code), createStrategy(false, host, email, code));
        assertFalse("isActivated should be false when the resources exists, but the activation code is invalid",
                activationManager.isActivated(host));
    }

    @Test
    public void nonExistantResourceReturnsIsActivatedFals() throws Exception {
        //The resource will say it does not exist
        ResourceBasedActivationManager activationManager = new ResourceBasedActivationManager(
                createResource(false, null, null), null);
        assertFalse("isActivated should be false when the resources doesn't exist.",
                activationManager.isActivated(host));
    }

    private ActivationStrategy createStrategy(final boolean valid, final String host,
                                              final String email, final String code) {
        final ActivationStrategy activationStrategy = context.mock(ActivationStrategy.class);
        context.checking(new Expectations() {{
            allowing(activationStrategy).isCodeValid(host, email, code); will(returnValue(valid));
        }});
        return activationStrategy;
    }

    private Resource createResource(final boolean exists, String email, String code) throws Exception {
        final Resource activationResource = context.mock(Resource.class, "activationResource");
        context.checking(new Expectations() {{
            allowing (activationResource).exists(); will(returnValue(exists));
            allowing (activationResource).getDescription(); will(returnValue("Mock Resource"));

        }});

        if(email != null && code != null) {
            //The resource returns the property string
            Properties activationProperties = new Properties();           
            activationProperties.setProperty(ResourceBasedActivationManager.VARIMAN_ACTIVATION_EMAIL, email);
            activationProperties.setProperty(ResourceBasedActivationManager.VARIMAN_ACTIVATION_CODE, code);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            activationProperties.store(out, "");
            context.checking(new Expectations() {{
                allowing (activationResource).getInputStream(); will(returnValue(
                    new ByteArrayInputStream(out.toByteArray())));
            }});
        }
        return activationResource;
    }
}
