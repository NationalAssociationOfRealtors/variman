package org.realtors.rets.server.activation;

import org.springframework.core.io.Resource;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.io.IOException;

/**
 * An ActivationManager that is tied to a resource to get the email and code tied to the Variman installation.
 */
public class ResourceBasedActivationManager implements ActivationManager {

    private static final Logger LOG = Logger.getLogger(ResourceBasedActivationManager.class);


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
        if (activationProperties != null && LOG.isDebugEnabled())
        {
                LOG.debug(VARIMAN_ACTIVATION_EMAIL + ": " + activationProperties.getProperty(VARIMAN_ACTIVATION_EMAIL));
                LOG.debug(VARIMAN_ACTIVATION_CODE + ": " + activationProperties.getProperty(VARIMAN_ACTIVATION_CODE));
        }
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
                LOG.warn("Unable to load activation information", e);
                return null;
            }
        } else {
            LOG.warn("Unable to locate activation resource: " + activationInformation.getDescription());
            return null;
        }
    }
}
