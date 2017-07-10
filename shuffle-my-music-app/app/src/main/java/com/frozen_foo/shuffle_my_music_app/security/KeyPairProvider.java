package com.frozen_foo.shuffle_my_music_app.security;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Frank on 08.07.2017.
 */

public class KeyPairProvider {

    public KeyPairGenerator keyPairGenerator() throws NoSuchProviderException, NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
    }
}
