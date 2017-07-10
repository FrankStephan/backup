package com.frozen_foo.shuffle_my_music_app.security;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import java.math.BigInteger;
import java.security.KeyStoreException;

import javax.security.auth.x500.X500Principal;

/**
 * Created by Frank on 08.07.2017.
 */

public class KeyPairService {

    private static final String ALIAS = "com.frozen_foo.shuffle_my_music_app.security.alias";

    KeyStoreProvider keyStoreProvider = new KeyStoreProvider();

    public String getAlias() {
        return ALIAS;
    }

    public boolean aliasExists() throws KeyStoreException {
        return keyStoreProvider.androidKeyStore().containsAlias(getAlias());
    }

    public void createNewKeyPair(Context context) throws KeyStoreException {
        if (!aliasExists()) {
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(getAlias())
                    .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                    .build();
        }
    }
}
