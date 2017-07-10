package com.frozen_foo.shuffle_my_music_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.security.CryptoService;
import com.frozen_foo.shuffle_my_music_app.security.KeyStoreService;

public class SmbConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smb_connection);
    }

    public void submitCredentials(View view) {
        CharSequence smbLoginName = ((TextView )findViewById(R.id.smbNameText)).getText();
        CharSequence smbLoginPassword = ((TextView )findViewById(R.id.smbPasswordText)).getText();
        try {
            CryptoService cryptoService = new CryptoService(new KeyStoreService());
            String encryptedPassword = cryptoService.encrypt(smbLoginPassword.toString());
            ((TextView) findViewById(R.id.testText)).setText(encryptedPassword);
			((TextView) findViewById(R.id.testText2)).setText(cryptoService.decrypt(encryptedPassword));
            Toast.makeText(getApplicationContext(), R.string.store_credentials_success, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
