package com.frozen_foo.shuffle_my_music_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.crypto.Cryptifier;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleListActivity;

import org.apache.commons.lang3.StringUtils;

public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Settings settings = new SettingsAccess().readSettings(getApplicationContext());
		try {
			Cryptifier cryptifier                 = new Cryptifier();
			String     encryptedIp                = settings.getIp();
			String     encryptedName              = settings.getUsername();
			String     encryptedShuffleMyMusicDir = settings.getShuffleMyMusicDir();
			String     encryptedMusicDir          = settings.getMusicDir();

			((TextView) findViewById(R.id.smbIpText))
					.setText(StringUtils.isNotEmpty(encryptedIp) ? cryptifier.decrypt(encryptedIp) : "");
			((TextView) findViewById(R.id.smbNameText))
					.setText(StringUtils.isNotEmpty(encryptedName) ? cryptifier.decrypt(encryptedName) : "");
			((TextView) findViewById(R.id.smbShuffleMyMusicText)).setText(
					StringUtils.isNotEmpty(encryptedShuffleMyMusicDir) ?
							cryptifier.decrypt(encryptedShuffleMyMusicDir) : "");
			((TextView) findViewById(R.id.smbMusicDirText))
					.setText(StringUtils.isNotEmpty(encryptedMusicDir) ? cryptifier.decrypt(encryptedMusicDir) : "");

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void submitCredentials(View view) {
		CharSequence smbIp                = ((TextView) findViewById(R.id.smbIpText)).getText();
		CharSequence smbLoginName         = ((TextView) findViewById(R.id.smbNameText)).getText();
		CharSequence smbLoginPassword     = ((TextView) findViewById(R.id.smbPasswordText)).getText();
		CharSequence smbShuffleMyMusicDir = ((TextView) findViewById(R.id.smbShuffleMyMusicText)).getText();
		CharSequence smbMusicDir          = ((TextView) findViewById(R.id.smbMusicDirText)).getText();
		;

		if (StringUtils.isNoneEmpty(smbIp, smbLoginName, smbShuffleMyMusicDir, smbMusicDir)) {
			String encryptedIp                = null;
			String encryptedName              = null;
			String encryptedPassword          = null;
			String encryptedShuffleMyMusicDir = null;
			String encryptedMusicDir          = null;
			try {
				Cryptifier cryptifier = new Cryptifier();
				encryptedIp = cryptifier.encrypt(smbIp.toString());
				encryptedName = cryptifier.encrypt(smbLoginName.toString());
				encryptedPassword =
						StringUtils.isNotEmpty(smbLoginPassword) ? cryptifier.encrypt(smbLoginPassword.toString()) :
								new SettingsAccess().readSettings(getApplicationContext()).getPassword();
				encryptedShuffleMyMusicDir = cryptifier.encrypt(smbShuffleMyMusicDir.toString());
				encryptedMusicDir = cryptifier.encrypt(smbMusicDir.toString());
				Toast.makeText(getApplicationContext(), R.string.store_credentials_success, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			new SettingsAccess().writeSettings(
					new Settings(encryptedIp, encryptedName, encryptedPassword, encryptedShuffleMyMusicDir,
							encryptedMusicDir), getApplicationContext());
			openShuffleListActivity();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.errorEmptySmbParameters).setTitle(R.string.error);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private void openShuffleListActivity() {
		Intent intent = new Intent(this, ShuffleListActivity.class);
		startActivity(intent);
	}

	public void cancel(View view) {
		Toast.makeText(getApplicationContext(), R.string.bye, Toast.LENGTH_SHORT).show();
		openShuffleListActivity();
	}
}
