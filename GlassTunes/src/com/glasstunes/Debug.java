package com.glasstunes;


public class Debug {
	public static final boolean LOG_CONTENT = BuildConfig.DEBUG && true;

	private Debug() {
		// no instances
	}

	public static String buildConcatString(String delimeter, String... strings) {
		StringBuilder builder = new StringBuilder();
		boolean isFirst = true;
		for (String string : strings) {
			if (string == null) {
				string = "NULL";
			}
			if (!isFirst) {
				builder.append(delimeter);
			}
			isFirst = false;
			builder.append(string);
		}
		return builder.toString();
	}
}
