package com.glasstunes.screenslide;

import android.app.Fragment;
import android.app.FragmentManager;

public class BasicCardPagerAdapter extends ScreenSlidePagerAdapter {

	private CardFragment[] mCards;

	public BasicCardPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setCards(CardFragment... cards) {
		mCards = cards;
		notifyDataSetChanged();
	}

	@Override
	public void onSelect(int position) {
		mCards[position].onSelect();
	}

	@Override
	public Fragment getItem(int position) {
		return mCards == null ? null : mCards[position];
	}

	@Override
	public int getCount() {
		return mCards == null ? 0 : mCards.length;
	}

}
