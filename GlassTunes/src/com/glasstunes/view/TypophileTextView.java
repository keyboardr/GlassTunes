package com.glasstunes.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.glasstunes.R;

public class TypophileTextView extends TextView {
	private static final int MASK_CONDENSED = 256;
	private static final int MASK_ITALIC = 16;
	private static final int MASK_WEIGHT = 15;

	public TypophileTextView(Context context) {
		this(context, null);
	}

	public TypophileTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TypophileTextView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);

		boolean condensed = true;

		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.TypophileTextView); // v0

		if (!attributes.hasValue(0)) {
			attributes.recycle();
			return;
		}

		int fontWeight = attributes.getInt(0, 0); // v2
		int weight = fontWeight & MASK_WEIGHT; // v5

		boolean italic; // v3
		if ((fontWeight & MASK_ITALIC) == 0) {
			italic = true;
		} else {
			italic = false;
		}

		if ((fontWeight & MASK_CONDENSED) == 0) {
			condensed = false;
		}

		Typeface typeface = RobotoTypefaces.getTypeface(context, weight,
				italic, condensed); // v4
		setTypeface(typeface);
	}
}