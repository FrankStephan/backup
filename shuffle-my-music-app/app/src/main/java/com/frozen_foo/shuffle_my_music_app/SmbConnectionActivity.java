package com.frozen_foo.shuffle_my_music_app;

import android.app.AlertDialog;
import android.content.Intent;
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
		Settings settings = new SettingsService().readSettings(getApplicationContext());
		try {
			CryptoService cryptoService = new CryptoService();
			String encryptedIp = settings.getIp();
			String encryptedName = settings.getUsername();
			String encryptedShuffleMyMusicDir = settings.getShuffleMyMusicDir();

			((TextView) findViewById(R.id.smbIpText)).setText(StringUtils.isNotEmpty(encryptedIp)
					? cryptoService.decrypt(encryptedIp) : "");
			((TextView) findViewById(R.id.smbNameText)).setText(StringUtils.isNotEmpty
					(encryptedName) ? cryptoService.decrypt(encryptedName) : "");
			((TextView) findViewById(R.id.smbShuffleMyMusicText)).setText(StringUtils.isNotEmpty
					(encryptedShuffleMyMusicDir) ? cryptoService.decrypt
					(encryptedShuffleMyMusicDir) : "");

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

	public void submitCredentials(View view) {
		CharSequence smbIp = ((TextView) findViewById(R.id.smbIpText)).getText();
		CharSequence smbLoginName = ((TextView) findViewById(R.id.smbNameText)).getText();
		CharSequence smbLoginPassword = ((TextView) findViewById(R.id.smbPasswordText)).getText();
		CharSequence smbShuffleMyMusicDir = ((TextView) findViewById(R.id.smbShuffleMyMusicText))
				.getText();
		;

		if (StringUtils.isNoneEmpty(smbIp, smbLoginName, smbShuffleMyMusicDir)) {
			String encryptedIp = null;
			String encryptedName = null;
			String encryptedPassword = null;
			String encryptedShuffleMyMusicDir = null;
			try {
				CryptoService cryptoService = new CryptoService();
				encryptedIp = cryptoService.encrypt(smbIp.toString());
				encryptedName = cryptoService.encrypt(smbLoginName.toString());
				encryptedPassword = StringUtils.isNotEmpty(smbLoginPassword) ? cryptoService
						.encrypt(smbLoginPassword.toString()) : new SettingsService().readSettings
						(getApplicationContext()).getPassword();
				encryptedShuffleMyMusicDir = cryptoService.encrypt(smbShuffleMyMusicDir.toString
						());
				Toast.makeText(getApplicationContext(), R.string.store_credentials_success, Toast
						.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast
						.LENGTH_LONG).show();
				e.printStackTrace();
			}
			new SettingsService().writeSettings(new Settings(encryptedIp, encryptedName,
					encryptedPassword, encryptedShuffleMyMusicDir), getApplicationContext());
			openMain();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.errorEmptySmbParameters).setTitle(R.string.error);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private void openMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void cancel(View view) {
		Toast.makeText(getApplicationContext(), R.string.bye, Toast.LENGTH_SHORT).show();
		openMain();
	}
}
