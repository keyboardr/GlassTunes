package com.glasstunes.cards;

import android.app.Activity;
import android.os.Bundle;

import com.glasstunes.R;
import com.glasstunes.controller.MusicController;
import com.glasstunes.screenslide.BasicCardFragment;

public class SkipPreviousCard extends BasicCardFragment {
	public static SkipPreviousCard newInstance() {
		SkipPreviousCard frag = new SkipPreviousCard();
		Bundle args = BasicCardFragment.generateArgs(R.drawable.ic_av_previous,
				R.string.av_previous);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onSelect() {
		super.onSelect();
		MusicController.getInstance().previous(getActivity());
		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
	}
}
