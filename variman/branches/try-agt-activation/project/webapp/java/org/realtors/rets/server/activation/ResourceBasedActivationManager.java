package org.realtors.rets.server.activation;

import org.springframework.core.io.Resource;
import org.realtors.rets.server.RetsServerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: atillman
 * Date: Jun 9, 2009
 * Time: 2:03:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceBasedActivationManager implements ActivationManager {

    private static final Log logger = LogFactory.getLog(ResourceBasedActivationManager.class);


    public static final String VARIMAN_ACTIVATION_NAME = "variman.activation.name";
    public static final String VARIMAN_ACTIVATION_EMAIL = "variman.activation.email";
    public static final String VARIMAN_ACTIVATION_CODE = "variman.activation.code";

    private final ActivationStrategy activationStrategy;
    private final Properties activationProperties;

    public ResourceBasedActivationManager(Resource activationInformation, ActivationStrategy activationStrategy) {
        this.activationStrategy = activationStrategy;
        if (activationInformation.exists()) {
            activationProperties = new Properties();
            try {
                activationProperties.load(activationInformation.getInputStream());
            } catch (IOException e) {
                logger.warn("Unable to load activation information", e);
            }
        } else {
            activationProperties = null;
        }
    }


    public boolean isActivated() {
        return activationProperties != null && activationStrategy.isCodeValid(
                activationProperties.getProperty(VARIMAN_ACTIVATION_NAME),
                activationProperties.getProperty(VARIMAN_ACTIVATION_EMAIL),
                activationProperties.getProperty(VARIMAN_ACTIVATION_CODE));

    }
}
