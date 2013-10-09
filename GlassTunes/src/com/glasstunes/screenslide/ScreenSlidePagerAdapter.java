package com.glasstunes.screenslide;

import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public abstract class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public abstract void onSelect(int position);
}