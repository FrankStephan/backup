package com.frozen_foo.shuffle_my_music_app;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Frank on 15.11.2017.
 */

public class Logger {

	static File LOG;

	private static void initLog(final Context context) {
		if (null == LOG) {
			File localDir = new LocalDirectoryAccess().localDir(context);;
			LOG = new File(localDir, "LOG.txt");
			try {
				LOG.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void appendToLog(final String logStatement) {
		Log.i(Logger.class.getSimpleName(), logStatement);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LOG, true))) {
			String dateString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(new Date());
			bufferedWriter.write("[" + dateString + "] " + logStatement);
			bufferedWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void logEvent(Context context, KeyEvent keyEvent, boolean consumed) {
		initLog(context);
		String logStatement = toString(keyEvent, consumed);
		appendToLog(logStatement);
	}

	static String toString(KeyEvent keyEvent, boolean consumed) {

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

		return buffer.toString();
	}

	private static String actionToString(int action) {
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

	public static synchronized void logException(Context context, Exception e) {
		initLog(context);
		e.printStackTrace();
		appendToLog(e.getMessage());
	}

	public static synchronized void log(Context context, String logMessage) {
		initLog(context);
		appendToLog(logMessage);
	}

	/**
	 * Created by Frank on 04.01.2018.
	 */

	public static class VolumeMaxController {


	}
}
