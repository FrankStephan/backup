package com.frozen_foo.shuffle_my_music_app;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.security.CryptoService;
import com.frozen_foo.shuffle_my_music_app.security.KeyStoreService;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsService;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class SmbConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smb_connection);
        Settings settings = new SettingsService().readSettings(getPreferences(MODE_PRIVATE));
        try {
            CryptoService cryptoService = new CryptoService(new KeyStoreService());
            String encryptedIp = settings.getIp();
            String encryptedName = settings.getUsername();
            if (StringUtils.isNoneEmpty(encryptedIp, encryptedName)) {
                ((TextView) findViewById(R.id.smbIpText)).setText(cryptoService.decrypt(encryptedIp));
                ((TextView) findViewById(R.id.smbNameText)).setText(cryptoService.decrypt(encryptedName));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void submitCredentials(View view) {
        CharSequence smbIp = ((TextView )findViewById(R.id.smbIpText)).getText();
        CharSequence smbLoginName = ((TextView )findViewById(R.id.smbNameText)).getText();
        CharSequence smbLoginPassword = ((TextView )findViewById(R.id.smbPasswordText)).getText();

        if (StringUtils.isNoneEmpty(smbIp, smbLoginName, smbLoginPassword)) {
            String encryptedIp = null;
            String encryptedName = null;
            String encryptedPassword = null;
            try {
                CryptoService cryptoService = new CryptoService(new KeyStoreService());
                encryptedIp = cryptoService.encrypt(smbIp.toString());
                encryptedName = cryptoService.encrypt(smbLoginName.toString());
                encryptedPassword = cryptoService.encrypt(smbLoginPassword.toString());
                Toast.makeText(getApplicationContext(), R.string.store_credentials_success, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            new SettingsService().writeSettings(new Settings(encryptedIp.toString(), encryptedName.toString(), encryptedPassword.toString()), getPreferences(MODE_PRIVATE));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.errorEmptySmbParameters).setTitle(R.string.error);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
