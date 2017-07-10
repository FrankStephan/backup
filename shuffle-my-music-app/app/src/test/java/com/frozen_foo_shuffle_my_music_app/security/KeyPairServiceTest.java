package com.frozen_foo_shuffle_my_music_app.security;

import android.security.KeyPairGeneratorSpec;

import com.frozen_foo.shuffle_my_music_app.security.KeyPairService;
import com.frozen_foo.shuffle_my_music_app.security.KeyStoreProvider;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * Created by Frank on 08.07.2017.
 */

public class KeyPairServiceTest {

    @Test
    public void aliasHasCorrectValue() {
        KeyPairService keyPairService = new KeyPairService();
        System.out.println(keyPairService.getAlias());
        Assert.assertEquals("com.frozen_foo.shuffle_my_music_app.security.alias", keyPairService.getAlias());
    }

    @Test
    public void existsChecksKeyStore() throws KeyStoreException {
        KeyPairService keyPairService = new KeyPairService();

        KeyStore keyStore = Mockito.mock(KeyStore.class);
        Mockito.when(keyStore.containsAlias(keyPairService.getAlias())).thenReturn(Boolean.TRUE);

        KeyStoreProvider keyStoreProvider = Mockito.mock(KeyStoreProvider.class);
        Mockito.when(keyStoreProvider.androidKeyStore()).thenReturn(keyStore);

        keyPairService.aliasExists();
        Mockito.verify(keyStore).containsAlias(keyPairService.getAlias());
    }

    @Test
    public void createsKeyPairUsingAlias() throws InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = Mockito.mock(KeyPairGenerator.class);
        KeyPairService keyPairService = new KeyPairService();
        keyPairService.createNewKeyPair();
        ArgumentCaptor<KeyPairGeneratorSpec> keyPairGeneratorSpecArgumentCaptor = ArgumentCaptor.forClass(KeyPairGeneratorSpec.class);
        Mockito.verify(keyPairGenerator).initialize(keyPairGeneratorSpecArgumentCaptor.capture());
        KeyPairGeneratorSpec capturedKeyPairGeneratorSpec = keyPairGeneratorSpecArgumentCaptor.getValue();
        assert keyPairService.getAlias().equals(capturedKeyPairGeneratorSpec.getKeystoreAlias());
        Mockito.verify(keyPairGenerator).generateKeyPair();
    }
}
