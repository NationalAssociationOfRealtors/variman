package org.realtors.rets.server.activation;

import org.springframework.core.io.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;
import java.io.IOException;

/**
 * An ActivationManager that is tied to a resource to get the email and code tied to the Variman installation.
 */
public class ResourceBasedActivationManager implements ActivationManager {

    private static final Log logger = LogFactory.getLog(ResourceBasedActivationManager.class);


    public static final String VARIMAN_ACTIVATION_EMAIL = "variman.activation.email";
    public static final String VARIMAN_ACTIVATION_CODE = "variman.activation.code";

    private final ActivationStrategy activationStrategy;
    private final Resource activationInformation;

    public ResourceBasedActivationManager(Resource activationInformation, ActivationStrategy activationStrategy) {
        this.activationStrategy = activationStrategy;
        this.activationInformation = activationInformation;
    }


    public boolean isActivated(String host) {
        Properties activationProperties = getActivationProperties();   
        return activationProperties != null && activationStrategy.isCodeValid(host,
                activationProperties.getProperty(VARIMAN_ACTIVATION_EMAIL),
                activationProperties.getProperty(VARIMAN_ACTIVATION_CODE));
    }

    private Properties getActivationProperties() {
        if (activationInformation.exists()) {
            Properties activationProperties = new Properties();
            try {
                activationProperties.load(activationInformation.getInputStream());
                return activationProperties;
            } catch (IOException e) {
                logger.warn("Unable to load activation information", e);
                return null;
            }
        } else {
            logger.warn("Unable to locate activation resource: " + activationInformation.getDescription());
            return null;
        }
    }
}
