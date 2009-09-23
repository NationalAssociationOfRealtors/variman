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
    String host = "127.0.0.1";
    String email = "test@test.com";
    String md5hash = "f74fbaa374a1afb2971d12b00da109a2";

    @Test
    public void validActivationCodeReturnsTrue() throws Exception {        
        assertTrue("Should have returned true when given a valid hash",
                activationStrategy.isCodeValid(host, email, md5hash));

    }

    @Test
    public void invalidActivationCodeReturnsFalse() {
        assertFalse("Should have returned false when given an invalid hash",
                activationStrategy.isCodeValid(host, email, "somecodethatwontwork"));
    }

}
