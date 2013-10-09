package com.glasstunes;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import com.glasstunes.cards.ContentCard;
import com.glasstunes.screenslide.CardFragment;

public class BrowseActivity extends BaseContentCardActivity {

	private static final String START_INDEX = "start_index";

	@Override
	protected Uri getContentUri() {
		if (getIntent().hasExtra(START_INDEX)) {
			return ContentUris.withAppendedId(getIntent().getData(),
					getIntent().getIntExtra(START_INDEX, 0) + 1);
		} else {
			return getIntent().getData();
		}
	}

	@Override
	protected CardFragment getCardFromCursor(Cursor cursor) {
		return ContentCard.newInstance(
				cursor.getString(cursor.getColumnIndex("image_uri")),
				cursor.getString(cursor.getColumnIndex("display_name")),
				cursor.getString(cursor.getColumnIndex("display_description")),
				cursor.getString(cursor.getColumnIndex("intent_uri")));
	}

}
