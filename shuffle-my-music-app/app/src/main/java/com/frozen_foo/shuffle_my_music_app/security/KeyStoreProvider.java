package com.frozen_foo.shuffle_my_music_app.security;

import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * Created by Frank on 08.07.2017.
 */

public class KeyStoreProvider {

    public KeyStore androidKeyStore() throws KeyStoreException {
        return KeyStore.getInstance("AndroidKeyStore");
    }
}
