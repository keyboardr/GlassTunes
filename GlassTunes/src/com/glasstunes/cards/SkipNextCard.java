package com.glasstunes.cards;

import android.app.Activity;
import android.os.Bundle;

import com.glasstunes.R;
import com.glasstunes.controller.MusicController;
import com.glasstunes.screenslide.BasicCardFragment;

public class SkipNextCard extends BasicCardFragment {

	public static SkipNextCard newInstance() {
		SkipNextCard frag = new SkipNextCard();
		Bundle args = BasicCardFragment.generateArgs(R.drawable.ic_av_next,
				R.string.av_next);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onSelect() {
		super.onSelect();
		MusicController.getInstance().next(getActivity());
		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
	}
}
