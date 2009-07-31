package org.realtors.rets.server.activation;

/**
 * This is the interface that determines how activation is performed.  It checks if an activation code is valid
 */
public interface ActivationStrategy {
    boolean isCodeValid(String host, String email, String code);
}
