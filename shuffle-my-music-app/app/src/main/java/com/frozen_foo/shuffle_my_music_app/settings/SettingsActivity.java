package com.frozen_foo.shuffle_my_music_app.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.io.remote.smb.SmbAccess;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleListActivity;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

	public static final String SETTINGS_CHANGED_FLAG = "settingsChanged";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Settings settings = null;
		try {
			settings = new SettingsAccess().readSettings(getApplicationContext());
		} catch (SettingsAccessException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Logger.logException(this, e);
		}

		if (null != settings) {
			((TextView) findViewById(R.id.smbIpText)).setText(settings.getIp());
			((TextView) findViewById(R.id.smbNameText)).setText(settings.getUsername());
			((TextView) findViewById(R.id.smbLocalDirText)).setText(settings.getLocalDir());
			((TextView) findViewById(R.id.smbRemoteDirText)).setText(settings.getRemoteDir());
		}
	}

	public void submitCredentials(View view) {
		CharSequence ip = ((TextView) findViewById(R.id.smbIpText)).getText();
		CharSequence username     = ((TextView) findViewById(R.id.smbNameText)).getText();
		CharSequence password = ((TextView) findViewById(R.id.smbPasswordText)).getText();
		CharSequence localDir      = ((TextView) findViewById(R.id.smbLocalDirText)).getText();
		CharSequence remoteDir     = ((TextView) findViewById(R.id.smbRemoteDirText)).getText();

		try {
			new SettingsAccess().writeSettings(ip.toString(), username.toString(), password.toString(),
					localDir.toString(), remoteDir.toString(), getApplicationContext());
		} catch (MissingSettingsException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.errorEmptySmbParameters).setTitle(R.string.error);
			AlertDialog dialog = builder.create();
			dialog.show();
			Logger.logException(this, e);
		} catch (SettingsAccessException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Logger.logException(this, e);
		}

		Toast.makeText(getApplicationContext(), R.string.store_credentials_success, Toast.LENGTH_SHORT).show();
		openShuffleListActivity(true);
	}

	private void openShuffleListActivity(final boolean settingsChanged) {
		Intent intent = new Intent(this, ShuffleListActivity.class);
		intent.putExtra(SETTINGS_CHANGED_FLAG, settingsChanged);
		startActivity(intent);
	}

	public void cancel(View view) {
		Toast.makeText(getApplicationContext(), R.string.bye, Toast.LENGTH_SHORT).show();
		openShuffleListActivity(false);
	}

	@Override
	public void onBackPressed() {
		openShuffleListActivity(false);
	}
}
