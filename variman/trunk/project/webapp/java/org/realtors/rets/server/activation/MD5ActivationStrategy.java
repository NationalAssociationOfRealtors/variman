package org.realtors.rets.server.activation;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

/**
 * ActivationStrategy that uses MD5 to check the activation code
 */
public class MD5ActivationStrategy implements ActivationStrategy {

    private static final Logger LOG = Logger.getLogger(MD5ActivationStrategy.class);

    public boolean isCodeValid(String host, String email, String code) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String nameAndEmail = host + email;
            md5.update(nameAndEmail.getBytes());
            String result = new BigInteger(1,md5.digest()).toString(16);
            while (result.length() < 32) result = "0" + result;
            return code.equals(result);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error hashing code", e);
            return false;
        }
    }
}
