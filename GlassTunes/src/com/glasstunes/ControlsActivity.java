package com.glasstunes;

import com.glasstunes.cards.HeaderCard;
import com.glasstunes.cards.PausePlayCard;
import com.glasstunes.cards.SkipNextCard;
import com.glasstunes.cards.SkipPreviousCard;
import com.glasstunes.screenslide.BasicCardPagerAdapter;
import com.glasstunes.screenslide.ScreenSlideActivity;
import com.glasstunes.screenslide.ScreenSlidePagerAdapter;

public class ControlsActivity extends ScreenSlideActivity {

	@Override
	protected ScreenSlidePagerAdapter onCreatePagerAdapter() {
		BasicCardPagerAdapter adapter = new BasicCardPagerAdapter(
				getFragmentManager());
		adapter.setCards(SkipPreviousCard.newInstance(),
				PausePlayCard.newInstance(), SkipNextCard.newInstance(),
				HeaderCard.newInstance());
		setCurrentItem(1);
		return adapter;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

}
