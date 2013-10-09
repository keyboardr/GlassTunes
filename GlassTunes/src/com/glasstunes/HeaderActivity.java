package com.glasstunes;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.glasstunes.cards.BrowseCard;
import com.glasstunes.screenslide.CardFragment;

public class HeaderActivity extends BaseContentCardActivity {

	@Override
	protected Uri getContentUri() {
		return Uri
				.parse("content://com.google.android.music.xdi/browse/headers");
	}

	@Override
	protected CardFragment getCardFromCursor(Cursor cursor) {
		Intent intent = new Intent("com.google.android.xdi.action.BROWSE");
		intent.setData(ContentUris.withAppendedId(
				Uri.parse("content://com.google.android.music.xdi/browse"),
				cursor.getLong(cursor.getColumnIndex("_id"))));
		return BrowseCard.newInstance(
				cursor.getString(cursor.getColumnIndex("display_name")),
				intent.toUri(Intent.URI_INTENT_SCHEME));
	}

	@Override
	protected int getNumSkippedCards() {
		return 1;
	}

	@Override
	protected int getLimitCards() {
		return 5;
	}
}
