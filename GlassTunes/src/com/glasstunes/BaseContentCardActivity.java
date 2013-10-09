package com.glasstunes;

import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ViewAnimator;

import com.glasstunes.screenslide.BasicCardPagerAdapter;
import com.glasstunes.screenslide.CardFragment;
import com.glasstunes.screenslide.ScreenSlideActivity;
import com.glasstunes.screenslide.ScreenSlidePagerAdapter;

public abstract class BaseContentCardActivity extends ScreenSlideActivity
		implements LoaderCallbacks<Cursor> {
	protected BasicCardPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
	}

	@Override
	protected ScreenSlidePagerAdapter onCreatePagerAdapter() {
		mAdapter = new BasicCardPagerAdapter(getFragmentManager());
		getLoaderManager().initLoader(0, null, this);
		return mAdapter;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		getSwitcher().setDisplayedChild(1);
		if (Debug.LOG_CONTENT) {
			Log.d("Uri", getContentUri().toString());
		}
		return new CursorLoader(this, getContentUri(), null, null, null, null);
	}

	protected abstract Uri getContentUri();

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		List<CardFragment> cards = new ArrayList<CardFragment>();
		if (cursor != null && cursor.moveToFirst()) {
			if (Debug.LOG_CONTENT) {
				Log.v("Cursor Count", "" + cursor.getCount());
				List<String> columns = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					columns.add(cursor.getColumnName(i));
				}
				Log.v("Column Names",
						Debug.buildConcatString(", ",
								columns.toArray(new String[columns.size()])));
			}
			do {
				if (Debug.LOG_CONTENT) {
					List<String> row = new ArrayList<String>();
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						row.add(cursor.getString(i));
					}

					Log.v("Row " + cursor.getPosition(),
							Debug.buildConcatString(", ",
									row.toArray(new String[row.size()])));
				}
				if (cursor.getPosition() >= getNumSkippedCards()
						&& (getLimitCards() == -1 || cards.size() < getLimitCards())) {
					cards.add(getCardFromCursor(cursor));
				}
			} while (cursor.moveToNext());
			getSwitcher().setDisplayedChild(0);
		} else {
			getSwitcher().setDisplayedChild(2);
		}
		mAdapter.setCards(cards.toArray(new CardFragment[cards.size()]));
	}

	protected ViewAnimator getSwitcher() {
		return (ViewAnimator) findViewById(R.id.switcher);
	}

	protected abstract CardFragment getCardFromCursor(Cursor cursor);

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.setCards();
	}

	@Override
	public void select() {
		if (getSwitcher().getDisplayedChild() == 0) {
			super.select();
		}
	}

	protected int getNumSkippedCards() {
		return 0;
	}

	protected int getLimitCards() {
		return -1;
	}
}
