package com.glasstunes.cards;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.glasstunes.screenslide.BasicCardFragment;

public class BrowseCard extends BasicCardFragment {
	protected static final String ARG_INTENT_URI = null;

	protected static Bundle generateArgs(String display_name, String intent_uri) {
		Bundle args = BasicCardFragment.generateArgs(0, display_name);
		args.putString(ARG_INTENT_URI, intent_uri);
		return args;
	}

	public static BrowseCard newInstance(String display_name, String intent_uri) {
		BrowseCard frag = new BrowseCard();
		Bundle args = generateArgs(display_name, intent_uri);
		frag.setArguments(args);
		return frag;
	}

	protected Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = savedInstanceState == null ? getArguments()
				: savedInstanceState;
		try {
			mIntent = Intent.parseUri(args.getString(ARG_INTENT_URI), 0);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(ARG_INTENT_URI,
				mIntent.toUri(Intent.URI_INTENT_SCHEME));
	}

	@Override
	public void onSelect() {
		super.onSelect();
		startActivityForResult(mIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity().setResult(resultCode);
		if (resultCode == Activity.RESULT_OK) {
			getActivity().finish();
		}
	}
}
