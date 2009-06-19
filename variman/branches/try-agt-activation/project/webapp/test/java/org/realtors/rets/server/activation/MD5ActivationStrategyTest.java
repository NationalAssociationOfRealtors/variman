package org.realtors.rets.server.activation;

import org.junit.Test;
import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: atillman
 * Date: Jun 19, 2009
 * Time: 4:15:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MD5ActivationStrategyTest {

    MD5ActivationStrategy activationStrategy = new MD5ActivationStrategy();
    String name = "testname";
    String email = "testemail";

    @Test
    public void validActivationCodeReturnsTrue() throws Exception {
        assertTrue("Should have returned true when given a valid hash",
                activationStrategy.isCodeValid(name, email, createMD5Hash(name, email)));
    }

    private String createMD5Hash(String name, String email) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String nameAndEmail = name + email;
        md5.update(nameAndEmail.getBytes());
        return new BigInteger(1,md5.digest()).toString();
    }

    @Test
    public void invalidActivationCodeReturnsFalse() {
        assertFalse("Should have returned false when given an invalid hash",
                activationStrategy.isCodeValid(name, email, "somecodethatwontwork"));
    }

}
