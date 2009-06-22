package org.realtors.rets.server.activation;

/**
 * The ActivationManager is the main interface to the Activation system.  From here you can check if Variman
 * has been activated, as well as enter your activation information.
 */
public interface ActivationManager {

    /**
     * Checks if Variman has been activated.
     * @return True if activated has been successfully completed.
     * @param host
     */
    boolean isActivated(String host);
}
