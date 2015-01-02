package net.npike.android.logwrap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;


public class LogWrap {
	private static boolean SHOULD_LOG = BuildConfig.DEBUG;
	private static ArrayList<String> mUnloggables = new ArrayList<String>();


    public static void setShouldLog(boolean shouldLog) {
        SHOULD_LOG = shouldLog;
    }

	public static void setLoggable(boolean loggable) {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		if (!loggable) {
			mUnloggables.add(stackTrace[1].getFileName());
		} else {
			mUnloggables.remove(stackTrace[1].getFileName());
		}
	}


	public static void d(String... args) {
		if (SHOULD_LOG) {
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String methodName = stackTrace[1].getMethodName();

			if (!mUnloggables.contains(stackTrace[1].getFileName())) {

                StringBuilder builder = new StringBuilder();
                for (String string : args) {
                    builder.append("[" + methodName + "] ");
                    builder.append(string);
                    builder.append("\n");
                }

                if (builder.length() == 0) {
                    builder.append("[" + methodName + "] ");
                }

				Log.d(stackTrace[1].getFileName(), builder.toString());
			}
		}
	}

    // TODO really should extract the logic and share between the log levels.
    public static void e(String... args) {
        if (SHOULD_LOG) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String methodName = stackTrace[1].getMethodName();

            if (!mUnloggables.contains(stackTrace[1].getFileName())) {

                StringBuilder builder = new StringBuilder();
                for (String string : args) {
                    builder.append("[" + methodName + "] ");
                    builder.append(string);
                    builder.append("\n");
                }

                if (builder.length() == 0) {
                    builder.append("[" + methodName + "] ");
                }

                Log.e(stackTrace[1].getFileName(), builder.toString());
            }
        }
    }

	public static void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (SHOULD_LOG) {
			StringBuilder builder = new StringBuilder();
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            new Throwable().printStackTrace();

			if (!mUnloggables.contains(stackTrace[1].getFileName())) {

				String resultCodeSting = "RESULT_OKAY";

				switch (resultCode) {
				case Activity.RESULT_CANCELED:
					resultCodeSting = "RESULT_CANCELED";
					break;
				case Activity.RESULT_FIRST_USER:
					resultCodeSting = "RESULT_FIRST_USER";
					break;
				case Activity.RESULT_OK:
				default:
					resultCodeSting = "RESULT_OKAY";
				}

				builder.insert(0, "[" + stackTrace[1].getMethodName() + "] ");
				builder.append("requestCode=" + requestCode + ", " + resultCode
						+ "=" + resultCode + "(" + resultCodeSting + ")");

				Log.d(stackTrace[1].getFileName(), builder.toString());
			}
		}
	}

	public static void w(String... args) {
		if (SHOULD_LOG) {
			StringBuilder builder = processVarArgs(args);
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();

			if (!mUnloggables.contains(stackTrace[1].getFileName())) {

				builder.insert(0, "[" + stackTrace[1].getMethodName() + "] ");

				Log.w(stackTrace[1].getFileName(), builder.toString());
			}
		}
	}

	private static StringBuilder processVarArgs(String... args) {
		StringBuilder builder = new StringBuilder();
		for (String string : args) {
			builder.append(string);
			builder.append("\n");
		}
		return builder;
	}
}
