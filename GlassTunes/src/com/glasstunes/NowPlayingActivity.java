package com.glasstunes;

import com.glasstunes.cards.NowPlayingCard;
import com.glasstunes.screenslide.BasicCardPagerAdapter;
import com.glasstunes.screenslide.ScreenSlideActivity;
import com.glasstunes.screenslide.ScreenSlidePagerAdapter;

public class NowPlayingActivity extends ScreenSlideActivity {

	@Override
	protected ScreenSlidePagerAdapter onCreatePagerAdapter() {
		BasicCardPagerAdapter adapter = new BasicCardPagerAdapter(
				getFragmentManager());
		adapter.setCards(NowPlayingCard.newInstance());
		return adapter;
	}

}
