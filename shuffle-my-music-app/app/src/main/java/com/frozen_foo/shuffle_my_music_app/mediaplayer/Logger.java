package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.view.KeyEvent;

import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Frank on 15.11.2017.
 */

public class Logger {

	static File LOG;

	public static void logEvent(Context context, KeyEvent keyEvent, boolean consumed) {
		if (null == LOG) {
			File localDir = null;
			try {
				localDir = new LocalDirectoryAccess().localDir(context);
			} catch (SettingsAccessException e) {
				e.printStackTrace();
			}
			LOG = new File(localDir, "LOG.txt");
			try {
				LOG.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LOG, true))) {
			bufferedWriter.write(toString(keyEvent, consumed));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String toString(KeyEvent keyEvent, boolean consumed) {
		synchronized (Logger.class) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(DateFormat.getTimeInstance().format(new Date(keyEvent.getEventTime())));
			buffer.append(", ");

			buffer.append("action:");
			buffer.append(actionToString(keyEvent.getAction()));
			buffer.append(", ");

			buffer.append("device");
			buffer.append(keyEvent.getDevice().getName());
			buffer.append(", ");

			buffer.append("source:");
			buffer.append(keyEvent.getSource());
			buffer.append(", ");

			buffer.append("keyCode:");
			buffer.append(KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
			buffer.append(", ");

			buffer.append("consumed:");
			buffer.append(consumed);
			buffer.append(System.lineSeparator());

			return buffer.toString();
		}
	}

	public static String actionToString(int action) {
		switch (action) {
			case KeyEvent.ACTION_DOWN:
				return "ACTION_DOWN";
			case KeyEvent.ACTION_UP:
				return "ACTION_UP";
			case KeyEvent.ACTION_MULTIPLE:
				return "ACTION_MULTIPLE";
			default:
				return Integer.toString(action);
		}
	}
}
