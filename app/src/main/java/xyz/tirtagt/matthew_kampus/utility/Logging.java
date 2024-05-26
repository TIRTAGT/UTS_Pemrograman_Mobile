package xyz.tirtagt.matthew_kampus.utility;

import android.util.Log;

public class Logging {
	private static boolean ENABLE_WRITELN = false;
	private static boolean ENABLE_BUILTIN_LOG = true;
	public static final int ASSERT = 7;
	public static final int DEBUG = 3;
	public static final int ERROR = 6;
	public static final int INFO = 4;
	public static final int VERBOSE = 2;
	public static final int WARN = 5;

	private static void WriteLog(int priority, String tag, String msg) {
		if (ENABLE_WRITELN) {
			switch (priority) {
				case ASSERT:
					tag = "T/" + tag + ": ";
					break;
				case DEBUG:
					tag = "D/" + tag + ": ";
					break;
				case ERROR:
					tag = "E/" + tag + ": ";
					break;
				case INFO:
					tag = "I/" + tag + ": ";
					break;
				case VERBOSE:
					tag = "V/" + tag + ": ";
					break;
				case WARN:
					tag = "W/" + tag + ": ";
					break;
			}

			System.out.println(tag + msg);
		}

		if (ENABLE_BUILTIN_LOG) {
			Log.println(priority, tag, msg);
		}
	}

	public static void verbose(String tag, String msg) {
		WriteLog(VERBOSE, tag, msg);
	}

	public static void v(String tag, String msg) {
		verbose(tag, msg);
	}

	public static void debug(String tag, String msg) {
		WriteLog(DEBUG, tag, msg);
	}

	public static void d(String tag, String msg) {
		debug(tag, msg);
	}

	public static void info(String tag, String msg) {
		WriteLog(INFO, tag, msg);
	}

	public static void i(String tag, String msg) {
		info(tag, msg);
	}

	public static void warn(String tag, String msg) {
		WriteLog(WARN, tag, msg);
	}

	public static void w(String tag, String msg) {
		warn(tag, msg);
	}

	public static void error(String tag, String msg) {
		WriteLog(ERROR, tag, msg);
	}

	public static void e(String tag, String msg) {
		error(tag, msg);
	}

	public static void WhatATerribleFailure(String tag, String msg) {
		WriteLog(ERROR, tag, msg);
	}

	public static void wtf(String tag, String msg) {
		WhatATerribleFailure(tag, msg);
	}
}
