package com.glasstunes.cards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.glasstunes.HeaderActivity;
import com.glasstunes.R;
import com.glasstunes.screenslide.BasicCardFragment;

public class HeaderCard extends BasicCardFragment {
	public static HeaderCard newInstance() {
		HeaderCard frag = new HeaderCard();
		Bundle args = BasicCardFragment.generateArgs(
				R.drawable.ic_musicplayer_radio, R.string.av_browse);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onSelect() {
		super.onSelect();
		Intent intent = new Intent(getActivity(), HeaderActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity().setResult(resultCode);
		if (resultCode == Activity.RESULT_OK) {
			getActivity().finish();
		}
	}
}
