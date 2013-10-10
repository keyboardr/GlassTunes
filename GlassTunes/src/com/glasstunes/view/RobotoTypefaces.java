package com.glasstunes.view;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

public class RobotoTypefaces {
	private static final String ASSETS_DIRECTORY = "fonts/";
	private static final String FONTS_DIRECTORY = "/system/glass_fonts";
	public static final int WEIGHT_BLACK = 5;
	public static final int WEIGHT_BOLD = 4;
	public static final int WEIGHT_LIGHT = 2;
	public static final int WEIGHT_MEDIUM = 3;
	public static final int WEIGHT_REGULAR = 0;
	public static final int WEIGHT_THIN = 1;
	private static final HashMap<TypefaceKey, Typeface> typefaces = new HashMap<TypefaceKey, Typeface>();

	private static String getTtfFileName(TypefaceKey key) {
		if ((key.condensed) && (key.weight != WEIGHT_REGULAR)
				&& (key.weight != WEIGHT_BOLD)) {
			throw new IllegalArgumentException(
					"Only regular (default) or bold can be combined with condensed.");
		}
		switch (key.weight) {
		default:
			if (key.condensed) {
				if (key.italic) {
					return "Roboto-CondensedItalic.ttf";
				}
				return "Roboto-Condensed.ttf";
			}
			if (key.italic) {
				return "Roboto-Italic.ttf";
			}
			return "Roboto-Regular.ttf";
		case WEIGHT_THIN:
			if (key.italic) {
				return "Roboto-ThinItalic.ttf";
			}
			return "Roboto-Thin.ttf";
		case WEIGHT_LIGHT:
			if (key.italic) {
				return "Roboto-LightItalic.ttf";
			}
			return "Roboto-Light.ttf";
		case WEIGHT_MEDIUM:
			if (key.italic) {
				return "Roboto-MediumItalic.ttf";
			}
			return "Roboto-Medium.ttf";
		case WEIGHT_BOLD:
			if (key.condensed) {
				if (key.italic) {
					return "Roboto-BoldCondensedItalic.ttf";
				}
				return "Roboto-BoldCondensed.ttf";
			}
			if (key.italic) {
				return "Roboto-BoldItalic.ttf";
			}
			return "Roboto-Bold.ttf";
		case WEIGHT_BLACK:
		}
		if (key.italic) {
			return "Roboto-BlackItalic.ttf";
		}
		return "Roboto-Black.ttf";
	}

	public static Typeface getTypeface(Context context, int weight) {
		return getTypeface(context, weight, false, false);
	}

	public static Typeface getTypeface(Context context, int weight,
			boolean italic, boolean condensed) {
		TypefaceKey localTypefaceKey = new TypefaceKey(weight, italic,
				condensed);
		synchronized (typefaces) {
			Typeface localTypeface = typefaces.get(localTypefaceKey);
			if (localTypeface == null) {
				localTypeface = loadTypeface(context, localTypefaceKey);
			}
			return localTypeface;
		}
	}

	private static Typeface loadTypeface(Context context, TypefaceKey key) {
		String tftFileName = getTtfFileName(key);
		File fontFile = new File(FONTS_DIRECTORY, tftFileName);
		Typeface typeface = null;

		if (fontFile.exists()) {
			typeface = Typeface.createFromFile(fontFile);
		} else {
			if (context != null) {
				typeface = Typeface.createFromAsset(context.getAssets(),
						ASSETS_DIRECTORY + tftFileName);
			}
		}

		if (typeface != null) {
			typefaces.put(key, typeface);
		}
		return typeface;
	}

	public static void warmCache(final Context context) {
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				RobotoTypefaces.getTypeface(context, WEIGHT_THIN);
			}
		});
	}

	private static final class TypefaceKey {
		final boolean condensed;
		final boolean italic;
		final int weight;

		TypefaceKey(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
			this.weight = paramInt;
			this.italic = paramBoolean1;
			this.condensed = paramBoolean2;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (condensed ? 1231 : 1237);
			result = prime * result + (italic ? 1231 : 1237);
			result = prime * result + weight;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			TypefaceKey other = (TypefaceKey) obj;
			if (condensed != other.condensed) {
				return false;
			}
			if (italic != other.italic) {
				return false;
			}
			if (weight != other.weight) {
				return false;
			}
			return true;
		}
	}
}