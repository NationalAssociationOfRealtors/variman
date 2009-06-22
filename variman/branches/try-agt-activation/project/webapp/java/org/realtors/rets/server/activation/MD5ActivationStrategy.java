package org.realtors.rets.server.activation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

/**
 * ActivationStrategy that uses MD5 to check the activation code
 */
public class MD5ActivationStrategy implements ActivationStrategy {

    private static final Log logger = LogFactory.getLog(MD5ActivationStrategy.class);

    public boolean isCodeValid(String host, String email, String code) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String nameAndEmail = host + email;
            md5.update(nameAndEmail.getBytes());
            String result = new BigInteger(md5.digest()).toString();
            return code.equals(result);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error hashing code", e);
            return false;
        }
    }
}
