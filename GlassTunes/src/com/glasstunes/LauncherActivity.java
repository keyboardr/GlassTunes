package com.glasstunes;

import android.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.net.Uri;

import com.glasstunes.cards.BrowseCard;
import com.glasstunes.screenslide.BasicCardFragment;

public class LauncherActivity extends BaseContentCardActivity implements
		LoaderCallbacks<Cursor> {

	@Override
	protected Uri getContentUri() {
		return Uri.parse("content://com.google.android.music.xdi/launcher");
	}

	@Override
	protected BasicCardFragment getCardFromCursor(Cursor cursor) {
		return BrowseCard.newInstance(
				cursor.getString(cursor.getColumnIndex("display_name")),
				cursor.getString(cursor.getColumnIndex("intent_uri")));
	}
}
